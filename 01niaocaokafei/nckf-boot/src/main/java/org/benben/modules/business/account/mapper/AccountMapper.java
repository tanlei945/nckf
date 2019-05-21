package org.benben.modules.business.account.mapper;


import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.account.entity.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 钱包表
 * @author： jeecg-boot
 * @date：   2019-04-24
 * @version： V1.0
 */
public interface AccountMapper extends BaseMapper<Account> {

	@Select("select * from user_account where user_id = #{userId}")
	Account queryByUserId(String userId);

}
