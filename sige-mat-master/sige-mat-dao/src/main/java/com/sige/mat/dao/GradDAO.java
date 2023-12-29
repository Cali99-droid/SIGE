package com.sige.mat.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.GradDAOImpl;
import com.tesla.colegio.model.bean.GradoCapacidad;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad grad.
 * @author MV
 *
 */
@Repository
public class GradDAO extends GradDAOImpl{
	final static Logger logger = Logger.getLogger(GradDAO.class);
	
	@Autowired
	private SQLUtil sqlUtil;

	public List<GradoCapacidad> listGradosCapacidad(Integer id_eva, Integer id_niv, String[] order) {

		String sql = "SELECT v.id id,g.nom, g.id id_grad,v.id_eva,v.`id_per`,v.`nro_vac`, v.`vac_ofe`, v.`post`";
	      sql= sql + " FROM `cat_grad` g LEFT JOIN `eva_vacante` v ON g.id=v.`id_grad` AND( v.`id_eva`="+id_eva+") ";
		  sql= sql	+ " WHERE g.`id_nvl`="+id_niv;

		logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<GradoCapacidad>() {

			
			public GradoCapacidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntityGradCap(rs,"");
			}

		});

	}	
	
	private GradoCapacidad rsToEntityGradCap(ResultSet rs,String alias) throws SQLException {
		GradoCapacidad grado = new GradoCapacidad();
		grado.setId(rs.getInt( alias + "id"));
		grado.setId_grad(rs.getInt( alias + "id_grad"));
		grado.setNom(rs.getString( alias + "nom"));
		grado.setNro_vac(rs.getString( alias + "nro_vac"));
		grado.setVac_ofe(rs.getString( alias + "vac_ofe"));
		grado.setPost(rs.getString( alias + "post"));
		//grado.setCapacidad(rs.getString( alias + "capacidad"));
		//grado.setMatriculados(rs.getString( alias + "matriculados"));
		//grado.setNom(rs.getString( alias + "nom"));
		//grad.setEst(rs.getString( alias + "est"));
								
		return grado;

	}
	
	public List<Row> listaTodosGrados(Integer id_niv) {
		
		String sql = "SELECT gra.id, gra.nom as value, nvl.nom nivel FROM `cat_grad` gra INNER JOIN cat_nivel nvl ON gra.id_nvl=nvl.id ";
				if(id_niv!=null) {
					sql= sql + " WHERE `id_nvl`="+id_niv;
				}
				
		logger.info(sql);
		return sqlUtil.query(sql);
	}
	
	public List<Row> listaTodosGradosxGiro(Integer id_niv, Integer id_gir) {
		
		String sql = "SELECT DISTINCT gra.id, gra.nom as value, nvl.nom nivel FROM `cat_grad` gra INNER JOIN cat_nivel nvl ON gra.id_nvl=nvl.id INNER JOIN ges_servicio srv ON srv.id_niv=nvl.id WHERE srv.id_gir="+id_gir;
				if(id_niv!=null) {
					sql= sql + " AND `id_nvl`="+id_niv;
				}
				
		logger.info(sql);
		return sqlUtil.query(sql);
	}
	
	public List<Row> listaGrados() {
		
		String sql = "SELECT gra.id id_gra, gra.`nom` grado, niv.id id_niv, niv.nom nivel, id_anio_usu_alu "
				+ " FROM cat_grad gra INNER JOIN cat_nivel niv ON gra.`id_nvl`=niv.`id`";
		return sqlUtil.query(sql);

	}
	
	public int actualizarGeneracionUsuario(Integer id_anio, Integer id_gra){
		String sql= "update cat_grad set id_anio_usu_alu="+id_anio+", usr_act=1,fec_act=curdate() where id=" + id_gra;
		return jdbcTemplate.update(sql);
		
	}

}
