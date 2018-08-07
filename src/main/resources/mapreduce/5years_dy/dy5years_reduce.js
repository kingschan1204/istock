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
    if(index<5){
        total=0;
    }
    return {percent:(total/index).toFixed(2),years:years,size:index};
}