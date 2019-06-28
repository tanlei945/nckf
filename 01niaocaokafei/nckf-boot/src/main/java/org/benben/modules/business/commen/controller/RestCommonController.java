package org.benben.modules.business.commen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.util.aliyun.OSSClientUtils;
import org.benben.modules.business.commen.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author: WangHao
 * @date: 2019/4/12 14:00
 * @description:
 */
@RestController
@RequestMapping("/api/v1/common/")
@Api(tags = {"通用接口"})
public class RestCommonController {

    @Value(value = "${benben.path.upload}")
    private String uploadpath;
    @Autowired
    private ICommonService commonService;

    @PostMapping(value = "/uploadImageLocal")
    @ApiOperation(value = "上传图片到本地",tags = {"通用接口"}, notes = "上传图片到本地")
    public RestResponseBean uploadImageLocal(@RequestParam(value = "files") MultipartFile[] files) {

		String images[] = new String[files.length];

		int num = 0;

    	if(files.length == 0){
			return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.getDesc(),null);
		}

		for(MultipartFile file : files){

			String dbpath =commonService.localUploadImage(file);

			images[num] = dbpath;

			num++;
		}

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),images);

    }


    @PostMapping(value = "/uploadImageAli")
    @ApiOperation(value = "上传图片到阿里云OSS",tags = {"通用接口"}, notes = "上传图片到阿里云OSS")
    public RestResponseBean uploadImageAli(@RequestParam(value = "file") MultipartFile[] files) {

        String result = "";

        try {
            result = OSSClientUtils.fileUpload(files);
            if(StringUtils.isBlank(result)){
            	return new RestResponseBean(ResultEnum.UPLOAD_FAILURE.getValue(),ResultEnum.UPLOAD_FAILURE.getDesc(),null);
			}
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), result);
        } catch (Exception e) {
            e.printStackTrace();
			return new RestResponseBean(ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getDesc(), result);
        }

    }


    @PostMapping(value = "/uploadVideo")
    @ApiOperation(value = "上传视频",tags = {"通用接口"}, notes = "上传视频")
    public Result<Object> uploadVideo(@RequestParam(value = "file") MultipartFile file) throws IOException {
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
