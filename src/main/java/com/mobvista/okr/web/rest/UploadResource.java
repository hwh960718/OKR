package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.constants.AwsConstants;
import com.mobvista.okr.dto.AWSResponseDTO;
import com.mobvista.okr.util.aws.AWS3ClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * 上传头像
 *
 * @author guwei
 */
@RestController
@RequestMapping("/api/u/upload")
public class UploadResource {

    private final Logger log = LoggerFactory.getLogger(UploadResource.class);


    /**
     * 上传头像
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(path = "/uploadFace")
    public CommonResult uploadFace(@RequestParam("file") MultipartFile file) throws IOException {
        AWSResponseDTO awsResponseDTO = AWS3ClientUtils.uploadFileToBucket(AwsConstants.PICTURE_PATH_USER, file.getInputStream(), file.getOriginalFilename());
        return CommonResult.success(awsResponseDTO);
    }
}
