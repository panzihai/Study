package xsh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xsh.service.ImageToTextService;

/**
 * 图片识别文字
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/1/29 13:36
 */
@RestController
@RequestMapping("/toText")
public class ImageToTextController {

    @Autowired
    private ImageToTextService toTextService;

    /**
     * 百度ai方式
     */
    @RequestMapping("/baiduAI")
    public void baiduAI() {
        String imagePath = "G://data/test/world_1.jpg"; //图片地址
        String text = toTextService.baiduAI(imagePath);
        System.out.println(text);
    }

    /**
     * tess4j方式
     */
    @RequestMapping("/tess4j")
    public void tess4j() {
        String imagePath = "G://data/test/world_1.jpg";
        String text = toTextService.tess4j(imagePath);
        System.out.println(text);
    }
}
