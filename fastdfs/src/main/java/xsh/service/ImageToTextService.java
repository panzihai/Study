package xsh.service;

import com.baidu.aip.ocr.AipOcr;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import xsh.domain.Tesseract2;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 图片识别文字
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/1/29 13:38
 */
@Service
public class ImageToTextService {

    //申请APPID/AK/SK：https://console.bce.baidu.com/ai/?fromai=1#/ai/ocr/app/list
    public static final String APP_ID = "23609702";
    public static final String API_KEY = "XxA9vpPHGq5Bc5nY0UFIhoba";
    public static final String SECRET_KEY = "0u0w9cxdHgbq8z88oNWkduQaFY0yqW67";

    /**
     * 百度ai：
     *  参考文档：https://blog.csdn.net/qq_42944520/article/details/88814104
     * @param imagePath
     * @return
     */
    public String baiduAI(String imagePath) {
        String result = "";
        try {
            // 初始化一个AipOcr
            AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

            // 可选：设置网络连接参数
            client.setConnectionTimeoutInMillis(2000);
            client.setSocketTimeoutInMillis(60000);

            // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
            // client.setHttpProxy("proxy_host", proxy_port); // 设置http代理
            // client.setSocketProxy("proxy_host", proxy_port); // 设置socket代理

            // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
            // 也可以直接通过jvm启动参数设置此环境变量
            // System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

            // 传入可选参数调用接口
            HashMap<String, String> options = new HashMap<String, String>();
            // options.put("language_type", "CHN_ENG");
            // options.put("detect_direction", "true");
            // options.put("detect_language", "true");
            // options.put("probability", "true");

            JSONObject res = client.basicGeneral(imagePath, options);
            // 通用文字识别, 图片参数为远程url图片
            // JSONObject res = client.basicGeneralUrl(url, options);
            System.err.println(res.toString(2));// 转换的文字

            // 提取文字
            // 获取到json数组
            org.json.JSONArray jsonArray = res.getJSONArray("words_result");
            // 创建一个字符串容器
            StringBuffer sb = new StringBuffer();
            Iterator iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                JSONObject type = (JSONObject) iterator.next();
                sb.append(type.getString("words"));
            }
            result = sb.toString();
        } catch (Exception e) {
            result = "";
        }
        return result;
    }


    /**
     * 使用tess4j方式
     *  参考文档：https://www.cnblogs.com/fengyuduke/p/11976234.html
     *  chi_sim.traineddata下载：https://codechina.csdn.net/mirrors/tesseract-ocr/tessdata?utm_source=csdn_github_accelerator
     *           见【百度网盘/笔记/File】
     *
     * @param filePath
     * @return
     */
    public String tess4j(String filePath) {
        //加载待读取图片
        File imageFile = new File(filePath);
        //创建tess对象
        Tesseract2 instance = new Tesseract2();
        //设置训练文件目录
        instance.setDatapath("G://data/tessdata");
        //设置训练语言
        instance.setLanguage("chi_sim");

        String result = "";
        //执行转换
        try {
            result = instance.doOCR(imageFile);
            if(StringUtils.isNoneEmpty(result)) {
                //匹配换行符
                result = result.replaceAll("(\\s*[\t|\r|\n]){2,}\\s*","\n");
            }

        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return result;
    }




}
