package client.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtil {

    public static <T> T jsonToObjcet(String json) {
        try {
            return JSONObject.parseObject(json, new TypeReference<T>() {
            }, new Feature[]{Feature.AllowISO8601DateFormat});
        } catch (Exception var2) {
            var2.printStackTrace();
            return null; 
        }
    }

    public static String objectToJson(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
    }
}
