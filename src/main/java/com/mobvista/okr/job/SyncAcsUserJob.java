package com.mobvista.okr.job;

import com.alibaba.fastjson.JSON;
import com.mobvista.okr.domain.Department;
import com.mobvista.okr.domain.UserProfile;
import com.mobvista.okr.dto.mbacs.AccountUserProfile;
import com.mobvista.okr.enums.AcsUserStatus;
import com.mobvista.okr.enums.UserStatus;
import com.mobvista.okr.repository.DepartmentRepository;
import com.mobvista.okr.repository.UserProfileRepository;
import com.mobvista.okr.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 同步acs系统用户信息
 * cron = "0 0 1 * * ?"
 */
@Component
public class SyncAcsUserJob {
    private final Logger log = LoggerFactory.getLogger(SyncAcsUserJob.class);


    @Resource
    private DepartmentRepository departmentRepository;


    @Resource
    private UserProfileRepository userProfileRepository;

    @Resource
    private UserService userService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void execute() {
        log.info("同步acs系统用户信息 start");
        syncUser();
        log.info("同步acs系统用户信息 end");
    }


    /**
     * 同步用户数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    void syncUser() {
        List<Department> departmentList = departmentRepository.findAll();

        List<UserProfile> allUserList = userProfileRepository.findAll();
        Map<Long, Long> userProfileIdMap = new HashMap<>();
        Map<String, UserProfile> userMap = new HashMap<>();
        for (UserProfile userProfile : allUserList) {
            userMap.put(userProfile.getEmail(), userProfile);
            userProfileIdMap.put(userProfile.getId(), userProfile.getId());
        }

        List<Long> departmentIdList = new ArrayList<>();
        for (Department department : departmentList) {
            departmentIdList.add(department.getId());
        }

        List<AccountUserProfile> allProfileResultList = userService.getAllAccountProfiles(departmentIdList);

        List<UserProfile> insertList = new ArrayList<>();
        List<UserProfile> updateList = new ArrayList<>();

        for (AccountUserProfile profilesResult : allProfileResultList) {
            //acs用户信息实体转换
            UserProfile userProfile = new UserProfile();
            if (userMap.containsKey(profilesResult.getEmail())) {
                userProfile = userMap.get(profilesResult.getEmail());
            }
            userProfile.setUserName(profilesResult.getUsername());
            userProfile.setEmail(profilesResult.getEmail());
            userProfile.setRealName(profilesResult.getRealName());
            long userId = profilesResult.getId();
            userProfile.setId(userId);
            //设置用户状态
            int status = profilesResult.getStatus();
            if (AcsUserStatus.NORMAL.getValue().equals(status)) {
                userProfile.setStatus(UserStatus.NORMAL.getCode());
            } else if (AcsUserStatus.LOCK.getValue().equals(status)) {
                //userProfile.setStatus(UserStatus.NO_ACTIVATED.getCode());
                userProfile.setStatus(UserStatus.NORMAL.getCode());
            } else if (AcsUserStatus.NOT_ACTIVE.getValue().equals(status)) {
                //userProfile.setStatus(UserStatus.NO_ACTIVATED.getCode());
                userProfile.setStatus(UserStatus.NORMAL.getCode());
            } else if (AcsUserStatus.INVALID.getValue().equals(status)) {
                userProfile.setStatus(UserStatus.INVALID.getCode());
            }
            //插入用户部门信息
            userProfile.setDepartmentId(UserService.getDepartmentIdFromJson(String.valueOf(profilesResult.getDepartmentDetail())));
            //职位
            userProfile.setJobName(UserService.makeJsonName(String.valueOf(profilesResult.getJobDetail())));
            if (profilesResult.getEntryTime() > 0) {
                userProfile.setEntryTime(new Date(profilesResult.getEntryTime()));
            }
            if (profilesResult.getLeavingTime() > 0) {
                userProfile.setLeavingTime(new Date(profilesResult.getLeavingTime()));
            }

            //设置直属上级
            String leaderJson = String.valueOf(profilesResult.getDirectLeaderDetail());
            if (StringUtils.isNotBlank(leaderJson)) {
                try {
                    Map.Entry entry = JSON.parseObject(leaderJson, Map.Entry.class);
                    userProfile.setLeaderId(Long.valueOf(String.valueOf(entry.getKey())));
                } catch (Exception e) {
                    log.error("设置直属上级异常，当前同步数据直属上级信息：{}，异常信息：{}", leaderJson, e.getMessage());
                }
            }

            if (null != userProfileIdMap.get(userId)) {
                updateList.add(userProfile);
            } else {
                insertList.add(userProfile);
            }
        }
        if (updateList.size() > 0) {
            userProfileRepository.updateList(updateList);
        }

        if (insertList.size() > 0) {
            userProfileRepository.saveList(insertList);
        }
        //同步新的用户数据库，移除当前所有使用OKR系统用户的邮箱缓存
        userService.removeAllOkrUserEmailRedisData();
    }
}
