package com.hasten.content.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("mq_message_history")
@ApiModel(value="MqMessageHistory对象", description="")
public class MqMessageHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "消息类型代码")
    private String messageType;

    @ApiModelProperty(value = "关联业务信息")
    private String businessKey1;

    @ApiModelProperty(value = "关联业务信息")
    private String businessKey2;

    @ApiModelProperty(value = "关联业务信息")
    private String businessKey3;

    @ApiModelProperty(value = "通知次数")
    private Integer executeNum;

    @ApiModelProperty(value = "处理状态，0:初始，1:成功，2:失败")
    private Integer state;

    @ApiModelProperty(value = "回复失败时间")
    private LocalDateTime returnfailureDate;

    @ApiModelProperty(value = "回复成功时间")
    private LocalDateTime returnsuccessDate;

    @ApiModelProperty(value = "回复失败内容")
    private String returnfailureMsg;

    @ApiModelProperty(value = "最近通知时间")
    private LocalDateTime executeDate;

    private String stageState1;

    private String stageState2;

    private String stageState3;

    private String stageState4;


}
