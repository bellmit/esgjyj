var zbid='';
var selfOffice = '';
$().ready(function() {
	zbid = GetQueryString('zbid');
	if(zbid=='1-1'){
        $('#note').val("最大分值为5分");
        validateRule(5);
	} else if(zbid=='2-1'){
        $('#note').val("最大分值为10分");
        validateRule(10);
	}else if(zbid=='3-1'){
        $('#note').val("最大分值为60分");
        selfOffice = true;
        validateRule(60);
    }else if(zbid=='4-1'){
        $('#note').val("最大分值为60分");
        selfOffice = true;
        validateRule(60);
    }
    else if(zbid=='8-1'){
        $('#note').val("最大分值为60分");
        validateRule(60);
    }
    else if(zbid=='6-1'){
        $('#note').val("最大分值为60分");
        selfOffice = true;
        validateRule(60);
    }
    else if(zbid=='7-1'){
        $('#note').val("最大分值为60分");
        validateRule(60);
        selfOffice = true;
    }
    else if(zbid=='5-4'){
        //书记员-综合评价分
        $('#note').val("最大分值为30分");
        validateRule(30)
        selfOffice = true;
    }
    else if(zbid=='9-3'){
        //书记员-综合评价分
        $('#note').val("最大分值为20分");
        validateRule(20)
    }
    else if(zbid=='9-4'){
        //书记员-综合评价分
        $('#note').val("最大分值为30分");
        validateRule(30);
        selfOffice = true;
    }
    else if(zbid=='9-2'){
        //书记员-卷宗管理工作
        $('#note').val("最大分值为10分");
        validateRule(10);
        selfOffice = true;
    }
    else if(zbid=='5-2'){
        //书记员-卷宗管理工作
        $('#note').val("最大分值为10分");
        validateRule(10);
        selfOffice = true;
    }
    else if(zbid=='5-3'){
        //书记员-卷宗管理工作
        $('#note').val("最大分值为20分");
        validateRule(20);
        selfOffice = true;
    }
	$("#zbid").val(zbid);
    selectsjLoad();
    //selectdxLoad();
    selectzbLoad();
	validateRule();
});

function selectsjLoad() {
    var html = "";
    $.ajax({
        url : '/esgjyj/yjkh/list',
        success : function(data) {
            //加载数据
            for (var i = 0; i < data.length; i++) {
                html += '<option value="' + data[i].id + '">' + data[i].khmc + '</option>'
            }
            $("#khid").append(html);
        }
    });
    $("#khid").on('change',function(){
        selectdxLoad($("#khid").val());
	})
}

function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
function selectdxLoad(khid) {
	$("#dxid").empty();
    var html = "";
    $.ajax({
        url : '/esgjyj/yjkhKhdx/list?khid='+khid+'&&zbid='+zbid+'&&selfOffice='+selfOffice,
        success : function(data) {
            //加载数据
            for (var i = 0; i < data.length; i++) {
                html += '<option value="' + data[i].id + '">' + data[i].name + '</option>'
            }
            $("#dxid").append(html);

        }
    });

}
function selectzbLoad() {
    var html = "";
    $.ajax({
        url : '/esgjyj/yjkhZbwh/list',
        success : function(data) {
            //加载数据
            for (var i = 0; i < data.length; i++) {
                html += '<option value="' + data[i].id + '">' + data[i].zbmc+ '</option>'
            }
            $("#zbid").append(html);

        }
    });
}
$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});
function save() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/esgjyj/yjkhZgkh/save",
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
function validateRule(max) {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			score : {
				required : true,
                max:max
			}
		},
		messages : {
            score : {
				required : icon + "请输入数值",
                max:icon + "最大值不能超过"+max
			}
		}
	})
}