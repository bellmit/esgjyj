$().ready(function() {
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});
function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
function update() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/esgjyj/yjkhZbwh/update",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.alert(data.msg)
			}

		}
	});

}
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			name : {
				required : true
			}
		},
		messages : {
			name : {
				required : icon + "请输入名字"
			}
		}
	})
}

var wrapper = new Vue({
    el:'#esapp',
    data:{yjkhZbwh:''},
    methods:{
        initForm:function(){
            $.getJSON('/esgjyj/yjkhZbwh/get/'+GetQueryString('id'),function(res){
                wrapper.yjkhZbwh = res;
            })
        }
    },
    mounted:function(){
        this.initForm();
    }
})