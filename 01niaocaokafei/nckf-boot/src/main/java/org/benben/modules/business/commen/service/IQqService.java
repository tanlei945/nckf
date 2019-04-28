package org.benben.modules.business.commen.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IQqService {

    public void login( String mobile,HttpServletResponse response);

    public String backCallBack(HttpServletRequest request);

//    public String beforeCallBack(HttpServletRequest request);
}
