package org.benben.modules.business.withdraw.service.impl;

import org.benben.common.util.DoubleUtil;
import org.benben.modules.business.account.entity.Account;
import org.benben.modules.business.account.mapper.AccountMapper;
import org.benben.modules.business.account.service.IAccountService;
import org.benben.modules.business.withdraw.entity.Withdraw;
import org.benben.modules.business.withdraw.mapper.WithdrawMapper;
import org.benben.modules.business.withdraw.service.IWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 提现
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
@Service
public class WithdrawServiceImpl extends ServiceImpl<WithdrawMapper, Withdraw> implements IWithdrawService {

    @Autowired
    private IAccountService accountService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private WithdrawMapper withdrawMapper;

    /**
     * 骑手提现
     * @param userId
     * @param money
     * @return
     */
    @Override
    public boolean withdrawApply(String userId, double money) {

        Account account = accountService.queryByUserId(userId);
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
