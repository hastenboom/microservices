package com.hasten.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageParams {

    //当前页码
    @ApiModelProperty("页码")
    @NotNull
    private Long pageNo = 1L;
    //每页显示记录数
    @ApiModelProperty("每页记录数")
    @NotNull
    private Long pageSize = 30L;

}
