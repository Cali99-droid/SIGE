package com.sige.mat.dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.NotaCreditoDAOImpl;
import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad nota_credito.
 * @author MV
 *
 */
@Repository
public class NotaCreditoDAO extends NotaCreditoDAOImpl{
	
	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	@Autowired
	private  SQLUtil sqlUtil;

	@Transactional
	public void actualizarCodRes(int id, String ticket, int id_eiv, String code,String nro_rec){
		
		String sql ="update fac_nota_credito set ticket=? , id_eiv=?, code=?, fec_act=?, usr_act=? where id=? ";
		
		sqlUtil.update(sql, new Object[]{ticket, id_eiv, code,id,new Date(), tokenSeguridad.getId()});
		
		//actualizar recibo
		String arr[] = nro_rec.split("-");
		String serie = arr[0];
		int correlativo = Integer.parseInt(arr[1]);
		
		sql ="update fac_conf_recibo set numero_nc=? where serie_nc=?";
		
		sqlUtil.update(sql, new Object[]{correlativo, serie});
	}
	
	@Transactional
	public void actualizarCorrelativoNC(String nro_rec){
				
		//actualizar recibo
		String arr[] = nro_rec.split("-");
		String serie = arr[0];
		int correlativo = Integer.parseInt(arr[1]);
		String sql = "";
		if(serie.equals("B014") || serie.equals("B017")) {
			sql ="update fac_conf_recibo_banco set numero_nc=? where serie_nc=?";
		} else {
			sql ="update fac_conf_recibo set numero_nc=? where serie_nc=?";
		}
		
		sqlUtil.update(sql, new Object[]{correlativo, serie});
	}
	
	
	public void actualizarMovimientoNC(Integer id, Integer id_fmo_nc){
		
		String sql ="update fac_nota_credito set id_fmo_nc=?, fec_act=? where id=? ";
		
		sqlUtil.update(sql, new Object[]{id_fmo_nc, new Date(), id});
		
	}
	
	public List<Row> listarNotasCreditos(Date fec_ini, Date fec_fin, Integer id_suc) {
		
		String sql = "SELECT nc.`id`, CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`) alumno, g.`nom` grado, niv.`nom` nivel, au.secc secc,  mov.`nro_rec`,mova.nro_rec nro_rec_afe, mov.`monto`, nc.`motivo`, nc.fec_emi ";
				sql += "\n FROM `fac_nota_credito` nc INNER JOIN `fac_movimiento` mov ON nc.`id_fmo_nc`=mov.`id`";
				sql += "\n INNER JOIN `fac_movimiento` mova ON nc.`id_fmo`=mova.`id`";
				sql += "\n INNER JOIN `mat_matricula`mat ON mova.`id_mat`= mat.`id`";
				sql += "\n INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`";
			    sql += "\n INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`";
			    sql += "\n INNER JOIN `cat_grad` g ON au.`id_grad`=g.`id`";
			    sql += "\n INNER JOIN `cat_nivel` niv ON g.`id_nvl`=niv.`id`";
			    sql += "\n WHERE nc.`fec_emi` between ? and ? ";
				if(id_suc!=null)
					sql += " and mova.id_suc="+id_suc;
				
		return sqlUtil.query(sql, new Object[] {fec_ini, fec_fin});

	}
}
