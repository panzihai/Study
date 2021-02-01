package xsh.service;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadFileStream;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xsh.config.FastdfsProperties;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

/**
 * TODO
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/1/28 20:12
 */
@Service
@Slf4j
@EnableConfigurationProperties(FastdfsProperties.class) //使@ConfigurationProperties注解的类生效
public class FastdfsService {

    @Resource
    private FastFileStorageClient storageClient;

    @Autowired
    private FastdfsProperties pro; //注入配置类

    /**
     * 上传图片
     * @param file
     * @return
     */
    public String  uploadImage(MultipartFile file) {
        //图片类型
        String type = file.getContentType();
        if(!pro.getAllowTypes().contains(type)) {
            throw  new RuntimeException("图片类型不支持!");
        }

        try {
            //图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image==null || image.getWidth()==0 || image.getHeight()==0){
                throw  new RuntimeException("图片内容有问题!");
            }
        } catch (Exception e) {
            log.error("校验图片内容失败....{}", e);
            throw new RuntimeException("校验图片内容失败"+e.getMessage());
        }

        //上传到fastdfs
        try {
            //1后缀
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            /**
             * 上传
             *  1) 输入流
             *  2) 文件大小
             *  3) 扩展名(后缀)
             *  4) 文件元数据
             */
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            //3返回路径
            return pro.getBaseUrl() + storePath.getFullPath();
        } catch (Exception e) {
            System.out.println(e.getMessage()) ;
        }

        return "";
    }


    /**
     * 下载
     */
    public void downloadFile(String fileId, OutputStream outputStream) {
        String group = fileId.substring(0, fileId.indexOf("/"));
        String path = fileId.substring(fileId.indexOf("/") + 1);
        DownloadFileStream downloadFileStream = new DownloadFileStream(outputStream);
        /**
         * 下载
         *  1)分组
         *  2)路径
         *  3)回调函数
         */
        storageClient.downloadFile(group, path, downloadFileStream);
    }
}
