package com.eastsoft.esgjyj.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 对 {@linkplain org.springframework.jdbc.core.JdbcTemplate JdbcTemplate} 的封装，
 * 解决 jdbc 从 sybase 数据库查询存在的问题：为 "" 的字段取出的值为空格。
 * @author chenkai
 * @since archetype-1.0.0
 * @version 1.0.0
 */
@Repository("baseDao")
@Transactional(propagation = Propagation.SUPPORTS)
public class BaseDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 查询并返回 {@link List} 类型结果。
	 * @param sql SQL 语句。
	 * @return 查询结果 {@link List} 。
	 */
	public List<Map<String, Object>> queryForList(String sql) {
		List<Map<String, Object>> list = jdbcTemplate.query(sql, getColumnMapRowMapper());
		return list;
	}
	
	/**
	 * 查询并返回 {@link List} 类型结果。
	 * @param sql SQL 语句。
	 * @param args 参数列表。
	 * @return 查询结果 {@link List} 。
	 */
	public List<Map<String, Object>> queryForList(String sql, Object... args) {
		List<Map<String, Object>> list = jdbcTemplate.query(sql, args, getColumnMapRowMapper());
		return list;
	}
	
	/**
	 * 查询并返回 {@link Map} 类型结果。
	 * @param sql SQL 语句。
	 * @return 查询结果 {@link Map} 。
	 */
	public Map<String, Object> queryForMap(String sql) {
		Map<String, Object> map = jdbcTemplate.queryForObject(sql, this.getColumnMapRowMapper());
		return map;
	}
	
	/**
	 * 查询并返回 {@link Integer} 类型结果。
	 * @param sql SQL 语句。
	 * @return 查询结果 {@link Integer} 。
	 */
	public int queryForInt(String sql) {
		int number = jdbcTemplate.queryForObject(sql, Integer.class);
		return number;
	}
	
	/**
	 * 插入或更新操作。
	 * @param sql SQL 语句。
	 * @return 影响行数。
	 */
	public int update(String sql) {
		int cnt = jdbcTemplate.update(sql);
		return cnt;
	}
	
	/**
	 * 插入或更新操作。
	 * @param sql SQL 语句。
	 * @param args 参数列表。
	 * @return 影响行数。
	 */
	public int update(String sql, Object... args) {
		int cnt = jdbcTemplate.update(sql, args);
		return cnt;
	}
	
	/**
	 * 组织单行数据。
	 * <p>解决 jdbc 从 sybase 数据库查询存在的问题：为""的字段取出的值为空格。
	 * @return {@link org.springframework.jdbc.core.RowMapper} 。
	 */
	protected RowMapper<Map<String, Object>> getColumnMapRowMapper() {
		class SimpleColumnMapRowMapper extends ColumnMapRowMapper {
			@Override
			protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
				Object obj = JdbcUtils.getResultSetValue(rs, index);
				if (obj == null) {
					return null;
				}
				
				if ("java.lang.String".equals(obj.getClass().getName()) && "".equals(obj.toString().trim())) {
					obj = "";
				}
				return obj;
			}
		}
		return new SimpleColumnMapRowMapper();
	}
}