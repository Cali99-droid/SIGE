package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import  com.sige.mat.dao.impl.DcnAreaDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad dcn_area.
 * @author MV
 *
 */
@Repository
public class DcnAreaDAO extends DcnAreaDAOImpl{
	
    @Autowired
    private SQLUtil sqlUtil;
    
	public List<Row> listarAreasCombo(Integer id_dcniv) {

		String sql ="SELECT dcnare.id, are.nom value\r\n" + 
				"FROM aca_dcn_area dcnare INNER JOIN cat_area are ON dcnare.id_are=are.id\r\n" + 
				"WHERE dcnare.id_dcniv=?;";
		return sqlUtil.query(sql,  new Object[] {id_dcniv});

	}
	
	public List<Row> listarAreasComboAnio(Integer id_dcniv, Integer id_anio, Integer id_gra, Integer id_gir) {

		String sql ="SELECT DISTINCT dcnare.id, are.nom value\r\n" + 
				"FROM aca_dcn_area dcnare INNER JOIN cat_area are ON dcnare.id_are=are.id\r\n" + 
				" INNER JOIN aca_dcn_nivel dcniv ON dcnare.`id_dcniv`=dcniv.`id` "+
				" INNER JOIN col_area_anio caa ON are.id=caa.`id_area` AND caa.`id_niv`=`dcniv`.`id_niv` "+
				"WHERE dcnare.id_dcniv=? AND caa.`id_anio`=? AND caa.id_gra=? AND caa.id_gir=? AND caa.est='A' ORDER BY dcnare.ord ;";
		return sqlUtil.query(sql,  new Object[] {id_dcniv, id_anio, id_gra, id_gir});

	}
}
