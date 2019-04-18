package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.service.score.ScoreUserDetailService;
import com.mobvista.okr.service.score.ScoreUserService;
import com.mobvista.okr.vm.ScoreUserVM;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户积分接口
 *
 * @author Weier Gu (顾炜)
 * @date 2018/7/24 17:25
 */
@RestController
@RequestMapping("/api/score/scoreUser")
public class ScoreUserResource {

    @Resource
    private ScoreUserService scoreUserService;
    @Resource
    private ScoreUserDetailService scoreUserDetailService;

    @ApiOperation("分页查询用户积分信息")
    @GetMapping("queryScoreUserInfoPage")
    public CommonResult queryScoreUserInfoPage(String userName, Pageable pageable) {
        ScoreUserVM scoreUserVM = new ScoreUserVM();
        scoreUserVM.setUserName(userName);
        return scoreUserService.queryScoreUserInfo(scoreUserVM, pageable);
    }


    /**
     * 分页查询用户积分明细
     *
     * @param userId
     * @param pageable
     * @return
     */
    @GetMapping("queryScoreUserDetailPage")
    public CommonResult queryScoreUserDetail(Long userId, Pageable pageable) {
        return scoreUserDetailService.queryScoreUserDetail(userId, pageable);
    }

    /**
     * 分页查询当前用户积分明细
     *
     * @param pageable
     * @return
     * @version v3.0
     */
    @GetMapping("queryCurrentScoreUserDetail")
    public CommonResult queryCurrentScoreUserDetail(Pageable pageable) {
        return scoreUserDetailService.queryScoreUserDetail(SecurityUtils.getCurrentUserId(), pageable);
    }


    /**
     * 查询当前用户积分数据
     *
     * @return
     * @version v3.0
     */
    @GetMapping("queryCurrentUserScoreInfo")
    public CommonResult queryCurrentUserScoreInfo() {
        return scoreUserService.queryCurrentUserScoreInfo();
    }

}
