package com.sige.mat.dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mysql.fabric.xmlrpc.base.Data;
import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.GrupoAulaVirtualDAOImpl;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad grupo_aula_virtual.
 * @author MV
 *
 */
@Repository
public class GrupoAulaVirtualDAO extends GrupoAulaVirtualDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listarGruposNoLlenos(Integer id_gra, Integer id_cgc, Integer id_anio) {
		
		String sql = "SELECT * FROM `cvi_grupo_aula_virtual` "
				+ " WHERE `id_gra`=? AND id_cgc=? AND id_anio=? AND lleno<>'1' "
				+ " ORDER BY nro ASC";
				
		return sqlUtil.query(sql, new Object[]{id_gra,id_cgc, id_anio});
	}
	
	public List<Row> listarGruposLlenos(Integer id_gra, Integer id_cgc, Integer id_anio) {
		
		String sql = "SELECT * FROM `cvi_grupo_aula_virtual` "
				+ " WHERE `id_gra`=? AND id_cgc=? AND id_anio=? AND lleno='1' "
				+ " ORDER BY nro ASC";
				
		return sqlUtil.query(sql, new Object[]{id_gra,id_cgc, id_anio});
	}
	
	public Integer inscritosGrupo(Integer id_cgr) {		
		Param param = new Param();
		param.put("id_cgr", id_cgr);
		String sql = "SELECT COUNT(*) inscritos FROM `cvi_grupo_alumno` cga WHERE id_cgr=:id_cgr and cga.est='A'";
		Integer inscritos=sqlUtil.queryForObject(sql, param, Integer.class);
		return inscritos;
	}
	
	public void actualizarEstadoGrupo(Integer id_cgr){
		String sql = "update cvi_grupo_aula_virtual set lleno=1, fec_act=? where id=?";
		sqlUtil.update(sql,new Object[]{new Date(), id_cgr});		
	}
	
	public void actualizarIdGrupo(String id_grupoClass,Integer id_cga){
		String sql = "update cvi_grupo_aula_virtual set id_grupoclass=? where id=?";
		sqlUtil.update(sql,new Object[]{id_grupoClass, id_cga});		
	}
	
	public List<Row> listarGruposAulaVirtual(Integer id_anio) {
		
		String sql = "SELECT cga.id id_cga, niv.nom nivel, gra.nom grado, CONCAT('GRUPO ',cga.`nro`) grupo, a.nom anio, cpv.abrev, gra.abrv_classroom, cga.`nro` "
				+ " FROM `cvi_grupo_aula_virtual` cga INNER JOIN `cat_grad` gra ON cga.`id_gra`=gra.id"
				+ " INNER JOIN `col_anio` a ON cga.`id_anio`=a.id"
				+ " INNER JOIN `cat_nivel` niv ON gra.id_nvl=niv.id"
				+ " INNER JOIN `cvi_periodo_aula_virtual` cpv ON cpv.id_anio=a.id AND cpv.id_niv=niv.id "
				+ " WHERE a.id=? and cga.id_grupoclass IS NULL";
				
		return sqlUtil.query(sql, new Object[]{id_anio});
	}
	
	public List<Row> listarGruposAulaVirtualTodos(Integer id_anio) {
		
		String sql = "SELECT cga.id id_cga, niv.nom nivel, gra.nom grado, CONCAT('GRUPO ',cga.`nro`) grupo, a.nom anio, cpv.abrev, gra.abrv_classroom, cga.`nro`, cga.id_grupoclass "
				+ " FROM `cvi_grupo_aula_virtual` cga INNER JOIN `cat_grad` gra ON cga.`id_gra`=gra.id"
				+ " INNER JOIN `col_anio` a ON cga.`id_anio`=a.id"
				+ " INNER JOIN `cat_nivel` niv ON gra.id_nvl=niv.id"
				+ " INNER JOIN `cvi_periodo_aula_virtual` cpv ON cpv.id_anio=a.id AND cpv.id_niv=niv.id "
				+ " WHERE a.id=? ";
				
		return sqlUtil.query(sql, new Object[]{id_anio});
	}
	
	public Row obtenerDatosGrupo(Integer id_cga) {
		
		String sql = "SELECT cga.id id_cga, niv.nom nivel, gra.nom grado, CONCAT('GRUPO ',cga.`nro`) grupo, a.nom anio, cpv.abrev, gra.abrv_classroom, cga.`nro`, cga.id_grupoclass "
				+ " FROM `cvi_grupo_aula_virtual` cga INNER JOIN `cat_grad` gra ON cga.`id_gra`=gra.id"
				+ " INNER JOIN `col_anio` a ON cga.`id_anio`=a.id"
				+ " INNER JOIN `cat_nivel` niv ON gra.id_nvl=niv.id"
				+ " INNER JOIN `cvi_periodo_aula_virtual` cpv ON cpv.id_anio=a.id AND cpv.id_niv=niv.id "
				+ " WHERE cga.id="+id_cga;
		
		List<Row> grupo = sqlUtil.query(sql);
		if (grupo.size()==0)
			return  null;
		else
			return grupo.get(0);
		
	}
	
	public List<Row> listarUsuariosxGrupo(Integer id_cga) {
		
		String sql = "SELECT alu.id_classRoom idUsuario" //cuc.`usr` usuario, 
				+ " FROM `cvi_grupo_aula_virtual` cgav INNER JOIN `cvi_grupo_alumno` cga ON cgav.id=cga.id_cgr"
				+ " INNER JOIN `cvi_inscripcion_campus` cic ON cga.`id_alu`=cic.`id_alu` AND cgav.`id_anio`=cic.`id_anio`"
				+ " INNER JOIN `cvi_usuario_campus` cuc ON cic.`id`=cuc.`id_cvic`"
				+ " INNER JOIN `mat_matricula` mat ON mat.`id_alu`=cga.`id_alu` "
				+ " INNER JOIN alu_alumno alu ON mat.id_alu=alu.id"
				+ " INNER JOIN `per_periodo` per ON mat.`id_per`=per.`id` AND cic.`id_anio`=per.`id_anio`"
				+ " WHERE cic.`id_anio`=4 AND (mat.`id_sit`<>'5' OR mat.`id_sit` IS NULL) AND cgav.`id`=?";
				
		return sqlUtil.query(sql, new Object[]{id_cga});
	}
	
	public List<Row> listarGrupos(Integer id_gra, Integer id_anio) {
		
		String sql = "SELECT id, CONCAT(des,' ',nro) as value FROM `cvi_grupo_aula_virtual` "
				+ " WHERE `id_gra`=? AND id_anio=?  "
				+ " ORDER BY nro ASC";
				
		return sqlUtil.query(sql, new Object[]{id_gra, id_anio});
	}
	
}
