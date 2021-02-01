package xsh.service;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

/**
 * 文字转语音播放
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/2/1 14:10
 */
@Service
public class TextToSpeechService {


    /**
     * jacob方式
     *  实现调用windows自带控件 实现文字转语音播放功能！
     *  参考文档：https://www.cnblogs.com/coder-wzr/p/11321754.html
     *  修改为男声播放：https://blog.csdn.net/ming19951224/article/details/81046488
     */
    public void jacob(String content, String filePath) {
        ActiveXComponent ax = null;
        try {
            ax = new ActiveXComponent("Sapi.SpVoice");
            //运行时输出语音内容
            Dispatch spVoice = ax.getObject();
            //音量0-100
            ax.setProperty("Volume", new Variant(100));
            //语音朗读速度-10到+10
            ax.setProperty("Rate", new Variant(-2));
            //执行朗读
            Dispatch.call(spVoice, "Speak", new Variant(content));

            //下面是构建文件流把生成语音文件
            ax = new ActiveXComponent("Sapi.SpFileStream");
            Dispatch spFileStream = ax.getObject();

            ax = new ActiveXComponent("Sapi.SpAudioFormat");
            Dispatch spAudioFormat = ax.getObject();

            //设置音频流格式
            Dispatch.put(spAudioFormat, "Type", new Variant(22));
            //设置文件输出流格式
            Dispatch.putRef(spFileStream, "Format", spAudioFormat);
            //调用输出 文件流打开方法，创建一个.wav文件
            Dispatch.call(spFileStream, "Open", new Variant(filePath), new Variant(3), new Variant(true));
            //设置声音对象的音频输出流为输出文件对象
            Dispatch.putRef(spVoice, "AudioOutputStream", spFileStream);
            //设置音量 0到100
            Dispatch.put(spVoice, "Volume", new Variant(100));
            //设置朗读速度
            Dispatch.put(spVoice, "Rate", new Variant(-2));
            //开始朗读
            Dispatch.call(spVoice, "Speak", new Variant(content));

            //关闭输出文件
            Dispatch.call(spFileStream, "Close");
            Dispatch.putRef(spVoice, "AudioOutputStream", null);

            spAudioFormat.safeRelease();
            spFileStream.safeRelease();
            spVoice.safeRelease();
            ax.safeRelease();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    //申请APPID/AK/SK：https://blog.csdn.net/October_zhang/article/details/112529712
    public static final String APP_ID = "23620172";
    public static final String API_KEY = "G0pWtaqhTXnHfF2rbcQqlK63";
    public static final String SECRET_KEY = "PefS8q1YWE6qKZGbdy4Vtx5gDlCU6Kyu";
    /**
     * 百度AI
     *  在线文档：https://ai.baidu.com/ai-doc/SPEECH/wk4nlz4li
     *  参考文档：https://blog.csdn.net/belonghuang157405/article/details/81707858
     */
    public void baiduAI(String content, String filePath) {
        //AipSpeech是百度语音的客户端,认证成功之后,客户端将被开启,这里的client就是已经开启的百度语音的客户端了
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 设置可选参数
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("spd", "3");//语速，取值0-9，默认为5中语速(值越大越快)      非必选
        options.put("pit", "5");//音调，取值0-9，默认为5中语调      非必选
        options.put("vol", "4"); //音量，取值0-15，默认为5中音量
        options.put("per", "4");//发音人选择, 0为女声，1为男声，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女 非必选
        options.put("aue", "3"); //下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
        /**
         * text: 合成的文本,使用UTF-8编码,请注意文本长度必须小于1024字节
         * lang:语言,中文:zh,英文:en
         * ctp:客户端信息这里就写1
         * options:这是一个dict类型的参数,里面的键值对才是关键
         */
        TtsResponse res = client.synthesis(content, "zh", 1, options);

        //生成的音频数据
        byte[] data = res.getData();
        JSONObject res1 = res.getResult();
        if (data != null) {
            try {
                Util.writeBytesToFileSystem(data, filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (res1 != null) {
            System.out.println(res1.toString(2));
        }

    }
}
