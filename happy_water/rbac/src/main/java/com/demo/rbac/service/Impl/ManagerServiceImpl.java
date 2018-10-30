package com.demo.rbac.service.Impl;


import com.demo.rbac.entity.Manager;
import com.demo.rbac.entity.ManagerRole;
import com.demo.rbac.entity.Result;
import com.demo.rbac.entity.Role;
import com.demo.rbac.mapper.ManagerMapper;
import com.demo.rbac.mapper.ManagerRoleMapper;
import com.demo.rbac.mapper.RoleMapper;
import com.demo.rbac.service.ManagerService;
import com.demo.rbac.utils.Md5Util;
import com.demo.rbac.vo.ManagerDynamic;
import com.demo.rbac.vo.ManagerJoin;
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
import java.util.UUID;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 21:59 2018/9/23
 * @Modified By:
 */
@Service
public class ManagerServiceImpl implements ManagerService {

    private static final Logger loger=LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired(required = false)
    private ManagerMapper managerMapper;

    @Autowired(required = false)
    private RoleMapper roleMapper;

    @Autowired(required = false)
    private ManagerRoleMapper managerRoleMapper;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     *
     * @Description: 分页查询
     * @auther: 快乐水 樱桃可乐
     * @date: 16:28 2018/10/2
     * @param: [pageNum]
     * @return: java.util.List<com.example.demorbac.entity.Manager>
     *
     */
    @Override
    public List<Manager> selectAll(int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return managerMapper.selectAll();
    }

    /**
     *
     * @Description: 多条件分页查询
     * @auther: 快乐水 樱桃可乐
     * @date: 16:29 2018/10/2
     * @param: [managerDynamic, pageNum]
     * @return: java.util.List<com.example.demorbac.entity.Manager>
     *
     */
    @Override
    public List<Manager> selectDynamic(ManagerDynamic managerDynamic, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return managerMapper.selectDynamic(managerDynamic);
    }

    /**
     *
     * @Description: 更新密码
     * @auther: 快乐水 樱桃可乐
     * @date: 16:29 2018/10/2
     * @param: [manager]
     * @return: void
     *
     */
    @Override
    public void updatePwdById(Manager manager) {
        managerMapper.updatePwdById(manager);
    }


    /**
     *
     * @Description: 添加账号
     * @auther: 快乐水 樱桃可乐
     * @date: 16:29 2018/10/2
     * @param: [manager]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result addManager(ManagerJoin manager, Map map){

        //账户判重，查询数据表中是否存在对应的账号名
        if(managerMapper.selectAccountRepeat(manager.getAccount())!=0){
            return new Result(false,"账户名重复",806);
        }

        //从JWT中获取到当前操作的用户ID
        Integer backstageId=(Integer)map.get("backstageId");

        //根据id查询操作者的最高角色等级与角色名
        Role roleDb=roleMapper.selectRoleLevelById(backstageId);

        //判断当前操作者是否具有角色
        if(roleDb==null){
            return new Result(false,"当前账户不具备任何角色",807);
        }

        //获取账户需要添加的角色（入参）
        List<String> roleNames=manager.getRoleName();

        //如果添加账户的角色集合不为空
        if(roleNames!=null&&roleNames.size()!=0){
            //遍历新添账户的角色
            for (String roleName:roleNames){
                if("超级管理员".equals(roleName)){
                    return new Result(false,"不允许添加超级管理员",808);
                }
                //获取当前角色元素的角色等级
                Integer roleLevel=roleMapper.selectRoleLevelByRoleName(roleName);
                //如果当前操作者不是超级管理员且当前角色的等级不大于新添加的角色，则返回false
                if(!"超级管理员".equals(roleDb.getRoleName())
                        &&roleDb.getRoleLevel()>=roleLevel){
                    return new Result(false,"不允许添加高于等于自身级别的角色",809);
                }

            }
        }

        //通过SQL查询出当前操作者的账户名
        String name=managerMapper.selectAccountById(backstageId);

        //设置对象参数
        manager.setCreatedBy(name);
        manager.setUpdatedBy(name);
        manager.setCreatedAt(System.currentTimeMillis());
        manager.setUpdatedAt(System.currentTimeMillis());
        manager.setSalt(UUID.randomUUID()+"");
        manager.setPassword(Md5Util.getMD5String(manager.getPassword()+manager.getSalt()));
        //添加账户,获取自增主键
        managerMapper.addManager(manager);
        long userId=manager.getId();

        loger.info("添加账户成功： "+userId);

        //添加用户角色映射
        List<ManagerRole> managerRoles=new ArrayList<>();
        //如果账户的角色不为空
        if(roleNames!=null&&roleNames.size()!=0) {
            //遍历用户选择的角色，添加映射记录
            for (String roleName : roleNames) {
                //创建账户角色映射对象
                ManagerRole managerRole = new ManagerRole();
                //根据用户选择的角色名查询角色的id
                long id = roleMapper.selectRoleIdByRoleName(roleName);
                if (id == 0) {
                    return new Result(false, "账户已添加，但不存在该角色", 810);
                }
                managerRole.setRoleId(id);
                managerRole.setUserId(userId);
                managerRole.setCreatedAt(System.currentTimeMillis());
                managerRole.setUpdatedAt(System.currentTimeMillis());
                //构造映射表集合
                managerRoles.add(managerRole);
            }
            //批插入
            managerRoleMapper.insertRecord(managerRoles);
        }
        return new Result(true,"添加成功");
    }

    /**
     *
     * @Description: 登录验证
     * @auther: 快乐水 樱桃可乐
     * @date: 22:30 2018/9/28
     * @param: [manager]
     * @return: boolean
     *
     */
    @Override
    public Result login(Manager manager) {
        //根据账号查询密码和盐
        Manager managerDb=managerMapper.login(manager.getAccount());

        //如果查询不到对象则表明账户不存在
        if(managerDb==null){
            return new Result(false,"账户不存在",701);
        }
        //查询冻结状态
        if(managerDb.getLocked()){
            return new Result(false,"账户已被冻结",703);
        }
        //将用户输入的密码+DB查询到的盐拼接并通过MD5加盐的方式生成新的DB密码，与DB查询到的密码对比
        if(Md5Util.getMD5String(manager.getPassword()+managerDb.getSalt())
                .equals(managerDb.getPassword())){
            return new Result<>(true,"登录成功");
        }
        return new Result(false,"密码错误",702);
    }

    /**
     *
     * @Description: 更新账户
     * @auther: 快乐水 樱桃可乐
     * @date: 4:12 2018/10/3
     * @param: [manager, map]
     * @return: boolean
     *
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result updateManager(ManagerJoin manager, Map map) {

        //判断被编辑的账户是否存在
        if(managerMapper.selectManagerExistByManagerId(manager.getId())==0){
            return new Result(false,"不存在账户",811);
        }

        //账户判重，查询数据表中是否存在对应的账号名
        if(managerMapper.selectAccountRepeat(manager.getAccount())!=0){
            return new Result(false,"账户名格式错误/重复，请重试",812);
        }

        //从JWT中获取到当前操作的用户ID
        Integer backstageId=(Integer)map.get("backstageId");

        loger.info("当前操作者的id是： "+backstageId);

        //根据id查询操作者的最高角色等级与角色名
        Role roleDb=roleMapper.selectRoleLevelById(backstageId);

        if(roleDb==null){
            return new Result(false,"当前账户不具备任何角色",813);
        }
        loger.info("被更新的账户的id为： "+manager.getId());
        loger.info("当前操作者的最高角色名为： "+roleDb.getRoleName());

        //获取原账户最高级别的角色id与角色名
        Role roleoriginal=roleMapper.selectRoleLevelById(manager.getId());

        loger.info("被更新账户的最高角色为： "+roleoriginal.getRoleName());

        //超级管理员不能被编辑
        if("超级管理员".equals(roleoriginal.getRoleName())){
            return new Result(false,"超级管理员不能被编辑",814);
        }

        //如果当前操作者不是超级管理员且当前角色的等级不大于被更新的原角色，则返回false.
        if(!"超级管理员".equals(roleDb.getRoleName())
                &&roleDb.getRoleLevel()>=roleoriginal.getRoleLevel()){
            return new Result(false,"不允许编辑高于等于自身级别的角色",815);
        }

        //获取账户需要添加的角色(更新后的角色)
        List<String> roleNames=manager.getRoleName();

        //如果需要更新的账户角色集合不为空
        if(roleNames!=null&&roleNames.size()!=0){
            //遍历新添账户角色
            for (String roleName:roleNames){
                loger.info("需要更新的角色名为： "+roleName);
                if("超级管理员".equals(roleName)){
                    return new Result(false,"不允许更新为超级管理员",816);
                }
                //获取当前角色元素的角色等级,根据角色名获取角色级别.
                int roleLevel=roleMapper.selectRoleLevelByRoleName(roleName);
                //如果当前操作者不是超级管理员且当前角色的等级不大于新添加的角色，则返回false.
                if(!"超级管理员".equals(roleDb.getRoleName())
                        &&roleDb.getRoleLevel()>=roleLevel){
                    return new Result(false,"不允许编辑为高于等于自身级别的角色",817);
                }

            }
        }


        //通过SQL查询出当前操作者的账户名
        String name=managerMapper.selectAccountById(backstageId);

        System.out.println("----------------------");
        System.out.println(manager);
        System.out.println("----------------------");

        //设置参数
        if(manager.getPassword()!=null) {
            manager.setSalt(UUID.randomUUID() + "");
            manager.setPassword(Md5Util.getMD5String(manager.getPassword() + manager.getSalt()));
        }

        //如果账户更新为被冻结
        if(manager.getLocked()!=null&&manager.getLocked()){
            //删除掉redis中的缓存，使其注销
            redisTemplate.delete("backstageId"+manager.getId());
        }
        manager.setUpdatedAt(System.currentTimeMillis());
        manager.setUpdatedBy(name);

        System.out.println("----------------------");
        System.out.println(manager);
        System.out.println("----------------------");

        //更新用户表
        managerMapper.upManager(manager);

        //删除映射表记录
        managerRoleMapper.deleteRecord(manager.getId());

        //构造账户角色表元素集
        List<ManagerRole> managerRoles= new ArrayList<>();

        if(roleNames!=null&&roleNames.size()!=0) {
            for (String roleName : roleNames) {
                ManagerRole managerRole = new ManagerRole();
                //设置user_id
                managerRole.setUserId(manager.getId());
                //设置role_id,先通过role_name查询到role_id。
                managerRole.setRoleId(roleMapper.selectRoleIdByRoleName(roleName));
                //设置更新时间
                managerRole.setUpdatedAt(System.currentTimeMillis());
                //设置创建时间(映射表不存在更新操作，都是删除后，创建新的映射)
                managerRole.setCreatedAt(System.currentTimeMillis());
                //构造账户角色映射表集合
                managerRoles.add(managerRole);
            }
            //添加映射表记录
            managerRoleMapper.insertRecord(managerRoles);
        }
        return new Result(true,"更新成功");
    }

    /**
     *
     * @Description: 后台-账户-删除
     * @auther: 快乐水 樱桃可乐
     * @date: 9:43 2018/10/3
     * @param: [id]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result deleteManager(long id,Map map) {

        //判断被删除的账户是否存在
        if(managerMapper.selectManagerExistByManagerId(id)==0){
            return new Result(false,"不存在该账户",818);
        }

        //从JWT中获取到当前操作的用户ID
        Integer backstageId=(Integer)map.get("backstageId");

        //根据id查询操作者的最高角色等级与角色名
        Role roleDb=roleMapper.selectRoleLevelById(backstageId);

        if(roleDb==null){
            return new Result(false,"当前账户不具备任何角色",819);
        }

        //获取原账户最高级别的角色id与角色名
        Role roleoriginal=roleMapper.selectRoleLevelById(id);

        //超级管理员不能被编辑
        if("超级管理员".equals(roleoriginal.getRoleName())){
            return new Result(false,"超级管理员不能被删除",820);
        }

        //如果当前操作者不是超级管理员且当前角色的等级不大于被删除的原角色，则返回false.
        if(!"超级管理员".equals(roleDb.getRoleName())
                &&roleDb.getRoleLevel()>=roleoriginal.getRoleLevel()){
            return new Result(false,"不允许删除高于等于自身级别的角色",821);
        }

        //删除账户表的账户
        managerMapper.deleteAccount(id);
        //删除账户角色映射表的记录
        managerRoleMapper.deleteRecord(id);
        //注销账户,删除redis中的对应账户信息
        redisTemplate.delete("backstageId"+id);
        return new Result(true,"删除成功");
    }

    /**
     *
     * @Description: 查询角色列表（去重）
     * @auther: 快乐水 樱桃可乐
     * @date: 15:38 2018/10/3
     * @param: []
     * @return: java.util.List<java.lang.String>
     *
     */
    @Override
    public List<String> selectRoleName() {
        return roleMapper.selectRoleName();
    }

    /**
     *
     * @Description: 获取账户名与角色（最高级）
     * @auther: 快乐水 樱桃可乐
     * @date: 9:28 2018/10/7
     * @param: [account]
     * @return: com.example.demorbac.entity.Manager
     *
     */
    @Override
    public Manager selectManagerByAccount(String account) {
        return managerMapper.selectManagerByAccount(account);
    }

    /**
     *
     * @Description: 根据账户Id查询密码与盐值
     * @auther: 快乐水 樱桃可乐
     * @date: 20:05 2018/10/17
     * @param: [managerId]
     * @return: com.example.demorbac.entity.Manager
     *
     */
    @Override
    public Manager selectPasswordAndSaltByManagerId(Long managerId) {
        return managerMapper.selectPasswordAndSaltByManagerId(managerId);
    }

    /**
     *
     * @Description: 编辑显示账户
     * @auther: 快乐水 樱桃可乐
     * @date: 10:02 2018/10/22
     * @param: [managerId]
     * @return: java.util.List<com.example.demorbac.entity.Manager>
     *
     */
    @Override
    public Manager selectManagerByManagerId(Long managerId) {
        return managerMapper.selectManagerByManagerId(managerId);
    }
}
