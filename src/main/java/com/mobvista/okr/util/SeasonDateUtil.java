package com.mobvista.okr.util;

import com.mobvista.okr.domain.Season;
import com.mobvista.okr.dto.AssessTaskDTO;
import com.mobvista.okr.dto.UserSeasonDTO;
import com.mobvista.okr.enums.ProgressStatusEnum;

import java.util.Date;

/**
 * @author 顾炜(GUWEI) 时间：2018/4/17 14:28
 */
public class SeasonDateUtil {

    public SeasonDateUtil() {
    }

    /**
     * 获取考核进度
     *
     * @param firstStageStartTime
     * @param firstStageEndTime
     * @param secondStageStartTime
     * @param secondStageEndTime
     * @return
     */
    public static ProgressStatusEnum getProcessStatus(Date firstStageStartTime, Date firstStageEndTime, Date secondStageStartTime, Date secondStageEndTime) {
        Date now = DateUtil.nowDate();
        if (now.compareTo(secondStageEndTime) > 0) {
            return ProgressStatusEnum.END;
        }
        if (now.compareTo(secondStageStartTime) >= 0 && now.compareTo(secondStageEndTime) <= 0) {
            return ProgressStatusEnum.SECOND_IN;
        }
        if (now.compareTo(firstStageEndTime) > 0 && now.compareTo(secondStageStartTime) < 0) {
            return ProgressStatusEnum.FIRST_OUT;
        }
        if (now.compareTo(firstStageStartTime) >= 0 && now.compareTo(firstStageEndTime) <= 0) {
            return ProgressStatusEnum.FIRST_IN;
        }
        if (now.compareTo(firstStageStartTime) < 0) {
            return ProgressStatusEnum.NOT_START;
        }
        return ProgressStatusEnum.NOT_START;
    }

    /**
     * 获取考核进度
     *
     * @param assessTaskDTO
     * @return
     */
    public static ProgressStatusEnum getProcessStatus(AssessTaskDTO assessTaskDTO) {
        return getProcessStatus(assessTaskDTO.getFirstStageStartTime(), assessTaskDTO.getFirstStageEndTime(), assessTaskDTO.getSecondStageStartTime(), assessTaskDTO.getSecondStageEndTime());
    }

    /**
     * 获取考核进度
     *
     * @param userSeasonDTO
     * @return
     */
    public static ProgressStatusEnum getProcessStatus(UserSeasonDTO userSeasonDTO) {
        return getProcessStatus(userSeasonDTO.getFirstStageStartTime(), userSeasonDTO.getFirstStageEndTime(), userSeasonDTO.getSecondStageStartTime(), userSeasonDTO.getSecondStageEndTime());
    }

    /**
     * 获取考核进度
     *
     * @param season
     * @return
     */
    public static ProgressStatusEnum getProcessStatus(Season season) {
        return getProcessStatus(season.getFirstStageStartTime(), season.getFirstStageEndTime(), season.getSecondStageStartTime(), season.getSecondStageEndTime());
    }

}
