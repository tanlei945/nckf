package org.benben.modules.business.banner.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.benben.common.api.vo.Result;
import org.benben.modules.business.banner.entity.Banner;
import org.benben.modules.business.banner.mapper.BannerMapper;
import org.benben.modules.business.banner.service.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 轮播图
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements IBannerService {
    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public List<String> queryImageList(QueryWrapper<Banner> queryWrapper) {
        queryWrapper.eq("del_flag","1");
        queryWrapper.and(wrapper -> wrapper.eq("use_flag", "1"));
        List<Banner> list = bannerMapper.selectList(queryWrapper);
        ArrayList<String> imageList = new ArrayList<>();
        for (Banner ban:list){

            imageList.add(ban.getImgUrl());
        }
        return imageList;
    }
}
