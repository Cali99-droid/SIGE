package com.sige.mat.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CursoAulaDAOImpl;
import com.tesla.colegio.model.Anio;
import com.tesla.frmk.common.exceptions.DAOException;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad curso_aula.
 * @author MV
 *
 */
@Repository
public class CursoAulaDAO extends CursoAulaDAOImpl{
	final static Logger logger = Logger.getLogger(CursoAulaDAO.class);

	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private SQLUtil sqlUtil;
	public List<Row> listarTrabajador(){
		
		String sql = "SELECT t.`id`, CONCAT(p.`ape_pat`,' ',p.ape_mat,' ', p.nom) as value FROM `ges_trabajador` t "
				+ " INNER JOIN col_persona p ON t.`id_per`=p.`id` "
				+ " INNER JOIN seg_usuario u ON t.`id`=u.`id_tra` "
				+ " INNER JOIN seg_usuario_rol ur ON u.id=ur.`id_usr`"
			//	+ " INNER JOIN rhh_contrato_trabajador con ON con.id_tra=t.`id`"
				+ " WHERE ur.id_rol IN(6,7,11,12) and t.est='A' order by p.ape_pat, p.ape_mat";
		//logger.info(sql);
		return sqlUtil.query(sql);
	}	
	
	public List<Row> listarCursos(int id_niv, int id_gra, int id_anio, int id_suc){
		
		String sql = "SELECT a.id, c.nom as value FROM col_curso_anio a "
				+ " INNER JOIN cat_curso c ON a.id_cur = c.id "
				+ " INNER JOIN col_ciclo cic ON a.id_cic=cic.id "
				//+ " INNER JOIN per_periodo p ON a.id_per=p.id "
				+ " INNER JOIN per_periodo p ON cic.id_per=p.id "
				+ " WHERE a.est='A' AND p.id_niv="+id_niv+" AND a.id_gra="+id_gra+" AND p.id_anio="+id_anio+" AND p.id_suc="+id_suc
				+ " ORDER BY c.nom ";
		//logger.info(sql);
		return sqlUtil.query(sql);
	}
	
	public Row listaAnual(Integer id_niv, Integer id_gra, Integer id_anio) {

		Param param  = new Param();
		 
		String sql = "select cca.id cca_id, cca.id_cua cca_id_cua , cca.id_au cca_id_au , cca.id_tra cca_id_tra  ,cca.est cca_est ";
		sql = sql + ", cua.id cua_id  , cua.id_per cua_id_per , cua.id_gra cua_id_gra , cua.id_caa cua_id_caa , cua.id_cur cua_id_cur , cua.peso cua_peso , cua.orden cua_orden , cua.flg_prom cua_flg_prom  ";
		sql = sql + ", cur.id cur_id, cur.nom cur_nom ";
		sql = sql + ", per.id per_id, per.id_anio per_id_anio, per.id_suc per_id_suc ";
		sql = sql + ", gra.id gra_id, gra.nom gra_nom ";
		sql = sql + ", niv.id niv_id, niv.nom niv_nom ";
		sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
		sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql + " from col_curso_aula cca";
		sql = sql + " inner join col_curso_anio cua on cua.id = cca.id_cua ";
		sql = sql + " inner join col_aula au on au.id = cca.id_au ";
		sql = sql + " inner join aeedu_asistencia.ges_trabajador tra on tra.id = cca.id_tra and tra.est='A'";
		sql = sql + " inner join cat_curso cur on cua.id_cur=cur.id ";
		sql = sql + " inner join per_periodo per on cua.id_per=per.id ";
		sql = sql + " inner join cat_grad gra on cua.id_gra=gra.id and gra.id=au.id_grad ";
		sql = sql + " inner join col_area_anio area_anio on area_anio.id=cua.id_caa ";
		sql = sql + " inner join cat_nivel niv on per.id_niv=niv.id and cca.est='A'";
		
		if (id_niv !=null){
			param.put("id_niv", id_niv);
			sql = sql + " and per.id_niv = :id_niv";	
		}
		if (id_gra !=null){
			param.put("id_gra", id_gra);
			sql = sql + " and gra.id = :id_gra";	
		}
		if (id_anio !=null){
			param.put("id_anio", id_anio);
			sql = sql + " and per.id_anio = :id_anio";	
		}

		sql = sql + "\norder by niv.nom asc, gra.id asc, au.secc asc";
		
		List<Row> cursos = sqlUtil.query(sql,param);
		
		//verificar si el a�o esta vacio
		sql = "select count(1) cantidad from col_curso_aula a inner join col_curso_anio cua on cua.id = a.id_cua inner join per_periodo per on per.id = cua.id_per where per.id_anio=?";
		
		Integer cantidad = sqlUtil.queryForObject(sql, new Object[]{id_anio}, Integer.class);
		Row row = new Row();
		row.put("cursos", cursos);
		row.put("cantidad", cantidad);

		return row;

	}
	

	@Transactional
	public Integer clonarAnio(Integer id_anio) throws Exception{
		
		//consultar A�o anterior
		Anio anio = anioDAO.get(id_anio);
		Param param = new Param();
		param.put("nom", Integer.parseInt(anio.getNom()) -1 );
		
		Anio anioAnterior = anioDAO.getByParams(param);
		
		if (anioAnterior==null)
			throw new Exception("A�o anterior no existe");
		
		String sql = "INSERT INTO col_curso_aula(id_cua, id_au, id_tra,nro_ses,est,usr_ins,fec_ins)";
		sql = sql + "\n select cua_act.id, cca.id_au, cca.id_tra,cca.nro_ses,'A',1,now() ";
		sql = sql + "\n from  col_curso_aula cca";
		sql = sql + "\n inner join col_curso_anio cua on cua.id=cca.id_cua";
		sql = sql + "\n inner join per_periodo per on per.id= cua.id_per";
		sql = sql + "\n inner join per_periodo per_act on per.id_srv= per_act.id_srv and per.id_suc= per_act.id_suc and per.id_niv = per_act.id_niv and per.id_tpe= per_act.id_tpe and per_act.id_anio=?";
		sql = sql + "\n inner join col_curso_anio cua_act on per_act.id = cua_act.id_per and cua_act.id_gra = cua.id_gra and cua_act.id_cur= cua.id_cur";
		sql = sql + "\n where per.id_anio=?";
		
		Integer registrosClonados = sqlUtil.update(sql, new Object[]{id_anio, anioAnterior.getId()});
		
		if (registrosClonados.intValue()==0)
			throw new DAOException("No se clono el a�o anterior, posiblemente no hay cursos configuradas para el a�o:" + anio.getNom());
		
		return registrosClonados;

	}


	/**
	 * Lista los cursos por profesor
	 * @param id_tra
	 * @param id_anio
	 * @return
	 */
	public List<Row> listarCursosProfesor(int id_anio,int id_tra){
		
		String sql = "select distinct cur.id, n.id id_niv, n.nom nivel, g.id id_gra, g.nom grado, cur.nom curso "
				+ " from col_curso_aula ca "
				+ " inner join col_curso_anio cua on cua.id = ca.id_cua"
				+ " inner join cat_curso cur on cur.id = cua.id_cur"
				+ " inner join col_aula au on au.id = ca.id_au"
				+ " inner join per_periodo p on p.id = au.id_per"
				+ " inner join cat_nivel n on n.id = p.id_niv"
				+ " inner join cat_grad g on g.id = au.id_grad"
				+ " where p.id_anio=? and ca.id_tra=?";

		return sqlUtil.query(sql, new Object[]{id_anio, id_tra});
	}
	
	public void actualizarIdClassroom(String cod_classroom, Integer id_cca){
		String sql = "update col_curso_aula set cod_classroom=? where id=?";
		sqlUtil.update(sql,new Object[]{cod_classroom, id_cca});		
	}
	
	public void desactivarCursoAula(Integer id_cca){
		String sql = "update col_curso_aula set est='I' where id=?";
		sqlUtil.update(sql,new Object[]{id_cca});		
	}
	
	public void desactivarAula(Integer id_au){
		String sql = "update col_aula set est='I' where id=?";
		sqlUtil.update(sql,new Object[]{id_au});		
	}
	
	public List<Row>  listaCursosAulaxNivelAnio(Integer id_anio, Integer id_niv, Integer id_tpe, Integer id_gir, Integer id_grad, Integer id_au, Integer id_tra) {
		 
	/*	String sql = "SELECT DISTINCT CONCAT(niv.nom, ' - ', gra.nom,' ', au.secc) aula, are.nom area, IF(cua.id IS NULL,'',cur.nom) curso, ";
				sql += "CASE WHEN caul.id_cua IS NULL AND caul.id IS NOT NULL THEN caul.id_tra\n" ;
				sql += "WHEN caul.id_cua IS NOT NULL AND caul.id_cua=cua.id THEN caul.id_tra\n" ;
				sql += "ELSE NULL END AS id_tra,";
				sql += "CASE WHEN caul.id IS NOT NULL AND caul.id_cua=cua.id THEN caul.id";
				sql += " WHEN caul.id IS NOT NULL AND caul.id_cua IS NULL THEN caul.id";
				sql += " ELSE NULL END AS id_caul";
				sql += ", caa.id id_caa, cua.id id_cua, au.id id_au\n" ; // , caul.id_cua id_cua_caul
				sql += "FROM `col_area_anio` caa \n" ; 
				sql += "INNER JOIN cat_nivel niv ON caa.id_niv=niv.id\n" ;
				sql += " INNER JOIN `col_aula` au ON caa.`id_gra`=au.`id_grad`\n" ; 
				sql += " INNER JOIN cat_grad gra ON au.`id_grad`=gra.id AND gra.id_nvl=niv.id\n" ; 
				sql += " INNER JOIN cat_area are ON caa.id_area=are.id\n" ; 
				sql += " INNER JOIN `col_ciclo` cic ON au.`id_cic`=cic.`id`\n" ; 
				sql += " INNER JOIN per_periodo per ON cic.`id_per`=per.id AND caa.id_anio=per.id_anio AND per.id_niv=niv.id\n" ; 
				sql += " INNER JOIN ges_servicio srv ON per.id_srv=srv.id\n" ; 
				sql += " LEFT JOIN `col_curso_anio` cua ON cua.id_caa=caa.id\n"; 
				sql += " LEFT JOIN cat_curso cur ON cua.`id_cur`=cur.id\n" ; 
				sql += " LEFT JOIN col_curso_aula caul ON caul.id_au=au.`id` AND caul.`id_caa`=caa.id \n"; 
				sql += " LEFT JOIN `ges_trabajador` tra ON caul.id_tra=tra.id\n"; 
				sql += " WHERE caa.`id_anio`=? AND caa.`id_niv`=? AND per.id_tpe=? AND srv.id_gir=? \n"; 
				sql += "";*/
		String sql ="SELECT DISTINCT CONCAT(niv.nom, ' - ', gra.nom,' ', au.secc) aula, are.nom area, IF(cua.id IS NULL,'',cur.nom) curso , \n" ; 
				sql += "CASE WHEN\n" ;
				sql += "caul.id_cua IS NULL AND caul.id IS NOT NULL  THEN caul.id_tra\n" ; 
				sql += "WHEN caul.id_cua IS NOT NULL AND caul.id_cua=cua.id  THEN caul.id_tra\n" ; 
				sql += " END AS id_tra  , cur.id id_cur,\n" ;
				sql += "CASE \n" ; 
				sql += "WHEN caul.id IS NOT NULL AND caul.id_cua=cua.id THEN caul.id\n" ; 
				sql += "WHEN caul.id IS NOT NULL AND caul.id_cua IS NULL THEN caul.id\n" ; 
				sql += "ELSE NULL END AS id_caul, caa.id id_caa, cua.id id_cua , \n" ; 
				sql += "CASE WHEN \n" ; 
				sql += "caul.id_cua=cua.id THEN caul.id_cua \n" ; 
				sql += "ELSE NULL END AS id_cua_caul, au.id id_au\n" ; 
				sql += "FROM `col_area_anio` caa \n" ; 
				sql += "INNER JOIN cat_nivel niv ON caa.id_niv=niv.id\n" ; 
				sql += "INNER JOIN `col_aula` au ON caa.`id_gra`=au.`id_grad`\n" ; 
				sql += "INNER JOIN cat_grad gra ON au.`id_grad`=gra.id AND gra.id_nvl=niv.id\n" ; 
				sql += "INNER JOIN cat_area are ON caa.id_area=are.id\n" ; 
				sql += "INNER JOIN `col_ciclo` cic ON au.`id_cic`=cic.`id`\n" ; 
				sql += "INNER JOIN per_periodo per ON cic.`id_per`=per.id AND caa.id_anio=per.id_anio AND per.id_niv=niv.id\n" ; 
				sql += "INNER JOIN ges_servicio srv ON per.id_srv=srv.id AND caa.id_gir=srv.id_gir \n" ; 
				sql += "INNER JOIN aca_dcn_area ac ON caa.id_adc=ac.id AND ac.id_are=are.id \n";
				sql += "LEFT JOIN `col_curso_anio` cua ON cua.id_caa=caa.id\n" ; 
				sql += "LEFT JOIN cat_curso cur ON cua.`id_cur`=cur.id\n" ; 
				sql += "LEFT JOIN col_curso_aula caul ON  caul.id_au=au.`id` AND caul.`id_caa`=caa.id  \n" ; 
				sql += "LEFT JOIN ges_trabajador tra ON  caul.id_tra=tra.`id`  \n" ; 
				sql += "WHERE caa.`id_anio`=? AND caa.`id_niv`=? AND per.id_tpe=? AND srv.id_gir=?\n" ; 
		
		if (id_grad !=null){
			sql = sql + " and gra.id ="+id_grad;	
		}
		if (id_au !=null){
			sql = sql + " and au.id ="+id_au;	
		}
		if (id_tra !=null){
			sql = sql + " and tra.id ="+id_tra;	
		}

		sql = sql + " ORDER BY niv.id, gra.id, au.secc, are.nom, cur.nom";
		
		//List<Row> cursos_aula = sqlUtil.query(sql,new Object[]{id_anio, id_niv, id_tpe, id_gir});
		
		return sqlUtil.query(sql, new Object[]{id_anio, id_niv, id_tpe, id_gir});
		
		//verificar si el a�o esta vacio
		/*sql = "select count(1) cantidad from col_curso_aula a inner join col_curso_anio cua on cua.id = a.id_cua inner join per_periodo per on per.id = cua.id_per where per.id_anio=?";
		
		Integer cantidad = sqlUtil.queryForObject(sql, new Object[]{id_anio}, Integer.class);
		Row row = new Row();
		row.put("cursos", cursos);
		row.put("cantidad", cantidad);

		return row;*/

	}
	
}
