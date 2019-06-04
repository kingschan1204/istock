function(key, values) {
    var total=0;
    var years="";
    var array=new Array();
    for (var i = 0; i < values.length; i++) {
        var title =values[i].title;
        array.push(title);
        total+=values[i].percent;
        years+=title+",";
    }
    //去掉重复key
    function unique3(arr){
        arr.sort();
        var hash=[arr[0]];
        for (var i = 1; i < arr.length; i++) {
            if(arr[i]!=hash[hash.length-1]){
                hash.push(arr[i]);
            }
        }
        return hash;
    }

    var arrays=unique3(array);
    return {percent:(total/arrays.length).toFixed(2),years:years,total:total,key:arrays.length};
}