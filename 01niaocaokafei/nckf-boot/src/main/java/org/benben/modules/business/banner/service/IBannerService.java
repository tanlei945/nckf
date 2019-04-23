package org.benben.modules.business.banner.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.benben.common.api.vo.Result;
import org.benben.modules.business.banner.entity.Banner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 轮播图
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
public interface IBannerService extends IService<Banner> {

    public List<String> queryImageList(  QueryWrapper<Banner> queryWrapper);

}
