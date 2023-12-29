package com.tesla.frmk.sql;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

public class SQLFrmkUtil {

	@Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
    private JdbcTemplate jdbcTemplate;
    
	public SQLFrmkUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static List<Row> listToRows(List<Map<String,Object>> list ){
		
		List<Row> rows = new ArrayList<Row>();

		for (Map<String, Object> map : list) {
			Row row = new Row(); 
			for(String key : map.keySet()){
				row.put(key, map.get(key));
			}

			rows.add(row);
		}
		return rows;
		
	}
	
	public static String getWhere(Param param){
		int i = 0;
		String sql = "";
		if(param==null)
			return "";
		
		for (String key : param.keySet()) {
			Object valuekey = param.get(key); 
			if(valuekey!=null && !"".equals(valuekey.toString()) && !"%%".equals(valuekey.toString()) && key.indexOf("TABLA")==-1 ){
				String value = valuekey.toString();
				if (i == 0){
					if (value.indexOf("null")>-1)
						sql = sql + " where " + key + " " + value;
					else if (value.indexOf("!=")>-1)
						sql = sql + " where " + key + value ;
					else if (value.indexOf("%")==-1)
						sql = sql + " where " + key + "='" + value + "'";
					else
						sql = sql + " where lower(" + key + ") like '" + value.toLowerCase() + "'";
				}else{
					if (value.indexOf("null")>-1)
						sql = sql + " and " + key + " " + value;
					else if (value.indexOf("!=")>-1)
						sql = sql + " and " + key + value ;
					else if (value.indexOf("%")==-1)
						sql = sql + " and " + key + "='" + value + "'";
					else
						sql = sql + " and lower(" + key + ") like '" + value.toLowerCase() + "'";
				}
					
				i++;
				
			}
		}
		return sql;
	}

	

		
	public static String getOrder(String[] order){
		int i = 0;
		String sql = "";
		if(order!=null){
			for (String string : order) {
				if (i == 0)
					sql = sql + " order by " + string;
				else
					sql = sql + " , " + string;
				i++;
				
			}

		}
		return sql;
	}
	
}
