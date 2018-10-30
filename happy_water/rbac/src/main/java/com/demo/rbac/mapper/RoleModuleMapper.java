package com.demo.rbac.mapper;

import com.demo.rbac.entity.RoleModule;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 11:52 2018/9/24
 * @Modified By:
 */
public interface RoleModuleMapper {
    /**
     *
     * @Description: 后台-角色-添加角色模块映射记录
     * @auther: 快乐水 樱桃可乐
     * @date: 19:27 2018/10/3
     * @param: [roleModule]
     * @return: void
     *
     */
    void addRecord(List<RoleModule> roleModules);

    /**
     *
     * @Description: 后台-角色-删除角色模块映射记录
     * @auther: 快乐水 樱桃可乐
     * @date: 21:01 2018/10/3
     * @param: [roleID]
     * @return: void
     *
     */
    void deleteRecord(long roleID);
    /**
     *
     * @Description: 根据模块名删除映射记录
     * @auther: 快乐水 樱桃可乐
     * @date: 15:36 2018/10/4
     * @param: [moduleName]
     * @return: void
     *
     */
    void deleteRecordBymoduleName(String moduleName);

    /**
     *
     * @Description: 根据模块名集合批量删除模块角色映射记录
     * @auther: 快乐水 樱桃可乐
     * @date: 8:44 2018/10/8
     * @param: [moduleNames]
     * @return: void
     *
     */
    void deleteRoleModulesByModuleName(@Param("sonNanes") List<String> moduleNames);

    /**
     *
     * @Description: 根据模块名获取具有该权限的账户ID集合
     * @auther: 快乐水 樱桃可乐
     * @date: 9:18 2018/10/8
     * @param: [moduleName]
     * @return: java.util.List<java.lang.Long>
     *
     */
    List<Long> selectManagerIdByModuleName(String moduleName);
}
