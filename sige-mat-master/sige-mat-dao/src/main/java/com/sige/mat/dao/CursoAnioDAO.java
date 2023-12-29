package com.sige.mat.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CursoAnioDAOImpl;
import com.tesla.colegio.model.Anio;
import com.tesla.frmk.common.exceptions.DAOException;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad curso_anio.
 * 
 * @author MV
 *
 */
@Repository
public class CursoAnioDAO extends CursoAnioDAOImpl {
	final static Logger logger = Logger.getLogger(CursoAnioDAO.class);

	@Autowired
	private SQLUtil sqlUtil;

	@Autowired
	private AnioDAO anioDAO;

	public List<Row> listaAreas(int id_niv, int id_anio) {

		String sql = "SELECT caa.id, ca.nom as value"
				+ " FROM col_area_anio caa INNER JOIN cat_area ca ON caa.id_area=ca.id " + " WHERE caa.id_niv=? and caa.id_anio=?";
		// logger.info(sql);
		return sqlUtil.query(sql, new Object[] { id_niv, id_anio });

	}

	public Row listaAnual(Integer id_niv, Integer id_gra, Integer id_anio) {

		Param param  = new Param();
		 
		String sql = "select cua.id cua_id, cua.id_per cua_id_per , cua.id_gra cua_id_gra , cua.id_caa cua_id_caa , cua.id_cur cua_id_cur , cua.peso cua_peso , cua.orden cua_orden , cua.flg_prom cua_flg_prom  ,cua.est cua_est ";
		sql = sql + "\n , per.id per_id  , per.id_anio per_id_anio , per.id_suc per_id_suc , per.id_niv per_id_niv , per.id_srv per_id_srv , per.id_tpe per_id_tpe , per.fec_ini per_fec_ini , per.fec_fin per_fec_fin , per.fec_cie_mat per_fec_cie_mat  ";
		sql = sql + "\n , gra.id gra_id  , gra.id_nvl gra_id_nvl, gra.nom gra_nom ";
		sql = sql + "\n , suc.id suc_id  , suc.nom suc_nom  ";
		sql = sql + "\n , niv.id niv_id  , niv.nom niv_nom  ";
		sql = sql + "\n , a.id id_a, a.nom a_nom ";
		sql = sql + "\n , caa.id caa_id  , caa.id_anio caa_id_anio , caa.id_niv caa_id_niv , caa.id_area caa_id_area , caa.ord caa_ord  ";
		sql = sql + "\n , cur.id cur_id  , cur.nom cur_nom ";
		sql = sql + "\n , ccs.id ccs_id, ccs.nro_ses ccs_nro_ses";
		sql = sql + "\n from col_curso_anio cua";
		sql = sql + "\n inner join per_periodo per on per.id = cua.id_per ";
		sql = sql + "\n inner join ges_sucursal suc on per.id_suc=suc.id ";
		sql = sql + "\n inner join cat_nivel niv on niv.id=per.id_niv ";
		sql = sql + "\n inner join cat_grad gra on gra.id = cua.id_gra ";
		sql = sql + "\n left join col_area_anio caa on caa.id = cua.id_caa ";
		sql = sql + "\n left join col_curso_sesion ccs on ccs.id_caa=caa.id and ccs.id_cur=cua.id_cur and ccs.id_gra=gra.id and ccs.id_niv=niv.id";
		sql = sql + "\n left join cat_area a on a.id=caa.id_area ";
		sql = sql + "\n left join cat_curso cur on cur.id = cua.id_cur where cua.est='A'";		
		
		if (id_niv !=null){
			param.put("id_niv", id_niv);
			sql = sql + "\n and per.id_niv = :id_niv";	
		}
		if (id_gra !=null){
			param.put("id_gra", id_gra);
			sql = sql + "\n and gra.id = :id_gra";	
		}
		if (id_anio !=null){
			param.put("id_anio", id_anio);
			sql = sql + "\n and per.id_anio = :id_anio";	
		}

		sql = sql + "\n order by suc.nom asc, niv.nom asc, gra.id asc, a.nom asc, caa.ord , cua.orden";
		
		List<Row> cursos = sqlUtil.query(sql,param);
		
		//verificar si el a�o esta vacio
		sql = "select count(1) cantidad from col_curso_anio a inner join per_periodo per on per.id = a.id_per where per.id_anio=?";
		
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
		
		String sql = "INSERT INTO col_curso_anio(id_per,id_caa,id_gra,id_cur,peso,orden,flg_prom,est,usr_ins,fec_ins)";
		sql = sql + "\n select per_act.id, caa_act.id, ca.id_gra, ca.id_cur, ca.peso, ca.orden, ca.flg_prom, 'A',1, now() from col_curso_anio ca";
		sql = sql + "\n inner join per_periodo per on per.id= ca.id_per";
		sql = sql + "\n inner join per_periodo per_act on per.id_srv= per_act.id_srv and per.id_suc= per_act.id_suc and per.id_niv = per_act.id_niv and per.id_tpe= per_act.id_tpe and per_act.id_anio=?";
		sql = sql + "\n inner join col_area_anio caa on caa.id=ca.id_caa";
		sql = sql + "\n inner join col_area_anio caa_act on caa_act.id_anio=? and caa_act.id_niv=caa.id_niv and caa_act.id_area=caa.id_area";
		sql = sql + "\n where per.id_anio=? and ca.est='A'";
		
		Integer registrosClonados = sqlUtil.update(sql, new Object[]{id_anio, id_anio, anioAnterior.getId()});

		if (registrosClonados.intValue() == 0 )
			throw new DAOException("No se clono el a�o anterior, posiblemente no hay areas configuradas para el a�o:" + anio.getNom());
		
		return registrosClonados;

	}
	
	
	public Row getAreaxCurso(Integer id_anio, Integer id_cur, Integer id_gra, Integer id_niv){
		String sql = " SELECT area.id, area.nom area FROM col_curso_anio  cca"
				+ "\n INNER JOIN per_periodo per ON per.id = cca.`id_per`"
				+ "\n INNER JOIN col_area_anio caa ON caa.id = cca.id_caa"
				+ "\n INNER JOIN cat_area area ON area.id = caa.id_area"
				+ "\n WHERE per.id_anio=? AND id_cur=? AND id_gra=? AND per.id_niv=?";
		
		List<Row> rows = sqlUtil.query(sql, new Object[]{id_anio, id_cur, id_gra, id_niv});
		
		if(rows.size()==0)
			return null;
		else 
			return rows.get(0);
		
	}
	
	public List<Row> listarCursosDCN(Integer id_caa){
		
		String sql = "SELECT cur.id, cur.nom curso, cua.peso, cua.orden, cua.id id_cua, cua.est\n" + 
				"FROM cat_curso cur \n" + 
				"LEFT JOIN `col_curso_anio` cua ON cua.`id_cur`=cur.id AND cua.`id_caa`=?";
		
		List<Row> cursos = sqlUtil.query(sql, new Object[]{id_caa});
		
		return cursos;

	}
	
	public List<Row> listarCursosporArea(Integer id_caa){
		
		String sql = "SELECT cur.id, cur.nom curso, cua.peso, cua.orden, cua.id id_cua, cua.est\n" + 
				"FROM cat_curso cur \n" + 
				"INNER JOIN `col_curso_anio` cua ON cua.`id_cur`=cur.id AND cua.`id_caa`=? AND cua.est='A' ";
		
		List<Row> cursos = sqlUtil.query(sql, new Object[]{id_caa});
		
		return cursos;

	}
	
public List<Row> listarCursosporAreaDc(Integer id_dcare, Integer id_gra, Integer id_gir, Integer id_anio){
		
		String sql = "SELECT cur.id, cur.nom curso, cua.peso, cua.orden, cua.id id_cua, cua.est\n" + 
				"FROM cat_curso cur \n" + 
				"INNER JOIN `col_curso_anio` cua ON cua.`id_cur`=cur.id "+
				" INNER JOIN col_area_anio caa ON cua.id_caa=caa.id "+
				" INNER JOIN aca_dcn_area dcare ON caa.id_adc=dcare.id "+
				" WHERE dcare.id=? AND caa.id_gra=? AND caa.id_gir=? AND caa.id_anio=? ";
		
		List<Row> cursos = sqlUtil.query(sql, new Object[]{id_dcare, id_gra, id_gir, id_anio});
		
		return cursos;

	}
	
   public List<Row> listarCursosporAreaxGradoCombo(Integer id_caa, Integer id_au, Integer id_tra){
		
		String sql = "SELECT cua.id, cur.nom value, cua.peso, cua.orden, cua.id id_cua, cua.est\n" ; 
				sql += "FROM cat_curso cur \n" ;
				sql += "INNER JOIN `col_curso_anio` cua ON cua.`id_cur`=cur.id AND cua.`id_caa`=?";
				sql += " INNER JOIN col_curso_aula cuau ON cuau.id_cua=cua.id AND cuau.id_caa=cua.id_caa";
				sql += " WHERE cuau.id_au=?";
				if(id_tra!=null) {
					sql += " AND cuau.id_tra="+id_tra;
				}
		
		List<Row> cursos = sqlUtil.query(sql, new Object[]{id_caa, id_au});
		
		return cursos;

	}
	
	public void desactivarCursoAnio(Integer id_cua){
		String sql = "update col_curso_anio set est='I' WHERE id="+id_cua; //and id_adc="+id_adc
		sqlUtil.update(sql);
		
	}
}
