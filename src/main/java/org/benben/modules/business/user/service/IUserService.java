package org.benben.modules.business.user.service;

import org.benben.modules.business.user.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.user.vo.UserStoreVo;
import org.benben.modules.business.user.vo.UserVo;

import java.io.Serializable;
import java.util.Collection;

/**
 * @Description: 普通用户
 * @author： jeecg-boot
 * @date：   2019-04-20
 * @version： V1.0
 */
public interface IUserService extends IService<User> {

//	/**
//	 * 添加一对多
//	 *
//	 */
//	public void saveMain(User user,List<UserThird> userThirdList) ;
//
//	/**
//	 * 修改一对多
//	 *
//	 */
//	public void updateMain(User user,List<UserThird> userThirdList);

//    /**
//     * 删除一对多
//     */
//    public void delMain (String id);
//
//    /**
//     * 批量删除一对多
//     */
//    public void delBatchMain (Collection<? extends Serializable> idList);

    /**
     * 删除一对多
     */
    public void delMain (String id);

    /**
     * 批量删除一对多
     */
    public void delBatchMain (Collection<? extends Serializable> idList);

    public User queryByUsername(String username);

    public User queryByMobile(String moblie);

    public User queryByMobileAndUserId(String mobile,String userId);

    public int bindingThird(String openId , String userId, String type);

    public int changePassword(String mobile, String password);

    UserVo queryUserVo(User user);

    User userRegister(String mobile,String password);

    User riderRegister(UserStoreVo userStoreVo);

    Boolean changeWorkStatus(String status,String userId);

}
