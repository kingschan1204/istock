package io.github.kingschan1204.istock.common.util.file;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * excel 操作工具类
 * @author chenguoxiang
 * @create 2018-01-24 15:56
 **/
public class ExcelOperactionTool {

    /**
     * 读取excel内容
     * @param url
     * @return
     * @throws Exception
     */
    public static List<Object[]> readExcelData(String url)throws Exception{

        // 从XLSX/ xls文件创建的输入流
        FileInputStream fis = new FileInputStream(url);
        List<Object[]> hospitalList = new ArrayList<Object[]>();
        // 创建工作薄Workbook
        Workbook workBook = null;
        // 读取2007版以.xlsx 结尾
        if(url.toLowerCase().endsWith("xlsx")){
            try {
                workBook = new XSSFWorkbook(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 读取2003版，以   .xls 结尾
        else if(url.toLowerCase().endsWith("xls")){
            try {
                workBook = new HSSFWorkbook(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Get the number of sheets in the xlsx file
        int numberOfSheets = workBook.getNumberOfSheets();

        // 循环 numberOfSheets
        for(int sheetNum = 0; sheetNum < numberOfSheets; sheetNum++){

            // 得到 工作薄 的第 N个表
            Sheet sheet = workBook.getSheetAt(sheetNum);
            Row row;
            String cell;
            for(int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++){
                // 循环行数
                row = sheet.getRow(i);
                List<String > cells = new ArrayList<>();
                for(int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++){
                    // 循环列数
                    cell = row.getCell(j).toString();
                    cells.add(cell);
                }
                hospitalList.add(cells.toArray(new Object[0]));
            }
        }
        return hospitalList;
    }
}
