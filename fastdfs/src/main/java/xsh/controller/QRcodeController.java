package xsh.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xsh.Utils.QRCodeUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成二维码
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/1/29 9:39
 */
@RestController
@RequestMapping("/qrCode")
public class QRcodeController {

    /**
     * 二维码(带图片)
     * @throws Exception
     */
    @RequestMapping("/codeLogo")
    public void codeLogo() throws Exception {
        String codeName = "code1-dog";  //二维码的图片名
        String text = "https://baijiahao.baidu.com/s?id=1664865957781775537&wfr=spider&for=pc";  //设置自定义网站url或文字
        String logoPath = "G://data/test/tiger.jpg"; //二维码图片
        String destPath = "G://data/test/";		//保存地址
        //调用工具类
        QRCodeUtils.encode(codeName, text, logoPath, destPath, true);
        //生成二维码地址
        System.out.println(destPath + codeName + ".jpg") ;
}


    /**
     * 纯二维码(不带图片)
     * @return
     */
    public void code() {
        String content= "https://blog.csdn.net/qq_45777315";  //设置自定义网站url或文字
        String path= "G://data/test/";		//保存地址
        try {
            String codeName = "code2-dog";
            String imageType = "jpg";//图片类型
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200, hints);
            File file1 = new File(path, codeName + "." + imageType);
            MatrixToImageWriter.writeToFile(bitMatrix, imageType, file1);
            //生成二维码地址
            System.out.println(path + codeName + "."+imageType) ;
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
