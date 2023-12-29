package com.sige.mat.web.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.util.FechaUtil;
import com.sige.common.enums.EnumTipoPeriodo;
import com.sige.mat.dao.CicloDAO;
import com.sige.mat.dao.CicloTurnoDAO;
import com.sige.mat.dao.NivelDAO;
import com.sige.mat.dao.PeriodoDAO;
import com.sige.mat.dao.TurnoDAO;
import com.tesla.colegio.model.Ciclo;
import com.tesla.colegio.model.CicloTurno;
import com.tesla.colegio.model.Periodo;


@RestController
@RequestMapping(value = "/api/periodo")
public class PeriodoRestController {
	
	@Autowired
	private PeriodoDAO periodoDAO;
	
	@Autowired
	private CicloDAO cicloDAO;
	
	@Autowired
	private CicloTurnoDAO cicloTurnoDAO;
	
	@Autowired
	private NivelDAO nivelDAO;
	
	@Autowired
	private TurnoDAO turnoDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(Integer id_anio, Integer id_gir, Integer id_suc) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		Param param = new Param();
		param.put("pee.id_anio", id_anio);
		if(id_gir!=null)
		param.put("ggn.id", id_gir);
		if(id_suc!=null)
		param.put("suc.id", id_suc);
		
		 
		result.setResult(periodoDAO.listFullByParams(param, new String[]{"pee.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarTiposPeriodo")
	public AjaxResponseBody listarNivelesCombo(Integer id_anio, Integer id_gir, Integer id_niv) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(periodoDAO.listarTiposPeriodoxGiro(id_anio, id_gir, id_niv));
		
		return result;
	}
	
	@RequestMapping(value = "/listarCiclos")
	public AjaxResponseBody listarCiclos(Ciclo ciclo, Integer id_per) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		ciclo.setId_per(id_per);
		 
		result.setResult(cicloDAO.listFullByParams( ciclo, new String[]{"cic.id"}) );
		
		return result;
	}
	
	@RequestMapping(value = "/listarCiclosCombo")
	public AjaxResponseBody listarCiclosCombo(Integer id_per) {

		AjaxResponseBody result = new AjaxResponseBody();
				 
		result.setResult(cicloDAO.listarCiclosCombo(id_per));
		
		return result;
	}
	
	@RequestMapping(value = "/listarCiclosxGiroNegocio")
	public AjaxResponseBody listarCiclosxGiroNegocio(Integer id_gir, Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();
				 
		result.setResult(cicloDAO.listarCiclosxGiroNegocio(id_gir, id_anio));
		
		return result;
	}
	
	@RequestMapping(value = "/listarCiclosxGiroNegocioTrabajador")
	public AjaxResponseBody listarCiclosxGiroNegocioAdministradorSede(Integer id_gir, Integer id_anio, Integer id_rol) {

		AjaxResponseBody result = new AjaxResponseBody();
				 
		result.setResult(cicloDAO.listarCiclosxGiroNegocioxTrabajador(id_gir, id_anio, id_rol));
		
		return result;
	}
	
	@RequestMapping(value = "/listarCiclosxGiroNegocioxTipPeriodo")
	public AjaxResponseBody listarCiclosxGiroNegocioxTipPeriodo(Integer id_gir, Integer id_anio, Integer id_tpe, Integer id_niv) {

		AjaxResponseBody result = new AjaxResponseBody();
				 
		result.setResult(cicloDAO.listarCiclosxGiroNegocioxPeriodo(id_gir, id_anio,id_tpe, id_niv));
		
		return result;
	}
	
	@RequestMapping(value = "/listarCiclosxAnio")
	public AjaxResponseBody listarCiclosxAnio(Integer id_anio, Integer id_gir, Integer id_niv, Integer id_tpe, Integer id_suc) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		 
		result.setResult(cicloDAO.listarCiclosxAnio(id_anio,id_gir, id_niv, id_tpe,id_suc));
		
		return result;
	}
	
	@RequestMapping(value = "/listarNivelesxGiroNegocio")
	public AjaxResponseBody listarNivelesxGiroNegocio(Integer id_gir, Integer id_suc) {

		AjaxResponseBody result = new AjaxResponseBody();
				 
		result.setResult(nivelDAO.listarNivelesxGiroNegocio(id_gir, id_suc));
		
		return result;
	}

	@Transactional
	@RequestMapping( value = "/grabarCiclo" ,method = RequestMethod.POST)
	public AjaxResponseBody grabar(Ciclo ciclo, String id_ctu[], Integer id_tur[], String hor_ini[], String hor_fin[]) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//Grabar Ciclo
			Integer id_cic=cicloDAO.saveOrUpdate(ciclo);
			
			//Obtenemos la evaluacion de la lista de aulas
			List<CicloTurno> ciclosTurno = new ArrayList<CicloTurno>();
			if (ciclo.getId()!=null){
				Param param = new Param();
				param.put("id_cic", id_cic);
				ciclosTurno = cicloTurnoDAO.listByParams(param, null);
			}
			
			//List<CicloTurno> ciclosTurnoInsert = new ArrayList<CicloTurno>();
			
			if(id_tur.length>0) {
				for (int i = 0; i < id_tur.length; i++) {
					if(ciclosTurno.size()>0) {
						if(i<=ciclosTurno.size()-1) {
							Integer id_ctu_ex=ciclosTurno.get(i).getId();
							if(id_ctu[i]!="") {
								if(Integer.parseInt(id_ctu[i])==id_ctu_ex) {
									//Actualizar los datos
									CicloTurno cicloTurno= new CicloTurno();
									cicloTurno.setId(Integer.parseInt(id_ctu[i]));
									cicloTurno.setId_cic(id_cic);
									cicloTurno.setId_tur(id_tur[i]);
									cicloTurno.setHor_ini(hor_ini[i]);
									cicloTurno.setHor_fin(hor_fin[i]);
									cicloTurno.setEst("A");
									cicloTurnoDAO.saveOrUpdate(cicloTurno);
									//ciclosTurnoInsert.add(e)
									
								} else {
									//Si no es igual desactivo
									CicloTurno cicloTurno= new CicloTurno();
									cicloTurno.setId(Integer.parseInt(id_ctu[i]));
									cicloTurno.setEst("I");
									cicloTurnoDAO.saveOrUpdate(cicloTurno);
								}	
							}
						} else {
							//Inserto los datos
							CicloTurno cicloTurno= new CicloTurno();
							cicloTurno.setId_cic(id_cic);
							cicloTurno.setId_tur(id_tur[i]);
							cicloTurno.setHor_ini(hor_ini[i]);
							cicloTurno.setHor_fin(hor_fin[i]);
							cicloTurno.setEst("A");
							cicloTurnoDAO.saveOrUpdate(cicloTurno);
						}
						
					} else {
						//Inserto los datos
						CicloTurno cicloTurno= new CicloTurno();
						cicloTurno.setId_cic(id_cic);
						cicloTurno.setId_tur(id_tur[i]);
						cicloTurno.setHor_ini(hor_ini[i]);
						cicloTurno.setHor_fin(hor_fin[i]);
						cicloTurno.setEst("A");
						cicloTurnoDAO.saveOrUpdate(cicloTurno);
					}
					
				}
			}
			
			if(ciclosTurno.size()>0) {
				int i=0;
				for (CicloTurno cicloTurno : ciclosTurno) {
					if(cicloTurno.getId_tur()!=id_tur[i]) {
						//Si no es igual desactiva
						cicloTurnoDAO.desactivarTurno(cicloTurno.getId(),cicloTurno.getId_tur());
					}	
				}
			}
			
			/*Map<String, Object> map = new HashMap<String,Object>();
			map.put("id_cic", id_cic);
			map.put("listaTurnos", value)*/
			result.setResult(id_cic);
			return result;
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Periodo periodo) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( periodoDAO.saveOrUpdate(periodo) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			periodoDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="eliminarCiclo/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminarCiclo(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			cicloTurnoDAO.delete(id);
			cicloDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( periodoDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="obtenerDatosCiclo/{id}", method = RequestMethod.GET)
	public AjaxResponseBody obtenerDatosCiclo(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(cicloDAO.get(id));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarTurnosxPeriodo", method = RequestMethod.GET)
	public AjaxResponseBody listarTurnosxServicio(Integer id_per ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(turnoDAO.listaTurnosxServicio(id_per));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarTurnosxCiclo", method = RequestMethod.GET)
	public AjaxResponseBody listarTurnosxCiclo(Integer id_cic ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(turnoDAO.listaTurnosxCiclo(id_cic));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarTurnosExistentesxCiclo", method = RequestMethod.GET)
	public AjaxResponseBody listarTurnosExistentesxCiclo(Integer id_cic ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult(turnoDAO.listaTurnosExCiclo(id_cic));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping(value = "/listarGeneracionUsrAlu")
	public AjaxResponseBody listarGenUsuAlu(Periodo periodo) {

		AjaxResponseBody result = new AjaxResponseBody();
		periodo.setId_tpe(EnumTipoPeriodo.ESCOLAR.getValue());

		result.setResult(periodoDAO.listFullByParams( periodo, new String[]{"pee.id"}) );
		
		return result;
	}
}
