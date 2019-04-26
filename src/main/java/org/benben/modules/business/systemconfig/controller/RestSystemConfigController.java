package org.benben.modules.business.systemconfig.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.systemconfig.service.ISystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
@RequestMapping("/api/systemConfig")
@Slf4j
@Api(tags = "首页文字接口")
public class RestSystemConfigController {

    @Autowired
    private ISystemConfigService systemConfigService;


    @GetMapping(value = "/query_by_time")
    @ApiOperation(value = "首页文字", notes = "首页文字",tags = "首页文字接口")
    public RestResponseBean queryByTime()  {
        Date date =new Date();
        //得到当前时间
        SimpleDateFormat sdf =new SimpleDateFormat("HH:mm");
        try {
            Date parse = sdf.parse(sdf.format(date));
            //根据当前时间返回不同的文字内容
            String words = systemConfigService.queryByTime(parse);
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),words);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }
}
