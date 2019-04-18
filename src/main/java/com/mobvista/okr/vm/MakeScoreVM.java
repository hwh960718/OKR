package com.mobvista.okr.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * 评分 VM
 */
@ApiModel("评分")
public class MakeScoreVM {

    @ApiModelProperty("评价任务id")
    @NotNull
    private Long taskId;

    @ApiModelProperty("评分项")
    @NotNull
    @Size(min = 1)
    @Valid
    private List<MakeScoreItem> items;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public List<MakeScoreItem> getItems() {
        return items;
    }

    public void setItems(List<MakeScoreItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "MakeScoreVM{" +
                "taskId=" + taskId +
                ", items=" + items +
                '}';
    }

    public static class MakeScoreItem {
        @ApiModelProperty("评分项id")
        @NotNull
        private Long optionId;

        @ApiModelProperty("评分")
        @NotNull
        @Max(100)
        @Min(0)
        private BigDecimal score;

        public Long getOptionId() {
            return optionId;
        }

        public void setOptionId(Long optionId) {
            this.optionId = optionId;
        }

        public BigDecimal getScore() {
            return score;
        }

        public void setScore(BigDecimal score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "MakeScoreItem{" +
                    "optionId=" + optionId +
                    ", score=" + score +
                    '}';
        }
    }
}
