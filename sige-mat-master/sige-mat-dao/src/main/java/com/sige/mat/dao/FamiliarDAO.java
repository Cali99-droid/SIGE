package com.sige.mat.dao;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.FamiliarDAOImpl;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.GruFam;
import com.tesla.colegio.model.GruFamAlumno;
import com.tesla.colegio.model.GruFamFamiliar;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;
import com.tesla.frmk.util.StringUtil;


/**
 * Define m�todos DAO operations para la entidad familiar.
 * @author MV
 *
 */
@Repository
public class FamiliarDAO extends FamiliarDAOImpl{
	final static Logger logger = Logger.getLogger(FamiliarDAO.class);

	@Autowired
	private SQLUtil sqlUtil;
	
	@Autowired
	private GruFamFamiliarDAO gruFamFamiliarDAO;

	@Autowired
	private GruFamAlumnoDAO gruFamAlumnoDAO;

	/**
     * Actualizar foto
     * @param familiar
     * @return Id 
     */	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int updatePhoto(Integer idFam, InputStream inputStream)  {
		
		String sql = "update alu_familiar set foto= ? where id=" + idFam;
		
        try {

			LobHandler lobHandler = new DefaultLobHandler(); 

			
			
			jdbcTemplate.update( sql,
			         new Object[] {
			           //new SqlLobValue(inputStream, (int)image.length(), lobHandler),
			        	new SqlLobValue(inputStream, inputStream.available(), lobHandler),
			        		 
			         },
			         new int[] {Types.BLOB});
		
		
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException ie) {
			// TODO Auto-generated catch block
			ie.printStackTrace();
		}
		return 1;
	}
	
	

	/**
     * Actualizar foto
     * @param familiar
     * @return Id 
     */	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int updateHuella(Integer idFam, InputStream inputStream)  {
		
		String sql = "update alu_familiar set huella= ? where id=" + idFam;
        try {
			LobHandler lobHandler = new DefaultLobHandler(); 
			jdbcTemplate.update( sql,
			         new Object[] {
			        	new SqlLobValue(inputStream, inputStream.available(), lobHandler),
			         },new int[] {Types.BLOB});
		
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException ie) {
			// TODO Auto-generated catch block
			ie.printStackTrace();
		}
		return 1;
	}
		
    /**
     * Ver foto
     * @param familiar
     * @return Id 
     */	
	public byte[] getPhoto(Integer idFam) {
		String sql = "select foto from alu_familiar where id=" + idFam;
		
		Familiar fotoFamiliar = jdbcTemplate.query(sql, new ResultSetExtractor<Familiar>() {

			@Override
			public Familiar extractData(ResultSet rs) throws SQLException,DataAccessException {
                Familiar fam = new Familiar();
				if (rs.next()) {
					LobHandler lobHandler1 = new DefaultLobHandler();
	                byte[] requestData = lobHandler1.getBlobAsBytes(rs,"foto");
	                fam.setFoto(requestData);
	                return fam;
				}
				
				return fam;

			}
			
		});
		
		return fotoFamiliar.getFoto();
	}
	
	public List<Row> getFamiliaGrupo(String dniPadre, String dniMadre){
		
		String sql = "select g.id, p.ape_pat, m.ape_mat from alu_gru_fam g "
				+ " inner join alu_gru_fam_familiar gp on  gp.id_gpf = g.id "
				+ " inner join alu_familiar p on ( p.id = gp.id_fam and p.id_par=2 ) "
				+ " inner join alu_gru_fam_familiar gm on  gm.id_gpf = g.id "
				+ " inner join alu_familiar m on  (m.id = gm.id_fam and m.id_par=1)"
				+ " where p.nro_doc='" + dniPadre + "' "
				+ " and m.nro_doc='" + dniMadre +"'";
		logger.info("getFamiliaGrupo:" + sql);
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
			return SQLFrmkUtil.listToRows(list);
	}
	
	public List<Row> getHijosxPadreList(Integer id_fam, Integer id_alu){
		
		String sql = "select alu.* from alu_gru_fam g"
				+ " inner join alu_gru_fam_familiar gp on  gp.id_gpf = g.id"
				+ " inner join alu_gru_fam_alumno ga on ga.id_gpf = g.id"
				+ " inner join alu_familiar p on  p.id = gp.id_fam"
				+ " inner join alu_alumno alu on  alu.id = ga.id_alu"
				+ " where p.id=" + id_fam + " and g.id in  (select id_gpf from alu_gru_fam_alumno where id_alu="  + id_alu + ")";
		
		logger.info("getFamiliaGrupo:" + sql);
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return SQLFrmkUtil.listToRows(list);
	}	
	
	public List<Row> Cantidad_Fam(){
		
		String sql = "SELECT COUNT(*) cantidad FROM alu_familiar";
		
		logger.info("Cantidad:" + sql);
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return SQLFrmkUtil.listToRows(list);
	}
	
	public Row obtenerUltimoCodigodeFamilia(){
		
		String sql = "SELECT MAX(cod) codigo FROM `alu_gru_fam`";

		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return SQLFrmkUtil.listToRows(list).get(0);
	}
	
	public Row apoderadMatricula(Integer id_mat){
		
		String sql = "SELECT a.id, concat (a.ape_pat,' ',a.ape_mat, ' ', a.nom) nom, a.nro_doc, cel, g.direccion, m.num_cont from alu_familiar a"
				+ " join mat_matricula m on m.id_fam=a.id "
				+ " inner join alu_gru_fam_familiar gf on gf.id_fam = a.id"
				+ " inner join alu_gru_fam g on g.id = gf.id_gpf "
				+ " where m.id=" + id_mat;
		
		logger.info("Cantidad:" + sql);
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return SQLFrmkUtil.listToRows(list).get(0);
	}
	
	
	public List<Row> generarClaveFamiliarUsuario(Integer id_anio,Integer id_sec){
		
		
		List<Row> rows  = listFamiliarUsuario(id_anio, id_sec);
		
		for (Row row : rows) {
			//generar clave SI es que notiene
			Integer id = row.getInteger("id");
			String pass  = row.getString("pass");
			
			if(pass==null || pass.equals("")){
				pass = StringUtil.randomStringLessComplex(4);
				row.put("pass", pass);
				
				updatePassword(id, pass, new Date(), -1);
				
			}
		}
		
		
		return rows;
	}
	
	public List<Row> listFamiliarUsuario(Integer id_anio,Integer id_sec){
		//au.secc existe? eso
		String sql = "select distinct fam.id, fam.ape_pat, fam.ape_mat, fam.nom , fam.nro_doc, fam.id_tdc, fam.pass, "
				+ "au.secc, "
				+ "gr.nom as grado," // ok ? si
				+ "n.nom as nivel" // ok ? si
				+ " from mat_matricula m"
				+ " inner join col_aula au on au.id = m.id_au_asi"
				+ " inner join cat_grad gr on gr.id = au.id_grad" //edta bien ese campo? si
				+ " inner join cat_nivel n on n.id = gr.id_nvl" //edta bien ese campo? ? si
				+ " inner join per_periodo per on per.id = au.id_per"
				+ " inner join alu_familiar fam on fam.id = m.id_fam"
				+ " where " //per.id_anio=? and "//and per.id_suc=? "
				+ " au.id=? "
				+ " order by fam.ape_pat, fam.ape_mat, fam.nom";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_sec}));
		
	}

	public List<Row> claveIntranet(Integer id_mat){
	String sql = "SELECT  fam.id, fam.ape_pat, fam.ape_mat, fam.nom , fam.nro_doc, fam.id_tdc, fam.pass, "
			+ "au.secc, "
			+ "gr.nom as grado," 
			+ "n.nom as nivel" 
			+ " FROM `alu_familiar` fam INNER JOIN `mat_matricula` mat ON mat.`id_fam`=fam.`id`"
			+ " inner join col_aula au on au.id = mat.id_au_asi"
			+ " inner join cat_grad gr on gr.id = au.id_grad" 
			+ " inner join cat_nivel n on n.id = gr.id_nvl" 
			+ " WHERE mat.id=?";
	
	return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_mat}));
	
	}

	public List<Row> buscarxNombre(Integer id_anio,String nom,String est){

	String sql = "select distinct fam.id, fam.ape_pat, fam.ape_mat, fam.nom , fam.nro_doc, fam.id_tdc,fam.fec_nac, fam.cel, fam.corr ,fam.est"
			+ " from mat_matricula m"
			+ " inner join col_aula au on au.id = m.id_au_asi"
			+ " inner join cat_grad gr on gr.id = au.id_grad" //edta bien ese campo? si
			+ " inner join cat_nivel n on n.id = gr.id_nvl" //edta bien ese campo? ? si
			+ " inner join per_periodo per on per.id = au.id_per"
			+ " inner join alu_familiar fam on fam.id = m.id_fam"
			+ " where per.id_anio=? and (?='' or fam.est=?) and"
			+ " lower(concat(fam.ape_pat,' ', fam.ape_mat, ' ', fam.nom)) like '%" + nom.toLowerCase() + "%' "
			+ " order by fam.ape_pat, fam.ape_mat, fam.nom";
	
	return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_anio,est,est}));
	
}

	
	public void updatePassword(Integer id, String pass, Date fecha, Integer id_usr ){
		String sql = "update alu_familiar set pass=?, fec_act=?, usr_act=? where id=?";
		jdbcTemplate.update(sql, new Object[]{pass, fecha, id_usr,id});
	}
	
	public void updatePasswordIni(Integer id, String pass, Date fecha, Integer id_usr ){
		String sql = "update alu_familiar set pass=?, fec_act=?, ini='5', usr_act=? where id=?";
		jdbcTemplate.update(sql, new Object[]{pass, fecha, id_usr,id});
	}

	public void updateCampoConfirmacion(Integer id,String conf, Integer id_usr ){
		String sql = "update alu_familiar set conf=?, fec_act=?, usr_act=? where id=?";
		jdbcTemplate.update(sql, new Object[]{conf, new Date(), id_usr,id});
	}

	
	public boolean validarConfirmacion(Integer id,String conf){
		String sql = "select count(id) from alu_familiar where id=? and conf=? ";
		
		@SuppressWarnings("rawtypes")
		List list = jdbcTemplate.queryForList(sql, new Object[]{id, conf});
		
		return list.size()>0;
	}

	public void updateIni(Integer id, String ini, Date fecha, Integer id_usr ){
		String sql = "update alu_familiar set ini=?, fec_act=?, ini=?, usr_act=? where id=?";
		jdbcTemplate.update(sql, new Object[]{ini, fecha, ini, id_usr,id});
	}

	public List<Row> listaPagosHijos(Integer id_anio, Integer id_fam, String tipo){
		String sql ="select a.id, p.nro_rec, p.fec_pago, p.id_mat , a.ape_mat, a.ape_pat, a.nom, p.banco, p.mens, p.desc_hermano, p.desc_pronto_pago, p.monto_total, p.banco, p.monto"
				+ " from fac_academico_pago p"
				+ " inner join mat_matricula m on m.id= p.id_mat"
				+ " inner join per_periodo per on per.id= m.id_per"
				+ " inner join alu_gru_fam_alumno fa on fa.id_alu = m.id_alu"
				+ " inner join alu_gru_fam gf on gf.id = fa.id_gpf"
				+ " inner join alu_gru_fam_familiar gff on gff.id_gpf = gf.id"
				+ " inner join alu_familiar f on f.id = gff.id_fam"
				+ " inner join alu_alumno a on a.id = fa.id_alu"
				+ " where p.canc=1 and per.id_anio=" + id_anio + " and f.id=" + id_fam + " and p.tip='" + tipo + "'"
				+ " order by p.mens";
		
		return sqlUtil.query(sql);
	}
	
	public List<Row> listaPagosPendientesHijos(Integer id_anio, Integer id_fam, String tipo){
		String sql ="select a.id, p.nro_rec, p.id id_fac, p.fec_pago, p.id_mat , a.ape_mat, a.ape_pat, a.nom, p.banco, p.mens, p.desc_hermano, p.desc_pronto_pago, p.monto_total, p.banco, p.monto"
				+ " from fac_academico_pago p"
				+ " inner join mat_matricula m on m.id= p.id_mat"
				+ " inner join per_periodo per on per.id= m.id_per"
				+ " inner join alu_gru_fam_alumno fa on fa.id_alu = m.id_alu"
				+ " inner join alu_gru_fam gf on gf.id = fa.id_gpf"
				+ " inner join alu_gru_fam_familiar gff on gff.id_gpf = gf.id"
				+ " inner join alu_familiar f on f.id = gff.id_fam"
				+ " inner join alu_alumno a on a.id = fa.id_alu"
				+ " where p.canc=0 and per.id_anio=" + id_anio + " and f.id=" + id_fam + " and p.tip='" + tipo + "'"
				+ " order by p.mens";
		
		return sqlUtil.query(sql);
	}

	public List<Row> listaHijos(Integer id_anio, Integer id_fam){
		String sql ="select distinct a.id, a.ape_mat, a.ape_pat, a.nom, m.id id_mat"
				+ " from  mat_matricula m "
				+ " inner join per_periodo p on p.id = m.id_per"
				+ " inner join alu_gru_fam_alumno fa on fa.id_alu = m.id_alu"
				+ " inner join alu_gru_fam gf on gf.id = fa.id_gpf"
				+ " inner join alu_gru_fam_familiar gff on gff.id_gpf = gf.id"
				+ " inner join alu_familiar f on f.id = gff.id_fam"
				+ " inner join alu_alumno a on a.id = fa.id_alu"
				+ " where f.id=" + id_fam + " and p.id_anio=" + id_anio
				+ " order by a.ape_pat, a.ape_mat ,a.nom";
		
		return sqlUtil.query(sql);
	}
	
	
	public List<Row> listaHijosLibreta(Integer id_fam, Integer id_anio){
		String sql ="select distinct a.id, a.ape_mat, a.ape_pat, a.nom, m.id id_mat, per.id_niv"
				+ "\n from  mat_matricula m "
				+ "\n inner join col_aula au on au.id = m.id_au_asi"
				+ "\n inner join per_periodo per on per.id= au.id_per"
				+ "\n inner join alu_gru_fam_alumno fa on fa.id_alu = m.id_alu"
				+ "\n inner join alu_gru_fam gf on gf.id = fa.id_gpf"
				+ "\n inner join alu_gru_fam_familiar gff on gff.id_gpf = gf.id"
				+ "\n inner join alu_alumno a on a.id = fa.id_alu"
				+ "\n where gff.id_fam=" + id_fam + "  and gff.flag_permisos='A'"
				+ "\n and per.id_anio=" + id_anio
				+  "\n order by a.ape_pat, a.ape_mat ,a.nom";
		
		return sqlUtil.query(sql);
	}
	
	/*
	 * Hijos matriculados
	 */
	public List<Row> listaHijosMatriculados(Integer id_usr, Integer id_anio){
		/*String sql ="select distinct a.id, m.id id_mat, m.fecha, p.id_niv,  concat( a.ape_pat,' ',a.ape_mat,', ' , a.nom) nom, a.usuario, a.pass_educando, a.nro_doc, suc.nom sucursal, ser.nom nivel, au.secc seccion, g.nom grado, cvi.tc_acept tc_acept, cuc.usr usuario_campus, cuc.psw psw_campus, gff.id_gpf "
				+ "\n , (select val from mod_parametro where nom='FECHA_CONF_NUEVA_MATR') fec_conf_nue_matr "
				+ "\n  from  mat_matricula m "
				+ "\n  inner join  per_periodo p on p.id = m.id_per "
				+ " inner join col_aula au on au.id = m.id_au_asi\n "
				+ " inner join alu_alumno a on a.id = m.id_alu\n "
				+ " INNER JOIN `alu_gru_fam_alumno` agfa ON a.`id`=agfa.`id_alu`\n "
				+ " inner join alu_familiar f on f.id = m.id_fam\n "
				+ " inner join alu_gru_fam_familiar gff on gff.id_fam = f.id AND gff.`id_gpf`=agfa.`id_gpf`\n "
				+ " inner join seg_usuario usr ON f.id_usr=usr.id\n "
				//+ " inner join alu_gru_fam_alumno fa on fa.id_alu = m.id_alu"
				//+ " inner join alu_gru_fam gf on gf.id = fa.id_gpf"			
				+ " inner join ges_servicio ser on p.id_srv=ser.id\n "
				+ " inner join ges_sucursal suc on suc.id =ser.id_suc\n "
				//+ " inner join col_anio an on p.id_anio=an.id"
				+ " INNER JOIN cat_grad g ON g.id = au.id_grad\n "
				+ " INNER JOIN col_conf_sesion ccs ON g.`id`=ccs.`id_gra`\n "
				+ " LEFT JOIN cvi_inscripcion_campus cvi ON cvi.id_alu=a.id AND cvi.id_fam=f.id\n "
				+ " LEFT JOIN cvi_usuario_campus cuc ON cvi.id=cuc.id_cvic\n "
				+ " WHERE p.id_anio="+id_anio+" AND usr.id="+id_usr+" and (m.id_sit<>5 OR m.id_sit IS NULL)\n " 		//AND ((NOW() BETWEEN ccs.`hora_ini` AND ccs.`hora_fin`) OR" + " ((CURTIME() >= (SELECT val FROM mod_parametro WHERE nom='HORA_INICIO_MAT_DIARIA'))  " + " AND (g.`id` IN (SELECT t.`id_gra` FROM col_conf_sesion t WHERE CAST(t.`hora_ini` AS DATE) <=  CAST(CURDATE() AS DATE)))))
				+ " ORDER BY a.ape_pat, a.ape_mat ,a.nom";
		//System.out.println(sql);*/
		
		String sql ="  SELECT DISTINCT alu.id, t.id_mat, t.fecha, t.id_niv,  CONCAT( alu.ape_pat,' ',alu.ape_mat,', ' , alu.nom) nom, alu.usuario, alu.pass_educando, alu.nro_doc\n" + 
				"  , t.sucursal, t.nivel, t.seccion, t.grado\n" + 
				" , cvi.tc_acept tc_acept, cuc.usr usuario_campus, cuc.psw psw_campus, agff.id_gpf \n" + 
				" , (SELECT val FROM mod_parametro WHERE nom='FECHA_CONF_NUEVA_MATR') fec_conf_nue_matr \n" + 
				"  FROM `alu_alumno` alu INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`\n" + 
				"  INNER JOIN `alu_gru_fam_familiar` agff ON agfa.`id_gpf`=agff.`id_gpf`\n" + 
				"  INNER JOIN `alu_familiar` fam ON agff.`id_fam`=fam.`id`\n" + 
				"  INNER JOIN `seg_usuario` usr ON fam.`id_usr`=usr.`id`\n" + 
				"  LEFT JOIN  (SELECT mat.`id_alu`, mat.`id` id_mat, mat.`fecha` fecha, niv.`nom` nivel, gra.`nom` grado, niv.`id` id_niv, suc.`nom` sucursal, au.`secc` seccion FROM `mat_matricula` mat \n" + 
				"  INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.id\n" + 
				"  INNER JOIN `col_ciclo` cic ON au.`id_cic`=cic.id\n" + 
				"  INNER JOIN `per_periodo` per ON cic.id_per=per.id \n" + 
				"  INNER JOIN ges_sucursal suc ON suc.id =per.id_suc\n" + 
				"  INNER JOIN `cat_nivel` niv ON per.id_niv=niv.id\n" + 
				"  INNER JOIN `cat_grad` gra ON niv.id=gra.id_nvl AND au.`id_grad`=gra.`id`\n" + 
				"  WHERE (mat.id_sit<>5 OR mat.id_sit IS NULL) AND per.id_anio=5 )t ON t.`id_alu`=alu.`id`\n" + 
				"  LEFT JOIN cvi_inscripcion_campus cvi ON cvi.id_alu=alu.id AND cvi.id_fam=fam.id\n" + 
				" LEFT JOIN cvi_usuario_campus cuc ON cvi.id=cuc.id_cvic\n" + 
				"  WHERE  usr.id=287 \n" + 
				"  ORDER BY alu.ape_pat, alu.ape_mat ,alu.nom;";
		
		return sqlUtil.query(sql);
	}
	
	public List<Row> listaTodosHijosFamilia(Integer id_usr){
		String sql ="SELECT per.`id`, alu.id id_alu, alu.`cod`, CONCAT(per.ape_pat,' ', per.ape_mat,' ', per.nom) alumno, CONCAT(per.ape_pat,' ', per.ape_mat,' ', per.nom) value \n" + 
				"FROM `col_persona` per INNER JOIN `alu_alumno` alu ON per.`id`=alu.`id_per`\n" + 
				"INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`\n" + 
				"INNER JOIN alu_gru_fam agf ON agfa.id_gpf=agf.id\n"+
				//"INNER JOIN `alu_gru_fam_familiar` agff ON agfa.`id_gpf`=agff.`id_gpf`\n" + 
				//"INNER JOIN `alu_familiar` fam ON agff.`id_fam`=fam.`id`\n" + 
				"INNER JOIN seg_usuario usr ON agf.id_usr=usr.id\n"+
				"WHERE usr.id="+id_usr;
		return sqlUtil.query(sql);
	}
	
	
	public List<Row> listaTodosHijosFamiliaMatNoTras(Integer id_usr, Integer id_anio){
		String sql ="SELECT per.`id`, alu.id id_alu, alu.`cod`, CONCAT(per.ape_pat,' ', per.ape_mat,' ', per.nom) alumno, CONCAT(per.ape_pat,' ', per.ape_mat,' ', per.nom) value, alu.usuario, alu.pass_google \n" + 
				"FROM `col_persona` per INNER JOIN `alu_alumno` alu ON per.`id`=alu.`id_per`\n" + 
				"INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`\n" + 
				"INNER JOIN alu_gru_fam agf ON agfa.id_gpf=agf.id\n"+
				"INNER JOIN mat_matricula mat ON alu.id=mat.id_alu "+
				"INNER JOIN col_aula au ON mat.id_au_asi=au.id "+
				"INNER JOIN per_periodo peri ON au.id_per=peri.id "+
				//"INNER JOIN `alu_gru_fam_familiar` agff ON agfa.`id_gpf`=agff.`id_gpf`\n" + 
				//"INNER JOIN `alu_familiar` fam ON agff.`id_fam`=fam.`id`\n" + 
				"INNER JOIN seg_usuario usr ON agf.id_usr=usr.id\n"+
				"WHERE usr.id="+id_usr+" AND peri.id_anio="+id_anio+" AND (mat.tipo<>'A' OR mat.tipo<>'V' OR mat.tipo IS NULL) ";
		return sqlUtil.query(sql);
	}
	
	/**
	 * Lista todas las familias que tiene el familiar
	 * @param id_fam
	 * @return
	 */
	public List<GruFam> getListGrupoFamiliares(Integer id_fam, Integer id_anio){
		
		String sql = "select g.*"
				+ "from alu_gru_fam_familiar gp "
				+ "inner join alu_gru_fam  g on  gp.id_gpf = g.id "
				+ "inner join alu_familiar p on ( p.id = gp.id_fam ) "
				+ "where p.id=" + id_fam ;
			
		List<GruFam> familias = sqlUtil.query(sql,GruFam.class);
		
		List<GruFam> listFamiliasConMatricula = new ArrayList<GruFam>();
		
		for (GruFam gruFam : familias) {

			
			//validar si la familia tiene algun hijo matriculado en el a�o
			List<GruFamAlumno> grupAlumnos = gruFamAlumnoDAO.alumnosMatriculados(gruFam.getId(), id_anio);
			
			if (grupAlumnos.size()>0){
				Param param = new Param();
				param.put("gfr.id_gpf", gruFam.getId());
				param.put("fam.est", "A");
//				param.put("gfr.id_par", "is not null");
				List<GruFamFamiliar> grupFamiliares = gruFamFamiliarDAO.listFullByParams(param, null);
				gruFam.setGruFamFamiliar(grupFamiliares);
				gruFam.setGruFamAlumno(grupAlumnos);
				listFamiliasConMatricula.add(gruFam);
				
			}
			
			
		}
		
		return listFamiliasConMatricula;
	}
	
	public List<Row> listaApoderado(Integer id_anio){
		//au.secc existe? eso
		String sql = "select fam.id id_fam, fam.id_par, m.id_alu " // ok ? si
				+ " from mat_matricula m"
				+ " inner join col_aula au on au.id = m.id_au_asi"
				+ " inner join cat_grad gr on gr.id = au.id_grad" //edta bien ese campo? si
				+ " inner join cat_nivel n on n.id = gr.id_nvl" //edta bien ese campo? ? si
				+ " inner join per_periodo per on per.id = au.id_per"
				+ " inner join alu_familiar fam on fam.id = m.id_fam"
				+ " where " //per.id_anio=? and "//and per.id_suc=? "
				+ " per.id_anio=? " //AND fam.nro_doc='48161728'
				+ " order by 1";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_anio}));
	}	

	public Integer actualizaApoderado(Integer id_fam,Integer id_par){
		String sql = "update alu_gru_fam_familiar set flag_permisos='A', id_par=" + id_par + ", usr_act=-1, DATE(fec_act)=CURDATE()  where id_fam=" +id_fam;
		return jdbcTemplate.update(sql);
	}
	
	public void actualizarEstado(Integer id,String est){
		String sql = "update alu_familiar set est='" + est + "',fec_act=CURDATE() where id=" + id;
		sqlUtil.update(sql);
		
	}
	
	public List<Row> familiarParentesco(Integer id_fam) {

		String sql = "SELECT par.id, par.par as value, fam.nro_doc aux1 from alu_familiar fam INNER JOIN cat_parentesco par ON fam.id_par=par.id AND fam.id=?";

		return sqlUtil.query(sql, new Object[] {id_fam });

	}
	
	public int actualizarDatosLight(Familiar familiar) {
		// update
		String sql = "UPDATE alu_familiar "
					+ "SET "
					+ "ini=?, "
					+ "nom=?, "
					+ "ape_pat=?, "
					+ "ape_mat=?, "
					+ "tlf=?, "
					+ "id_gin=?, "
					+ "ocu=?, "
					+ "cto_tra=?, "
					+ "id_eci=?, "
					+ "id_gen=?, "
					+ "fec_nac=?, "
					+ "id_rel=?, "
					+ "dir=?, "
					+ "id_dist=?, "
					+ "viv_alu=?, "
					+ "usr_act=?,fec_act=? "
				+ "WHERE id=?";
		
		//logger.info(sql);

		return jdbcTemplate.update(sql, 
					familiar.getIni(),
					familiar.getNom(),
					familiar.getApe_pat(),
					familiar.getApe_mat(),
					familiar.getTlf(),
					familiar.getId_gin(),
					familiar.getOcu(),
					familiar.getCto_tra(),
					familiar.getId_eci(),
					familiar.getId_gen(),
					familiar.getFec_nac(),
					familiar.getId_rel(),
					familiar.getDir(),
					familiar.getId_dist(),
					familiar.getViv_alu(),
					familiar.getUsr_act(),
					new java.util.Date(),
					familiar.getId()); 

	}
	
	public boolean esCorreoValidado(Integer id_fam){
		String sql = "select corr_val from alu_familiar where id=" + id_fam;
		List<Row> rows = sqlUtil.query(sql);
		
		if(rows.size()==0 )
			return false;
		
		if(rows.get(0).get("corr_val")!=null)
			return true;
		else
			return false;
			
	}
	
	public boolean celularExiste(Integer id_fam,String cel){
		
		cel = cel.replace(" ", "").replaceAll("-", "").replaceAll("(", "").replaceAll(")", "");
		String sql = "select corr_val from alu_familiar where id!=? and cel=? and ini=2";
		List<Row> rows = sqlUtil.query(sql, new Object[]{id_fam,cel});
		
		return !rows.isEmpty();

	}
	
	public List<Row> listarFamiliares(Integer id_gpf){

		String sql = "SELECT fam.`id`,par.id id_par, agff.id_gpf, per.`nro_doc`, CONCAT(per.`ape_pat`,' ', per.`ape_mat`,' ', per.`nom`) familiar, per.corr fam_corr,  par.`par` parentesco, fam.id_anio_act"
				+ " FROM `alu_familiar` fam INNER JOIN `alu_gru_fam_familiar` agff ON fam.`id`=agff.`id_fam`"
				+ " INNER JOIN col_persona per ON fam.id_per=per.id "
				+ " INNER JOIN `cat_parentesco` par ON fam.`id_par`=par.`id`"
				+ " WHERE agff.`id_gpf`=? AND fam.`id_par` IN (1,2);";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_gpf}));
	}	
	
	public List<Row> listarOtrosFamiliares(Integer id_gpf){

		String sql = "SELECT fam.`id`, agff.id_gpf, fam.`nro_doc`, CONCAT(fam.`ape_pat`,' ', fam.`ape_mat`,' ', fam.`nom`) familiar, par.`par` parentesco"
				+ " FROM `alu_familiar` fam INNER JOIN `alu_gru_fam_familiar` agff ON fam.`id`=agff.`id_fam`"
				+ " INNER JOIN `cat_parentesco` par ON fam.`id_par`=par.`id`"
				+ " WHERE agff.`id_gpf`=? AND fam.`id_par` NOT IN (1,2);";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_gpf}));
	}	
	
	/**
	 * Obtener datos del familiar
	 * @param id_fam
	 * @return
	 */
	public Row obtenerDatosFamiliar(Integer id_fam, String nro_doc) {
				
		String sql = " SELECT per.*, fam.id_par, fam.prof, fam.ocu, fam.id id_fam, pro.`id` id_pro, dep.`id` id_dep, fam.id_pais, agff.id id_gpf";
			   sql += " FROM `alu_familiar` fam LEFT JOIN `cat_distrito` dist ON fam.`id_dist`=dist.`id`";
			   sql += " INNER JOIN col_persona per ON fam.id_per=per.id ";
			   sql += " INNER JOIN alu_gru_fam_familiar agff ON fam.id=agff.id_fam";
			   sql += " LEFT JOIN `cat_provincia` pro ON dist.`id_pro`=pro.`id`";
			   sql += " LEFT JOIN `cat_departamento` dep ON pro.`id_dep`=dep.`id`";
			   //sql += " LEFT JOIN cat_pais p ON dep.id_pais=p.id ";
				if(id_fam!=null)
					sql += " WHERE fam.`id`="+id_fam;
				else if(nro_doc!=null)
					sql += " WHERE fam.nro_doc="+nro_doc;
				
		List<Row> familiares =sqlUtil.query(sql);
		if(familiares.size()>0)
			return familiares.get(0);
		else 
			return null;

	}
	
	public void eliminarFamiliarGrupo(Integer id_gpf,Integer id_fam){
		String sql = "DELETE FROM `alu_gru_fam_familiar` WHERE id_gpf="+id_gpf+" AND id_fam="+id_fam;
		sqlUtil.update(sql);
		
	}
	
	public void eliminarFamiliar(Integer id_fam){
		String sql = "DELETE FROM alu_familiar WHERE id="+id_fam;
		sqlUtil.update(sql);
		
	}
	
	public void actualizarAnioFamiliarActualizacion(Integer id_anio, Integer id_fam){
		String sql = "update alu_familiar set id_anio_act="+id_anio+" WHERE id="+id_fam;
		sqlUtil.update(sql);
		
	}
	
	/**
	 * Obtener Datos del Familiar
	 * @param id_gpf
	 * @param id_par
	 * @return
	 */
	public Row obtenerDatosDomiciliariosFamiliar(Integer id_gpf, Integer id_par) {
		
		String sql = "SELECT fam.`id`, fam.`dir`, fam.`ref`, fam.tlf, dist.`id` id_dist, pro.`id` id_pro, dep.`id` id_dep, dep.id_pais id_pais"
				+ " FROM `alu_gru_fam_familiar` agff INNER JOIN `alu_familiar` fam ON agff.`id_fam`=fam.`id`"
				+ " INNER JOIN `cat_distrito` dist ON fam.`id_dist`=dist.`id`"
				+ " INNER JOIN `cat_provincia` pro ON pro.`id`=dist.`id_pro`"
				+ " INNER JOIN `cat_departamento` dep ON pro.`id_dep`=dep.`id`"
				+ " WHERE agff.`id_gpf`=? AND fam.`id_par`=?";				
		List<Row> datosFamiliar =sqlUtil.query(sql,new Object[]{ id_gpf, id_par});
		if(datosFamiliar.size()>0)
			return datosFamiliar.get(0);
		else 
			return null;

	}
	
	public Row mensajeExiste(Integer id_fam,Integer id_alu){
		
		String sql = "SELECT id, flg_en FROM `msj_mensajeria_familiar` WHERE id_alu=? AND id_des=?";
		List<Row> datosMensaje =sqlUtil.query(sql,new Object[]{ id_alu, id_fam});
		if(datosMensaje.size()>0)
			return datosMensaje.get(0);
		else 
			return null;

	}
	
	public List<Row> listarHijosFamiliar(Integer id_fam, Integer id_gpf, Integer id_anio){

		String sql = "SELECT fam.`id` id_fam, fam.`corr` fam_corr, fam.`ape_pat` fam_ape_pat, fam.`ape_mat` fam_ape_mat, fam.`nom` fam_nom, "
				+ " alu.`id` id_alu, alu.`nom` , alu.ape_pat , alu.`ape_mat` , alu.nom, alu.usuario, alu.pass_educando "
				+ " FROM `alu_familiar` fam INNER JOIN `alu_gru_fam_familiar` agff ON fam.`id`=agff.`id_fam`"
				+ " INNER JOIN `alu_gru_fam_alumno` agfa ON agff.`id_gpf`=agfa.`id_gpf`"
				+ " INNER JOIN `alu_alumno` alu ON agfa.`id_alu`=alu.`id`"
				+ " INNER JOIN `mat_matricula` mat ON alu.`id`=mat.`id_alu`"
				+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+ " WHERE fam.`id`=? AND agff.`id_gpf`=? AND per.`id_anio`=?";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{id_fam, id_gpf, id_anio}));
	}
	
	/**
	 * Listar Apoderados po Anio
	 * @param id_anio
	 * @return
	 */
	/*public List<Row> listarApoderados(Integer id_anio){

		String sql = "SELECT DISTINCT fam.id, CONCAT(fam.`ape_pat`,' ',fam.`ape_mat`,' ', fam.`nom`) familiar , fam.`cel`, fam.`corr`, CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.nom) alumno,"
				+ " niv.`nom` nivel, gra.`nom` grado, au.`secc` aula"
				+ " FROM `mat_matricula` mat INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.`id`"
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ " INNER JOIN `cat_grad` gra ON mat.`id_gra`=gra.`id`"
				+ " INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
				+ " INNER JOIN `per_periodo` per ON mat.`id_per`=per.`id`"
				+ " WHERE per.`id_anio`=? AND mat.`mat_val`=1 AND fam.nro_doc='48161728' ";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{id_anio}));
	}	*/
	
	public void actualizarPswFamiliar(String pass, Integer id_fam){
		String sql = "update alu_familiar set pass="+pass+" WHERE id="+id_fam;
		sqlUtil.update(sql);
		
	}
	
	/*public List<Row> verificarSesion(Integer id_fam){

		String sql = "SELECT fam.* "
				+ " FROM `mat_matricula` mat INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.`id`"
				+ " INNER JOIN `cat_grad` gra ON mat.`id_gra`=gra.`id`"
				+ " INNER JOIN col_conf_sesion ccs ON gra.`id`=ccs.`id_gra`"
				+ " WHERE fam.`id`=?; ";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{id_fam}));
	}
	*/
	public boolean existeCronograma(Integer id_fam) {
		
		String sql = "SELECT DISTINCT "
				+ " fam.*"
				+ " FROM `mat_matricula` mat "
				+ " INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.`id` "
				+ " INNER JOIN `cat_grad` gra ON mat.`id_gra`=gra.`id` "
				+ " INNER JOIN col_conf_sesion ccs ON gra.`id`=ccs.`id_gra`"
				+ " INNER JOIN `per_periodo` per ON mat.`id_per`=per.id"
				+ " WHERE"
				+ " fam.`id`=? AND per.`id_anio`=4  AND ((NOW() BETWEEN ccs.`hora_ini` AND ccs.`hora_fin`) OR"
				+ " ((CURTIME() >= (SELECT val FROM mod_parametro WHERE nom='HORA_INICIO_MAT_DIARIA'))  "
				+ " AND (gra.`id` IN"
				+ " (SELECT t.`id_gra` FROM col_conf_sesion t WHERE CAST(t.`hora_ini` AS DATE) <=  CAST(CURDATE() AS DATE)))))";
		//System.out.println(sql);
		//logger.info(sql);

		List<Map<String,Object>> alumnos = jdbcTemplate.queryForList(sql, new Object[]{id_fam});
		
		return (alumnos.size()>0);

	}
	
	/**
	 * Actualizar Datos verificados
	 * @param cel
	 * @param corr
	 * @param fec_emi_dni
	 * @param ubigeo
	 * @param id_fam
	 */
	public void actualizarDatosVerficadosFamiliar(String cel, String corr, Date fec_emi_dni, String ubigeo,Integer id_fam){
	/*	Param param = new Param();
		param.put(":cel", cel);
		param.put(":corr", corr);
		param.put(":fec_emi_dni", fec_emi_dni);
		param.put(":ubigeo", ubigeo);
		param.put(":id_fam", id_fam);*/
		String sql = "UPDATE `alu_familiar` SET cel=?, corr=?, fec_emi_dni=?, ubigeo=? WHERE id=?";
		sqlUtil.update(sql,new Object[]{cel,corr,fec_emi_dni,ubigeo,id_fam});
		
	}
	
	/** Listar alumnos segun tipo*/
	public List<Row> listarFamiliasSegunTipo(String tipBusqueda, Integer id_anio, Integer id_gir, Integer id_suc, Integer id_niv) {
		String sql="";
		if(tipBusqueda.equals("T")) {
			sql = "SELECT DISTINCT agf.cod, agf.nom familia, agfa.id_gpf "
			    + " FROM alu_gru_fam agf "
			    + " LEFT JOIN `alu_gru_fam_alumno` agfa ON agfa.id_gpf=agf.id"
				+ " LEFT JOIN `alu_alumno` alu  ON alu.id=agfa.id_alu"
				+ " LEFT JOIN col_persona per ON alu.id_per=per.id"
				+ " ORDER BY agf.nom";
		} else if(tipBusqueda.equals("A")) {
			sql += "SELECT DISTINCT agf.cod, agf.nom familia, agfa.id_gpf \r\n" ;
			sql += " FROM `alu_alumno` alu INNER JOIN col_persona per ON alu.id_per=per.id \r\n";
			sql += "INNER JOIN `alu_gru_fam_alumno` agfa ON alu.id=agfa.id_alu \r\n" ;
			sql += " INNER JOIN alu_gru_fam agf ON agfa.id_gpf=agf.id";
			sql +=" INNER JOIN `mat_matricula` mat ON mat.`id_alu`=alu.`id` \r\n" ; 
			sql +=" INNER JOIN `per_periodo` peri ON mat.`id_per`=peri.`id`\r\n" ;
			sql += " WHERE peri.`id_anio`=" +id_anio;
			if(id_suc!=null) {
				sql += " AND peri.id_suc="+id_suc;	
			}
			if(id_niv!=null) {
				sql += " AND peri.id_niv="+id_niv;	
			}
			sql +=" ORDER BY per.ape_pat, per.ape_mat, per.nom";
		} else if(tipBusqueda.equals("R")) {
			sql += "SELECT DISTINCT agf.cod, agf.nom familia, agfa.id_gpf \r\n" ;
			sql += " FROM `alu_alumno` alu INNER JOIN col_persona per ON alu.id_per=per.id \r\n";
			sql += "INNER JOIN `alu_gru_fam_alumno` agfa ON alu.id=agfa.id_alu \r\n" ;
			sql += " INNER JOIN alu_gru_fam agf ON agfa.id_gpf=agf.id";
			sql +="INNER JOIN `mat_matricula` mat ON mat.`id_alu`=alu.`id`\r\n" ;
			sql +="INNER JOIN `per_periodo` peri ON mat.`id_per`=peri.`id`\r\n" ; 
			sql +="WHERE peri.`id_anio`=" +id_anio+" and mat.id_sit=5";
			if(id_suc!=null) {
				sql += " AND peri.id_suc="+id_suc;	
			}
			if(id_niv!=null) {
				sql += " AND peri.id_niv="+id_niv;	
			}
			sql +=" ORDER BY per.ape_pat, per.ape_mat, per.nom";
		} else if(tipBusqueda.equals("E")) {
			sql += "SELECT DISTINCT agf.cod, agf.nom familia, agfa.id_gpf \r\n" ;
			sql += " FROM `alu_alumno` alu INNER JOIN col_persona per ON alu.id_per=per.id \r\n";
			sql += "INNER JOIN `alu_gru_fam_alumno` agfa ON alu.id=agfa.id_alu \r\n" ;
			sql += " INNER JOIN alu_gru_fam agf ON agfa.id_gpf=agf.id";
			sql +="INNER JOIN `mat_matricula` mat ON mat.`id_alu`=alu.`id`\r\n" ;
			sql +="INNER JOIN `per_periodo` per ON mat.`id_per`=per.`id`\r\n" ; 
			sql +="WHERE per.`id_anio` and id_sit='1' AND id_gra='14' ";
			if(id_suc!=null) {
				sql += " AND per.id_suc="+id_suc;	
			}
			sql +=" ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
		}
		
		//return sqlUtil.query(sql);	
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	public Row datosFamiliaxCodigo(String cod, Integer id_anio) {
		String sql = "SELECT agf.id, agf.id_usr,  agf.`cod`, agf.`nom`, agf.`est`, dep.`id` id_dep, pro.`id` id_pro, dist.`id` id_dist, agf.id_seg, agf.id_csal, agf.cod_aseg, agf.`direccion`, agf.`referencia`,\r\n" + 
				"seg.`nom` tipo_seguro, ccs.`nom` centro_salud, agf.`direccion`, usr.login, usr.password pass\r\n" + 
				"FROM `alu_gru_fam` agf LEFT JOIN `cat_distrito` dist ON agf.`id_dist`=dist.`id`\r\n" + 
				"LEFT JOIN cat_provincia pro ON dist.id_pro=pro.id\r\n" + 
				"LEFT JOIN cat_departamento dep ON pro.id_dep=dep.id\r\n" + 
				"LEFT JOIN `cat_tipo_seguro` seg ON agf.`id_seg`=seg.`id`\r\n" + 
				"LEFT JOIN `cat_centro_salud` ccs ON agf.`id_csal`=ccs.id\r\n" + 
				"LEFT JOIN `seg_usuario` usr ON agf.`id_usr`=usr.id\r\n" + 
				"WHERE agf.`cod`='"+cod+"'";

		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return SQLFrmkUtil.listToRows(list).get(0);	
	}
	
	public Row datosFamiliaxUsuario(Integer id_usr) {
		String sql = "SELECT agf.id, agf.id_usr,  agf.`cod`, agf.`nom`, agf.`est`, dep.`id` id_dep, pro.`id` id_pro, dist.`id` id_dist, agf.id_seg, agf.id_csal, agf.cod_aseg, agf.`direccion`, agf.`referencia`,\r\n" + 
				"seg.`nom` tipo_seguro, ccs.`nom` centro_salud, agf.`direccion`, usr.login, usr.password pass\r\n" + 
				"FROM `alu_gru_fam` agf LEFT JOIN `cat_distrito` dist ON agf.`id_dist`=dist.`id`\r\n" + 
				"LEFT JOIN cat_provincia pro ON dist.id_pro=pro.id\r\n" + 
				"LEFT JOIN cat_departamento dep ON pro.id_dep=dep.id\r\n" + 
				"LEFT JOIN `cat_tipo_seguro` seg ON agf.`id_seg`=seg.`id`\r\n" + 
				"LEFT JOIN `cat_centro_salud` ccs ON agf.`id_csal`=ccs.id\r\n" + 
				"INNER JOIN `seg_usuario` usr ON agf.`id_usr`=usr.id\r\n" + 
				"WHERE usr.id='"+id_usr+"'";

		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return SQLFrmkUtil.listToRows(list).get(0);	
	}
	
	public Row datosFamiliaxGrupoFamiliar(Integer id_gpf) {
		String sql = "SELECT agf.id, agf.id_usr,  agf.`cod`, agf.`nom`, agf.`est`, dep.`id` id_dep, pro.`id` id_pro, dist.`id` id_dist, agf.id_seg, agf.id_csal, agf.cod_aseg, agf.`direccion`, agf.`referencia`,\r\n" + 
				"seg.`nom` tipo_seguro, ccs.`nom` centro_salud, agf.`direccion`, usr.login, usr.password pass\r\n" + 
				"FROM `alu_gru_fam` agf LEFT JOIN `cat_distrito` dist ON agf.`id_dist`=dist.`id`\r\n" + 
				"LEFT JOIN cat_provincia pro ON dist.id_pro=pro.id\r\n" + 
				"LEFT JOIN cat_departamento dep ON pro.id_dep=dep.id\r\n" + 
				"LEFT JOIN `cat_tipo_seguro` seg ON agf.`id_seg`=seg.`id`\r\n" + 
				"LEFT JOIN `cat_centro_salud` ccs ON agf.`id_csal`=ccs.id\r\n" + 
				"INNER JOIN `seg_usuario` usr ON agf.`id_usr`=usr.id\r\n" + 
				"WHERE agf.id='"+id_gpf+"'";

		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return SQLFrmkUtil.listToRows(list).get(0);	
	}
	
	public Row datosFamiliar(Integer id_fam, String nro_doc) {
		String sql = "SELECT per.*, par.id id_par, fam.prof, fam.ocu, fam.id_anio_act, fam.email_inst, fam.id id_fam,pro.id id_pro, dep.id id_dep, agff.id_gpf\r\n" ; 
				sql+= "FROM `col_persona` per LEFT JOIN `alu_familiar` fam ON per.`id`=fam.`id_per`\r\n"; 
				sql+= " LEFT JOIN alu_gru_fam_familiar agff ON fam.id=agff.id_fam ";
				sql+= " LEFT JOIN cat_parentesco par ON fam.id_par=par.id";
				sql+= " LEFT JOIN cat_distrito dist ON per.id_dist_nac=dist.id"; 
				sql+= " LEFT JOIN cat_provincia pro ON dist.id_pro=pro.id";
				sql+= " LEFT JOIN cat_departamento dep ON pro.id_dep=dep.id";
				if(id_fam!=0)
					sql += " WHERE fam.`id`="+id_fam;
				else if(nro_doc!=null)
					sql += " WHERE per.nro_doc="+nro_doc;
		List<Row> datosFamiliar =sqlUtil.query(sql);
		if(datosFamiliar.size()>0)
			return datosFamiliar.get(0);
		else 
			return null;
		/*List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return SQLFrmkUtil.listToRows(list).get(0);	*/
	}
	
	public Row datosFamiliarxParentesco(Integer id_per, Integer id_par) {
		String sql = "SELECT per.*, par.id id_par, fam.id id_fam, agff.id_gpf\r\n" + 
				"FROM `col_persona` per INNER JOIN `alu_familiar` fam ON per.`id`=fam.`id_per`\r\n" + 
				" INNER JOIN alu_gru_fam_familiar agff ON fam.id=agff.id_fam "+
				" INNER JOIN cat_parentesco par ON fam.id_par=par.id"+
				//" LEFT JOIN cat_distrito dist ON per.id_dist_nac=dist.id"+ 
				//" LEFT JOIN cat_provincia pro ON dist.id_pro=pro.id"+
				//" LEFT JOIN cat_departamento dep ON pro.id_dep=dep.id"+
				" WHERE per.`id`="+id_per+ " and par.id="+id_par;

		List<Row> datosFamiliar =sqlUtil.query(sql);
		if(datosFamiliar.size()>0)
			return datosFamiliar.get(0);
		else 
			return null;
		/*List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return SQLFrmkUtil.listToRows(list).get(0);	*/
	}
	
	public Row datosFamiliaxParentescoGruFam(Integer id_par, Integer id_gpf) {
		String sql = "SELECT gff.id, fam.id_par \r\n" + 
				"FROM alu_gru_fam_familiar gff INNER JOIN `alu_familiar` fam ON gff.id_fam=fam.id \r\n" + 
				" INNER JOIN cat_parentesco par ON fam.id_par=par.id"+
				//" LEFT JOIN cat_distrito dist ON per.id_dist_nac=dist.id"+ 
				//" LEFT JOIN cat_provincia pro ON dist.id_pro=pro.id"+
				//" LEFT JOIN cat_departamento dep ON pro.id_dep=dep.id"+
				" WHERE gff.`id_gpf`="+id_gpf+ " and par.id="+id_par;

		List<Row> datosFamilia =sqlUtil.query(sql);
		if(datosFamilia.size()>0)
			return datosFamilia.get(0);
		else 
			return null;
		/*List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return SQLFrmkUtil.listToRows(list).get(0);	*/
	}
	
	public Row validarApoderadoxAnio(Integer id_anio, Integer id_fam) {
		String sql = "SELECT mat.* FROM \n" + 
				"`mat_matricula` mat INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.`id`\n" + 
				"INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\n" + 
				"INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`\n" + 
				"WHERE per.`id_anio`="+id_anio+" AND per.`id_tpe`=1 AND mat.id_fam="+id_fam;

		List<Row> apoderado =sqlUtil.query(sql);
		if(apoderado.size()>0)
			return apoderado.get(0);
		else 
			return null;
		/*List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return SQLFrmkUtil.listToRows(list).get(0);	*/
	}
	
}