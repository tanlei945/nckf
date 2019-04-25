package org.benben.modules.business.account.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.benben.common.util.DoubleUtil;
import org.benben.modules.business.account.entity.Account;
import org.benben.modules.business.account.mapper.AccountMapper;
import org.benben.modules.business.account.service.IAccountService;
import org.benben.modules.business.accountbill.entity.AccountBill;
import org.benben.modules.business.accountbill.mapper.AccountBillMapper;
import org.benben.modules.business.recharge.entity.Recharge;
import org.benben.modules.business.recharge.mapper.RechargeMapper;
import org.benben.modules.business.withdraw.entity.Withdraw;
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
    private AccountBillMapper accountBillMapper;
    @Autowired
    private RechargeMapper rechargeMapper;
    @Autowired
    private WithdrawMapper withdrawMapper;


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


    /**
     * 用户是否设置支付密码
     * @param userId
     * @return
     */
    @Override
    public boolean isPayPassword(String userId) {

        Account account = this.queryByUserId(userId);

        if(StringUtils.equals(account.getPayPassword(),"")){
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

        if(StringUtils.equals(account.getAccountNo(),"")){
            return false;
        }

        return true;
    }

    /**
     * 重置密码
     * @param userId
     * @return
     */
    @Override
    public boolean resetPayPassword(String userId,String payPassword) {

        Account account = this.queryByUserId(userId);
        account.setPayPassword(payPassword);
        int result = accountMapper.updateById(account);

        if(result == 0){
            return false;
        }

        return true;
    }


    /**
     * 用户充值
     * @param userId 申请充值用户ID
     * @param money 操作金额
     * @param type 充值方式
     * @param state 是否充值成功
     * @param orderNo 订单号
     * @return
     */
    @Override
    @Transactional
    public boolean recharge(String userId, double money, String type, String state, String orderNo) {

        Account account = this.queryByUserId(userId);
        Recharge recharge = new Recharge();
        AccountBill accountBill = new AccountBill();
        //钱包操作
        double before = account.getMoney();
        double after = DoubleUtil.add(money,before);
        //充值成功
        if(StringUtils.equals(state,"1")){
            account.setMoney(after);
            accountMapper.updateById(account);
            //生成账单记录
            accountBill.setUserId(userId);
            accountBill.setAfterMoney(before);
            accountBill.setBeforeMoney(after);
            accountBill.setSign("+");
            accountBill.setBillType("1");
            accountBillMapper.insert(accountBill);
        }

        //生成充值记录
        recharge.setUserId(userId);
        recharge.setRechargeMoney(money);
        recharge.setStatus(state);
        recharge.setRechargeType(type);
        recharge.setOrderNo(orderNo);
        rechargeMapper.insert(recharge);

        return false;
    }

    /**
     * 骑手提现
     * @param userId
     * @param money
     * @return
     */
    @Override
    public boolean withdraw(String userId, double money) {

        Account account = this.queryByUserId(userId);
        Withdraw withdraw = new Withdraw();
        //钱包操作
        double before = account.getMoney();
        double after = DoubleUtil.sub(money,before);
        account.setMoney(after);
        int resultAccount = accountMapper.updateById(account);
        withdraw.setUserId(userId);
        withdraw.setMoney(money);
        withdraw.setStatus("0");
        withdraw.setWithdrawType(account.getAccountType());
        int resultWithdraw = withdrawMapper.insert(withdraw);

        if(resultAccount == 0 || resultWithdraw == 0){
            return false;
        }

        return true;
    }
}
