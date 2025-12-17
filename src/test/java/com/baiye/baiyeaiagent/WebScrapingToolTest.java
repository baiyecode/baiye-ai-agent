package com.baiye.baiyeaiagent;

import com.baiye.baiyeaiagent.tools.WebScrapingTool;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertNotNull;


public class WebScrapingToolTest {

    @Test
    public void testScrapeWebPage() {
        WebScrapingTool tool = new WebScrapingTool();
        String url = "https://mc.kurogames.com/";
        String result = tool.scrapeWebPage(url);
        assertNotNull(result);
    }
}
