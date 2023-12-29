package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.ContratoTrabajadorDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad contrato_trabajador.
 * @author MV
 *
 */
@Repository
public class ContratoTrabajadorDAO extends ContratoTrabajadorDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listarContratoTrabajador(Integer id_tra) {

		String sql ="SELECT con.*, rem_cat.rem remuneracion from rhh_contrato_trabajador con"
				+ " inner join rhh_remuneracion_cat rem_cat ON con.id_rem_cat=rem_cat.id "
				+ " where con.id_tra=?";
		
		return sqlUtil.query(sql, new Object[] {id_tra});

	}
}
