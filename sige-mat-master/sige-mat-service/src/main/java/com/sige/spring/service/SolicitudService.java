package com.sige.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.SolicitudDAO;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Solicitud;
import com.tesla.frmk.sql.Param;

@Service
public class SolicitudService {

	@Autowired
	private SolicitudDAO solicitudDAO;

	@Autowired
	private SQLUtil sqlUtil;

	@Autowired
	private TokenSeguridad tokenSeguridad;

	/**
	 * Agrega y desactiva las solicitudes anteriores del alumno
	 * 
	 * @return
	 */
	@Transactional
	public Integer agregarSolicitud(Solicitud solicitud) {

		//Integer id_suc_ori_ant = null;
		if (solicitud.getTipo().equals("S")){
			//significa que hay una solicitud activa...
			Param param = new Param();
			param.put("est", "A");
			param.put("id_alu", solicitud.getId_alu());
			param.put("id_anio", solicitud.getId_anio());
			Solicitud solicitudAnterior = solicitudDAO.getByParams(param);
			solicitud.setTipo(solicitudAnterior.getTipo());//hAY q mantener el tipo de solicitud M, R o A
			
		}
		String sql = "update mat_solicitud set est='I', usr_act=?, fec_act=? where est='A' and id_alu=? and id_anio=?";

 
		//query para saber si ya se hizo una solicitud similar
		Param param = new Param();
		param.put("id_alu", solicitud.getId_alu());
		param.put("id_anio", solicitud.getId_anio());
		List<Solicitud> primeraSolictuds = solicitudDAO.listByParams(param, new String[]{"id asc"});
		

		
		//desactivando 
		sqlUtil.update(sql, new Object[] { tokenSeguridad.getId(),new java.util.Date() , solicitud.getId_alu(), solicitud.getId_anio()  });

		
		if (primeraSolictuds.size()>0 &&primeraSolictuds.get(0).getId_suc_or().equals(solicitud.getId_suc_des())){
			solicitud.setEst("I");//SE DESACTIVA POR Q EVIATARIA UN BUCLE SIN SENTIDO
		}
			return solicitudDAO.saveOrUpdate(solicitud);

		

	}

	public void desactivarSolicitudes( Integer id_alu, Integer id_anio){
		String sql = "update mat_solicitud set est='I', usr_act=?, fec_act=? where est='A' and id_alu=? and id_anio=?";
		sqlUtil.update(sql, new Object[] { tokenSeguridad.getId(),new java.util.Date() , id_alu, id_anio  });


	}
}
