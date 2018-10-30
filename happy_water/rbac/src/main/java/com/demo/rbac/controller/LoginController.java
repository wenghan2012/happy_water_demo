package com.demo.rbac.controller;


import com.demo.rbac.entity.Manager;
import com.demo.rbac.entity.Result;
import com.demo.rbac.entity.managerValidateGroup.LoginGroup;
import com.demo.rbac.service.Impl.LoginServiceImpl;
import com.demo.rbac.service.Impl.ManagerServiceImpl;
import com.demo.rbac.service.Impl.ModuleServiceImpl;
import com.demo.rbac.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 10:27 2018/9/25
 * @Modified By:
 */
@RestController
@RequestMapping(produces = "application/json")
public class LoginController {

    private static final Logger logger=LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private ManagerServiceImpl managerService;

    @Autowired
    private LoginServiceImpl loginService;

    @Autowired
    private ModuleServiceImpl moduleService;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     *
     * @Description: 后台-登录
     * @auther: 快乐水 樱桃可乐
     * @date: 10:31 2018/9/25
     * @param: [account, password]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @PostMapping(value = "/login")
    public Result postLogin(@Validated({LoginGroup.class}) @RequestBody Manager manager,
                            BindingResult bindingResult,
                            HttpServletResponse response
                            ){
        //获取登录结果
        Result result=managerService.login(manager);
        //判断是否登录成功
        if(result.getStatus()){
            //获取JWT
            String jwt=loginService.getJwt(manager.getAccount());
            //封装到响应头中
            response.setHeader("JWT",jwt);
            //获取账户id、角色名、角色等级、角色id
            Manager managerDb = managerService.selectManagerByAccount(manager.getAccount());
            //登录成功日志记录登录账户的id
            logger.info("登录成功，登录的用户是： "+managerDb.getId());
            //将账户保存到结果的data中，供前端使用
            result.setData(managerDb);
            //后台以backstageId外键，将JWT存入redis缓存中,用于保存着登录状态，存在即对应的JWT有效
            redisTemplate.opsForValue().set("backstageId"+managerDb.getId(),jwt);
        }
        return result;
    }

    /**
     *
     * @Description: 后台-注销
     * @auther: 快乐水 樱桃可乐
     * @date: 10:34 2018/9/25
     * @param: [managerId]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @GetMapping(value = "/logout/{managerId:\\d+}")
    public Result getLogout(@PathVariable long managerId,
                            HttpServletRequest request){
        //从请求头中获取Header中的JWT
        String jwt=request.getHeader("JWT");
        //解密jwt
        Map map=JwtUtil.parseJWT(jwt);
        //获取荷载中的账户ID
        Integer id=(Integer)map.get("backstageId");
        logger.info("当前执行注销操作的id: "+id);
        logger.info("需要被注销的id： "+managerId);
        if(id!=managerId){
            return new Result(false,"登出用户与操作用户不对应",704);
        }
        //注销成功将注销的账户打到日志中
        logger.info("注销的账户是： "+id);
        //注销操作本质删除掉redis中的缓存
        redisTemplate.delete("backstageId"+managerId);
        return new Result(true,"注销成功");
    }

    /**
     *
     * @Description: 获取左边栏
     * @auther: 快乐水 樱桃可乐
     * @date: 13:09 2018/10/6
     * @param: [id]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @GetMapping(value = "/modules/{managerId:\\d+}")
    public Result getModules(@PathVariable long managerId,
                             HttpServletRequest request){
        String jwt=request.getHeader("JWT");
        Map map=JwtUtil.parseJWT(jwt);
        Integer backstageId=(Integer) map.get("backstageId");
        if(managerId!=backstageId){
            return new Result(false,"不能获取前其他账户的左边栏");
        }
        return new Result<>(true,moduleService.selectModuleList(managerId));
    }
}
