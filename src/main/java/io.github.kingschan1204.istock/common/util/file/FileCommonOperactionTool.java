package io.github.kingschan1204.istock.common.util.file;

import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Enumeration;

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
     * @referrer        来源
     * @param dir      存放目录
     * @param filename 文件名
     * @throws Exception
     */
    public static String downloadFile(String url,String referrer, String dir, String filename) throws Exception {
        log.info("start download file :{}",url);
        if(!new File(dir).exists()){
            FileUtils.forceMkdir(new File(dir));
        }
        //Open a URL Stream
        Connection.Response resultResponse = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3346.9 Safari/537.36")
                .referrer(referrer)
                .ignoreContentType(true).execute();
        String defaultFileName="";
        if(resultResponse.statusCode()!=200){
            log.error("文件下载失败：{}",url);
            throw new Exception(String.format("文件下载失败：%s 返回码:%s",url,resultResponse.statusCode()));
        }
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
           log.error("文件下载失败：{}",url);
           ex.printStackTrace();
       }finally {
           out.close();
       }
        return path;
    }


    /**
     * 解压文件
     * @param zipPath 要解压的目标文件
     * @param descDir 指定解压目录
     * @return 解压结果：成功，失败
     */
   
    @SuppressWarnings("rawtypes")
    public static boolean decompressZip(String zipPath, String descDir) {
        File zipFile = new File(zipPath);
        boolean flag = false;
        
        // Normalize the destination directory path for security checks
        File destDirectory = new File(descDir);
        String canonicalDestDir;
        try {
            canonicalDestDir = destDirectory.getCanonicalPath();
        } catch (IOException e) {
            log.error("Failed to get canonical path", e);
            return false;
        }
        
        if(!destDirectory.exists()){
            destDirectory.mkdirs();
        }
        
        try (ZipFile zip = new ZipFile(zipFile, "UTF-8")) {
            for(Enumeration entries = zip.getEntries(); entries.hasMoreElements();){
                ZipEntry entry = (ZipEntry)entries.nextElement();
                String zipEntryName = entry.getName();
                
                // Create target file with safe path handling
                File outputFile = new File(destDirectory, zipEntryName);
                
                // Security check - validate path is within destination directory
                String canonicalOutputPath = outputFile.getCanonicalPath();
                if (!canonicalOutputPath.startsWith(canonicalDestDir + File.separator)) {
                    log.warn("Security risk: Zip entry is outside of target directory: {}", zipEntryName);
                    throw new SecurityException("Zip entry is outside of target directory: " + zipEntryName);
                }
                
                if(entry.isDirectory()){
                    // Create directory if it doesn't exist
                    outputFile.mkdirs();
                    continue;
                }
                
                // Create parent directories if needed
                File parent = outputFile.getParentFile();
                if(!parent.exists()){
                    parent.mkdirs();
                }
                
                // Extract file with proper resource management
                try (InputStream in = zip.getInputStream(entry);
                     FileOutputStream out = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[2048];
                    int len;
                    while((len = in.read(buffer)) > 0){
                        out.write(buffer, 0, len);
                    }
                }
                
                log.info("File extracted to: {}", outputFile.getPath());
            }
            flag = true;
        } catch (IOException e) {
            log.error("Error decompressing zip file", e);
        }
        
        return flag;
    }
}
