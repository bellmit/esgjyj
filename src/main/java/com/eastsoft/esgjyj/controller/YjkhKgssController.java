package com.eastsoft.esgjyj.controller;

import java.util.*;

import com.eastsoft.esgjyj.context.ScopeUtil;
import com.eastsoft.esgjyj.dao.UserMapper;
import com.eastsoft.esgjyj.domain.User;
import com.eastsoft.esgjyj.domain.YjkhKhdxDO;
import com.eastsoft.esgjyj.service.YjkhKhdxService;
import com.eastsoft.esgjyj.service.YjkhZbwhService;
import com.eastsoft.esgjyj.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.eastsoft.esgjyj.domain.YjkhKgssDO;
import com.eastsoft.esgjyj.service.YjkhKgssService;
import com.eastsoft.esgjyj.util.PageUtils;
import com.eastsoft.esgjyj.util.Query;
import com.eastsoft.esgjyj.util.R;

/**
 * ${comments}
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 22, 2017 1:47:47 PM CST
 */

@RestController
@RequestMapping("/esgjyj/yjkhKgss")
public class YjkhKgssController {
    @Autowired
    private YjkhKgssService yjkhKgssService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private YjkhZbwhService yjkhZbwhService;

    @GetMapping("/get/{id}")
    YjkhKgssDO get(@PathVariable("id") String id) {
        YjkhKgssDO yjkhKgssDO = yjkhKgssService.get(id);
        yjkhKgssDO.setUserName(userMapper.selectByPrimaryKey(yjkhKgssDO.getUserid()).getUsername());
        yjkhKgssDO.setZbName(yjkhZbwhService.get(yjkhKgssDO.getZbid()).getZbmc());
        return yjkhKgssDO;
    }

    @ResponseBody
    @GetMapping("/list")
    public List<YjkhKgssDO> list(@RequestParam Map<String, Object> param) {
        //查询列表数据
        //	Map<String,Object> param = new HashMap<>();
        List<YjkhKgssDO> yjkhKgssList = new ArrayList<>();
        if("true".equals(param.get("self"))){
        param.put("userid", ScopeUtil.getSessionUser(User.class).getUserid());
             yjkhKgssList = yjkhKgssService.list(param);
        }else{
            if("A".equals(param.get("zbid"))){
                yjkhKgssList = yjkhKgssService.list("调研理论成果");
            }
            if("B".equals(param.get("zbid"))){
                yjkhKgssList = yjkhKgssService.list("案例采用");
            }
            if("C".equals(param.get("zbid"))){
                yjkhKgssList = yjkhKgssService.list("案例采用");
            }
            if("D".equals(param.get("zbid"))){
                yjkhKgssList = yjkhKgssService.list("奖惩得分");
            }
            if("E".equals(param.get("zbid"))){
                yjkhKgssList = yjkhKgssService.list("审判调研");
            }
        }

        List<YjkhKgssDO> yjkhKgssListRes = new ArrayList<>();
        for (YjkhKgssDO yjkhKgssDO : yjkhKgssList) {
            yjkhKgssDO.setUserName(userMapper.selectByPrimaryKey(yjkhKgssDO.getUserid()).getUsername());
            yjkhKgssDO.setZbName(yjkhZbwhService.get(yjkhKgssDO.getZbid()).getZbmc());
            yjkhKgssListRes.add(yjkhKgssDO);
        }
        return yjkhKgssListRes;
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(YjkhKgssDO yjkhKgss) {
        yjkhKgss.setXh(0);
        yjkhKgss.setId(UUID.randomUUID().toString().replace("-", ""));
        if ("self".equals(yjkhKgss.getUserid())) {
            yjkhKgss.setUserid(ScopeUtil.getSessionUser(User.class).getUserid());
            yjkhKgss.setZt("0");
        }
        if (yjkhKgssService.save(yjkhKgss) > 0) {
            return R.ok();
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(YjkhKgssDO yjkhKgss) {
        yjkhKgssService.update(yjkhKgss);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    public R remove(String id) {
        if (yjkhKgssService.remove(id) > 0) {
            return R.ok();
        }
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    public R remove(@RequestParam("ids[]") String[] ids) {
        yjkhKgssService.batchRemove(ids);
        return R.ok();
    }

}
