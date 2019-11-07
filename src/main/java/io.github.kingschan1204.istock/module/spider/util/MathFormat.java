package io.github.kingschan1204.istock.module.spider.util;

import java.math.BigDecimal;

/**
 * 数字格式化工具类
 * @author chenguoxiang
 * @create 2019-07-01 15:55
 **/
public class MathFormat {
    static final String REGEX_NUMBER = "^[-+]?([0]{1}(\\.[0-9]+)?|[1-9]{1}\\d*(\\.[0-9E]+)?)";

    /**
     * 字符串转成整数
     * @param data
     * @return 如果不是数字则返回0
     */
    public static int intFormart(String data) {
        if (data.matches("\\d+")) {
            return Integer.valueOf(data);
        }
        return 0;
    }

    /**
     * 格式化数据，如果不是数字全部返回null
     *
     * @param value
     * @return
     */
   public static Double doubleFormat(String value) {
        /*if (v.matches(REGEX_NUMBER)) {
            return Double.valueOf(v);
        }*/
       String v = value.replaceAll("[^0-9.E+\\-]", "");
       if (v.isEmpty()||v.equals("--")||v.equals("-")){
           return null;
       }
       try{
          return Double.parseDouble(v);
       }catch (Exception e){
           e.printStackTrace();
           return null;
       }
    }
    /**
     * 处理百分比的数字 4舍5入 保留两位小数
     * @param math
     * @param percent 是否是百分比数值
     * @return 去掉百分号 X 100 保存
     */
    public static double doubleFormat(String math, boolean percent) {
        Double value= doubleFormat(math);
        if(null==value){
            return 0d;
        }
        if (percent) {
            value = value * 100;
        }
        BigDecimal b = new BigDecimal(value);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * 字符串转double 可去掉非数字的字符
     * @param value  字符串
     * @param divisor 除数 当数字比较大时用来压缩数字单位
     * @param rounded 是否四舍五入 (保留两位小数)
     * @return 如果非数字则返回null
     */
   public static Double doubleFormat(String value,int divisor,boolean rounded) {
        Double d= doubleFormat(value);
        if(null==d){return null;}
        Double math=d/divisor;
        if (rounded){
            BigDecimal bd= new BigDecimal(math);
            return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return math;
    }

    /**
     * 从一段字符串里找出数字
     * @param text
     * @return
     */
    public static Double parseMath(String text){
        char chars[]=text.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c:chars) {
            String str =String.valueOf(c);
            if (str.equals("-")||str.matches("\\d")||str.equals(".")){
                sb.append(str);
            }
        }
        return sb.toString().isEmpty()?0:Double.parseDouble(sb.toString());
    }


}
