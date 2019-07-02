package org.benben.modules.business.config.service.impl;

import org.benben.modules.business.config.entity.Config;
import org.benben.modules.business.config.mapper.ConfigMapper;
import org.benben.modules.business.config.service.IConfigService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 功能向导
 * @author： jeecg-boot
 * @date：   2019-07-02
 * @version： V1.0
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {

}
