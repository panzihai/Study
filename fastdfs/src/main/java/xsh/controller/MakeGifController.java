package xsh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xsh.service.MakeGifService;

import javax.servlet.http.HttpServletResponse;

/**
 * 动态图gif相关操作
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/2/1 15:23
 */
@Controller //使用@RestController会直接返回字符串
@ResponseBody
@RequestMapping("/makeGif")
public class MakeGifController {

    @Autowired
    private MakeGifService makeGif;

//    /**
//     * 获取普通验证码
//     *   通过Controller跳转访问html页面
//     *
//     */
//    @RequestMapping("/gifcode")
//    static String gifcode() {
//        return "/gifcode";
//    }

    /**
     * 获取普通验证码
     *
     */
    @RequestMapping("/jpgCode")
    public String jpgCode(HttpServletResponse response) {
        makeGif.jpgCode(response);
        return "/gifcode";
    }

    /**
     * 获取gif动态验证码
     *
     */
    @RequestMapping("/gifCode")
    public String gifCode(HttpServletResponse response) {
        makeGif.gifCode(response);
        return "/gifcode";
    }


    /**
     *  将多张jpg图片 合成.gif动态图
     */
    @RequestMapping("/jpgToGif")
    public void jpgToGif() {
        /**
         * 多张jpg静态图
         * 1)图片大小最好一致
         */
        String[] picsPath = {
                "G://data/test/tiger.jpg",
                "G://data/test/dog.jpg",
                "G://data/test/dinosaur.jpeg"};
        //生成的gif图
        String gifPath = "G://data/test/1.gif";
        makeGif.jpgToGif(picsPath, gifPath);
    }


    /**
     * 将gif拆分成多张jpg图
     * 1)JPG可以包含bai6万多种颜色，而GIF只有256种颜色，所以JPG转换成GIF后都会产生色偏
     *
     */
    @RequestMapping("/gifToJpg")
    public void gifToJpg() {
        //git图地址
        String gifPath = "G://data/test/1.gif";
        //生成的jpg
        String jpgPath = "G://data/test/";
        String jpgName = "gifToJpg.jpg";
        makeGif.gifToJpg(gifPath, jpgPath, jpgName);
    }
}
