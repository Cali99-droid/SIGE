package com.sige.mat.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.UsuarioDAOImpl;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.FechaUtil;


/**
 * Define m�todos DAO operations para la entidad usuario.
 * @author MV
 *
 */
@Repository
public class UsuarioDAO extends UsuarioDAOImpl{
	final static Logger logger = Logger.getLogger(UsuarioDAO.class);

	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listaDatosUsuario() {
		String sql = "SELECT u.id id,  u.login aux1 , CONCAT(per.ape_pat,' ', per.ape_mat,' ', per.nom) value "
				+ " FROM seg_usuario u INNER JOIN ges_trabajador tra ON u.`id_tra`=tra.id"
				+ " INNER JOIN col_persona per on tra.id_per=per.id "
				+ " ORDER BY per.ape_pat, per.ape_mat, per.nom ";

		return sqlUtil.query(sql);
	}

	/**
	 * Lista los trabajadres con contrato vigente
	 * @param id_pue Puesto de trabajo ges_puesto_trabajador
	 * @return
	 */
	public List<Row> listaTrabajador(Integer id_pue) {
		String sql = "select distinct tra.id, concat(tra.ape_pat,' ',tra.ape_mat,', ',tra.nom) as value, con.id_pue aux1, tra.nro_doc aux2 "
				+ " from ges_trabajador tra " //inner join aeedu_asistencia.col_persona per ON tra.id_per=per.id
				//+ "\n inner join asi_contrato_trabajador con on con.id_tra= tra.id "
				+ "\n inner join rhh_contrato_trabajador con on con.id_tra= tra.id "
				+ "\n where tra.est='A' "
				+ "and (?=0 or con.id_pue=?) and ((? BETWEEN con.fec_ini and DATE_ADD( con.fec_fin,  INTERVAL 20 DAY )) OR con_indf=1) "
				+ "\n order by tra.ape_pat,tra.ape_mat,tra.nom";

		return sqlUtil.query(sql, new Object[]{id_pue,id_pue, FechaUtil.toStringMYQL(new Date())});
	}
	
	public void updatePassword(Integer id, String password){
		String sql = "update seg_usuario set password='" + password + "' where id=" + id;
		sqlUtil.update(sql);
		
	}
	public void actualizarEstado(Integer id,String est){
		String sql = "update seg_usuario set est='" + est + "', fec_act=curdate()  where id=" + id;
		sqlUtil.update(sql);
		
	}
	
	public List<Row> listaDatosTrabajador(Integer id_usr) {
		String sql = "SELECT usr.`id`, tra.id tra_id, CONCAT(tra.`ape_pat`,' ', tra.`ape_mat`,' ', tra.`nom`) trabajador, CONCAT('*****',SUBSTRING(tra.`nro_doc`,5,7)) dni, rol.`nom` rol"
				+ " FROM  `seg_usuario` usr INNER JOIN aeedu_asistencia.`ges_trabajador` tra ON usr.`id_tra`=tra.`id`"
				+ " INNER JOIN seg_usuario_rol sur ON usr.`id`=sur.`id_usr`"
				+ " INNER JOIN seg_rol rol ON sur.`id_rol`=rol.id"
				+ " WHERE tra.est='A' and usr.`id`=? ";

		return sqlUtil.query(sql, new Object[]{id_usr});
	}
	
	public List<Row> listarUsuariosDocentes() {
		String sql = "SELECT u.id id,  u.login, u.password , CONCAT(per.ape_pat,' ', per.ape_mat,' ', per.nom) trabajador, p.nom perfil, suc.nom sucursal "
				+ " FROM seg_usuario u INNER JOIN ges_trabajador tra ON u.`id_tra`=tra.id"
				+ " INNER JOIN seg_perfil p ON u.id_per=p.id"
				+ " INNER JOIN col_persona per on tra.id_per=per.id "
				+ " INNER JOIN seg_usuario_rol rol on u.id=rol.id_usr "
				+ " INNER JOIN seg_rol r on rol.id_rol=r.id "
				+ " LEFT JOIN ges_sucursal suc ON u.id_suc=suc.id "
				+ " WHERE r.nom='PROFESOR'"
				+ " ORDER BY per.ape_pat, per.ape_mat, per.nom ";

		return sqlUtil.query(sql);
	}
	
	public List<Row> listarLocales(Integer id_usr) {
		String sql = "SELECT suc.`id` , suc.`nom` value \n" + 
				"FROM `seg_usuario` usr INNER JOIN `ges_sucursal` suc ON usr.`id_suc`=suc.`id`\n" + 
				"WHERE usr.`id`=? ";

		return sqlUtil.query(sql, new Object[]{id_usr});
	}
	
}
