package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import  com.sige.mat.dao.impl.DcnNivelDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad dcn_nivel.
 * @author MV
 *
 */
@Repository
public class DcnNivelDAO extends DcnNivelDAOImpl{
	
    @Autowired
    private SQLUtil sqlUtil;
	
	public List<Row> listarNivelesCombo(Integer id_dcn) {

		String sql ="SELECT DISTINCT dcniv.id, niv.nom value, niv.id aux1 \r\n" + 
				"FROM aca_dcn_nivel dcniv INNER JOIN cat_nivel niv ON dcniv.id_niv=niv.id\r\n" + 
				//"INNER JOIN ges_servicio  srv ON srv.id_niv=niv.id "+
				"WHERE dcniv.id_dcn=? ORDER BY niv.nom";
		return sqlUtil.query(sql,  new Object[] {id_dcn});

	}
	
	public List<Row> listarNivelesComboxGiro(Integer id_anio, Integer id_gir) {

		String sql ="SELECT DISTINCT niv.id, niv.nom value, niv.id aux1 \r\n" + 
				"FROM aca_dcn_nivel dcniv INNER JOIN cat_nivel niv ON dcniv.id_niv=niv.id\r\n" + 
				"INNER JOIN ges_servicio  srv ON srv.id_niv=niv.id "+
				"INNER JOIN col_conf_anio_acad_dcn aca ON aca.id_dcn=dcniv.id_dcn"+
				" WHERE aca.id_anio=? and srv.id_gir=? ORDER BY niv.nom";
		return sqlUtil.query(sql,  new Object[] {id_anio, id_gir});

	}
}
