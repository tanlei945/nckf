package org.benben.modules.business.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.user.entity.UserThird;

import java.util.List;

/**
 * @Description: 用户三方关联
 * @author： jeecg-boot
 * @date：   2019-04-20
 * @version： V1.0
 */
public interface UserThirdMapper extends BaseMapper<UserThird> {

	public boolean deleteByMainId(String mainId);

	public List<UserThird> selectByMainId(String mainId);

}
