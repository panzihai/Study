package xsh.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 配置类 加载属性
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/1/28 19:54
 */
@Data //简写set/get
//@Component //交由IOC容器创建bean
@ConfigurationProperties(prefix="upload") //属性加载
public class FastdfsProperties {

    //图片上传成功后, 可访问的跟目录
    private String baseUrl;

    //图片类型列表
    private List<String> allowTypes;
}
