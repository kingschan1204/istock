package io.github.kingschan1204.istock.model.dto;

import java.math.BigDecimal;

/**
 * Created by kingschan on 2017/6/28.
 * http://hq.sinajs.cn/list
 */
public class SinaStockPriceDto {

    private String sCode;
    private String sStockName;
    private BigDecimal sCurrentPrice;
    private BigDecimal sYesterdayPrice;
    private BigDecimal sRangePrice;


    public SinaStockPriceDto(String code,String name,Double price,Double yprice,Double rangePrice){
        this.sCode=code;
        this.sStockName=name;
        this.sCurrentPrice=BigDecimal.valueOf(price);
        this.sYesterdayPrice=BigDecimal.valueOf(yprice);
        this.sRangePrice=BigDecimal.valueOf(rangePrice);

    }


    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }

    public String getsStockName() {
        return sStockName;
    }

    public void setsStockName(String sStockName) {
        this.sStockName = sStockName;
    }

    public BigDecimal getsCurrentPrice() {
        return sCurrentPrice;
    }

    public void setsCurrentPrice(BigDecimal sCurrentPrice) {
        this.sCurrentPrice = sCurrentPrice;
    }

    public BigDecimal getsYesterdayPrice() {
        return sYesterdayPrice;
    }

    public void setsYesterdayPrice(BigDecimal sYesterdayPrice) {
        this.sYesterdayPrice = sYesterdayPrice;
    }

    public BigDecimal getsRangePrice() {
        return sRangePrice;
    }

    public void setsRangePrice(BigDecimal sRangePrice) {
        this.sRangePrice = sRangePrice;
    }
}
