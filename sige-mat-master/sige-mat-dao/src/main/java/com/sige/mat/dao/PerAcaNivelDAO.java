package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.PerAcaNivelDAOImpl;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad per_aca_nivel.
 * @author MV
 *
 */
@Repository
public class PerAcaNivelDAO extends PerAcaNivelDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listarPeriodosAcademicosxNivelAnio(Integer id_niv, Integer id_anio, Integer id_gir) {
		
		String sql = "SELECT cpan.id, cpa.nom value FROM `cat_per_aca_nivel` cpan INNER JOIN cat_periodo_aca cpa ON cpa.`id`=cpan.`id_cpa`\n" + 
				" WHERE cpan.`id_niv`="+id_niv+" AND cpan.`id_anio`="+id_anio+" AND cpan.id_gir="+id_gir;
		
		return sqlUtil.query(sql);

	}
	
}
