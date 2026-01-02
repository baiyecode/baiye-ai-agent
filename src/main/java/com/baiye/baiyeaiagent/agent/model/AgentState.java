package com.baiye.baiyeaiagent.agent.model;

/**
 * ClassName: AgentState
 * Package: com.baiye.baiyeaiagent.agent.model
 * Description: 代理执行状态的枚举类
 *
 * @Author 白夜
 * @Create 2025/12/30 14:37
 * @Version 1.0
 */
public enum AgentState {

    /**
     * 空闲状态
     */
    IDLE,

    /**
     * 运行中状态
     */
    RUNNING,

    /**
     * 已完成状态
     */
    FINISHED,

    /**
     * 错误状态
     */
    ERROR
}

