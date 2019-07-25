function(key, values) {
    var total=0;
    var index=0;
    var years="";
    for (var i = 0; i < values.length; i++) {
        var title =values[i].title;
        total+=values[i].percent;
        index++;
        years+=title+",";
    }
    var percent=(total/index).toFixed(2);
    return {percent:parseFloat(percent),years:years,size:index};
}