package com.demo.rbac.aop;

import com.demo.rbac.entity.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 21:34 2018/10/1
 * @Modified By:
 */
//注解切面
@Aspect
//注解Bean
@Component
public class ValidAspect {
    //反射
    private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    //验证工厂
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    //可执行验证器
    private final ExecutableValidator validator = factory.getValidator().forExecutables();
    //泛型验证器
    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object[] params) {
        return validator.validateParameters(obj, method, params);
    }
    private ObjectError error;

    //声明切点
    @Pointcut("execution(public * com.example.demorbac.controller.*.*(..))")
    /**
     *
     * @Description: 无参构造函数，实现切点复用
     * @auther: 快乐水 樱桃可乐
     * @date: 9:11 2018/10/2
     * @param: []
     * @return: void
     *
     */
    public void valid() {
    }

    //环绕通知,环绕增强，相当于MethodInterceptor，选择复用的切点
    @Around("valid()")
    public Object around(ProceedingJoinPoint pjp) {
        System.out.println("方法环绕start.....");
        try {
            //取参数，如果没参数，那肯定不校验了
            Object[] objects = pjp.getArgs();
            if (objects.length == 0) {
                System.out.println("无参");
                //继续执行切点
                return pjp.proceed();
            }
            /**************************校验封装好的javabean**********************/
            //寻找带BindingResult参数的方法，然后判断是否有error，如果有则是校验不通过
            for (Object object : objects) {
                if (object instanceof BeanPropertyBindingResult) {
                    //有校验
                    BeanPropertyBindingResult result = (BeanPropertyBindingResult) object;
                    if (result.hasErrors()) {
                        System.out.println("javaBean");
                        List<ObjectError> list = result.getAllErrors();
                        for (ObjectError error : list) {
                            System.out.println(error.getCode() + "---" + error.getArguments() + "--" + error.getDefaultMessage());
                            //返回第一条校验失败信息。也可以拼接起来返回所有的
                            return new Result(false,error.getDefaultMessage());
                        }
                    }
                }
            }

            /**************************校验普通参数*************************/
            //  获得切入目标对象
            Object target = pjp.getThis();
            System.out.println("target:"+target);
            // 获得切入的方法
            Method method = ((MethodSignature) pjp.getSignature()).getMethod();
            // 执行校验，获得校验结果，(类、方法、参数集合)
            Set<ConstraintViolation<Object>> validResult = validMethodParams(target, method, objects);
            //如果有校验不通过的
            if (!validResult.isEmpty()) {
                System.out.println("基础类型");
                // 获得方法的参数名称
                String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

                for(ConstraintViolation<Object> constraintViolation : validResult) {
                    // 获得校验的参数路径信息
                    PathImpl pathImpl = (PathImpl) constraintViolation.getPropertyPath();
                    // 获得校验的参数位置
                    int paramIndex = pathImpl.getLeafNode().getParameterIndex();
                    // 获得校验的参数名称
                    String paramName = parameterNames[paramIndex];

                    System.out.println(paramName);
                    //校验信息
                    System.out.println(constraintViolation.getMessage());
                    //返回第一条
                    return new Result(false,constraintViolation.getMessage(),855);
                }
            }

            return pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

}

