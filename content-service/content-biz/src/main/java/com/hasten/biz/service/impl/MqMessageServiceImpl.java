package com.hasten.biz.service.impl;

import com.hasten.biz.mapper.MqMessageMapper;
import com.hasten.biz.service.IMqMessageService;
import com.hasten.domain.entity.MqMessage;
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
