package com.demo.rbac.service;

import com.demo.rbac.entity.Result;
import com.demo.rbac.entity.Role;
import com.demo.rbac.vo.RoleJoin;
import java.util.List;
import java.util.Map;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 9:35 2018/9/24
 * @Modified By:
 */
public interface RoleService {
    List<Role> selectAll(int pageNum, int pageSize);
    List<String> selectDynamic(Role role, int pageNum, int pageSize);
    Result addRole(RoleJoin role, Map map);
    Result updateRole(RoleJoin role, Map map);
    Result deleteRole(long id, Map map);
    Role selectRoleByRoleId(Long roleId);
}
