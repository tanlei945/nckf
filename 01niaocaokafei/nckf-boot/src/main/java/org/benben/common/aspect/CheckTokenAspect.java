package org.benben.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.shiro.LoginUser;
import org.springframework.stereotype.Component;

/**
 * @author: YonggangZhao
 * @Description:
 * @Date: Created in 18:03 2019/5/20
 * @Modified By:
 */
@Aspect
@Component
@Slf4j
public class CheckTokenAspect {

    @Pointcut("@annotation(org.benben.common.aspect.annotation.CheckToken)")
    public void checkToken() {

    }

    @Around("checkToken()")
    public RestResponseBean check(ProceedingJoinPoint point) throws Throwable {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        RestResponseBean restResponseBean = (RestResponseBean)point.proceed();
        log.info("这是切面类------------的restResponseBean"+restResponseBean);
        return  restResponseBean;
    }
}
