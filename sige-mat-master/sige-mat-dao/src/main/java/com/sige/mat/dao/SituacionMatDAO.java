package com.sige.mat.dao;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.common.enums.EnumAreaSIAGE;
import com.sige.common.enums.EnumSituacionFinal;
import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.SituacionMatDAOImpl;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Nota;
import com.tesla.colegio.model.RecuperacionArea;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad situacion_mat.
 * @author MV
 *
 */
@Repository
public class SituacionMatDAO extends SituacionMatDAOImpl{
	final static Logger logger = Logger.getLogger(SituacionMatDAO.class);

	@Autowired
	private SQLUtil sqlUtil;
	
	@Autowired
	private MatriculaDAO matriculaDAO;
	
	@Autowired
	private RecuperacionAreaDAO recuperacionAreaDAO;
	
	
	public void actualizaPago(Integer id_mat, Integer mens, BigDecimal monto) {
		String sql = "update fac_academico_pago set monto=? where id_mat=? and mens=?";
		logger.info(sql);
		sqlUtil.update(sql, new Object[] { monto, id_mat, mens});
	}
	
	public void deshabilitaPago(Integer id_mat, Integer mens) {
		String sql = "update fac_academico_pago set est='I' where id_mat=? and mens=?";
		logger.info(sql);
		sqlUtil.update(sql, new Object[] { id_mat, mens});
	}
	
	public void habilitaPago(Integer id_mat, Integer mens, BigDecimal monto) {
		String sql = "update fac_academico_pago set est='A', set monto=? where id_mat=? and mens=?";
		logger.info(sql);
		sqlUtil.update(sql, new Object[] { id_mat, mens, monto});
	}
	
	public List<Row> obtenerMonto(Integer id_mat) {
		String sql = "SELECT cu.cuota FROM mat_matricula mat INNER JOIN col_aula au ON mat.id_au=au.id"
				+ " INNER JOIN mat_conf_cuota cu ON au.id_per=cu.id_per AND cu.est='A' AND mat.id="+id_mat;

		return sqlUtil.query(sql);
	}
	

	/**
	 * ACtualizar situacion final por id_per ( por nivel)
	 * 
	 * @param id_per
	 */
	public Integer actualizarSituacionFinal(Integer id_per, Integer id_sit) {
		
		String sql = "update mat_matricula " + " set id_sit=:sit_prom, " + " usr_act = :usr_act, fec_act=curdate()"
				+ " where id_au in (select id from col_aula where id_per=:id_per) "
				+ " and ifnull(id_sit,0) not in (:sit_fall,:sit_ret,:sit_tras)";

		Param param = new Param();
		param.put("sit_prom", id_sit);
		param.put("usr_act", -2);
		param.put("id_per", id_per);
		param.put("sit_fall", EnumSituacionFinal.FALLECIDO.getValue());
		param.put("sit_ret", EnumSituacionFinal.RETIRADO.getValue());
		param.put("sit_tras", EnumSituacionFinal.TRASLADADO.getValue());

		int actualizados = sqlUtil.update(sql, param);
		
		return actualizados;
	}

	/**
	 * ACtualizar situacion final por id_per ( por nivel)
	 * 
	 * @param id_per
	 */
	public Integer actualizarSituacionFinal(Integer id_per, Integer[] id_grad,Integer id_sit) {
		String sql = "update mat_matricula " 
				+ " set id_sit=:sit_prom, " 
				+ " usr_act = :usr_act, fec_act=curdate()"
				+ " where id_au_asi in (select au.id from col_aula au where au.id_per=:id_per and au.id_grad in (:id_grad)) "
				+ " and ifnull(id_sit,0) not in (4,5,6) ";

		Param param = new Param();
		param.put("sit_prom", id_sit);
		param.put("id_grad", Arrays.asList(id_grad));
		param.put("usr_act", 1);
		param.put("id_per", id_per);
		/*
		param.put("sit_fall", EnumSituacionFinal.FALLECIDO.getValue());
		param.put("sit_ret", EnumSituacionFinal.RETIRADO.getValue());
		param.put("sit_tras", EnumSituacionFinal.TRASLADADO.getValue());
*/
		logger.debug(sql);
		int nro_actualizados= sqlUtil.update(sql, param);
		logger.debug("nro_actualizados" + nro_actualizados);

		return nro_actualizados;
	}

	/**
	 * ACtualizar situacion final por id_per ( por nivel)
	 * 
	 * @param id_per
	 */
	public Integer actualizarSituacionFinalNulos(Integer id_per, Integer[] id_grad,Integer id_sit) {
		String sql = "update mat_matricula " + " set id_sit=:sit_prom, " + " usr_act = :usr_act, fec_act=curdate()"
				+ " where id_au_asi in (select id from col_aula where id_per=:id_per and id_grad in (:id_grad)) "
				+ " and ifnull(id_sit,0) not in (4,5,6,3,2) ";

		Param param = new Param();
		param.put("sit_prom", id_sit);
		param.put("id_grad", Arrays.asList(id_grad));
		param.put("usr_act", 1);
		param.put("id_per", id_per);

		logger.debug(sql);
		int nro_actualizados= sqlUtil.update(sql, param);
		logger.debug("nro_actualizados" + nro_actualizados);

		return nro_actualizados;
	}
	
	public Integer limpiarSituacionFinal(Integer id_per, Integer[] id_grad) {
		String sql = "update mat_matricula " + " set id_sit=null, " + " usr_act = :usr_act, fec_act=curdate()"
				+ " where id_au_asi in (select id from col_aula where id_per=:id_per and id_grad in (:id_grad)) "
				+ " and ifnull(id_sit,0) not in (4,5,6) ";

		Param param = new Param();
		param.put("id_grad", Arrays.asList(id_grad));
		param.put("usr_act", 1);
		param.put("id_per", id_per);

		logger.debug(sql);
		int nro_actualizados= sqlUtil.update(sql, param);
		logger.debug("nro_actualizados" + nro_actualizados);

		return nro_actualizados;
	}
	
	/**
	 * C -> NOTA MEJOR IGUAL QUE 10
	 * @param id_per
	 * @param id_grados
	 * @param id_areas
	 * @param id_sit
	 * @param nump
	 * @return
	 */
	public Integer repetir2do4to(Integer id_per, Integer[] id_grados, Integer[] id_areas, Integer id_sit, Integer nump) {

		String sqlPromedio = "update mat_matricula set id_sit=:id_sit, fec_act=curdate(), usr_act=1 "
				+ " where  ifnull(id_sit,0) not in (4,5,6)"
				//+ " and id  in (select  f1.id_mat, count(f1.promedio) promedio  from("
				+ " and id  in (select  f1.id_mat   from("
				+ "\n select  f.id_mat, f.id_area, round(avg(f.nota_com))  promedio  from("
				+ "\n select com.id_mat, com.nump, com.id_area, com.id_cur ,round(cast(sum(com.nota_cap*com.peso) as decimal(10,2))/cast(sum(com.peso) as decimal(10,2)))  nota_com"
				+ "\n from("
				+ "\n select cap.id_mat, cap.nump, cap.id_area, cap.id_cur, cap.id, cap.peso, round(avg(cap.nota_ind)) nota_cap"
				+ "\n from ("
				+ "\n SELECT mat.id id_mat, ne.nump, caa.id_area, cua.id_cur, com.id, com.peso,cap.id cap_id, nni.nota  nota_ind"
				+ "\n FROM col_curso_aula cca "
				+ "\n INNER JOIN  mat_matricula mat  ON mat.id_au_asi=cca.id_au"
				+ "\n INNER JOIN alu_alumno alu ON mat.id_alu=alu.id  "
				+ "\n INNER JOIN col_curso_anio cua ON cca.id_cua=cua.id"
				+ "\n INNER JOIN col_area_anio caa ON caa.`id`=cua.`id_caa`"
				+ "\n INNER JOIN not_evaluacion ne ON  ne.`id_cca`=cca.`id`  "
				+ "\n INNER JOIN not_nota nn ON nn.`id_alu`=alu.`id` AND nn.`id_ne`=ne.`id`"
				+ "\n INNER JOIN not_ind_eva nie ON  nie.`est`='A' AND nie.`id_ne`=ne.`id` "
				+ "\n INNER JOIN not_nota_indicador nni ON nn.`id`=nni.`id_not`  AND nni.`id_nie`=nie.`id` "
				+ "\n INNER JOIN `col_indicador` ind ON nie.`id_ind`=ind.`id`"
				+ "\n INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
				+ "\n INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+ "\n INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ "\n INNER JOIN col_capacidad cap ON cgc.`id_cap`=cap.`id`" 
				+ "\n INNER JOIN col_competencia com ON cap.id_com=com.id "
				+ "\n inner join col_aula au on au.id = cca.id_au "
				+ "\n WHERE au.id_per=:id_per and au.id_grad in (:id_grados)"
				+ "\n AND (ne.nump=:nump)  and caa.id_area in (:id_areas)"
				+ "\n and mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
				+ "\n )cap"
				+ "\n group by cap.id_mat, cap.nump, cap.id_area, cap.id_cur, cap.id, cap.peso "
				+ "\n ) com"
				+ "\n group by com.id_mat , com.nump, com.id_area, com.id_cur"
				+ "\n ) f  group by f.id_mat, f.id_area"
				+ "\n ) f1 where f1.promedio<=10 group by f1.id_mat having count(*)>1)";
		
		Param param = new Param();
		param.put("id_sit", id_sit);
		param.put("id_per", id_per);
		param.put("nump", nump);
		param.put("id_grados", Arrays.asList(id_grados));
		param.put("id_areas", Arrays.asList(id_areas));
		 
		int repetidos= sqlUtil.update(sqlPromedio, param);
		logger.debug("repetidos:" + repetidos);
		return repetidos;
 

	}

	/**
	 * Matematica y Comunicaci�n A, las areas restantes B: Promovido
	 * A -->  NOTA MAYOR QUE 12
	 * @param id_per
	 * @param id_grados
	 * @param id_areas
	 * @param id_sit
	 * @param nump
	 * @return
	 */
	public Integer recuperacion2do4to(Integer id_per, Integer[] id_grados,  Integer id_sit, Integer nump) {

		/*RECUPERACION SI TIENE MATEMATICA O COMUNICACION MEJOR QUE 13*/
		String sqlPromedio = "update mat_matricula set id_sit=:id_sit, fec_act=curdate(), usr_act=1 "
				+ "\n where ifnull(id_sit,0) not in(4,5,6,3)" //SE AGREGO DESAPROBADO
				+ "\n and id  in (select  f1.id_mat"
				+ "\n from ("
				+ "\n select ff.id_mat, ff.id_area, "
				+ "\n case when ff.id_area=7 and ff.promedio<13 then 0 else 1 end as mate,"
				+ "\n case when ff.id_area=14 and ff.promedio<13 then 0 else 1 end as comu"
				+ "\n from (select  f.id_mat, f.id_area, "
				+ "\n round(avg(f.nota_com))  promedio  from("
				+ "\n select com.id_mat, com.nump, com.id_area, com.id_cur ,round(cast(sum(com.nota_cap*com.peso) as decimal(10,2))/cast(sum(com.peso) as decimal(10,2)))  nota_com"
				+ "\n from("
				+ "\n select cap.id_mat, cap.nump, cap.id_area, cap.id_cur, cap.id, cap.peso, round(avg(cap.nota_ind)) nota_cap"
				+ "\n from ("
				+ "\n SELECT mat.id id_mat, ne.nump, caa.id_area, cua.id_cur, com.id, com.peso,cap.id cap_id, nni.nota  nota_ind"
				+ "\n FROM col_curso_aula cca "
				+ "\n INNER JOIN  mat_matricula mat  ON mat.`id_au_asi`=cca.`id_au`"
				+ "\n INNER JOIN alu_alumno alu ON mat.`id_alu`=alu.`id`  "
				+ "\n INNER JOIN col_curso_anio cua ON cca.`id_cua`=cua.`id`"
				+ "\n INNER JOIN col_area_anio caa ON caa.`id`=cua.`id_caa`"
				+ "\n INNER JOIN not_evaluacion ne ON  ne.`id_cca`=cca.`id`  "
				+ "\n INNER JOIN not_nota nn ON nn.`id_alu`=alu.`id` AND nn.`id_ne`=ne.`id`"
				+ "\n INNER JOIN not_ind_eva nie ON  nie.`est`='A' AND nie.`id_ne`=ne.`id` "
				+ "\n INNER JOIN not_nota_indicador nni ON nn.`id`=nni.`id_not`  AND nni.`id_nie`=nie.`id` "
				+ "\n INNER JOIN `col_indicador` ind ON nie.`id_ind`=ind.`id`"
				+ "\n INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
				+ "\n INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+ "\n INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ "\n INNER JOIN col_capacidad cap ON cgc.`id_cap`=cap.`id`" 
				+ "\n INNER JOIN col_competencia com ON cap.id_com=com.id "
				+ "\n inner join col_aula au on au.id = cca.id_au "
				+ "\n WHERE au.id_per=:id_per and au.id_grad in (:id_grados) "
				+ "\n AND (ne.nump=:nump)  "
				+ "\n and mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
				//+ " and mat.id_sit is null"
				+ "\n )cap"
				+ "\n group by cap.id_mat, cap.nump, cap.id_area, cap.id_cur, cap.id, cap.peso "
				+ "\n ) com"
				+ "\n group by com.id_mat , com.nump, com.id_area, com.id_cur"
				+ "\n ) f  group by f.id_mat, f.id_area"
				+ "\n ) ff "
				+ " ) f1 where f1.mate=0 or f1.comu=0 ) ";
		
		Param param = new Param();
		param.put("id_sit", id_sit);
		param.put("id_per", id_per);
		param.put("nump", nump);
		param.put("id_grados", Arrays.asList(id_grados));
 		 
		int actualizados= sqlUtil.update(sqlPromedio, param);
		logger.debug("recuperacion-POR MATE O COMU.:" + actualizados);
		
		/*grabar los cursos jalados*/
		String sqlCursosUpdate = "select  f1.id_mat, f1.mate, f1.comu"
				+ "\n from ("
				+ "\n select ff.id_mat, ff.id_area, "
				+ "\n case when ff.id_area=7 and ff.promedio<13 then 0 else 1 end as mate,"
				+ "\n case when ff.id_area=14 and ff.promedio<13 then 0 else 1 end as comu"
				+ "\n from (select  f.id_mat, f.id_area, "
				+ "\n round(avg(f.nota_com))  promedio  from("
				+ "\n select com.id_mat, com.nump, com.id_area, com.id_cur ,round(cast(sum(com.nota_cap*com.peso) as decimal(10,2))/cast(sum(com.peso) as decimal(10,2)))  nota_com"
				+ "\n from("
				+ "\n select cap.id_mat, cap.nump, cap.id_area, cap.id_cur, cap.id, cap.peso, round(avg(cap.nota_ind)) nota_cap"
				+ "\n from ("
				+ "\n SELECT mat.id id_mat, ne.nump, caa.id_area, cua.id_cur, com.id, com.peso,cap.id cap_id, nni.nota  nota_ind"
				+ "\n FROM col_curso_aula cca "
				+ "\n INNER JOIN  mat_matricula mat  ON mat.`id_au_asi`=cca.`id_au`"
				+ "\n INNER JOIN alu_alumno alu ON mat.`id_alu`=alu.`id`  "
				+ "\n INNER JOIN col_curso_anio cua ON cca.`id_cua`=cua.`id`"
				+ "\n INNER JOIN col_area_anio caa ON caa.`id`=cua.`id_caa`"
				+ "\n INNER JOIN not_evaluacion ne ON  ne.`id_cca`=cca.`id`  "
				+ "\n INNER JOIN not_nota nn ON nn.`id_alu`=alu.`id` AND nn.`id_ne`=ne.`id`"
				+ "\n INNER JOIN not_ind_eva nie ON  nie.`est`='A' AND nie.`id_ne`=ne.`id` "
				+ "\n INNER JOIN not_nota_indicador nni ON nn.`id`=nni.`id_not`  AND nni.`id_nie`=nie.`id` "
				+ "\n INNER JOIN `col_indicador` ind ON nie.`id_ind`=ind.`id`"
				+ "\n INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
				+ "\n INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+ "\n INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ "\n INNER JOIN col_capacidad cap ON cgc.`id_cap`=cap.`id`" 
				+ "\n INNER JOIN col_competencia com ON cap.id_com=com.id "
				+ "\n inner join col_aula au on au.id = cca.id_au "
				+ "\n WHERE au.id_per=:id_per and au.id_grad in (:id_grados) "
				+ "\n AND (ne.nump=:nump)  "
				+ "\n and mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
				+ "\n )cap"
				+ "\n group by cap.id_mat, cap.nump, cap.id_area, cap.id_cur, cap.id, cap.peso "
				+ "\n ) com"
				+ "\n group by com.id_mat , com.nump, com.id_area, com.id_cur"
				+ "\n ) f  group by f.id_mat, f.id_area"
				+ "\n ) ff  "
				+ "\n ) f1 where f1.mate=0 or f1.comu=0" ; 
		/*
		List<Row> listaRecuperacion = sqlUtil.query(sqlCursosUpdate,param);
		for (Row row : listaRecuperacion) {
			Integer id_mat_rec = row.getInteger("id_mat");
			Integer mate = row.getInteger("mate");
			Integer comu = row.getInteger("comu");
			
			if (mate.intValue()==0){
				RecuperacionArea recuperacionArea = new RecuperacionArea();
				recuperacionArea.setId_mat(id_mat_rec);
				recuperacionArea.setId_area(EnumAreaSIAGE.MATEMATICA.getValue());
				recuperacionArea.setEst("A");
				recuperacionAreaDAO.saveOrUpdate(recuperacionArea);
			}

			if (comu.intValue()==0){
				RecuperacionArea recuperacionArea = new RecuperacionArea();
				recuperacionArea.setId_mat(id_mat_rec);
				recuperacionArea.setId_area(EnumAreaSIAGE.COMUNICACION.getValue());
				recuperacionArea.setEst("A");
				recuperacionAreaDAO.saveOrUpdate(recuperacionArea);
			}
			
		}
	 */
		
		/*RECUPERACION SI APROBO MATE Y COM PERO BAJO EN OTROS CURSOS*/
		sqlPromedio = "update mat_matricula set id_sit=:id_sit, fec_act=curdate(), usr_act=1 "
				+ "\n where ifnull(id_sit,0) not in(4,5,6,3)" //SE AGREGO DESAPROBADO
					+ "\n and id  in (select  f1.id_mat"
					+ "\n from ("
					+ "\n select ff.id_mat, ff.id_area, "
					+ "\n case when ff.id_area=7 and ff.promedio<13 then 0 else 1 end as mate,"
					+ "\n case when ff.id_area=14 and ff.promedio<13 then 0 else 1 end as comu"
					+ "\n from (select  f.id_mat, f.id_area, "
					+ "\n round(avg(f.nota_com))  promedio  from("
					+ "\n select com.id_mat, com.nump, com.id_area, com.id_cur ,round(cast(sum(com.nota_cap*com.peso) as decimal(10,2))/cast(sum(com.peso) as decimal(10,2)))  nota_com"
					+ "\n from("
					+ "\n select cap.id_mat, cap.nump, cap.id_area, cap.id_cur, cap.id, cap.peso, round(avg(cap.nota_ind)) nota_cap"
					+ "\n from ("
					+ "\n SELECT mat.id id_mat, ne.nump, caa.id_area, cua.id_cur, com.id, com.peso,cap.id cap_id, nni.nota  nota_ind"
					+ "\n FROM col_curso_aula cca "
					+ "\n INNER JOIN  mat_matricula mat  ON mat.`id_au_asi`=cca.`id_au`"
					+ "\n INNER JOIN alu_alumno alu ON mat.`id_alu`=alu.`id`  "
					+ "\n INNER JOIN col_curso_anio cua ON cca.`id_cua`=cua.`id`"
					+ "\n INNER JOIN col_area_anio caa ON caa.`id`=cua.`id_caa`"
					+ "\n INNER JOIN not_evaluacion ne ON  ne.`id_cca`=cca.`id`  "
					+ "\n INNER JOIN not_nota nn ON nn.`id_alu`=alu.`id` AND nn.`id_ne`=ne.`id`"
					+ "\n INNER JOIN not_ind_eva nie ON  nie.`est`='A' AND nie.`id_ne`=ne.`id` "
					+ "\n INNER JOIN not_nota_indicador nni ON nn.`id`=nni.`id_not`  AND nni.`id_nie`=nie.`id` "
					+ "\n INNER JOIN `col_indicador` ind ON nie.`id_ind`=ind.`id`"
					+ "\n INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
					+ "\n INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
					+ "\n INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
					+ "\n INNER JOIN col_capacidad cap ON cgc.`id_cap`=cap.`id`" 
					+ "\n INNER JOIN col_competencia com ON cap.id_com=com.id "
					+ "\n inner join col_aula au on au.id = cca.id_au "
					+ "\n left join not_curso_exoneracion nce on nce.id_mat = mat.id and nce.id_cur =cua.id_cur"
					+ "\n WHERE au.id_per=:id_per and au.id_grad in (:id_grados) "
					+ "\n AND (ne.nump=:nump)  "
					+ "\n and mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
					+ "\n and ifnull(nce.id,0)=0" // NO TOMA EN CUENTA EXONERADOS
					+ "\n )cap"
					+ "\n group by cap.id_mat, cap.nump, cap.id_area, cap.id_cur, cap.id, cap.peso "
					+ "\n ) com"
					+ "\n group by com.id_mat , com.nump, com.id_area, com.id_cur"
					+ "\n ) f  group by f.id_mat, f.id_area"
					+ "\n ) ff where ff.promedio<11"
					+ "\n ) f1 where f1.mate=1 and f1.comu=1 ) ";
		
		param = new Param();
		param.put("id_sit", id_sit);
		param.put("id_per", id_per);
		param.put("nump", nump);
		param.put("id_grados", Arrays.asList(id_grados));
 		 
		actualizados= sqlUtil.update(sqlPromedio, param);
		logger.debug("recuperacion-POR BAJO OTROS CURSOS.:" + actualizados);
	 
		
		return actualizados;

	}

	/**
	 * Matematica y Comunicaci�n A, las areas restantes B: Promovido
	 * A -->  NOTA MAYOR QUE 12
	 * @param id_per
	 * @param id_grados
	 * @param id_areas
	 * @param id_sit
	 * @param nump
	 * @return
	 */
	public Integer recuperacion5tdo6to(Integer id_per, Integer[] id_grados,  Integer id_sit, Integer nump) {

		String sqlPromedio = "update mat_matricula set id_sit=:id_sit, fec_act=curdate(), usr_act=1 "
				+ " where ifnull(id_sit,0) not in(4,5,6,3)"
				+ " and id  in (select  f1.id_mat"
				+ " from ("
				+ " select ff.id_mat, ff.id_area, "
				+ " case when ff.id_area=" + EnumAreaSIAGE.MATEMATICA.getValue()  + " and ff.promedio<13 then 0 else 1 end as mate,"
				+ " case when ff.id_area=" + EnumAreaSIAGE.COMUNICACION.getValue()  + " and ff.promedio<13 then 0 else 1 end as comu,"
				+ " case when ff.id_area=" + EnumAreaSIAGE.CIENCIA_Y_TECNLOGIA.getValue()  + " and ff.promedio<13 then 0 else 1 end as ciencia,"
				+ " case when ff.id_area=" + EnumAreaSIAGE.PERSONAL_SOCIAL.getValue()  + " and ff.promedio<13 then 0 else 1 end as pers_social "
				+ " from (select  f.id_mat, f.id_area, "
				+ " round(avg(f.nota_com))  promedio  from("
				+ " select com.id_mat, com.nump, com.id_area, com.id_cur ,round(cast(sum(com.nota_cap*com.peso) as decimal(10,2))/cast(sum(com.peso) as decimal(10,2)))  nota_com"
				+ " from("
				+ " select cap.id_mat, cap.nump, cap.id_area, cap.id_cur, cap.id, cap.peso, round(avg(cap.nota_ind)) nota_cap"
				+ " from ("
				+ " SELECT mat.id id_mat, ne.nump, caa.id_area, cua.id_cur, com.id, com.peso,cap.id cap_id, nni.nota  nota_ind"
				+ " FROM col_curso_aula cca "
				+ " INNER JOIN  mat_matricula mat  ON mat.`id_au_asi`=cca.`id_au`"
				+ " INNER JOIN alu_alumno alu ON mat.`id_alu`=alu.`id`  "
				+ " INNER JOIN col_curso_anio cua ON cca.`id_cua`=cua.`id`"
				+ " INNER JOIN col_area_anio caa ON caa.`id`=cua.`id_caa`"
				+ " INNER JOIN not_evaluacion ne ON  ne.`id_cca`=cca.`id`  "
				+ " INNER JOIN not_nota nn ON nn.`id_alu`=alu.`id` AND nn.`id_ne`=ne.`id`"
				+ " INNER JOIN not_ind_eva nie ON  nie.`est`='A' AND nie.`id_ne`=ne.`id` "
				+ " INNER JOIN not_nota_indicador nni ON nn.`id`=nni.`id_not`  AND nni.`id_nie`=nie.`id` "
				+ "\n INNER JOIN `col_indicador` ind ON nie.`id_ind`=ind.`id`"
				+ "\n INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
				+ "\n INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+ "\n INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ "\n INNER JOIN col_capacidad cap ON cgc.`id_cap`=cap.`id`" 
				+ " INNER JOIN col_competencia com ON cap.id_com=com.id "
				+ " inner join col_aula au on au.id = cca.id_au "
				+ " WHERE au.id_per=:id_per and au.id_grad in (:id_grados) "
				+ " AND (ne.nump=:nump)  "
				+ " and mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
				+ " )cap"
				+ " group by cap.id_mat, cap.nump, cap.id_area, cap.id_cur, cap.id, cap.peso "
				+ " ) com"
				+ " group by com.id_mat , com.nump, com.id_area, com.id_cur"
				+ " ) f  group by f.id_mat, f.id_area"
				+ " ) ff "
				+ " ) f1 where f1.mate=0 or f1.comu=0 or f1.ciencia=0 or f1.pers_social=0 ) ";
		
		Param param = new Param();
		param.put("id_sit", id_sit);
		param.put("id_per", id_per);
		param.put("nump", nump);
		param.put("id_grados", Arrays.asList(id_grados));
 		 
		int actualizados= sqlUtil.update(sqlPromedio, param);
		logger.debug("recuperacion_JALAR MATE, COM Y OTROS:" + actualizados);
	 
		sqlPromedio = "update mat_matricula set id_sit=:id_sit, fec_act=curdate(), usr_act=1 "
				+ " where ifnull(id_sit,0) not in(4,5,6,3)"
				+ " and id  in (select  f1.id_mat"
				+ " from ("
				+ " select ff.id_mat, ff.id_area, "
				+ " case when ff.id_area=" + EnumAreaSIAGE.MATEMATICA.getValue()  + " and ff.promedio<13 then 0 else 1 end as mate,"
				+ " case when ff.id_area=" + EnumAreaSIAGE.COMUNICACION.getValue()  + " and ff.promedio<13 then 0 else 1 end as comu,"
				+ " case when ff.id_area=" + EnumAreaSIAGE.CIENCIA_Y_TECNLOGIA.getValue()  + " and ff.promedio<13 then 0 else 1 end as ciencia,"
				+ " case when ff.id_area=" + EnumAreaSIAGE.PERSONAL_SOCIAL.getValue()  + " and ff.promedio<13 then 0 else 1 end as pers_social "
				+ " from (select  f.id_mat, f.id_area, "
				+ " round(avg(f.nota_com))  promedio  from("
				+ " select com.id_mat, com.nump, com.id_area, com.id_cur ,round(cast(sum(com.nota_cap*com.peso) as decimal(10,2))/cast(sum(com.peso) as decimal(10,2)))  nota_com"
				+ " from("
				+ " select cap.id_mat, cap.nump, cap.id_area, cap.id_cur, cap.id, cap.peso, round(avg(cap.nota_ind)) nota_cap"
				+ " from ("
				+ " SELECT mat.id id_mat, ne.nump, caa.id_area, cua.id_cur, com.id, com.peso,cap.id cap_id, nni.nota  nota_ind"
				+ " FROM col_curso_aula cca "
				+ " INNER JOIN  mat_matricula mat  ON mat.`id_au_asi`=cca.`id_au`"
				+ " INNER JOIN alu_alumno alu ON mat.`id_alu`=alu.`id`  "
				+ " INNER JOIN col_curso_anio cua ON cca.`id_cua`=cua.`id`"
				+ " INNER JOIN col_area_anio caa ON caa.`id`=cua.`id_caa`"
				+ " INNER JOIN not_evaluacion ne ON  ne.`id_cca`=cca.`id`  "
				+ " INNER JOIN not_nota nn ON nn.`id_alu`=alu.`id` AND nn.`id_ne`=ne.`id`"
				+ " INNER JOIN not_ind_eva nie ON  nie.`est`='A' AND nie.`id_ne`=ne.`id` "
				+ " INNER JOIN not_nota_indicador nni ON nn.`id`=nni.`id_not`  AND nni.`id_nie`=nie.`id` "
				+ "\n INNER JOIN `col_indicador` ind ON nie.`id_ind`=ind.`id`"
				+ "\n INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
				+ "\n INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+ "\n INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ "\n INNER JOIN col_capacidad cap ON cgc.`id_cap`=cap.`id`" 
				+ " INNER JOIN col_competencia com ON cap.id_com=com.id "
				+ " inner join col_aula au on au.id = cca.id_au "
				+ " left join not_curso_exoneracion nce on nce.id_mat = mat.id and nce.id_cur =cua.id_cur"
				+ " WHERE au.id_per=:id_per and au.id_grad in (:id_grados) "
				+ " AND (ne.nump=:nump)  "
				+ " and mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
				+ " and ifnull(nce.id,0)=0" // NO TOMA EN CUENTA EXONERADOS				
				+ " )cap"
				+ " group by cap.id_mat, cap.nump, cap.id_area, cap.id_cur, cap.id, cap.peso "
				+ " ) com"
				+ " group by com.id_mat , com.nump, com.id_area, com.id_cur"
				+ " ) f  group by f.id_mat, f.id_area"
				+ " ) ff where ff.promedio<11"
				+ " ) f1 where f1.mate=1 and f1.comu=1 and f1.ciencia=1 and f1.pers_social=1 ) ";
		
		param = new Param();
		param.put("id_sit", id_sit);
		param.put("id_per", id_per);
		param.put("nump", nump);
		param.put("id_grados", Arrays.asList(id_grados));
 		 
		actualizados= sqlUtil.update(sqlPromedio, param);
		logger.debug("recuperacion:" + actualizados);
		
		return actualizados;

	}

	public Integer repetir1ro5to(Integer id_per,  Integer id_sit ) {

		String sqlPromedio = "update mat_matricula set id_sit=:id_sit, fec_act=curdate(), usr_act=1 "
				+ " where  ifnull(id_sit,0) not in (4,5,6)"
				//+ " and id  in (select  f1.id_mat, count(f1.promedio) promedio  from("
				+ " and id  in (select  f1.id_mat   from("
				+ "\n select  f.id_mat, f.id_area, round(avg(f.nota_com))  promedio  from("
				+ "\n select  x.id_mat, x.id_area, x.id_cur, round(avg(x.nota_com))  nota_com  from ("
				+ "\n select com.id_mat, com.nump, com.id_area, com.id_cur ,round(cast(sum(com.nota_cap*com.peso) as decimal(10,2))/cast(sum(com.peso) as decimal(10,2)))  nota_com"
				+ "\n from("
				+ "\n select cap.id_mat, cap.nump, cap.id_area, cap.id_cur, cap.id, cap.peso, round(avg(cap.nota_ind)) nota_cap"
				+ "\n from ("
				+ "\n SELECT mat.id id_mat, ne.nump, caa.id_area, cua.id_cur, com.id, com.peso,cap.id cap_id, nni.nota  nota_ind"
				+ "\n FROM col_curso_aula cca "
				+ "\n INNER JOIN  mat_matricula mat  ON mat.id_au_asi=cca.id_au"
				+ "\n INNER JOIN alu_alumno alu ON mat.id_alu=alu.id  "
				+ "\n INNER JOIN col_curso_anio cua ON cca.id_cua=cua.id"
				+ "\n INNER JOIN col_area_anio caa ON caa.`id`=cua.`id_caa`"
				+ "\n INNER JOIN not_evaluacion ne ON  ne.`id_cca`=cca.`id`  "
				+ "\n INNER JOIN not_nota nn ON nn.`id_alu`=alu.`id` AND nn.`id_ne`=ne.`id`"
				+ "\n INNER JOIN not_ind_eva nie ON  nie.`est`='A' AND nie.`id_ne`=ne.`id` "
				+ "\n INNER JOIN not_nota_indicador nni ON nn.`id`=nni.`id_not`  AND nni.`id_nie`=nie.`id` "
				+ "\n INNER JOIN `col_indicador` ind ON nie.`id_ind`=ind.`id`"
				+ "\n INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
				+ "\n INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+ "\n INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ "\n INNER JOIN col_capacidad cap ON cgc.`id_cap`=cap.`id`" 
				+ "\n INNER JOIN col_competencia com ON cap.id_com=com.id "
				+ "\n inner join col_aula au on au.id = cca.id_au "
				+ "\n left join not_curso_exoneracion nce on nce.id_mat = mat.id and nce.id_cur =cua.id_cur"
				+ "\n WHERE au.id_per=:id_per " //and au.id_grad in (:id_grados)"
				+ " and ifnull(nce.id,0)=0" // NO TOMA EN CUENTA EXONERADOS
				//+ "\n AND caa.id_area in (:id_areas)"
				+ "\n and mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
				+ "\n )cap"
				+ "\n group by cap.id_mat, cap.nump, cap.id_area, cap.id_cur, cap.id, cap.peso "
				+ "\n ) com"
				+ "\n group by com.id_mat , com.nump, com.id_area, com.id_cur"
				+ "\n )x group by x.id_mat, x.id_area, x.id_cur"
				+ "\n ) f  group by f.id_mat, f.id_area"
				+ "\n ) f1 where f1.promedio<=10 group by f1.id_mat having count(*)>3)";
		
		Param param = new Param();
		param.put("id_sit", id_sit);
		param.put("id_per", id_per);  
		 
		int repetidos= sqlUtil.update(sqlPromedio, param);
		logger.debug("repetidos:" + repetidos);
		return repetidos;
 

	}
	
	public Integer recuperacion1ro5to(Integer id_per,  Integer id_sit ) {
/*
		String sqlPromedio = "update mat_matricula set id_sit=:id_sit, fec_act=curdate(), usr_act=1 "
				+ " where  ifnull(id_sit,0) not in (4,5,6,3)" //YA NO SE TOMA EN CUENTA LOS JALADOS
				+ " and id  in (select  f1.id_mat   from("
				+ "\n select  f.id_mat, f.id_area, round(avg(f.nota_com))  promedio  from("
				+ "\n select  x.id_mat, x.id_area, x.id_cur, round(avg(x.nota_com))  nota_com  from ("
				+ "\n select com.id_mat, com.nump, com.id_area, com.id_cur ,round(cast(sum(com.nota_cap*com.peso) as decimal(10,2))/cast(sum(com.peso) as decimal(10,2)))  nota_com"
				+ "\n from("
				+ "\n select cap.id_mat, cap.nump, cap.id_area, cap.id_cur, cap.id, cap.peso, round(avg(cap.nota_ind)) nota_cap"
				+ "\n from ("
				+ "\n SELECT mat.id id_mat, ne.nump, caa.id_area, cua.id_cur, com.id, com.peso,cap.id cap_id, nni.nota  nota_ind"
				+ "\n FROM col_curso_aula cca "
				+ "\n INNER JOIN  mat_matricula mat  ON mat.id_au_asi=cca.id_au"
				+ "\n INNER JOIN alu_alumno alu ON mat.id_alu=alu.id  "
				+ "\n INNER JOIN col_curso_anio cua ON cca.id_cua=cua.id"
				+ "\n INNER JOIN col_area_anio caa ON caa.`id`=cua.`id_caa`"
				+ "\n INNER JOIN not_evaluacion ne ON  ne.`id_cca`=cca.`id`  "
				+ "\n INNER JOIN not_nota nn ON nn.`id_alu`=alu.`id` AND nn.`id_ne`=ne.`id`"
				+ "\n INNER JOIN not_ind_eva nie ON  nie.`est`='A' AND nie.`id_ne`=ne.`id` "
				+ "\n INNER JOIN not_nota_indicador nni ON nn.`id`=nni.`id_not`  AND nni.`id_nie`=nie.`id` "
				+ "\n INNER JOIN col_ind_sub cis ON cis.id=nie.`id_cis`"
				+ "\n INNER JOIN col_indicador ind ON cis.`id_ind`=ind.`id`"
				+ "\n INNER JOIN col_capacidad cap ON cap.id=ind.id_cap "
				+ "\n INNER JOIN col_competencia com ON cap.id_com=com.id "
				+ "\n inner join col_aula au on au.id = cca.id_au "
				+ "\n left join not_curso_exoneracion nce on nce.id_mat = mat.id and nce.id_cur =cua.id_cur"
				+ "\n WHERE au.id_per=:id_per " //and au.id_grad in (:id_grados)"
				+ "\n and ifnull(nce.id,0)=0" // NO TOMA EN CUENTA EXONERADOS
				//+ "\n AND caa.id_area in (:id_areas)"
				+ "\n and mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
				+ "\n )cap"
				+ "\n group by cap.id_mat, cap.nump, cap.id_area, cap.id_cur, cap.id, cap.peso "
				+ "\n ) com"
				+ "\n group by com.id_mat , com.nump, com.id_area, com.id_cur"
				+ "\n )x group by x.id_mat, x.id_area, x.id_cur"
				+ "\n ) f  group by f.id_mat, f.id_area"
				+ "\n ) f1 where f1.promedio<=10 group by f1.id_mat having count(*)<4)";
		*/
		
		String sqlPromedio = "update mat_matricula set id_sit=:id_sit, fec_act=curdate(), usr_act=1 "
				+ "  where  ifnull(id_sit,0) not in (4,5,6,3) and id  in (select  f1.id_mat   from(SELECT FIN.id_mat, FIN.id_area, ROUND(AVG(FIN.promedio))  promedio"
				+"\n FROM"
				+"\n ("
				+"\n 	SELECT ARE.id_mat, ARE.id_area, ARE.id_cur, ROUND(AVG(ARE.promedio))  promedio"
				+"\n 	FROM"
				+"\n 	(	"
				+"\n 		SELECT PER.id_mat, PER.nump, PER.id_area,PER.id_cur, ROUND(AVG(PER.nota_curso))  promedio"
				+"\n 		FROM"
				+"\n 		(	"
				+"\n 			SELECT CURSO.id_mat, CURSO.nump, CURSO.id_area, CURSO.id_cur, CURSO.com_id, ROUND(CAST(SUM(CURSO.nota_ind*CURSO.peso) AS DECIMAL(10,2))/CAST(SUM(CURSO.peso) AS DECIMAL(10,2)))  nota_curso"
				+"\n 			FROM ("
				+"\n 				SELECT mat.id id_mat, ne.nump, caa.id_area, cua.id_cur, com.id com_id, com.peso,cap.id cap_id, nni.nota  nota_ind"
				+"\n 				FROM not_evaluacion ne"
				+"\n 				LEFT JOIN not_nota nn ON nn.id_ne = ne.id " //AND nn.id_alu=:id_alu 
				+"\n 				INNER JOIN `mat_matricula` mat ON nn.`id_alu`=mat.`id_alu`"
				+"\n 				INNER JOIN `col_aula` au ON mat.`id_au`=au.`id`"
				+"\n 				INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+"\n 				INNER JOIN not_ind_eva nie ON nie.id_ne = ne.id"
			//	+"\n 				INNER JOIN col_ind_sub cis ON cis.id = nie.id_cis"
				+"\n 				INNER JOIN `col_indicador` ind ON nie.`id_ind`=ind.`id`"	
				+"\n				INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
				+"\n				INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+ "\n				INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ "\n				INNER JOIN col_capacidad cap ON cgc.`id_cap`=cap.`id`"
				+ "\n				INNER JOIN col_competencia com ON cap.id_com=com.id "
			//	+"\n				INNER JOIN col_grup_capacidad ON cgp.id_cap="			
			//	+"\n 				INNER JOIN col_curso_subtema ccs ON ccs.id = cis.id_sub"
			//	+"\n 				INNER JOIN col_subtema sub ON sub.id = ccs.id_sub"
			//	+"\n 				INNER JOIN col_tema tem ON tem.id = sub.id_tem"
			//	+"\n 				INNER JOIN col_indicador ci ON cis.id_ind=ci.id"
			//	+"\n 				INNER JOIN col_capacidad cap ON cap.id = ci.id_cap"
			//	+"\n 				INNER JOIN col_competencia com ON com.id = cap.id_com"
				+"\n 				INNER JOIN not_nota_indicador nni ON nni.id_nie= nie.id AND nni.id_not= nn.id"
				+"\n 				INNER JOIN col_curso_aula cca ON cca.id = ne.id_cca "
				+"\n 				INNER JOIN col_curso_anio cua ON cua.id = cca.id_cua AND  cua.id_cur ";
		sqlPromedio += "\n				IN (SELECT cua1.id_cur FROM col_curso_anio cua1, col_curso_aula cca1 WHERE cua1.id = cca1.id_cua AND cca1.id_au = mat.id_au_asi )";
		sqlPromedio += "\n 			INNER JOIN `col_area_anio` caa ON caa.`id`=cua.`id_caa`"
				+"\n 				WHERE  au.id_per=:id_per  "
				//+"\n 				per.id_anio=:id_anio"
				+"\n 				AND nie.est='A'"
				//+"\n 				AND caa.`id_area`=14"
				//+"\n 				AND mat.ID=:id_mat"
				+"\n 				AND mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
				+"\n 			) CURSO"
				+"\n 			GROUP BY 	"
				+"\n 			CURSO.id_mat,CURSO.nump, CURSO.id_area, CURSO.id_cur, CURSO.com_id"
				+"\n 		) PER "
				+"\n 		GROUP BY PER.id_mat, PER.nump, PER.id_area,PER.id_cur"
				+"\n 	)ARE "
				+"\n 	GROUP BY ARE.id_mat, ARE.id_area, ARE.id_cur"
				+"\n ) FIN GROUP BY FIN.id_mat, FIN.id_area ) f1 where f1.promedio<=10 group by f1.id_mat having count(*)<4)";

		
		Param param = new Param();
		param.put("id_sit", id_sit);
		param.put("id_per", id_per);  
		 
		int repetidos= sqlUtil.update(sqlPromedio, param);
		logger.debug("recuperacion:" + repetidos);
		return repetidos;
 

	}
	
	
}
