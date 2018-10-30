package com.demo.rbac.service.Impl;


import com.demo.rbac.entity.Result;
import com.demo.rbac.entity.Role;
import com.demo.rbac.entity.RoleModule;
import com.demo.rbac.mapper.ManagerMapper;
import com.demo.rbac.mapper.ManagerRoleMapper;
import com.demo.rbac.mapper.RoleMapper;
import com.demo.rbac.mapper.RoleModuleMapper;
import com.demo.rbac.service.RoleService;
import com.demo.rbac.vo.RoleJoin;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 9:36 2018/9/24
 * @Modified By:
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired(required = false)
    private RoleMapper roleMapper;

    @Autowired(required = false)
    private ManagerMapper managerMapper;

    @Autowired(required = false)
    private RoleModuleMapper roleModuleMapper;

    @Autowired(required = false)
    private ManagerRoleMapper managerRoleMapper;

    @Autowired(required = false)
    private RedisTemplate redisTemplate;

    private static final Logger logger=LoggerFactory.getLogger(RoleServiceImpl.class);

    /**
     *
     * @Description: 查询角色列表
     * @auther: 快乐水 樱桃可乐
     * @date: 16:51 2018/10/3
     * @param: [pageNum]
     * @return: java.util.List<com.example.demorbac.entity.Role>
     *
     */
    @Override
    public List<Role> selectAll(int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return roleMapper.selectAll();
    }

    /**
     *
     * @Description: 多条件查询角色列表
     * @auther: 快乐水 樱桃可乐
     * @date: 16:51 2018/10/3
     * @param: [role]
     * @return: java.util.List<java.lang.String>
     *
     */
    @Override
    public List<String> selectDynamic(Role role,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return roleMapper.selectDynamic(role);
    }

    /**
     *
     * @Description: 后台-角色-添加角色
     * @auther: 快乐水 樱桃可乐
     * @date: 18:48 2018/10/3
     * @param: [role]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result addRole(RoleJoin role, Map map) {

        //账户名判重
        if(roleMapper.selectRoleNameRepeat(role.getRoleName())!=0){
            return new Result(false,"角色名重复",901);
        }

        //从JWT中获取当前操作用户的账户id；
        Integer id=(Integer) map.get("backstageId");
        //根据id查询操作者账户名
        String name=managerMapper.selectAccountById(id);

        //根据账户id查询操作者的最高角色等级与角色名
        Role roleDb=roleMapper.selectRoleLevelById(id);

        if(roleDb==null){
            return new Result(false,"当前账户不具备任何角色，无法添加角色",902);
        }

        //如果操作者角色名不是"超级管理员"，且自身等级等于或者小与添加的角色等级则直接返回false
        if(!"超级管理员".equals(roleDb.getRoleName())
                &&roleDb.getRoleLevel()>=role.getRoleLevel()){
            return new Result(false,"不允许添加高于等于自身级别的角色",903);
        }

        //设置角色属性
        role.setCreatedAt(System.currentTimeMillis());
        role.setUpdatedAt(System.currentTimeMillis());
        role.setUpdatedBy(name);
        role.setCreatedBy(name);
        //添加角色
        roleMapper.addRole(role);
        //获取自增主键
        long roleId=role.getId();

        //添加角色模块表记录
        //获取用户输入的模块集合
        List<String> moduleNames=role.getModuleName();
        List<RoleModule> roleModules=new ArrayList<>();
        //如果模块集合不为空，遍历添加角色模块映射记录
        if(moduleNames!=null) {
            for (String moduleName : moduleNames) {
                RoleModule roleModule = new RoleModule();
                roleModule.setCreatedAt(System.currentTimeMillis());
                roleModule.setUpdatedAt(System.currentTimeMillis());
                roleModule.setModuleName(moduleName);
                roleModule.setRoleId(roleId);
                //添加到角色模块映射集合中
                roleModules.add(roleModule);
            }
            //批量添加映射记录
            roleModuleMapper.addRecord(roleModules);
        }
        return new Result(true,"添加角色成功");
    }

    /**
     *
     * @Description: 后台-角色-更新角色
     * @auther: 快乐水 樱桃可乐
     * @date: 21:06 2018/10/3
     * @param: [role, map]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result updateRole(RoleJoin role, Map map) {

        logger.info("被更新的角色id是： "+role.getId());

        //判断被编辑的角色是否存在
        if(roleMapper.selectRoleExistByRoleId(role.getId())==0){
            return new Result(false,"不存在该角色",904);
        }

        //如果更新的角色名不为空，角色名判重
        if(role.getRoleName()!=null
                &&roleMapper.selectRoleNameRepeat(role.getRoleName())!=0){
            return new Result(false,"角色名重复",905);
        }

        if(role.getRoleName()!=null&&"超级管理员".equals(role.getRoleName())){
            return new Result(false,"不能更新为超级管理员",906);
        }

        //从JWT中获取操作者的账户id；
        Integer backstageId=(Integer) map.get("backstageId");
        //根据账户id查询操作者账户名
        String name=managerMapper.selectAccountById(backstageId);

        //根据操作者账户id查询操作者的最高角色等级与角色名
        Role roleDb=roleMapper.selectRoleLevelById(backstageId);

        //如果账户不存在任何角色，返回false
        if(roleDb==null){
            return new Result(false,"当前账户不具备任何角色",907);
        }

        logger.info("操作者的角色是： "+roleDb.getRoleName());

        //根据角色id查询被更改的角色等级和角色名
        Role roleOriginal=roleMapper.selectRoleNameAndRoleLevelByRoleId(role.getId());

        logger.info("被更新的原角色是： "+roleOriginal.getRoleName());

        //如果被更新的角色的原角色名是超级管理员，返回false
        if("超级管理员".equals(roleOriginal.getRoleName())){
            return new Result(false,"不能更新超级管理员",908);
        }

        //如果操作者角色名不是"超级管理员"，且自身等级等于或者小于更新的原现角色等级则直接返回false
        if(!"超级管理员".equals(roleDb.getRoleName())
                &&roleDb.getRoleLevel()>=roleOriginal.getRoleLevel()){
            return new Result(false,"不允许更新高于等于自身级别的角色",909);
        }

        //如果操作者角色名不是"超级管理员",且角色等级不为空,且自身等级等于或者小于更新的角色等级则直接返回false
        if(role.getRoleLevel()!=null&&!"超级管理员".equals(roleDb.getRoleName())
                &&roleDb.getRoleLevel()>=role.getRoleLevel()){
            return new Result(false,"不允许更新为高于等于自身级别的角色",910);
        }

        //设置角色属性
        role.setUpdatedBy(name);
        role.setUpdatedAt(System.currentTimeMillis());

        //更新角色
        roleMapper.updateRole(role);

        //删除角色模块映射记录
        roleModuleMapper.deleteRecord(role.getId());

        //添加角色模块表记录
        //获取用户输入的模块集合
        List<String> moduleNames=role.getModuleName();
        List<RoleModule> roleModules=new ArrayList<>();
        //遍历
        for (String moduleName:moduleNames){
            RoleModule roleModule=new RoleModule();
            roleModule.setCreatedAt(System.currentTimeMillis());
            roleModule.setUpdatedAt(System.currentTimeMillis());
            roleModule.setModuleName(moduleName);
            roleModule.setRoleId(role.getId());
            //添加到角色模块映射集合中
            roleModules.add(roleModule);
        }
        //批量添加映射记录
        roleModuleMapper.addRecord(roleModules);

        return new Result(true,"更新角色成功");
    }

    /**
     *
     * @Description: 后台-角色-删除角色
     * @auther: 快乐水 樱桃可乐
     * @date: 21:34 2018/10/3
     * @param: [id]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result deleteRole(long roleId,Map map) {

        //判断被删除的角色是否存在
        if(roleMapper.selectRoleExistByRoleId(roleId)==0){
            return new Result(false,"不存在该角色",911);
        }

        //从JWT中获取当前操作账户id；
        Integer id=(Integer) map.get("backstageId");

        //根据账户id查询操作者的最高角色等级与角色名
        Role roleDb=roleMapper.selectRoleLevelById(id);

        if(roleDb==null){
            return new Result(false,"当前账户不具备任何角色",912);
        }

        //根据角色id查询被删除的角色等级
        Role roleOriginal=roleMapper.selectRoleNameAndRoleLevelByRoleId(roleId);

        if("超级管理员".equals(roleOriginal.getRoleName())){
            return new Result(false,"超级管理员不能被删除",913);
        }

        //如果操作者角色名不是"超级管理员"，且自身等级等于或者小于被删除的角色等级则直接返回false
        if(!"超级管理员".equals(roleDb.getRoleName())
                &&roleDb.getRoleLevel()>=roleOriginal.getRoleLevel()){
            return new Result(false,"不允许删除高于等于自身级别的角色",914);
        }

        //删除角色表记录
        roleMapper.deleteRole(roleId);

        //删除角色模块映射表记录
        roleModuleMapper.deleteRecord(roleId);

        //获取具有该角色的账户集合
        List<Long> userIds=managerRoleMapper.selectUserIDByRoleId(roleId);
        //集合判空
        if(userIds.size()!=0) {
            //遍历删除掉具有该角色的账户的JWT
            for (long userId : userIds) {
                if (null != redisTemplate.opsForValue().get("backstageId" + userId)) {
                    redisTemplate.delete("backstageId" + userId);
                }
            }
        }

        //删除账户角色映射表记录
        managerRoleMapper.deleteRecordByRoleId(roleId);

        return new Result(true,"删除角色成功");
    }

    /**
     *
     * @Description: 编辑查询接口
     * @auther: 快乐水 樱桃可乐
     * @date: 11:05 2018/10/22
     * @param: [roleId]
     * @return: com.example.demorbac.entity.Role
     *
     */
    @Override
    public Role selectRoleByRoleId(Long roleId) {
        return roleMapper.selectRoleByRoleId(roleId);
    }

}
