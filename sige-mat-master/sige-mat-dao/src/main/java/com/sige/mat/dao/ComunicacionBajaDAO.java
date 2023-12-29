package com.sige.mat.dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.ComunicacionBajaDAOImpl;
import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad comunicacion_baja.
 * @author MV
 *
 */
@Repository
public class ComunicacionBajaDAO extends ComunicacionBajaDAOImpl{
	
	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	@Autowired
	private  SQLUtil sqlUtil;

	/**
	 * consultar los documentos para dar de baja ( boleta, nc y nd)
	 * @param fec_ini
	 * @param fec_fin
	 * @param id_suc
	 * @return
	 */
	public List<Row> consultarReporteCaja(String cliente, String alumno, Integer id_suc){
		
		String sql ="select t.* from (select DISTINCT mov.id, mov.nro_rec, case when mov.tipo='I' then 'BOLETA' when mov.tipo='N' then 'NOTA DE CREDITO' else '' end as doc, "
				+ "\n if (mov.tipo='I', mov.monto_total,mov.monto_total*-1  ) monto_total, "
				+ "\n IF(ISNULL(mov.id_mat), m.obs, concat( alu.ape_pat,' ' , alu.ape_mat, ', ', alu.nom) ) obs, con.nom as concepto, mov.tipo, "
				+ "\n mov.id_suc,g.nom nivel, gr.nom grado, au.secc, 0 mes, mov.fec fecha, b.est baja_est, b.motivo, b.id baja_id "
				+ "\n from fac_movimiento_detalle m "
				+ "\n inner join fac_movimiento mov on mov.id= m.id_fmo "
				+ "\n inner join fac_concepto con on con.id= m.id_fco "
				+ "\n left join ( mat_matricula mat join alu_alumno alu on alu.id = mat.id_alu )  on mov.id_mat = mat.id "
				+ "\n inner join alu_familiar f on f.id =mov.id_fam "
				+ "\n left join col_aula au on au.id = mat.id_au"
				+ "\n left join cat_grad gr on au.id_grad = gr.id"
				+ "\n left join per_periodo p on p.id = au.id_per "
				+ "\n left join ges_servicio g on g.id=p.id_srv "
				+ " left join fac_comunicacion_baja b on b.id_fmo=mov.id "
				+ "\n where mov.tipo in ('I','N') ";
		
		if(cliente!=null && !"".equals(cliente.trim()))
				sql += "\n and concat(f.ape_pat,' ' , f.ape_mat,' ' ,f.nom) like '%" + cliente.toUpperCase() + "%'";
		
		if(alumno!=null && !"".equals(alumno.trim()))
				sql += "\n and concat(alu.ape_pat,' ' , alu.ape_mat,' ' ,alu.nom) like '%" + alumno.toUpperCase() + "%'";
		
				sql += ")t ";
		
		if (id_suc!=null && id_suc!=0 )
			sql = sql + " where t.id_suc = " + id_suc;
		sql = sql + " order by 1,mes ";
		
		return sqlUtil.query(sql);
		
	}
	
	/**
	 * Actualiza la respuesta de la sunat en la comunicaci�n de baja
	 * @param id
	 * @param ticket
	 * @param id_eiv
	 * @param code
	 */
	public void actualizarCodRes(int id, String ticket, int id_eiv, String code, int id_fmo){
		
		String sql ="update fac_comunicacion_baja set ticket=? , id_eiv=?, code=?, fec_act=?, usr_act=? where id=? ";
		
		sqlUtil.update(sql, new Object[]{ticket, id_eiv, code,new Date(), tokenSeguridad.getId(),id});
		
		sql = "update fac_movimiento set est='B', fec_act=?, usr_act=?  where id=?";
		sqlUtil.update(sql, new Object[]{new Date(), tokenSeguridad.getId(), id_fmo});
		
	}

}
