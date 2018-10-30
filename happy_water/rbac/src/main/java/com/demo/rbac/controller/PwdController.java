package com.demo.rbac.controller;


import com.demo.rbac.entity.Manager;
import com.demo.rbac.entity.Result;
import com.demo.rbac.service.ManagerService;
import com.demo.rbac.utils.JwtUtil;
import com.demo.rbac.utils.Md5Util;
import com.demo.rbac.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 16:29 2018/9/24
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/background/password",produces = "application/json")
public class PwdController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     *
     * @Description: 后台：后台管理：密码管理：更换密码接口
     * @auther: 快乐水 樱桃可乐
     * @date: 16:40 2018/9/24
     * @param: [confirmPassword, newPassword, usedPassword]
     * @return: com.example.demorbac.entity.Result
     *
     */
    @PutMapping(value = "/password/{managerId}")
    public Result putPassword(@RequestParam String confirmPassword,
                              @RequestParam String newPassword,
                              @RequestParam String usedPassword,
                              @PathVariable long managerId,
                              HttpServletRequest request){
        if(ValidatorUtils.StringValidator(confirmPassword,newPassword,usedPassword)){
            return new Result(false,"密码不能为空",801);
        }
        if(ValidatorUtils.PwdValidator(newPassword)){
            return new Result(false,"新密码格式错误，请重试。",802);
        }
        if(ValidatorUtils.pwdCorrespond(newPassword,usedPassword)){
            return new Result(false,"新密码二次输入不一致，请重试。",803);
        }

        String jwt=request.getHeader("JWT");
        Map map=JwtUtil.parseJWT(jwt);
        Integer backstageId=(Integer) map.get("backstageId");

        //判断被修改密码的账户与当前操作者的一致性
        if(backstageId!=managerId){
            return new Result(false,"修改的账户与当前操作者不符",804);
        }
        //根据账号id查询密码与盐
        Manager managerDB=managerService.selectPasswordAndSaltByManagerId(managerId);
        //判断原来的密码是否正确
        if(!managerDB.getPassword().equals(Md5Util.getMD5String(confirmPassword+managerDB.getSalt()))){
            return new Result(false,"旧密码输入错误，请重试");
        }

        Manager manager=new Manager();
        manager.setId(managerId);
        manager.setSalt(UUID.randomUUID()+"");
        manager.setPassword(Md5Util.getMD5String(newPassword+manager.getSalt()));

        managerService.updatePwdById(manager);

        //删除掉redis中的缓存，使原密码获得的JWT无效
        redisTemplate.delete(managerId);

        return new Result(true,"更换密码成功");
    }
}
