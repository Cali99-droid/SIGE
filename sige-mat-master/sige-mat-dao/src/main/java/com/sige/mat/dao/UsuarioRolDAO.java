package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.UsuarioRolDAOImpl;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad usuario_rol.
 * @author MV
 *
 */
@Repository
public class UsuarioRolDAO extends UsuarioRolDAOImpl{
	
    @Autowired
    private SQLUtil sqlUtil;
	
	public List<Row> listarUsuarioRolxContrato(String tip_con) {	
		String sql = "SELECT uro.id uro_id, uro.id_usr uro_id_usr , uro.id_rol uro_id_rol  ,uro.est uro_est \n" ; 
				sql +="		, usr.id usr_id  , usr.id_per usr_id_per , usr.login usr_login , usr.password usr_password  \n" ;
				sql +="		, rol.id rol_id  , rol.nom rol_nom  \n" ;
				sql +="		, tra.id per_id , per.nom per_nom, per.ape_pat per_ape_pat, per.ape_mat per_ape_mat\n" ; 
				sql +="		 FROM seg_usuario_rol uro\n" ; 
				sql +="		 INNER JOIN seg_usuario usr ON usr.id = uro.id_usr \n"; 
				sql +="		INNER JOIN seg_rol rol ON rol.id = uro.id_rol \n" ; 
				sql +="		 INNER JOIN ges_trabajador tra ON tra.id=usr.id_tra\n"; 
				sql +="		 INNER JOIN col_persona per ON tra.id_per=per.id\n"; 
				sql +="		 LEFT JOIN `rhh_contrato_trabajador` con ON tra.`id`=con.`id_tra`\n"; 
				sql +="		 WHERE 1=1\n" ;
				if(tip_con!=null) {
					if(tip_con.equals("1")) {
						sql +="AND  ((CURDATE() BETWEEN con.`fec_ini` AND con.`fec_fin` ) or con.con_indf=1) ";
					} else if(tip_con.equals("0")) {
						sql +=" AND CURDATE()>con.fec_fin " ;
					}
				} 
				
		// logger.info(sql);
		return sqlUtil.query(sql);

	}
}
