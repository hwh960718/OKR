package com.mobvista.okr.service.score;

import com.google.common.collect.Lists;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.domain.ScoreUser;
import com.mobvista.okr.dto.score.ScoreUserDTO;
import com.mobvista.okr.repository.ScoreUserRepository;
import com.mobvista.okr.repository.UserProfileRepository;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.service.DepartmentService;
import com.mobvista.okr.util.AwsPictureProcessUtil;
import com.mobvista.okr.vm.ScoreUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 用户积分
 *
 * @author Weier Gu (顾炜)
 * @date 2018/7/23 17:19
 */
@Service
public class ScoreUserService {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(ScoreUserService.class);

    @Resource
    private ScoreUserRepository scoreUserRepository;
    @Resource
    private UserProfileRepository userProfileRepository;
    @Resource
    private DepartmentService departmentService;

    /**
     * 根据用户名查询用户积分列表
     *
     * @param scoreUserVM
     * @param pageable
     * @return
     */
    public CommonResult queryScoreUserInfo(ScoreUserVM scoreUserVM, Pageable pageable) {
        if (null == scoreUserVM) {
            return CommonResult.error("入参不能为空");
        }

        String userName = scoreUserVM.getUserName();
        List<Long> departmentIds = departmentService.getOkrALLDepartmentIds();
        long count = scoreUserRepository.countPageByNameLike(userName, departmentIds);
        List<ScoreUserDTO> list = Lists.newArrayList();
        if (count > 0) {
            list = scoreUserRepository.queryPageByNameLike(userName, departmentIds, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
            AwsPictureProcessUtil.assembleScoreUserDTO(list);
        }
        Page<ScoreUserDTO> page = new PageImpl<>(list, pageable, count);
        return CommonResult.success(page);
    }


    /**
     * 同步用户积分数据
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void synScoreUserRule() {
        //获取当前用户所有的有效用户id
        List<Long> allUserIdList = userProfileRepository.findAllByDepartmentIdInExceptInvalidUser(null);
        //获取当前存在的用户积分用户id
        List<Long> currentUserIdList = scoreUserRepository.getAllUserId();
        //移除已存在的用户id
        allUserIdList.removeAll(currentUserIdList);
        if (allUserIdList.size() > 0) {
            scoreUserRepository.insertList(init(allUserIdList));

        }
    }


    /**
     * 获取当前用户的积分数据
     *
     * @return
     */
    public CommonResult queryCurrentUserScoreInfo() {
        ScoreUser scoreUser = scoreUserRepository.selectByPrimaryKey(SecurityUtils.getCurrentUserId());
        ScoreUserDTO dto = new ScoreUserDTO();
        if (null != scoreUser) {
            dto = convertPo2Dto(scoreUser);
            dto.setValidScoreTotal(dto.getValidScoreTotal() - dto.getLockedScoreTotal());
        }
        return CommonResult.success(dto);
    }

    private List<ScoreUser> init(List<Long> userIdList) {
        List<ScoreUser> list = Lists.newArrayList();
        for (Long userId : userIdList) {
            list.add(init(userId));
        }
        return list;
    }

    private ScoreUser init(Long userId) {
        ScoreUser su = new ScoreUser();
        su.setId(userId);
        su.setAbatementScoreTotal(0L);
        su.setValidScoreTotal(0L);
        su.setGrantScoreTotal(0L);
        su.setModifyDate(new Date());
        return su;
    }


    private ScoreUserDTO convertPo2Dto(ScoreUser po) {
        ScoreUserDTO dto = new ScoreUserDTO();
        dto.setId(po.getId());
        dto.setAbatementScoreTotal(po.getAbatementScoreTotal());
        dto.setValidScoreTotal(po.getValidScoreTotal());
        dto.setGrantScoreTotal(po.getGrantScoreTotal());
        dto.setLockedScoreTotal(po.getLockedScoreTotal());
        return dto;
    }

}
