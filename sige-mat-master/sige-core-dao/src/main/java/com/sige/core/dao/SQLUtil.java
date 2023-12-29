package com.sige.core.dao;

import java.util.List;

import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;


/**
 * Defines DAO operations for the contact model.
 * @author www.codejava.net
 *
 */
public interface SQLUtil {
	
	
	public List<Row> list(String entidad);
	
	public List<Row> query(String sql);

	public List<Row> query(String sql, Object[] params);
		
	public <T> List<T> query(String sql, Object[] params, Class<T> clazz);

	public <T> List<T> query(String sql, Class<T> clazz);
	
	public void update(String sql);
	
	public Integer update(String sql,Object[] params);
	
	public List<Row> query(String sql, Param params);

	public int update(String sql, Param params);

    public <T>T queryForObject(String sql,  Class<T> clazz);
  
    public <T>T queryForJavaBean(String sql,  Class<T> clazz);

	public <T> T queryForJavaBean(String sql,Object[] params, Class<T> clazz);
	
    public <T> T queryForObject(String sql, Object[] params, Class<T> clazz);
    
    public <T> T queryForObject(String sql, Param params, Class<T> clazz) ;
    
	public Integer getLastInsertId();

}
