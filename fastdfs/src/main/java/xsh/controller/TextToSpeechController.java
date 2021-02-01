package xsh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xsh.service.ImageToTextService;
import xsh.service.TextToSpeechService;

/**
 * 文字转语音播放
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/1/29 16:57
 */
@RestController
@RequestMapping("/textToSpeech")
public class TextToSpeechController {

    @Autowired
    private TextToSpeechService textToSpeech;

    /**
     * jacob方式
     */
    @RequestMapping("/jacob")
    public void jacob() {
        String content = "打工人,打工魂,打工人上人！"; //文本内容
        String filePath = "G://data/test/jacob.wav"; //生成wav文件
        textToSpeech.jacob(content, filePath);
    }

    /**
     * 百度ai方式
     */
    @RequestMapping("/baiduAI")
    public void baiduAI() {
        String content = "二月不会对你好，转账我20 我对你好！";
        String filePath = "G://data/test/baidu.mp3";
        textToSpeech.baiduAI(content, filePath);
    }


    /**
     * 功能整合：
     *  图片-》文字-》语音
     */
    @Autowired
    private ImageToTextService toTextService;

    @RequestMapping("/baidu_text_speech")
    public void baiduTextSpeech() {
        String imagePath = "G://data/test/word_3.jpg"; //图片地址
        //获取图片文字
        String content = toTextService.baiduAI(imagePath);
        System.out.println(content);
        String filePath = "G://data/test/word_3.mp3"; //生成map3文件
        //文字转语音
        textToSpeech.baiduAI(content, filePath);
    }


}
