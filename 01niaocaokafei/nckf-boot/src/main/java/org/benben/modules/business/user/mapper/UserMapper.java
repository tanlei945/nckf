package org.benben.modules.business.user.mapper;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.benben.modules.business.user.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 普通用户
 * @author： jeecg-boot
 * @date：   2019-04-20
 * @version： V1.0
 */
public interface UserMapper extends BaseMapper<User> {

    @Update("update user set work_flag=#{status} where id=#{userId}")
    void changeWorkStatus(@Param("status") String status, @Param("userId") String userId);
}
