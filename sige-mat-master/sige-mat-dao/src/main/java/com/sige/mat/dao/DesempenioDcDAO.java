package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.DesempenioDcDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad desempenio_dc.
 * @author MV
 *
 */
@Repository
public class DesempenioDcDAO extends DesempenioDcDAOImpl{
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listarDesempenios(Integer id_com, Integer id_gra) {
		String sql = "select id, nom value from aca_desempenio_dc des where des.id_com="+id_com+" and id_gra="+id_gra+" ORDER BY orden";
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarDesempeniosxAula(Integer id_au, Integer id_dcare, Integer id_gra, Integer id_cpu, Integer id_cua) {
		String sqlDesempenios="SELECT DISTINCT des.`id` id_des, des.nom des_nom, cap.`id` id_cap, com.nom com_nom, com.id id_com, desau.id id_desau  \n" ; 
				sqlDesempenios += "								 FROM aca_capacidad_dc cap INNER JOIN aca_competencia_dc com ON cap.`id_com`=com.`id`\n"; 
				sqlDesempenios += "								 INNER JOIN `aca_desempenio_dc` des ON des.`id_com`=com.id \n";
				sqlDesempenios += "				 				 LEFT JOIN `aca_desempenio_aula` desau ON desau.`id_desdc`=des.`id` AND desau.`est`='A' AND desau.id_cap=cap.id AND desau.`id_au`=\n" + id_au+" AND desau.id_cpu="+id_cpu; 
				if(id_cua!=null) {
					sqlDesempenios += " AND desau.id_cua="+id_cua;
				}
				sqlDesempenios += "								 WHERE com.`id_dcare`=? AND des.id_gra=?";
		
		return sqlUtil.query(sqlDesempenios,new Object[] {id_dcare, id_gra});
	}
	
}
