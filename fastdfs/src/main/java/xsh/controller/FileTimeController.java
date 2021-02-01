package xsh.controller;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import it.sauronsoftware.jave.VideoSize;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * 获取视频时长和大小
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/1/29 9:45
 */
public class FileTimeController {
    public static void main(String[] args) {
//        File source = new File("F:\\\\360Download\\MP4拍照.mp4");
        File source = new File("F:\\\\360Download\\1969.wav");

        Encoder encoder = new Encoder();
        try {
            MultimediaInfo info = encoder.getInfo(source);
            //获取视频时间
            long time = info.getDuration();

            //方式一：
            long hour = time/(60*60*1000);
            long minute = (time - hour*60*60*1000)/(60*1000);
            long second = (time - hour*60*60*1000 - minute*60*1000)/1000;
            System.out.println("视频时长:"+formatTime(hour)+":"+formatTime(minute)+":"+formatTime(second));

            //方式二：
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00")); //时区偏移量
            String hms = formatter.format(time);
            System.out.println(hms);

            //视频大小
            FileInputStream fis = new FileInputStream(source);
            FileChannel fc = fis.getChannel();
            BigDecimal fileSize = new BigDecimal(fc.size());
            String size = fileSize.divide(new BigDecimal(1048576), 2, RoundingMode.HALF_UP) + "MB";
            System.out.println("视频大小：" + size) ;

            //获取视频格式
            String format = info.getFormat();
            System.out.println("视频格式:" + format) ;

            //视频宽度高 和 长高
            VideoSize videoSize = info.getVideo().getSize();
            System.out.println("视频宽高:" + videoSize.getWidth()+",视频长高:" + videoSize.getHeight()) ;

        }catch (Exception e) {
            System.out.println("异常信息:"+e.getMessage());
        }
    }

    /**
     * 格式化时间
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        return time<10? "0"+time : time+"";
    }


}
