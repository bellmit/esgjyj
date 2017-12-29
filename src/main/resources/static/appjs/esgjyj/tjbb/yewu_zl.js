var prefix = "/tjbb/fgzlRe";
var khid ='';
var ofid = '';
$(function () {
    selectLoad();
    selectOfficeLoad();
});

function selectLoad() {
    var html = "";
    $.ajax({
        url: '/esgjyj/yjkh/list',
        success: function (data) {
            //加载数据
            for (var i = 0; i < data.length; i++) {
                html += '<option value="' + data[i].id + '">' + data[i].khmc + '</option>'
            }
            $(".chosen-select").append(html);
            $(".chosen-select").chosen({
                maxHeight: 200
            });
            khid = $('.chosen-select').val();
            load();
            //点击事件
            $('.chosen-select').on('change', function (e, params) {
                khid = params.selected;
                var opt = {
                    query: {
                        khid: params.selected,
                    }
                }
                $('#exampleTable').bootstrapTable('refresh', opt);
            });
        }
    });
}



function load() {
    $('#exampleTable')
        .bootstrapTable(
            {
                method: 'get', // 服务器数据的请求方式 get or post
                url: prefix + "/list", // 服务器数据的加载地址
                //	showRefresh : true,
                //	showToggle : true,
                //	showColumns : true,
                iconSize: 'outline',
                toolbar: '#exampleToolbar',
                striped: true, // 设置为true会有隔行变色效果
                dataType: "json", // 服务器返回的数据类型
                pagination: true,                 cache: false,
                // queryParamsType : "limit",
                // //设置为limit则会发送符合RESTFull格式的参数
                singleSelect: false, // 设置为true将禁止多选
                // contentType : "application/x-www-form-urlencoded",
                // //发送到服务器的数据编码类型
                pageSize: 10, // 如果设置了分页，每页数据条数
                pageNumber: 1, // 如果设置了分布，首页页码
                //search : true, // 是否显示搜索框
                showColumns: false, // 是否显示内容下拉框（选择显示的列）
                sidePagination: "client", // 设置在哪里进行分页，可选值为"client" 或者 "server"
                queryParams: function (params) {
                    return {
                        //说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
                        // limit: params.limit,
                        // offset:params.offset
                        // name:$('#searchName').val(),
                        // username:$('#searchName').val()
                        khid: khid
                    };
                },
                // //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
                // queryParamsType = 'limit' ,返回参数必须包含
                // limit, offset, search, sort, order 否则, 需要包含:
                // pageSize, pageNumber, searchText, sortName,
                // sortOrder.
                // 返回false将会终止请求
                columns: [
                    [
                        {
                            title: "法官助理辅助办案一览表",
                            halign: "center",
                            align: "center",
                            colspan: 9,
                            width: '100%',
                        }
                    ],
                    [
                        {
                            field: 'SHORTNAME',
                            title: '部门',
                            align: "center",
                        },
                        {
                            field: 'XS',
                            title: '新收',
                            align: "center",
                            visible:false
                        },
                        {
                            field: 'YJ',
                            title: '已结',
                            align: "center",
                        },
                        {
                            field: 'XS_ZS',
                            title: '折算收案数',
                            align: "center",
                            visible:false

                        },
                        {
                            field: 'YJ_ZS',
                            title: '折算结案数',
                            align: "center",
                            visible:false

                        },
                        {
                            field: 'AVERAGE_SCORE',
                            title: '人均辅助结案数',
                            align: "center",

                        },

                    ]]
            });
    columns: [
        [
            {
                title: "综合审判部门主要负责人考评报表",
                halign: "center",
                align: "center",
                colspan: 9,
                width: '100%',
            }
        ],
        [
            {
                field: 'office',
                title: '部门',
                align: "center",
                rowspan: 2,
            },
            {
                field: 'name',
                title: '姓名',
                align: "center",
                rowspan: 2,
            },
            {
                field: 'toScore',
                title: '考核总分',
                align: "center",
                rowspan: 2,
            },
            {
                field: 'a3',
                title: '综合审判业绩',
                align: "center",
                rowspan: 2,
                formatter: function (value, row, index) {
                    return '<a href="#" onclick="preview(\'a3\',\'' + row.khdx + '\')">' + row.a3 + '</a>';
                }
            },
            {
                field: 'a4',
                title: '个人办案业绩',
                align: "center",
                rowspan: 2,
                formatter: function (value, row, index) {
                    return '<a href="#" onclick="preview(\'a4\',\'' + row.khdx + '\')">' + row.a4 + '</a>';
                }
            },
            {
                title: "综合审判调研",
                valign: "middle",
                align: "center",
                colspan: 3,
                rowspan: 1
            }

        ],
        [
            {
                field: 'a5',
                title: '调研、理论成果',
                valign: "middle",
                align: "center",
                formatter: function (value, row, index) {
                    return '<a href="#" onclick="preview(\'a5\',\'' + row.khdx + '\')">' + row.a5 + '</a>';
                }
            },
            {
                field: 'a6',
                title: '案例采用',
                valign: "middle",
                align: "center",
                formatter: function (value, row, index) {
                    return '<a href="#" onclick="preview(\'a6\',\'' + row.khdx + '\')">' + row.a6 + '</a>';
                }
            },
            {
                field: 'a7',
                title: '表彰奖励',
                valign: "middle",
                align: "center",
                formatter: function (value, row, index) {
                    return '<a href="#" onclick="preview(\'a7\',\'' + row.khdx + '\')">' + row.a7 + '</a>';
                }
            },
        ]
    ]
}

function reLoad() {
    $('#exampleTable').bootstrapTable('refresh');
}

function add() {
    layer.open({
        type: 2,
        title: '增加',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/add.html' // iframe的url
    });
}

function preview(filed, khdx) {
    // layer.alert('崔勇：3.0; </br> 栾建德：18.0;</br>李永生：12.0;</br>颜振贞：9.0;</br>陈东强：18.0;</br>马丽：16.0;</br>邱建坡：24.0;</br>丁国红：24.0;</br>王新兵：3.0;</br>娄勇军：9.0;</br>陈浩：21.0;</br>任楷：3.0;</br>岳彩林：33.0;</br>张豪：15.0;</br>李守军：30.0;</br>曹毅：27.0;</br>付文文：12.0;</br>张磊：30.0;</br>卢伟：9.0;</br>王海娜：31.0;</br>孔祥昆：3.0;</br>毕中兴：15.0;</br>部门平均法官结案分值：10.43');
    layer.open({
        type: 2,
        title: '编辑',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: "/esgjyj/tjbb/tjbx.html?colIndex=" + filed + "&&khdxid=" + khdx // iframe的url
    });
    // $.getJSON("/esgjyj/yjkhAjmx/list",{colIndex:filed,khdxid:khdx},function(data){
    //     alert(data);
    //     console.log(1);
    // });

}

function edit(id) {
    layer.open({
        type: 2,
        title: '编辑',
        maxmin: true,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        content: prefix + '/edit.html?id=' + id // iframe的url
    });
}

function remove(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: prefix + "/remove",
            type: "post",
            data: {
                'id': id
            },
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    })
}

function resetPwd(id) {
}

function batchRemove() {
    var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
    if (rows.length == 0) {
        layer.msg("请选择要删除的数据");
        return;
    }
    layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
        btn: ['确定', '取消']
        // 按钮
    }, function () {
        var ids = new Array();
        // 遍历所有选择的行数据，取每条数据对应的ID
        $.each(rows, function (i, row) {
            ids[i] = row['id'];
        });
        $.ajax({
            type: 'POST',
            data: {
                "ids": ids
            },
            url: prefix + '/batchRemove',
            success: function (r) {
                if (r.code == 0) {
                    layer.msg(r.msg);
                    reLoad();
                } else {
                    layer.msg(r.msg);
                }
            }
        });
    }, function () {

    });
}