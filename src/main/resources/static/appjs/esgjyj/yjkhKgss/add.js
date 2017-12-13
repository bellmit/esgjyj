var  zbid='';
$().ready(function() {
    laydateInit();
    zbid = GetQueryString('zbid');
    $('#zbid').val(GetQueryString('zbid'));
    if(GetQueryString('self')){
        $("#self").hide();
        $('#userid').val('self');
        $('#username').val('self');
    }
	validateRule();
    selectdfLoad();

});
function laydateInit(){
    laydate.render({
        elem:'#fssj',
    });

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
		url : "/esgjyj/yjkhKgss/save",
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

function loadUser(id,username){
    $("#userid").val(id);
    $("#username").val(username);
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
            $("#zbid").chosen({
                maxHeight : 200
            });
        }
    });
}

function selectdfLoad() {
    var html = "";
    $.ajax({
        url : '/esgjyj/datacode/list/ESGJYJ_'+zbid,
        success : function(data) {
        	console.log(data);
            //加载数据
            for (var i = 0; i < data.length; i++) {
                html += '<option value="' + data[i].note + '">' + data[i].dispval+ '</option>'
            }
            $("#score").append(html);
            $("#score").chosen({
                maxHeight : 200
            });
            $("#note").val($("#score option:selected").text());
            $("#score").on('change',function(){
                $("#note").val($("#score option:selected").text());
            })
        }
    });

    //$(select+" option:selected").text();

}

