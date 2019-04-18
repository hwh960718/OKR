package com.mobvista.okr.repository;

import com.mobvista.okr.domain.Notice;
import com.mobvista.okr.dto.NoticeDTO;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author 顾炜[GuWei]
 */
@Mapper
public interface NoticeRepository extends BaseRepository<Notice> {

    /**
     * 查询不需要删除的的公告
     *
     * @param status
     * @param validDate
     * @return
     */
    List<NoticeDTO> queryNoticeByStatusAndBeforeValidDate(@Param("status") Byte status, @Param("validDate") Date validDate);


    List<Notice> queryNoticeByStatusAndAfterValidDate(@Param("status") Byte status, @Param("validDate") Date validDate);


    /**
     * 根据id集合批量更新状态
     *
     * @param status
     * @param idList
     */
    void updateNoticeStatusByIdList(@Param("status") byte status, @Param("idList") List<Long> idList);

}