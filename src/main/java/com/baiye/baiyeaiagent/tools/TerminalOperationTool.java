package com.baiye.baiyeaiagent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ClassName: TerminalOperationTool
 * Package: com.baiye.baiyeaiagent.tools
 * Description: 终端操作工具，它允许程序在运行它的操作系统上启动一个子进程并执行指定的命令，然后捕获并返回该命令的输出结果。
 *
 * @Author 白夜
 * @Create 2025/12/16 20:57
 * @Version 1.0
 */
public class TerminalOperationTool {

    @Tool(description = "Execute a command in the terminal")
    public String executeTerminalCommand(@ToolParam(description = "Command to execute in the terminal") String command) {
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
//            Process process = Runtime.getRuntime().exec(command);
            Process process = builder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            //等待由 builder.start() 启动的进程完全执行完毕。这是一个阻塞调用，直到子进程结束才会继续执行后续代码。它返回进程的退出码。
            int exitCode = process.waitFor();
            //通常，0 表示成功，非 0 值表示执行过程中出现了某种错误。
            if (exitCode != 0) {
                //如果命令失败，将一个错误信息追加到 output 的末尾，包含具体的退出码，以便用户了解失败的原因。
                output.append("Command execution failed with exit code: ").append(exitCode);
            }
        } catch (IOException | InterruptedException e) {
            output.append("Error executing command: ").append(e.getMessage());
        }
        return output.toString();
    }
}

