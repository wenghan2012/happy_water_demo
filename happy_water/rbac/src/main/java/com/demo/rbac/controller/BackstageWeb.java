package com.demo.rbac.controller;


import com.demo.rbac.entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 13:38 2018/9/30
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/backstage",produces = "application/json")
public class BackstageWeb {

    /**
     *
     * @Description: 请求头中无JWT
     * @auther: 快乐水 樱桃可乐
     * @date: 14:36 2018/9/30
     * @param: []
     * @return: com.wh.demozull.entity.Result
     *
     */
    @RequestMapping(value = "/notJwt")
    public Result notJwt(){
        return new Result(false,"请求头中无JWT，请重新登录",1101);
    }

    /**
     *
     * @Description: JWT验证失败
     * @auther: 快乐水 樱桃可乐
     * @date: 14:36 2018/9/30
     * @param: []
     * @return: com.wh.demozull.entity.Result
     *
     */
    @RequestMapping(value = "/UntrustworthyJwt")
    public Result UntrustworthyJwt(){
        return new Result(false,"service:不可信的JWT,请重新登录",1102);
    }

    /**
     *
     * @Description: Redis中不存在对应的JWT
     * @auther: 快乐水 樱桃可乐
     * @date: 14:42 2018/9/30
     * @param: []
     * @return: com.wh.demozull.entity.Result
     *
     */
    @RequestMapping(value = "/notCorrespondingJwt")
    public Result notCorrespondingJwt(){
        return new Result(false,"已注销，请重新登录",1103);
    }

    /**
     *
     * @Description: 失效的JWT
     * @auther: 快乐水 樱桃可乐
     * @date: 14:47 2018/9/30
     * @param: []
     * @return: com.wh.demozull.entity.Result
     *
     */
    @RequestMapping(value = "/invalidJwt")
    public Result invalidJwt(){
        return new Result(false,"JWT已失效，请重新登录",1104);
    }

    /**
     *
     * @Description:
     * @auther: 快乐水 樱桃可乐
     * @date: 15:52 2018/9/30
     * @param: []
     * @return: com.wh.demozull.entity.Result
     *
     */
    @RequestMapping(value = "/notAuthority")
    public Result notAuthority(){
        return new Result(false,"无访问权限",1105);
    }


}
