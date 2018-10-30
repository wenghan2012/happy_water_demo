package com.demo.rbac.controller;


import com.demo.rbac.entity.Module;
import com.demo.rbac.entity.Result;
import com.demo.rbac.entity.moduleValidateGroup.AddModuleGroup;
import com.demo.rbac.service.Impl.ModuleServiceImpl;
import com.demo.rbac.utils.JwtUtil;
import com.demo.rbac.utils.PageInfo;
import com.demo.rbac.vo.ModuleDynamic;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 10:57 2018/9/24
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/background/module",produces = "application/json")
public class ModuleController {

    @Autowired
    private ModuleServiceImpl moduleService;

    /**
     *
     * @Description: 后台-模块-列表
     * @auther: 快乐水 樱桃可乐
     * @date: 2:29 2018/10/4
     * @param: [pageNum]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @GetMapping(value = "/modules")
    public Result getModules(@Range(min=1,message = "页码必须是大于1的数字")
                                 @NotNull(message = "页码不能为空")
                                 @RequestParam(value = "pageNum",required = false,defaultValue ="1" )
                                         Integer pageNum,
                                     @Range(min=1,message = "页面尺寸必须是大于1的数字")
                                 @NotNull(message = "页面尺寸不能为空")
                                 @RequestParam(value = "pageSize",required = false) Integer pageSize
    ){
        List<Module> modules = moduleService.selectAll(pageNum,pageSize);
        PageInfo pageInfo =new PageInfo(modules);
        Map map=new HashMap();
        map.put("modules",modules);
        map.put("pageTotal",pageInfo.getTotal());
        return new Result<>(true,map);
    }

    /**
     *
     * @Description: 后台-模块-多条件
     * @auther: 快乐水 樱桃可乐
     * @date: 9:09 2018/10/4
     * @param: [moduleID, moduleName, moduleFatherName, createdBy, pageNum]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @GetMapping(value = "/modulessearch")
    public Result getModulesSearch(@RequestParam(name = "serialId",required = false) String moduleID,
                                   @RequestParam(name = "moduleName",required = false) String moduleName,
                                   @RequestParam(name = "moduleFatherName",required = false) String moduleFatherName,
                                   @RequestParam(name = "createdBy",required = false) String createdBy,
                                   @Range(min=1,message = "页码必须是大于1的数字")
                                       @NotNull(message = "页码不能为空")
                                       @RequestParam(value = "pageNum",required = false,defaultValue ="1" )
                                               Integer pageNum,
                                   @Range(min=1,message = "页面尺寸必须是大于1的数字")
                                       @NotNull(message = "页面尺寸不能为空")
                                       @RequestParam(value = "pageSize",required = false) Integer pageSize
    ){
        ModuleDynamic moduleDynamic=new ModuleDynamic();
        moduleDynamic.setModuleId(moduleID);
        moduleDynamic.setCreatedBy(createdBy);
        moduleDynamic.setModuleFatherName(moduleFatherName);
        moduleDynamic.setModuleName(moduleName);
        System.out.println(moduleDynamic);
        List<Module> modules = moduleService.selectDynamic(moduleDynamic,pageNum,pageSize);
        PageInfo pageInfo =new PageInfo(modules);
        Map map=new HashMap();
        map.put("modules",modules);
        map.put("pageTotal",pageInfo.getTotal());
        return new Result<>(true,map);
    }

    /**
     *
     * @Description: 后台-模块-添加模块
     * @auther: 快乐水 樱桃可乐
     * @date: 15:27 2018/10/4
     * @param: [module, result, request]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @PostMapping(value = "/module")
    public Result postModule(@Validated(AddModuleGroup.class)
                                 @RequestBody Module module,
                             BindingResult result,
                             HttpServletRequest request){
        //获取荷载
        Map map=JwtUtil.parseJWT(request.getHeader("JWT"));
        return moduleService.addModule(module,map);
    }

    /**
     *
     * @Description: 后台-模块-更新模块
     * @auther: 快乐水 樱桃可乐
     * @date: 15:27 2018/10/4
     * @param: [module, id, result, request]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @PutMapping(value = "/module/{id:\\d+}")
    public Result putModule(@RequestBody Module module,
                             @PathVariable long id,
                             BindingResult result,
                             HttpServletRequest request){
        //获取荷载
        Map map=JwtUtil.parseJWT(request.getHeader("JWT"));
        //设置id
        module.setId(id);
        return moduleService.updateModule(module,map);
    }

    /**
     *
     * @Description: 后台-模块-删除模块
     * @auther: 快乐水 樱桃可乐
     * @date: 15:27 2018/10/4
     * @param: [id]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @DeleteMapping(value = "/module/{id:\\d+}")
    public Result deleteModule(@PathVariable long id){
        return moduleService.deleteModule(id);
    }

    /**
     *
     * @Description: 编辑显示接口
     * @auther: 快乐水 樱桃可乐
     * @date: 11:22 2018/10/22
     * @param: [id]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @GetMapping(value = "/module/{id:\\d+}")
    public Result getModule(@PathVariable long id){
        return new Result<>(true,moduleService.selectModuleByModuleId(id));
    }

}
