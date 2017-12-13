$().ready(function() {
    $('#khid').val(GetQueryString('khid'));
    validateRule();
    khr($("#khrlb").val());
    $("#khrlb").on('change',function(){
        khr($(this).val());
	})
});
function khr(opt){
	if(opt=='0'){
       $("#khrbsText").on('click',function(){
           layer.open({
               type:2,
               title:"选择人员",
               area : [ '300px', '450px' ],
               content:"/userTree.html"
           })
	   })
	}
}

function loadUser(id,text){
	$("#khrbs").val(id);
	$("#khrbsText").val(text);
}

$.validator.setDefaults({
    submitHandler : function() {
        save();
    }
});
function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
function save() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/esgjyj/yjkhZbwh/save",
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
				required : icon + "请输入姓名"
			}
		}
	})
}

var esapp =  new Vue({
	el:'#esapp',
	data:{
		options:[
			{text:'案件质量评查结果'},
			{text:'调研、理论成果'},
            {text:'案例采用'},
            {text:'宣传表彰'},
            {text:'综合评价'},
            {text:'浮动加减分'},
            {text:'形成电子卷宗'},
            {text:'卷宗管理工作'},
            {text:'工作技能分'},
            {text:'奖惩得分'},
            {text:'审判调研'},
		],
        optionsZfz:[
            {text:'加分',value:'+'},
            {text:'减分',value:'-'},
        ]
	}
})