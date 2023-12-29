package com.sige.mat.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.TarifasEmergenciaDAOImpl;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;

/**
 * Define m�todos DAO operations para la entidad tarifas_emergencia.
 * @author MV
 *
 */
@Repository
public class TarifasEmergenciaDAO extends TarifasEmergenciaDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public Row listarTarifasMEs( Integer mes, Integer id_anio){

		String sql = "SELECT emer.* "
				+ " FROM `mat_tarifas_emergencia` emer INNER JOIN `per_periodo` per ON emer.`id_per`=per.`id`"
				//+ " WHERE per.id=? AND emer.mes=?";
				+ " WHERE emer.mes=? and per.id_anio="+id_anio;
		List<Row> list = sqlUtil.query(sql, new Object[]{  mes});
		if(list.size()>0)
		return list.get(0);
		else
			return null;
	}
	
	public void updateTarifaEmergencia(Integer id_tar) {
		
		String sql = "UPDATE mat_tarifas_emergencia set procesado=1  where id=?";
		 sqlUtil.update(sql, new Object[] { id_tar });

	}
}
