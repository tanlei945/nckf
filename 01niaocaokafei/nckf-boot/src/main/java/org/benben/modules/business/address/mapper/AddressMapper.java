package org.benben.modules.business.address.mapper;

import java.util.List;

import org.benben.modules.business.address.entity.Address;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 用户地址
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
public interface AddressMapper extends BaseMapper<Address> {

    public boolean deleteByMainId(String mainId);

    public List<Address> selectByMainId(String mainId);

}
