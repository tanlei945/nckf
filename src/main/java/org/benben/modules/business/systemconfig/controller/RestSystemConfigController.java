package org.benben.modules.business.systemconfig.controller;

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
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/systemConfig")
@Slf4j
@Api(tags = {"首页"})
public class RestSystemConfigController {

    @Autowired
    private ISystemConfigService systemConfigService;

    /**
     * showdoc
     * @catalog 首页
     * @title 首页文字
     * @description 首页文字
     * @method GET
     * @url /nckf-boot/api/v1/systemConfig/queryByTime
     * @return {"code": 1,"data": "下午好","msg": "操作成功","time": "1561014629886"}
     * @return_param code String 响应状态
     * @return_param data String 文字信息
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 8
     */
    @GetMapping(value = "/queryByTime")
    @ApiOperation(value = "首页文字", notes = "首页文字",tags = {"首页"})
    public RestResponseBean queryByTime()  {
        Date date =new Date();
        //得到当前时间
        SimpleDateFormat sdf =new SimpleDateFormat("HH:mm");
        try {
            Date parse = sdf.parse(sdf.format(date));
            //根据当前时间返回不同的文字内容
            String words = systemConfigService.queryByTime(parse);
            String desc = systemConfigService.queryWord();

            Map<String,String> map = new HashMap<String,String>(){{
                put("word",words);
                put("desc",desc);
            }};
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),map);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }
}
