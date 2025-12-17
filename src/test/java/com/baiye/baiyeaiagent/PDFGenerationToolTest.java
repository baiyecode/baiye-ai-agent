package com.baiye.baiyeaiagent;

import com.baiye.baiyeaiagent.tools.PDFGenerationTool;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class PDFGenerationToolTest {

    @Test
    public void testGeneratePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "白夜的项目.pdf";
        String content = "AI超级智能体项目 https://github.com/baiyecode/baiye-ai-agent";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}
