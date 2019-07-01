package org.benben.modules.business.account.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.benben.common.util.PasswordUtil;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.account.entity.Account;
import org.benben.modules.business.account.mapper.AccountMapper;
import org.benben.modules.business.account.service.IAccountService;
import org.benben.modules.business.accountbill.mapper.AccountBillMapper;
import org.benben.modules.business.recharge.mapper.RechargeMapper;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.mapper.UserMapper;
import org.benben.modules.business.withdraw.mapper.WithdrawMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 钱包/账户表
 * @author： jeecg-boot
 * @date：   2019-04-24
 * @version： V1.0
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserMapper userMapper;


    /**
     * 根据用户ID查询钱包/账户信息
     * @param userId
     * @return
     */
    @Override
    public Account queryByUserId(String userId){

        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        Account account = accountMapper.selectOne(queryWrapper);

        return account;
    }

	@Override
	public Account getByUid(String uid) {
		return accountMapper.queryByUserId(uid);
	}


	/**
     * 用户是否设置支付密码
     * @param userId
     * @return
     */
    @Override
    public boolean isPayPassword(String userId) {

        Account account = this.queryByUserId(userId);

        if(StringUtils.isBlank(account.getPayPassword())){
            return false;
        }

        return true;
    }

    /**
     * 骑手是否设置收款账户
     * @param userId
     * @return
     */
    @Override
    public boolean isWithdrawAccount(String userId) {

        Account account = this.queryByUserId(userId);

        if(StringUtils.isBlank(account.getAccountNo())){
            return false;
        }

        return true;
    }

	/**
	 * 骑手设置收款账户
	 * @param accountType
	 * @param accountNo
	 * @return
	 */
	@Override
	public boolean setWithdrawAccount(String userId, String accountType, String accountNo) {

		Account account = this.queryByUserId(userId);

		account.setAccountType(accountType);
		account.setAccountNo(accountNo);
		int result = accountMapper.updateById(account);

		if(result == 0){
			return true;
		}

		return false;
	}

	/**
     * 重置支付密码
     * @param userId
     * @return
     */
    @Override
	@Transactional
    public boolean resetPayPassword(String userId,String payPassword) {

        Account account = this.queryByUserId(userId);
        User user = userMapper.selectById(userId);

        if(account == null || user == null){
            return false;
        }

        String salt = oConvertUtils.randomGen(8);
        account.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(user.getMobile(), payPassword, salt);
        account.setPayPassword(passwordEncode);
        int result = accountMapper.updateById(account);

        if(result == 0){
            return false;
        }

        return true;
    }

}
