package com.mobvista.okr.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mobvista.okr.constants.RedisConstants;
import com.mobvista.okr.domain.Season;
import com.mobvista.okr.domain.UserSeason;
import com.mobvista.okr.domain.UserSeasonItem;
import com.mobvista.okr.dto.CurrentSeasonDTO;
import com.mobvista.okr.dto.SeasonDTO;
import com.mobvista.okr.enums.AssessStatus;
import com.mobvista.okr.enums.SeasonStatus;
import com.mobvista.okr.exception.ExceptionUtil;
import com.mobvista.okr.repository.SeasonRepository;
import com.mobvista.okr.repository.UserProfileRepository;
import com.mobvista.okr.repository.UserSeasonItemRepository;
import com.mobvista.okr.repository.UserSeasonRepository;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.util.DateUtil;
import com.mobvista.okr.vm.SeasonVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Season.
 *
 * @author Weier Gu (顾炜)
 */
@Service
public class SeasonService {

    private final Logger log = LoggerFactory.getLogger(SeasonService.class);

    @Resource
    private SeasonRepository seasonRepository;

    @Resource
    private UserSeasonRepository userSeasonRepository;

    @Resource
    private UserProfileRepository userProfileRepository;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private UserSeasonItemRepository userSeasonItemRepository;
    @Resource
    private RedisService redisService;

    /**
     * 创建季度考核
     *
     * @param seasonVM
     * @return
     */
    @Transactional
    public SeasonDTO create(SeasonVM seasonVM) {
        //补全结束时间
        completionEndTime(seasonVM);
        //校验时间是否合法
        checkSeasonDate(seasonVM);

        Season seasonDB = seasonRepository.findOneByTitle(seasonVM.getTitle());
        ExceptionUtil.checkState(seasonDB == null, "季度考核标题已存在");

        Season season = new Season();
        season.setTitle(seasonVM.getTitle());
        season.setStartTime(seasonVM.getStartTime());
        season.setEndTime(seasonVM.getEndTime());
        season.setFirstStageStartTime(seasonVM.getFirstStageStartTime());
        season.setFirstStageEndTime(seasonVM.getFirstStageEndTime());
        season.setSecondStageStartTime(seasonVM.getSecondStageStartTime());
        season.setSecondStageEndTime(seasonVM.getSecondStageEndTime());
        season.setStatus(SeasonStatus.NEW.getCode());
        season.setMakeAssessTask(false);
        season.setMakeSeasonScore(false);
        seasonRepository.insert(season);
        return new SeasonDTO(season);
    }

    /**
     * 校验时间是否合法
     *
     * @param seasonVM
     */
    private void checkSeasonDate(SeasonVM seasonVM) {
        ExceptionUtil.checkState(seasonVM.getStartTime().compareTo(seasonVM.getEndTime()) <= 0, "结束时间必须大于开始时间");
        ExceptionUtil.checkState(seasonVM.getFirstStageStartTime().compareTo(seasonVM.getFirstStageEndTime()) <= 0, "制定OKR结束时间必须大于开始时间");
        ExceptionUtil.checkState(seasonVM.getSecondStageStartTime().compareTo(seasonVM.getSecondStageEndTime()) <= 0, "给同事打分结束时间必须大于开始时间");

        ExceptionUtil.checkState(seasonVM.getFirstStageEndTime().after(new Date()), "制定OKR结束时间必须大于当前时间");

        ExceptionUtil.checkState(seasonVM.getFirstStageEndTime().compareTo(seasonVM.getSecondStageStartTime()) <= 0, "制定OKR时间必须在给同事打分时间之前");

        ExceptionUtil.checkState(seasonVM.getStartTime().compareTo(seasonVM.getFirstStageStartTime()) <= 0, "制定OKR起始时间必须大于考核开始时间");
        ExceptionUtil.checkState(seasonVM.getEndTime().compareTo(seasonVM.getFirstStageEndTime()) >= 0, "制定OKR结束时间必须小于考核结束时间");
        ExceptionUtil.checkState(seasonVM.getStartTime().compareTo(seasonVM.getSecondStageStartTime()) <= 0, "给同事打分起始时间必须大于考核开始时间");
        ExceptionUtil.checkState(seasonVM.getEndTime().compareTo(seasonVM.getSecondStageEndTime()) >= 0, "给同事打分结束时间必须小于考核结束时间");
    }


    /**
     * 补全结束时间
     *
     * @param seasonVM
     */
    private void completionEndTime(SeasonVM seasonVM) {
        seasonVM.setEndTime(DateUtil.endDate(seasonVM.getEndTime()));
        seasonVM.setFirstStageEndTime(DateUtil.endDate(seasonVM.getFirstStageEndTime()));
        seasonVM.setSecondStageEndTime(DateUtil.endDate(seasonVM.getSecondStageEndTime()));
    }

    /**
     * 删除季度考核数据
     *
     * @param id
     */
    @Transactional
    public void remove(Long id) {
        Season seasonDB = seasonRepository.selectByPrimaryKey(id);
        ExceptionUtil.checkState(seasonDB.getStatus().equals(SeasonStatus.NEW.getCode()), "只有新建的季度考核才能删除");
        seasonRepository.deleteByPrimaryKey(id);
    }

    /**
     * 修改季度考核数据
     *
     * @param seasonVM
     * @return
     */
    @Transactional
    public SeasonDTO update(SeasonVM seasonVM) {
        Season seasonDB = seasonRepository.selectByPrimaryKey(seasonVM.getId());
        ExceptionUtil.checkState(seasonDB.getStatus().equals(SeasonStatus.NEW.getCode()), "只有新建的的季度考核才能修改");
        //补全结束时间
        completionEndTime(seasonVM);
        //校验时间是否合法
        checkSeasonDate(seasonVM);

        seasonDB.setTitle(seasonVM.getTitle());
        seasonDB.setStartTime(seasonVM.getStartTime());
        seasonDB.setEndTime(DateUtil.setDateEnd(seasonVM.getEndTime()));
        seasonDB.setFirstStageStartTime(seasonVM.getFirstStageStartTime());
        seasonDB.setFirstStageEndTime(DateUtil.setDateEnd(seasonVM.getFirstStageEndTime()));
        seasonDB.setSecondStageStartTime(seasonVM.getSecondStageStartTime());
        seasonDB.setSecondStageEndTime(DateUtil.setDateEnd(seasonVM.getSecondStageEndTime()));
        seasonRepository.updateByPrimaryKeySelective(seasonDB);
        return new SeasonDTO(seasonDB);
    }

    /**
     * 发布季度考核
     *
     * @param seasonId
     */
    @Transactional
    public void publish(Long seasonId) {
        //1. 检查季度考核状态
        Season seasonDB = seasonRepository.selectByPrimaryKey(seasonId);
        ExceptionUtil.checkState(seasonDB.getStatus().equals(SeasonStatus.NEW.getCode()), "只有新建的季度考核才能发布");

        //2. 查询技术部员工
        List<Long> depIds = departmentService.getOkrDepartmentIds();
        List<Long> allDepIdList = departmentService.getDepartmentIdAndChildrens(depIds);

        //3. 创建考核任务 排除离职的
        List<Long> userIdList = userProfileRepository.findAllByDepartmentIdInExceptInvalidUser(allDepIdList);

        List<UserSeason> userSeasonList = new ArrayList<>();
        for (Long userId : userIdList) {
            UserSeason userSeason = new UserSeason();
            userSeason.setSeasonId(seasonDB.getId());
            userSeason.setUserId(userId);
            userSeason.setAssessStatus(AssessStatus.UNDERWAY.getCode());
            userSeason.setCreatedDate(new Date());
            userSeasonList.add(userSeason);
        }
        userSeasonRepository.insertList(userSeasonList);
        //考核得分详情总计
        saveUserSeasonItemList(userSeasonList);

        //4. 修改任务状态为已发布
        seasonDB.setStatus(SeasonStatus.PUBLISHED.getCode());
        seasonRepository.updateByPrimaryKeySelective(seasonDB);

        //5. 发送邮件通知
        //mailService.sendPublishSeasonEmail(seasonDB, users);
    }


    /**
     * 重新发布功能
     * 目的：给未参与考核的用户绑定考核任务
     *
     * @param seasonId
     */
    @Transactional
    public void rePublish(Long seasonId) {
        //1. 检查季度考核状态
        Season seasonDB = seasonRepository.selectByPrimaryKey(seasonId);
        if (null == seasonDB) {
            log.info("rePublish 重新发布考核信息不存在，seasonId：{}", seasonId);
            return;
        }
        log.info("rePublish 重新发布考核：{}", seasonDB);
        //判断同事互评时间若小于当前时间，说明考核任务已经生成
        //则该考核不在补填用户参与考核
        Date nowDate = new Date();
        if (seasonDB.getSecondStageStartTime().before(nowDate)) {
            log.info("rePublish 考核任务已进入同事互评阶段，用户不可再参与考核。当前时间：{}，同事互评开始时间：{}",
                    DateUtil.toString(nowDate), DateUtil.toString(seasonDB.getSecondStageStartTime()));
            return;
        }

        //2. 查询技术部员工
        List<Long> depIds = departmentService.getOkrDepartmentIds();
        List<Long> allDepIdList = departmentService.getDepartmentIdAndChildrens(depIds);

        //3. 获取所有技术部员工信息
        List<Long> userIdList = userProfileRepository.findAllByDepartmentIdInExceptInvalidUser(allDepIdList);
        //4.获取所有参与考核的用户
        List<Long> seasonUserIdList = userProfileRepository.findAllBySeasonId(seasonId);
        //5 排除已经参与考核的用户
        if (null != seasonUserIdList && seasonUserIdList.size() > 0) {
            userIdList.removeAll(seasonUserIdList);
        }
        //6 去除已存在的用户，添加新的用户考核
        if (null != userIdList && userIdList.size() > 0) {
            List<UserSeason> userSeasonList = new ArrayList<>();
            for (Long userId : userIdList) {
                UserSeason userSeason = new UserSeason();
                userSeason.setSeasonId(seasonDB.getId());
                userSeason.setUserId(userId);
                userSeason.setAssessStatus(AssessStatus.UNDERWAY.getCode());
                userSeason.setCreatedDate(new Date());
                userSeasonList.add(userSeason);
                log.info("rePublish 重新发布将用户id：{}，加入考核任务：{}！", userId, seasonDB.getTitle());
            }
            userSeasonRepository.insertList(userSeasonList);
            //考核得分详情总计
            saveUserSeasonItemList(userSeasonList);
        }
        log.info("rePublish 重新发布将用户，执行完成。总共添加用户数：{}", null == userIdList ? 0 : userIdList.size());

    }

    /**
     * 保存用户考核详情（总计详情）
     *
     * @param userSeasonList
     */
    private void saveUserSeasonItemList(List<UserSeason> userSeasonList) {
        List<UserSeasonItem> userSeasonItemList = new ArrayList<>();
        for (UserSeason userSeason : userSeasonList) {
            UserSeasonItem userSeasonItem = new UserSeasonItem();
            userSeasonItem.setUserSeasonId(userSeason.getId());
            userSeasonItem.setOptionId(0L);
            userSeasonItemList.add(userSeasonItem);
        }
        userSeasonItemRepository.insertList(userSeasonItemList);
    }


    /**
     * 查找季度考核数据
     *
     * @param id
     * @return
     */
    public SeasonDTO findById(Long id) {
        Season season = seasonRepository.selectByPrimaryKey(id);
        ExceptionUtil.checkState(season != null, "用户季度考核不存在");
        return new SeasonDTO(season);
    }

    /**
     * 查找季度考核列表
     *
     * @param pageable
     * @return
     */
    public Page<SeasonDTO> findList(Pageable pageable) {
        long count = seasonRepository.countPage();
        List<SeasonDTO> seasonDTOs = new ArrayList<>();
        if (count > 0) {
            List<Season> seasonList = seasonRepository.queryPage(pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
            seasonDTOs = seasonList.stream()
                    .map(season -> new SeasonDTO(season))
                    .collect(Collectors.toList());
        }
        return new PageImpl<>(seasonDTOs, pageable, count);
    }

    /**
     * 获取当前season
     *
     * @return
     */
    public CurrentSeasonDTO getCurrentSeason() {
        Season querySeason = new Season();
        querySeason.setStatus(SeasonStatus.PUBLISHED.getCode());
        List<Season> seasonList = seasonRepository.findAllBySeason(querySeason, 0, 1);
        if (seasonList.size() > 0) {
            Season season = seasonList.get(0);
            UserSeason userSeason = userSeasonRepository.findOneBySeasonIdAndUserId(season.getId(), SecurityUtils.getCurrentUserId());
            return new CurrentSeasonDTO(season, userSeason);
        }
        return null;
    }

    /**
     * 获取当前季度考核列表
     *
     * @return
     */
    public List<CurrentSeasonDTO> getCurrentSeasons() {
        Season querySeason = new Season();
        querySeason.setStatus(SeasonStatus.PUBLISHED.getCode());
        List<Season> seasonList = seasonRepository.findAllBySeason(querySeason, 0, 0);
        if (seasonList == null || seasonList.size() == 0) {
            return new ArrayList<>();
        }
        List<CurrentSeasonDTO> dtoList = new ArrayList<>();
        for (Season season : seasonList) {
            //排除已经结束的季度考核
            if (season.isMakeSeasonScore()) {
                continue;
            }
            UserSeason userSeason = userSeasonRepository.findOneBySeasonIdAndUserId(season.getId(), SecurityUtils.getCurrentUserId());
            dtoList.add(new CurrentSeasonDTO(season, userSeason));
        }
        return dtoList;
    }


    /**
     * 获取所有年度
     */
    public List<String> getAllSeasonYears() {
        String value = redisService.get(RedisConstants.SEASON_ALL_YEARS);
        if (StringUtils.isBlank(value)) {
            List<String> list = getAllSeasonYearList();
            redisService.set(RedisConstants.SEASON_ALL_YEARS, JSON.toJSONString(list));
            return list;
        }
        return JSON.parseArray(value, String.class);
    }

    private List<String> getAllSeasonYearList() {
        return seasonRepository.queryAllSeasonYear(Lists.newArrayList(SeasonStatus.PUBLISHED.getCode(), SeasonStatus.FINISH.getCode()));
    }

    /**
     * 获取所有年度的季度
     *
     * @param year
     */
    public List<SeasonDTO> getAllSeasonsByYear(String year) {
        return seasonRepository.queryAllBySeasonStatusAndYear(Lists.newArrayList(SeasonStatus.PUBLISHED.getCode(), SeasonStatus.FINISH.getCode()), year);
    }


}
