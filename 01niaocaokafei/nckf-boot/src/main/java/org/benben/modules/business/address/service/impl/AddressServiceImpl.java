package org.benben.modules.business.address.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.benben.modules.business.address.entity.Address;
import org.benben.modules.business.address.mapper.AddressMapper;
import org.benben.modules.business.address.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 用户地址
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {
  @Autowired
  private AddressMapper addressMapper;

    @Override
    public Address queryAddress(String userId) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.and(wrapper -> wrapper.eq("default_flag", "1"));
        Address address = addressMapper.selectOne(queryWrapper);

        return address;
    }

    @Override
    public String queryDistance(Double lng1, Double lat1,Double lng2, Double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(
                Math.sqrt(
                        Math.pow(Math.sin(a/2),2)
                                + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)
                )
        );
        s = s * 6378137;
        s = Math.round(s * 10000) / 10000;
        String distance = String.valueOf(s);
        return distance;
    }

    private  double rad(double d){
        return d * Math.PI / 180.0;
    }
}
