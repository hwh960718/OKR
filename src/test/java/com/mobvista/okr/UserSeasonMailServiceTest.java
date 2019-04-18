package com.mobvista.okr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author 顾炜(GUWEI) 时间：2018/4/11 15:58
 */
@RunWith(SpringRunner.class)
//@SpringBootTest(classes = App.class)
public class UserSeasonMailServiceTest {

    @Test
    public void sendFeedBackEmail() throws IOException {


        // 读入 文件
        File file = new File("/Users/guwei/Pictures/3385df60ef277a38c9163da48e05d691.jpg");
        FileInputStream in_file = new FileInputStream(file);

        // 转 MultipartFile
        MultipartFile multi = new MockMultipartFile("模板.xls", in_file);
//        mailService.sendFeedBackEmail("测试邮件内容", multi);

//        String name = multi.getOriginalFilename();
//

    }

    public static void main(String[] args) throws IOException {
        // 读入 文件
        File file = new File("/Users/guwei/Pictures/3385df60ef277a38c9163da48e05d691.jpg");
        FileInputStream in_file = new FileInputStream(file);

        // 转 MultipartFile
        MultipartFile multi = new MockMultipartFile("模板.jpg", in_file);

        // 创建文件夹
        String dire = "1.jpg";
        File file_dire = new File(dire);
        if (!file_dire.exists()) {
            file_dire.createNewFile();
        }

        //写入文件
        multi.transferTo(file_dire);
    }
}