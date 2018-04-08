package io.github.kingschan1204.istock;

import io.github.kingschan1204.istock.common.util.file.ExcelOperactionTool;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * excel 操作测试
 * @author chenguoxiang
 * @create 2018-01-24 15:10
 **/
@SpringBootTest
public class ExcelOperactionTest {


    /**
     * excel 年度报表测试
     * @throws Exception
     */
    @Test
    public  void main()throws Exception {
        List<Object[]> list = ExcelOperactionTool.readExcelData("./000568_main_year.xls");
        list.stream().forEach(lis -> {
            for (Object cell :
                    lis) {
                System.out.print(cell);
                System.out.print(" | ");
            }
            System.out.println();
        });
    }
}
