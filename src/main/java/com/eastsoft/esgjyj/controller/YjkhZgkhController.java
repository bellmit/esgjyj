package com.eastsoft.esgjyj.controller;

 import java.util.ArrayList;
 import java.util.List;
 import java.util.Map;
 import java.util.UUID;

 import com.eastsoft.esgjyj.dao.UserMapper;
 import com.eastsoft.esgjyj.service.YjkhKhdxService;
 import com.eastsoft.esgjyj.service.YjkhZbwhService;
 import com.eastsoft.esgjyj.util.PageUtils;
 import com.eastsoft.esgjyj.vo.YjkhZgkhVO;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.*;

import com.eastsoft.esgjyj.domain.YjkhZgkhDO;
import com.eastsoft.esgjyj.service.YjkhZgkhService;
import com.eastsoft.esgjyj.util.PageUtils;
import com.eastsoft.esgjyj.util.Query;
import com.eastsoft.esgjyj.util.R;

/**
 * ${comments}
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 13, 2017 5:27:32 PM CST
 */
 
@RestController
@RequestMapping("/esgjyj/yjkhZgkh")
public class YjkhZgkhController {
	@Autowired
	private YjkhZgkhService yjkhZgkhService;

	@Autowired
	private YjkhKhdxService yjkhKhdxService;

	@Autowired
	private YjkhZbwhService yjkhZbwhService;

	@Autowired
	private UserMapper userMapper;
	
	@GetMapping("/get/{id}")
	YjkhZgkhVO get(@PathVariable("id") String id){
		YjkhZgkhDO yjkhDO =   yjkhZgkhService.get(id);
		YjkhZgkhVO yjkhZgkhVO = new YjkhZgkhVO();
		yjkhZgkhVO.setId(yjkhDO.getId());
		yjkhZgkhVO.setDxName(userMapper.selectByPrimaryKey(yjkhKhdxService.get(yjkhDO.getDxid()).getUserid()).getUsername());
		yjkhZgkhVO.setZbName(yjkhZbwhService.get(yjkhDO.getZbid()).getZbmc());
		yjkhZgkhVO.setScore(yjkhDO.getScore());
		yjkhZgkhVO.setNote(yjkhDO.getNote());
		return yjkhZgkhVO;
	}
	
	@GetMapping("/list")
	public List<YjkhZgkhVO> list(@RequestParam Map<String, Object> params){
		//查询列表数据
		List<YjkhZgkhDO> yjkhZgkhList = yjkhZgkhService.list(params);
		List<YjkhZgkhVO> yjkhZgkhVOS  = new ArrayList<>();
		for (YjkhZgkhDO yjkhDO:yjkhZgkhList ) {
			YjkhZgkhVO yjkhZgkhVO = new YjkhZgkhVO();
			yjkhZgkhVO.setId(yjkhDO.getId());
			yjkhZgkhVO.setDxName(userMapper.selectByPrimaryKey(yjkhKhdxService.get(yjkhDO.getDxid()).getUserid()).getUsername());
			yjkhZgkhVO.setZbName(yjkhZbwhService.get(yjkhDO.getZbid()).getZbmc());
			yjkhZgkhVO.setScore(yjkhDO.getScore());
			yjkhZgkhVO.setNote(yjkhDO.getNote());
			yjkhZgkhVOS.add(yjkhZgkhVO);
		}
		return yjkhZgkhVOS;
	}

	@GetMapping("/listBykhid")
	public List<YjkhZgkhVO> listBykhid(@RequestParam Map<String, String> map){
		//查询列表数据
		List<YjkhZgkhDO> yjkhZgkhList = yjkhZgkhService.listBykhid(map);
		List<YjkhZgkhVO> yjkhZgkhVOS  = new ArrayList<>();
		for (YjkhZgkhDO yjkhDO:yjkhZgkhList ) {
			YjkhZgkhVO yjkhZgkhVO = new YjkhZgkhVO();
			yjkhZgkhVO.setId(yjkhDO.getId());
			yjkhZgkhVO.setDxName(userMapper.selectByPrimaryKey(yjkhKhdxService.get(yjkhDO.getDxid()).getUserid()).getUsername());
			yjkhZgkhVO.setZbName(yjkhZbwhService.get(yjkhDO.getZbid()).getZbmc());
			yjkhZgkhVO.setScore(yjkhDO.getScore());
			yjkhZgkhVO.setNote(yjkhDO.getNote());
			yjkhZgkhVOS.add(yjkhZgkhVO);
		}
		return yjkhZgkhVOS;
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public R save( YjkhZgkhDO yjkhZgkh){
		yjkhZgkh.setId(UUID.randomUUID().toString().replace("-",""));
		yjkhZgkh.setXh(0);
		if(yjkhZgkhService.save(yjkhZgkh)>0){
			return R.ok();
		}
		return R.ok();
	}
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update( YjkhZgkhDO yjkhZgkh){
		yjkhZgkhService.update(yjkhZgkh);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	public R remove( String id){
		if(yjkhZgkhService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	public R remove(@RequestParam("ids[]") String[] ids){
		yjkhZgkhService.batchRemove(ids);
		return R.ok();
	}
	
}
