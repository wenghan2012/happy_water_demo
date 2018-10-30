package com.demo.zuul.utils;

import com.demo.zuul.accessfilter.BackstageFilter;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 18:06 2018/9/28
 * @Modified By:
 */
public class JwtUtil {

    private static Logger logger = LoggerFactory.getLogger(BackstageFilter.class);

    /**
     * 解密
     * @param jsonWebToken
     * @param base64Security
     * @return
     */
    public static Claims parseJWT(String jsonWebToken, String base64Security) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(base64Security.getBytes())
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        } catch (Exception ex) {
            logger.info("解密异常");
            return null;
        }
    }

    /**
     * 前三个参数为自己用户token的一些信息比如id，权限，名称等。不要将隐私信息放入（大家都可以获取到）
     * @param map
     * @param base64Security
     * @return
     */
    public static String createJWT(Map<String, Object> map, String base64Security) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .setPayload(new Gson().toJson(map))
                //估计是第三段密钥
                .signWith(signatureAlgorithm,base64Security.getBytes());
        //生成JWT
        return builder.compact();
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("province", "898765");
        map.put("city", "898765");
        map.put("appkey", "HMu1H/cmyKDOiHv41Y9lLROuOlOo+PPG8F4/RotRmNc=");
        map.put("timestamp", System.currentTimeMillis());

        //密钥
        String keyt = "Happywater";
        String token=JwtUtil.createJWT(map, keyt);
        System.out.println("JWT加密的结果："+ token);
        System.out.println("JWT解密的结果："+ parseJWT(token, keyt));
        Map map1=parseJWT(token,keyt);
        System.out.println(map1.get("city"));
    }
}
