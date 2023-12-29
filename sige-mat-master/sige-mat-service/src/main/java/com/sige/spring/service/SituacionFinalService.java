package com.sige.spring.service;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.common.enums.EnumAreaSIAGE;
import com.sige.common.enums.EnumGrado;
import com.sige.common.enums.EnumNivel;
import com.sige.common.enums.EnumSituacionFinal;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.PerUniDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.SitHistorialDAO;
import com.sige.mat.dao.SituacionMatDAO;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.PerUni;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.SitHistorial;

@Service
public class SituacionFinalService {

	final static Logger logger = Logger.getLogger(SituacionFinalService.class);


	@Autowired
	private PeriodoDAO periodoDAO;

	@Autowired
	private MatriculaDAO matriculaDAO;

	@Autowired
	private PerUniDAO peruniDAO;

	@Autowired
	private SituacionMatDAO situacionMatDAO;

	@Autowired
	private SitHistorialDAO sitHistorialDAO;
	
	@Transactional
	public void grabar(Integer[] id) {

		for (Integer id_per : id) {
			Periodo periodo = new Periodo();
			periodo = periodoDAO.get(id_per);
			Integer id_niv = periodo.getId_niv();

			if (id_niv.equals(EnumNivel.INICIAL.getValue())) {

				// automaticamente todos los alumnos pasan a situacion aprobado
				Integer actualizados = situacionMatDAO.actualizarSituacionFinal(id_per,
						EnumSituacionFinal.APROBADO.getValue());
				logger.debug("inicial actualizados:" + actualizados);

			} else if (id_niv.equals(EnumNivel.PRIMARIA.getValue())) {
				// PARA PRIMARIA SOLO SE TOMA EN CUENTA EL PROMEDIO DEL ULTIMO
				// NIVEL
				PerUni perUni = peruniDAO.getUltimoPeriodo(id_niv);

				// actualizacion PRIMER GRADO
				// todos son promovidos
				situacionMatDAO.actualizarSituacionFinal(id_per,
						new Integer[] { EnumGrado.PRIMARIA_PRIMERO.getValue() },
						EnumSituacionFinal.APROBADO.getValue());

				// actualizacion 2DO - GRADO AL 4TO GRADO
				// Matematica y Comunicaci�n: C Repitencia Automatica
				situacionMatDAO.repetir2do4to(id_per,
						new Integer[] { EnumGrado.PRIMARIA_SEGUNDO.getValue(), EnumGrado.PRIMARIA_TERCERO.getValue(),
								EnumGrado.PRIMARIA_CUARTO.getValue() },
						new Integer[] { EnumAreaSIAGE.MATEMATICA.getValue(), EnumAreaSIAGE.COMUNICACION.getValue() },
						EnumSituacionFinal.DESAPROBADO.getValue(), // REPITENCIA
						perUni.getId());

				// Matematica y Comunicaci�n A, las areas restantes B: Promovido
				situacionMatDAO.recuperacion2do4to(id_per,
						new Integer[] { EnumGrado.PRIMARIA_SEGUNDO.getValue(), EnumGrado.PRIMARIA_TERCERO.getValue(),
								EnumGrado.PRIMARIA_CUARTO.getValue() },
						EnumSituacionFinal.REQUIERE_RECUPERACION_PEDAGOGICA.getValue(), // RECUPERACION
						perUni.getId());

				// EL RESTO PROMOVIDO
				situacionMatDAO.actualizarSituacionFinalNulos(
						id_per, new Integer[] { EnumGrado.PRIMARIA_SEGUNDO.getValue(),
								EnumGrado.PRIMARIA_TERCERO.getValue(), EnumGrado.PRIMARIA_CUARTO.getValue() },
						EnumSituacionFinal.APROBADO.getValue());

				// actualizacion 5TO AL 6TO
				// Matematica y Comunicaci�n: C Repitencia Automatica
				situacionMatDAO.repetir2do4to(id_per,
						new Integer[] { EnumGrado.PRIMARIA_QUINTO.getValue(), EnumGrado.PRIMARIA_SEXTO.getValue() },
						new Integer[] { EnumAreaSIAGE.MATEMATICA.getValue(), EnumAreaSIAGE.COMUNICACION.getValue() },
						EnumSituacionFinal.DESAPROBADO.getValue(), // REPITENCIA
						perUni.getId());

				situacionMatDAO.recuperacion5tdo6to(id_per,
						new Integer[] { EnumGrado.PRIMARIA_QUINTO.getValue(), EnumGrado.PRIMARIA_SEXTO.getValue() },
						EnumSituacionFinal.REQUIERE_RECUPERACION_PEDAGOGICA.getValue(), // RECUPERACION
						perUni.getId());

				situacionMatDAO.actualizarSituacionFinalNulos(id_per,
						new Integer[] { EnumGrado.PRIMARIA_QUINTO.getValue(), EnumGrado.PRIMARIA_SEXTO.getValue() },
						EnumSituacionFinal.APROBADO.getValue());

				/*
				 * Param param = new Param(); param.put("id_per", id_per);
				 * List<Aula> aulas = aulaDAO.listByParams(param, new
				 * String[]{"id"});
				 * 
				 * for (Aula aula : aulas) {
				 * 
				 * List<Row> notasAula =
				 * notaDAO.getPromedioGeneralAula(aula.getId(), perUni.getId());
				 * for (Row row : notasAula) {
				 * 
				 * } }
				 */
			}
			if (id_niv.equals(EnumNivel.SECUNDARIA.getValue())) {

				situacionMatDAO.limpiarSituacionFinal(id_per,
						new Integer[] { EnumGrado.SECUNDARIA_PRIMERO.getValue(),
								EnumGrado.SECUNDARIA_SEGUNDO.getValue(), EnumGrado.SECUNDARIA_TERCERO.getValue(),
								EnumGrado.SECUNDARIA_CUARTO.getValue(), EnumGrado.SECUNDARIA_QUINTO.getValue() });

				// repetir
				situacionMatDAO.repetir1ro5to(id_per, EnumSituacionFinal.DESAPROBADO.getValue()); // REPITENCIA
				// recuperacion
				situacionMatDAO.recuperacion1ro5to(id_per,
						EnumSituacionFinal.REQUIERE_RECUPERACION_PEDAGOGICA.getValue()); // RECUPERACION

				// aprobado
				situacionMatDAO.actualizarSituacionFinalNulos(id_per,
						new Integer[] { EnumGrado.SECUNDARIA_PRIMERO.getValue(),
								EnumGrado.SECUNDARIA_SEGUNDO.getValue(), EnumGrado.SECUNDARIA_TERCERO.getValue(),
								EnumGrado.SECUNDARIA_CUARTO.getValue(), EnumGrado.SECUNDARIA_QUINTO.getValue() },
						EnumSituacionFinal.APROBADO.getValue());
			}

			logger.debug("periodo a cerrar situacion:" + id_per);
			periodo.setFlag_sit("1");
			periodoDAO.saveOrUpdate(periodo);

		}

	}

	@Transactional
	public Integer actualizarSituacion(Integer id_mat, Integer id_sit) throws Exception {

		/*
		Param param = new Param();
		param.put("id_mat", id_mat);
		SitHistorial sitHistorial = sitHistorialDAO.getByParams(param);
		*/
 
		Matricula matricula = matriculaDAO.get(id_mat); 
		
		SitHistorial sitHistorial = new SitHistorial();
		sitHistorial.setId_mat(id_mat);
		sitHistorial.setId_sit(id_sit);
		sitHistorial.setEst("A");
		sitHistorialDAO.saveOrUpdate(sitHistorial);
		
		// TODO MODIFICAR
		Integer id = matriculaDAO.actualizarSituacion(id_mat, id_sit);

		
		return id;

	}
}
