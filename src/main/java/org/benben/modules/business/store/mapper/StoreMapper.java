package org.benben.modules.business.store.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.store.entity.Store;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.user.entity.User;

/**
 * @Description: 店面
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
public interface StoreMapper extends BaseMapper<Store> {

    @Select("select * from bb_store")
   List<Store> queryByDistance();

    @Select("select store_scope from bb_store where id = #{id}")
    String queryScopeById(String id);
}
