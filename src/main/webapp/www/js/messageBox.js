/**
 * 封装对话框
 * @type {{dialog: null, alert: messageBox.alert, confirm: messageBox.confirm, error: messageBox.error, success: messageBox.success, success: messageBox.success, prompt: messageBox.prompt, showLoading: messageBox.showLoading, closeLoading: messageBox.closeLoading, notify: messageBox.notify}}
 */
var messageBox = {
    dialog: null,
    alert: function (msg) {
        swal({title: "",type: 'warning', text: msg, timer: 5000});
    },
    confirm: function (msg, funcallback) {
        swal({
            title: "",
            text: msg,
            type: "info",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            cancelButtonText: "否",
            confirmButtonText: "是"
        }).then(funcallback);
    },
    error:function(title,text){
        swal
        (
            title, text, 'error'
        )
    },
    success:function(title,text){
        swal(
            title, text, 'success'
        )
    },
    success:function(title,text,funcallback){
        swal({
            title: title,
            text: text,
            type: 'success'
        },funcallback)
    },
    /**
     *
     * @param title
     * @param inputType text, email, password, number, tel, range, textarea, select, radio, checkbox, file and url.
     * @param inputVal 默认值
     * @param callback
     */
    prompt: function (title,inputType,inputVal,callback) {
        swal({
            title: title,
            input: inputType,
            inputValue:inputVal,
            showCancelButton: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            confirmButtonColor: '#42c02e',
            cancelButtonColor: '#d33',
            showLoaderOnConfirm: true,
            //preConfirm: callback,
            allowOutsideClick: false
        },callback);
    }, showLoading: function (msg) {
        swal({
            title: '',
            text: msg,
            width:'300px',
            imageUrl: '/www/img/loading.gif',
            showConfirmButton:false,
            showLoaderOnConfirm:true,
            allowOutsideClick:false
        })
    },
    closeLoading: function () {
        swal.close();
    },
    /**
     *
     * @param message
     * @param modeltype warning danger success
     */
    notify: function (message, modeltype) {
        swal.close();
        $.notify({
                title: '<strong>提示</strong>',
                message: message
            }, {
                type: modeltype,
                placement: {from: 'top', align: 'center'},
                animate: {
                    enter: 'animated bounceInDown',
                    exit: 'animated bounceOutUp'
                }
            }
        );
    }
};