package org.benben.modules.business.address.service;

import org.benben.modules.business.address.entity.Address;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.address.vo.AddressVO;
import org.benben.modules.business.rideraddress.entity.RiderAddress;

import java.util.List;

/**
 * @Description: 用户地址
 * @author： jeecg-boot
 * @date： 2019-04-23
 * @version： V1.0
 */
public interface IAddressService extends IService<Address> {

    public Address queryAddress(String userId);

    public RiderAddress queryRiderAddress(String riderId);

    public String queryDistance(Double lng, Double lat, Double lng2, Double lat2);

    public List<Address> selectByMainId(String mainId);

    public boolean editDefaultAddress(String userId,String id);

	Boolean save(AddressVO addressVO,String userId);
}
