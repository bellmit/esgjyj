package com.eastsoft.esgjyj.controller;

 import java.util.List;
 import java.util.Map;
 import java.util.UUID;

 import com.eastsoft.esgjyj.context.ScopeUtil;
 import com.eastsoft.esgjyj.domain.User;
 import com.eastsoft.esgjyj.util.PageUtils;
 import org.apache.commons.collections.map.HashedMap;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.*;

import com.eastsoft.esgjyj.domain.DatacodeDO;
import com.eastsoft.esgjyj.service.DatacodeService;
import com.eastsoft.esgjyj.util.PageUtils;
import com.eastsoft.esgjyj.util.Query;
import com.eastsoft.esgjyj.util.R;

/**
 * ${comments}
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 21, 2017 8:10:42 PM CST
 */
 
@Controller
@RequestMapping("/esgjyj/datacode")
public class DatacodeController {
	@Autowired
	private DatacodeService datacodeService;
	
	@GetMapping("/get/{id}")
	DatacodeDO get(@PathVariable("id") String id){
	    return   datacodeService.get(id);
	}
	
	@ResponseBody
	@GetMapping("/list/{did}")
	public List<DatacodeDO> list(@PathVariable("did") String did){
		Map<String,Object> param = new HashedMap();
		param.put("courtNo", ScopeUtil.getSessionUser(User.class).getCourtNo());
		param.put("dId",did);
		return datacodeService.list(param);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	public R save( DatacodeDO datacode){
		if(datacodeService.save(datacode)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update( DatacodeDO datacode){
		datacodeService.update(datacode);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	public R remove( String courtNo){
		if(datacodeService.remove(courtNo)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	public R remove(@RequestParam("ids[]") String[] courtNos){
		datacodeService.batchRemove(courtNos);
		return R.ok();
	}
	
}
