package io.github.kingschan1204.istock.common.util.file;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * 文件常用操作工具类
 * @author chenguoxiang
 * @create 2018-01-24 12:38
 **/
public class FileCommonOperactionTool {

    private static Logger log = LoggerFactory.getLogger(FileCommonOperactionTool.class);

    /**
     * 通过指定的文件下载URL以及下载目录下载文件
     * @param url      下载url路径
     * @param dir      存放目录
     * @param filename 文件名
     * @throws Exception
     */
    public static String downloadFile(String url, String dir, String filename) throws Exception {
        log.info("start download file :{}",url);
        //Open a URL Stream
        Connection.Response resultResponse = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3346.9 Safari/537.36")
                .ignoreContentType(true).execute();
        String defaultFileName="";
        if(resultResponse.contentType().contains("name")){
            String[] list =resultResponse.contentType().split(";");
            defaultFileName = Arrays.stream(list)
                    .filter(s -> s.startsWith("name")).findFirst().get().replaceAll("name=|\"", "");
        }
        // output here
        String path = dir + (null == filename ? defaultFileName : filename);
        FileOutputStream out=null;
       try{
            out = (new FileOutputStream(new java.io.File(path)));
           out.write(resultResponse.bodyAsBytes());
       }catch (Exception ex){
           log.error("{}",ex);
           ex.printStackTrace();
       }finally {
           out.close();
       }
        return path;
    }
}
