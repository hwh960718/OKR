/*
package com.mobvista.okr.service;

import com.mobvista.okr.config.QiniuConfig;
import com.mobvista.okr.dto.AWSResponseDTO;
import com.mobvista.okr.util.RandomUtil;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

*/
/**
 * Created by jiahuijie on 2017/4/21.
 *//*

@Component
public class QiniuService {

    @Resource
    private QiniuConfig qiniuConfig;

    @Resource
    private UploadManager uploadManager;

    @Resource
    private RestTemplate restTemplate;

    public AWSResponseDTO uploadFile(MultipartFile url, String module) throws IOException {
        Auth auth = Auth.create(qiniuConfig.getAk(), qiniuConfig.getSk());
        String token = auth.uploadToken(qiniuConfig.getBucket());
        String fileTypeName = url.getOriginalFilename().substring(url.getOriginalFilename().lastIndexOf("."));
        String fileName = module + RandomUtil.generateRandomFileName() + fileTypeName;
        uploadManager.put(url.getBytes(), fileName, token);
        return new AWSResponseDTO(fileName);
    }

    public AWSResponseDTO uploadFile(MultipartFile url, String module, String name) throws IOException {
        Auth auth = Auth.create(qiniuConfig.getAk(), qiniuConfig.getSk());
        String token = auth.uploadToken(qiniuConfig.getBucket());
        String fileTypeName = url.getOriginalFilename().substring(url.getOriginalFilename().lastIndexOf("."));
        String fileName = module + name + fileTypeName;
        uploadManager.put(url.getBytes(), fileName, token);
        return new AWSResponseDTO(fileName);
    }

    public AWSResponseDTO uploadFile(File url, String module) throws IOException {
        Auth auth = Auth.create(qiniuConfig.getAk(), qiniuConfig.getSk());
        String token = auth.uploadToken(qiniuConfig.getBucket());
        String fileTypeName = url.getName().substring(url.getName().lastIndexOf("."));
        String fileName = module + RandomUtil.generateRandomFileName() + fileTypeName;
        uploadManager.put(url, fileName, token);
        return new AWSResponseDTO(fileName);
    }

    public AWSResponseDTO uploadFile(InputStream inputStream, String module) throws IOException {
        Auth auth = Auth.create(qiniuConfig.getAk(), qiniuConfig.getSk());
        String token = auth.uploadToken(qiniuConfig.getBucket());
        String fileName = module + RandomUtil.generateRandomFileName();
        uploadManager.put(inputStream, fileName, token, null, null);
        return new AWSResponseDTO(fileName);
    }

    public AWSResponseDTO uploadFile(String url, String module) throws IOException {
        Auth auth = Auth.create(qiniuConfig.getAk(), qiniuConfig.getSk());
        String token = auth.uploadToken(qiniuConfig.getBucket());
        String fileName = module + RandomUtil.generateRandomFileName();

        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<byte[]>(headers),
                byte[].class);

        byte[] result = response.getBody();
        InputStream inputStream = new ByteArrayInputStream(result);
        uploadManager.put(inputStream, fileName, token, null, null);
        return new AWSResponseDTO(fileName);
    }
}
*/
