package org.benben.modules.business.store.service.impl;

import org.benben.common.util.DistanceUtil;
import org.benben.common.util.sortMapByValue;
import org.benben.modules.business.store.entity.Store;
import org.benben.modules.business.store.mapper.StoreMapper;
import org.benben.modules.business.store.service.IStoreService;
import org.benben.modules.business.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 店面
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements IStoreService {

    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private IStoreService storeService;
    @Override
    public List<Store> queryByDistance(double longitude,double latitude) {
        List<Store> stores = storeMapper.queryByDistance();
        ConcurrentHashMap<String, String> objectObjectConcurrentHashMap = new ConcurrentHashMap<>();
        HashMap<String, Store> objectObjectHashMap = new HashMap<>();
        stores.forEach(store -> {
            //  id  :  store对象
            objectObjectHashMap.put(store.getId(),store);
            //根据经纬度计算两者距离
            String algorithm = DistanceUtil.algorithm(store.getLng(), store.getLat(), longitude, latitude);
            objectObjectConcurrentHashMap.put(store.getId(),algorithm);
        });
        //根据map的value排序
        Map<String, String> stringStringMap = sortMapByValue.sortToList(objectObjectConcurrentHashMap,"asc");
        Iterator<Map.Entry<String, String>> iterator = stringStringMap.entrySet().iterator();
        //排序后的距离为key ：Store为value
        HashMap<String, Store> storeHashMap = new HashMap<>();
        List<Store> storeList = new LinkedList<>();
        while (iterator.hasNext()){
            Object key = iterator.next();
            Object key1 = ((Map.Entry) key).getKey();
            Object value = ((Map.Entry) key).getValue();
            Store store = objectObjectHashMap.get(key1.toString());
            store.setDistance(value.toString());
            storeList.add(store);
        }
        return storeList;
    }

    @Override
    public Boolean queryScopeById(String id,double lng,double lat) {

        Store store = storeService.getById(id);
        String distance = store.getDistance();
        String algorithm = DistanceUtil.algorithm(store.getLng(), store.getLat(), lng, lat);
        return Double.parseDouble(distance)>Double.parseDouble(algorithm);
    }
}
