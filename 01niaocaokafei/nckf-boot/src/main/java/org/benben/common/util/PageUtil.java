package org.benben.common.util;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;


public class PageUtil {

    /**
     * 开始分页
     *
     * @param list
     * @param pageNo  页码
     * @param pageSize 每页多少条数据
     * @return
     */
    public static List startPage(List list, Integer pageNo, Integer pageSize) {
        if(list == null){
            return null;
        }
        if(list.size() == 0){
            return null;
        }

        Integer count = list.size(); //记录总数
        Integer pageCount = 0; //页数
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }

        int start = 0; //开始索引
        int end = 0; //结束索引

        if (pageNo != pageCount) {
            start = (pageNo - 1) * pageSize;
            end = start + pageSize;
        } else {
            start = (pageNo - 1) * pageSize;
            end = count;
        }
        try{
            List pageList = list.subList(start, end);
            return pageList;
        }catch (Exception e){
            e.getMessage();
            return null;
        }

    }

}

