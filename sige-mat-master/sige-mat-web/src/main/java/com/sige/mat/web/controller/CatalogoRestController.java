package com.sige.mat.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.AluDocumentosDAO;
import com.sige.mat.dao.CategoriaOcupacionalDAO;
import com.sige.mat.dao.CentralRiesgoDAO;
import com.sige.mat.dao.CentroSaludDAO;
import com.sige.mat.dao.ClienteDAO;
import com.sige.mat.dao.ColSituacionDAO;
import com.sige.mat.dao.ColegioDAO;
import com.sige.mat.dao.CondicionPerDAO;
import com.sige.mat.dao.DenominacionDAO;
import com.sige.mat.dao.EstCivilDAO;
import com.sige.mat.dao.GradoInstruccionDAO;
import com.sige.mat.dao.IdiomaDAO;
import com.sige.mat.dao.LineaCarreraDAO;
import com.sige.mat.dao.ModalidadEstudioDAO;
import com.sige.mat.dao.ModalidadTrabajoDAO;
import com.sige.mat.dao.MotivoBullingDAO;
import com.sige.mat.dao.NacionalidadDAO;
import com.sige.mat.dao.ParentescoDAO;
import com.sige.mat.dao.PeriodoAcaDAO;
import com.sige.mat.dao.PeriodoPruebaDAO;
import com.sige.mat.dao.ReligionDAO;
import com.sige.mat.dao.RemuneracionDAO;
import com.sige.mat.dao.TipFrecPagoDAO;
import com.sige.mat.dao.TipPeriodoDAO;
import com.sige.mat.dao.TipoDocumentoDAO;
import com.sige.mat.dao.TipoSeguroDAO;
import com.tesla.colegio.model.AluDocumentos;
import com.tesla.colegio.model.CategoriaOcupacional;
import com.tesla.colegio.model.CentralRiesgo;
import com.tesla.colegio.model.CentroSalud;
import com.tesla.colegio.model.Cliente;
import com.tesla.colegio.model.ColSituacion;
import com.tesla.colegio.model.Colegio;
import com.tesla.colegio.model.CondicionPer;
import com.tesla.colegio.model.Denominacion;
import com.tesla.colegio.model.EstCivil;
import com.tesla.colegio.model.GradoInstruccion;
import com.tesla.colegio.model.Idioma;
import com.tesla.colegio.model.LineaCarrera;
import com.tesla.colegio.model.ModalidadEstudio;
import com.tesla.colegio.model.ModalidadTrabajo;
import com.tesla.colegio.model.MotivoBulling;
import com.tesla.colegio.model.Nacionalidad;
import com.tesla.colegio.model.Parentesco;
import com.tesla.colegio.model.PeriodoAca;
import com.tesla.colegio.model.PeriodoPrueba;
import com.tesla.colegio.model.Religion;
import com.tesla.colegio.model.TipFrecPago;
import com.tesla.colegio.model.TipPeriodo;
import com.tesla.colegio.model.TipoDocumento;
import com.tesla.colegio.model.TipoSeguro;

@RestController
@RequestMapping(value = "/api/catalogo")
public class CatalogoRestController {
	
	@Autowired
	private AluDocumentosDAO aluDocumentosDAO;
	
	@Autowired
	private CentralRiesgoDAO centralRiesgoDAO;
	
	@Autowired
	private CentroSaludDAO centroSaludDAO;
	
	@Autowired
	private ClienteDAO clienteDAO;
	
	@Autowired
	private ColSituacionDAO colSituacionDAO;
	
	@Autowired
	private CondicionPerDAO condicionPerDAO;
	
	@Autowired
	private EstCivilDAO estCivilDAO;
	
	@Autowired
	private GradoInstruccionDAO gradoInstruccionDAO;
	
	@Autowired
	private IdiomaDAO idiomaDAO;
	
	@Autowired
	private MotivoBullingDAO motivoBullingDAO;
	
	@Autowired
	private NacionalidadDAO nacionalidadDAO;
	
	@Autowired
	private ParentescoDAO parentescoDAO;
	
	@Autowired
	private ReligionDAO religionDAO;
	
	@Autowired
	private PeriodoAcaDAO periodoAcaDAO;
	
	@Autowired
	private TipPeriodoDAO tipPeriodoDAO;
	
	@Autowired
	private TipoDocumentoDAO tipoDocumentoDAO;
	
	@Autowired
	private TipoSeguroDAO tipoSeguroDAO;
	
	@Autowired
	private ModalidadTrabajoDAO modalidadTrabajoDAO;
	
	@Autowired
	private CategoriaOcupacionalDAO categoriaOcupacionalDAO;
	
	@Autowired
	private PeriodoPruebaDAO periodoPruebaDAO;
	
	@Autowired
	private LineaCarreraDAO lineaCarreraDAO;
	
	@Autowired
	private DenominacionDAO denominacionDAO;
	
	@Autowired
	private RemuneracionDAO remuneracionDAO;
	
	@Autowired
	private TipFrecPagoDAO tipFrecPagoDAO;
	
	@Autowired
	private ModalidadEstudioDAO modalidadEstudioDAO;

	@Autowired
	private CacheManager cacheManager;
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(String cat) {

		AjaxResponseBody result = new AjaxResponseBody();
		
		if(cat.equals("doc_en")) {
			result.setResult(aluDocumentosDAO.list());
		} else if(cat.equals("cent_rie")) {
			result.setResult(centralRiesgoDAO.list());
		} else if(cat.equals("cent_sal")) {
			result.setResult(centroSaludDAO.list());
		} else if(cat.equals("tip_cli")) {
			result.setResult(clienteDAO.list());
		} else if(cat.equals("sit_aca_alu")) {
			result.setResult(colSituacionDAO.list());
		} else if(cat.equals("con_tra_per")) {
			result.setResult(condicionPerDAO.list());
		} else if(cat.equals("est_civil")) {
			result.setResult(estCivilDAO.list());
		} else if(cat.equals("grad_ins")) {
			result.setResult(gradoInstruccionDAO.list());
		} else if(cat.equals("idi")) {
			result.setResult(idiomaDAO.list());
		} else if(cat.equals("mot_bu")) {
			result.setResult(motivoBullingDAO.list());
		} else if(cat.equals("nacionalidad")) {
			result.setResult(nacionalidadDAO.list());
		} else if(cat.equals("parentesco")) {
			result.setResult(parentescoDAO.list());
		} else if(cat.equals("religion")) {
			result.setResult(religionDAO.list());
		} else if(cat.equals("tip_per_aca")) {
			result.setResult(periodoAcaDAO.list());
		} else if(cat.equals("tip_per")) {
			result.setResult(tipPeriodoDAO.list());
		} else if(cat.equals("tip_doc_ide")) {
			result.setResult(tipoDocumentoDAO.list());
		} else if(cat.equals("tip_seg")) {
			result.setResult(tipoSeguroDAO.list());
		} else if(cat.equals("mod_tra")) {
			result.setResult(modalidadTrabajoDAO.list());
		} else if(cat.equals("cat_ocu")) {
			result.setResult(categoriaOcupacionalDAO.list());
		} else if(cat.equals("per_prue")) {
			result.setResult(periodoPruebaDAO.list());
		} else if(cat.equals("lin_carr")) {
			result.setResult(lineaCarreraDAO.list());
		} else if(cat.equals("den")) {
			result.setResult(denominacionDAO.list());
		} else if(cat.equals("rem")) {
			result.setResult(remuneracionDAO.list());
		} else if(cat.equals("frec_pag")) {
			result.setResult(tipFrecPagoDAO.list());
		} else if(cat.equals("mod_est")) {
			result.setResult(modalidadEstudioDAO.list());
		} else {
			result.setResult(null);
		}
		
		return result;
	}

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(Integer id, String nom, String est, String cat) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if(cat.equals("doc_en")) {
				AluDocumentos aluDocumentos= new AluDocumentos();
				aluDocumentos.setId(id);
				aluDocumentos.setNom(nom);
				aluDocumentos.setEst(est);
				result.setResult(aluDocumentosDAO.saveOrUpdate(aluDocumentos));
			} else if(cat.equals("cent_rie")) {
				CentralRiesgo centralRiesgo= new CentralRiesgo();
				centralRiesgo.setId(id);
				centralRiesgo.setNom(nom);
				centralRiesgo.setEst(est);
				result.setResult(centralRiesgoDAO.saveOrUpdate(centralRiesgo));
			} else if(cat.equals("cent_sal")) {
				CentroSalud centralSalud = new CentroSalud();
				centralSalud.setId(id);
				centralSalud.setNom(nom);
				centralSalud.setEst(est);
				result.setResult(centroSaludDAO.saveOrUpdate(centralSalud));
			} else if(cat.equals("tip_cli")) {
				Cliente cliente = new Cliente();
				cliente.setId(id);
				cliente.setNom(nom);
				cliente.setEst(est);
				result.setResult(clienteDAO.saveOrUpdate(cliente));
			} else if(cat.equals("sit_aca_alu")) {
				ColSituacion colSituacion = new ColSituacion();
				colSituacion.setId(id);
				colSituacion.setNom(nom);
				colSituacion.setEst(est);
				result.setResult(colSituacionDAO.saveOrUpdate(colSituacion));
			} else if(cat.equals("con_tra_per")) {
				CondicionPer condicionPer = new CondicionPer();
				condicionPer.setId(id);
				condicionPer.setNom(nom);
				condicionPer.setEst(est);
				result.setResult(condicionPerDAO.saveOrUpdate(condicionPer));
			} else if(cat.equals("est_civil")) {
				EstCivil estCivil = new EstCivil();
				estCivil.setId(id);
				estCivil.setNom(nom);
				estCivil.setEst(est);
				result.setResult(estCivilDAO.saveOrUpdate(estCivil));
			} else if(cat.equals("grad_ins")) {
				GradoInstruccion gradoInstruccion= new GradoInstruccion();
				gradoInstruccion.setId(id);
				gradoInstruccion.setNom(nom);
				gradoInstruccion.setEst(est);
				result.setResult(gradoInstruccionDAO.saveOrUpdate(gradoInstruccion));
			} else if(cat.equals("idi")) {
				Idioma idioma = new Idioma();
				idioma.setId(id);
				idioma.setNom(nom);
				idioma.setEst(est);
				result.setResult(idiomaDAO.saveOrUpdate(idioma));
			} else if(cat.equals("mot_bu")) {
				MotivoBulling motivoBulling = new MotivoBulling();
				motivoBulling.setId(id);
				motivoBulling.setNom(nom);
				motivoBulling.setEst(est);
				result.setResult(motivoBullingDAO.saveOrUpdate(motivoBulling));
			} else if(cat.equals("nacionalidad")) {
				Nacionalidad nacionalidad = new Nacionalidad();
				nacionalidad.setId(id);
				nacionalidad.setNom(nom);
				nacionalidad.setEst(est);
				result.setResult(nacionalidadDAO.saveOrUpdate(nacionalidad));
			} else if(cat.equals("parentesco")) {
				Parentesco parentesco = new Parentesco();
				parentesco.setId(id);
				parentesco.setPar(nom);
				parentesco.setEst(est);
				result.setResult(parentescoDAO.saveOrUpdate(parentesco));
			} else if(cat.equals("religion")) {
				Religion religion = new Religion();
				religion.setId(id);
				religion.setNom(nom);
				religion.setEst(est);
				result.setResult(religionDAO.saveOrUpdate(religion));
			} else if(cat.equals("tip_per_aca")) {
				PeriodoAca periodoAca = new PeriodoAca();
				periodoAca.setId(id);
				periodoAca.setNom(nom);
				periodoAca.setEst(est);
				result.setResult(periodoAcaDAO.saveOrUpdate(periodoAca));
			} else if(cat.equals("tip_per")) {
				TipPeriodo tipPeriodo = new TipPeriodo();
				tipPeriodo.setId(id);
				tipPeriodo.setNom(nom);
				tipPeriodo.setEst(est);
				result.setResult(tipPeriodoDAO.saveOrUpdate(tipPeriodo));
			} else if(cat.equals("tip_doc_ide")) {
				TipoDocumento tipoDocumento = new TipoDocumento();
				tipoDocumento.setId(id);
				tipoDocumento.setNom(nom);
				tipoDocumento.setEst(est);
				result.setResult(tipoDocumentoDAO.saveOrUpdate(tipoDocumento));
			} else if(cat.equals("tip_seg")) {
				TipoSeguro tipoSeguro = new TipoSeguro();
				tipoSeguro.setId(id);
				tipoSeguro.setNom(nom);
				tipoSeguro.setEst(est);
				result.setResult(tipoSeguroDAO.saveOrUpdate(tipoSeguro));
			} else if(cat.equals("mod_tra")) {
				ModalidadTrabajo modalidadTrabajo = new ModalidadTrabajo();
				modalidadTrabajo.setId(id);
				modalidadTrabajo.setNom(nom);
				modalidadTrabajo.setEst(est);
				result.setResult(modalidadTrabajoDAO.saveOrUpdate(modalidadTrabajo));
			} else if(cat.equals("cat_ocu")) {
				CategoriaOcupacional categoriaOcupacional = new CategoriaOcupacional();
				categoriaOcupacional.setId(id);
				categoriaOcupacional.setNom(nom);
				categoriaOcupacional.setEst(est);
				result.setResult(categoriaOcupacionalDAO.saveOrUpdate(categoriaOcupacional));
			} else if(cat.equals("per_prue")) {
				PeriodoPrueba periodoPrueba = new PeriodoPrueba();
				periodoPrueba.setId(id);
				periodoPrueba.setNom(nom);
				periodoPrueba.setEst(est);
				result.setResult(periodoPruebaDAO.saveOrUpdate(periodoPrueba));
			} else if(cat.equals("lin_carr")) {
				LineaCarrera lineaCarrera = new LineaCarrera();
				lineaCarrera.setId(id);
				lineaCarrera.setNom(nom);
				lineaCarrera.setEst(est);
				result.setResult(lineaCarreraDAO.saveOrUpdate(lineaCarrera));
			} else if(cat.equals("den")) {
				Denominacion denominacion = new Denominacion();
				denominacion.setId(id);
				denominacion.setNom(nom);
				denominacion.setEst(est);
				result.setResult(denominacionDAO.saveOrUpdate(denominacion));
			} else if(cat.equals("frec_pag")) {
				TipFrecPago tipFrecPago = new TipFrecPago();
				tipFrecPago.setId(id);
				tipFrecPago.setNom(nom);
				tipFrecPago.setEst(est);
				result.setResult(tipFrecPagoDAO.saveOrUpdate(tipFrecPago));
			} else if(cat.equals("mod_est")) {
				ModalidadEstudio modalidadEstudio = new ModalidadEstudio();
				modalidadEstudio.setId(id);
				modalidadEstudio.setNom(nom);
				modalidadEstudio.setEst(est);
				result.setResult(modalidadEstudioDAO.saveOrUpdate(modalidadEstudio));
			} else {
				result.setResult(null);
			}
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			//colegioDAO.delete(id);
			cacheManager.update(Colegio.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}/{cat}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id, @PathVariable String cat ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			if(cat.equals("doc_en")) {
				result.setResult(aluDocumentosDAO.get(id));
			} else if(cat.equals("cent_rie")) {
				result.setResult(centralRiesgoDAO.get(id));
			} else if(cat.equals("cent_sal")) {
				result.setResult(centroSaludDAO.get(id));
			} else if(cat.equals("tip_cli")) {
				result.setResult(clienteDAO.get(id));
			} else if(cat.equals("sit_aca_alu")) {
				result.setResult(colSituacionDAO.get(id));
			} else if(cat.equals("con_tra_per")) {
				result.setResult(condicionPerDAO.get(id));
			} else if(cat.equals("est_civil")) {
				result.setResult(estCivilDAO.get(id));
			} else if(cat.equals("grad_ins")) {
				result.setResult(gradoInstruccionDAO.get(id));
			} else if(cat.equals("idi")) {
				result.setResult(idiomaDAO.get(id));
			} else if(cat.equals("mot_bu")) {
				result.setResult(motivoBullingDAO.get(id));
			} else if(cat.equals("nacionalidad")) {
				result.setResult(nacionalidadDAO.get(id));
			} else if(cat.equals("parentesco")) {
				result.setResult(parentescoDAO.get(id));
			} else if(cat.equals("religion")) {
				result.setResult(religionDAO.get(id));
			} else if(cat.equals("tip_per_aca")) {
				result.setResult(periodoAcaDAO.get(id));
			} else if(cat.equals("tip_per")) {
				result.setResult(tipPeriodoDAO.get(id));
			} else if(cat.equals("tip_doc_ide")) {
				result.setResult(tipoDocumentoDAO.get(id));
			} else if(cat.equals("tip_seg")) {
				result.setResult(tipoSeguroDAO.get(id));
			} else if(cat.equals("mod_tra")) {
				result.setResult(modalidadTrabajoDAO.get(id));
			} else if(cat.equals("cat_ocu")) {
				result.setResult(categoriaOcupacionalDAO.get(id));
			} else if(cat.equals("per_prue")) {
				result.setResult(periodoPruebaDAO.get(id));
			} else if(cat.equals("lin_carr")) {
				result.setResult(lineaCarreraDAO.get(id));
			} else if(cat.equals("den")) {
				result.setResult(denominacionDAO.get(id));
			} else if(cat.equals("frec_pag")) {
				result.setResult(tipFrecPagoDAO.get(id));
			} else if(cat.equals("mod_est")) {
				result.setResult(modalidadEstudioDAO.get(id));
			} else {
				result.setResult(null);
			}
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarcolegios", method = RequestMethod.GET)
	public AjaxResponseBody colegios() {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
				//result.setResult(colegioDAO.listColegios());
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
	@RequestMapping( value="/listarcolegiosConcurso", method = RequestMethod.GET)
	public AjaxResponseBody colegiosConcurso(String nivel) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			
			//result.setResult(colegioDAO.listColegiosConcurso(nivel));
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
	
}
