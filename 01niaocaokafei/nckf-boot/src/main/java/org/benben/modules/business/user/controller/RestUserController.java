package org.benben.modules.business.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.common.util.PasswordUtil;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.commen.service.*;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Title: RestUserController
 * @Description: 用户
 * @author： jeecg-boot
 * @date： 2019-04-19
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = {"用户接口"})
@Slf4j
public class RestUserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private ICommonService commonService;


    /**
     * 通过id查询
     * @param id
     * @return
     */
    @GetMapping(value = "/query_by_id")
    @ApiOperation(value = "通过id查询用户", tags = {"用户接口"}, notes = "通过id查询用户")
    @ApiImplicitParam(name = "id",value = "用户密码",dataType = "String",defaultValue = "1",required = true)
    public RestResponseBean queryById(@RequestParam(name="id",required=true) String id) {

        User user = userService.getById(id);

        if(user==null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),user);
    }


    /**
     * 用户修改
     * @param user
     * @return
     */
    @PostMapping(value = "/edit")
    @ApiOperation(value = "用户修改", tags = {"用户接口"}, notes = "用户修改")
    public RestResponseBean edit(@RequestBody User user) {

        User userEntity = userService.getById(user.getId());

        if(userEntity==null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }else {

            if(StringUtils.isNotBlank(user.getMobile())&&StringUtils.isNotBlank(user.getPassword())){
                String salt = oConvertUtils.randomGen(8);
                user.setSalt(salt);
                String passwordEncode = PasswordUtil.encrypt(user.getMobile(), user.getPassword(), salt);
                user.setPassword(passwordEncode);
            }

            boolean ok = userService.updateById(user);

            if(ok) {
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),user);
            }
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),user);
    }

    @PostMapping(value = "/change_avatar")
    @ApiOperation(value = "修改头像", tags = {"用户接口"}, notes = "修改头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户的ID",dataType = "String",defaultValue = "1",required = true),
            @ApiImplicitParam(name = "file",value = "待上传图片",dataType = "MultipartFile",required = true)
    })
    public RestResponseBean changeAvatar(@RequestParam String userId,@RequestParam(value = "file") MultipartFile file){

        if(StringUtils.isBlank(userId) || StringUtils.isBlank(file.getOriginalFilename())){
            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.getDesc(),null);
        }

        User user = userService.getById(userId);
        if(user == null){
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.desc(),null);
        }

        String avatar = commonService.localUploadImage(file);
        user.setAvatar(avatar);

        if(userService.updateById(user)){

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.desc(),null);
        }

        return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),null);
    }


    @PostMapping(value = "/change_username")
    @ApiOperation(value = "修改用户名", tags = {"用户接口"}, notes = "修改用户名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户的ID",dataType = "String",defaultValue = "1",required = true),
            @ApiImplicitParam(name = "username",value = "用户名",dataType = "String",defaultValue = "1",required = true)
    })
    public RestResponseBean changeUsername(@RequestParam String userId,@RequestParam String username){

        if(StringUtils.isBlank(userId) || StringUtils.isBlank(username)){
            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.getDesc(),null);
        }

        User user = userService.getById(userId);
        if(user == null){
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.desc(),null);
        }
        user.setUsername(username);

        if(userService.updateById(user)){

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.desc(),null);
        }

        return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),null);
    }

    /**
     * 修改手机号
     * @param userId
     * @param mobile
     * @return
     */
    @PostMapping(value = "/change_mobile")
    @ApiOperation(value = "修改手机号", tags = {"用户接口"}, notes = "修改手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户的ID",dataType = "String",defaultValue = "1",required = true),
            @ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String",defaultValue = "1",required = true),
            @ApiImplicitParam(name = "password",value = "用户密码",dataType = "String",defaultValue = "1",required = true)
    })
    public RestResponseBean changeMobile(@RequestParam String userId,@RequestParam String mobile,@RequestParam String password) {

        if(StringUtils.isBlank(userId) || StringUtils.isBlank(mobile)){

            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.desc(),null);
        }

        User user = userService.getById(userId);
        if(user == null){
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.desc(),null);
        }
        user.setMobile(mobile);
        //TODO 密码生成依据手机号，修改手机号 ，必须重新设置密码
        String salt = oConvertUtils.randomGen(8);
        user.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(mobile, password, salt);
        user.setPassword(passwordEncode);

        if(userService.updateById(user)){

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.desc(),null);
        }

        return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),null);
    }


    @PostMapping(value = "/forget_password")
    @ApiOperation(value = "忘记密码/修改密码", tags = {"用户接口"}, notes = "忘记密码/修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String",defaultValue = "1",required = true),
            @ApiImplicitParam(name = "password",value = "用户密码",dataType = "String",defaultValue = "1",required = true)
    })
    public RestResponseBean forgetPassword(@RequestParam String mobile,@RequestParam String password){

        if(StringUtils.equals(mobile,"")||StringUtils.equals(password,"")){
            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(), null);
        }

        if(userService.forgetPassword(mobile,password) == 0){
            return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
    }

    /**
     * 根据姓名查找
     *
     * @param username
     * @return
     */
    @GetMapping(value = "/queryByName")
    public User queryByName(@RequestParam String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User user = userService.getOne(userQueryWrapper);
        return user;
    }

}
