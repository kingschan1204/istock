package io.github.kingschan1204.istock.module.spider.crawl.csindex;

import io.github.kingschan1204.istock.common.util.file.ExcelOperactionTool;
import io.github.kingschan1204.istock.common.util.file.FileCommonOperactionTool;
import io.github.kingschan1204.istock.module.maindata.po.CsIndexIndustry;
import io.github.kingschan1204.istock.module.spider.util.TradingDateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author chenguoxiang
 * @create 2019-08-21 10:26
 **/
@Slf4j
public class CsindexIndustrySpider implements Callable<List<CsIndexIndustry>> {

    private static String url ="http://www.csindex.com.cn/uploads/downloads/other/files/zh_CN/ZzhyflWz.zip";
    private static String referrer ="http://www.csindex.com.cn/zh-CN/downloads/industry-class";
    private static String download_folder ="./download/";
    @Override
    public List<CsIndexIndustry> call() throws Exception {
        log.info("开始下载文件：{}",url);
        String path= FileCommonOperactionTool.downloadFile(url, referrer,download_folder, "csindextype.zip");
        log.info("分类表下载路径：{}",path);
        FileCommonOperactionTool.decompressZip(path,download_folder);
        List<Object[]> list =ExcelOperactionTool.readExcelData(download_folder+"cicslevel2.xls");
        Integer dateTime=Integer.valueOf(TradingDateUtil.getDateYYYYMMdd());
        List<CsIndexIndustry> data = new ArrayList<>();
        for (Object[] cell:list){
            String code=cell[0].toString();
            //900 为B股
            if (!code.matches("\\d{6}")||code.startsWith("900")){
                continue;
            }
            data.add(new CsIndexIndustry(
                    code,
                    cell[1].toString(),
                    cell[5].toString(),
                    cell[8].toString(),
                    cell[11].toString(),
                    cell[14].toString(),
                    cell[16].toString(),
                    dateTime
            ));
        }
        return data;
    }


}
