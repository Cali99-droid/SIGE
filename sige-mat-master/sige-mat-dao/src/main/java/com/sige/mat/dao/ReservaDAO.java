package com.sige.mat.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.ReservaDAOImpl;
import com.tesla.colegio.model.Reserva;
import com.tesla.colegio.model.ReservaCuota;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row; 

/**
 * Define m�todos DAO operations para la entidad reserva.
 * @author MV
 *
 */
@Repository
public class ReservaDAO extends ReservaDAOImpl{
	final static Logger logger = Logger.getLogger(ReservaDAO.class);
	
	@Autowired
	private SQLUtil sqlUtil;

	/*Devuelve los reservados por grado*/
	public Integer[] getCapacidadGrado(Integer id_per, Integer id_gra) {
		
		Integer[] capacidadVacante = new Integer[]{0,0};
				
		String sql = "select sum(cap) cap from col_aula where id_grad=" + id_gra + " and id_per=" + id_per;
		List<Map<String,Object>> cap = jdbcTemplate.queryForList(sql);
		capacidadVacante[0] =Integer.parseInt(cap.get(0).get("cap").toString());
		
		//tomar en cuenta el periodo
		sql = "select count(r.id ) total from mat_reserva r where r.id_per=? and r.id_gra=? and curdate()<=r.fec_lim and not exists( "
			+ " select m.id_alu from mat_matricula m where m.id_alu = r.id_alu and m.id_per=? and m.id_gra=?)";
		
		List<Map<String,Object>> insctritos = jdbcTemplate.queryForList(sql, new Object[]{id_per,id_gra,id_per,id_gra});
		capacidadVacante[1] = Integer.parseInt(insctritos.get(0).get("total").toString());
		
		return capacidadVacante;
	}
	
	
	/*Devuelve los reservados por aula*/
	public Integer[] getCapacidad(Integer id_au) {
		
		Integer[] capacidadVacante = new Integer[]{0,0};
		
		String sqlParametro = "select val from mod_parametro where nom='VACANTES' AND ID_MOD=2";
		
		String criterio = jdbcTemplate.queryForObject(sqlParametro,String.class);
		
		String sql = "select cap from col_aula where id=" + id_au;
		List<Map<String,Object>> cap = jdbcTemplate.queryForList(sql);
		capacidadVacante[0] =Integer.parseInt(cap.get(0).get("cap").toString());
		
		//tomar en cuenta el periodo
		List<Map<String,Object>> insctritos = new ArrayList<Map<String,Object>>();
		
		if (criterio.equals("0")){
			sql = "select count(r.id ) total from mat_reserva r where r.id_au=? and curdate()<=r.fec_lim and not exists( "
				+ " select m.id_alu from mat_matricula m where m.id_alu = r.id_alu and m.id_au=?)";
		
			insctritos = jdbcTemplate.queryForList(sql, new Object[]{id_au,id_au});
			
		}else if (criterio.equals("1")){
			sql = "select count(r.id ) total from mat_reserva r where r.id_au=? and curdate()<=r.fec_lim and not exists( "
					+ " select m.id_alu from mat_matricula m where m.id_alu = r.id_alu )";
		
			insctritos = jdbcTemplate.queryForList(sql, new Object[]{id_au});
		}
		else {
			sql = criterio;
		
			insctritos = jdbcTemplate.queryForList(sql, new Object[]{id_au});
		}
		
		
		capacidadVacante[1] = Integer.parseInt(insctritos.get(0).get("total").toString());
		
		return capacidadVacante;
	}
	
public List<Map<String,Object>> listSecciones(Integer id_suc, Integer id_grad, Integer id_anio) {
		
		String sql = "SELECT au.* FROM `col_aula` au, `per_periodo` per, ges_servicio ser, `ges_sucursal` suc, `col_anio` a"
				+ " WHERE au.`id_per`=per.`id` AND per.`id_srv`=ser.id AND ser.`id_suc`=suc.`id` AND per.`id_anio`=a.`id`"
				+ " AND suc.id="+id_suc+" AND au.`id_grad`="+id_grad+" AND a.id='"+id_anio+"' ORDER BY au.`secc` ASC";
		List<Map<String,Object>> listSecciones = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return listSecciones;
	} 

public ReservaCuota getMontoReserva(Integer id_anio, Integer id_alu){
	
	String sql = "SELECT f.* FROM `mat_reserva` r "
			+ " inner join per_periodo p on p.id = r.id_per "
			+ " inner JOIN fac_reserva_cuota f on f.id_res = r.id "
			+ " where p.id_anio=" + id_anio + " and r.id_alu=" + id_alu;
	
	List<ReservaCuota> reservas = jdbcTemplate.query(sql, new BeanPropertyRowMapper<ReservaCuota>(ReservaCuota.class));
	
	logger.info(sql);
	
	if (reservas.size()==0)
		return null;
	else
		return reservas.get(0);
}
	
	public List<Map<String,Object>> listReserva(String fec, Integer id_au, String mat, Integer id_gra, Integer id_suc) {
		String q_fec=" ";
		String q_au=" ";
		String q_mat=" ";
		String q_grad=" ";
		
		if(! fec.equals("")){
			q_fec=" AND res.`fec`='"+fec+"'";
		}
		if(id_au!=null){
			q_au=" AND res.id_au="+id_au;
		 }
		
		if(id_gra!=null){
			q_grad=" AND res.id_gra="+id_gra;
		 }
		
		if(mat.equals("S")){
			q_mat=" AND mat.id IS NOT NULL ";
		} else if(mat.equals("N")){
			q_mat=" AND mat.id IS NULL ";
		} 
		 
		 
		
		String sql = "SELECT DISTINCT fa.`nro_recibo`, CONCAT( alu.ape_pat,' ', alu.ape_mat,' ', alu.nom) AS 'alumno', niv.`nom` nivel, g.`nom` gra, au.`secc`, "
				+ " res.`fec`, res.`fec_lim`,res.`id_au`,"
				+ " IF (mat.id IS NOT NULL,'SI','NO') AS 'matricula'"
				+ " FROM alu_alumno alu LEFT JOIN `mat_reserva` res ON `alu`.id=res.`id_alu`"
				+ " LEFT JOIN `fac_reserva_cuota` fa ON fa.`id_res`=res.id"
				+ " LEFT JOIN `cat_grad` g ON res.`id_gra`=g.id"
				+ " LEFT JOIN `col_aula` au ON res.`id_au`= au.`id` AND au.`id_grad`=g.`id` "
				+ " LEFT JOIN `cat_nivel` niv ON g.`id_nvl`=niv.`id`"
				+ " LEFT JOIN `mat_matricula` mat ON mat.`id_alu`=alu.`id` "
				+ " LEFT JOIN per_periodo per ON per.id=res.id_per "
				+ " LEFT JOIN ges_servicio ser ON per.id_srv=ser.id AND ser.nom=niv.nom"
				+ " LEFT JOIN ges_sucursal suc ON ser.id_suc=suc.id"
				+ " WHERE res.`id`<>'NULL' and per.id_anio=2 and suc.id="+id_suc+" "+q_fec+" "+q_au+" "+q_mat+" "+q_grad+" "
				+ " ORDER BY g.`id` ASC, au.`secc` ASC, alu.`ape_pat` ASC, alu.`ape_mat` ASC";
		List<Map<String,Object>> listReserva = jdbcTemplate.queryForList(sql);
		logger.info(sql);
		return listReserva;
	}
	
	public Reserva getReservaxAnio(Integer id_anio, Integer id_alu){
		String sql = "select r.* from mat_reserva  r inner join per_periodo p on p.id = r.id_per  where p.id_anio=? and id_alu=?";
		return jdbcTemplate.queryForObject(sql, new Object[]{id_anio, id_alu}, Reserva.class);
	}
	
	public Row obtenerReservaAlumno(Integer id_anio, Integer id_alu){

		String sql = "select r.* from mat_reserva  r inner join per_periodo p on p.id = r.id_per where p.id_anio=? and id_alu=?";
				//+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
		
		List<Row> list = sqlUtil.query(sql, new Object[]{id_anio, id_alu});
		if(list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
		
		
		//return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_mat}));
	}
	
	public List<Row> listarReserva( Integer id_au, String mat,Integer id_niv, Integer id_gra, Integer id_suc, String id_anio)throws Exception {
		
		String q_fec=" ";
		String q_au=" ";
		String q_mat=" ";
		String q_niv=" ";
		String q_grad=" ";
		
		Param param = new Param();
		
		/*if(! fec.equals("")){
			//q_fec=" AND res.`fec`= DATE_FORMAT('"+fec+"','%Y-%m-%d')";
			q_fec=" AND res.fec= :fec";
			param.put("fec", FechaUtil.toDate(fec));
		}*/
		if(id_au!=null){
			q_au=" AND res.id_au=:id_au";
			param.put("id_au", id_au);
		 }
		
		if(id_niv!=null){
			q_niv=" AND res.id_niv=:id_niv";
			param.put("id_niv", id_niv);
		 }
		
		if(id_gra!=null){
			q_grad=" AND res.id_gra=:id_gra";
			param.put("id_gra", id_gra);
		 }
		
		if(mat.equals("S")){
			q_mat="WHERE t.matricula='SI' ";
		} else if(mat.equals("N")){
			q_mat="WHERE t.matricula='NO' ";
		} 
		 
		 
		
		String sql = "SELECT * FROM (SELECT DISTINCT fa.`nro_recibo`, CONCAT( p.ape_pat,' ', p.ape_mat,' ', p.nom) AS 'alumno', niv.`nom` nivel, g.`nom` gra, au.`secc`, "
				+ "\n res.`fec`, res.`fec_lim`,res.`id_au`,"
				+ "\n IF((SELECT mat.id FROM `mat_matricula` mat INNER JOIN `per_periodo` per_mat ON per_mat.id=mat.`id_per` WHERE per_mat.id_anio="+id_anio+" AND mat.`id_alu`=alu.`id` and per_mat.id_tpe=1)IS NOT NULL,'SI','NO') AS matricula"
				+ "\n FROM alu_alumno alu LEFT JOIN `mat_reserva` res ON `alu`.id=res.`id_alu`"
				+ "\n LEFT JOIN `col_persona` p ON alu.`id_per`=p.id"
				+ "\n LEFT JOIN `fac_reserva_cuota` fa ON fa.`id_res`=res.id"
				+ "\n LEFT JOIN `cat_grad` g ON res.`id_gra`=g.id"
				+ "\n LEFT JOIN `col_aula` au ON res.`id_au`= au.`id` AND au.`id_grad`=g.`id` "
				+ "\n LEFT JOIN `cat_nivel` niv ON g.`id_nvl`=niv.`id`"
				+ "\n LEFT JOIN per_periodo per ON per.id=res.id_per "
				+ "\n LEFT JOIN ges_servicio ser ON per.id_srv=ser.id AND ser.nom=niv.nom"
				+ "\n LEFT JOIN ges_sucursal suc ON ser.id_suc=suc.id"
				+ "\n WHERE res.`id`<>'NULL' and per.id_anio="+id_anio+" and suc.id="+id_suc+" "+q_fec+" "+q_au+" "+q_niv+" "+q_grad+" "
				+ "\n ORDER BY g.`id` ASC, au.`secc` ASC, alu.`ape_pat` ASC, alu.`ape_mat` ASC) t "+q_mat+"";
		logger.info(sql);
		return sqlUtil.query(sql,param);

	}
	
	public List<Row> datosAlumno(Integer id_res) {
		String sql = "SELECT mat.id, mat.id_fam, CONCAT (alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`) alumno, alu.nro_doc, concat(niv.`nom`,' - ', gra.`nom`,' - ', au.`secc`) aula, niv.`nom` nivel, gra.`nom` grado, au.`secc` seccion, IF(id_gen=0,'FEMENINO','MASCULINO') sexo, alu.`celular`,  TIMESTAMPDIFF(YEAR, alu.`fec_nac`, CURDATE()) AS edad, alu.id id_alu"
				+ " FROM `mat_reserva` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN `col_aula` au ON mat.`id_au`=au.`id` "
				+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`"
				+ " INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
				+ " WHERE mat.`id`=?";
		Object[] params = new Object[]{id_res};
		return sqlUtil.query(sql,params);	
	}
	
	public Reserva getPeriodoByParams(Param param){
		String sql ="select r.* from mat_reserva r inner join per_periodo p on p.id  = r.id_per and r.id_alu and r.id_alu=" + param.get("id_alu") + " and p.id_anio=" + param.get("id_anio");
		
		return sqlUtil.queryForJavaBean(sql, Reserva.class);
		
	}
}
