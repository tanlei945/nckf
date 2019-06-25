package org.benben.modules.business.userstore.service;

import org.benben.modules.business.userstore.entity.UserStore;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 骑手信息
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
public interface IUserStoreService extends IService<UserStore> {

	UserStore queryByUserId(String userId);

}
