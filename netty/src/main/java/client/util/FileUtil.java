package client.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {

    public static byte[] readFileByByte(String path) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
        //读取文件内容
        byte[] b = new byte[bis.available()];
        bis.read(b);
        //关闭流(关闭bis就可以了)
        bis.close(); 

        return b;
    }
}
