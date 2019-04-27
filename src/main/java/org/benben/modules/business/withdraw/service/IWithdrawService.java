package org.benben.modules.business.withdraw.service;

import org.benben.modules.business.withdraw.entity.Withdraw;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 提现
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
public interface IWithdrawService extends IService<Withdraw> {

    public boolean withdrawApply(String userId, double money);

}
