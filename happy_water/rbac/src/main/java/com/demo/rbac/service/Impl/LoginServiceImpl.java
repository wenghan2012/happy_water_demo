package com.demo.rbac.service.Impl;


import com.demo.rbac.mapper.ManagerMapper;
import com.demo.rbac.mapper.ModuleMapper;
import com.demo.rbac.mapper.RoleMapper;
import com.demo.rbac.service.LoginService;
import com.demo.rbac.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 23:01 2018/9/28
 * @Modified By:
 */
@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger loger=LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired(required = false)
    private ManagerMapper managerMapper;

    @Autowired(required = false)
    private RoleMapper roleMapper;

    @Autowired(required = false)
    private ModuleMapper moduleMapper;

    /**
     *
     * @Description: 获取JWT
     * @auther: 快乐水 樱桃可乐
     * @date: 1:24 2018/9/29
     * @param: [account]
     * @return: java.lang.String
     *
     */
    @Override
    public String getJwt(String account) {
        //获取账户的id(作为jwt的荷载数据)
        long id = managerMapper.selectIdByAccount(account);
        //获取账户的角色(作为jwt的荷载数据)
        List<String> roleNames = roleMapper.selectRoleByAccount(account);
        //获取账户的权限(作为jwt的荷载数据)
        List<String> moduleNames =moduleMapper.selectModuleByAccount(account);
        //创建Map来封装数据(保存荷载数据：键值对)
        Map<String, Object> map = new HashMap<>(4);
        map.put("roleNames", roleNames);
        map.put("authority", moduleNames);
        //后台jwt使用：backstageId为账户id的键，前台使用receptionId
        map.put("backstageId", id);
        map.put("timestamp", System.currentTimeMillis());
        //调用工具类生成jwt
        return JwtUtil.createJWT(map);
    }

    /**
     *
     * @Description: 根据账户名查询账户Id
     * @auther: 快乐水 樱桃可乐
     * @date: 9:27 2018/10/6
     * @param: [account]
     * @return: java.lang.Long
     *
     */
    @Override
    public Long selectIdByAccount(String account) {
        return managerMapper.selectIdByAccount(account);
    }

}
