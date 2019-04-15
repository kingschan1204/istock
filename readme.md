## 前言

> 本应用是一个基于Spring boot 实现的股票指数爬虫工具，数据来源于：

- `同花顺`
- `新浪财经`
- `雪球`
- `上交所`
- `深交所`
- `东方财富`

:heavy_exclamation_mark: :point_right: 本项目初衷纯属技术交流，勿作商业用途:heavy_exclamation_mark::bangbang:

## 实现功能

 :ballot_box_with_check: 指数更新
 
 :ballot_box_with_check: 股票数据范围：目前只支持深市和沪市（A股）
 
 :ballot_box_with_check: 市盈率，市净率，ROE ,所属行业
 
 :ballot_box_with_check: 历史数据：市盈率，市净率 ，分红，净资产收益率
 
 :ballot_box_with_check: 公司基本信息、前10大股东信息

## 2.x版预计更动

- 升级至spring boot 2.x
- 废弃quartz 任务调试 -> 并发编程实现
- 引入代理ip机制
- 更丰富的可视化分析
- 分布式爬虫（看情况）










