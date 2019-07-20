package org.benben.modules.business.systemconfig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.benben.common.api.vo.Result;
import org.benben.common.util.DateUtils;
import org.benben.modules.business.systemconfig.entity.SystemConfig;
import org.benben.modules.business.systemconfig.mapper.SystemConfigMapper;
import org.benben.modules.business.systemconfig.service.ISystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 系统配置
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements ISystemConfigService {

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Override
    public String queryByTime(Date date) throws Exception{
        QueryWrapper<SystemConfig> systemConfigWrapper = new QueryWrapper<>();
        SimpleDateFormat sim = new SimpleDateFormat("HH:mm");
        String dateformat = sim.format(date);
        String words = null;


        if(date.after(sim.parse("3:59"))&&date.before(sim.parse("7:01"))){
            systemConfigWrapper.eq("config_name","time_morning");
            SystemConfig systemConfigone = systemConfigMapper.selectOne(systemConfigWrapper);
            if(systemConfigone != null){
                words=(String)systemConfigone.getConfigValue();
            }


        }
        if(date.after(sim.parse("7:00"))&&date.before(sim.parse("12:01"))){
            systemConfigWrapper.eq("config_name","time_noon");
            SystemConfig systemConfigone = systemConfigMapper.selectOne(systemConfigWrapper);
            if(systemConfigone != null){
                words=(String)systemConfigone.getConfigValue();
            }


        }


        if(date.after(sim.parse("12:00"))&&date.before(sim.parse("13:01"))){
            systemConfigWrapper.eq("config_name","time_midday");
            SystemConfig systemConfigone = systemConfigMapper.selectOne(systemConfigWrapper);
            if(systemConfigone != null){
                words=(String)systemConfigone.getConfigValue();
            }

        }
        if(date.after(sim.parse("13:00"))&&date.before(sim.parse("18:01"))){
            systemConfigWrapper.eq("config_name","time_afternoon");
            SystemConfig systemConfigone = systemConfigMapper.selectOne(systemConfigWrapper);
            if(systemConfigone != null){
                words=(String)systemConfigone.getConfigValue();
            }


        }
        if(date.after(sim.parse("18:00"))&&date.before(sim.parse("24:00"))||date.after(sim.parse("0:00"))&&date.before(sim.parse("4:00"))||"0:00".equals(dateformat)){
            systemConfigWrapper.eq("config_name","time_night");
            SystemConfig systemConfigone = systemConfigMapper.selectOne(systemConfigWrapper);
            if(systemConfigone != null){
                words=(String)systemConfigone.getConfigValue();
            }
        }
        return words;
    }


    @Override
    public String queryWord() {
        String word = "";
        QueryWrapper<SystemConfig> systemConfigWrapper = new QueryWrapper<>();
        systemConfigWrapper.eq("config_name","shouye_word");
        SystemConfig systemConfigone = systemConfigMapper.selectOne(systemConfigWrapper);
        if(systemConfigone != null){
            word=(String)systemConfigone.getConfigValue();
        }
        return word;
    }
}
