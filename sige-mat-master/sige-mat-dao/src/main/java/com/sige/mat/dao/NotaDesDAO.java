package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import  com.sige.mat.dao.impl.NotaDesDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad nota_des.
 * @author MV
 *
 */
@Repository
public class NotaDesDAO extends NotaDesDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listarNotasDesempenio(Integer id_desau) {
		String sql = "SELECT * FROM `aca_desempenio_aula` desau INNER JOIN not_nota_des nota ON desau.id=nota.`id_desau`\r\n" + 
				"WHERE nota.`id_desau`=?";
		return sqlUtil.query(sql, new Object[]{id_desau});

	}
	
	public List<Row> listarNotasCapacidades(Integer id_cap, Integer id_au, Integer id_cpu, Integer id_cua) {
		String sql = "SELECT * FROM `aca_desempenio_aula` desau INNER JOIN not_nota_des nota ON desau.id=nota.`id_desau`\r\n" ;
			   sql += "WHERE desau.`id_cap`=? and desau.id_au=? and desau.id_cpu=?";
		if(id_cua!=null) {
			sql += " AND desau.id_cua="+id_cua;
		}
		return sqlUtil.query(sql, new Object[]{id_cap, id_au, id_cpu});

	}
	
	public List<Row> listarNotasCompetencias(Integer id_com, Integer id_au, Integer id_cpu, Integer id_cua) {
		String sql = "SELECT * FROM `aca_desempenio_aula` desau INNER JOIN not_nota_des nota ON desau.id=nota.`id_desau`\r\n" ;
			   sql += " INNER JOIN aca_capacidad_dc cap ON desau.id_cap=cap.id ";
			   sql += " INNER JOIN aca_competencia_dc com ON cap.id_com=com.id ";
			   sql += "WHERE com.`id`=? and desau.id_au=? and desau.id_cpu=?";
		if(id_cua!=null) {
			sql += " AND desau.id_cua="+id_cua;
		}
		return sqlUtil.query(sql, new Object[]{id_com, id_au, id_cpu});

	}
	
	public void deleteNotasDesempenios(Integer id_au, Integer id_cpu, Integer id_cua) {
		String sql = "DELETE FROM not_nota_des WHERE id_desau IN (SELECT desau.id FROM `aca_desempenio_aula` desau WHERE desau.id_au=? AND desau.id_cpu=? ";
		if(id_cua!=0) {
			sql += " and desau.id_cua="+id_cua;
		}
		sql +=" )";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, new Object[]{id_au,id_cpu});
	}
}
