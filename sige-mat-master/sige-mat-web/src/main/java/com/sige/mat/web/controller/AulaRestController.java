package com.sige.mat.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.common.enums.EnumGrado;
import com.sige.common.enums.EnumNivel;
import com.sige.common.enums.EnumTipoPeriodo;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.AulaDetalleDAO;
import com.sige.mat.dao.AulaModalidadDetDAO;
import com.sige.mat.dao.CicloDAO;
import com.sige.mat.dao.GiroNegocioDAO;
import com.sige.mat.dao.GradDAO;
import com.sige.mat.dao.ModalidadEstudioDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.TurnoAulaDAO;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.AreaAnio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.AulaDetalle;
import com.tesla.colegio.model.AulaModalidadDet;
import com.tesla.colegio.model.Ciclo;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.ModalidadEstudio;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.TurnoAula;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.util.JsonUtil;


@RestController
@RequestMapping(value = "/api/aula")
public class AulaRestController {
	
	@Autowired
	private AulaDAO aulaDAO;
	
	@Autowired
	private AulaDetalleDAO aulaDetalleDAO;
	
	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private GradDAO gradDAO;

	@Autowired
	private PeriodoDAO periodoDAO;
	
	@Autowired
	private TurnoAulaDAO turnoAulaDAO;
	
	@Autowired
	private GiroNegocioDAO giroNegocioDAO;
	
	@Autowired
	private CicloDAO cicloDAO;
	
	@Autowired
	private ModalidadEstudioDAO modalidadEstudioDAO;
	
	@Autowired
	private AulaModalidadDetDAO aulaModalidadDetDAO;
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/obtenerDatosDetalleAula/{id_au}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosDetalleAula(@PathVariable Integer id_au ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDetalleDAO.get(id_au));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/obtenerDatosAula/{id_au}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosAula(@PathVariable Integer id_au) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.obtenerDatosAula(id_au));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//elimnamos los turnos del aula
			turnoAulaDAO.delete(id);
			aulaDAO.delete(id);
			//cacheManager.update(AreaAnio.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="obtenerModalidadMes/{id}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerModalidadMes(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//elimnamos los turnos del aula
			turnoAulaDAO.delete(id);
			aulaDAO.delete(id);
			//cacheManager.update(AreaAnio.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping(value = "/listarModalidadesxAulaMes")
	public AjaxResponseBody listarTurnoxAula(Integer id_au) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		//if (id_cic!=null)
			result.setResult(aulaModalidadDetDAO.listarModalidadesxAulaxMes(id_au));
		//else
			//result.setResult(null);
		
		return result;
	}
	
	@RequestMapping(value = "/grabarModalidad", method = RequestMethod.POST)
	public AjaxResponseBody grabarModalidad(AulaModalidadDet aulaModalidadDet) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		//if (id_cic!=null)
			result.setResult(aulaModalidadDetDAO.saveOrUpdate(aulaModalidadDet));
		//else
			//result.setResult(null);
		
		return result;
	}
	
	@RequestMapping( value="/eliminarModalidad/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarModalidad(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//Buscamos si ya existen pensiones pagadas para matriculas en la modalidad y mes
			
			//cacheManager.update(AreaAnio.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Aula aula, Integer id_cit[], String id_atur[]) throws IOException {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//Verificar si es del tipo colegio
			/*Periodo periodo = periodoDAO.getByParams(new Param("id", aula.getId_per()));
			Integer id_gir= periodo.getServicio().getId_gir();
			if(id_gir!=null) {
				if(id_gir.equals(1)) {
					//Verificar si existe una modalidad configurad para el periodo correspondiente
					Param param = new Param();
					param.put("id_per", periodo.getId());
					param.put("id_cme", aula.getId_cme());
					ModalidadEstudio modalidadEstudio = modalidadEstudioDAO.getByParams(param);
					if(modalidadEstudio==null) {
						Map<String, Object> map = new HashMap<String,Object>();
						map.put("error", "No existe una configuración de la modalidad de pago para el siguiente Periodo y Modalidad.");
						result.setResult(map);
						//map.put("id", alumno.getId());
						//response.setContentType("application/json");
				        //response.getWriter().write(JsonUtil.toJson(map));
				        return result;		
					}
				}
			}*/
			
			
			Integer id_au= aulaDAO.saveOrUpdate(aula);
			
			//Obtenemos los turnos del aula
			List<TurnoAula> turnosAula = new ArrayList<TurnoAula>();
			if (aula.getId()!=null){
				Param param = new Param();
				param.put("id_au", id_au);
				turnosAula = turnoAulaDAO.listByParams(param, null);
			}
			
			if(id_cit.length>0) {
			 for (int i = 0; i < id_cit.length; i++) {
					if(turnosAula.size()>0) {
						Integer id_atur_ex=turnosAula.get(i).getId();
						if(Integer.parseInt(id_atur[i])!=id_atur_ex) {
							//Actualizar los datos
							//Si no es igual desactivo
							TurnoAula turnoAula= new TurnoAula();
							turnoAula.setId(Integer.parseInt(id_atur[i]));
							turnoAula.setEst("I");
							turnoAulaDAO.saveOrUpdate(turnoAula);
						} else {
							//Inserto los datos
							TurnoAula turnoAula= new TurnoAula();
							if(id_atur[i]!=null) {
								turnoAula.setId(Integer.parseInt(id_atur[i]));
							}	
							turnoAula.setId_au(id_au);
							turnoAula.setId_cit(id_cit[i]);
							turnoAula.setEst("A");
							turnoAulaDAO.saveOrUpdate(turnoAula);
						}
					} else {
						//Inserto los datos
						TurnoAula turnoAula= new TurnoAula();
						turnoAula.setId_au(id_au);
						turnoAula.setId_cit(id_cit[i]);
						turnoAula.setEst("A");
						turnoAulaDAO.saveOrUpdate(turnoAula);
					}
			 }
			} 
			
			if(turnosAula.size()>0) {
				int i=0;
				for (TurnoAula turnoAula : turnosAula) {
					if(turnoAula.getId_cit()!=id_cit[i]) {
						//Si no es igual desactiva
						turnoAulaDAO.desactivarTurnoAula(turnoAula.getId(),turnoAula.getId_cit());
					}	
				}
			}
			result.setResult(id_au);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/grabarAulaDetalle", method = RequestMethod.POST)
	public AjaxResponseBody grabarAulaDetalle(AulaDetalle aulaDetalle) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(aulaDetalleDAO.saveOrUpdate(aulaDetalle));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	/**
	 * Lista las aulas del siguiente periodo
	 * @param aula_especial
	 * @return
	 */
	@RequestMapping(value = "/siguientePeriodo")
	public AjaxResponseBody getLista(Integer id_au, Integer id_gra, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 

		Anio anio = anioDAO.get(id_anio);
		Param param = new Param();
		param.put("nom", Integer.parseInt( anio.getNom()) + 1);
		
		Anio anioSigueinte = anioDAO.getByParams(param);
		
		if (anioSigueinte==null  ){
			result.setCode("403");
			result.setMsg("No existe un a�o siguiente configurado");
			return result;
		}
			
			
		Aula aula = aulaDAO.get(id_au);
		
		//Obtenemos el ciclo academico
		//Ciclo ciclo = cicloDAO.get(aula.getId_cic());
		Periodo periodo = periodoDAO.get(aula.getId_per());
		//Periodo periodo = periodoDAO.get(ciclo.getId_per());
		
		//periodo siguiente
		param = new Param();
		param.put("id_anio", anioSigueinte.getId());
		//param.put("id_srv", periodo.getId_srv());
		param.put("id_suc", periodo.getId_suc() );
		
		if (aula.getId_grad().intValue() == EnumGrado.INICIAL_5_ANIOS.getValue())
			param.put("id_niv", EnumNivel.PRIMARIA.getValue());
		else if (aula.getId_grad().intValue() == EnumGrado.PRIMARIA_SEXTO.getValue())
			param.put("id_niv", EnumNivel.SECUNDARIA.getValue());
		else
			param.put("id_niv", periodo.getId_niv());
		
		param.put("id_tpe", EnumTipoPeriodo.ESCOLAR.getValue());
		
		Periodo periodoSiguiente = periodoDAO.getByParams(param);
		
		param = new Param();
		param.put("id_gra_ant", id_gra);
		Grad gradoSiguiente = gradDAO.getByParams(param);
		if (gradoSiguiente==null && id_gra.intValue() != EnumGrado.SECUNDARIA_QUINTO.getValue() ){
			result.setCode("403");
			result.setMsg("El grado sugerido no existe para el siguiente a�o");
			return result;
		}
		param = new Param();
		//param.put("id_per", periodoSiguiente.getId());
		//param.put("id_grad", gradoSiguiente.getId());
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		//AULAS DEL PROXIMO A�O APROBADO
		//map.put("aulas_aprobados", aulaDAO.listByParams(param, new String[]{"secc"}));
		map.put("aulas_aprobados", aulaDAO.listAulasTodos(anioSigueinte.getId(), gradoSiguiente.getId()));		
		
		//AULAS DEL PROXIMO A�O DESAPROBADO
		param = new Param();
		param.put("id_anio", anioSigueinte.getId());
		param.put("id_srv", periodo.getId_srv());
		param.put("id_suc", periodo.getId_suc() );
		param.put("id_niv", periodo.getId_niv());
		param.put("id_tpe", EnumTipoPeriodo.ESCOLAR.getValue());
		Periodo periodoDesaprobado= periodoDAO.getByParams(param);

		if(periodoDesaprobado!=null) {
			param = new Param();
			param.put("id_per", periodoDesaprobado.getId());
			param.put("id_grad", id_gra);
			map.put("aulas_desaprobados", aulaDAO.listByParams(param, new String[]{"secc"}));
		} else {
			map.put("aulas_desaprobados", 1);
		}
		
		//map.put("aulas_desaprobados", null);
		
		result.setResult(map);
		
		return result;
	}
	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getListAulas(Integer id_anio, Integer id_cic, Integer id_gra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 Param param = new Param();
		 param.put("pee.id_anio", id_anio);
		 param.put("cic.id", id_cic);
		 if(id_gra!=null) {
			 param.put("grad.id", id_gra);
		 }
		 
		result.setResult(aulaDAO.listFullByParams( param, new String[]{"grad.id_nvl"," grad.id","aula.secc"}) );
		
		return result;
	}
	
	@RequestMapping( value="/listarAulas", method = RequestMethod.GET)
	public AjaxResponseBody listarAulas(Integer id_suc, Integer id_tra, Integer id_anio, Integer id_gra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.listAulasxTutoryLocal(id_suc, id_tra, id_anio,id_gra));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAulasxNivelGradoSucursal", method = RequestMethod.GET)
	public AjaxResponseBody listarAulasxNivelGradoSucursal(Integer id_anio,Integer id_niv, Integer id_gra, Integer id_suc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.listarAulasxNivelGradoLocal(id_anio, id_niv, id_gra, id_suc));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAulasxNivelGrado", method = RequestMethod.GET)
	public AjaxResponseBody listarAulasxNivelGrado(Integer id_anio,Integer id_niv, Integer id_gra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.listarAulasxNivelGrado(id_anio, id_niv, id_gra));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping(value = "/listarGiroNegocio")
	public AjaxResponseBody listarNivelesCombo() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(giroNegocioDAO.listarGiroNegocio());
		
		return result;
	}
	
	@RequestMapping( value="/listarAulasxCicloTurnoGrado", method = RequestMethod.GET)
	public AjaxResponseBody listarAulasxCicloTurnoGrado(Integer id_cic, Integer id_grad, Integer id_tur, Integer id_au) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.listAulasXCicloTurnoGrado(id_cic, id_grad, id_tur, id_au));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAulasxCicloTurnoGradoDocente", method = RequestMethod.GET)
	public AjaxResponseBody listarAulasxCicloTurnoGradoTrabajador(Integer id_cic, Integer id_grad, Integer id_tur, Integer id_au, Integer id_tra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.listAulasXCicloTurnoGradoDocente(id_cic, id_grad, id_tur, id_au, id_tra));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAulasxGradoLocal", method = RequestMethod.GET)
	public AjaxResponseBody listarAulasxGiro(Integer id_suc, Integer id_gra, Integer id_anio, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.listAulasxGradoLocal(id_anio, id_gra, id_suc, id_gir));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarGradosxTurnoCiclo", method = RequestMethod.GET)
	public AjaxResponseBody listarGradosxTurno(Integer id_cic, Integer id_tur) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.listGradosxCicloTurno(id_cic, id_tur));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/seccionAnterior", method = RequestMethod.GET)
	public AjaxResponseBody listarSecionAnterior(Integer id_anio, Integer id_grad, Integer id_suc) {

		AjaxResponseBody result = new AjaxResponseBody();
		 Anio anio = anioDAO.getByParams(new Param("id",id_anio));
		 Integer anio_nom = Integer.parseInt(anio.getNom())-1;
		 Anio anio_ant = anioDAO.getByParams(new Param("nom",anio_nom));
		 Integer id_anio_ant= anio_ant.getId();
		 Integer id_gra_ant=id_grad-1;
		try {
			if(id_suc!=null)
				result.setResult( aulaDAO.seccAnt(id_anio_ant, id_gra_ant, id_suc));
			else 
				result.setResult(null);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/generarDescripcion/{id_au}", method = RequestMethod.GET)
	public AjaxResponseBody listarAulas(@PathVariable Integer id_au) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.generarDescripcionAula(id_au));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAulasxDocente", method = RequestMethod.GET)
	public AjaxResponseBody listarAulasxDocente(Integer id_anio, Integer id_tra, Integer id_gir ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.listAulasxDocente(id_anio, id_tra, id_gir));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAulasxCoordinadorNivel", method = RequestMethod.GET)
	public AjaxResponseBody listarAulasxCoordinadorNivel(Integer id_anio, Integer id_tra, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.listAulasxCoordinadorNivel(id_anio, id_tra, id_gir));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAulasxCoordinadorArea", method = RequestMethod.GET)
	public AjaxResponseBody listarAulasxCoordinadorArea(Integer id_anio, Integer id_tra, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.listAulasxCoordinadorArea(id_anio, id_tra, id_gir));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAulasxCoordinadorGiro", method = RequestMethod.GET)
	public AjaxResponseBody listarAulasxCoordinadorGiro(Integer id_anio, Integer id_tra, Integer id_gir, Integer id_gra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.listAulasxCoordinadorxGiro(id_anio, id_tra, id_gir,id_gra));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listAulasxTutorxAnio", method = RequestMethod.GET)
	public AjaxResponseBody listAulasxTutorxAnio(Integer id_tra, Integer id_anio, Integer id_gir, Integer id_gra) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.listAulasxTutorxAnio(id_tra, id_anio, id_gir, id_gra));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarAulasxGiroNivelGrado", method = RequestMethod.GET)
	public AjaxResponseBody listarAulasxGiroNivelGrado(Integer id_anio, Integer id_gra, Integer id_gir) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.listarAulasxGiroNivelGrado(id_anio, id_gra, id_gir));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarModalidadesxLocalyGrado", method = RequestMethod.GET)
	public AjaxResponseBody listarModalidadesxLocalyGrado(Integer id_anio, Integer id_suc, Integer id_grad) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.listarModalidadesxLocalyGrado(id_anio, id_suc, id_grad));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarLocalesxNivelyAnio", method = RequestMethod.GET)
	public AjaxResponseBody listarLocalesxNivelyAnio(Integer id_anio, Integer id_niv) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.listarLocalesxNivel(id_niv, id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarLocalesxAnio", method = RequestMethod.GET)
	public AjaxResponseBody listarLocalesxAnio(Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( aulaDAO.listarLocalesxAnio(id_anio));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}

}
