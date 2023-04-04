**此项目停止开发，对股票数据爬取感兴趣的同学可以关注项目：https://github.com/kingschan1204/easycrawl**
> easycrawl 介绍：初衷想用java写一个简单高效爬取互联网数据的工具包，单元测试里有大量爬取股票数据的例子。
---

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

## 重要说明
> 此项目为个人兴趣之余开发，很多地方不完善，很抱歉时间有限。没办法跟产品相比。但是本人不接受吐槽，
前面一段时间遇到很多网友直接对我进行人身攻击，指责。另外如果遇到问题，请客气跟我沟通，语气难听的
一概无视。谢谢！  个人无偿开源小项目，不容易。望大家多多包含。如无法理解，请绕道。谢谢！

**请勿用于商业用途！！！**



---

# 运行步骤说明

## step 1 下载代码导入idea
## step 2 设置配置文件重要参数
```properties
spring.data.mongodb.host=你的mongodb服务器地址
xueqiu.token=上文介绍的雪球token
eastmoney.token=上文介绍的东方财富token
tushare.token=上文有介绍
spring.security.user.password=程序管理密码 自定义
```
## step3 创建mongodb 数据库 istock
![](https://raw.githubusercontent.com/kingschan1204/istock/develop/readme-res/create-mongo-istock.png )
## step 4 运行项目
## step 5 初始化代码
![](https://raw.githubusercontent.com/kingschan1204/istock/develop/readme-res/init-code.png )
操作成功后为初始化A股所有公司代码【效果始下】
![](https://raw.githubusercontent.com/kingschan1204/istock/develop/readme-res/code-list.png )
## step 6 更改windows 时间初始化股票主ipo主数据
> 因为项目是爬虫项目，是严格按照时间逻辑去执行的，并且频率并发有严格的控制（请求过高，网站有反爬虫
策略，会封锁ip）,另外istock 项目更新股票价格是在证券交易时间内进行的，也就是说是周一~周五 上午9：30 - 11：30 下
午 13：00 ~ 15：00 这段时间内才会更新股票价格等指标。所以如果当前时间不是在交易时间内请修改windows时间让程序立马初始化。

### 初始化stock 这张表只需要 3分钟左右 3分钟后就可以改回时间了

## step 7 挂机
> 让istock 程序挂机运行，所有数据完成初始化一般4个小时左右，第一次数据初始化比较麻烦，往后就好了。
往后都是自动化，让程序挂在那就好。会每天自动更新数据。

# 常见问题
## mongodb 要脚本建表吗？
不用，运行程序自动生成
## 为什么要用雪球token
因为有在雪球网爬取数据，雪球网有反爬虫机制，要登录后才能取数据，token就是绕过验证环节
## 为什么有时候会报403错误
如果遇到控制台输出dy 403就是雪球token过期了，你重新登录雪球网，换新的token就好。一般一个星期换一次吧。
## 此项目会一直更新下去吗？
会的，这个项目我会一直做下去的，只是我业余时间不多，我会尽量多用业余时间多写的。

# 赞赏
> 如果你觉得此项目还不错，可以随意打赏，收到款项将用于istock 服务器部署费用 。打赏超50元 可加微信回答一切关于istock的问题。

![](https://raw.githubusercontent.com/kingschan1204/istock/develop/readme-res/alipay.jpg )
![](https://raw.githubusercontent.com/kingschan1204/istock/develop/readme-res/wxpay.jpg )










