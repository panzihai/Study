package xsh.service;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.madgag.gif.fmsware.GifDecoder;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.springframework.stereotype.Service;
import xsh.Utils.GifCodeUtil;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;


/**
 * 动态图gif相关操作
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/2/1 15:25
 */
@Service
public class MakeGifService {


    /**
     * 生成 普通验证码
     * @param response
     * @return
     */
    public void jpgCode(HttpServletResponse response) {
        response.reset();
        try {
            //生成验证码图片,4个字符
            BufferedImage img = GifCodeUtil.createImageCode(4);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(img, "jpg", imOut);
            InputStream is = new ByteArrayInputStream(bs.toByteArray());

            response.setContentType("image/jpg");
            OutputStream out = response.getOutputStream();
            int b = 0;
            byte[] buffer = new byte[1024];
            b = is.read(buffer);
            while (b != -1) {
                //写到输出流(out)中
                out.write(buffer, 0, b);
                b = is.read(buffer);
            }
            is.close();
            out.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * 生成 动态验证码
     * @param response
     * @return
     */
    public void gifCode(HttpServletResponse response) {
        //定义文件字节输入流
        FileInputStream fileIn = null;
        //定义字节输入流
        InputStream is = null;
        //定义文件对象
        File file = null;
        try {
            //创建临时文件
            file = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".gif");
            //实例化jpg合成gif组件
            AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
            gifEncoder.setRepeat(0); //重复次数
            gifEncoder.start(file.getCanonicalPath()); //合成位置
            //生成验证码图片,4个字符
            BufferedImage[] src = GifCodeUtil.createImageCodes(4);
            for (int i = 0; i < src.length; i++) {
                gifEncoder.setDelay(300); //播放的延迟时间
                gifEncoder.addFrame(src[i]); //添加到帧中
            }
            gifEncoder.finish();
            fileIn = new FileInputStream(file);
            is = fileIn;
        } catch (Exception e) {
            System.out.println("生成jap动态验证码失败:"+e.getMessage()) ;
        }

        try {
            response.setContentType("image/gif");
            OutputStream out = response.getOutputStream();
            int b = 0;
            byte[] buffer = new byte[1024];
            b = is.read(buffer);
            while (b != -1) {
                // 写到输出流(out)中
                out.write(buffer, 0, b);
                b = is.read(buffer);
            }
            is.close();
            out.close();
            // 因为返回时InputStream已经关闭，所以不需要关闭文件字节输入流
            fileIn.close();
            // 删除临时文件
            file.delete();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


    /**
     * 把多张jpg图片合成一张
     * @param pics
     * @param gifPath
     */
    public void jpgToGif(String[] pics, String gifPath) {
        try {
            AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
            gifEncoder.setRepeat(0);  //重复次数
            gifEncoder.start(gifPath); //合成位置(生成gif图)
            BufferedImage src[] = new BufferedImage[pics.length];
            for (int i = 0; i < src.length; i++) {
                //延迟时间
                gifEncoder.setDelay(500);
                //读取jpg图片
                src[i] = ImageIO.read(new File(pics[i]));
                //添加到帧中
                gifEncoder.addFrame(src[i]);
            }
            gifEncoder.finish();
        } catch (IOException e) {
            System.out.println("生成gif图失败!") ;
            e.printStackTrace();
        }
    }


    /**
     * 将gif动态图拆成多张jpg图
     * @param gifPath
     * @param jpgPath
     */
    public void gifToJpg(String gifPath, String jpgPath, String jpgName) {
        try {
            GifDecoder decoder = new GifDecoder();
            FileInputStream is = new FileInputStream(gifPath);
            if (decoder.read(is) != 0) {
                System.out.println("读取有误！") ;
                return;
            }
            is.close();

            System.out.println("帧数=" + decoder.getFrameCount()) ;
            for (int i = 0; i < decoder.getFrameCount(); i++) {
                BufferedImage frame = decoder.getFrame(i);
                int delay = decoder.getDelay(i);
                System.out.println("延迟时间：" + delay) ;
                //生成jpg图
                OutputStream out = new FileOutputStream(jpgPath + i + jpgName);
                ImageIO.write(frame, "jpeg", out);
                JPEGCodec.createJPEGEncoder(out);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
