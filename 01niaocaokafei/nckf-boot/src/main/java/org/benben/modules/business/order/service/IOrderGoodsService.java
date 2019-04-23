package org.benben.modules.business.order.service;

import org.benben.modules.business.order.entity.OrderGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 订单详情
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
public interface IOrderGoodsService extends IService<OrderGoods> {

	public List<OrderGoods> selectByMainId(String mainId);
}
