package com.demo.rbac.service.Impl;


import com.demo.rbac.mapper.RoleModuleMapper;
import com.demo.rbac.service.RoleModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 11:57 2018/9/24
 * @Modified By:
 */
@Service
public class RoleModuleServiceImpl implements RoleModuleService {
    @Autowired(required = false)
    private RoleModuleMapper roleModuleMapper;
}
