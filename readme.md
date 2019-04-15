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
## Demo 效果 

:link: [点我查看效果](http://211.159.182.106/) (demo版) :link:

## :boom: 效果图 :boom:

![](https://kingschan1204.github.io/istock/readme-res/stock-list.png )

![](https://kingschan1204.github.io/istock/readme-res/company-info.png )

![](https://kingschan1204.github.io/istock/readme-res/top-holders.png )

![](https://kingschan1204.github.io/istock/readme-res/his-dy.png )

![](https://kingschan1204.github.io/istock/readme-res/his-pb-pe.png )

![](https://kingschan1204.github.io/istock/readme-res/his-report.png )

![](https://kingschan1204.github.io/istock/readme-res/his-roe.png )

## 代码分支说明

- develop 正在开发版本（不稳定）
- master 发布版(稳定)
- 1.x  1.x版本代码（稳定）
- 2.x 将要发布的2.0版本，正在开发中（不稳定）

## 重要配置说明
```properties
#web 
# 项目在哪个端口启动
server.port = 80
# 你的mongodb 数据库名称
spring.data.mongodb.database=istock
# 你的mongodb 数据库地址
spring.data.mongodb.host=
# 你的mongodb 服务监听端口
spring.data.mongodb.port=27017
# 爬虫超时抓取时间
spider.timeout=8000
# 爬虫模拟用的useagent头部信息
spider.useagent=Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3346.9 Safari/537.36
# 雪球网token 非常重要 要有token才能爬取雪球网信息 【往下翻介绍获取方法】
xueqiu.token=
# 东方财富网token 【往下翻介绍获取方法】
eastmoney.token=
#tushare token 非常重要【往下翻介绍获取方法】
tushare.token=

# spring boot 健康检查管理端口
management.server.port=80
# spring boot 健康检查管理前缀
management.endpoints.web.base-path=/admin
# spring boot 健康检查 帐号【自定义】
spring.security.user.name=root
# spring boot 健康检查管理密码【自定义】
spring.security.user.password=

```
## 雪球网token 获取方法
登录雪球网：https://xueqiu.com/ 按下浏览器F12 在cookie 里找到key : xq_a_token 把这个token复制到application.properties里
![](https://user-images.githubusercontent.com/4113891/41651979-83cf2718-74b4-11e8-88d0-ce7979955304.png )
![](https://user-images.githubusercontent.com/4113891/41651992-8b2020c6-74b4-11e8-86f0-c87c167f7ef0.png )

## tushare token 获取方法
- 1.[点击这个链接注册帐号](https://tushare.pro/register?reg=133400)
- 2.然后https://tushare.pro/user/token   在这个页面可以拿到token
- 3.把token复制到application.properties 
![微信截图_20190409152849](https://user-images.githubusercontent.com/4113891/55781105-f8cc7f80-5adb-11e9-83d0-b191fddea24a.png)

![微信截图_20190409152802](https://user-images.githubusercontent.com/4113891/55781065-e4888280-5adb-11e9-8fc9-77a246bff9d0.png)

## 东方财富token获取
登录网站：http://data.eastmoney.com/rzrq/total.html 即可获取token









