package org.benben.modules.business.systemconfig.service;

import org.benben.common.api.vo.Result;
import org.benben.modules.business.systemconfig.entity.SystemConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * @Description: 系统配置
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
public interface ISystemConfigService extends IService<SystemConfig> {
    public String queryByTime(Date date)throws Exception;

}
