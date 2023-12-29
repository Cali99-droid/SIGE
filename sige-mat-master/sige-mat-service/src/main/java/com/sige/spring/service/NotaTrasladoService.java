package com.sige.spring.service;

import java.util.Date;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.NotaDAO;
import com.sige.mat.dao.NotaIndicadorDAO;
import com.tesla.colegio.model.Nota;
import com.tesla.colegio.model.NotaIndicador;
import com.tesla.frmk.sql.Row;

@Service
public class NotaTrasladoService {
 
	
	@Autowired
	private NotaIndicadorDAO notaIndicadorDAO;
	
	@Autowired
	private NotaDAO notaDAO;
	
	@Autowired
	private SQLUtil sqlUtil;	

	@Transactional
	public Integer grabarNotasPeriodo(Integer id_cpu,Integer id_mat, Integer[] notas){
		
		//cursos del alumno
		
		String sqlCursos = "select cua.id_cur"
				+ " from mat_matricula mat"
				+ " inner join col_curso_aula cca on cca.id_au = mat.id_au_asi"
				+ " inner JOIN col_curso_anio cua ON cua.id = cca.id_cua "
				+ " where mat.id= ? order by cua.id_cur";
		
		List<Row> cursos = sqlUtil.query(sqlCursos, new Object[]{id_mat});
		int i=0;
		
		Map<Integer, BigDecimal> notaMap = new HashMap<Integer, BigDecimal>();
		for (Row row : cursos) {
			notaMap.put(row.getInteger("id_cur"), new BigDecimal(notas[i]));
			i++;
		}
		
		
		String sql = //"SELECT  ne.nump, cua.id_cur, com.id com_id, com.peso,cap.id cap_id, nni.nota  nota_ind"
                    "select cua.id_cur nn_id_cur, ne.id nn_id_ne, cca.id_tra nn_id_tra, mat.id_alu,"//nota
			        + "\n  nie.id nni_id_nie" 
					+ "\n                        from mat_matricula mat"
					+ "\n							inner join col_curso_aula cca on cca.id_au = mat.id_au_asi"
					+ "\n							inner JOIN col_curso_anio cua ON cua.id = cca.id_cua  "
					+ "\n							inner JOIN col_aula au ON mat.id_au_asi=au.id"
					+ "\n							inner JOIN per_periodo per ON au.id_per=per.id"
					+ "\n							inner JOIN not_evaluacion ne ON (ne.nump=? and cca.id = ne.id_cca )"
					+ "\n							inner JOIN not_ind_eva nie ON nie.id_ne = ne.id " 
					+ "\n							inner JOIN col_ind_sub cis ON cis.id = nie.id_cis" 
					+ "\n							left JOIN not_nota nn ON (nn.id_alu=mat.id_alu and nn.id_ne = ne.id )"
					+ "\n							left JOIN not_nota_indicador nni ON nni.id_not= nn.id -- AND nie.est='A'"
					+ "\n							left JOIN col_curso_subtema ccs ON ccs.id = cis.id_sub"
					+ "\n							left JOIN col_subtema sub ON sub.id = ccs.id_sub"
					+ "\n							left JOIN col_tema tem ON tem.id = sub.id_tem"
					+ "\n							left JOIN col_indicador ci ON cis.id_ind=ci.id"
					+ "\n							left JOIN col_capacidad cap ON cap.id = ci.id_cap"
					+ "\n							left JOIN col_competencia com ON com.id = cap.id_com"
					+ "\n						WHERE  "
					+ "\n						 mat.id=? and  nie.est='A'" //-- nd mat.id_alu= 
					+ "\n						AND mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5) "
					+ "\n						order by cua.id_cur";
		
		List<Row> notaPorIngresar = sqlUtil.query(sql, new Object[]{id_cpu, id_mat});
		for (Row row : notaPorIngresar) {
			
			//datos de la nota
			
			Nota nota = new Nota();
			nota.setFec(new Date());
			nota.setId_alu(row.getInteger("id_alu"));
			nota.setId_ne(row.getInteger("nn_id_ne"));
			nota.setId_tra(row.getInteger("nn_id_tra"));
			nota.setProm(notaMap.get(row.getInteger("nn_id_cur")));
			nota.setEst("A");
			nota.setUsr_ins(1);
			
			Integer id_not = notaDAO.saveOrUpdate(nota);
			
			//datos de la nota indicador
			
			NotaIndicador notaIndicador = new NotaIndicador();
			notaIndicador.setId_nie(row.getInteger("nni_id_nie"));
			notaIndicador.setId_not(id_not);
			notaIndicador.setNota(nota.getProm().intValue());
			notaIndicador.setEst("A");
			notaIndicador.setUsr_ins(1);
			notaIndicadorDAO.saveOrUpdate(notaIndicador);
			
		}
		
		return notaPorIngresar.size();
	}
}
