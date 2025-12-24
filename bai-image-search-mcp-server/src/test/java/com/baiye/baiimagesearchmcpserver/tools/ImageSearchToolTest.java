package com.baiye.baiimagesearchmcpserver.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * ClassName: ImageSearchToolTest
 * Package: com.baiye.baiimagesearchmcpserver.tools
 * Description:
 *
 * @Author 白夜
 * @Create 2025/12/24 15:55
 * @Version 1.0
 */
@SpringBootTest
class ImageSearchToolTest {

    @Resource
    private ImageSearchTool imageSearchTool;

    @Test
    void searchImage() {
        String result = imageSearchTool.searchImage("computer");
        Assertions.assertNotNull(result);
    }
}

