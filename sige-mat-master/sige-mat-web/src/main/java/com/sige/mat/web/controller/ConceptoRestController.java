package com.sige.mat.web.controller;
//package com.sige.spring.rest.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sige.core.dao.cache.CacheManager;
import com.sige.mat.dao.AcademicoPagoDAO;
import com.sige.mat.dao.ConceptoDAO;
import com.sige.mat.dao.ConfMensualidadDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Concepto;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Usuario;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;

@RestController
@RequestMapping(value = "/api/concepto")
public class ConceptoRestController {
	final static Logger logger = Logger.getLogger(ConceptoRestController.class);
	@Autowired
	private ConceptoDAO conceptoDAO;

	@Autowired
	private MatriculaDAO matriculaDAO;

	@Autowired
	private AcademicoPagoDAO academicoPagoDAO;

	@Autowired
	private ConfMensualidadDAO confMensualidadDAO;

	
	@Autowired
	private CacheManager cacheManager;
	
	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista() {

		AjaxResponseBody result = new AjaxResponseBody();

		result.setResult(conceptoDAO.listFullByParams(new Param(), new String[] { "fco.id" }));

		return result;

	}

	@RequestMapping(method = RequestMethod.POST)
	public AjaxResponseBody grabar(Concepto concepto) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			result.setResult(conceptoDAO.saveOrUpdate(concepto));
			cacheManager.update(Concepto.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			conceptoDAO.delete(id);
			cacheManager.update(Concepto.TABLA);
		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			result.setResult(conceptoDAO.get(id));
		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}

	@RequestMapping(value = "/anio/{id_anio}", method = RequestMethod.GET)
	public AjaxResponseBody pagosMensualidad(@PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();

		try {
			List<Matricula> matriculaList = matriculaDAO.listFullByParams(new Param("pee.id_anio", id_anio),
					new String[] { "mat.id" });

			for (int i = 0; i < matriculaList.size(); i++) {
				int id_mat = matriculaList.get(i).getId();
				BigDecimal monto = confMensualidadDAO.getByParams(new Param("id_per", matriculaList.get(i).getId_per()))
						.getMonto();
				Param param = new Param();
				param.put("id_mat", id_mat);
				param.put("tip", "MEN");
				logger.debug(i + "-" + id_mat);
				List<AcademicoPago> pagoList = academicoPagoDAO.listByParams(param, new String[] { "mens" });
				if (pagoList.size() > 0) {
					for (int j = 0; j < pagoList.size(); j++) {
						AcademicoPago mensualidad = pagoList.get(j);
						mensualidad.setMonto(monto);
						academicoPagoDAO.saveOrUpdate(mensualidad);
					}
				} else {
					for (int j = 3; j <= 12; j++) {
						AcademicoPago academicoPago = new AcademicoPago();
						academicoPago.setId_mat(id_mat);
						academicoPago.setTip("MEN");
						academicoPago.setMens(j);
						academicoPago.setMonto(monto);
						academicoPago.setCanc("0");
						academicoPago.setEst("A");
						academicoPago.setFec_ins(new Date());
						academicoPagoDAO.saveOrUpdate(academicoPago);
					}
				}

			}

		} catch (Exception e) {
			result.setException(e);
		}

		return result;

	}
	
	/*@RequestMapping( value="/listarHistorial", method = RequestMethod.GET)
	public AjaxResponseBody listarHistorial(Integer id_mat) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( reporte_conductualDAO.listarHistorial(id_mat));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}*/
}
