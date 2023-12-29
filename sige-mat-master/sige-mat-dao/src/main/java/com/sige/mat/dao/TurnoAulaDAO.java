package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.TurnoAulaDAOImpl;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;

/**
 * Define m�todos DAO operations para la entidad turno_aula.
 * @author MV
 *
 */
@Repository
public class TurnoAulaDAO extends TurnoAulaDAOImpl{
	
	@Autowired
    private SQLUtil sqlUtil;
	
	/*Lista de instrumentos usados*/
	public List<Row> listarTurnosxAula(Integer id_au, Integer id_cic) {
		String sql = "SELECT tur.*, t.id_atur, cit.id id_cit, cit.`hor_ini`, cit.`hor_fin`FROM \r\n" + 
				"`col_turno` tur  INNER JOIN `col_ciclo_turno` cit ON tur.`id`=cit.`id_tur`\r\n" + 
				"LEFT JOIN ( \r\n" + 
				"SELECT atur.id id_atur, atur.`id_cit` FROM col_turno_aula atur \r\n" + 
				"WHERE atur.`id_au`="+id_au+") t ON cit.id=t.id_cit \r\n" + 
				"WHERE cit.`id_cic`=" +id_cic+" AND cit.est='A' "+
				" ORDER BY tur.id";
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	public void desactivarTurnoAula(Integer id_atur, Integer id_cit){
		String sql = "update col_turno_aula set est='I' WHERE id="+id_atur+" AND id_cit="+id_cit;
		sqlUtil.update(sql);
		
	}
}
