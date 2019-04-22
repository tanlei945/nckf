package org.benben.modules.business.feedback.service.impl;

import org.benben.modules.business.feedback.entity.FeedBack;
import org.benben.modules.business.feedback.mapper.FeedBackMapper;
import org.benben.modules.business.feedback.service.IFeedBackService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 用户反馈表
 * @author： jeecg-boot
 * @date：   2019-04-22
 * @version： V1.0
 */
@Service
public class FeedBackServiceImpl extends ServiceImpl<FeedBackMapper, FeedBack> implements IFeedBackService {

}
