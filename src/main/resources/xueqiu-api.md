## 股票基本信息接口
https://stock.xueqiu.com/v5/stock/quote.json?symbol=SH601288&extend=detail
请求方式：get 
必带参数：cookie : xq_a_token= 
### 返回参数
```json
{
    "data": {
        "market": {
            "status_id": 7,
            "region": "CN",
            "status": "已收盘",
            "time_zone": "Asia/Shanghai"
        },
        "quote": {
            "symbol": "SH601288",
            "code": "601288",
            "high52w": 3.7606,  52周最高
            "avg_price": 3.645, 
            "delayed": 0,
            "type": 11,
            "percent": -0.27, 涨幅
            "tick_size": 0.01,
            "float_shares": 294055293904, 流通股
            "limit_down": 3.29, 跌停
            "amplitude": 0.55, 振幅
            "current": 3.65,
            "high": 3.66,
            "current_year_percent": 6.23,
            "float_market_capital": 1073301822750, 	流通值
            "issue_date": 1279123200000,
            "low": 3.64, 最低
            "sub_type": "ASH",
            "market_capital": 1277438073636, 总市值
            "dividend": 0.1739,  	股息
            "dividend_yield": 4.764, 股息率
            "currency": "CNY", 	货币单位
            "lot_size": 100,
            "lock_set": null,
            "navps": 4.72, 每股净资产
            "profit": 202783000000,
            "timestamp": 1562310000000,
            "pe_lyr": 6.3, 市盈率(静)
            "amount": 581968132, 成交额
            "chg": -0.01, 股价波动
            "eps": 0.6, 每股收益
            "last_close": 3.66, 	昨收
            "profit_four": 205298000000,
            "volume": 159652038, 成交量
            "volume_ratio": 0.55, 量比
            "pb": 0.773, 市净率
            "profit_forecast": 245004000000,
            "limit_up": 4.03, 	涨停
            "turnover_rate": 0.05, 换手
            "low52w": 3.1402, 	52周最低
            "name": "农业银行",
            "pe_ttm": 6.222, 市盈率(TTM)
            "exchange": "SH",
            "pe_forecast": 5.214, 	市盈率(动)
            "time": 1562310000000,
            "total_shares": 349983033873, 总股本
            "open": 3.66, 今开
            "status": 1
        },
        "others": {
            "pankou_ratio": -31.79 委比
        },
        "tags": [
            {
                "description": "沪股通",沪股通
                "value": 1
            },
            {
                "description": "融", 可融资
                "value": 6
            },
            {
                "description": "空", 可卖空
                "value": 7
            }
        ]
    },
    "error_code": 0,
    "error_description": ""
}
```