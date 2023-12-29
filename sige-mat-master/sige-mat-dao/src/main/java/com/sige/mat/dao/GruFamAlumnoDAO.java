package com.sige.mat.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.GruFamAlumnoDAOImpl;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.GruFamAlumno;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Nivel;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad gru_fam_alumno.
 * @author MV
 *
 */
@Repository
public class GruFamAlumnoDAO extends GruFamAlumnoDAOImpl{
	final static Logger logger = Logger.getLogger(GruFamAlumnoDAO.class);

	@Autowired
	SQLUtil sqlUtil;
	
	/**
	 * Lista de alumnos matriculados pro familia y a�o
	 * @param id_gpf
	 * @param id_anio
	 * @return
	 */
	public List<GruFamAlumno> alumnosMatriculados(Integer id_gpf,Integer id_anio){
		
		String sql = "select mat.id id_mat, alu.id id_alu, au.secc, alu.ape_pat, alu.ape_mat, alu.nom, gra.nom grado, niv.nom nivel "
				+ " from alu_gru_fam_alumno gfa "
				+ " inner join mat_matricula mat on mat.id_alu=gfa.id_alu"
				+ " inner join alu_alumno alu on mat.id_alu=alu.id"
				+ " inner join col_aula au on mat.id_au =au.id"
				+ " inner join cat_grad gra on gra.id =au.id_grad"
				+ " inner join cat_nivel niv on niv.id =gra.id_nvl"
				+ " inner join per_periodo per on au.id_per =per.id"
				+ " where gfa.id_gpf=" + id_gpf + " and per.id_anio=" + id_anio ;
		
		List<Row> lista= sqlUtil.query(sql);
		List<GruFamAlumno> listFamAlu = new ArrayList<GruFamAlumno>();
		for (Row row : lista) {
			
			GruFamAlumno famAlu = new GruFamAlumno();
			Alumno alumno = new Alumno();
			alumno.setApe_mat(row.getString("ape_mat"));
			alumno.setApe_pat(row.getString("ape_pat"));
			alumno.setNom(row.getString("nom"));
			alumno.setId(row.getInteger("id_alu"));
			Matricula matricula = new Matricula();
			matricula.setId(row.getInteger("id_mat"));
			
			Aula aula = new Aula();
			aula.setSecc(row.getString("secc"));
			
			Nivel nivel = new Nivel();
			nivel.setNom(row.getString("nivel"));
			Grad grad = new Grad();
			grad.setNivel(nivel);
			grad.setNom(row.getString("grado"));
			aula.setGrad(grad);
			matricula.setGrad(grad);
			matricula.setAula(aula);
			alumno.setMatricula(matricula);
			famAlu.setAlumno(alumno);
			listFamAlu.add(famAlu);
		}
		
		return listFamAlu;
	}
	
	public void actualizarGrupoFamiliar(Integer id_gpf, Integer id_alu){
		String sql = "update alu_gru_fam_alumno set id_gpf="+id_gpf+" WHERE id_alu="+id_alu;
		sqlUtil.update(sql);
		
	}
	
}
