package com.sige.spring.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.mat.dao.AdjuntoDAO;
import com.sige.mat.dao.EstadoMensajeriaDAO;
import com.sige.mat.dao.HistorialDAO;
import com.sige.mat.dao.MensajeDAO;
import com.sige.mat.dao.MensajeriaFamiliarDAO;
import com.sige.mat.dao.ReceptorDAO;
import com.sige.mat.dao.UsuarioDAO;
import com.tesla.colegio.model.Adjunto;
import com.tesla.colegio.model.Historial;
import com.tesla.colegio.model.Mensaje;
import com.tesla.colegio.model.Receptor;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

@Service
public class MensajeriaService {

	Logger logger;

	@Autowired
	private MensajeriaFamiliarDAO mensajeriaFamiliarDAO;
	
	@Autowired
	private MensajeDAO mensajeDAO;
	
	@Autowired
	private AdjuntoDAO adjuntoDAO;
	
	@Autowired
	private EstadoMensajeriaDAO estadoMensajeriaDAO;
	
	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@Autowired
	private ReceptorDAO receptorDAO;
	
	@Autowired
	private HistorialDAO historialDAO;

	/**
	 * Mensajes pendientes
	 * 
	 * @return
	 */
	public List<Row> getMensajesPendientesFamiliar() {
		List<Row> mensajes = mensajeriaFamiliarDAO.mensajesPendientes();

		return mensajes;
	}

	@Transactional
	public void actalizarLeidos(String ids) {
		String arr[] = ids.split("\\|");

		for (int i = 0; i < arr.length; i++) {
			String id = arr[i];

			if (!id.equals("")) {
				mensajeriaFamiliarDAO.actualizarLeido(id);
			}
		}

	}
	
	@Transactional
	public void enviarMensaje(Mensaje mensaje, Adjunto adjunto, Integer id_rec[]) {
		//Insertamos el adjunto
		Integer id_adj= adjuntoDAO.saveOrUpdate(adjunto);
		//Insertamos el mensaje 
		mensaje.setId_adj(id_adj);
		mensaje.setFec_envio(new Date());
		Integer id_msj= mensajeDAO.saveOrUpdate(mensaje);
		
		//Insertamos a los detinatarios
		//Buscamos el ID del estado enviado
		Integer id_est= estadoMensajeriaDAO.getByParams(new Param("cod",Constante.ESTADO_MENSAJE_ENVIADO)).getId();
		for (int i = 0; i < id_rec.length; i++) {
			Receptor receptor = new Receptor();
			receptor.setId_usr(id_rec[i]);
			//Buscamos el perfil del usuario
			Integer id_per=usuarioDAO.getByParams(new Param("id",id_rec[i])).getId_per();
			receptor.setId_per(id_per);
			receptor.setId_est(id_est);
			receptor.setId_msj(id_msj);
			Integer id_receptor=receptorDAO.saveOrUpdate(receptor);
			
			//Insertamos en el historial
			Historial historial = new Historial();
			historial.setId_rec(id_receptor);
			historial.setId_est(id_est);
			historialDAO.saveOrUpdate(historial);
		}
		
		
	}
	
	@Transactional
	public void leerMensaje(Integer id_msj, Integer id_rec) {
		//Buscamos el estado de leido
		Integer id_est= estadoMensajeriaDAO.getByParams(new Param("cod",Constante.ESTADO_MENSAJE_LEIDO)).getId();
		
		//Actualizamos el estado del mensaje
		receptorDAO.actualizarEstadoMensaje(id_msj, id_rec, id_est);
		
		//Insertamos el el historial
		
		Historial historial = new Historial();
		historial.setId_est(id_est);
		historial.setId_rec(id_rec);
		historialDAO.saveOrUpdate(historial);
	}
	
	@Transactional
	public void archivarMensaje(Integer id_msj, Integer id_rec) {
		//Buscamos el estado de leido
		Integer id_est= estadoMensajeriaDAO.getByParams(new Param("cod",Constante.ESTADO_MENSAJE_ARCHIVADO)).getId();
		
		//Actualizamos el estado del mensaje
		receptorDAO.actualizarEstadoMensaje(id_msj, id_rec, id_est);
		
		//Insertamos el el historial
		
		Historial historial = new Historial();
		historial.setId_est(id_est);
		historial.setId_rec(id_rec);
		historialDAO.saveOrUpdate(historial);
	}
	
	@Transactional
	public void eliminarMensaje(Integer id_msj, Integer id_rec) {
		//Buscamos el estado de leido
		Integer id_est= estadoMensajeriaDAO.getByParams(new Param("cod",Constante.ESTADO_MENSAJE_ELIMINADO)).getId();
		
		//Actualizamos el estado del mensaje
		receptorDAO.actualizarEstadoMensaje(id_msj, id_rec, id_est);
		
		//Insertamos el el historial
		
		Historial historial = new Historial();
		historial.setId_est(id_est);
		historial.setId_rec(id_rec);
		historialDAO.saveOrUpdate(historial);
	}

}
