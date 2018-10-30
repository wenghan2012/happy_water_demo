package com.demo.rbac.service.Impl;

import com.demo.rbac.mapper.ManagerRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 11:09 2018/9/24
 * @Modified By:
 */
@Service
public class ManagerRoleServiceImpl {
    @Autowired(required = false)
    private ManagerRoleMapper managerRoleMapper;
}
