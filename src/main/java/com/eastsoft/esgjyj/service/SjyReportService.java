package com.eastsoft.esgjyj.service;

import java.util.List;
import java.util.Map;

public interface SjyReportService {
	/**
	 * 获取书记员记录案件数
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> sjyList(Map<String, Object> map);
}
