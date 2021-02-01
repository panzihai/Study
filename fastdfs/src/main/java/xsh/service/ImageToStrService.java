package xsh.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图片转成字符画
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/2/1 14:32
 */
@Service
public class ImageToStrService {

    /**
     * 生成字符画
     *  建议图片尺寸不要太大，长宽不要超过200px效果最佳
     *  参考文档：https://jingyan.baidu.com/article/295430f1946b4e0c7e00503c.html
     *
     *  整体思想是：
     *    ->读取图片
     *    ->读取图片的每个像素点的rgb信息
     *    ->将每个像素的rgb信息转化成对应的灰度值
     *    ->将灰度值分成11等分
     *    ->每份对应一个字符
     *    ->得到字符画
     *
     * @param path
     */
    public void createAsciiPic(final String path) {
        //final String base = "@#&$%*o!;.";//字符串由复杂到简单,顺序和后面灰度值的顺序是相对应的
        final String base = "KSPksp;.";
        try {
            //读取图片
            final BufferedImage image = ImageIO.read(new File(path));
            //图片的高和宽
            System.out.println("W:"+image.getWidth()+" H:"+image.getHeight());
            //读取每个像素
            for (int y = 0; y < image.getHeight(); y += 2) {
                for (int x = 0; x < image.getWidth(); x++) {
                    //通过像素点位置(x,y)得到这个点ARGB像素值，并将ARGB像素值存储到rgbData数组中，
                    //rgbData数组中的数据是以0xAARRGGBB格式存储的，也就是十六进制储存，所以getRGB()返回一个8位整数
                    final int pixel = image.getRGB(x, y);
                    //r为红色值
                    final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
                    //将rgb值转化成灰度值,gray值在0-255
                    final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
                    //index值越小，颜色越黑, 范围是0-11
                    final int index = Math.round(gray * (base.length() + 1) / 255);
                    //index值超出范围的用空白代替
                    System.out.print(index >= base.length() ? " " : String.valueOf(base.charAt(index)));
                }
                System.out.println();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }



}
