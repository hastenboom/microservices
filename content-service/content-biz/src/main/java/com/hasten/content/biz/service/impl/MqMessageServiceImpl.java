package com.hasten.content.biz.service.impl;

import com.hasten.content.biz.mapper.MqMessageMapper;
import com.hasten.content.biz.service.IMqMessageService;
import com.hasten.content.domain.entity.MqMessage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hasten
 * @since 2024-09-05
 */
@Service
public class MqMessageServiceImpl extends ServiceImpl<MqMessageMapper, MqMessage> implements IMqMessageService {

}
