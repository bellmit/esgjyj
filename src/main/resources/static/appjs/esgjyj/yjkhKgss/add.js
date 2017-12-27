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
    inputfunction();
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
	$('#signupForm').ajaxSubmit({
		type : "POST",
		url : "/esgjyj/yjkhKgss/save",
//		data : $('#signupForm').serialize(),// 你的formid
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
function inputfunction() {
	$('#input-pic').fileinput({//初始化上传文件框
	    showUpload : false,
	    showRemove : false,
	    uploadAsync: true,
	    uploadLabel: "上传",//设置上传按钮的汉字
	    uploadClass: "btn btn-primary",//设置上传按钮样式
	    showCaption: false,//是否显示标题
	    language: "zh",//配置语言
	    uploadUrl: "/esgjyj/yjkhKgss/upload",
	    maxFileSize : 0,
	    maxFileCount: 1,/*允许最大上传数，可以多个，当前设置单个*/
	    enctype: 'multipart/form-data',
	    //allowedPreviewTypes : [ 'image' ], //allowedFileTypes: ['image', 'video', 'flash'],
	    allowedFileExtensions : ["jpg", "png","gif"],/*上传文件格式*/
	    msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
	    dropZoneTitle: "请通过拖拽图片文件放到这里", 
	    dropZoneClickTitle: "或者点击此区域添加图片",
	    //uploadExtraData: {"id": id},//这个是外带数据
	    showBrowse: false,
	    browseOnZoneClick: true,
	    slugCallback : function(filename) {
	        return filename.replace('(', '_').replace(']', '_');
	    }
	});
}
