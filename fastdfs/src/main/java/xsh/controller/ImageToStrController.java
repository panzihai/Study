package xsh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xsh.service.ImageToStrService;

/**
 * 图片转成字符画
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/1/29 16:21
 */
@RestController
@RequestMapping("/imageToStr")
public class ImageToStrController {

    @Autowired
    private ImageToStrService imageToStr;

    /**
     * jacob方式
     */
    @RequestMapping("/createPic")
    public void createPic() {
        String path = "G://data/test/tiger.jpg";
        imageToStr.createAsciiPic(path);
    }


}
