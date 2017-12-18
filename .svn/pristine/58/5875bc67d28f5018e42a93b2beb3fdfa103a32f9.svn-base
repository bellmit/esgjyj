package com.eastsoft.esgjyj.controller;

 import java.util.List;
 import java.util.Map;
 import java.util.UUID;

 import com.eastsoft.esgjyj.util.PageUtils;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.*;

import com.eastsoft.esgjyj.domain.YjkhZbwhDO;
import com.eastsoft.esgjyj.service.YjkhZbwhService;
import com.eastsoft.esgjyj.util.PageUtils;
import com.eastsoft.esgjyj.util.Query;
import com.eastsoft.esgjyj.util.R;

/**
 * ${comments}
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 13, 2017 5:16:24 PM CST
 */
 
@RestController
@RequestMapping("/esgjyj/yjkhZbwh")
public class YjkhZbwhController {
	@Autowired
	private YjkhZbwhService yjkhZbwhService;
	
	@GetMapping("/get/{id}")
	YjkhZbwhDO get(@PathVariable("id") String id){
	    return   yjkhZbwhService.get(id);
	}
	
	@ResponseBody
	@GetMapping("/list")
	public List<YjkhZbwhDO> list(@RequestParam Map<String, Object> params){
		//查询列表数据
		List<YjkhZbwhDO> yjkhZbwhList = yjkhZbwhService.list(params );
		return yjkhZbwhList;
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	public R save( YjkhZbwhDO yjkhZbwh){
		//yjkhZbwh.setId(UUID.randomUUID().toString().replace("-",""));
		if(yjkhZbwhService.save(yjkhZbwh)>0){
			return R.ok();
		}
		return R.ok();
	}
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update( YjkhZbwhDO yjkhZbwh){
		yjkhZbwhService.update(yjkhZbwh);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	public R remove( String id){
		if(yjkhZbwhService.remove(id)>0){
		return R.ok();
		}
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	public R remove(@RequestParam("ids[]") String[] ids){
		yjkhZbwhService.batchRemove(ids);
		return R.ok();
	}
	
}
