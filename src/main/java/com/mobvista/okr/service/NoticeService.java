package com.mobvista.okr.service;

import com.alibaba.fastjson.JSON;
import com.mobvista.okr.constants.RedisConstants;
import com.mobvista.okr.domain.Notice;
import com.mobvista.okr.dto.NoticeDTO;
import com.mobvista.okr.enums.NoticeStatus;
import com.mobvista.okr.exception.ExceptionUtil;
import com.mobvista.okr.repository.NoticeRepository;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.vm.NoticeVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Notice.
 *
 * @author Weier Gu (顾炜)
 */
@Service
public class NoticeService {

    @Resource
    private NoticeRepository noticeRepository;
    @Resource
    private RedisService redisService;

    /**
     * 创建公告
     *
     * @param noticeVM
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void create(NoticeVM noticeVM) {
        ExceptionUtil.checkState(!noticeVM.getValidDate().before(new Date()), "有效时间必须大于当前时间");
        noticeRepository.insert(convertVM2PO(noticeVM));
        getValidNoticeListAndUpdateRedis();
    }


    /**
     * 删除公告
     *
     * @param id
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(Long id) {
        Notice preNotice = noticeRepository.selectByPrimaryKey(id);
        //只有有效的公告才可以删除
        ExceptionUtil.checkState(null != preNotice && preNotice.getStatus() == NoticeStatus.Valid.getCode(), "只有有效的公告才可以删除");
        Notice notice = new Notice();
        notice.setId(id);
        notice.setStatus(NoticeStatus.Delete.getCode());
        noticeRepository.updateByPrimaryKeySelective(notice);
        getValidNoticeListAndUpdateRedis();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void expiredUnValidNotice() {
        //获取所有炒作有效时间的∂公告
        List<Notice> list = noticeRepository.queryNoticeByStatusAndAfterValidDate(NoticeStatus.Valid.getCode(), new Date());
        if (null == list || list.size() == 0) {
            return;
        }
        List<Long> idList = list.stream().map(Notice::getId).collect(Collectors.toList());
        noticeRepository.updateNoticeStatusByIdList(NoticeStatus.UnValid.getCode(), idList);
        getValidNoticeListAndUpdateRedis();
    }

    /**
     * 获取有效公告
     *
     * @return
     */
    public List<NoticeDTO> findNotice(Byte status, Date date) {
        List<NoticeDTO> list = noticeRepository.queryNoticeByStatusAndBeforeValidDate(status, date);
        return list;
    }


    /**
     * 获取有效的公告列表
     *
     * @return
     */
    public List<NoticeDTO> findValidNoticeList() {
        String value = redisService.get(RedisConstants.NOTICE_VALID_LIST_KEY);
        List<NoticeDTO> list;
        if (StringUtils.isBlank(value)) {
            list = getValidNoticeListAndUpdateRedis();
        } else {
            list = JSON.parseArray(value, NoticeDTO.class);
        }
        return list;
    }

    /**
     * 获取有效的公告并更新redis缓存
     *
     * @return
     */
    public List<NoticeDTO> getValidNoticeListAndUpdateRedis() {
        List<NoticeDTO> list = findNotice(NoticeStatus.Valid.getCode(), new Date());
        redisService.set(RedisConstants.NOTICE_VALID_LIST_KEY, JSON.toJSONString(list));
        return list;
    }


    private Notice convertVM2PO(NoticeVM vm) {
        Notice po = new Notice();
        po.setContent(vm.getContent());
        po.setValidDate(vm.getValidDate());
        po.setCreateUserId(SecurityUtils.getCurrentUserId());
        po.setStatus(NoticeStatus.Valid.getCode());
        po.setCreatedDate(new Date());
        po.setLastModifiedDate(new Date());
        return po;
    }
}
