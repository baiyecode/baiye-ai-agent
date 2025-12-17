package com.baiye.baiyeaiagent;

import com.baiye.baiyeaiagent.tools.TerminalOperationTool;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class TerminalOperationToolTest {

    @Test
    public void testExecuteTerminalCommand() {
        TerminalOperationTool tool = new TerminalOperationTool();
        String command = "dir";
        String result = tool.executeTerminalCommand(command);
        assertNotNull(result);
    }
}
