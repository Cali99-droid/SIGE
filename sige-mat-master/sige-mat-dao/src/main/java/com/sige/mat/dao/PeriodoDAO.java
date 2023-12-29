package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.PeriodoDAOImpl;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Define m�todos DAO operations para la entidad periodo.
 * @author MV
 *
 */
@Repository
public class PeriodoDAO extends PeriodoDAOImpl{
	final static Logger logger = Logger.getLogger(PeriodoDAO.class);

	public List<Map<String,Object>> VerifPeriodo( String fec_ini, String fec_fin,String tip_per,String ser,String anio,String  fec_mat) {
		String sql = "SELECT * FROM per_periodo where date_format('%d/%m/%Y',fec_ini)='"+fec_ini+"' and date_format('%d/%m/%Y',fec_fin)='"+fec_fin+"' and id_tpe="+tip_per+"  and id_srv="+ser+" and id_anio="+anio+" and date_format('%d/%m/%Y',fec_cie_mat)='"+fec_mat+"'";
		//String sql = "SELECT * FROM per_periodo where  id_tpe="+id_tpe+"  and id_srv="+id_srv+" and id_anio="+anio;
		List<Map<String,Object>> Periodo = jdbcTemplate.queryForList(sql);			
		logger.info(sql);
		return Periodo;
	} 
	
	public List<Map<String,Object>> ListPeriodo() {
		//String q_aux="";
		//if(id != null || id != ""){
		//	q_aux="and anio.id="+id;
		//}
		String sql = "SELECT pee.id pee_id, pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , "
				+ " pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ,pee.est pee_est "
				+ " , anio.id anio_id  , anio.nom anio_nom"
				+ " , srv.id srv_id  , srv.id_suc srv_id_suc , srv.nom srv_nom"
				+ " , tpe.id tpe_id  , tpe.nom tpe_nom , tpe.des tpe_des"
				+ " , suc.nom nom_suc, suc.dir suc_dir, suc.tel suc_tel, suc.correo suc_cor"
				+ " FROM per_periodo pee"
				+ " JOIN col_anio anio ON anio.id = pee.id_anio "//+q_aux
				+ "  JOIN ges_servicio srv ON srv.id = pee.id_srv"
				+ " JOIN cat_tip_periodo tpe ON tpe.id = pee.id_tpe"
				+ " JOIN ges_sucursal suc ON suc.id=srv.id_suc "
				+ " ORDER BY anio.nom DESC,tpe.nom ASC,suc.nom DESC, srv.nom ASC";//TODO ordenar
		
		
		List<Map<String,Object>> PeriodoList = jdbcTemplate.queryForList(sql);
		
		return PeriodoList;
	} 
	
	public int actualizarSituacionFinal(Integer id_per){
		String sql= "update per_periodo set flag_sit='1', usr_act=1,fec_act=curdate() where id=" + id_per;
		return jdbcTemplate.update(sql);
		
	}
	
	public List<Row> listarTiposPeriodoxGiro(Integer id_anio, Integer id_gir, Integer id_niv) {
		String sql = "SELECT DISTINCT tpe.`id`, tpe.`nom` value\n" ; 
				sql += "FROM `per_periodo` per INNER JOIN `ges_servicio` srv ON per.`id_srv`=srv.`id`\n" ; 
				sql += "INNER JOIN `ges_giro_negocio` gir ON srv.`id_gir`=gir.`id`\n" ; 
				sql += "INNER JOIN `cat_tip_periodo` tpe ON per.`id_tpe`=tpe.`id`\n" ; 
				sql += "WHERE per.`id_anio`=? AND gir.`id`=?";
				if(id_niv!=null) {
					sql +=" AND per.`id_niv`="+id_niv;
				}
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql,new Object[]{id_anio,id_gir}));
	}
	
}
