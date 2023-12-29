package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.common.enums.EnumPerfil;
import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.MensajeriaFamiliarDAOImpl;
import com.tesla.colegio.model.MensajeriaFamiliar;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad mensajeria_familiar.
 * 
 * @author MV
 *
 */
@Repository
public class MensajeriaFamiliarDAO extends MensajeriaFamiliarDAOImpl {
	@Autowired
	private SQLUtil sqlUtil;

	public List<Row> mensajesPendientes() {

		String sql = "SELECT mmf.id, fam.cel, mmf.msj FROM  `msj_mensajeria_familiar` mmf "
				+ " inner join alu_familiar fam on fam.id= mmf.id_des" 
				+ " WHERE mmf.`flg_en`=0 AND mmf.est='A'  AND fam.cel IS NOT NULL AND LENGTH(fam.cel)=9 "
				+ " ORDER BY mmf.fec_ins LIMIT 10  ";

		return sqlUtil.query(sql);
	}

	public void actualizarLeido(String id) {
		String sql = "update msj_mensajeria_familiar set flg_en='1', fec_act= now() where id=" + id;
		sqlUtil.update(sql);
	}
	
	/**
	 * Valida si tiene mensajes pendientes de enviar al familiar
	 * @param id_fam
	 */
	public MensajeriaFamiliar ultimoMensajeEnviado(Integer id_fam){
		
		String sql = "select * from msj_mensajeria_familiar where date(fec_ins)=CURDATE() and id_per=? and id_des=?  order by fec_ins desc";
		
		List<MensajeriaFamiliar> list  = sqlUtil.query(sql, new Object []{EnumPerfil.PERFIL_FAMILIAR.getValue(), id_fam}, MensajeriaFamiliar.class);
		
		if(list.isEmpty())
			return null;
		else 
			return list.get(0);
		
	}
	
	public void actualizaEnviado(Integer id_msj, String flag) {
		String sql = "update msj_mensajeria_familiar set flg_en='"+flag+"', fec_act= now() where id=" + id_msj;
		sqlUtil.update(sql);
	}
}
