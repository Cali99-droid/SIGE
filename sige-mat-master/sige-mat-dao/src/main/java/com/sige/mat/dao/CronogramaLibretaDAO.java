package com.sige.mat.dao;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CronogramaLibretaDAOImpl;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Define m�todos DAO operations para la entidad cronograma_libreta.
 * @author MV
 *
 */
@Repository
public class CronogramaLibretaDAO extends CronogramaLibretaDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listarNiveles(){
		
		String sql = "select distinct n.id, n.nom  value "
				+ " from not_cronograma_libreta c inner join cat_nivel n  on c.id_niv= n.id"
				+ " where curdate() between c.fec_ini and c.fec_fin";
		
		return sqlUtil.query(sql);
		
	}
	



	public List<Row> listaPeriodos(int id_niv, int id_anio) {//

		String sql = "SELECT per_uni.id, CONCAT(per_aca.nom,' ',per_uni.nump) as value, per_uni.nump AS aux1,per_uni.numu_ini AS aux2,"
				+ " per_uni.numu_fin AS aux3 " 
				+ " FROM col_per_uni per_uni "
				+ " inner JOIN cat_per_aca_nivel per_niv ON per_uni.id_cpa=per_niv.id"
				+ " inner JOIN cat_periodo_aca per_aca ON per_aca.id=per_niv.id_cpa"
				+ " inner JOIN not_cronograma_libreta c on c.id_niv= per_niv.id and c.id_cpu=per_uni.id"
				+ " where "
				+ " (curdate() between c.fec_ini and c.fec_fin) and "
				+ " per_niv.id_niv=? and per_uni.id_anio=?";

		return sqlUtil.query(sql, new Object[] { id_niv, id_anio });

	}
	
	public List<Row> listarPeriodosX_Nivel(int id_niv, int id_anio, int id_gir) {

		String sql = "SELECT per_uni.id, CONCAT(per_aca.nom,' ',per_uni.nump) as value, per_uni.nump AS aux1,per_uni.numu_ini AS aux2,"
				+ "\n per_uni.numu_fin AS aux3, CASE WHEN per_uni.nump=1 THEN 'I' WHEN per_uni.nump=2 THEN 'II' WHEN per_uni.nump=3 THEN 'III' WHEN per_uni.nump=4 THEN 'IV' WHEN per_uni.nump=5 THEN 'V' ELSE ''END nro_per_rom " 
				+ "\n FROM col_per_uni per_uni "
				+ "\n inner JOIN cat_per_aca_nivel per_niv ON per_uni.id_cpa=per_niv.id"
				+ "\n inner JOIN cat_periodo_aca per_aca ON per_aca.id=per_niv.id_cpa"
				+ "\n where per_niv.id_niv=? and per_uni.id_anio=? and per_niv.id_gir=? order by  per_uni.nump  ";

		return sqlUtil.query(sql, new Object[] { id_niv, id_anio, id_gir });

	}
	
	public Row datosPeriodo(Integer id_cpu, int id_anio) {

		String sql = "SELECT per_uni.id, CONCAT(per_aca.nom,' ',per_uni.nump) as value, per_uni.nump AS aux1,per_uni.numu_ini AS aux2,"
				+ "\n per_uni.numu_fin AS aux3, CASE WHEN per_uni.nump=1 THEN 'I' WHEN per_uni.nump=2 THEN 'II' WHEN per_uni.nump=3 THEN 'III' WHEN per_uni.nump=4 THEN 'IV' WHEN per_uni.nump=5 THEN 'V' ELSE ''END nro_per_rom " 
				+ "\n FROM col_per_uni per_uni "
				+ "\n inner JOIN cat_per_aca_nivel per_niv ON per_uni.id_cpa=per_niv.id"
				+ "\n inner JOIN cat_periodo_aca per_aca ON per_aca.id=per_niv.id_cpa"
				+ "\n where per_uni.id=? and per_uni.id_anio=? order by  per_uni.nump  ";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,new Object[] { id_cpu, id_anio });
		
		return SQLFrmkUtil.listToRows(list).get(0);	

	}

	public List<Row> listarPeriodosVigentes(Integer id_niv, int id_anio) {

		String sql = "SELECT per_uni.id, CONCAT(per_aca.nom,' ',per_uni.nump) as value, per_uni.nump AS aux1,per_uni.numu_ini AS aux2,"
				+ " per_uni.numu_fin AS aux3 "
				+ " FROM col_per_uni per_uni "
				+ " INNER JOIN cat_per_aca_nivel per_niv ON per_uni.id_cpa=per_niv.id"
				+ " INNER JOIN cat_periodo_aca per_aca ON per_aca.id=per_niv.id_cpa"
				+ " WHERE per_niv.id_niv=? AND per_uni.id_anio=? AND CURDATE()>= per_uni.`fec_ini_ing` AND CURDATE()<=per_uni.`fec_fin_ing`"
				+ " ORDER BY  per_uni.nump";

		return sqlUtil.query(sql, new Object[] { id_niv, id_anio });

	}
	
}
