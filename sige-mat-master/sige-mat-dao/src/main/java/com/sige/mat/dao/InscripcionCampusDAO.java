package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.InscripcionCampusDAOImpl;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;

/**
 * Define m�todos DAO operations para la entidad inscripcion_campus.
 * @author MV
 *
 */
@Repository
public class InscripcionCampusDAO extends InscripcionCampusDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listarInscripcionesAulaVirtual(Integer id_cga, Integer id_niv, Integer id_grad, Integer id_anio, String inscrito) { //id_au
		
		String sql = "SELECT DISTINCT * FROM (";
				sql += "SELECT fam.id id_fam, CONCAT(fam.`ape_pat`,' ', fam.`ape_mat`,' ', fam.`nom`) apoderado, alu.`id` id_alu, fam.cel celular, fam.corr correo,";
				sql +=" CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.nom) alumno, cuc.`usr`, cuc.`psw`, gra.`nom` grado, gra.id id_gra, niv.`nom` nivel, au.secc seccion, cic.est, cgr.nro,  CONCAT(cgr.`des`,' ', cgr.`nro`) grupo, cic.`id` id_cic, cic.`tc_acept`";
				sql +=" FROM mat_matricula mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id` and (mat.id_sit<>5 OR mat.id_sit IS NULL)"; 
				sql +=" INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.`id`";
				sql +=" INNER JOIN `cat_grad` gra ON mat.`id_gra`=gra.`id`";
				sql +=" INNER JOIN cat_nivel niv ON gra.`id_nvl`=niv.`id`";
				sql +=" INNER JOIN col_aula au ON mat.id_au_asi=au.id";
				sql +=" INNER JOIN per_periodo per ON per.id=au.id_per";
				sql +=" LEFT JOIN `cvi_inscripcion_campus` cic ON mat.`id_alu`=cic.`id_alu` AND mat.`id_fam`=cic.`id_fam` AND cic.`id_anio`="+id_anio;
				sql +=" LEFT JOIN `cvi_usuario_campus` cuc ON cic.`id`=cuc.`id_cvic`";
				sql +=" LEFT JOIN `cvi_grupo_alumno` cga ON alu.`id`=cga.`id_alu`";
				sql +=" LEFT JOIN `cvi_grupo_aula_virtual` cgr ON cga.`id_cgr`=cgr.`id` AND cgr.`id_gra`=gra.`id` AND cgr.`id_anio`="+id_anio;
				sql +=" WHERE niv.`id`=?  AND per.id_anio=?";
				if(id_grad!=null)
					sql += "\n AND au.id_grad="+id_grad;
			   if (id_cga!=null)
				    sql += "\n AND cgr.id="+id_cga;
			   sql += "\n ORDER BY gra.id, alu.ape_pat, alu.ape_mat, alu.nom)t ";
			   if(inscrito.equals("1")){
				   sql +="\n WHERE t.id_cic IS NOT NULL AND t.tc_acept=1 AND t.est='A' ";
			   } else if(inscrito.equals("0")){
				   sql +="\n WHERE (t.id_cic IS  NULL OR t.tc_acept=0)";
			   }
			 sql +=" ORDER BY t.id_gra, t.nro";
			   
		return sqlUtil.query(sql, new Object[]{id_niv,id_anio});
	}
	
	/**
	 * Listar Inscritos agrupamiento
	 * @param id_anio
	 * @param id_gra
	 * @return
	 */
	public List<Row> listarInscritosAgrupamiento(Integer id_anio, Integer id_gra) {
		
		String sql = "SELECT alu.`id` id_alu, CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`) alumno, niv.`nom` nivel, gra.`nom` grado, au.`secc`"
				+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `cat_grad` gra ON mat.`id_gra`=gra.`id`"
				+ " INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
				+ " INNER JOIN `cvi_inscripcion_campus` cic ON cic.`id_alu`=mat.`id_alu` AND cic.`id_fam`=mat.`id_fam`"
				+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+ " WHERE per.`id_anio`=? AND gra.`id`=? and alu.id NOT IN (select id_alu from cvi_grupo_alumno cga inner join cvi_grupo_aula_virtual cgr ON cga.id_cgr=cgr.id where cgr.id_anio="+id_anio+")"
				+ " ORDER BY  au.`secc`, alu.ape_pat, alu.ape_mat, alu.nom";	// cic.`fec_ins`;
				
		return sqlUtil.query(sql, new Object[]{id_anio,id_gra});
	}
	
	public List<Row> listarInscritosxAnio(Integer id_anio) {
		
		String sql = "SELECT alu.`usuario`, gra.`nom` grado, niv.`nom` nivel  "
				+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.`id`"
				+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`"
				+ " INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
				+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+ " INNER JOIN `cvi_inscripcion_campus` cvi ON cvi.`id_alu`=alu.`id` AND cvi.`id_fam`=fam.`id` AND cvi.`id_anio`=per.`id_anio`"
				+ " WHERE per.`id_anio`=? and (mat.id_sit<>5 OR mat.id_sit IS NULL)";
			   
		return sqlUtil.query(sql, new Object[]{id_anio});
	}
	
	public List<Row> listarInscritosxAnioyBimestre(Integer id_anio, Integer nro_pe) {
		
		String sql = "SELECT alu.id, alu.id_classRoom, cuc.usr, a.`nom` anio  , cpa.`abrev` periodo_abrev,  niv.`cod_mod`, gra.`abrv_classroom` , ccav.`abrev` curso_abrev, cgr.`nro` nro_grupo"
				+ " FROM `alu_alumno` alu INNER JOIN `cvi_inscripcion_campus` cvi ON alu.`id`=cvi.`id_alu`"
				+ " INNER JOIN `col_anio` a ON cvi.`id_anio`=a.`id`"
				+ " INNER JOIN cvi_usuario_campus cuc ON cvi.id=cuc.id_cvic "
				+ " INNER JOIN `cvi_grupo_alumno` cga ON alu.`id`=cga.`id_alu`"
				+ " INNER JOIN `cvi_grupo_aula_virtual` cgr ON cga.`id_cgr`=cgr.`id`"
				+ " INNER JOIN `cat_grad` gra ON cgr.`id_gra`=gra.`id`"
				+ " INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
				+ " INNER JOIN `cvi_curso_aula_virtual` ccav ON ccav.`id_gra`=gra.`id`"
				+ " INNER JOIN `cvi_periodo_curso` cpc ON ccav.`id`=cpc.`id_cau`"
				+ " INNER JOIN `cvi_periodo_aula_virtual` cpa ON cpc.`id_cpv`=cpa.`id` AND cpa.`id_niv`=niv.`id`"
				+ " WHERE cvi.`id_anio`==  AND cpa.`nro_per`=? ";
			   
		return sqlUtil.query(sql, new Object[]{id_anio, nro_pe});
	}
}
