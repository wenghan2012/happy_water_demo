package com.demo.rbac.service.Impl;

import com.demo.rbac.entity.Module;
import com.demo.rbac.entity.Result;
import com.demo.rbac.mapper.ManagerMapper;
import com.demo.rbac.mapper.ModuleMapper;
import com.demo.rbac.mapper.RoleModuleMapper;
import com.demo.rbac.service.ModuleService;
import com.demo.rbac.vo.ModuleDynamic;
import com.demo.rbac.vo.ModuleJoin;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 10:55 2018/9/24
 * @Modified By:
 */
@Service
public class ModuleServiceImpl implements ModuleService {

    private static final Logger loger=LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired(required = false)
    private ModuleMapper moduleMapper;

    @Autowired(required = false)
    private ManagerMapper managerMapper;

    @Autowired(required = false)
    private RoleModuleMapper roleModuleMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     *
     * @Description: 后台-模块-列表
     * @auther: 快乐水 樱桃可乐
     * @date: 8:55 2018/10/4
     * @param: [pageNum]
     * @return: java.util.List<com.example.demorbac.entity.Module>
     *
     */
    @Override
    public List<Module> selectAll(int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return moduleMapper.selectAll();
    }

    /**
     *
     * @Description: 后台-模块-多条件
     * @auther: 快乐水 樱桃可乐
     * @date: 8:55 2018/10/4
     * @param: [moduleDynamic]
     * @return: com.example.demorbac.entity.Module
     *
     */
    @Override
    public List<Module> selectDynamic(ModuleDynamic moduleDynamic, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return moduleMapper.selectDynamic(moduleDynamic);
    }

    /**
     *
     * @Description: 后台-模块-添加模块
     * @auther: 快乐水 樱桃可乐
     * @date: 15:48 2018/10/4
     * @param: [module, map]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @Override
    public Result addModule(Module module, Map map) {

        //模块名重名判断
        if(moduleMapper.selectModuleRepeat(module.getModuleName())!=0){
            return new Result(false,"模块名重名",1000);
        }

        //获取当前操作人账户名
        Integer id=(Integer) map.get("backstageId");
        String name=managerMapper.selectAccountById(id);

        //获取入参父模块id
        Long fatherModuleId=module.getFatherModule();

        //判断用户输入的父模块是否存在(除了0以外)
        if(fatherModuleId!=0&&moduleMapper.selectModuleExistByModuleId(fatherModuleId)==0){
            return new Result(false,"父模块不存在",1001);
        }

        //如果父模块ID为0
        if(fatherModuleId==0){
            //将模块等级设置为一级
            module.setModuleLevel(1);
        }else {
            //根据父模块id获取父模块等级
            Integer fatherModuleLevel=moduleMapper.selectModuleLevelById(fatherModuleId);
            if(fatherModuleLevel>=3){
                return new Result(false,"第三级模块不能添加子模块",1002);
            }
            //并将添加的模块等级设置为父模块等级+1
            module.setModuleLevel(fatherModuleLevel+1);
        }

        module.setCreatedAt(System.currentTimeMillis());
        module.setUpdatedAt(System.currentTimeMillis());
        module.setUpdatedBy(name);
        module.setCreatedBy(name);

        loger.info(module.toString());
        //添加模块
        moduleMapper.addModule(module);

        return new Result(true,"添加成功");
    }

    /**
     *
     * @Description: 后台-模块-更新模块
     * @auther: 快乐水 樱桃可乐
     * @date: 15:17 2018/10/4
     * @param: [module, map]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @Override
    public Result updateModule(Module module, Map map) {

        //判断被编辑的模块是否存在
        if(moduleMapper.selectModuleExistByModuleId(module.getId())==0){
            return new Result(false,"不存在该被编辑的模块",1003);
        }

        //模块名重名判断
        if(module.getModuleName()!=null
                &&moduleMapper.selectModuleRepeat(module.getModuleName())!=0){
            return new Result(false,"模块名重名",1004);
        }

        //获取当前操作人账户名
        Integer backstageId=(Integer) map.get("backstageId");
        String name=managerMapper.selectAccountById(backstageId);

        //获取父模块id
        Long fatherModuleId=module.getFatherModule();

        //如果需要更改父模块
        if(fatherModuleId!=null){
            
            //判断用户输入的父模块是否存在(除了0以外)
            if (fatherModuleId != 0 && moduleMapper.selectModuleExistByModuleId(fatherModuleId) == 0) {
                return new Result(false, "父模块不存在",1005);
            }
            //如果父模块ID为0
            if (fatherModuleId == 0) {
                //将模块等级设置为一级
                module.setModuleLevel(1);
            } else {
                //根据父模块id获取父模块等级
                Integer fatherModuleLevel=moduleMapper.selectModuleLevelById(fatherModuleId);
                if(fatherModuleLevel>=3){
                    return new Result(false,"第三级模块不能更新子模块",1006);
                }
                //并将更新的模块等级设置为父模块等级+1
                module.setModuleLevel(fatherModuleLevel+1);
            }
        }

        module.setUpdatedAt(System.currentTimeMillis());
        module.setUpdatedBy(name);

        //更新模块
        moduleMapper.upModule(module);

        return new Result(true,"更新成功");
    }

    /**
     *
     * @Description: 后台-模块-删除模块
     * @auther: 快乐水 樱桃可乐
     * @date: 15:28 2018/10/4
     * @param: [id]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result deleteModule(long id) {

        //判断被删除的模块是否存在
        if(moduleMapper.selectModuleExistByModuleId(id)==0){
            return new Result(false,"不存在该被编辑的模块",1007);
        }

        loger.info("需要删除的模块是： "+id);

        //获取模块名和模块等级
        Module module=moduleMapper.selectModuleNameAndLevelById(id);

        //获取模块等级
        int moduleLevel=module.getModuleLevel();

        loger.info("删除的模块等级为： "+moduleLevel);

        //如果是顶级模块
        if(moduleLevel==1){
            if("后台管理".equals(module.getModuleName())){
                return new Result(false,"后台系统不可以被删除",1008);
            }
            //获取子模块的id集合
            List<Long> sonIds=moduleMapper.selectSonModuleByModuleId(id);
            loger.info("二级模块id集合为： "+sonIds);
            if(sonIds.size()!=0) {
                //遍历二级模块id
                for (long sId : sonIds){
                    //获取三级模块的id集合
                    List<Long> sIds=moduleMapper.selectSonModuleByModuleId(sId);
                    loger.info("三级模块id集合为： "+sIds);
                    if(sIds.size()!=0) {
                        //批量删除模块表中模块
                        moduleMapper.deleteModules(sIds);
                        //批量查询子模块的模块名
                        List<String> sNanes = moduleMapper.selectModulesByModuleId(sIds);
                        //批量删除角色模块映射表记录
                        roleModuleMapper.deleteRoleModulesByModuleName(sNanes);
                    }
                }
                //批量删除模块表中模块
                moduleMapper.deleteModules(sonIds);
                //批量查询子模块的模块名
                List<String> sonNanes = moduleMapper.selectModulesByModuleId(sonIds);
                loger.info("模块名集合为："+sonNanes);
                //批量删除角色模块映射表记录
                roleModuleMapper.deleteRoleModulesByModuleName(sonNanes);
            }
            moduleMapper.deleteModule(id);
        }

        //如果是二级模块
        if(moduleLevel==2){
            //根据模块名获取顶级模块
            Module moduleTop=moduleMapper.selectFatherModuleByModuleName(module.getModuleName());
            if("后台管理".equals(moduleTop.getModuleName())){
                return new Result(false,"后台系统不可以被删除",1008);
            }
            //获取子模块的id集合
            List<Long> sonIds=moduleMapper.selectSonModuleByModuleId(id);
            if(sonIds.size()!=0) {
                //批量删除模块表中模块
                moduleMapper.deleteModules(sonIds);

                //批量查询子模块的模块名
                List<String> sonNanes = moduleMapper.selectModulesByModuleId(sonIds);
                //批量删除角色模块映射表记录
                roleModuleMapper.deleteRoleModulesByModuleName(sonNanes);
            }
            moduleMapper.deleteModule(id);
        }

        //如果是三级模块
        if(moduleLevel==3){
            //获得顶级的父模块
            Module moduleTop=moduleMapper.selectFatherModuleByModuleName(module.getModuleName());
            loger.info(moduleTop.toString());
            moduleTop=moduleMapper.selectFatherModuleByModuleName(moduleTop.getModuleName());
            loger.info(moduleTop.toString());
            if("后台管理".equals(moduleTop.getModuleName())){
                return new Result(false,"后台系统不可以被删除",1008);
            }
            //删除模块角色映射表记录
            roleModuleMapper.deleteRecordBymoduleName(module.getModuleName());

            //删除模块表中的模块
            moduleMapper.deleteModule(id);

        }

        //根据模块名获取具有该模块权限的账户ID集合(不管是几级模块，拥有子级权限必然具有父级权限)
        List<Long> managerIds=roleModuleMapper.selectManagerIdByModuleName(module.getModuleName());

        loger.info("需要注销的账户id数： "+managerIds.size());
        loger.info("需要注销的账户id： "+managerIds);

        if(managerIds!=null&&managerIds.size()!=0){
            //遍历id集合
            for (long managerId:managerIds){
                loger.info("被注销的账户id为： "+managerId);
                //删除掉该id中redis中的缓存
                redisTemplate.delete(managerId);
            }
        }

        return new Result(true,"删除模块成功");
    }

    /**
     *
     * @Description: 获取左边栏-模块
     * @auther: 快乐水 樱桃可乐
     * @date: 22:05 2018/10/5
     * @param: [id]
     * @return: java.util.List<com.example.demorbac.vo.ModuleJoin>
     *
     */
    @Override
    public List<ModuleJoin> selectModuleList(long managerId) {

        //根据账号ID和父模块ID(一级模块的父模块ID默认为0)获取一级权限的集合
        List<ModuleJoin> moduleJoinsOne=moduleMapper.selectTopModuleById(managerId,0);
        //遍历一级权限的集合，根据账户ID和一级模块的ID获取二级权限的集合
        for(ModuleJoin moduleJoin:moduleJoinsOne){
            //设置一级模块的子模块属性
            moduleJoin.setSonModule(moduleMapper.selectTopModuleById(managerId,moduleJoin.getId()));
        }
        //遍历一级模块
        for(ModuleJoin moduleJoin:moduleJoinsOne){
            //遍历二级权限的集合，根据账户ID和二级模块的ID获取三级权限的集合
            for(ModuleJoin moduleJoin1:moduleJoin.getSonModule()){
                //设置二级模块的子模块属性
                moduleJoin1.setSonModule(moduleMapper.selectTopModuleById(managerId,moduleJoin1.getId()));
            }
        }
        //返回封装了三级模块/二级模块的一级模块
        return moduleJoinsOne;
    }

    @Override
    public Module selectModuleByModuleId(Long moduleId) {
        return moduleMapper.selectModuleByModuleId(moduleId);
    }

}
