package cn.auntec.framework.components.util.value.jwt;

import cn.auntec.framework.components.util.value.data.ObjectUtil;
import cn.auntec.framework.components.util.value.data.StrUtil;
import io.jsonwebtoken.*;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 蒋时华
 * @date 2020-06-28 11:13:35
 */
@Builder
@Getter
public class JwtUtil {

    /**
     * Token过期时间必须大于生效时间（秒）
     */
    private final Long tokenExpireTime;
    /**
     * Token加密解密的密码
     */
    private final String tokenSecret;
    /**
     * 加密类型 三个值可取 HS256  HS384  HS512
     */
    private final SignatureAlgorithm jwtAlg;
    /**
     * 添加一个前缀
     */
    private final String jwtSeparator;
    /**
     * token生效时间(默认是从当前开始生效)（秒）
     * 默认：new Date(System.currentTimeMillis() + START_TIME * 1000)
     */
    private final Long startTime;
    /**
     * token在什么时间之前是不可用的（默认从当前时间）（秒）
     * 默认：new Date(System.currentTimeMillis() + BEFORE_TIME * 1000)
     */
    private final Long beforeTime;

    public JwtUtil(Long tokenExpireTime
            , String tokenSecret
            , SignatureAlgorithm jwtAlg
            , String jwtSeparator
            , Long startTime
            , Long beforeTime) {
        // 默认三小时
        long tokenExpireTimeDefault = 10800L;
        this.tokenExpireTime = ObjectUtil.isNotNull(tokenExpireTime) ? tokenExpireTime : tokenExpireTimeDefault;

        String tokenSecretDefault = "auntec.cn";
        this.tokenSecret = ObjectUtil.isNotNull(tokenSecret) ? tokenSecret : tokenSecretDefault;

        SignatureAlgorithm jwtAlgDefault = SignatureAlgorithm.HS256;
        this.jwtAlg = ObjectUtil.isNotNull(jwtAlg) ? jwtAlg : jwtAlgDefault;

        String jwtSeparatorDefault = "Bearer#";
        this.jwtSeparator = ObjectUtil.isNotNull(jwtSeparator) ? jwtSeparator : jwtSeparatorDefault;

        long startTimeDefault = 0L;
        this.startTime = ObjectUtil.isNotNull(startTime) ? startTime : startTimeDefault;

        long beforeTimeDefault = 0L;
        this.beforeTime = ObjectUtil.isNotNull(beforeTime) ? beforeTime : beforeTimeDefault;
    }

    private Key generateKey() {
        // 将将密码转换为字节数组
        byte[] bytes = Base64.decodeBase64(tokenSecret);
        // 根据指定的加密方式，生成密钥
        String jwtType = jwtAlg.getJcaName();
        return new SecretKeySpec(bytes, jwtType);
    }

    private Long getMills(long time) {
        return 1000L * time;
    }

    /**
     * 创建token
     *
     * @param sub token所面向的用户
     * @param aud 接收token的一方
     * @param jti token的唯一身份标识，主要用来作为一次性token,从而回避重放攻击
     * @param iss token签发者
     * @param map 自定义信息的存储
     * @return 加密后的token字符串
     */
    public String createToken(String sub, String aud, String jti, String iss, Map<String, Object> map) {
        final JwtBuilder builder = Jwts.builder();
        if (!map.isEmpty()) {
            builder.setClaims(map);
        }
        String token = builder
                .signWith(jwtAlg, generateKey())
                .setSubject(sub)
                .setAudience(aud)
                .setId(jti)
                .setIssuer(iss)
                .setNotBefore(new Date(System.currentTimeMillis() + this.getMills(beforeTime)))
                .setIssuedAt(new Date(System.currentTimeMillis() + this.getMills(startTime)))
                .setExpiration(new Date(System.currentTimeMillis() + this.getMills(tokenExpireTime)))
                .compact();
        return jwtSeparator + token;
    }

    /**
     * 创建token
     *
     * @param sub token所面向的用户
     * @param aud 接收token的一方
     * @param map 自定义信息存储
     * @return token 字符串
     */
    public String createToken(String sub, String aud, Map<String, Object> map) {
        return createToken(sub, aud, new Date().toString(), null, map);
    }

    /**
     * 创建token
     *
     * @param sub token所面向的用户
     * @param map 自定义信息存储
     * @return token字符串
     */
    public String createToken(String sub, Map<String, Object> map) {
        return createToken(sub, null, map);
    }

    /**
     * 创建token
     *
     * @param sub token所面向的用户
     * @return token字符串
     */
    public String createToken(String sub) {
        return createToken(sub, null);
    }

    /**
     * 创建token
     *
     * @param map 自定义信息存储
     * @return token字符串
     */
    public String createToken(Map<String, Object> map) {
        return createToken(null, map);
    }

    /**
     * 解析token
     * 可根据Jws<Claims>   获取  header|body|getSignature三部分数据
     *
     * @param token token字符串
     * @return Jws
     */
    public Jws<Claims> parseToken(String token) {
        // 移除 token 前的"Bearer#"字符串
        token = StrUtil.subAfter(token, jwtSeparator, true);
        // 解析 token 字符串
        return Jwts.parser().setSigningKey(generateKey()).parseClaimsJws(token);
    }

    /**
     * 校验token,校验是否是本服务器的token
     *
     * @param token token字符串
     * @return boolean
     */
    public Boolean checkToken(String token) {
        return parseToken(token).getBody() != null;
    }

    /**
     * 根据sub判断token
     *
     * @param token token字符串
     * @param sub   面向的用户
     * @return boolean
     */
    public Boolean checkToken(String token, String sub) {
        return parseToken(token).getBody().getSubject().equals(sub);
    }

    public static void main(String[] args) {

        JwtUtil jwtUtil = JwtUtil.builder().build();

        Map<String, Object> map = new HashMap();
        map.put("userId", "1234567890");
        map.put("userName", "jphoebe");
        //创建token
        final String token = jwtUtil.createToken(map);
        System.out.println("token = " + token);

        //解析token
        final Jws<Claims> claimsJws = jwtUtil.parseToken(token);

        //header
        final JwsHeader header = claimsJws.getHeader();
        System.out.println("header.getAlgorithm() = " + header.getAlgorithm());
        System.out.println("header.getKeyId() = " + header.getKeyId());

        //body部分
        final Claims body = claimsJws.getBody();
        for (String s : body.keySet()) {
            System.out.print(s + "   ");
        }

        System.out.println("body.getSubject() = " + body.getSubject());
        System.out.println("body.getId() = " + body.getId());
        System.out.println("body.getExpiration() = " + body.getExpiration());
        System.out.println("body.getIssuedAt() = " + body.getIssuedAt());
        System.out.println("body.getNotBefore() = " + body.getNotBefore());
        System.out.println("body.getIssuer() = " + body.getIssuer());
        System.out.println("body.getAudience() = " + body.getAudience());

        //获取自定义信息
        System.out.println("body.get(\"userId\",String.class) = " + body.get("userId", String.class));
        System.out.println("body.get(\"userName\",String.class) = " + body.get("userName", String.class));

        //获取签名
        System.out.println("claimsJws.getSignature() = " + claimsJws.getSignature());


    }


}
