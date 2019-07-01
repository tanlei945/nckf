package org.benben.modules.business.account.service;

import org.benben.modules.business.account.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 钱包表
 * @author： jeecg-boot
 * @date：   2019-04-24
 * @version： V1.0
 */
public interface IAccountService extends IService<Account> {

    public boolean isPayPassword(String userId);

    public boolean isWithdrawAccount(String userId);

    public boolean setWithdrawAccount(String userId,String accountType,String accountNo);

    public boolean resetPayPassword(String userId,String payPassword);

    public Account queryByUserId(String userId);

	Account getByUid(String id);

}
