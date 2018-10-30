package com.demo.rbac.utils;


/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 16:45 2018/9/24
 * @Modified By:
 */
public class ValidatorUtils {

    private static final String pwdRegular="(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{6,18}$";
    /**
     *
     * @Description: 参数校验：字符串类型判空、判null
     * @auther: 快乐水 樱桃可乐
     * @date: 16:51 2018/9/24
     * @param: [str]
     * @return: boolean
     *
     */
    public static boolean StringValidator(String ...str){
        for (String aStr : str) {
            //判断空字符串||判参数为null
            if ("".equals(aStr)||aStr==null) {
                return true;
            }
        }
        return false;
    }


    /**
     *
     * @Description: 密码正则表达式校验
     * @auther: 快乐水 樱桃可乐
     * @date: 22:34 2018/9/24
     * @param: [str]
     * @return: boolean
     *
     */
    public static boolean PwdValidator(String ...str){
        for (String aStr : str) {
            //正则表达式验证
            if (RegexUtils.isMatch(pwdRegular,aStr)) {
                return false;
            }
        }
        return true;
    }

    public static boolean pwdCorrespond(String pwd1,String pwd2){
        if(pwd1.equals(pwd2)){
            return false;
        }
        return true;
    }
}
