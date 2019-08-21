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
        File pathFile = new File(descDir);
        if(!pathFile.exists()){
            pathFile.mkdirs();
        }
        ZipFile zip = null;
        try {

            zip = new ZipFile(zipFile,"UTF-8");//防止中文目录，乱码
            for(Enumeration entries = zip.getEntries(); entries.hasMoreElements();){
                ZipEntry entry = (ZipEntry)entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                //指定解压后的文件夹+当前zip文件的名称
                String outPath = (descDir+zipEntryName).replace("/", File.separator);
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
                if(!file.exists()){
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if(new File(outPath).isDirectory()){
                    continue;
                }
                //保存文件路径信息（可利用md5.zip名称的唯一性，来判断是否已经解压）
                log.info("当前zip解压之后的路径为：{}", outPath);
                OutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[2048];
                int len;
                while((len=in.read(buf1))>0){
                    out.write(buf1,0,len);
                }
                in.close();
                out.close();
            }
            flag = true;
            //必须关闭，要不然这个zip文件一直被占用着，要删删不掉，改名也不可以，移动也不行，整多了，系统还崩了。
            zip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
