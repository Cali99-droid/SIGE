package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.sige.mat.dao.impl.PromedioComDAOImpl;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;

/**
 * Define m�todos DAO operations para la entidad promedio_com.
 * @author MV
 *
 */
@Repository
public class PromedioComDAO extends PromedioComDAOImpl{
	
	public void deletePromedioCom(Integer id_au, Integer id_cpu, Integer id_cua) {
		String sql = "delete from not_promedio_com where id_au=? and id_cpu=? ";
		if(id_cua!=0) {
			sql += " and id_cua="+id_cua;
		}
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, new Object[]{id_au,id_cpu});
	}
	
	public void deletePromedioComxCompetencia(Integer id_au, Integer id_cpu, Integer id_cua, Integer id_com, Integer id_alu) {
		String sql = "delete from not_promedio_com where id_au=? and id_cpu=? and id_com=? and id_alu=?";
		if(id_cua!=0) {
			sql += " and id_cua="+id_cua;
		}
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, new Object[]{id_au,id_cpu, id_com,id_alu});
	}
	
	public Row notaPromedioCom(Integer id_cua, Integer id_com, Integer id_cpu, Integer id_alu) {
		String sql = "SELECT distinct ncom.* FROM not_promedio_com ncom \r\n" + 
				"INNER JOIN `aca_capacidad_dc` cap ON ncom.`id_com`=cap.`id_com`\r\n" + 
				"INNER JOIN `aca_desempenio_aula` desau ON desau.`id_cap`=cap.id AND ncom.`id_cpu`=desau.`id_cpu` AND desau.`id_cua`=ncom.`id_cua`\r\n" + 
				"WHERE ncom.id_cua=? AND ncom.id_com=?  AND ncom.id_cpu=? AND ncom.id_alu=? AND desau.`est`='A'";
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,new Object[]{id_cua,id_com, id_cpu, id_alu});
		
		if(list.size()>0)
			return SQLFrmkUtil.listToRows(list).get(0);	
		else
			return null;	
	}
	
}
