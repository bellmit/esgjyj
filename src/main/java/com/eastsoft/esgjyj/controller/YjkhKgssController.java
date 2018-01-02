package com.eastsoft.esgjyj.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eastsoft.esgjyj.context.ScopeUtil;
import com.eastsoft.esgjyj.dao.UserMapper;
import com.eastsoft.esgjyj.domain.User;
import com.eastsoft.esgjyj.domain.YjkhKgssDO;
import com.eastsoft.esgjyj.service.YjkhKgssService;
import com.eastsoft.esgjyj.service.YjkhZbwhService;
import com.eastsoft.esgjyj.util.R;
import com.eastsoft.esgjyj.util.Tools;



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
    
    @Value("${data.path}")
    private String dataDir;

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
                yjkhKgssList = yjkhKgssService.list("表彰奖励");
            }
            if("D".equals(param.get("zbid"))){
                yjkhKgssList = yjkhKgssService.list("奖惩得分");
            }
            if("E".equals(param.get("zbid"))){
                yjkhKgssList = yjkhKgssService.list("审判调研");
            }
            if("F".equals(param.get("zbid"))){
                yjkhKgssList = yjkhKgssService.list("信访投诉数");
            }
            if("G".equals(param.get("zbid"))){
                yjkhKgssList = yjkhKgssService.list("引发负面舆情次数");
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
    public R save(YjkhKgssDO yjkhKgss, @RequestParam(name="file", required=false) MultipartFile file) {
    	try {
    		String uuid = UUID.randomUUID().toString().replace("-", "");
    		String userid = yjkhKgss.getUserid();
    		if(!Tools.isEmpty(userid)) userid = userid.substring(userid.indexOf("_") + 1);
    		if(file == null) {
    			yjkhKgss.setXh(0);
    	        yjkhKgss.setId(uuid);
    	        yjkhKgss.setUserid(userid);
    	        if("F".equals(yjkhKgss.getZbid())) {
    	        	yjkhKgss.setZbid("1-5");
    	        } else {
    	        	yjkhKgss.setZbid("1-6");
    	        }
    	        yjkhKgss.setScore(-1f);
    	        yjkhKgss.setZt("0");
    	        if (yjkhKgssService.save(yjkhKgss) > 0) {
    	            return R.ok();
    	        }
    	        return R.ok();
    		}
    		String filename = file.getOriginalFilename();
    		if(filename.contains("\\") || filename.contains("//") || filename.contains("/")) {
    			filename = filename.substring(filename.lastIndexOf("\\") + 1);
    		}
			byte[] bytes = file.getBytes();
			String destPath = dataDir + File.separator
					+ "img" + File.separator + uuid + "_" + filename;
			FileUtils.writeByteArrayToFile(new File(destPath), bytes);
			yjkhKgss.setXh(0);
	        yjkhKgss.setId(uuid);
	        if ("self".equals(yjkhKgss.getUserid())) {
	            yjkhKgss.setUserid(ScopeUtil.getSessionUser(User.class).getUserid());
	            yjkhKgss.setZt("0");
	            yjkhKgss.setPath(destPath);
	        }
	        if (yjkhKgssService.save(yjkhKgss) > 0) {
	            return R.ok();
	        }
	        return R.ok();
		} catch (IOException e) {
			throw new RuntimeException();
		}
    }
    
    @PostMapping("/imgUrl")
    @ResponseBody
    public Map<String, Object> getPhotoStr(String id) {
    	YjkhKgssDO yjkhKgssDO = yjkhKgssService.get(id);
    	Map<String, Object> map = new HashMap<>();
    	map.put("url", "");
    	if (yjkhKgssDO == null) {
			return map;
		}
    	String path = yjkhKgssDO.getPath();
    	String base64Str = "";
    	byte[] bytes;
		try {
			bytes = FileUtils.readFileToByteArray(new File(path));
			base64Str = Tools.generateImgBase64Str(bytes);
		} catch (IOException e) {
			throw new RuntimeException("读取图片时出错！");
		}
		map.put("url", base64Str);
		return map;
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
