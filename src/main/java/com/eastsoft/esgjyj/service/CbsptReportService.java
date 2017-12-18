package com.eastsoft.esgjyj.service;

import java.util.List;
import java.util.Map;

public interface CbsptReportService {
	/**
	 * 获取承办案件数
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> cbrList(Map<String, Object> map);
}
