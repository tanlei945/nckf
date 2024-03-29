package org.benben.modules.business.cart.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;
@Data
public class ListCartVo {
    private java.lang.String id;
    /**用户id*/
    @Excel(name = "用户id", width = 15)
    private java.lang.String userId;
    /**是否选中*/
    @Excel(name = "是否选中", width = 15)
    private java.lang.String checkedFlag;
    /**商品id*/
    @Excel(name = "商品id", width = 15)
    private java.lang.String goodsId;
    /**商户id*/
    @Excel(name = "商户id", width = 15)
    private java.lang.String storeId;
    /**商品规格*/
    @Excel(name = "商品规格", width = 15)
    private java.lang.String goodstSpecid;
    /**商品数量*/
    @Excel(name = "商品数量", width = 15)
    private java.lang.Integer goodsCount;
    /**商品价格*/
    @Excel(name = "商品价格", width = 15)
    private double price;
    /**创建者*/
    @Excel(name = "创建者", width = 15)
    private java.lang.String createBy;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;
    /**更新人*/
    @Excel(name = "更新人", width = 15)
    private java.lang.String updateBy;
    /**更新时间*/
    @Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateTime;

    public double getPrice() {
        this.price=this.getGoodsCount()*this.price;
        return this.price;
    }
}
