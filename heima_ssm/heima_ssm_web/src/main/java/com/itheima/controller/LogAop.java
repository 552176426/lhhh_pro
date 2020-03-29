package com.itheima.controller;

import com.itheima.domain.SysLog;
import com.itheima.service.SysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class LogAop {


    private Date visitTime;
    private Class clazz;
    private Method method;

    @Autowired
    //此处需要在web.xml配置RequestContextListener
    private HttpServletRequest request;

    @Autowired
    private SysLogService sysLogService;


    @Before("execution(* com.itheima.controller.*.*(..))")
    public void DoBefore(JoinPoint jp) throws NoSuchMethodException {
        //访问时间
        visitTime = new Date();

        //访问类
        clazz = jp.getTarget().getClass();

        //获取方法名称
        String methodName = jp.getSignature().getName();
        //method
        Object[] args = jp.getArgs();
        if(args!=null && args.length!=0){
            Class[] classArgs = new Class[args.length];
            for(int i = 0;i<args.length;i++){
                classArgs[i] = args[i].getClass();
            }
            method = clazz.getMethod(methodName,classArgs);
        } else {
            method = clazz.getMethod(methodName);
        }
    }

    @After("execution(* com.itheima.controller.*.*(..))")
    public void DoAfter(JoinPoint jp) throws Exception {
        //执行时间
        Date endDate = new Date();
        long time = endDate.getTime()-visitTime.getTime();

        //url
        String url = "";
        if(clazz!=null && method!=null && clazz!=LogAop.class){
            RequestMapping classAnnotation =(RequestMapping) clazz.getAnnotation(RequestMapping.class);
            if(classAnnotation!=null){
                String url1 = classAnnotation.value()[0];
                RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
                if(methodAnnotation!=null){
                    String url2 = methodAnnotation.value()[0];
                    url=url1+url2;

                    //ip
                    String ip = request.getRemoteAddr();

                    //username
                    SecurityContext context = SecurityContextHolder.getContext();
                    User user =(User) context.getAuthentication().getPrincipal();
                    String username = user.getUsername();

                    SysLog sysLog = new SysLog();
                    sysLog.setVisitTime(visitTime);
                    sysLog.setUsername(username);
                    sysLog.setIp(ip);
                    sysLog.setUrl(url);
                    sysLog.setExecutionTime(time);
                    sysLog.setMethod("[类名]"+clazz.getName()+"[方法]"+method.getName());
                    sysLogService.save(sysLog);

                }
            }



        }


        //username

        //ip



    }


}
