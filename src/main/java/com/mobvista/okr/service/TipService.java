package com.mobvista.okr.service;

import com.google.common.collect.Lists;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.domain.Tip;
import com.mobvista.okr.repository.TipRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签服务
 *
 * @author Weier Gu (顾炜)
 * @date 2018/6/29 10:18
 */
@Service
public class TipService {

    @Resource
    private TipRepository tipRepository;


    /**
     * 根据标签名称，模糊查询
     *
     * @param title
     * @return
     */
    public CommonResult findLikeTitle(String title) {
        List<Tip> list = Lists.newArrayList();
        if (StringUtils.isNotBlank(title)) {
            list = tipRepository.queryLikeTitle(title);
        }
        return CommonResult.success(list);
    }
}
