package com.sige.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.EvaPadreDAO;
import com.sige.mat.dao.EvaluacionDAO;
import com.sige.mat.dao.IndEvaDAO;
import com.sige.mat.dao.NotaDAO;
import com.tesla.colegio.model.CursoAula;
import com.tesla.colegio.model.EvaPadre;
import com.tesla.colegio.model.Evaluacion;
import com.tesla.colegio.model.IndEva;
import com.tesla.colegio.model.Nota;
import com.tesla.frmk.common.exceptions.ServiceException;
import com.tesla.frmk.sql.Param;

@Service
public class EvaluacionService {

	final static Logger logger = Logger.getLogger(EvaluacionService.class);

	@Autowired
	private EvaluacionDAO evaluacionDAO;

	@Autowired
	private NotaDAO notaDAO;

	@Autowired
	private EvaPadreDAO evaPadreDAO;

	@Autowired
	private AulaDAO aulaDAO;
	
	@Autowired
	private IndEvaDAO indEvaDAO;

	/**
	 * Grabar evaluaciones en la pantalla de curricula > sesiones 
	 * en el momento a agrega el tipo examen
	 * 
	 * @param id_ind
	 * @param id_nep
	 * @param id_tra
	 * @param id_cur
	 * @param id_gra
	 * @param id_anio
	 * @param id_niv
	 * @param _id_cpu
	 * @param id_ses
	 * @param ins
	 * @param evi
	 * @param id_nte
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public Integer grabarEvaluaciones(Integer id_ind[], Integer id_nep, Integer id_tra, Integer id_cur, Integer id_gra,
			Integer id_anio, Integer id_niv, Integer _id_cpu, Integer id_ses,String ins, String evi, Integer id_nte) throws ServiceException {

	for (int i = 0; i < id_ind.length; i++) {
		logger.debug(i + ":" + id_ind[i]);
	}
	
	logger.debug("id_nep:" + id_nep); 
	logger.debug("id_tra:" + id_tra); 
	logger.debug("id_cur:" + id_cur); 
	logger.debug("id_gra:" + id_gra);
	logger.debug("id_anio:" + id_anio); 
	logger.debug("id_niv:" + id_niv); 
	logger.debug("_id_cpu:" + _id_cpu);
		
		try {

			// validamos si tiene notas
			List<Evaluacion> evaluacionesActualues = new ArrayList<Evaluacion>();
			if (id_nep != null) {
				Param param = new Param();
				param.put("id_nep", id_nep);

				// evaluaciones con nota
				evaluacionesActualues = evaluacionDAO.listByParams(param, null);

				for (Evaluacion evaluacion2 : evaluacionesActualues) {
					int id = evaluacion2.getId();
					logger.debug(id);

					Param param1 = new Param();
					param1.put("id_ne", id);

					List<Nota> notas = notaDAO.listByParams(param1, null);

					if (notas.size() > 0) {

						throw new ServiceException(
								"No se puede modificar la evaluacion si tiene notas, por favor elimine todas las notas de la evaluacion para proceder a modificar");
					}

				}

			} 

			List<Integer> listaPorDesactivar = new ArrayList<Integer>();

			// GRABAMOS EL PADRE (insert o update)
			EvaPadre evaPadre = new EvaPadre();
			evaPadre.setId(id_nep);
			evaPadre.setId_tra(id_tra);
			evaPadre.setEst("A");
			id_nep = evaPadreDAO.saveOrUpdate(evaPadre);

			// grabamos evaluaciones
			List<CursoAula> cursoAula = aulaDAO.listAulasxGrado(id_anio, id_cur, id_gra);
			
			if(cursoAula.size()==0){
				throw new ServiceException("El docente no tiene aulas configuradas!!");
			}
			
			
			if(evaluacionesActualues.size()==0){
				for (CursoAula aula : cursoAula) {

					//NUEVA EVALUACION
					Evaluacion evaluacion = new Evaluacion();
					evaluacion.setId_nep(id_nep);
					evaluacion.setId_cca(aula.getId());
					evaluacion.setNump(_id_cpu);
					evaluacion.setId_ses(id_ses);
					evaluacion.setEvi(evi);
					evaluacion.setIns(ins);
					evaluacion.setId_nte(id_nte);
					
					evaluacion.setEst("A");
					Integer id_ne = evaluacionDAO.saveOrUpdate(evaluacion);
					

					//NUEVOS INDICADORES
					for (int i = 0; i < id_ind.length; i++) {
						
						IndEva indicador = new IndEva();
						indicador.setEst("A");
						indicador.setId_ind(id_ind[i]);
						indicador.setId_ne(id_ne);
						indEvaDAO.saveOrUpdate(indicador);
					}
				}
				
				
			}


			if (listaPorDesactivar.size() > 0)
				evaluacionDAO.deshabilitar(listaPorDesactivar);

		  
 
		} catch (Exception e) {
			logger.error("Grabar evaluacion",e);
			throw new ServiceException("Existe un error al grabar las evaluaciones");
			
 		}
		
		return null;
	}

}
