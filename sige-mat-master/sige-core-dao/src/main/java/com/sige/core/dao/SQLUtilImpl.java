package com.sige.core.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tesla.frmk.sql.SQLFrmkUtil;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

/**
 * An implementation of the SQLUtil interface.
 * 
 * @author mvalle
 *
 */
@Repository
public class SQLUtilImpl implements SQLUtil {
    private final Logger logger = LoggerFactory.getLogger(SQLUtilImpl.class);

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public SQLUtilImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public List<Row> list(String entidad) {
		String sql = "select * from " + entidad;

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		return SQLFrmkUtil.listToRows(list);

	}

	@Override
	public List<Row> query(String sql, Object[] params) {

		try {
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, params);

			return SQLFrmkUtil.listToRows(list);
		} catch (Exception e) {

			logger.error("sql:" + sql);
			for (int i = 0; i < params.length; i++) {
				logger.error("param[" + i + "]=" + params[i]);
			}

			throw e;

		}

	}

	@Override
	public List<Row> query(String sql) {

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		return SQLFrmkUtil.listToRows(list);

	}
 
	public <T> List<T> query(String sql, Object[] params, Class<T> clazz) {

		try {
			List<T> list = jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<T>(clazz));
			return list;
		} catch (Exception e) {

			logger.error("query:" + sql);
			throw e;

		}

	}

	public <T> List<T> query(String sql, Class<T> clazz) {

		try {
			List<T> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(clazz));

			return list;
		} catch (Exception e) {

			logger.error("query:" + sql);
			throw e;

		}

	}

	@Override
	public void update(String sql) {

		try {
			jdbcTemplate.update(sql);
		} catch (Exception e) {

			logger.error("update:" + sql);
			throw e;

		}

	}

	@Override
	public Integer update(String sql, Object[] params) {

		return jdbcTemplate.update(sql, params);
	}

	@Override
	public List<Row> query(String sql, Param params) {

		List<Map<String, Object>> list = namedParameterJdbcTemplate.queryForList(sql, params);

		return SQLFrmkUtil.listToRows(list);

	}

	@Override
	public int update(String sql, Param params) {
		return namedParameterJdbcTemplate.update(sql, params);

	}

	@Override
	public <T> T queryForObject(String sql, Class<T> clazz) {

		return jdbcTemplate.queryForObject(sql, clazz);
	}

	public <T> T queryForJavaBean(String sql, Class<T> clazz) {
		List<T> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(clazz));
		return !list.isEmpty() ? list.get(0) : null;

	}
	
	public <T> T queryForJavaBean(String sql,Object[] params, Class<T> clazz) {
		List<T> list = jdbcTemplate.query(sql, params,new BeanPropertyRowMapper<T>(clazz));
		return !list.isEmpty() ? list.get(0) : null;

	}

	@Override
	public <T> T queryForObject(String sql, Object[] params, Class<T> clazz) {
		return jdbcTemplate.queryForObject(sql, params, new SingleColumnRowMapper<T>(clazz));
	}

	@Override
	public <T> T queryForObject(String sql, Param params, Class<T> clazz) {
		return namedParameterJdbcTemplate.queryForObject(sql, params, new SingleColumnRowMapper<T>(clazz));
	}

	public Integer getLastInsertId() {
		return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
	}

}
