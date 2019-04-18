package com.mobvista.okr.service;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.domain.AssessTask;
import com.mobvista.okr.domain.UserProfile;
import com.mobvista.okr.domain.UserSeasonComment;
import com.mobvista.okr.domain.UserSeasonCommentTop;
import com.mobvista.okr.dto.UserSeasonCommentDTO;
import com.mobvista.okr.exception.ExceptionUtil;
import com.mobvista.okr.repository.*;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.vm.UserSeasonCommentVM;
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
 * Service class for managing UserSeasonComment.
 */
@Service
public class UserSeasonCommentService {

    private final Logger log = LoggerFactory.getLogger(UserSeasonCommentService.class);

    @Resource
    private UserSeasonCommentRepository userSeasonCommentRepository;

    @Resource
    private UserSeasonCommentTopRepository userSeasonCommentTopRepository;

    @Resource
    private UserSeasonRepository userSeasonRepository;

    @Resource
    private AssessTaskRepository assessTaskRepository;

    @Resource
    private UserProfileRepository userProfileRepository;

    /**
     * 发表评论
     *
     * @param userSeasonCommentVM
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public UserSeasonCommentDTO create(UserSeasonCommentVM userSeasonCommentVM) {
        UserSeasonComment userSeasonComment = new UserSeasonComment();
        userSeasonComment.setAssessorId(SecurityUtils.getCurrentUserId());
        userSeasonComment.setContent(userSeasonCommentVM.getContent());
        userSeasonComment.setUserId(userSeasonCommentVM.getUserId());
        userSeasonComment.setTopCount(0L);
        userSeasonComment.setCreatedDate(new Date());
        userSeasonCommentRepository.insert(userSeasonComment);
        return new UserSeasonCommentDTO(userSeasonComment, true);
    }

    /**
     * 更新评论
     *
     * @param vm
     * @return
     */
    public CommonResult update(UserSeasonCommentVM vm) {
        if (vm == null || vm.getCommentId() == null) {
            return CommonResult.error("评论不存在");
        }
        UserSeasonComment comment = userSeasonCommentRepository.selectByPrimaryKey(vm.getCommentId());
        if (comment == null) {
            return CommonResult.error("评论不存在");
        }
        if (!SecurityUtils.getCurrentUserId().equals(comment.getAssessorId())) {
            return CommonResult.error("无权限更新评论");
        }
        if (vm.getContent() == null || vm.getContent().length() == 0) {
            return CommonResult.error("请输入评论内容");
        }

        comment.setContent(vm.getContent());
        comment.setCreatedDate(new Date());
        userSeasonCommentRepository.updateByPrimaryKey(comment);
        return CommonResult.success(new UserSeasonCommentDTO(comment, true));
    }

    /**
     * 删除评论
     *
     * @param commentId
     * @return
     */
    public CommonResult delete(Long commentId) {
        if (commentId == null || commentId == 0) {
            return CommonResult.error("评论不存在");
        }
        UserSeasonComment comment = userSeasonCommentRepository.selectByPrimaryKey(commentId);
        if (comment == null) {
            return CommonResult.error("评论不存在");
        }
        if (!SecurityUtils.getCurrentUserId().equals(comment.getAssessorId())) {
            return CommonResult.error("无权限删除评论");
        }
        userSeasonCommentRepository.deleteByPrimaryKey(commentId);
        return CommonResult.success();
    }

    /**
     * 评论点赞
     *
     * @param commentId
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void top(Long commentId) {
        long count = userSeasonCommentTopRepository.countByUserIdAndCommentId(SecurityUtils.getCurrentUserId(), commentId);
        ExceptionUtil.checkState(count == 0, "已经顶过了");

        UserSeasonComment userSeasonComment = userSeasonCommentRepository.selectByPrimaryKey(commentId);
        userSeasonComment.setTopCount(userSeasonComment.getTopCount() + 1);
        userSeasonCommentRepository.updateByPrimaryKeySelective(userSeasonComment);

        UserSeasonCommentTop userSeasonCommentTop = new UserSeasonCommentTop();
        userSeasonCommentTop.setCommentId(commentId);
        userSeasonCommentTop.setUserId(SecurityUtils.getCurrentUserId());
        userSeasonCommentTopRepository.insert(userSeasonCommentTop);
    }

    /**
     * 获取考核评论
     *
     * @return
     */
    public Page<UserSeasonCommentDTO> getUserCommentList(Long userId, Long taskId, Pageable pageable) {
        //如果传入taskId表示是领导查看下属评分状况和评论详情，判断角色
        if (taskId != null) {
            return getSubordinateSeasonCommentList(userId, taskId, pageable);
        }
        int count = userSeasonCommentRepository.countByUserIdAndAssessorId(userId, null);
        List<UserSeasonComment> userSeasonCommentList = new ArrayList<>();
        if (count > 0) {
            userSeasonCommentList = userSeasonCommentRepository.findAllByUserIdAndAssessorId(userId, null, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        }
        return makeUserSeasonCommentPageToDTOPage(userSeasonCommentList, count, pageable, null);
    }


    /**
     * 领导查看下属考核评论
     *
     * @return
     */
    private Page<UserSeasonCommentDTO> getSubordinateSeasonCommentList(Long userId, Long taskId, Pageable pageable) {
        AssessTask assessTask = assessTaskRepository.selectByPrimaryKey(taskId);
        ExceptionUtil.checkState(assessTask != null, "评价任务不存在");
        UserProfile userProfile = userProfileRepository.selectByPrimaryKey(assessTask.getAssessorId());
        int count = userSeasonCommentRepository.countByUserIdAndAssessorId(userId, assessTask.getAssessorId());
        List<UserSeasonComment> userSeasonCommentList = new ArrayList<>();
        if (count > 0) {
            userSeasonCommentList = userSeasonCommentRepository.findAllByUserIdAndAssessorId(userId, assessTask.getAssessorId(), pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        }
        Page<UserSeasonCommentDTO> result = makeUserSeasonCommentPageToDTOPage(userSeasonCommentList, count, pageable, userProfile);
        return result;
    }

    /**
     * 组装dto page
     *
     * @param userSeasonCommentList
     * @param pageable
     * @return
     */
    private Page<UserSeasonCommentDTO> makeUserSeasonCommentPageToDTOPage(List<UserSeasonComment> userSeasonCommentList, long count, Pageable pageable, UserProfile userProfile) {
        List<Long> commentIds = userSeasonCommentList
                .stream()
                .map(UserSeasonComment::getId)
                .collect(Collectors.toList());
        List<UserSeasonCommentDTO> userSeasonCommentDTOS = new ArrayList<>();
        if (null != commentIds && commentIds.size() > 0) {
            userSeasonCommentDTOS = userSeasonCommentList
                    .stream()
                    .map(userSeasonComment -> {
                        UserSeasonCommentDTO commentDTO = new UserSeasonCommentDTO();
                        commentDTO.setCanComment(true);//设置可顶，不做限制
                        commentDTO.setContent(userSeasonComment.getContent());
                        commentDTO.setCreatedDate(userSeasonComment.getCreatedDate());
                        commentDTO.setId(userSeasonComment.getId());
                        commentDTO.setTopCount(userSeasonComment.getTopCount());
                        if (null != userProfile) {
                            commentDTO.setAssessorRealname(userProfile.getRealName());
                            commentDTO.setAssessorProfilePhoto(userProfile.getProfilePhoto());
                        }
                        if (SecurityUtils.getCurrentUserId().equals(userSeasonComment.getAssessorId())) {
                            commentDTO.setCanUpdate(Boolean.TRUE);
                        } else {
                            commentDTO.setCanUpdate(Boolean.FALSE);
                        }
                        return commentDTO;
                    })
                    .collect(Collectors.toList());
        }
        return new PageImpl<>(userSeasonCommentDTOS, pageable, count);


    }
}