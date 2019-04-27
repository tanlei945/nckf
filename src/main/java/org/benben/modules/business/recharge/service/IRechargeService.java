package org.benben.modules.business.recharge.service;

import org.benben.modules.business.recharge.entity.Recharge;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 充值
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
public interface IRechargeService extends IService<Recharge> {

    public Recharge recharge(String userId, double money,String type);

    public boolean rechargeReturn(String rechargeId,String orderNo);

    public int rechargeFail(String rechargeId);

}
