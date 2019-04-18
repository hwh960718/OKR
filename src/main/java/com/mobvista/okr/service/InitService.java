package com.mobvista.okr.service;

import com.mobvista.okr.domain.UserProfile;
import com.mobvista.okr.job.SyncAcsDepartmentJob;
import com.mobvista.okr.job.SyncAcsUserJob;
import com.mobvista.okr.repository.UserProfileRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for init project data.
 */
@Service
public class InitService {

    private final Logger log = LoggerFactory.getLogger(InitService.class);

    @Resource
    private SyncAcsUserJob syncAcsUserJob;
    @Resource
    private SyncAcsDepartmentJob syncAcsDepartmentJob;
    @Resource
    private UserProfileRepository userProfileRepository;

    /**
     * 首次同步数据
     */
    public void initData() {
        log.info("系统首次初始化数据 start");
        syncAcsDepartmentJob.execute();
        syncAcsUserJob.execute();
        initUserRank();
        log.info("系统首次初始化数据 end");
    }

    /**
     * 初始化用户职级
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void initUserRank() {
        try {
            org.springframework.core.io.Resource resource = new ClassPathResource("config/data/init_user_rank.csv");
            EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(encodedResource.getInputStream(), "GB2312"));
            //获取所有用户
            List<UserProfile> allUser = userProfileRepository.findAll();
            //按邮箱将用户拆分
            Map<String, UserProfile> emailMap = new HashMap<>();
            //按真实姓名将用户拆分
            Map<String, UserProfile> realNameMap = new HashMap<>();
            for (UserProfile userProfile : allUser) {
                emailMap.put(userProfile.getEmail(), userProfile);
                realNameMap.put(userProfile.getRealName(), userProfile);
            }
            List<UserProfile> modifyUserList = new ArrayList<>();
            String str = null;
            int line = 0;
            while ((str = br.readLine()) != null) {
                line++;
                if (line == 1) {
                    continue;
                }
                String[] props = str.split(",");

//                String realname = props[1];
//                String depName = props[5];
//                String leaderName = props[9];
                String sex = props[12];
                String rank = props[19];

//                String collage = props[14];
//                String workplace = props[18];
//                String major = props[15]; //专业
//                String degree = props[13]; //学历
                String email = props[17];


                UserProfile userProfile = emailMap.get(email);
                if (userProfile == null) {
                    continue;
                }
//                userProfile.setCollege(collage);
//                userProfile.setMajor(major);
//                userProfile.setWorkplace(workplace);
                userProfile.setGender("男".equals(sex) ? (byte) 1 : (byte) 2);
//                if (StringUtils.isNotBlank(degree)) {
//                    userProfile.setDegree(degree);
//                }
                if (StringUtils.isNotBlank(rank)) {
                    userProfile.setRank(rank);
                }

//                UserProfile leaderUser = realNameMap.get(leaderName);
//                if (null != leaderUser) {
//                    userProfile.setLeaderId(leaderUser.getId());
//                }
                modifyUserList.add(userProfile);
            }
            //批量保存需要修改的用户信息
            userProfileRepository.updateList(modifyUserList);
            br.close();
        } catch (Exception e) {
            log.error("初始化用户职级数据错误", e);
            throw new RuntimeException(e);
        }
    }

}
