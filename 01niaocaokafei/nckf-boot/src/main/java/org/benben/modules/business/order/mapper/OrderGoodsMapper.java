package org.benben.modules.business.order.mapper;

import java.util.List;
import org.benben.modules.business.order.entity.OrderGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 订单详情
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
public interface OrderGoodsMapper extends BaseMapper<OrderGoods> {

	public boolean deleteByMainId(String mainId);
    
	public List<OrderGoods> selectByMainId(String mainId);
}
