package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.exception.ServiceErrorException;
import com.mobvista.okr.service.UserSeasonCommentService;
import com.mobvista.okr.vm.CommonRequestVM;
import com.mobvista.okr.vm.UserSeasonCommentVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/u/userSeasonComment")
public class UserSeasonCommentResource {

    private final Logger log = LoggerFactory.getLogger(UserSeasonCommentResource.class);

    @Resource
    private UserSeasonCommentService userSeasonCommentService;

    /**
     * 发表评论
     *
     * @param vm
     * @return
     * @verison v3.0
     */
    @PostMapping("/create")
    public CommonResult create(@RequestBody @Valid UserSeasonCommentVM vm) {
        log.debug("REST request to create UserSeasonComment: {}", vm);
        try {
            return CommonResult.success(userSeasonCommentService.create(vm));
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[发表评论]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    /**
     * 更新评论
     *
     * @param vm
     * @return
     * @verison v3.0
     */
    @PostMapping("/update")
    public CommonResult update(@RequestBody @Valid UserSeasonCommentVM vm) {
        log.debug("REST request to update UserSeasonComment: {}", vm);
        try {
            return userSeasonCommentService.update(vm);
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[更新评论]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    /**
     * 删除评论
     *
     * @param commentId
     * @return
     * @verison v3.0
     */
    @PostMapping("/delete")
    public CommonResult delete(@RequestParam("commentId") Long commentId) {
        log.debug("REST request to delete UserSeasonComment: {}", commentId);
        try {
            return userSeasonCommentService.delete(commentId);
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[删除评论]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    /**
     * 评论点赞
     *
     * @param commonRequestVM
     * @return
     * @verison v3.0
     */
    @PostMapping("/top")
    public CommonResult top(@RequestBody CommonRequestVM commonRequestVM) {
        log.debug("REST request to top UserSeasonComment: {}", commonRequestVM.getId());
        try {
            userSeasonCommentService.top(commonRequestVM.getId());
            return CommonResult.success();
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[评论点赞]", e);
            return CommonResult.error(e.getMessage());
        }
    }

    /**
     * 获取评论列表
     *
     * @param userId
     * @param taskId
     * @param pageable
     * @return
     * @verison v3.0
     */
    @GetMapping("/list")
    public CommonResult getUserCommentList(@RequestParam Long userId, @RequestParam(required = false) Long taskId, Pageable pageable) {
        log.debug("REST request to getUserCommentList: {}, {}, {}", userId, taskId, pageable);
        try {
            return CommonResult.success(userSeasonCommentService.getUserCommentList(userId, taskId, pageable));
        } catch (ServiceErrorException e) {
            log.error("ServiceErrorException[获取评论列表]", e);
            return CommonResult.error(e.getMessage());
        }
    }

}
