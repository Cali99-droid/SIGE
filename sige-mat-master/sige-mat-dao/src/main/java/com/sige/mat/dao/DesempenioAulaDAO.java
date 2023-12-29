package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import  com.sige.mat.dao.impl.DesempenioAulaDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad desempenio_aula.
 * @author MV
 *
 */
@Repository
public class DesempenioAulaDAO extends DesempenioAulaDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listarDesmepenioAulaCurso(Integer id_au, Integer id_cpu, Integer id_cua) {
		String sql = "SELECT des.id, desau.`id_cap`, desau.id id_desau \r\n" + 
				"FROM `aca_desempenio_aula` desau\r\n" + 
				"INNER JOIN `aca_desempenio_dc` des ON desau.`id_desdc`=des.`id`\r\n" + 
				"WHERE desau.`id_au`=? AND desau.`id_cpu`=? AND desau.`id_cua`=?";
		return sqlUtil.query(sql, new Object[]{id_au, id_cpu, id_cua});

	}
	
	public List<Row> listarDesmepenioAulaArea(Integer id_au, Integer id_cpu, Integer id_dcare) {
		String sql = "SELECT des.id, desau.`id_cap`, desau.id id_desau \r\n" + 
				"FROM `aca_desempenio_aula` desau\r\n" + 
				"INNER JOIN `aca_desempenio_dc` des ON desau.`id_desdc`=des.`id`\r\n" + 
				"INNER JOIN `aca_competencia_dc` com ON des.`id_com`=com.id\r\n" + 
				"WHERE desau.`id_au`=? AND desau.`id_cpu`=? AND com.`id_dcare`=?";
		return sqlUtil.query(sql, new Object[]{id_au, id_cpu, id_dcare});

	}
	
	public List<Row> listarCompetenciasAulaCurso(Integer id_au, Integer id_cpu, Integer id_cua) {
		String sql = "SELECT comau.id, comau.`id_com` \r\n" + 
				"FROM `aca_competencia_aula` comau\r\n" + 
				"INNER JOIN `aca_competencia_dc` com ON comau.`id_com`=com.`id`\r\n" + 
				"WHERE comau.`id_au`=? AND comau.`id_cpu`=? AND comau.`id_cua`=?";
		return sqlUtil.query(sql, new Object[]{id_au, id_cpu, id_cua});

	}
	
	public List<Row> listarCompetenciasAulaArea(Integer id_au, Integer id_cpu, Integer id_dcare) {
		String sql = "SELECT comau.id, comau.`id_com` \r\n" + 
				"FROM `aca_competencia_aula` comau\r\n" + 
				"INNER JOIN `aca_competencia_dc` com ON comau.`id_com`=com.`id`\r\n" + 
				"WHERE comau.`id_au`=? AND comau.`id_cpu`=? AND com.`id_dcare`=?";
		return sqlUtil.query(sql, new Object[]{id_au, id_cpu, id_dcare});

	}
	
	public Row existeDesempenio(Integer id_au, Integer id_cpu, Integer id_dcare, Integer id_cap, Integer id_desdc, Integer id_cua) {
		String sql = "SELECT desau.id, desau.id_cap, desau.est \r\n" ; 
				sql += "FROM `aca_desempenio_aula` desau\r\n" ; 
				sql += "INNER JOIN `aca_desempenio_dc` des ON desau.`id_desdc`=des.`id`\r\n"; 
				sql += "INNER JOIN `aca_competencia_dc` com ON des.`id_com`=com.id\r\n"; 
				sql += "WHERE desau.`id_au`=? AND desau.`id_cpu`=? AND com.`id_dcare`=? AND desau.id_cap=? and desau.id_desdc=?";
				if(id_cua!=null) {
					sql += " AND desau.id_cua="+id_cua;
				}
				
				List<Row> rows = sqlUtil.query(sql, new Object[]{id_au, id_cpu, id_dcare, id_cap, id_desdc});
				
				if(rows.size()==0)
					return null;
				else 
					return rows.get(0);

	}
	
	public void desactivarDesemepnioAulaxArea(Integer id_au, Integer id_des, Integer id_cap, Integer id_cpu){
		String sql = "update aca_desempenio_aula set est='I' WHERE id_au="+id_au+" AND id_desdc="+id_des+" AND id_cap="+id_cap+" AND id_cpu="+id_cpu; 
		sqlUtil.update(sql);
		
	}
	
	public void desactivarDesemepnioAulaxCurso(Integer id_au, Integer id_des, Integer id_cap, Integer id_cua, Integer id_cpu){
		String sql = "update aca_desempenio_aula set est='I' WHERE id_au="+id_au+" AND id_desdc="+id_des+" AND id_cap="+id_cap+" AND id_cua="+id_cua+" AND id_cpu="+id_cpu; 
		sqlUtil.update(sql);
		
	}
	
	public void activarDesemepnioAulaxArea(Integer id_au, Integer id_des, Integer id_cap, Integer id_cpu){
		String sql = "update aca_desempenio_aula set est='A' WHERE id_au="+id_au+" AND id_desdc="+id_des+" AND id_cap="+id_cap+" AND id_cpu="+id_cpu; 
		sqlUtil.update(sql);
		
	}
	
	public void activarDesemepnioAulaxCurso(Integer id_au, Integer id_des, Integer id_cap, Integer id_cua, Integer id_cpu){
		String sql = "update aca_desempenio_aula set est='A' WHERE id_au="+id_au+" AND id_desdc="+id_des+" AND id_cap="+id_cap+" AND id_cua="+id_cua+" AND id_cpu="+id_cpu; 
		sqlUtil.update(sql);
		
	}
	
	public List<Row> listarDesmepenioAulaxCompetencia(Integer id_com, Integer id_au, Integer id_cpu, Integer id_cua) {
		String sql = "SELECT * , com.id id_com FROM `aca_desempenio_aula` desau INNER JOIN `aca_capacidad_dc` cap ON desau.`id_cap`=cap.`id`\r\n"; 
				sql +="INNER JOIN `aca_competencia_dc` com ON cap.`id_com`=com.`id`\r\n"; 
				sql +="WHERE com.`id`=? AND desau.`id_au`=? AND desau.`id_cpu`=? AND desau.`est`='A'";
				if(id_cua!=null) {
					sql +=" AND desau.id_cua="+id_cua;
				}
		return sqlUtil.query(sql, new Object[]{id_com, id_au, id_cpu});

	}
	
	public List<Row> listarDesmepenioAulaxCapacidad(Integer id_cap, Integer id_au, Integer id_cpu, Integer id_cua) {
		String sql = "SELECT * FROM `aca_desempenio_aula` desau INNER JOIN `aca_capacidad_dc` cap ON desau.`id_cap`=cap.`id`\r\n" ;
				sql +="INNER JOIN `aca_competencia_dc` com ON cap.`id_com`=com.`id`\r\n" ; 
				sql +="WHERE cap.`id`=? AND desau.`id_au`=? AND desau.`id_cpu`=? AND desau.`est`='A'";
				if(id_cua!=null) {
					sql +=" AND desau.id_cua="+id_cua;
				}
		return sqlUtil.query(sql, new Object[]{id_cap, id_au, id_cpu});

	}
	
	public List<Row> listarDesmepenioAula(Integer id_desdc, Integer id_au, Integer id_cpu, Integer id_cua, Integer id_cap) {
		String sql = "SELECT desau.* FROM `aca_desempenio_aula` desau INNER JOIN `aca_capacidad_dc` cap ON desau.`id_cap`=cap.`id`\r\n" ;
				sql +="INNER JOIN `aca_competencia_dc` com ON cap.`id_com`=com.`id`\r\n" ; 
				//sql +="INNER JOIN `aca_competencia_aula` comau ON comau.`id_com`=com.`id`\r\n" ; 
				sql +="WHERE desau.`id_desdc`=? AND desau.`id_au`=? AND desau.`id_cpu`=? AND desau.`est`='A' AND desau.id_cap=?";
				if(id_cua!=null) {
					sql +=" AND desau.id_cua="+id_cua;
				}
		return sqlUtil.query(sql, new Object[]{id_desdc, id_au, id_cpu, id_cap});

	}
	
}
