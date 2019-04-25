package org.benben.modules.business.commen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.util.aliyun.OSSClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: WangHao
 * @date: 2019/4/12 14:00
 * @description:
 */
@RestController
@RequestMapping("/api/common/")
@Api(tags = {"通用接口"})
public class RestCommonController {

    @Value(value = "${benben.path.upload}")
    private String uploadpath;

    @PostMapping(value = "/upload_image")
    @ApiOperation(value = "上传图片",tags = {"通用接口"}, notes = "上传图片")
    public RestResponseBean uploadImage(HttpServletRequest request) {

        try {
            String ctxPath = uploadpath;
            String fileName = null;
            String bizPath = "user";
            String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile mf = multipartRequest.getFile("file");// 获取上传文件对象
            String orgName = mf.getOriginalFilename();// 获取文件名
            fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
            String savePath = file.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), savefile);
            String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
            if (dbpath.contains("\\")) {
                dbpath = dbpath.replace("\\", "/");
            }
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),dbpath);
        } catch (IOException e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),e.getMessage());
        }

    }


    @PostMapping("/upload")
    @ApiOperation(value = "上传文件",tags = {"通用接口"}, notes = "上传文件")
    public RestResponseBean upload(@RequestParam(value = "file") MultipartFile[] files) {

        String result = "";

        try {
            result = OSSClientUtils.fileUpload(files);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), result);
    }

//    /**
//     * 上传图片
//     *
//     * @param files
//     * @return
//     * @throws Exception
//     */
//    @PostMapping(value = "/image_upload")
//    @ApiOperation(value = "上传图片",tags = {"通用接口"}, notes = "上传图片")
//    public Result<Object> imageUpload(@RequestParam(value = "file") MultipartFile[] files) throws Exception{
//        Result<Object> result = new Result<Object>();
//
//        if (files.length < 1) {
//            return Result.error(401,"上传失败");
//        }
//
//        return result.ok(OSSClientUtils.fileUpload(files));
//    }

    /**
     * 上传视频
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/file_upload")
    @ApiOperation(value = "上传视频",tags = {"通用接口"}, notes = "上传视频")
    public Result<Object> fileUpload(@RequestParam(value = "file") MultipartFile file) throws IOException {
        Result<Object> result = new Result<Object>();

        if (StringUtils.isEmpty(file.getOriginalFilename())) {
            result.error500("上传失败!");
        }

        InputStream fileContent = file.getInputStream();
        String fileName = file.getOriginalFilename();
        String url = OSSClientUtils.uploadFile(fileContent, fileName);
        String[] strarray = {fileName, url};

        return result.ok(strarray);
    }
}
