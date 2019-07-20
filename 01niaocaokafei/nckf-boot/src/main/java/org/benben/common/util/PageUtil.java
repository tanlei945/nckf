package org.benben.common.util;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

public class PageUtil {
    public static List page(List beforeList,int pageNo,int pageSize){

        int totalCount=beforeList.size();
        int pageCount = (totalCount / pageSize) + ((totalCount % pageNo > 0) ? 1 : 0);

        int start=(pageNo-1) * pageSize;
        int end = pageNo==pageCount ? totalCount : pageNo * pageSize;

        List updateList=beforeList.subList(start,end);
        return updateList;
    }
}
