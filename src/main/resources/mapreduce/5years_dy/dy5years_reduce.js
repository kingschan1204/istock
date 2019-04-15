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

    var max =parseInt(values[0].title);
    var min=parseInt(values[values.length-1].title);
    var calc=max-min+1;
    return {percent:(total/calc).toFixed(2),years:years,size:index,pmin:min,pmax:max,pcalc:calc};
}