package com.eastsoft.esgjyj.service;

import java.util.List;
import java.util.Map;

public interface FgzlReportService {
	/**
	 * 获取法官助理辅助结案数
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> fgzlList(Map<String, Object> map);
}
