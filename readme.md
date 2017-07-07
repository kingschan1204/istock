

由于电脑上装了几个jdk 所以运行的时候临时设置一下对应的jdk版本：(我设置为jdk 1.7 运行maven)
set path=C:\Program Files\Java\jdk1.7.0_21\bin

C:\Users\kingschan\Documents\apache-maven-3.3.9\bin\mvn package

## spring boot maven 常用命令
cd C:\Users\kingschan\Documents\GitHub\SpringBoot-Pritice 进入项目目录下
- mvn package  打包
- mvn spring-boot:run  运行spring boot

```
CREATE TABLE `stock_master` (
  `s_code` varchar(6) NOT NULL COMMENT '股票代码',
  `s_stock_name` varchar(30) NOT NULL COMMENT '股票名称',
  `s_current_price` decimal(7,2) NOT NULL COMMENT '现价',
  `s_yesterday_price` decimal(7,2) NOT NULL,
  `s_range_price` decimal(7,2) NOT NULL COMMENT '涨幅',
  `s_main_business` varchar(100) NOT NULL COMMENT '主营业务',
  `s_industry` varchar(20) NOT NULL COMMENT '所属行业',
  `s_pe_dynamic` decimal(5,2) NOT NULL COMMENT '市盈率(动态)',
  `s_pe_static` decimal(5,2) NOT NULL COMMENT '市盈率(静态)',
  `s_pb` decimal(5,2) NOT NULL COMMENT '市净率',
  `s_total_value` decimal(8,2) NOT NULL COMMENT '总市值',
  `s_roe` decimal(8,2) NOT NULL COMMENT '净资产收益率',
  `s_dividend_rate` decimal(5,2) NOT NULL COMMENT '分红率',
  `s_dividend_year` int(4) DEFAULT NULL COMMENT '分红年份',
  `s_version` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`s_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

```

## 新浪实时价格接口

```
http://hq.sinajs.cn/list=

				0：股票名字；
				1：今日开盘价；
				2：昨日收盘价；
				3：当前价格；
				4：今日最高价；
				5：今日最低价；
				6：竞买价，即“买一”报价；
				7：竞卖价，即“卖一”报价；
				8：成交的股票数，由于股票交易以一百股为基本单位，所以在使用时，通常把该值除以一百；
				9：成交金额，单位为“元”，为了一目了然，通常以“万元”为成交金额的单位，所以通常把该值除以一万；
				10：买一”申请4695股，即47手；
				11：“买一”报价；
				12：“买二”
				13：“买二”
				14：“买三”
				15：“买三”
				16：“买四”
				17：“买四”
				18：“买五”
				19：“买五”
				20：“卖一”申报3100股，即31手；
				21：“卖一”报价
				(22, 23), (24, 25), (26,27), (28, 29)分别为“卖二”至“卖四的情况”
				30：日期；
				31：时间；
```
## 历史市盈率 历史市净率
http://biz.finance.sina.com.cn/company/compare/img_syl_compare.php?stock_code=600036,&limit=3650
600036改成想查的股票代码 2400改成任何你想查看的多少天 syl是查市盈率，如果查市净率就把syl的y改成j


## 国内指数
`http://hq.sinajs.cn/list=sz399001,sh000001,sz399006,sh000300`