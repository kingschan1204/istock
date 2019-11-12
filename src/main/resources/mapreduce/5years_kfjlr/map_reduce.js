db.stock_year_report.mapReduce(
    function () {
        emit(this.code, {profits: this.kfjlr, year: this.year})
    },  //map 函数
    function(key, values) {
        var result="";
        for (var i = 0; i < values.length; i++) {
            if(values[i].profits>0){
                result+="+";
            }else{
                result+="-";
            }
        }
        return {flag:result};
    },   //reduce 函数
    {
        out: "demo",
        query:{"year":{$gte:2014}},
        sort:{"code":1,"year":1}
    }
)
