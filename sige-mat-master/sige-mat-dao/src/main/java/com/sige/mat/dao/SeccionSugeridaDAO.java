package com.sige.mat.dao;

import java.util.List; 
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sige.common.enums.EnumGrado;
import com.sige.common.enums.EnumSituacionFinal;
import com.sige.common.enums.EnumTipoPeriodo;
import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.SeccionSugeridaDAOImpl;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.SeccionSugerida;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad seccion_sugerida.
 * @author MV
 *
 */
@Repository
public class SeccionSugeridaDAO extends SeccionSugeridaDAOImpl{
	final static Logger logger = Logger.getLogger(MatriculaDAO.class);

	@Autowired
	private SQLUtil sqlUtil;

	@Autowired
	private AulaDAO aulaDAO;

	@Autowired
	private PeriodoDAO periodoDAO;

	
	public List<Row> listaralumnosYseccionSugerida(Integer id_au,Integer id_anio, Integer id_anio_sig){
		
		String sql = "SELECT DISTINCT alu.id id_alu, mat.id id_mat, CONCAT(pera.`ape_pat`,' ', pera.`ape_mat`,' ', pera.`nom`) alumno,"
				+ "\n CASE  WHEN pera.id_gen='1' THEN 'M'  WHEN pera.id_gen='0' THEN 'F' END AS 'genero', "
				//+ "\n col.cod_mod, "
				+ "\n (SELECT perf.nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf, col_persona perf "
				+ "\n  WHERE perf.id_gen='1' AND alu_gpf.id_gpf=fam_gpf.id_gpf AND alu.id=alu_gpf.id_alu AND fam_gpf.id_fam=fam.id AND perf.id=fam.id_per LIMIT 1) AS papa_dni,"
				+ "\n (SELECT perf.nro_doc FROM alu_familiar fam,alu_gru_fam_familiar fam_gpf,alu_gru_fam_alumno alu_gpf, col_persona perf WHERE perf.id_gen='0'" 
				+ "\n AND alu_gpf.id_gpf=fam_gpf.id_gpf AND alu.id=alu_gpf.id_alu AND fam_gpf.id_fam=fam.id AND perf.id=fam.id_per LIMIT 1) AS mama_dni,"
				+ "\n niv.`nom` nivel, gra.`nom` grado, au.`secc` seccion, sit.`nom`, sit.id id_sit, "
				+ "\n msg.id sug_id, msg.id_suc_nue sug_id_suc, au_nue.id sug_au_id, au_nue.secc sug_secc,  gra_nue.`nom` sug_grado "
				+ "\n FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ "\n INNER JOIN col_persona pera ON alu.`id_per`=pera.`id`"
				+ "\n INNER JOIN col_aula au ON mat.`id_au_asi`=au.`id`"
				+ "\n INNER JOIN per_periodo per ON au.`id_per`=per.`id` and per.id_anio="+id_anio+" AND per.id_tpe=1 "
				+ "\n INNER JOIN cat_grad  gra ON au.id_grad=gra.id"
				+ "\n INNER JOIN cat_nivel niv ON gra.id_nvl=niv.`id`"
				//+ "\n LEFT JOIN eva_matr_vacante mat_vac ON mat_vac.id_alu=alu.id AND mat_vac.res='A'"
				//+ "\n  LEFT JOIN `eva_evaluacion_vac` eva ON mat_vac.id_eva=eva.id"
				//+ "\n LEFT JOIN per_periodo pervac ON eva.id_per=pervac.id AND per.id_anio=5"
				//+ "\n LEFT JOIN col_colegio col ON mat_vac.id_col=col.id"
				+ "\n LEFT JOIN cat_col_situacion sit ON mat.`id_sit`=sit.`id`"
				+ "\n LEFT JOIN mat_seccion_sugerida msg ON mat.id=msg.id_mat and msg.id_anio= " + id_anio_sig
				+ "\n LEFT JOIN col_aula au_nue ON au_nue.id=msg.id_au_nue "
				+ "\n LEFT JOIN per_periodo per_nue ON au_nue.`id_per`=per_nue.`id`"
				//+ "\n LEFT JOIN cat_grad  gra_nue ON au_nue.`id_grad`=gra_nue.`id`" Funciono hasta el 2021
				+ "\n LEFT JOIN cat_grad  gra_nue ON msg.`id_gra_nue`=gra_nue.`id`"
				//+ "\n LEFT JOIN ges_sucursal  suc_nue ON suc_nue.`id`=msg.`id_suc_nue`"
				+ "\n WHERE (  (mat.id_au=:id_au and mat.id_au in (select esp.id_au from col_aula_especial esp where  esp.id_mat= mat.id ) )  or mat.id_au_asi=:id_au)"
				+ "\n and (mat.id_sit not in (4,5,6) OR mat.id_sit IS NULL) "
				+ "\n ORDER BY pera.ape_pat, pera.ape_mat, pera.nom";
 
		Param param = new Param();
		param.put("id_au", id_au);
		
		List<Row>  lista = sqlUtil.query(sql,param);
 
		return lista;
	}
	
	
	@Transactional
	public Integer actualizarAlumnosSeccionSugerida(Integer id_au,Integer id_anio, Integer id_anio_sig) throws Exception{
		String sql = "SELECT mat.id_au mat_id_au,  au.id id_au, alu.id id_alu, mat.id id_mat, CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`) alumno,"
				+ " niv.`nom` nivel, gra.id id_gra, gra.`nom` grado, au.`secc` seccion, sit.nom, sit.id id_sit "
				+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN col_aula au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN per_periodo per ON au.`id_per`=per.`id`"
				+ " INNER JOIN cat_grad  gra ON au.id_grad=gra.id"
				+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.`id`"
				+ " LEFT JOIN cat_col_situacion sit ON mat.`id_sit`=sit.`id`"
				+ " WHERE mat.id_sit not in (4,5,6)";
		
				if ( id_au !=null)
					//	sql = sql	+ " and  mat.id_au_asi="+id_au;
					sql = sql	+ " and (  (mat.id_au=" + id_au + " and mat.id_au in (select esp.id_au from col_aula_especial esp where  esp.id_mat= mat.id ) )  or mat.id_au_asi=" + id_au + ")";

				
				sql = sql	+  " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
 
		//borrando la seccion sugerida del a�o siguiente
		String sqlDelete = "delete from mat_seccion_sugerida where id_anio=" + id_anio_sig ;
		if ( id_au !=null)
			sqlDelete += " and  id_mat in (select id FROM mat_matricula mat where mat.id_au_asi =" + id_au + "  )";
		
		sqlUtil.update(sqlDelete);

		List<Row>  lista = sqlUtil.query(sql);
		
		for (Row row : lista) {
			Integer id_au_ori = null;
			int id_grado = row.getInteger("id_gra");
			
			if(row.getInteger("id_gra").intValue() == EnumGrado.SECUNDARIA_QUINTO.getValue() && 
					row.getInteger("id_sit").intValue() != EnumSituacionFinal.DESAPROBADO.getValue() )
				continue;
			
			id_au_ori = row.getInteger("id_au");
			
			if (id_grado ==15)
				id_au_ori =row.getInteger("mat_id_au");
			
			Aula aulaPeriodoAnterior = aulaDAO.get(id_au_ori);
			
			//AULA QUE PASA DE A�O
			Param param = new Param();
			param.put("id_secc_ant", id_au_ori);
			Aula aulaAprobado = aulaDAO.getByParams(param); 
			
			if (aulaAprobado==null){
				logger.error("No existe aula siguiente para Aula:" + aulaPeriodoAnterior.getSecc() + "(" + row.getInteger("id_au") + ")");
				continue;
				//throw new Exception("No existe aula siguiente para Aula:" + aulaPeriodoAnterior.getSecc() + "(" + row.getInteger("id_au") + ")");
			}
			
			//MISMA AULA.. PERO ID DIFERENTE ( POR EL CAMBIO DE A�O)
			Periodo periodo = periodoDAO.get(aulaPeriodoAnterior.getId_per());
			param = new Param();
			param.put("id_srv", periodo.getId_srv());
			param.put("id_anio", id_anio_sig );
			param.put("id_suc", periodo.getId_suc() );
			param.put("id_niv", periodo.getId_niv());
			param.put("id_tpe", EnumTipoPeriodo.ESCOLAR.getValue());
			
			Periodo periodoActual = periodoDAO.getByParams(param);
			
			if (periodoActual==null)
				logger.error("No existe periodo configurado para los datos:" + param);
				//throw new Exception("No existe periodo configurado para los datos:" + param);
			
			param = new Param();
			param.put("id_per", periodoActual.getId());
			param.put("id_grad",aulaPeriodoAnterior.getId_grad());
			param.put("secc", aulaPeriodoAnterior.getSecc());
			Aula aulaRepeticion = aulaDAO.getByParams(param);
			
			//if (aulaRepeticion==null)
			//	throw new Exception("No existe aula para el a�o siguiente:" + param);
			
				Integer id_mat = row.getInteger("id_mat");
				Integer id_sit = row.getInteger("id_sit");
				Integer id_au_sug = null;
				
				if (id_sit==null)
					throw new Exception("Situacion del alumno " + row.getString("alumno") + " no existe");
				
				if (id_sit.intValue() == EnumSituacionFinal.APROBADO.getValue() 
						|| id_sit.intValue() == EnumSituacionFinal.ADELANTO_EVALUACION.getValue()
						|| id_sit.intValue() == EnumSituacionFinal.REQUIERE_RECUPERACION_PEDAGOGICA.getValue())
					id_au_sug = aulaAprobado.getId();
				else if (id_sit.intValue() == EnumSituacionFinal.DESAPROBADO.getValue() ){
					if (aulaRepeticion!=null)
						id_au_sug = aulaRepeticion.getId();
				}
				SeccionSugerida seccionSugerida = new SeccionSugerida();
				seccionSugerida.setId_au_nue(id_au_sug);//sugerencia del a�o siguiente
				seccionSugerida.setId_mat(id_mat);
				seccionSugerida.setId_anio(id_anio_sig);//a�o actual
				seccionSugerida.setEst("A");
				
				saveOrUpdate(seccionSugerida);
				
		}
		 
		
 
		
		return lista.size();
	}
}
