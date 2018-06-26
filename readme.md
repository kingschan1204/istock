## 前言

> 本应用是一个基于Spring boot 实现的股票指数爬虫工具，数据来源于`同花顺`,`新浪财经`,`雪球`,`上交所`,`深交所`

:heavy_exclamation_mark: :point_right: 本项目初衷纯属技术交流，勿作商业用途:heavy_exclamation_mark::bangbang:

## 实现功能

 :ballot_box_with_check: 指数更新
 
 :ballot_box_with_check: 股票数据范围：目前只支持上市和沪市（A股）
 
 :ballot_box_with_check: 市盈率，市净率，ROE ,所属行业
 
 :ballot_box_with_check: 历史数据：市盈率，市净率 ，分红，净资产收益率

## Demo 效果 

:link: [点我查看效果](http://stock.51so.info/) (demo版) :link:

## :boom: 效果图 :boom:

![](https://kingschan1204.github.io/istock/readme-res/stock-list.png )

![](https://kingschan1204.github.io/istock/readme-res/his-roe.png )

![](https://kingschan1204.github.io/istock/readme-res/his-dy.png )

![](https://kingschan1204.github.io/istock/readme-res/his-pe.png )

![](https://kingschan1204.github.io/istock/readme-res/his-pb.png )




## 国内指数
`http://hq.sinajs.cn/list=sz399001,sh000001,sz399006,sh000300`

# 数据结构
```
	"_id" : "000783",
	"type" : "sz",
	"name" : "长江证券",
	"price" : 6.62,
	"yesterdayPrice" : 6.69,
	"fluctuate" : -1.05,
	"todayMax" : 6.72,
	"todayMin" : 6.62,
	"priceDate" : ISODate("2018-05-29T06:58:01.117Z"),
	"industry" : "证券",
	"mainBusiness" : "证券代理买卖、证券自营、证券承销、受托资产管理等证券类业务。",
	"totalValue" : 369,
	"pb" : 1.38,
	"roe" : 1.21,
	"bvps" : 4.85,
	"pes" : 23.94,
	"ped" : 28.704,
	"dividendDate" : "-", 
	"dividend" : 2.2,
	"dy" : 2.24,
	"dyDate" : 20180529,
	"infoDate" : 20180529,
	"dividendUpdateDay" : 20180524 

```
