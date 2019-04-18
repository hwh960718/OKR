package com.mobvista.okr.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 公告类VM
 * @author Weier Gu (顾炜)
 */
@ApiModel("公告类")
public class NoticeVM {

    @ApiModelProperty("公告内容")
    @NotBlank(message = "公告内容不能为空")
    @Size(min = 1, max = 500, message = "公告内容1-500个字符")
    private String content;

    @ApiModelProperty("公告有效时间")
    @NotNull(message = "公告有效时间不能为空")
    private Date validDate;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }
}
