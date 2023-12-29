package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import  com.sige.mat.dao.impl.CompetenciaDcDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad competencia_dc.
 * @author MV
 *
 */
@Repository
public class CompetenciaDcDAO extends CompetenciaDcDAOImpl{

	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listarCompetencias(Integer id_dcare) {
		String sql = "select id, nom value from aca_competencia_dc com where com.id_dcare="+id_dcare+" ORDER BY orden";
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarCompetenciasxAula(Integer id_au,Integer id_dcare, Integer id_gra) {
		String sqlCompetencias ="SELECT DISTINCT com.`id` com_id, com.`nom`, COUNT(com.id) count,desau.id id_desau, desau.id_cua \n" + 
				"				 FROM aca_competencia_dc com \n" + 
				"				 INNER JOIN `aca_capacidad_dc` cap ON com.`id`=cap.`id_com`"+
				"				 INNER JOIN `aca_desempenio_dc` des ON des.`id_com`=com.id\n" + 
				"				 LEFT JOIN `aca_desempenio_aula` desau ON desau.`id_desdc`=des.`id` AND desau.`est`='A' AND desau.id_cap=cap.id AND desau.`id_au`=\n" + id_au+
				"				 WHERE com.`id_dcare`=? AND des.id_gra=? "+
				"				 GROUP  BY com.id "	;	
		return sqlUtil.query(sqlCompetencias, new Object[] { id_dcare, id_gra});

	}
	
	public void desactivarCompetenciaAulaxArea(Integer id_au, Integer id_com, Integer id_cpu){
		String sql = "update aca_competencia_aula set est='I' WHERE id_au="+id_au+" AND id_com="+id_com+" AND id_cpu="+id_cpu; 
		sqlUtil.update(sql);
		
	}
	
	public void desactivarCompetenciaAulaxCurso(Integer id_au, Integer id_com, Integer id_cua, Integer id_cpu){
		String sql = "update aca_competencia_aula set est='I' WHERE id_au="+id_au+" AND id_com="+id_com+" AND id_cua="+id_cua+" AND id_cpu="+id_cpu; 
		sqlUtil.update(sql);
		
	}
	
	public void activarCompetenciaAulaxArea(Integer id_au, Integer id_com, Integer id_cpu){
		String sql = "update aca_competencia_aula set est='A' WHERE id_au="+id_au+" AND id_com="+id_com+" AND id_cpu="+id_cpu; 
		sqlUtil.update(sql);
		
	}
	
	public void activarCompetenciaAulaxCurso(Integer id_au, Integer id_com, Integer id_cua, Integer id_cpu){
		String sql = "update aca_competencia_aula set est='A' WHERE id_au="+id_au+" AND id_com="+id_com+" AND id_cua="+id_cua+" AND id_cpu="+id_cpu; 
		sqlUtil.update(sql);
		
	}
	
	public Row existeCompetencia(Integer id_au, Integer id_cpu, Integer id_dcare,Integer id_desdc, Integer id_cua) {
		String sql = "SELECT comau.id, comau.id_com, comau.est \r\n" ; 
				sql += "FROM `aca_competencia_aula` comau\r\n" ; 
				sql += "INNER JOIN `aca_competencia_dc` com ON comau.`id_com`=com.id\r\n"; 
				sql += "WHERE comau.`id_au`=? AND comau.`id_cpu`=? AND com.`id_dcare`=? and comau.id_com=?";
				if(id_cua!=null) {
					sql += " AND comau.id_cua="+id_cua;
				}
				
				List<Row> rows = sqlUtil.query(sql, new Object[]{id_au, id_cpu, id_dcare, id_desdc});
				
				if(rows.size()==0)
					return null;
				else 
					return rows.get(0);

	}
}
