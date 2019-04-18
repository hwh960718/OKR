package com.mobvista.okr.service;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.domain.OkrContent;
import com.mobvista.okr.dto.OkrContentDTO;
import com.mobvista.okr.exception.ExceptionUtil;
import com.mobvista.okr.repository.OkrContentRepository;
import com.mobvista.okr.util.DateUtil;
import com.mobvista.okr.vm.OkrContentVM;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 注释：okr目标
 * 作者：刘腾飞【liutengfei】
 * 时间：2018-03-30 14:50
 */
@Service
public class OkrContentService {

    @Resource
    private OkrContentRepository okrContentRepository;

    /**
     * 创建
     *
     * @param vm
     * @return
     */
    public OkrContentDTO create(OkrContentVM vm) {
        OkrContent okrContent = new OkrContent();
        okrContent.setUserSeasonId(vm.getUserSeasonId());
        okrContent.setOkrContent(vm.getOkrContent());
        okrContent.setId(vm.getId());
        okrContent.setOkrTitle(vm.getOkrTitle());
        okrContent.setCreatedDate(DateUtil.nowDate());
        okrContent.setLastModifiedDate(DateUtil.nowDate());
        okrContentRepository.insert(okrContent);
        return new OkrContentDTO(okrContent);
    }

    /**
     * 更新
     *
     * @param vm
     */
    public CommonResult update(OkrContentVM vm) {
        OkrContent okrContent = okrContentRepository.selectByPrimaryKey(vm.getId());
        if (null == okrContent) {
            return CommonResult.error("考核目标数据不存在");
        }
        okrContent.setUserSeasonId(vm.getUserSeasonId());
        okrContent.setOkrTitle(vm.getOkrTitle());
        okrContent.setOkrContent(vm.getOkrContent());
        okrContentRepository.updateByPrimaryKeySelective(okrContent);
        return CommonResult.success();
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        OkrContent okrContent = okrContentRepository.selectByPrimaryKey(id);

        List<OkrContent> contentList = okrContentRepository.findAllByUserSeasonId(okrContent.getUserSeasonId());

        ExceptionUtil.checkState(contentList.size() > 1, "不能删除，至少需要录入一条OKR目标");

        okrContentRepository.deleteByPrimaryKey(id);
    }

    /**
     * 获取okr
     *
     * @param userSeasonId
     * @return
     */
    public List<OkrContentDTO> list(Long userSeasonId) {
        List<OkrContent> okrContentList = okrContentRepository.findAllByUserSeasonId(userSeasonId);

        List<OkrContentDTO> dtoList = okrContentList.stream().map(okrContent -> {
            return new OkrContentDTO(okrContent);
        }).collect(Collectors.toList());

        return dtoList;
    }
}
