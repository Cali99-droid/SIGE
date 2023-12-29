package com.sige.spring.service;

 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sige.mat.dao.CursoAulaDAO;
import com.sige.mat.dao.CursoHorarioDAO;
import com.sige.mat.dao.CursoHorarioSesDAO;
import com.sige.mat.dao.CursoUnidadDAO;
import com.sige.mat.dao.NotaDAO;
import com.sige.mat.dao.UnidadSesionDAO;
import com.tesla.colegio.model.CursoAula;
import com.tesla.colegio.model.CursoHorario;
import com.tesla.colegio.model.CursoHorarioSes;
import com.tesla.colegio.model.CursoUnidad;
import com.tesla.colegio.model.UnidadSesion;
import com.tesla.frmk.common.exceptions.ServiceException;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;


@Service
public class CursoHorarioSesionService {
	
	@Autowired
	private CursoHorarioSesDAO cursoHorarioSesDAO;
	
	@Autowired
	private NotaDAO notaDAO;
	
	@Autowired
	private CursoHorarioDAO cursoHorarioDAO;
	
	@Autowired
	private UnidadSesionDAO unidadSesionDAO;
	 
	/**
	 * validarHorarioSesionxSemana
	 * @param id_au
	 * @param nro_sem
	 * @return
	 * @throws ServiceException
	 */
	public int validarHorarioSesionxSemana(Integer id_cca, Integer nro_sem) throws ServiceException{
		
		
		for (int i = nro_sem-1; i >1; i--) {
		
			List<Row> horarios_sem=cursoHorarioSesDAO.validarHorarioSesionxSemana(id_cca, nro_sem-1);
			if(horarios_sem.size()==0)
				return nro_sem-1;
		}
		

		return nro_sem;
	}
	
	public Integer validarNotasxHorarioSesion (Integer id_cch, Integer id_sem) throws ServiceException{
		
		Param param = new Param();
		param.put("id_ccs", id_sem);
		param.put("id_cch", id_cch);
		CursoHorarioSes sesion =cursoHorarioSesDAO.getByParams(param);
		CursoHorario cursoHorario = cursoHorarioDAO.getByParams(new Param("id",id_cch));
		if(sesion==null)
			return 0;
		else{
			Integer id_uns= sesion.getId_uns();
			UnidadSesion unidadSesion = unidadSesionDAO.getByParams(new Param("id",id_uns));
			Integer id_cca=cursoHorario.getId_cca();
			Integer id_uni=unidadSesion.getId_uni();
			List<Row> notas = notaDAO.listarNotasxUnidadSesion(id_cca, id_uni);
			return notas.size();
		}


	}
	
	public List<Row> listaAgenda(Integer id_alu,Integer mes){
		
		return cursoHorarioSesDAO.listaAgenda(id_alu,  mes);
	}
	
	public List<Row> listarDetalle(Integer id_cchs){
		List<Row> tema = cursoHorarioSesDAO.listarTemasxCursoSesion(id_cchs);
		//List<Row> tema_subtema = new ArrayList<>();
		for (Row temaList : tema) {
			List<Row> subtema = cursoHorarioSesDAO.listarSubtemasxCursoSesion(id_cchs, temaList.getInteger("id"));
			for (Row subtemaList : subtema) {
			List<Row> indicador = cursoHorarioSesDAO.listarIndicadoresxSubtema(id_cchs, subtemaList.getInteger("id"));
			subtemaList.put("indicadores", indicador);
			}
			temaList.put("subtemas", subtema);
			//tema.add(row);	
		}
		return tema;
	}
	
	
	/*
	public Integer validarNotasxHorarioSesion (Integer id_cchs) throws ServiceException{
		Integer id_uns= cursoHorarioSesDAO.getByParams(new Param("id",id_cchs)).getId_uns();
		List<Row> notas = notaDAO.listarNotasxUnidadSesion(id_uns);
		return notas.size();
		
		if(notas.size()>0){
			throw new ServiceException(
					"No se puede modificar la vinculas�on de sesiones, porque ya tiene notas registradas!!");
		} else
			return 0;
		
	}
	*/
}
