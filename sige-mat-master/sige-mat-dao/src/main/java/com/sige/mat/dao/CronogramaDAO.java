package com.sige.mat.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.CronogramaDAOImpl;
import com.tesla.colegio.model.ConfFechas;
import com.tesla.colegio.model.Cronograma;
import com.tesla.colegio.model.Matricula;


/**
 * Define m�todos DAO operations para la entidad cronograma.
 * @author MV
 *
 */
@Repository
public class CronogramaDAO extends CronogramaDAOImpl{
	final static Logger logger = Logger.getLogger(CronogramaDAO.class);

	/**
	 * Valida que alumnos antiguas esten dentro del cronograma de matricula
	 * @param id_anio A�o de matricula
	 * @param apellidoPaterno Apellido paterno
	 * @return
	 */
	public boolean dentroCronograma(Integer id_anio, String apellidoPaterno){
		
		String sql = "SELECT * from mat_cronograma c where fec_mat=curdate() and upper(c.del)<=upper('" + apellidoPaterno + "') and upper(c.al)>=upper('" + apellidoPaterno + "') and id_anio=" + id_anio; 
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		//logger.info(sql);
		return list.size()>0;
	}

	/**
	 * A: Cronograma para los antiguos alumnos
	 * E: Extemporaneos para los alumnos antiguos
	 * N: Pueden matricularse los alumnos nuevos
	 * null: No hay cronograma para nadie!!!!!!!! 
	 * @param id_anio
	 * @return tipo de cronograma vigente
	 */
	public String getTipoCronograma(Integer id_anio){
		
		String sql = "select * from mat_cronograma where id_anio=? and CURRENT_DATE = fec_mat"; 
		
		List<Cronograma> cronogramas = jdbcTemplate.query(sql, new Object[] { id_anio},new BeanPropertyRowMapper<Cronograma>(Cronograma.class));

		if (cronogramas.size()>0){
			//estamos en epoca de cronograma
			return "A";
		}else{
			//ver si estamos en epoca de matricula
			sql = "select * from mat_conf_fechas where id_anio=? and CURRENT_DATE between del and al and tipo=?";
			List<ConfFechas> fechas = jdbcTemplate.query(sql, new Object[] { id_anio,"E"},new BeanPropertyRowMapper<ConfFechas>(ConfFechas.class));
	
			if (fechas.size()>0){
				//estamos en epoca de cronograma
				return "E";
			}else{
				sql = "select * from mat_conf_fechas where id_anio=? and tipo=? and  CURRENT_DATE>= del and (al is null or CURRENT_DATE<= al )";
				fechas = jdbcTemplate.query(sql, new Object[] { id_anio,"N"},new BeanPropertyRowMapper<ConfFechas>(ConfFechas.class));
			
				if (fechas.size()>0){
					return "N";
					
				}else
					return null;
			}
			
		}

		
	}
	

	/**
	 * Consulta si existe un cronograma vigente (alumnos antiguos - extemporaneo y normal)
	 * @param id_anio
	 * @param tipoCronograma
	 * @return
	 */
	public boolean alumnosAntiguosTienenVigente(Integer id_anio){
		
		String sql = "select id from mat_cronograma where id_anio=? and date(curdate()) = fec_mat and est=?"; 
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, new Object[]{id_anio, "A"});
		
		sql = "select id from mat_conf_fechas where id_anio=? and CURRENT_TIMESTAMP() >=del and CURRENT_TIMESTAMP() <=al and tipo=?"; 
		
		List<Map<String,Object>> list2 = jdbcTemplate.queryForList(sql, new Object[]{id_anio, "AC"});
		
		
		return list.size()>0 && list2.size()>0 ;
	}

	
}
