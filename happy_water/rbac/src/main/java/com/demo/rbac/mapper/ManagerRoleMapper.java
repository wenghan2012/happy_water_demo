package com.demo.rbac.mapper;


import com.demo.rbac.entity.ManagerRole;
import java.util.List;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 11:06 2018/9/24
 * @Modified By:
 */
public interface ManagerRoleMapper {
    /**
     *
     * @Description: 批量插入
     * @auther: 快乐水 樱桃可乐
     * @date: 4:52 2018/10/3
     * @param: [managerRole]
     * @return: void
     *
     */
    void insertRecord(List<ManagerRole> managerRole);
    /**
     *
     * @Description: 删除
     * @auther: 快乐水 樱桃可乐
     * @date: 4:53 2018/10/3
     * @param: [userId]
     * @return: void
     *
     */
    void deleteRecord(long userId);

    /**
     *
     * @Description: 根据角色id删除映射表记录
     * @auther: 快乐水 樱桃可乐
     * @date: 22:21 2018/10/3
     * @param: [roleId]
     * @return: void
     *
     */
    void deleteRecordByRoleId(long roleId);

    /**
     *
     * @Description: 根据角色id查询账户id
     * @auther: 快乐水 樱桃可乐
     * @date: 22:29 2018/10/3
     * @param: [roleId]
     * @return: long
     *
     */
    List<Long> selectUserIDByRoleId(long roleId);
}
