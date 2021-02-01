package xsh.base64;

import java.util.UUID;

/**
 * TODO
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/1/29 11:12
 */
public class Base64Test {
    public static void main(String[] args) throws  Exception{
        String u1 = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println("原密码：" + u1) ;
        String u2 = Base64Utils.Base64Encode(u1);
        System.out.println("加密：" + u2) ;
        String u3 = Base64Utils.Base64Decode(u2);
        System.out.println("解密：" + u3) ;
    }
}
