package org.benben.modules.business.recharge.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.benben.common.util.DoubleUtil;
import org.benben.modules.business.account.entity.Account;
import org.benben.modules.business.account.service.IAccountService;
import org.benben.modules.business.accountbill.entity.AccountBill;
import org.benben.modules.business.accountbill.mapper.AccountBillMapper;
import org.benben.modules.business.recharge.entity.Recharge;
import org.benben.modules.business.recharge.mapper.RechargeMapper;
import org.benben.modules.business.recharge.service.IRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 充值
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
@Service
@Slf4j
public class RechargeServiceImpl extends ServiceImpl<RechargeMapper, Recharge> implements IRechargeService {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private AccountBillMapper accountBillMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    /**
     * 生成充值订单
     * @param userId 申请充值用户ID
     * @param money 操作金额
     * @param type 充值方式
     * @return
     */
    @Override
    @Transactional
    public Recharge recharge(String userId, double money, String type) {

        Recharge recharge = new Recharge();

        //生成充值记录
        recharge.setUserId(userId);
        recharge.setRechargeMoney(money);
        recharge.setStatus("2");
        recharge.setRechargeType(type);
        rechargeMapper.insert(recharge);

        return recharge;
    }

    /**
     * 处理充值成功
     * @param rechargeId 充值ID
     * @param orderNo 三方订单ID
     * @return
     */
    @Override
    @Transactional
    public boolean rechargeReturn(String rechargeId,String orderNo) {

        AccountBill accountBill = new AccountBill();

        try {
            Recharge recharge = rechargeMapper.selectById(rechargeId);
            Account account = accountService.queryByUserId(recharge.getUserId());

            //充值前金额
            double before = account.getMoney();
            //充值后金额
            double after = DoubleUtil.add(recharge.getRechargeMoney(),before);

            //支付成功修改钱包金额
            account.setMoney(after);
            accountService.updateById(account);

            //支付成功修改钱包充值信息
            recharge.setOrderNo(orderNo);
            recharge.setStatus("1");
            rechargeMapper.updateById(recharge);

            //生成账单记录
            accountBill.setUserId(recharge.getUserId());
            accountBill.setAfterMoney(before);
            accountBill.setBeforeMoney(after);
            accountBill.setSign("+");
            accountBill.setBillType("1");
            accountBillMapper.insert(accountBill);

        } catch (Exception e){
            e.getMessage();
            return false;
        }

        return true;
    }

    /**
     * 处理充值失败
     * @param rechargeId 充值ID
     * @return
     */
    @Override
    @Transactional
    public int rechargeFail(String rechargeId) {

        Recharge recharge = rechargeMapper.selectById(rechargeId);
        recharge.setStatus("0");

        return rechargeMapper.updateById(recharge);
    }




}
