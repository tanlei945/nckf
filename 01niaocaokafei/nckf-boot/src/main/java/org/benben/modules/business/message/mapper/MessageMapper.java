package org.benben.modules.business.message.mapper;

import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.message.entity.Message;

/**
 * @Description: 消息
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
public interface MessageMapper extends BaseMapper<Message> {
    @Select("SELECT COUNT(*) FROM `user_message_send` where read_flag='0' and user_id=#{userId}")
    int queryCount(String userId);
}
