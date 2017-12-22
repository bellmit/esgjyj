$().ready(function() {
	$('#khid').val(GetQueryString('khid'));
	validateRule();
	$('#officeName').on('click',function(){
		openOffice();
	})
});

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
		url : "/esgjyj/yjkhKhdx/save",
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

var openUser = function(){
    layer.open({
        type:2,
        title:"选择人员",
        area : [ '300px', '450px' ],
        content:"/userTree.html"
    })
}
var openOffice = function(){
    layer.open({
        type:2,
        title:"选择部门",
        area : [ '300px', '450px' ],
        content:"/officeTree.html"
    })
}
function loadUser(id,username){
    $("#ids").val(id);
    $("#username").val(username);
    //id = id.replace("user_","");
//    alert(id[0].replace("user_",""));
    $.getJSON("/getOfficeByUserId",{userId:id[0].replace("user_","")},function(res){
        $("#officeid").val(res.ofid);
        $("#officeName").val(res.shortname);
	})
}
function loadOffice(id,name){
    $("#officeid").val(id);
    $("#officeName").val(name);
}