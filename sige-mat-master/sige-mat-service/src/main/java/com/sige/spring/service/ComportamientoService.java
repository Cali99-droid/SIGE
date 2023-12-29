package com.sige.spring.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.common.enums.EnumTipoSesion;
import com.sige.mat.dao.CapComDAO;
import com.sige.mat.dao.ComportamientoDAO;
import com.sige.rest.request.ComportamientoReq;
import com.sige.rest.request.NotaComAlumnoReq;
import com.sige.rest.request.SemanaRq;
import com.tesla.colegio.model.CapCom;
import com.tesla.colegio.model.Comportamiento;
import com.tesla.frmk.common.exceptions.ServiceException;
import com.tesla.frmk.sql.Row;


@Service
public class ComportamientoService {
		
	@Autowired
	private ComportamientoDAO comportamientoDAO;
	
	@Autowired
	private CapComDAO capComDAO;
	/**
	 * Grabar notas de compprtamiento
	 * @param comportamientoReq
	 * @return
	 */
	@Transactional
	public void grabar(ComportamientoReq comportamientoReq) throws ServiceException{
		
		Map<Integer,BigDecimal> peso = new HashMap<Integer,BigDecimal>();
		BigDecimal tota_peso = new BigDecimal(0);
		 
		for(int i=0; i<comportamientoReq.getId_cap().length; i++){
			Integer id_cap =comportamientoReq.getId_cap()[i];
			List<Row> pesos = comportamientoDAO.obtenerPeso(id_cap);
			
			//logger.debug("ind:" + id_ind);
			
			if(pesos==null || pesos.size()!=1){
				throw new ServiceException("La capacidad con codigo:" + id_cap + " no tiene un peso correspondiente");
			}
			String peso_string = pesos.get(0).get("peso").toString();
			BigDecimal peso_cap = new BigDecimal(peso_string);
			tota_peso = tota_peso.add( peso_cap);
			peso.put(id_cap, peso_cap);
		}
		for (NotaComAlumnoReq notaComAlumnoReq : comportamientoReq.getNotaComAlumno()) {
			Comportamiento comportamiento = new Comportamiento();
			comportamiento.setId_tra(comportamientoReq.getId_tra());
			comportamiento.setId_alu(notaComAlumnoReq.getId_alu());
			comportamiento.setId_au(comportamientoReq.getId_au());
			comportamiento.setId_cpu(comportamientoReq.getId_cpu());
			comportamiento.setEst("A");
			//aun ver como sera el promedio
			comportamiento.setProm(new BigDecimal(0));;
			Integer id_comp=comportamientoDAO.saveOrUpdate(comportamiento);
			comportamiento.setId(id_comp);
			BigDecimal total = new BigDecimal(0);
			
			for(int i=0; i<comportamientoReq.getId_cap().length; i++){
				Integer notaComportamiento_nota = notaComAlumnoReq.getNotas()[i];
				if(notaComportamiento_nota==null)
					notaComportamiento_nota  = 0;
				total =  total.add( new BigDecimal( notaComportamiento_nota).multiply(peso.get(comportamientoReq.getId_cap()[i])));
				CapCom capCom= new CapCom();
				capCom.setId_nc(id_comp);
				capCom.setId_cap(comportamientoReq.getId_cap()[i]);
				capCom.setNota(notaComportamiento_nota);
				capCom.setFec(new Date());
				capCom.setEst("A");
				
				capComDAO.saveOrUpdate(capCom);
			}
			
			comportamiento.setProm(total.divide(tota_peso,2));				
			
			comportamientoDAO.actualizaPromedio(comportamiento);
		}

	}
	 	
}
