package com.sige.mat.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.ReservaCuotaDAOImpl;
import com.tesla.colegio.model.ReservaCuota;


/**
 * Define m�todos DAO operations para la entidad reserva_cuota.
 * @author MV
 *
 */
@Repository
public class ReservaCuotaDAO extends ReservaCuotaDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;

	
	public BigDecimal obtenerMonto(Integer id_alu){
		
		String sql = "select mov.monto from fac_reserva_cuota fr ";
		sql += " inner join mat_reserva r on r.id = fr.id_res ";
		sql += " inner join fac_movimiento mov on mov.id = fr.id_fmo ";
		sql += " and r.id_alu=? ";
		sql += " and id_per in ( select id from per_periodo where id_anio=4)";
		//sql += " and mov.fec_sunat is  not null";
		
		List<Map<String,Object>> listReserva = jdbcTemplate.queryForList(sql,id_alu);

		if(listReserva.size()>0 && listReserva.get(0).get("monto")!=null){
			return new BigDecimal(listReserva.get(0).get("monto").toString());
		}else
			return new BigDecimal(0);
		
	}
}
