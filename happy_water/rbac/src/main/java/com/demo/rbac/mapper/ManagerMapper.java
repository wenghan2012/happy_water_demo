package com.demo.rbac.mapper;

import com.demo.rbac.entity.Manager;
import com.demo.rbac.vo.ManagerDynamic;
import com.demo.rbac.vo.ManagerJoin;
import java.util.List;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 21:35 2018/9/23
 * @Modified By:
 */
public interface ManagerMapper {

    /**
     *
     * @Description: 联表查询全表
     * @auther: 快乐水 樱桃可乐
     * @date: 11:36 2018/9/25
     * @param: []
     * @return: java.util.List<com.example.demorbac.entity.Manager>
     *
     */
    List<Manager> selectAll();

    /**
     *
     * @Description: 多条件模糊查询
     * @auther: 快乐水 樱桃可乐
     * @date: 15:16 2018/9/25
     * @param: []
     * @return: java.util.List<com.example.demorbac.vo.ManagerJoint>
     *
     */
    List<Manager> selectDynamic(ManagerDynamic managerDynamic);

    /**
     *
     * @Description: 根据ID更新密码
     * @auther: 快乐水 樱桃可乐
     * @date: 22:53 2018/9/24
     * @param: [id]
     * @return: void
     *
     */
    void updatePwdById(Manager manager);

    /**
     *
     * @Description: 根据id查询账户名
     * @auther: 快乐水 樱桃可乐
     * @date: 19:46 2018/9/27
     * @param: [id]
     * @return: java.lang.String
     *
     */
    String selectAccountById(long id);
    /**
     *
     * @Description:  账户判重
     * @auther: 快乐水 樱桃可乐
     * @date: 8:30 2018/9/28
     * @param: [account]
     * @return: int
     *
     */
    int selectAccountRepeat(String account);
    /**
     *
     * @Description: 添加一个账户
     * @auther: 快乐水 樱桃可乐
     * @date: 19:59 2018/9/27
     * @param: [manager]
     * @return: void
     *
     */
    void addManager(ManagerJoin manager);
    /**
     *
     * @Description: 登录接口
     * @auther: 快乐水 樱桃可乐
     * @date: 16:53 2018/9/28
     * @param: [account]
     * @return: com.example.demorbac.entity.Manager
     *
     */
    Manager login(String account);
    /**
     *
     * @Description: 根据账户名获取账号id
     * @auther: 快乐水 樱桃可乐
     * @date: 23:23 2018/9/28
     * @param: [account]
     * @return: java.lang.Long
     *
     */
    long selectIdByAccount(String account);
    /**
     *
     * @Description: 根据账户名获取账户id与角色名
     * @auther: 快乐水 樱桃可乐
     * @date: 10:30 2018/10/6
     * @param: [account]
     * @return: com.example.demorbac.entity.Manager
     *
     */
    Manager selectManagerByAccount(String account);
    /**
     *
     * @Description: 更新账户
     * @auther: 快乐水 樱桃可乐
     * @date: 4:38 2018/10/3
     * @param: [manager]
     * @return: boolean
     *
     */
    boolean upManager(ManagerJoin manager);
    /**
     *
     * @Description: 后台-账户-删除
     * @auther: 快乐水 樱桃可乐
     * @date: 9:49 2018/10/3
     * @param: [id]
     * @return: boolean
     *
     */
    boolean deleteAccount(long id);

    /**
     *
     * @Description: 根据账户id查询是否存在该账户
     * @auther: 快乐水 樱桃可乐
     * @date: 11:18 2018/10/7
     * @param: [id]
     * @return: int
     *
     */
    int selectManagerExistByManagerId(long id);
    /**
     *
     * @Description: 根据账号ID查询冻结状态
     * @auther: 快乐水 樱桃可乐
     * @date: 20:42 2018/10/7
     * @param: [id]
     * @return: boolean
     *
     */
    Boolean selectLockedByManagerAccount(String account);
    /**
     *
     * @Description: 根据账号id查询密码和盐值
     * @auther: 快乐水 樱桃可乐
     * @date: 18:20 2018/10/8
     * @param: [id]
     * @return: com.example.demorbac.entity.Manager
     *
     */
    Manager selectPasswordAndSaltByManagerId(Long managerId);

    /**
     *
     * @Description: 编辑显示接口
     * @auther: 快乐水 樱桃可乐
     * @date: 9:57 2018/10/22
     * @param: [managerId]
     * @return: com.example.demorbac.entity.Manager
     *
     */
    Manager selectManagerByManagerId(Long managerId);
}
