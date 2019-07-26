package org.benben.modules.business.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qq.connect.QQConnectException;
import com.qq.connect.utils.QQConnectConfig;
import org.benben.common.constant.CommonConstant;
import org.benben.common.util.PasswordUtil;
import org.benben.common.util.RedisUtil;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.account.entity.Account;
import org.benben.modules.business.account.mapper.AccountMapper;
import org.benben.modules.business.store.entity.Store;
import org.benben.modules.business.store.service.IStoreService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.entity.UserThird;
import org.benben.modules.business.user.mapper.UserThirdMapper;
import org.benben.modules.business.user.mapper.UserMapper;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.user.vo.UserStoreVo;
import org.benben.modules.business.user.vo.UserVo;
import org.benben.modules.business.usercoupons.entity.UserCoupons;
import org.benben.modules.business.usercoupons.mapper.UserCouponsMapper;
import org.benben.modules.business.userstore.entity.UserStore;
import org.benben.modules.business.userstore.mapper.UserStoreMapper;
import org.benben.modules.shiro.authc.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * @Description: 普通用户
 * @author： jeecg-boot
 * @date：   2019-04-20
 * @version： V1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserThirdMapper userThirdMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserCouponsMapper userCouponsMapper;
    @Autowired
    private UserStoreMapper userStoreMapper;
    @Autowired
	private IStoreService storeService;
    @Autowired
    private RedisUtil redisUtil;

//	@Override
//	@Transactional
//	public void saveMain(User user, List<UserThird> userThirdList) {
//		userMapper.insert(user);
//		for(UserThird entity:userThirdList) {
//			//外键设置
//			entity.setUserId(user.getId());
//			userThirdMapper.insert(entity);
//		}
//	}
//
//	@Override
//	@Transactional
//	public void updateMain(User user,List<UserThird> userThirdList) {
//		userMapper.updateById(user);
//
//		//1.先删除子表数据
//		userThirdMapper.deleteByMainId(user.getId());
//
//		//2.子表数据重新插入
//		for(UserThird entity:userThirdList) {
//			//外键设置
//			entity.setUserId(user.getId());
//			userThirdMapper.insert(entity);
//		}
//	}

//    @Override
//    @Transactional
//    public void delMain(String id) {
//        userMapper.deleteById(id);
//        userAddressMapper.deleteByMainId(id);
//    }
//
//    @Override
//    @Transactional
//    public void delBatchMain(Collection<? extends Serializable> idList) {
//        for(Serializable id:idList) {
//            userMapper.deleteById(id);
//            userAddressMapper.deleteByMainId(id.toString());
//        }
//    }

    @Override
    @Transactional
    public void delMain(String id) {
        userMapper.deleteById(id);
        userThirdMapper.deleteByMainId(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for(Serializable id:idList) {
            userMapper.deleteById(id);
            userThirdMapper.deleteByMainId(id.toString());
        }
    }

    @Override
    public User queryByUsername(String username) {
        QueryWrapper<User> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("username", username);
        User user = userMapper.selectOne(userInfoQueryWrapper);
        return user;
    }

    @Override
    public User queryByMobile(String moblie) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(User::getMobile,moblie)
				.eq(User::getUserType,"0");

        return userMapper.selectOne(queryWrapper);
    }

	@Override
	public User queryByMobileAndUserType(String moblie,String userType) {

		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("mobile", moblie);
		queryWrapper.eq("user_type",userType);

		return userMapper.selectOne(queryWrapper);
	}


	@Override
	public User queryByMobileAndUserId(String mobile, String userId) {

    	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
				.eq(User::getMobile,mobile)
				.eq(User::getId,userId);

		return userMapper.selectOne(queryWrapper);
	}

	/**
     * 绑定三方信息
     * @param openId 识别
     * @param userId 用户ID
     * @param type 类型  0/QQ,1/微信,2/微博
     * @return
     */
    @Override
    @Transactional
    public int bindingThird(String openId, String userId, String type) {

        UserThird userThird = new UserThird();
        userThird.setUserId(userId);
        userThird.setOpenId(openId);
        userThird.setType(type);

        return userThirdMapper.insert(userThird);
    }


    /**
     * 忘记密码
     * @param mobile
     * @param password
     * @return
     */
    @Override
    @Transactional
    public int changePassword(String mobile, String password,String userType) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        queryWrapper.eq("user_type",userType);

        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            return 0;
        }

        String salt = oConvertUtils.randomGen(8);
        user.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(password, password, salt);

        if (passwordEncode.equals(user.getPassword())){
        	return -1;
		}

        user.setPassword(passwordEncode);

		int i = userMapper.updateById(user);

		if(i != 0){
			//刷新用户信息到缓存中
			redisUtil.set(CommonConstant.SIGN_PHONE_USER + user.getId(),user, JwtUtil.APP_EXPIRE_TIME / 1000);
		}

		return i;
    }



	/**
	 * 忘记密码
	 * @param mobile
	 * @param password
	 * @return
	 */
	@Override
	@Transactional
	public int forgetPassword(String mobile, String password,String userType) {

		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("mobile",mobile);
		queryWrapper.eq("user_type",userType);

		User user = userMapper.selectOne(queryWrapper);
		if(user == null){
			return 0;
		}

		String salt = oConvertUtils.randomGen(8);
		user.setSalt(salt);
		String passwordEncode = PasswordUtil.encrypt(password, password, salt);
		user.setPassword(passwordEncode);

		int i = userMapper.updateById(user);

		if(i != 0){
			//刷新用户信息到缓存中
			redisUtil.set(CommonConstant.SIGN_PHONE_USER + user.getId(),user, JwtUtil.APP_EXPIRE_TIME / 1000);
		}

		return i;
	}



	/**
	 * 用户详情
	 * @param user
	 * @return
	 */

	@Autowired
	private  IUserService userService;
	@Override
	public UserVo queryUserVo(User user) {

		UserVo userVo = new UserVo();

		Account account = accountMapper.queryByUserId(user.getId());
		List<UserCoupons> list = userCouponsMapper.queryByUserId(user.getId());
		BeanUtils.copyProperties(user,userVo);
		user = userService.getById(user.getId());
		userVo.setWorkFlag(user.getWorkFlag());
		Store store = storeService.getById(user.getStoreId());
		if(store != null){
			userVo.setStoreName(store.getStoreName());
		}

		//userVo.setWorkFlag("0");
		userVo.setMoney(account.getMoney());
		userVo.setCouponsNumber(list.size());

		return userVo;
	}

	/**
	 * 用户注册
	 * @param mobile
	 * @param password
	 * @return
	 */
	@Override
	@Transactional
	public User userRegister(String mobile, String password) {

		User user = new User();
		Account account = new Account();

		//保存用户信息
		user.setMobile(mobile);
		user.setUserType("0");
		String salt = oConvertUtils.randomGen(8);
		user.setSalt(salt);
		String passwordEncode = PasswordUtil.encrypt(password, password, salt);
		user.setPassword(passwordEncode);
		userMapper.insert(user);

		//生成钱包
		account.setUserId(user.getId());
		accountMapper.insert(account);

		return user;
	}

	/**
	 * 骑手注册
	 * @param userStoreVo
	 * @return
	 */
	@Override
	@Transactional
	public User riderRegister(UserStoreVo userStoreVo) {

		User user = new User();
		UserStore userStore = new UserStore();
		Account account = new Account();

		BeanUtils.copyProperties(userStoreVo,user);
		BeanUtils.copyProperties(userStoreVo,userStore);
		String idCard = userStoreVo.getIdCard();
		idCard = idCard.substring(idCard.length() - 6);

		user.setUserType("1");
		String salt = oConvertUtils.randomGen(8);
		user.setSalt(salt);
		String passwordEncode = PasswordUtil.encrypt(idCard, idCard, salt);
		user.setPassword(passwordEncode);
		userMapper.insert(user);
		// 保存骑手信息
		userStore.setUserId(user.getId());
		userStore.setCompleteFlag("0");
		userStoreMapper.insert(userStore);
		// 生成账单
		account.setUserId(user.getId());
		accountMapper.insert(account);

		return user;
	}

	public String getAuthorizeURL(String response_type, String state, String scope) throws QQConnectException {
        return QQConnectConfig.getValue("authorizeURL").trim() + "?client_id=" + QQConnectConfig.getValue("app_ID").trim() + "&redirect_uri=" + QQConnectConfig.getValue("redirect_URI").trim() + "&response_type=" + response_type + "&state=" + state + "&scope=" + scope;
    }

	@Override
	public Boolean changeWorkStatus(String status,String userId) {
		try{
			userMapper.changeWorkStatus(status,userId);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
