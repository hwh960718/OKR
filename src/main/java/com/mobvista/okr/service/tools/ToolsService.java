package com.mobvista.okr.service.tools;

import com.google.common.collect.Lists;
import com.mobvista.okr.constants.AwsConstants;
import com.mobvista.okr.domain.UserProfile;
import com.mobvista.okr.dto.AWSResponseDTO;
import com.mobvista.okr.repository.UserProfileRepository;
import com.mobvista.okr.util.OkHttpUtil;
import com.mobvista.okr.util.aws.AWS3ClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 描述：系统工具处理
 *
 * @author Weier Gu (顾炜)
 * @date 2018/9/21 10:20
 */
@Service
public class ToolsService {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(ToolsService.class);


    @Resource
    private UserProfileRepository userProfileRepository;

    @Value("${qiniu.host}")
    private String qiNiuUrl;


    /**
     * 同步七牛云图片至亚马逊
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void synQiNiuPicture2Aws() {
        log.info("同步七牛云图片至亚马逊-----开始");
        List<UserProfile> allUser = userProfileRepository.findAll();

        List<UserProfile> modifyUserList = Lists.newArrayList();
        for (UserProfile userProfile : allUser) {
            String photo = userProfile.getProfilePhoto();
            if (StringUtils.isNotBlank(photo) && (photo.startsWith("userface") || photo.startsWith("default_faces"))) {
                try {
                    AWSResponseDTO awsResponseDTO = AWS3ClientUtils.uploadFileToBucket(AwsConstants.PICTURE_PATH_USER, OkHttpUtil.getInputStream(String.format("http://%s/", qiNiuUrl) + photo), photo);
                    userProfile.setProfilePhoto(awsResponseDTO.getKey());
                    log.info("同步七牛云图片至亚马逊-----替换图片awsResponseDTO.getKey()----" + awsResponseDTO.getKey());
                    log.info("同步七牛云图片至亚马逊-----替换图片 userProfile.getProfilePhoto()----" + userProfile.getProfilePhoto());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                modifyUserList.add(userProfile);
            }
        }
        log.info("同步七牛云图片至亚马逊-----同步数据：" + modifyUserList.size());
        if (modifyUserList.size() > 0) {
            userProfileRepository.updateListPicture(modifyUserList);
        }
        log.info("同步七牛云图片至亚马逊-----开始");
    }


    public void synDefaultPicture() {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        for (int i = 1; i <= 10; i++) {
            String photo = "default_faces/Fruit-" + decimalFormat.format(i) + ".png";
            try {
                AWS3ClientUtils.uploadFileToBucketByKey(AwsConstants.PICTURE_PATH_USER + photo, OkHttpUtil.getInputStream(String.format("http://%s/", qiNiuUrl) + photo), photo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
