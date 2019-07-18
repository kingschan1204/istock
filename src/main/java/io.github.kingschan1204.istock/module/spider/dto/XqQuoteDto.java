package io.github.kingschan1204.istock.module.spider.dto;

import lombok.Setter;

@Setter
public class XqQuoteDto {
    //52周最高
    private Double high52w;
    //股息率
    private Double dividend_yield;
    //52周最低
    private Double low52w;
    //市盈率(TTM)
    private Double pe_ttm;

    public Double getHigh52w() {
        return null==high52w?-1:high52w;
    }

    public Double getDividend_yield() {
        return null==dividend_yield?-1:dividend_yield;
    }

    public Double getLow52w() {
        return null==low52w?-1:low52w;
    }

    public Double getPe_ttm() {
        return null==pe_ttm?-1:pe_ttm;
    }
}
