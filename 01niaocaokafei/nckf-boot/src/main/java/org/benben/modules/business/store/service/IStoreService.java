package org.benben.modules.business.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.store.entity.Store;

import java.util.List;

/**
 * @Description: 店面
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
public interface IStoreService extends IService<Store> {

    List<Store> queryByDistance(double longitude, double latitude);

    Boolean queryScopeById(String id,double lng,double lat);
}
