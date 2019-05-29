package org.benben.modules.business.address.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.benben.modules.business.address.entity.Address;
import org.benben.modules.business.address.mapper.AddressMapper;
import org.benben.modules.business.address.service.IAddressService;
import org.benben.modules.business.address.vo.AddressVO;
import org.benben.modules.business.rideraddress.entity.RiderAddress;
import org.benben.modules.business.rideraddress.mapper.RiderAddressMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 用户地址
 * @author： jeecg-boot
 * @date： 2019-04-23
 * @version： V1.0
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private RiderAddressMapper riderAddressMapper;

    /**
     * 根据userId查询所有地址
     *
     * @param mainId
     * @return
     */
    @Override
    public List<Address> selectByMainId(String mainId) {
        return addressMapper.selectByMainId(mainId);
    }


    /**
     * 根据userId查询默认地址
     *
     * @param userId
     * @return
     */
    @Override
    public Address queryAddress(String userId) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.and(wrapper -> wrapper.eq("default_flag", "1"));
        Address address = addressMapper.selectOne(queryWrapper);

        return address;
    }

    @Override
    public RiderAddress queryRiderAddress(String riderId) {
        QueryWrapper<RiderAddress> riderAddressQueryWrapper = new QueryWrapper<>();
        riderAddressQueryWrapper.eq("rider_id",riderId);
        RiderAddress riderAddress = riderAddressMapper.selectOne(riderAddressQueryWrapper);

        return riderAddress;
    }

    /**
     * 修改默认地址
     * @param userId 用户ID
     * @param id 地址ID
     * @return
     */
    @Override
    @Transactional
    public boolean editDefaultAddress(String userId, String id) {

        Address beforeAddress = this.queryAddress(userId);
        Address afterAddress = addressMapper.selectById(id);

        if(beforeAddress == null || afterAddress == null){
            return false;
        }

        beforeAddress.setDefaultFlag("0");
        afterAddress.setDefaultFlag("1");
        addressMapper.updateById(beforeAddress);
        addressMapper.updateById(afterAddress);

        return true;
    }

	/**
	 * 保存地址
	 * @param addressVO
	 * @return
	 */
	@Override
	public Boolean save(AddressVO addressVO,String userId) {

		Address address = new Address();
		address.setUserId(userId);
		BeanUtils.copyProperties(addressVO,address);

		if(addressMapper.insert(address) == 0){
			return false;
		}
		return true;
	}

	@Override
    public String queryDistance(Double lng1, Double lat1, Double lng2, Double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(
                Math.sqrt(
                        Math.pow(Math.sin(a / 2), 2)
                                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)
                )
        );
        s = s * 6378137;
        s = Math.round(s * 10000) / 10000;
        String distance = String.valueOf(s);
        return distance;
    }

    private double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
