package com.baiye.baiyeaiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ClassName: FileBasedChatMemory
 * Package: com.baiye.baiyeaiagent.chatmemory
 * Description: 基于文件的对话记忆存储库，提供标准的对话记忆 CRUD 操作
 *
 * @Author 白夜
 * @Create 2025/11/27 20:54
 * @Version 1.0
 */
public class FileBasedChatMemory implements ChatMemoryRepository {
    private final String BASE_DIR;

    // 1. 锁池：用于确保同一个 conversationId 在同一时间只能被一个线程操作
    private final ConcurrentHashMap<String, Object> conversationLocks = new ConcurrentHashMap<>();

    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);//关闭自动注册
        //设置实例化策略
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    });

    //构造对象时，指定文件保存目录
    public FileBasedChatMemory(String dir) {
        this.BASE_DIR = dir;
        File baseDir = new File(dir);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }

    // 获取针对特定 conversationId 的锁对象
    private Object getLock(String conversationId) {
        // computeIfAbsent 保证原子性，如果 key 存在返回旧值，不存在放入新值并返回
        return conversationLocks.computeIfAbsent(conversationId, k -> new Object());
    }

    /**
     * 查找所有对话 ID
     * 列出基础目录下所有 .kryo 文件
     * 提取文件名（去除扩展名）作为对话 ID
     * 处理目录不存在的情况
     * @return
     */
    @Override
    public List<String> findConversationIds() {
        File dir = new File(BASE_DIR);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".kryo"));
        if (files == null) return List.of();

        return Arrays.stream(files)
                .map(File::getName)
                .map(name -> name.substring(0, name.lastIndexOf('.')))
                .collect(Collectors.toList());
    }

    /**
     * 查找指定对话 ID 的所有消息
     * @param conversationId
     * @return
     */
    @Override
    public List<Message> findByConversationId(String conversationId) {
        // 2. 加锁读取，防止读取到写入写了一半的文件
        synchronized (getLock(conversationId)) {
            return getOrCreateConversation(conversationId);
        }
    }

    /**
     * 保存指定对话 ID 的所有消息
     * @param conversationId
     * @param messages
     */
    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        // 3. 加锁写入，防止多个线程同时写坏文件
        synchronized (getLock(conversationId)) {
            saveConversation(conversationId, messages);
        }
    }

    /**
     * 删除指定对话 ID 的所有消息
     * @param conversationId
     */
    @Override
    public void deleteByConversationId(String conversationId) {
        synchronized (getLock(conversationId)) {
            File file = getConversationFile(conversationId);
            if (file.exists()) {
                file.delete();
            }
            // 可以在删除文件后清理锁，防止 Map 无限膨胀（可选优化）
            conversationLocks.remove(conversationId);
        }
    }


    /**
     * Java 的泛型在运行时会被类型擦除，但在编译时仍会进行类型检查,
     * 当编译器无法确定类型转换的安全性时，会产生警告。
     * readObject 方法返回 T 类型（泛型）
     * 由于类型擦除，编译器无法完全确定返回类型的安全性
     * 因此产生 "unchecked conversion" 警告
     * 由于使用了 Kryo 序列化，保存时是 List<Message>
     * 读取时也期望是 List<Message>
     * 类型转换是安全的（假设文件没有被外部修改）
     * // 抑制未检查的转换警告
     * @SuppressWarnings("unchecked") ,用于告诉编译器忽略特定的编译警告。
     */

    /**
     * 获取或创建指定对话 ID 的消息列表
     * @param conversationId
     * @return
     */
    @SuppressWarnings("unchecked")  //抑制未检查的转换警告
    private List<Message> getOrCreateConversation(String conversationId) {
        File file = getConversationFile(conversationId);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (Input input = new Input(new FileInputStream(file))) {
            return kryoThreadLocal.get().readObject(input, ArrayList.class);
        } catch (Exception e) {
            // 建议日志记录
            System.err.println("读取记忆文件失败: " + conversationId + ", " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 保存指定对话 ID 的消息列表
     * @param conversationId
     * @param messages
     */
    private void saveConversation(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            kryoThreadLocal.get().writeObject(output, messages);
        } catch (IOException e) {
            throw new RuntimeException("保存记忆失败", e);
        }
    }

    /**
     * 获取指定对话 ID 的文件
     * @param conversationId
     * @return
     */
    private File getConversationFile(String conversationId) {
        // 简单的安全过滤,路径安全：过滤特殊字符，防止路径遍历攻击
        //[^a-zA-Z0-9.-]：匹配除了字母、数字、句点、连字符之外的所有字符
        //将所有不符合安全规则的字符替换为下划线 _ ,保留安全的字符不变
        String safeId = conversationId.replaceAll("[^a-zA-Z0-9.-]", "_");
        return new File(BASE_DIR, safeId + ".kryo");
    }
}
