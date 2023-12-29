package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CapacidadDcDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad capacidad_dc.
 * @author MV
 *
 */
@Repository
public class CapacidadDcDAO extends CapacidadDcDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listarCapacidades(Integer id_com) {
		String sql = "select id, nom value from aca_capacidad_dc cap where cap.id_com="+id_com+" ORDER BY orden";
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarCapacidadesxAula(Integer id_au, Integer id_dcare, Integer id_gra) {
		String sqlCapacidad="SELECT DISTINCT cap.`id_com` com_id, com.nom com_nom, cap.`id`, cap.`nom`, COUNT(cap.id) count_cap, desau.id id_desau  \n" + 
				"				 FROM aca_capacidad_dc cap INNER JOIN aca_competencia_dc com ON cap.`id_com`=com.`id`\n" + 
				"				 INNER JOIN `aca_desempenio_dc` des ON des.`id_com`=com.id\n" + 
				"				 LEFT JOIN `aca_desempenio_aula` desau ON desau.`id_desdc`=des.`id` AND desau.`est`='A' AND desau.id_cap=cap.id AND desau.`id_au`=\n" + id_au+ 
				"				 WHERE com.`id_dcare`=? AND des.id_gra=?"+
		        "				 GROUP  BY cap.id "	;	
		
		return sqlUtil.query(sqlCapacidad,new Object[] {id_dcare, id_gra});

	}
	
}
