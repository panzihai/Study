package xsh.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xsh.service.FastdfsService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 *  图片/文件 控制类
 *
 * @author xushaohai
 * @version 1.0
 * @date 2021/1/28 20:06
 */
@RestController
@RequestMapping("/fastdfs")
public class FastdfsController {

    @Autowired
    private FastdfsService fastdfsService;

    @RequestMapping("/upload")
    public void upload(MultipartFile file) {
       String filePath = fastdfsService.uploadImage(file);
       System.out.println(StringUtils.isEmpty(filePath)? "上传失败":"上传成功："+filePath) ;
    }

    /**
     * 下载文件，避免内存泄漏
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(HttpServletResponse response) throws IOException {
        OutputStream outStream = null;
        try {
            outStream = response.getOutputStream();
            String fileId = "group1/M00/00/35/CgUcjmATclCAD-oOAACBPlT0COc591.jpg";
            fastdfsService.downloadFile(fileId, outStream);

            String fileExt = "大老斧.jpg";
            if (Arrays.asList("jpg", "png", "gif", "jpeg").contains(fileExt)) {
                response.addHeader("Content-Type", "image/" + fileExt);
            } else {
                response.addHeader("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
            }
        } finally {
            if (outStream != null) {
                outStream.close();
                outStream.flush();
            }
        }

    }



    /**
     * 下载
     *   不建议使用，因为该方法会将文件转换成byte数组，当大文件时会导致内存有问题
     *    返回ResponseEntity<byte[]>
     * @return
     */
    @RequestMapping(value = "/download2", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download2() {
        String fileId = "group1/M00/00/35/CgUcjmATclCAD-oOAACBPlT0COc591.jpg";
        //TODO 这里将文件转了byte再返回，存在内存问题
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        fastdfsService.downloadFile(fileId, outputStream);

        byte[] fileBytes = outputStream.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        //设置文件名
        headers.add("Content-Disposition", "attachment;filename=" + "图片下载");
        //设置文件类型
        headers.add("Content-Type", "application/octet-stream");
        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }

}
