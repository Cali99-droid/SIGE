package com.sige.mat.dao;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.PerUniDAOImpl;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.PerUni;
import com.tesla.frmk.sql.Row; 

/**
 * Define m�todos DAO operations para la entidad per_uni.
 * @author MV
 *
 */
@Repository
public class PerUniDAO extends PerUniDAOImpl{
	
	@Autowired
	private TokenSeguridad tokenSeguridad;

	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Map<String,Object>> verificarPeriodoVig(Integer id_niv, int id_anio, Integer nump) {

		String sql = "SELECT per_uni.id, CONCAT(per_aca.nom,' ',per_uni.nump) AS VALUE, 1 flag"
				+ " FROM col_per_uni per_uni "
				+ " INNER JOIN cat_per_aca_nivel per_niv ON per_uni.id_cpa=per_niv.id"
				+ " INNER JOIN cat_periodo_aca per_aca ON per_aca.id=per_niv.id_cpa"
				+ " WHERE per_niv.id_niv=? AND per_uni.id_anio=? AND per_uni.id=? AND CURDATE()>= per_uni.`fec_ini_ing` AND CURDATE()<=per_uni.`fec_fin_ing`"
				+ " ORDER BY  per_uni.nump";
		return jdbcTemplate.queryForList(sql, new Object[]{ id_niv, id_anio, nump } );
 
	}
	
	public PerUni getUltimoPeriodo(Integer id_niv) {

		String sql = "select cpu.* from col_per_uni cpu "
				+ " inner join  cat_per_aca_nivel cpa on cpa.id = cpu.id_cpa"
				+ " where cpa.id_niv=? order by nump desc";
		
		List<PerUni> perunis = sqlUtil.query(sql, new Object[]{id_niv}, PerUni.class);
		
		return perunis.get(0);

		
	}
	
	/**
     * Clonar un la configuraci�n de Unidades por Periodo
     * @param idAlu
     * @return Id 
     */	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int clonarConfiguraciones(Integer id_anio,   Integer id_anio_ant)  {
			
		String sql = "INSERT INTO col_per_uni (id_cpa,nump,`numu_ini`, `numu_fin`, est,`fec_ini`, `fec_fin`, `fec_ini_ing`, `fec_fin_ing`, `usr_ins`, `fec_ins`,id_anio) "
				    + "	SELECT `id_cpa`, `nump`, `numu_ini`, `numu_fin`, `est`, DATE_ADD(`fec_ini`,INTERVAL 1 YEAR), DATE_ADD(`fec_fin`,INTERVAL 1 YEAR),DATE_ADD(`fec_ini_ing`,INTERVAL 1 YEAR), DATE_ADD(`fec_fin_ing`,INTERVAL 1 YEAR),"+tokenSeguridad.getId()+",NOW(),"+id_anio+" FROM `col_per_uni` WHERE id_anio="+id_anio_ant;
		
		//logger.info(sql);
        try {

			jdbcTemplate.update( sql);		
		} catch (DataAccessException e) {
			e.printStackTrace();
		} 
		return 1;
	}
	
	/**
	 * Obteenr los datos de los periodos acad�micos por nivel, necesario par la programaci�n anual
	 * @param id_anio
	 * @param id_niv
	 * @return
	 */
	public List<Row> datosPeriodoxNivel(Integer id_anio, Integer id_niv) {

		String sql = "SELECT cpu.id, cpa.`nom` periodo, cpu.`nump`, cpu.`fec_ini`, cpu.`fec_fin`, SUM(cpud.`nro_sem`) total"
				+ " FROM `col_per_uni` cpu "
				+ " INNER JOIN `cat_per_aca_nivel` cpan ON cpu.`id_cpa`=cpan.`id`"
				+ " INNER JOIN `cat_periodo_aca` cpa ON cpan.`id_cpa`=cpa.`id`"
				+ " INNER JOIN `col_per_uni_det` cpud ON cpu.id=cpud.`id_cpu`"
				+ " WHERE cpu.`id_anio`=? AND cpan.`id_niv`=?"
				+ " GROUP BY cpud.`id_cpu`;";
		return sqlUtil.query(sql, new Integer[]{id_anio, id_niv});
 
	}
	
	public List<Row> datosPeriodo(Integer id_cpu) {

		String sql = "SELECT cpu.id, cpa.`nom` periodo, cpu.`nump`, cpu.`fec_ini`, cpu.`fec_fin`, SUM(cpud.`nro_sem`) total"
				+ " FROM `col_per_uni` cpu "
				+ " INNER JOIN `cat_per_aca_nivel` cpan ON cpu.`id_cpa`=cpan.`id`"
				+ " INNER JOIN `cat_periodo_aca` cpa ON cpan.`id_cpa`=cpa.`id`"
				+ " INNER JOIN `col_per_uni_det` cpud ON cpu.id=cpud.`id_cpu`"
				+ " WHERE cpu.`id`=? "
				+ " GROUP BY cpud.`id_cpu`;";
		return sqlUtil.query(sql, new Integer[]{id_cpu});
 
	}
	
	public List<Row> listarUnidadesPeriodoxAnio(Integer id_anio, Integer id_tra) {

		String sql = "SELECT DISTINCT cpu.id id, cpu.id_cpa cpu_id_cpa , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini, cpu.numu_fin cpu_numu_fin ,cpu.est cpu_est, cpu.fec_ini cpu_fec_ini, cpu.fec_fin cpu_fec_fin,  cpu.fec_ini_ing cpu_fec_ini_ing, cpu.fec_fin_ing cpu_fec_fin_ing, cpu.id_anio cpu_id_anio, cpu.id_gir cpu_id_gir \n" ; 
				sql +="		, cpan.id cpan_id  , cpan.id_niv cpan_id_niv , cpan.id_cpa cpan_id_cpa  \n" ;
				sql +="		, niv.id niv_id , niv.nom niv_nom\n" ;
				sql +="		, cpa.id cpa_id, cpa.nom cpa_nom \n" ;
				sql +="		, gir.id gir_id, gir.nom gir_nom \n" ; 
				sql +="		 FROM col_per_uni cpu\n" ;
				sql +="		 LEFT JOIN cat_per_aca_nivel cpan ON cpan.id = cpu.id_cpa \n" ;
				sql +="		 LEFT JOIN cat_nivel niv ON cpan.id_niv=niv.id \n" ; 
				sql +="		 LEFT JOIN cat_periodo_aca cpa ON cpa.id=cpan.id_cpa \n" ; 
				sql +="		 LEFT JOIN ges_giro_negocio gir ON gir.id=cpan.id_gir \n" ; 
				sql +="		 LEFT JOIN col_nivel_coordinador cniv ON gir.id=cniv.id_gir AND cniv.id_niv=niv.id\n" ;
				sql +="		 WHERE cpu.`id_anio`=? \n" ;
				if(id_tra!=null) {
					sql +="		AND cniv.id_tra="+id_tra ;
				}
				sql +="		 ORDER BY gir.`id`, niv.`id`";
		return sqlUtil.query(sql, new Integer[]{id_anio});
 
	}
}
