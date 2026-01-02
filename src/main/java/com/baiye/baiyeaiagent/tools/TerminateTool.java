package com.baiye.baiyeaiagent.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * 一个终止工具，让智能体可以自行决定任务结束，合理地中断。
 */
public class TerminateTool {
  
    @Tool(description = """  
            Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task.  
            "When you have finished all the tasks, call this tool to end the work.  
            """)  
    public String doTerminate() {  
        return "任务结束";  
    }


}
