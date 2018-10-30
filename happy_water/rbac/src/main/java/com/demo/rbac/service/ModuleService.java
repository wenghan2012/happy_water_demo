package com.demo.rbac.service;


import com.demo.rbac.entity.Module;
import com.demo.rbac.entity.Result;
import com.demo.rbac.vo.ModuleDynamic;
import com.demo.rbac.vo.ModuleJoin;
import java.util.List;
import java.util.Map;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 10:55 2018/9/24
 * @Modified By:
 */
public interface ModuleService {
    List<Module> selectAll(int pageNum, int pageSize);
    List<Module> selectDynamic(ModuleDynamic moduleDynamic, int pageNum, int pageSize);
    Result addModule(Module module, Map map);
    Result updateModule(Module module, Map map);
    Result deleteModule(long id);
    List<ModuleJoin> selectModuleList(long managerId);
    Module selectModuleByModuleId(Long moduleId);
}
