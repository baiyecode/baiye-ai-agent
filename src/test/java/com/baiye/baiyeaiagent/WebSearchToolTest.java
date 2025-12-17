package com.baiye.baiyeaiagent;

import com.baiye.baiyeaiagent.tools.WebSearchTool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class WebSearchToolTest {

    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Test
    public void testSearchWeb() {
        WebSearchTool tool = new WebSearchTool(searchApiKey);
        String query = "鸣潮";
        String result = tool.searchWeb(query);
        assertNotNull(result);
    }
}
