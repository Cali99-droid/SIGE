package com.sige.mat.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.OpcionDAOImpl;
import com.tesla.colegio.model.Opcion;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad opcion.
 * 
 * @author MV
 *
 */
@Repository
public class OpcionDAO extends OpcionDAOImpl {

	@Autowired
	private SQLUtil sqlUtil;

	/**
	 * Mostrar el menu con left join roles
	 * 
	 * @return
	 */
	public List<Row> getListMenuRol(Integer id_rpo) {

		String sql = "select r.id id_rop, o.* from seg_opcion o left join seg_rol_opcion r on r.id_rol=? and r.id_opc = o.id";
		return sqlUtil.query(sql, new Object[] { id_rpo });
	}

	public List<Opcion> getListUltimoNivel() {

		String sql = "select * from seg_opcion where est='A' and url!='#'";
		return sqlUtil.query(sql, Opcion.class);
	}


	public List<Opcion> getListAplicacionesXusuario(Integer id_usr) {

		String sql = "\r\n" + 
				"SELECT op.* FROM seg_rol_opcion ro \r\n" + 
				"INNER JOIN seg_opcion op  ON op.`id` = ro.`id_opc`\r\n" + 
				"INNER JOIN seg_usuario_rol ur  ON ur.`id_rol` = ro.`id_rol`\r\n" + 
				"INNER JOIN seg_usuario  us  ON us.`id` = ur.`id_usr`\r\n" + 
				"WHERE us.id=? AND op.id_opc IS NULL";
		return sqlUtil.query(sql,new Object[] {id_usr} ,Opcion.class);
	}

	public List<Opcion> getListOpcionesXusuario(Integer id_usr) {

		String sql = "\r\n" + 
				"SELECT op.* FROM seg_rol_opcion ro \r\n" + 
				"INNER JOIN seg_opcion op  ON op.`id` = ro.`id_opc`\r\n" + 
				"INNER JOIN seg_usuario_rol ur  ON ur.`id_rol` = ro.`id_rol`\r\n" + 
				"INNER JOIN seg_usuario  us  ON us.`id` = ur.`id_usr`\r\n" + 
				"WHERE us.id=? AND op.id_opc IS NULL";
		return sqlUtil.query(sql,new Object[] {id_usr} ,Opcion.class);
	}
	
	/**
	 * Para obtener el arbol del menu en el dasboard
	 * 
	 * @return
	 */
	public List<Opcion> getListMenuDashBoard(Integer id_rol,Integer id_padre) {
		String sql = "select o.id o_id, o.id_opc o_id_opc, o.icon o_icon, o.nom o_nom , o.url o_url, o.titulo o_titulo, o.subtitulo o_subtitulo, o.orden o_orden, o.est o_est,"
				+ "\n padre.id padre_id, padre.id_opc padre_id_opc, padre.icon padre_icon, padre.nom padre_nom , padre.url padre_url, padre.titulo padre_titulo, padre.subtitulo padre_subtitulo, padre.orden padre_orden ,padre.est padre_est,"
				+ "\n abuelo.id abuelo_id, abuelo.id_opc abuelo_id_opc, abuelo.icon abuelo_icon, abuelo.nom abuelo_nom, abuelo.url abuelo_url, abuelo.titulo abuelo_titulo, abuelo.subtitulo abuelo_subtitulo, abuelo.orden abuelo_orden, abuelo.est abuelo_est  "
				+ "\n from seg_opcion o " 
				+ "\n inner join seg_rol_opcion ro on ro.id_opc=o.id"
				+ "\n left join seg_opcion padre on padre.id = o.id_opc"
				+ "\n left join seg_opcion abuelo on abuelo.id = padre.id_opc" 
				+ "\n where o.est='A' and ro.id_rol=" + id_rol
				+ "\n order by o.id_opc,o.orden";

		return jdbcTemplate.query(sql, new RowMapper<Opcion>() {

			@Override
			public Opcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				Opcion opcion= rsToEntity(rs,"o_");
				if (opcion.getId_opc()!=null){
					opcion.setOpcion(rsToEntity(rs, "padre_"));
					if (opcion.getOpcion().getId_opc()!=null){
						opcion.getOpcion().setOpcion(rsToEntity(rs, "abuelo_"));
					}
				}
				return opcion;
			}
		});
	}

}
