package com.demo.rbac.service;


import com.demo.rbac.entity.Manager;
import com.demo.rbac.entity.Result;
import com.demo.rbac.vo.ManagerDynamic;
import com.demo.rbac.vo.ManagerJoin;
import java.util.List;
import java.util.Map;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 21:59 2018/9/23
 * @Modified By:
 */
public interface ManagerService {
    List<Manager> selectAll(int pageNum, int pageSize);
    List<Manager> selectDynamic(ManagerDynamic managerDynamic, int pageNum, int pageSize);
    void updatePwdById(Manager manager);
    Result addManager(ManagerJoin manager, Map map);
    Result login(Manager manager);
    Result updateManager(ManagerJoin manager, Map map);
    Result deleteManager(long id, Map map);
    List<String> selectRoleName();
    Manager selectManagerByAccount(String account);
    Manager selectPasswordAndSaltByManagerId(Long managerId);
    Manager selectManagerByManagerId(Long managerId);
}
