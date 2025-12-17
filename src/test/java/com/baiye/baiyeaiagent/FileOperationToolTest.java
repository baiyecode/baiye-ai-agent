package com.baiye.baiyeaiagent;

import com.baiye.baiyeaiagent.tools.FileOperationTool;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileOperationToolTest {

    @Test
    public void testReadFile() {
        FileOperationTool tool = new FileOperationTool();
        String fileName = "白夜.txt";
        String result = tool.readFile(fileName);
        assertNotNull(result);
    }

    @Test
    public void testWriteFile() {
        FileOperationTool tool = new FileOperationTool();
        String fileName = "白夜.txt";
        String content = "白夜的AI Agent";
        String result = tool.writeFile(fileName, content);
        assertNotNull(result);
    }
}
