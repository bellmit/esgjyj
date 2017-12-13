$(function () {
    /*横向柱形图*/
    var barChart = echarts.init(document.getElementById("echarts-bar-chart"));
    var baroption = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        legend: {
            data: ['办案基础', '办案质量', '办案效率', '办案效果', '综合审判调研']
        },
        grid: {
            // left: '3%',
            // right: '4%',
            // bottom: '3%',
            // containLabel: true
        },
        xAxis: [
            {
                type: 'value'
            }
        ],
        yAxis: [
            {
                type: 'category',
                data: ['张法官', '李法官', '王法官', '赵法官',]
            }
        ],
        series: [
            {
                name: '办案基础',
                type: 'bar',
                stack: '总量',
                label: {
                    normal: {
                        show: true,
                        position: 'insideRight'
                    }
                },
                data: [320, 302, 301, 334]
            },
            {
                name: '办案质量',
                type: 'bar',
                stack: '总量',
                label: {
                    normal: {
                        show: true,
                        position: 'insideRight'
                    }
                },
                data: [120, 132, 101, 134]
            },
            {
                name: '办案效率',
                type: 'bar',
                stack: '总量',
                label: {
                    normal: {
                        show: true,
                        position: 'insideRight'
                    }
                },
                data: [220, 182, 191, 234]
            },
            {
                name: '办案效果',
                type: 'bar',
                stack: '总量',
                label: {
                    normal: {
                        show: true,
                        position: 'insideRight'
                    }
                },
                data: [150, 212, 201, 154, 190, 330, 410]
            },
            {
                name: '综合审判调研',
                type: 'bar',
                stack: '总量',
                label: {
                    normal: {
                        show: true,
                        position: 'insideRight'
                    }
                },
                data: [820, 832, 901, 934, 1290, 1330, 1320]
            }
        ]
    };
    barChart.setOption(baroption);
    window.onresize = barChart.resize;
    /*饼状图echarts-bar-chart*/
    var pieChart = echarts.init(document.getElementById("echarts-pie-chart"));
    var pieoption = {
        title : {
            text: '',
            subtext: '',
            x:'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient : 'vertical',
            x : 'left',
            data:['立案庭','民一庭','民二庭','刑一庭','刑二庭']
        },
        toolbox: {
            show : true,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {
                    show: true,
                    type: ['pie', 'funnel'],
                    option: {
                        funnel: {
                            x: '25%',
                            width: '50%',
                            funnelAlign: 'left',
                            max: 1548
                        }
                    }
                },
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        series : [
            {
                name:'访问来源',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:[
                    {value:335, name:'立案庭'},
                    {value:310, name:'民一庭'},
                    {value:234, name:'民二庭'},
                    {value:135, name:'刑一庭'},
                    {value:1548, name:'刑二庭'}
                ]
            }
        ]
    };
    pieChart.setOption(pieoption);
    $(window).resize(pieChart.resize);
});
