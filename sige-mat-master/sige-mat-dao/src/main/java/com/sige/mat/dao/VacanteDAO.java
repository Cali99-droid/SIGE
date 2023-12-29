package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.Periodo;
import com.tesla.frmk.sql.Param;
import com.sige.common.enums.EnumSituacionFinal;
import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.VacanteDAOImpl;


/**
 * Define m�todos DAO operations para la entidad vacante.
 * @author MV
 *
 */
@Repository
public class VacanteDAO extends VacanteDAOImpl{
	final static Logger logger = Logger.getLogger(VacanteDAO.class);
	
	 @Autowired
	    private SQLUtil sqlUtil;
	 
	 @Autowired
	    private PeriodoDAO periodoDAO;
	 
	public List<Map<String,Object>> Capacidad(String anio, Integer id_grad, Integer suc) {
		
		String sql = "select sum(au.cap) as capacidad from col_aula au, per_periodo per, col_anio a, ges_servicio ser, "
				+ " ges_sucursal suc where au.id_per=per.id and per.id_anio=a.id and per.id_srv=ser.id "
				+ " and ser.id_suc=suc.id and a.nom='"+anio+"' and au.id_grad="+id_grad+" and suc.id="+suc;
		List<Map<String,Object>> Capacidad = jdbcTemplate.queryForList(sql);	
		logger.info(sql);
		return Capacidad;
	}
	
	/**
	 * Capacidad Grado
	 * @param anio
	 * @param id_grad
	 * @param suc
	 * @return
	 */
	public Integer capacidadGrado(Integer anio, Integer id_grad, Integer id_suc) {
		
		Param param = new Param();
		param.put("anio", anio);
		param.put("id_grad", id_grad);
		param.put("id_suc", id_suc);
		String sql = "select sum(au.cap) as capacidad from col_aula au, col_ciclo cic, per_periodo per, col_anio a, ges_servicio ser, "
				+ " ges_sucursal suc where au.id_cic=cic.id and cic.id_per=per.id and per.id_anio=a.id and per.id_srv=ser.id "
				+ " and ser.id_suc=suc.id and a.nom=:anio and au.id_grad=:id_grad and suc.id=:id_suc and per.id_tpe=1";
		Integer capacidadGrado=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("getCapacidadxAula:" + sql);
		return capacidadGrado;
	}
	
	/**
	 * Capacidad Grado
	 * @param id_cic
	 * @param id_grad
	 * @return
	 */
	public Integer capacidadGradoCiclo(Integer id_cic, Integer id_grad) {
		
		Param param = new Param();
		param.put("id_cic", id_cic);
		param.put("id_grad", id_grad);
		String sql = "SELECT SUM(au.cap) \n" + 
				"FROM `col_aula` au INNER JOIN `col_ciclo` cic ON au.`id_cic`=cic.`id`\n" + 
				"WHERE cic.`id`=:id_cic AND au.`id_grad`=:id_grad";
		Integer capacidadGrado=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("getCapacidadxGrado:" + sql);
		return capacidadGrado;
	}
	
	/**
	 * Capacidad Grado
	 * @param id_cic
	 * @param id_grad
	 * @return
	 */
	public Integer capacidadGradoCicloyModalidad(Integer id_cic, Integer id_grad, Integer id_cme) {
		
		Param param = new Param();
		param.put("id_cic", id_cic);
		param.put("id_grad", id_grad);
		param.put("id_cme", id_cme);
		String sql = "SELECT SUM(au.cap) \n" + 
				"FROM `col_aula` au INNER JOIN `col_ciclo` cic ON au.`id_cic`=cic.`id`\n" + 
				"WHERE cic.`id`=:id_cic AND au.`id_grad`=:id_grad AND au.id_cme=:id_cme";
		Integer capacidadGrado=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("getCapacidadxGrado:" + sql);
		return capacidadGrado;
	}
	
	/*public Integer CapacidadAula(Integer id_au) {
		Param param = new Param();
		param.put("id_au", id_au);
		String sql = "SELECT cap cantidad FROM `col_aula` WHERE id=:id_au;";
		sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("getCapacidadxAula:" + sql);
		return cantidad;
		
	} */
	
	/*Devuelve los matriculados del a�o anterior y grado anterior*/
	/**
	 * Cantidad de matriculados po a�o, grado y local que no esten trasladados, fallecido, retirados o con recupercion pedagogica
	 * @param id_anio
	 * @param id_gra
	 * @param id_suc
	 * @return
	 */
	public Integer getTotalMatriculadosGrado(Integer id_anio, Integer id_gra, Integer id_suc) {
		Integer id_anio_act=id_anio+1;
		Param param = new Param();
		param.put("id_anio", id_anio);
		param.put("id_gra", id_gra);
		param.put("id_suc", id_suc);
		String sql = "SELECT COUNT(mat.`id`) matriculados FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` INNER JOIN col_ciclo cic ON au.id_cic=cic.id INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id`"
				+ " WHERE per.`id_anio`=:id_anio AND gra.`id`=:id_gra AND per.id_suc=:id_suc AND per.id_tpe=1"
				+ " AND (mat.id_sit NOT IN ("+EnumSituacionFinal.TRASLADADO.getValue()+","+EnumSituacionFinal.RETIRADO.getValue()+","+EnumSituacionFinal.FALLECIDO.getValue()+","+EnumSituacionFinal.DESAPROBADO.getValue()+") OR id_sit IS NULL)"
				+ " AND mat.id NOT IN (SELECT id_mat FROM mat_seccion_sugerida sug WHERE sug.id_mat=mat.id)"
				+ " AND mat.id_alu NOT IN (SELECT id_alu FROM mat_matricula mat_act INNER JOIN per_periodo peri ON mat_act.id_per=peri.id WHERE peri.id_tpe='1' AND peri.id_anio="+id_anio_act+")";
		Integer matriculados=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("matriculados:" + sql);
		return matriculados;
	}
	
	public Integer getTotalNoRatificaronxGrado(Integer id_anio_ant, Integer id_anio_act, Integer id_gra, Integer id_suc) {
		Param param = new Param();
		param.put("id_anio_ant", id_anio_ant);
		param.put("id_anio_act", id_anio_act);
		param.put("id_gra", id_gra);
		param.put("id_suc", id_suc);
		String sql = "SELECT COUNT(rat.id) desistieron FROM `mat_ratificacion` rat INNER JOIN `mat_matricula` mat ON rat.`id_mat`=mat.`id`\n" + 
				"INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.id\n" + 
				"INNER JOIN cat_grad gra ON au.id_grad=gra.id \n" + 
				"INNER JOIN col_ciclo cic ON au.id_cic=cic.id \n" + 
				"INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id`\n" + 
				"WHERE rat.`res`=0 AND rat.`id_anio_rat`=:id_anio_act  AND per.`id_anio`=:id_anio_ant AND gra.`id`=:id_gra AND per.id_suc=:id_suc AND per.id_tpe=1"+
				" AND (mat.id_sit NOT IN ("+EnumSituacionFinal.TRASLADADO.getValue()+","+EnumSituacionFinal.RETIRADO.getValue()+","+EnumSituacionFinal.FALLECIDO.getValue()+","+EnumSituacionFinal.DESAPROBADO.getValue()+") OR id_sit IS NULL)"+
				" AND mat.id NOT IN (SELECT id_mat FROM mat_seccion_sugerida sug WHERE sug.id_mat=mat.id)";
		Integer matriculados=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("desistieron:" + sql);
		return matriculados;
	}
	
	
	public Integer getTotalMatriculasAnteriosNoMatriculados(Integer id_anio, Integer id_gra, Integer id_suc, Integer id_anio_act) {
		Param param = new Param();
		param.put("id_anio", id_anio);
		param.put("id_gra", id_gra);
		param.put("id_suc", id_suc);
		param.put("id_anio_act", id_anio_act);
		String sql = "SELECT COUNT(mat.`id`) matriculados FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` INNER JOIN col_ciclo cic ON au.id_cic=cic.id INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id`"
				+ " WHERE per.`id_anio`=:id_anio AND gra.`id`=:id_gra AND per.id_suc=:id_suc AND per.id_tpe=1"
				+ " AND (mat.id_sit NOT IN ("+EnumSituacionFinal.TRASLADADO.getValue()+","+EnumSituacionFinal.RETIRADO.getValue()+","+EnumSituacionFinal.FALLECIDO.getValue()+","+EnumSituacionFinal.DESAPROBADO.getValue()+") OR id_sit IS NULL) "
				+ " AND mat.id_alu NOT IN (SELECT id_alu FROM mat_matricula mat_act INNER JOIN per_periodo peri ON mat_act.id_per=peri.id WHERE peri.id_tpe='1' AND peri.id_anio=:id_anio_act)"
				+ " AND mat.id NOT IN (SELECT id_mat FROM mat_seccion_sugerida sug WHERE sug.id_mat=mat.id) ";
		Integer matriculados=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("matriculados:" + sql);
		return matriculados;
	}
	
	public Integer getTotalMatriculasAnteriosNoMatriculadosNosugeridos(Integer id_anio, Integer id_gra, Integer id_suc, Integer id_anio_act) {
		Param param = new Param();
		param.put("id_anio", id_anio);
		param.put("id_gra", id_gra);
		param.put("id_suc", id_suc);
		param.put("id_anio_act", id_anio_act);
		String sql = "SELECT COUNT(mat.`id`) matriculados FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` INNER JOIN col_ciclo cic ON au.id_cic=cic.id INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id`"
				+ " WHERE per.`id_anio`=:id_anio AND gra.`id`=:id_gra AND per.id_suc=:id_suc AND per.id_tpe=1"
				+ " AND (mat.id_sit NOT IN ("+EnumSituacionFinal.TRASLADADO.getValue()+","+EnumSituacionFinal.RETIRADO.getValue()+","+EnumSituacionFinal.FALLECIDO.getValue()+","+EnumSituacionFinal.DESAPROBADO.getValue()+") OR id_sit IS NULL) "
				+ " AND mat.id_alu NOT IN (SELECT id_alu FROM mat_matricula mat_act INNER JOIN per_periodo peri ON mat_act.id_per=peri.id WHERE peri.id_tpe='1' AND peri.id_anio=:id_anio_act)"
		        + " AND mat.id NOT IN (SELECT id_mat FROM mat_seccion_sugerida mat_sug INNER JOIN mat_matricula mat ON mat_sug.id_mat=mat.id )";
		Integer matriculados=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("matriculados:" + sql);
		return matriculados;
	}
	
	/*Devuelve los matriculados del a�o anterior y grado anterior*/
	/**
	 * Cantidad de matriculados po a�o, grado y local que no esten trasladados, fallecido, retirados o con recupercion pedagogica
	 * @param id_anio
	 * @param id_gra
	 * @param id_suc
	 * @return
	 */
	public Integer getTotalMatriculadosGradoyModalidad(Integer id_anio, Integer id_gra, Integer id_suc, Integer id_cme) {
		Param param = new Param();
		param.put("id_anio", id_anio);
		param.put("id_gra", id_gra);
		param.put("id_suc", id_suc);
		param.put("id_cme", id_cme);
		String sql = "SELECT COUNT(mat.`id`) matriculados FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` INNER JOIN col_ciclo cic ON au.id_cic=cic.id INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id`"
				+ " WHERE per.`id_anio`=:id_anio AND gra.`id`=:id_gra AND per.id_suc=:id_suc AND au.id_cme=:id_cme AND per.id_tpe=1"
				+ " AND (mat.id_sit NOT IN ("+EnumSituacionFinal.TRASLADADO.getValue()+","+EnumSituacionFinal.RETIRADO.getValue()+","+EnumSituacionFinal.FALLECIDO.getValue()+","+EnumSituacionFinal.DESAPROBADO.getValue()+") OR id_sit IS NULL)";
		Integer matriculados=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("matriculados:" + sql);
		return matriculados;
	}
	
	public Integer getTotalMatriculadosGradoCiclo(Integer id_cic, Integer id_gra) {
		Param param = new Param();
		param.put("id_cic", id_cic);
		param.put("id_gra", id_gra);

		String sql = "SELECT COUNT(mat.`id`) matriculados\n" + 
				" FROM `mat_matricula` mat INNER JOIN `col_ciclo` cic ON mat.`id_cic`=cic.`id` \n" + 
				" WHERE mat.`id_cic`=:id_cic AND mat.`id_gra`=:id_gra AND mat.`est`='A' "
				+ " AND (mat.id_sit NOT IN ("+EnumSituacionFinal.TRASLADADO.getValue()+","+EnumSituacionFinal.RETIRADO.getValue()+","+EnumSituacionFinal.FALLECIDO.getValue()+","+EnumSituacionFinal.DESAPROBADO.getValue()+") OR id_sit IS NULL)";
		Integer matriculados=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("matriculados:" + sql);
		return matriculados;
	}
	
	/**
	 * Total de desaprobados
	 * @param id_anio
	 * @param id_gra
	 * @param id_suc
	 * @return
	 */
	public Integer getTotalDesaprobados(Integer id_anio, Integer id_gra, Integer id_suc){
		Param param = new Param();
		param.put("id_anio", id_anio);
		param.put("id_gra", id_gra);
		param.put("id_suc", id_suc);
		String sql = "SELECT COUNT(mat.`id`) matriculados FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+ " WHERE per.`id_anio`=:id_anio AND gra.`id`=:id_gra AND per.id_suc=:id_suc"
				+ " AND (mat.`id_sit`='"+EnumSituacionFinal.DESAPROBADO.getValue()+"')";
		Integer desaprobados=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("desaprobados:" + sql);
		return desaprobados;
	}
	
	/**
	 * Total de desaprobados por aula
	 * @param id_au
	 * @return
	 */
	public Integer getTotalDesaprobadosxAula(Integer id_au){
		Param param = new Param();
		param.put("id_au", id_au);
		String sql = "SELECT COUNT(mat.`id`) matriculados FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+ " WHERE mat.id_au_asi=:id_au AND mat.`id_sit`='"+EnumSituacionFinal.DESAPROBADO.getValue()+"'";
		Integer desaprobados=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("desaprobados:" + sql);
		return desaprobados;
	}
	
	/**
	 * Desaprobados por grado, seccion y anio
	 * @param id_grad
	 * @param secc
	 * @param id_anio
	 * @return
	 */
	public Integer getTotalDesaprobadosxGradoSecc(Integer id_grad, String secc, Integer id_anio){
		Param param = new Param();
		param.put("id_grad", id_grad);
		param.put("secc", secc);
		param.put("id_anio", id_anio);
		String sql = "select count(*) desaprobados from mat_matricula mat inner join col_aula au on mat.id_au_asi=au.id"
				+ " inner join per_periodo per on au.id_per=per.id"
				+ " where au.id_grad=:id_grad and au.secc=:secc and per.id_anio=:id_anio AND mat.`id_sit`='"+EnumSituacionFinal.DESAPROBADO.getValue()+"'";
		Integer desaprobados=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("desaprobados:" + sql);
		return desaprobados;
	}
	
	/**
	 * Desaprobados por grado, seccion y anio
	 * @param id_grad
	 * @param secc
	 * @param id_anio
	 * @return
	 */
	public Integer getTotalDesaprobadosxGrado(Integer id_grad , Integer id_anio){
		Param param = new Param();
		param.put("id_grad", id_grad);
		param.put("id_anio", id_anio);
		String sql = "select count(*) desaprobados from mat_matricula mat inner join col_aula au on mat.id_au_asi=au.id"
				+ " inner join per_periodo per on au.id_per=per.id"
				+ " where au.id_grad=:id_grad and per.id_anio=:id_anio AND mat.`id_sit`='"+EnumSituacionFinal.DESAPROBADO.getValue()+"'";
		Integer desaprobados=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("desaprobados:" + sql);
		return desaprobados;
	}
	/**
	 * Cantidad de Matriculados por aula
	 * @param id_au
	 * @return
	 */
	public Integer getTotalMatriculadosAula(Integer id_au){
		Param param = new Param();
		param.put("id_au", id_au);
		String sql = "SELECT COUNT(*) cantidad FROM `mat_matricula` WHERE id_au_asi=:id_au AND (id_sit not in ("+EnumSituacionFinal.TRASLADADO.getValue()+","+EnumSituacionFinal.RETIRADO.getValue()+","+EnumSituacionFinal.FALLECIDO.getValue()+","+EnumSituacionFinal.DESAPROBADO.getValue()+") OR id_sit IS NULL)";
		Integer matriculados=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("matriculados:" + sql);
		return matriculados;
	}
	/**
	 * Reservas para el periodo escolar que aun no tienen matricula y que su fecla limite sea vigente
	 * @param id_per periodo escolar q tiene a�o, local y nivel
	 * @param id_gra
	 * @return
	 */
	public Integer getReservasGrado(Integer id_per, Integer id_gra){
		Periodo periodo = periodoDAO.get(id_per);
		Integer id_anio=periodo.getId_anio();
		Param param = new Param();
		param.put("id_per", id_per);
		param.put("id_gra", id_gra);
		String sql = "select count(r.id ) total from mat_reserva r where r.id_per=:id_per and r.id_gra=:id_gra and curdate()<=r.fec_lim and not exists( "
				+ " select m.id_alu from mat_matricula m inner join col_aula au on m.id_au_asi=au.id inner join col_ciclo cic on au.id_cic=cic.id inner join per_periodo per on cic.id_per=per.id where m.id_alu = r.id_alu and per.id_tpe=1 and per.id_anio="+id_anio+" and m.id_gra=:id_gra)";	
		Integer reservas=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("reservas:" + sql);
		return reservas;
	}
	
	public Integer getReservasGradoxLocal(Integer id_anio, Integer id_gra, Integer id_suc){
		//Periodo periodo = periodoDAO.get(id_per);
		//Integer id_anio=periodo.getId_anio();
		Param param = new Param();
		param.put("id_anio", id_anio);
		param.put("id_gra", id_gra);
		param.put("id_suc", id_suc);
		String sql = "select count(r.id ) total from mat_reserva r inner join per_periodo peri ON r.id_per=peri.id where r.id_gra=:id_gra AND peri.id_anio=:id_anio AND peri.id_suc=:id_suc and curdate()<=r.fec_lim and not exists( "
				+ " select m.id_alu from mat_matricula m inner join col_aula au on m.id_au_asi=au.id inner join col_ciclo cic on au.id_cic=cic.id inner join per_periodo per on cic.id_per=per.id where m.id_alu = r.id_alu and per.id_tpe=1 and per.id_anio="+id_anio+" and m.id_gra=:id_gra)";	
		Integer reservas=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("reservas:" + sql);
		return reservas;
	}
	
	/**
	 * Reservas para el periodo escolar que aun no tienen matricula y que su fecla limite sea vigente
	 * @param id_per periodo escolar q tiene a�o, local y nivel
	 * @param id_gra
	 * @return
	 */
	public Integer getReservasGradoyModalidad(Integer id_per, Integer id_gra, Integer id_cme){
		Periodo periodo = periodoDAO.get(id_per);
		Integer id_anio=periodo.getId_anio();
		Param param = new Param();
		param.put("id_per", id_per);
		param.put("id_gra", id_gra);
		param.put("id_cme", id_cme);
		String sql = "select count(r.id ) total from mat_reserva r where r.id_per=:id_per and r.id_gra=:id_gra and curdate()<=r.fec_lim and not exists( "
				+ " select m.id_alu from mat_matricula m inner join col_aula au on m.id_au_asi=au.id inner join col_ciclo cic on au.id_cic=cic.id inner join per_periodo per on cic.id_per=per.id where m.id_alu = r.id_alu and per.id_tpe=1 and per.id_anio="+id_anio+" and m.id_gra=:id_gra and au.id_cme=:id_cme )";	
		Integer reservas=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("reservas:" + sql);
		return reservas;
	}
	
	/**
	 * Reservas por aula
	 * @param id_au
	 * @return
	 */
	public Integer getReservasAula(Integer id_au, Integer id_anio){
		Param param = new Param();
		param.put("id_au", id_au);
		param.put("id_anio", id_anio);
		String sql = "select count(r.id) total from mat_reserva r where r.id_au=:id_au and curdate()<=r.fec_lim and not exists( "
				+ " SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au_asi=au.id INNER JOIN per_periodo per ON au.id_per=per.id WHERE m.id_alu = r.id_alu AND per.id_anio=:id_anio and per.id_tpe=1)";	
		Integer reservas=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("reservas:" + sql);
		return reservas;//
	}
	
	public boolean tieneReservasxAula(Integer id_au,Integer id_alu){
		Param param = new Param();
		param.put("id_au", id_au);
		param.put("id_alu", id_alu);

		String sql = "select count(r.id) total from mat_reserva r where r.id_au=:id_au and curdate()<=r.fec_lim and not exists( "
				+ " select m.id_alu from mat_matricula m where m.id_alu = r.id_alu and m.id_au_asi=:id_au) and id_alu=:id_alu";
		Integer tieneReserva=sqlUtil.queryForObject(sql, param, Integer.class);
		
		if (tieneReserva.intValue()>0)
			return true;
		else
			return false;
		
	}
	
	public boolean tieneReservasxGrado(Integer id_grad,Integer id_anio, Integer id_alu){
		Param param = new Param();
		param.put("id_gra", id_grad);
		param.put("id_anio", id_anio);
		param.put("id_alu", id_alu);
		
		String sql = "select count(r.id) total from mat_reserva r INNER JOIN per_periodo per ON r.id_per=per.id where r.id_gra=:id_gra and per.id_anio=:id_anio and per.id_tpe=1 and curdate()<=r.fec_lim and not exists( "
				+ " select m.id_alu from mat_matricula m inner join per_periodo p on m.id_per=p.id where m.id_alu = r.id_alu and m.id_gra=:id_gra and p.id_anio=:id_anio and p.id_tpe=1) and id_alu=:id_alu";
		Integer tieneReserva=sqlUtil.queryForObject(sql, param, Integer.class);
		
		if (tieneReserva.intValue()>0)
			return true;
		else
			return false;
		
	}


	public Integer getMatriculadosNoSug(Integer id_au){
		Param param = new Param();
		param.put("id_au", id_au);
		
		StringBuilder sql = new StringBuilder("SELECT COUNT(1) cantidad FROM mat_matricula mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`");
					  sql.append("\n WHERE mat.`id_au`=:id_au AND mat.`id_alu` NOT IN (SELECT alu.id FROM `mat_seccion_sugerida` sug INNER JOIN `mat_matricula` mat ");
					  sql.append("\n ON sug.`id_mat`=mat.`id` INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id` WHERE sug.`id_au_nue`=:id_au)"); 
		Integer matriculados_no_sugeridos=sqlUtil.queryForObject(sql.toString(), param, Integer.class);
		logger.debug("reservas:" + sql);
		return matriculados_no_sugeridos;
	}
	
	public Integer getMatriculadosNoSugxGrado(Integer id_grad, Integer id_anio, Integer id_suc){
		Param param = new Param();
		param.put("id_gra", id_grad);
		param.put("id_anio", id_anio);
		param.put("id_suc", id_suc);
		
		StringBuilder sql = new StringBuilder("SELECT COUNT(1) cantidad FROM mat_matricula mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id` INNER JOIN  per_periodo peri ON mat.id_per=peri.id ");
					  sql.append("\n WHERE mat.id_gra=:id_gra AND peri.id_anio=:id_anio AND per.id_suc=:id_suc AND mat.`id_alu` NOT IN (SELECT alu.id FROM `mat_seccion_sugerida` sug INNER JOIN `mat_matricula` mat ");
					  sql.append("\n ON sug.`id_mat`=mat.`id` INNER JOIN col_aula asug ON sug.id_au_nue=asug.id INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id` INNER JOIN per_periodo perio ON asug.id_per=perio.id WHERE asug.`id_grad`=:id_gra AND perio.id_anio=:id_anio )"); 
		Integer matriculados_no_sugeridos=sqlUtil.queryForObject(sql.toString(), param, Integer.class);
		logger.debug("reservas:" + sql);
		return matriculados_no_sugeridos;
	}

	/**
	 * Alumnos que obtuvieron vacante por Grado y estan dentro de la fecha de vigencia	
	 * @param anio
	 * @param id_grad
	 * @param suc
	 * @return
	 */
	public Integer alumnosObtieneVac(Integer anio, Integer id_grad, Integer id_suc) {
		Param param = new Param();
		param.put("id_gra", id_grad);
		param.put("anio", anio);
		param.put("id_suc", id_suc);
		String sql = "SELECT COUNT(*) AS 'alumnos_vacante' FROM `eva_matr_vacante` m, `eva_evaluacion_vac` eva,"
				+ " `per_periodo` p, `col_anio` a, `ges_sucursal` suc, `ges_servicio` ser"
				+ " WHERE m.`id_eva`=eva.id AND eva.`id_per`=p.`id` AND p.`id_anio`=a.id AND p.`id_srv`=ser.`id` AND CURDATE()<=eva.fec_vig_vac"
				+ " AND ser.`id_suc`=suc.id AND m.res='A' AND m.`id_gra`=:id_gra AND a.`nom`=:anio AND suc.`id`=:id_suc";
		Integer alumnosObtieneVac=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("Alumnos Obtiene Vacante:" + sql);
		return alumnosObtieneVac;
	} 
	
	/**
	 * Matriculas Vacante que no tiene pierda vacante y este en la fecha limite
	 * @param id_eva
	 * @param id_gra
	 * @param id_per
	 * @return
	 */
	/*public Integer getMatriculasVacante(int id_eva,int id_gra, int id_per) {
		Param param = new Param();
		param.put("id_eva", id_eva);
		param.put("id_gra", id_gra);
		param.put("id_per", id_per);
		String sql = "SELECT COUNT(eva_mat.`id`) matriculados FROM `eva_matr_vacante` eva_mat INNER JOIN `eva_evaluacion_vac` eva ON `eva_mat`.`id_eva`=eva.`id` "
				+ " WHERE eva.`id`="+id_eva+" AND eva_mat.`id_gra`=:id_gra"
				+ " AND eva_mat.res<>'N' AND CURDATE()<=eva.fec_vig_vac"
				+ " AND `eva_mat`.`id_alu` NOT IN (SELECT res.id_alu FROM `mat_reserva` res WHERE res.id_alu AND res.`id_per`=:id_per)";
		Integer matriculasVacante=sqlUtil.queryForObject(sql, param, Integer.class);
		logger.debug("Alumnos Obtiene Vacante:" + sql);
		return matriculasVacante;
	}*/
	public Integer getMatriculasVacante(int id_eva,int id_gra, int id_anio) {
		Param param = new Param();
		param.put("id_eva", id_eva);
		param.put("id_gra", id_gra);
		param.put("id_anio", id_anio);
		String sql = "SELECT COUNT(eva_mat.`id`) matriculados FROM `eva_matr_vacante` eva_mat INNER JOIN `eva_evaluacion_vac` eva ON `eva_mat`.`id_eva`=eva.`id` "
				+ " WHERE eva.`id`="+id_eva+" AND eva_mat.`id_gra`=:id_gra"
				+ " AND (eva_mat.res<>'N' OR eva_mat.`res` IS NULL) AND CURDATE()<=eva.fec_vig_vac"
				+ " AND `eva_mat`.`id_alu` NOT IN (SELECT res.id_alu FROM `mat_reserva` res INNER JOIN per_periodo per ON res.id_per=per.id AND per.id_anio=:id_anio)";
		Integer matriculasVacante=sqlUtil.queryForObject(sql, param, Integer.class);
		return matriculasVacante;
	}
	
	public Integer getMatriculasVacantexLocal(Integer id_suc,Integer id_gra, Integer id_anio) {
		Param param = new Param();
		param.put("id_suc", id_suc);
		param.put("id_gra", id_gra);
		param.put("id_anio", id_anio);
		String sql = "SELECT COUNT(eva_mat.`id`) matriculados FROM `eva_matr_vacante` eva_mat INNER JOIN `eva_evaluacion_vac` eva ON `eva_mat`.`id_eva`=eva.`id` "
				+ " INNER JOIN per_periodo per ON eva.id_per=per.id "
				+ " WHERE per.`id_suc`=:id_suc AND eva_mat.`id_gra`=:id_gra"
				+ " AND (eva_mat.res<>'N' OR eva_mat.`res` IS NULL) AND CURDATE()<=eva.fec_vig_vac"
				+ " AND `eva_mat`.`id_alu` NOT IN (SELECT res.id_alu FROM `mat_reserva` res INNER JOIN per_periodo per ON res.id_per=per.id AND per.id_anio=:id_anio)";
		Integer matriculasVacante=sqlUtil.queryForObject(sql, param, Integer.class);
		return matriculasVacante;
	}
	/**
	 * Vacantes Ocupadas por grado y evaluacion
	 * @param id_grad
	 * @param id_eva
	 * @return
	 */
	public Integer vacOcupadas(Integer id_grad, Integer id_eva) {
		Param param = new Param();
		param.put("id_gra", id_grad);
		param.put("id_eva", id_eva);
		String sql = "SELECT COUNT(*) cantidad FROM `eva_matr_vacante` eva WHERE id_gra="+id_grad+" AND id_eva="+id_eva;
		Integer vac_ocu = sqlUtil.queryForObject(sql, param, Integer.class);
		logger.info("Vacantes Ocupadas:"+sql);
		return vac_ocu;
	} 
		
	
	/**
	 * Sugeridos del a�o X que no esten matriculados y que ademas no est�n reservados
	 * 
	 * @param id_au
	 * @param id_anio
	 * @return
	 */
	public Integer sugeridos(Integer id_au, Integer id_anio) {
		Param param = new Param();
		param.put("id_au", id_au);
		param.put("id_anio", id_anio);
		
		//SUGERIDOS
		StringBuilder sql = new StringBuilder(" SELECT count(1)");
		sql.append("\n FROM `mat_seccion_sugerida` sug INNER JOIN `mat_matricula` mat ON sug.`id_mat`=mat.`id`");
		sql.append("\n INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`");
		sql.append("\n WHERE sug.`id_au_nue`=:id_au AND `id_anio`=:id_anio and mat.id_sit<>5");
		sql.append("\n AND ( mat.id_alu NOT IN (SELECT mat.`id_alu` FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.`id_au`=au.`id` INNER JOIN `per_periodo` per ON au.`id_per`=per.`id` WHERE per.`id_anio`=:id_anio and per.id_tpe=1))");
		sql.append("\n AND ");
		sql.append("\n  (mat.`id_alu` NOT IN (SELECT res.id_alu FROM `mat_reserva` res INNER JOIN `col_aula` au ON res.`id_au`=au.`id` INNER JOIN `per_periodo` per ON au.`id_per`=per.`id` WHERE per.`id_anio`=:id_anio))");
		
		Integer sugeridos = sqlUtil.queryForObject(sql.toString(),param, Integer.class);
		logger.info("Sugeridos:"+sql);

		return sugeridos;
	} 
	
	public Integer sugeridosGrado(Integer id_gra, Integer id_anio, Integer id_suc) {
		Param param = new Param();
		param.put("id_gra", id_gra);
		param.put("id_anio", id_anio);
		param.put("id_suc", id_suc);
		//SUGERIDOS
		StringBuilder sql = new StringBuilder(" SELECT count(1)");
		sql.append("\n FROM `mat_seccion_sugerida` sug INNER JOIN `mat_matricula` mat ON sug.`id_mat`=mat.`id`");
		sql.append("\n INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`");
		//sql.append("\n INNER JOIN `col_aula` asug ON sug.id_au_nue=asug.id");
		//sql.append("\n INNER JOIN `per_periodo` peri ON asug.id_per=peri.id");
		//sql.append("\n WHERE asug.`id_grad`=:id_gra AND peri.id_suc=:id_suc AND peri.`id_anio`=:id_anio and mat.id_sit<>5");
		sql.append("\n WHERE sug.`id_gra_nue`=:id_gra AND sug.id_suc_nue=:id_suc AND sug.`id_anio`=:id_anio and (mat.id_sit NOT IN (5,4) OR mat.id_sit IS NULL)");
		sql.append("\n AND ( mat.id_alu NOT IN (SELECT mat.`id_alu` FROM `mat_matricula` mat INNER JOIN `per_periodo` per ON mat.`id_per`=per.`id` WHERE per.`id_anio`=:id_anio AND per.id_tpe=1 ))");
		sql.append("\n AND ");
		sql.append("\n  (mat.`id_alu` NOT IN (SELECT res.id_alu FROM `mat_reserva` res INNER JOIN `col_aula` au ON res.`id_au`=au.`id` INNER JOIN col_ciclo cic ON au.id_cic=cic.id  INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id` WHERE per.`id_anio`=:id_anio))");
		
		Integer sugeridos = sqlUtil.queryForObject(sql.toString(),param, Integer.class);
		logger.info("Sugeridos:"+sql);

		return sugeridos;
	} 
	/*	public List<Map<String,Object>> Vacantes() {
		
			String sql = "select v.id, g.nom grado, ser.nom nivel, suc.nom local, v.nro_vac, v.vac_ofe,v.post,a.nom anio from eva_vacante v, per_periodo p,"
					+ " cat_grad g, ges_sucursal suc, ges_servicio ser, col_anio a where v.id_per=p.id and p.id_srv=ser.id"
					+ " and ser.id_suc=suc.id and v.id_grad=g.id and p.id_anio=a.id";
			List<Map<String,Object>> Vacantes = jdbcTemplate.queryForList(sql);	
			logger.info(sql);
			return Vacantes;
		} */
		
		/*Listar evaluaciones por periodo*/
		public List<Map<String,Object>> EvaluacionVacLista(Integer id_per) {
			String q_aux="";
			if (id_per!= null){
			 q_aux="AND e.`id_per`="+id_per+"";}
			String sql = "SELECT e.id, e.id_per,concat(e.des,'-',ser.nom) value FROM eva_evaluacion_vac e, per_periodo per,"
					+ " ges_servicio ser, ges_sucursal suc WHERE e.id_per=per.id AND per.id_srv=ser.id AND ser.id_suc=suc.id"
					+ " AND e.est='A'"+q_aux;
			List<Map<String,Object>> evaLista = jdbcTemplate.queryForList(sql);	
 			return evaLista;
		}
		
		public List<Map<String,Object>> listarGrados(Integer id_eva, Integer id_niv) {
			
			String sql = "SELECT v.id id,g.nom, g.id id_grad,v.id_eva,v.`id_per`,v.`nro_vac`, v.`vac_ofe`, v.`post`"
					+ " FROM `cat_grad` g LEFT JOIN `eva_vacante` v ON g.id=v.`id_grad` AND( v.`id_eva`=?) "
					+ " WHERE g.`id_nvl`=?";
			List<Map<String,Object>> listGrados = jdbcTemplate.queryForList(sql,new Object[] { id_eva,id_niv});	
			logger.info(sql);
			return listGrados;
		} 
		
		
}
