package com.demo.rbac.mapper;



import com.demo.rbac.entity.Role;
import com.demo.rbac.vo.RoleJoin;
import java.util.List;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 9:32 2018/9/24
 * @Modified By:
 */
public interface RoleMapper {
    List<Role> selectAll();
    /**
     *
     * @Description:  根据角色名查询角色id
     * @auther: 快乐水 樱桃可乐
     * @date: 10:32 2018/9/28
     * @param: [rolsName]
     * @return: long
     *
     */
    long selectRoleIdByRoleName(String roleName);
    /**
     *
     * @Description: 根据账号名查询角色
     * @auther: 快乐水 樱桃可乐
     * @date: 22:59 2018/9/28
     * @param: [Account]
     * @return: java.util.List<java.lang.String>
     *
     */
    List<String> selectRoleByAccount(String Account);
    /**
     *
     * @Description: 查询角色类型
     * @auther: 快乐水 樱桃可乐
     * @date: 15:33 2018/10/3
     * @param: []
     * @return: java.util.List<java.lang.String>
     *
     */
    List<String> selectRoleName();
    /**
     *
     * @Description: 多条件查询
     * @auther: 快乐水 樱桃可乐
     * @date: 16:50 2018/10/3
     * @param: [role]
     * @return: java.util.List<java.lang.String>
     *
     */
    List<String> selectDynamic(Role role);

    /**
     *
     * @Description: 后台-角色-添加角色
     * @auther: 快乐水 樱桃可乐
     * @date: 18:59 2018/10/3
     * @param: [role]
     * @return: void
     *
     */
    void addRole(RoleJoin role);
    /**
     *
     * @Description: 后台-角色-添加-角色名判重
     * @auther: 快乐水 樱桃可乐
     * @date: 19:07 2018/10/3
     * @param: [roleName]
     * @return: int
     *
     */
    int selectRoleNameRepeat(String roleName);
    /**
     *
     * @Description: 后台-角色-更新角色
     * @auther: 快乐水 樱桃可乐
     * @date: 20:55 2018/10/3
     * @param: [role]
     * @return: void
     *
     */
    void updateRole(RoleJoin role);
    /**
     *
     * @Description: 后台-角色-删除角色
     * @auther: 快乐水 樱桃可乐
     * @date: 21:28 2018/10/3
     * @param: [id]
     * @return: void
     *
     */
    void deleteRole(long id);

    /**
     *
     * @Description: 根据id查询role_Level
     * @auther: 快乐水 樱桃可乐
     * @date: 22:09 2018/10/6
     * @param: [id]
     * @return: int
     *
     */
    Role selectRoleLevelById(long id);

    /**
     *
     * @Description: 根据角色名获取角色等级
     * @auther: 快乐水 樱桃可乐
     * @date: 23:37 2018/10/6
     * @param: [roleName]
     * @return: int
     *
     */
    int selectRoleLevelByRoleName(String roleName);

    /**
     *
     * @Description: 根据角色id查询是否存在该角色
     * @auther: 快乐水 樱桃可乐
     * @date: 11:22 2018/10/7
     * @param: [RoleId]
     * @return: int
     *
     */
    int selectRoleExistByRoleId(long RoleId);
    /**
     *
     * @Description: 根据角色id查询角色名于角色等级
     * @auther: 快乐水 樱桃可乐
     * @date: 13:59 2018/10/9
     * @param: [RoleId]
     * @return: com.example.demorbac.entity.Role
     *
     */
    Role selectRoleNameAndRoleLevelByRoleId(Long RoleId);
    /**
     *
     * @Description: 编辑显示
     * @auther: 快乐水 樱桃可乐
     * @date: 11:04 2018/10/22
     * @param: [RoleId]
     * @return: com.example.demorbac.entity.Role
     *
     */
    Role selectRoleByRoleId(Long roleId);
}
