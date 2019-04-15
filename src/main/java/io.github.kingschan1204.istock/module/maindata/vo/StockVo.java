package io.github.kingschan1204.istock.module.maindata.vo;

/**
 * stock 主数据vo
 * @author chenguoxiang
 * @create 2018-10-16 14:41
 * http://localhost/stock/q?rows=20&page=1&sidx=&sord=asc&
 **/
public class StockVo {

    private String code;
    private String type;
    private String name;
    private Double price;
    private Double yesterdayPrice;
    private String fluctuate;
    private Double todayMax;
    private Double todayMin;
    private Long priceDate;
    private String industry;
    private String totalValue;
    private String pb;
    private String roe;
    private String bvps;
    private String pes;
    private String ped;
    private Integer infoDate;
    /**
     * 静态分红日期
     */
    private String dividendDate;
    private String dividend;
    /**
     * 实时股息率
     */
    private String dy;
    /**
     * 5年平均股息
     */
    private String fiveYearDy;
    /**
     * 5年平均Roe
     */
    private String fiveYearRoe;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getYesterdayPrice() {
        return yesterdayPrice;
    }

    public void setYesterdayPrice(Double yesterdayPrice) {
        this.yesterdayPrice = yesterdayPrice;
    }

    public String getFluctuate() {
        return fluctuate;
    }

    public void setFluctuate(Double fluctuate) {
        this.fluctuate = String.format("%s%%",fluctuate);
    }

    public Double getTodayMax() {
        return todayMax;
    }

    public void setTodayMax(Double todayMax) {
        this.todayMax = todayMax;
    }

    public Double getTodayMin() {
        return todayMin;
    }

    public void setTodayMin(Double todayMin) {
        this.todayMin = todayMin;
    }

    public Long getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(Long priceDate) {
        this.priceDate = priceDate;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = -1==totalValue?"--":String.format("%s亿",totalValue);
    }

    public String getPb() {
        return pb;
    }

    public void setPb(Double pb) {
        this.pb = -1==pb?"--":String.format("%s",pb);
    }

    public String getRoe() {
        return roe;
    }

    public void setRoe(Double roe) {
        this.roe = -1==roe?"--":String.format("%s%%",roe);
    }

    public String getBvps() {
        return bvps;
    }

    public void setBvps(Double bvps) {
        this.bvps =  -1==bvps?"--":String.format("%s",bvps);
    }

    public String getPes() {
        return pes;
    }

    public void setPes(Double pes) {
        this.pes = -1==pes?"--":String.format("%s",pes);
    }

    public String getPed() {
        return ped;
    }

    public void setPed(Double ped) {
        this.ped = -1==ped?"--":String.format("%s",ped);
    }

    public Integer getInfoDate() {
        return infoDate;
    }

    public void setInfoDate(Integer infoDate) {
        this.infoDate = infoDate;
    }

    public String getDividendDate() {
        return dividendDate;
    }

    public void setDividendDate(String dividendDate) {
        this.dividendDate = dividendDate;
    }

    public String getDividend() {
        return dividend;
    }

    public void setDividend(Double dividend) {
        if(0==dividend){
            this.dividend="";
            return;
        }
        this.dividend =  -1==dividend?"--":String.format("%s%%",dividend);
    }

    public String getDy() {
        return dy;
    }

    public void setDy(Double dy) {
        this.dy = -1==dy?"--":String.format("%s%%",dy);
    }

    public String getFiveYearDy() {
        return fiveYearDy;
    }

    public void setFiveYearDy(Double fiveYearDy) {
        this.fiveYearDy = -1==fiveYearDy?"--":String.format("%s%%",fiveYearDy);
    }

    public String getFiveYearRoe() {
        return fiveYearRoe;
    }

    public void setFiveYearRoe(Double fiveYearRoe) {
        this.fiveYearRoe = -1==fiveYearRoe?"--":String.format("%s%%",fiveYearRoe);
    }

    public StockVo() {
    }
}
