package org.benben.modules.business.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.user.entity.UserThird;

import java.util.List;

/**
 * @Description: 用户三方关联
 * @author： jeecg-boot
 * @date：   2019-04-20
 * @version： V1.0
 */
public interface IUserThirdService extends IService<UserThird> {

	public List<UserThird> selectByMainId(String mainId);

	public UserThird queryByOpenid(String openid);

	public UserThird queryByUserIdAndType(String userId, String type);

	public int bindOpenId(String userid,String openid,String type);

}
