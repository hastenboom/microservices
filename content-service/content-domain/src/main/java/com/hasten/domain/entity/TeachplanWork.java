package com.hasten.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Hasten
 * @since 2024-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("teachplan_work")
@ApiModel(value="TeachplanWork对象", description="")
public class TeachplanWork implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "作业信息标识")
    private Long workId;

    @ApiModelProperty(value = "作业标题")
    private String workTitle;

    @ApiModelProperty(value = "课程计划标识")
    private Long teachplanId;

    @ApiModelProperty(value = "课程标识")
    private Long courseId;

    private LocalDateTime createDate;

    private Long coursePubId;


}
