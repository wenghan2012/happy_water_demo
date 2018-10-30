package com.demo.rbac.service;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 23:01 2018/9/28
 * @Modified By:
 */
public interface LoginService {
    String getJwt(String account);
    Long selectIdByAccount(String account);
}
