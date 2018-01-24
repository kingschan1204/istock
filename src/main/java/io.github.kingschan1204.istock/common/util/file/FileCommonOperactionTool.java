package io.github.kingschan1204.istock.common.util.file;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * 文件常用操作工具类
 *
 * @author chenguoxiang
 * @create 2018-01-24 12:38
 **/
public class FileCommonOperactionTool {

    /**
     * 通过指定的文件下载URL以及下载目录下载文件
     * @param url 下载url路径
     * @param dir  存放目录
     * @param filename 文件名
     * @throws Exception
     */
    public static String downloadFile(String url, String dir,String filename) throws Exception {
        //Open a URL Stream
        Connection.Response resultResponse = Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
            .ignoreContentType(true).execute();
        String defaultFileName= Arrays.stream(resultResponse.contentType().split(";"))
                .filter(s -> s.startsWith("name")).findFirst().get().replaceAll("name=|\"","");
        // output here
        String path =dir+(null==filename?defaultFileName:filename);
        FileOutputStream out = (new FileOutputStream(new java.io.File(path)));
        out.write(resultResponse.bodyAsBytes());
        out.close();
        return path;
    }
}
