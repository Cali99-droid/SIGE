package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.PagoRealizadoDAOImpl;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad pago_realizado.
 * @author MV
 *
 */
@Repository
public class PagoRealizadoDAO extends PagoRealizadoDAOImpl{
	final static Logger logger = Logger.getLogger(PagoRealizadoDAO.class);

	public List<Map<String,Object>> consultarReportePagos(Integer id_au, Integer id_anio_sig){
		String sql ="select t2.* from ("
				+ "\n select alu.id, t.alumno,  mat_sig.id id_mat_sig, "
				+ "\n GROUP_CONCAT(CASE WHEN t.tip='MAT' THEN t.nro_rec END) AS 'mat_nro_rec',"
				+ "\n GROUP_CONCAT(CASE WHEN t.tip='MAT' THEN  t.monto_total END)   AS 'mat_monto'," // t.mens IS NULL
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN t.tip='ING' AND t.mens IS NULL THEN t.nro_rec END),'') AS 'ing_nro_rec', "
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN t.tip='ING' AND t.mens IS NULL THEN  t.monto_total END),'')   AS 'ing_monto',  "
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=3 and t.monto_total is not null THEN t.nro_rec else null END),'') AS  'marzo_nro_rec',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=3  AND t.canc=1 THEN t.monto_total else null END),'') AS  'marzo_monto',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=3   AND t.canc=1 THEN t.banco ELSE NULL END),'') AS  'marzo_banco',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=4 and t.monto_total is not null THEN t.nro_rec else null END),'') AS  'abril_nro_rec',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=4   AND t.canc=1 THEN t.monto_total else null END),'') AS  'abril_monto',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=4   AND t.canc=1 THEN t.banco ELSE NULL END),'') AS  'abril_banco',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=5  and t.monto_total is not null THEN t.nro_rec else null END),'') AS  'mayo_nro_rec',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=5   AND t.canc=1 THEN t.monto_total else null END),'') AS  'mayo_monto',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=5   AND t.canc=1 THEN t.banco ELSE NULL END),'') AS  'mayo_banco',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=6 and t.monto_total is not null THEN t.nro_rec else null END),'') AS  'junio_nro_rec',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=6   AND t.canc=1 THEN t.monto_total else null END),'') AS  'junio_monto',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=6   AND t.canc=1 THEN t.banco ELSE NULL END),'') AS  'junio_banco',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=7 and t.monto_total is not null THEN t.nro_rec else null END),'') AS  'julio_nro_rec',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=7  AND t.canc=1 THEN t.monto_total else null END),'') AS  'julio_monto',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=7  AND t.canc=1 THEN t.banco ELSE NULL END),'') AS  'julio_banco',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=8 and t.monto_total is not null THEN t.nro_rec else null END),'') AS  'agosto_nro_rec',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=8   AND t.canc=1 THEN t.monto_total else null END),'') AS  'agosto_monto',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=8  AND t.canc=1 THEN t.banco ELSE NULL END),'') AS  'agosto_banco',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=9 and t.monto_total is not null THEN t.nro_rec else null END),'') AS  'setiembre_nro_rec',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=9   AND t.canc=1 THEN t.monto_total else null END),'') AS  'setiembre_monto',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=3  AND t.canc=1 THEN t.banco ELSE NULL END),'') AS  'setiembre_banco',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=10 and t.monto_total is not null THEN t.nro_rec else null END),'') AS  'octubre_nro_rec',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=10   AND t.canc=1 THEN t.monto_total else null END),'') AS  'octubre_monto',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=10   AND t.canc=1 THEN t.banco ELSE NULL END),'') AS  'octubre_banco',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=11 and t.monto_total is not null THEN t.nro_rec else null END),'') AS  'noviembre_nro_rec',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=11  AND t.canc=1  THEN t.monto_total else null END),'') AS  'noviembre_monto',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=11   AND t.canc=1 THEN t.banco ELSE NULL END),'') AS  'noviembre_banco',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=12 and t.monto_total is not null THEN t.nro_rec else null END),'') AS  'diciembre_nro_rec',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=12   AND t.canc=1 THEN t.monto_total else null END),'') AS  'diciembre_monto',"
				+ "\n COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=12   AND t.canc=1 THEN t.banco ELSE NULL END),'') AS  'diciembre_banco'"
				+ "\n from alu_alumno alu left join"
				+ "\n (SELECT CONCAT(pera.ape_pat, ' ', pera.ape_mat,' ', pera.nom) AS 'alumno', fac.mens, fac.`nro_rec`, fac.`monto_total`, fac.fec_pago, fac.banco, alu.id id_alu, fac.tip, mat.id_au_asi, fac.canc"
				+ "\n FROM `mat_matricula` mat"
				+ "\n LEFT JOIN fac_academico_pago fac ON mat.id=fac.`id_mat`"
				+ "\n LEFT JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id` "
				+ "\n LEFT JOIN `col_persona` pera ON alu.`id_per`=pera.`id` "
				+ "\n LEFT JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ "\n LEFT JOIN `per_periodo` per ON per.`id`=mat.`id_per`"
				+ "\n WHERE mat.id_au_asi=?  ) t on t.id_alu = alu.id "
				+ "\n left join (mat_matricula mat_sig inner join per_periodo pp on pp.id= mat_sig.id_per and pp.id_anio="+id_anio_sig+" and pp.id_tpe=1) ON mat_sig.id_alu=t.id_alu "
				+ "\n group by t.id_alu "
				+ "\n ) t2 where t2.mat_nro_rec is not null"
				+ "\n order by t2.alumno";
		
		
		return jdbcTemplate.queryForList(sql, new Object[]{id_au} );	
	}
	
	public List<Map<String,Object>> listaDeudas(Integer id_anio, Integer id_niv, Integer id_gra, Integer id_au, Integer mes, String mat, String ord){
		//String sql = "SELECT f.nro_doc, p.* FROM `fac_academico_pago` p inner join mat_matricula m on m.id=p.id_mat inner join alu_familiar f on f.id=m.id_fam WHERE p.banco IS NOT NULL AND p.fec_pago=? order by p.nro_rec";
		String sql = "SELECT alu.id, t.alumno, t.familiar, t.celular, t.corr correo, t.direccion,t.situacion, t.aula, t.id_niv, t.id_gra, t.secc,\n" ; 
		if(mes!=null) {
			for (int i=3; i<=mes; i++) {
				if(i==3) {
					sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=3 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=3 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'marzo_monto'\n" ;	
				} else if(i==4) {
					sql += " , COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=4 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=4 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'abril_monto'\n";
				} else if(i==5) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=5 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=5 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'mayo_monto' \n";
				} else if(i==6) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=6 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=6 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'junio_monto' \n";
				} else if(i==7) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=7 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=7 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'julio_monto'\n";
				} else if(i==8) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=8 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=8 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'agosto_monto' \n";
				} else if(i==9) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=9 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=9 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'setiembre_monto'\n"; 
				} else if(i==10) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=10 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=10 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'octubre_monto'\n"; 
				} else if(i==11) {
					sql +=", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=11 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=11 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'noviembre_monto'\n";
				} else if(i==12) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=12 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=12 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'diciembre_monto'\n";
				}			
			}
		} else {
			sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=3 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=3 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'marzo_monto',\n" ;	
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=4 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=4 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'abril_monto',\n";
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=5 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=5 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'mayo_monto',\n";
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=6 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=6 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'junio_monto',\n";
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=7 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=7 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'julio_monto',\n";
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=8 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=8 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'agosto_monto',\n";
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=9 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=9 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'setiembre_monto',\n"; 
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=10 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=10 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'octubre_monto',\n"; 
			sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=11 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=11 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'noviembre_monto',\n";
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=12 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=12 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'diciembre_monto'\n";
		}		
				sql += " ,1 FROM alu_alumno alu INNER JOIN\n"; 
				sql += " (SELECT CONCAT(pers.ape_pat, ' ', pers.ape_mat,' ', pers.nom) AS 'alumno',CONCAT(persf.ape_pat, ' ', persf.ape_mat,' ', persf.nom) AS 'familiar', persf.cel celular, persf.corr,  gf.`direccion`, sit.nom situacion, fac.mens, fac.`nro_rec`, fac.`monto`, fac.`monto_total`, fac.fec_pago, fac.banco, alu.id id_alu, fac.tip, mat.id_au_asi, fac.canc, per.id id_per\n" ; 
				sql += ",CONCAT(suc.abrv,' - ',niv.nom,' - ', gra.nom,' ', au.secc) aula, niv.id id_niv, gra.id id_gra, au.secc\n" ;
				sql += " FROM `mat_matricula` mat\n" ;
				sql += " INNER JOIN fac_academico_pago fac ON mat.id=fac.`id_mat`\n" ; 
				sql += " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`\n" ; 
				sql += " INNER JOIN col_persona pers ON alu.id_per=pers.id\n" ; 
				sql += " INNER JOIN `alu_familiar` fam ON mat.id_fam_res_pag=fam.id\n" ; 
				sql += " INNER JOIN `alu_gru_fam_familiar` agf ON fam.id=agf.`id_fam`\n" ; 
				sql += " INNER JOIN `alu_gru_fam` gf ON agf.`id_gpf`=gf.id\n" ;
				sql += " INNER JOIN `alu_gru_fam_alumno` agfa ON agfa.id_alu=alu.id AND agfa.id_gpf=gf.id \n";
				sql += " INNER JOIN col_persona persf ON fam.id_per=persf.id\n" ; 
				sql += " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\n" ;
				sql += " INNER JOIN `per_periodo` per ON per.`id`=mat.`id_per`\n" ; 
				sql += " INNER JOIN ges_sucursal suc ON per.id_suc=suc.id \n";
				sql += " INNER JOIN `cat_grad` gra ON au.id_grad=gra.id\n" ;
				sql += " INNER JOIN `cat_nivel` niv ON gra.id_nvl=niv.id\n" ; 
				sql += " LEFT JOIN `cat_col_situacion` sit ON mat.`id_sit`=sit.`id`";
				sql += " WHERE per.id_anio="+id_anio+" AND per.`id_tpe`=1 AND fac.tip='MEN' AND fac.canc=0 \n" ;
				sql += " AND fac.mens in (";
				if(mes==null) {
					for (int i=3; i<=12; i++) {
						/*if(mes==3) {
							sql += "3";
						} else {*/
							sql += ""+i+",";
						//}	
					}
				} else {
					for (int i=3; i<=mes; i++) {
						/*if(mes==3) {
							sql += "3";
						} else {*/
							sql += ""+i+",";
						//}	
					}
				}
				
				sql += "3)";
				if(id_niv!=null) {
					sql +=" AND per.id_niv="+id_niv;
				}
				if(id_gra!=null) {
					sql +=" AND au.id_grad="+id_gra;
				}
				if(id_au!=null) {
					sql +=" AND au.id="+id_au;
				}
				if(mat!=null) {
					if(mat.equals("S")) {
						sql +=" AND (mat.id_sit NOT IN (5) OR mat.id_sit IS NULL)";
					} else if(mat.equals("N")) {
						sql +=" AND mat.id_sit IN (5)";
					}
				}
				sql += " ) t ON t.id_alu = alu.id \n" ; 
				sql += " LEFT JOIN `mat_tarifas_emergencia` emer ON t.id_per=emer.id_per AND t.mens=emer.`mes`\n" ; 
				sql += " GROUP BY t.id_alu \n" ;
				sql += " ORDER BY";
				if(ord.equals("A"))
					sql +=" t.alumno";
				else if (ord.equals("AU")) {
					sql +=" t.id_niv, t.id_gra, t.secc , t.alumno";
				}
		logger.info(sql);
		return jdbcTemplate.queryForList(sql);	
	
	}
	
	public List<Map<String,Object>> listaDeudasAcadVac(Integer id_anio, Integer id_gir,Integer id_cic, Integer id_niv, Integer id_gra, Integer id_au, Integer mes, String mat, String ord){
		//String sql = "SELECT f.nro_doc, p.* FROM `fac_academico_pago` p inner join mat_matricula m on m.id=p.id_mat inner join alu_familiar f on f.id=m.id_fam WHERE p.banco IS NOT NULL AND p.fec_pago=? order by p.nro_rec";
		String sql = "SELECT alu.id, t.alumno, t.responsable, t.celular, t.direccion, t.corr correo, t.situacion, t.ciclo, t.aula, t.id_niv, t.id_gra, t.secc,\n" ; 
		/*if(mes!=null) {
			for (int i=1; i<=mes; i++) {
				if(i==1) {
					sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.nro_cuota=1 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.nro_cuota=1 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'cuota1'\n" ;	
				} else if(i==2) {
					sql += " , COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.nro_cuota=2 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.nro_cuota=2 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'cuota2'\n";
				} else if(i==3) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.nro_cuota=3 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.nro_cuota=3 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'cuota3' \n";
				} else if(i==4) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.nro_cuota=4 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.nro_cuota=4 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'cuota4' \n";
				} else if(i==5) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.nro_cuota=5 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.nro_cuota=5 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'cuota5'\n";
				} else if(i==6) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.nro_cuota=6 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.nro_cuota=6 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'cuota6' \n";
				} else if(i==7) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.nro_cuota=7 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.nro_cuota=7 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'cuota7'\n"; 
				} else if(i==8) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.nro_cuota=8 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.nro_cuota=8 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'cuota8'\n"; 
				} else if(i==9) {
					sql +=", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.nro_cuota=9 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.nro_cuota=9 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'cuota9'\n";
				} else if(i==10) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.nro_cuota=10 AND t.canc=0 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.nro_cuota=10 AND t.canc=0 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'cuota10'\n";
				}			
			}
		//} else {*/
			sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.nro_cuota=1 AND t.canc=0 THEN t.monto END),'') AS  'cuota1',\n" ;	
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN t.nro_cuota=2 AND t.canc=0 THEN t.monto END),'') AS  'cuota2',\n";
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.nro_cuota=3 AND t.canc=0 THEN t.monto END),'') AS  'cuota3',\n";
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.nro_cuota=4 AND t.canc=0 THEN t.monto END),'') AS  'cuota4',\n";
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.nro_cuota=5 AND t.canc=0 THEN t.monto END),'') AS  'cuota5',\n";
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.nro_cuota=6 AND t.canc=0 THEN t.monto END),'') AS  'cuota6',\n";
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.nro_cuota=7 AND t.canc=0 THEN t.monto END),'') AS  'cuota7',\n"; 
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.nro_cuota=8 AND t.canc=0 THEN t.monto END),'') AS  'cuota8',\n"; 
			sql +=" COALESCE(GROUP_CONCAT(CASE WHEN   t.nro_cuota=9 AND t.canc=0 THEN t.monto END),'') AS  'cuota9',\n";
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.nro_cuota=10 AND t.canc=0 THEN t.monto END),'') AS  'cuota10'\n";
		//}		
				sql += " ,1 FROM alu_alumno alu INNER JOIN\n"; 
				sql += " (SELECT CONCAT(pers.ape_pat, ' ', pers.ape_mat,' ', pers.nom) AS 'alumno',CONCAT(peres.ape_pat, ' ', peres.ape_mat,' ', peres.nom) AS 'responsable', peres.cel celular, peres.corr,  peres.dir `direccion`, sit.nom situacion, fac.nro_cuota, fac.`nro_rec`, fac.`monto`, fac.`monto_total`, fac.fec_pago, fac.banco, alu.id id_alu, fac.tip, mat.id_au_asi, fac.canc, per.id id_per\n" ; 
				sql += ",CONCAT(suc.abrv,' - ',niv.nom,' - ', gra.nom,' ', au.secc) aula, cic.nom ciclo, cic.id id_cic, niv.id id_niv, gra.id id_gra, au.secc\n" ;
				sql += " FROM `mat_matricula` mat\n" ;
				sql += " INNER JOIN fac_academico_pago fac ON mat.id=fac.`id_mat`\n" ; 
				sql += " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`\n" ; 
				sql += " INNER JOIN col_persona pers ON alu.id_per=pers.id\n" ; 
				sql += " INNER JOIN col_persona peres ON mat.id_per_res=peres.id\n" ; 
				sql += " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\n" ;
				sql += " INNER JOIN `per_periodo` per ON per.`id`=mat.`id_per`\n" ; 
				sql += " INNER JOIN ges_sucursal suc ON per.id_suc=suc.id \n";
				sql += " INNER JOIN `col_ciclo` cic ON per.`id`=cic.`id_per` AND mat.`id_cic`=cic.id\n" ; 
				sql += " INNER JOIN `cat_grad` gra ON au.id_grad=gra.id\n" ;
				sql += " INNER JOIN `cat_nivel` niv ON gra.id_nvl=niv.id\n" ; 
				sql += " INNER JOIN `ges_servicio` srv ON per.id_srv=srv.id\n" ; 
				//sql += " INNER JOIN `ges_servicio` srv ON per.id_srv=srv.id\n" ; 
				sql += " LEFT JOIN `cat_col_situacion` sit ON mat.`id_sit`=sit.`id`";
				sql += " WHERE per.id_anio="+id_anio+" AND srv.id_gir="+id_gir+" AND fac.canc=0 \n" ;
				if(id_cic!=null) {
					sql +=" AND cic.id="+id_cic;
				}
				if(id_niv!=null) {
					sql +=" AND per.id_niv="+id_niv;
				}
				if(id_gra!=null) {
					sql +=" AND au.id_grad="+id_gra;
				}
				if(id_au!=null) {
					sql +=" AND au.id="+id_au;
				}
				if(mat!=null) {
					if(mat.equals("S")) {
						sql +=" AND (mat.id_sit NOT IN (5) OR mat.id_sit IS NULL)";
					} else if(mat.equals("N")) {
						sql +=" AND mat.id_sit IN (5)";
					}
				}
				sql += " ) t ON t.id_alu = alu.id \n" ; 
				//sql += " LEFT JOIN `mat_tarifas_emergencia` emer ON t.id_per=emer.id_per AND t.mens=emer.`mes`\n" ; 
				sql += " GROUP BY t.id_alu,t.id_cic \n" ;
				sql += " ORDER BY";
				if(ord.equals("A"))
					sql +=" t.alumno";
				else if (ord.equals("AU")) {
					sql +=" t.id_niv, t.id_gra, t.secc , t.alumno";
				}
		logger.info(sql);
		return jdbcTemplate.queryForList(sql);	
	
	}
	
	public List<Map<String,Object>> listaPagados(Integer id_anio, Integer id_niv, Integer id_gra, Integer id_au, Integer mes, String mat, String ord){
		//String sql = "SELECT f.nro_doc, p.* FROM `fac_academico_pago` p inner join mat_matricula m on m.id=p.id_mat inner join alu_familiar f on f.id=m.id_fam WHERE p.banco IS NOT NULL AND p.fec_pago=? order by p.nro_rec";
		String sql = "SELECT alu.id, t.alumno, t.familiar, t.celular, t.direccion,t.situacion, t.aula, t.id_niv, t.id_gra, t.secc,\n" ; 
		if(mes!=null) {
			for (int i=3; i<=mes; i++) {
				if(i==3) {
					sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=3 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=3 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'marzo_monto'\n" ;	
					sql +=", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=3 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'marzo_recibo'\n" ;	
				} else if(i==4) {
					sql += " , COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=4 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=4 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'abril_monto'\n";
					sql +=", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=4 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'abril_recibo'\n" ;
				} else if(i==5) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=5 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=5 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'mayo_monto' \n";
					sql +=", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=4 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'mayo_recibo'\n" ;
				} else if(i==6) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=6 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=6 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'junio_monto' \n";
					sql +=", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=6 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'junio_recibo'\n" ;
				} else if(i==7) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=7 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=7 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'julio_monto'\n";
					sql +=", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=7 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'julio_recibo'\n" ;
				} else if(i==8) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=8 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=8 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'agosto_monto' \n";
					sql +=", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=8 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'agosto_recibo'\n" ;
				} else if(i==9) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=9 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=9 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'setiembre_monto'\n"; 
					sql +=", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=9 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'setiembre_recibo'\n" ;
				} else if(i==10) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=10 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=10 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'octubre_monto'\n"; 
					sql +=", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=10 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'octubre_recibo'\n" ;
				} else if(i==11) {
					sql +=", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=11 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=11 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'noviembre_monto'\n";
					sql +=", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=11 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'noviembre_recibo'\n" ;
				} else if(i==12) {
					sql += ", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=12 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=12 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'diciembre_monto'\n";
					sql +=", COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=12 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'diciembre_recibo'\n" ;
				}			
			}
		} else {
			sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=3 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=3 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'marzo_monto',\n" ;	
			sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=3 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'marzo_recibo' ,\n" ;
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=4 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=4 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'abril_monto',\n";
			sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=4 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'abril_recibo' ,\n" ;
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=5 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=5 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'mayo_monto',\n";
			sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=5 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'mayo_recibo' ,\n" ;
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=6 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=6 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'junio_monto',\n";
			sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=6 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'junio_recibo' ,\n" ;
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=7 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=7 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'julio_monto',\n";
			sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=7 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'julio_recibo' ,\n" ;
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=8 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=8 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'agosto_monto',\n";
			sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=8 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'agosto_recibo' ,\n" ;
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=9 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=9 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'setiembre_monto',\n"; 
			sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=9 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'setiembre_recibo' ,\n" ;
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=10 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=10 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'octubre_monto',\n"; 
			sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=10 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'octubre_recibo' ,\n" ;
			sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=11 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=11 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'noviembre_monto',\n";
			sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=11 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'noviembre_recibo' ,\n" ;
			sql += " COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=12 AND t.canc=1 AND emer.id IS NULL THEN t.monto WHEN t.tip='MEN' AND t.mens=12 AND t.canc=1 AND emer.id IS NOT NULL THEN emer.monto ELSE NULL END),'') AS  'diciembre_monto', \n";
			sql +=" COALESCE(GROUP_CONCAT(CASE WHEN  t.tip='MEN' AND t.mens=12 AND t.canc=1 THEN t.nro_rec  ELSE NULL END),'') AS  'diciembre_recibo' \n" ;
		}		
				sql += " ,1 FROM alu_alumno alu INNER JOIN\n"; 
				sql += " (SELECT CONCAT(pers.ape_pat, ' ', pers.ape_mat,' ', pers.nom) AS 'alumno',CONCAT(persf.ape_pat, ' ', persf.ape_mat,' ', persf.nom) AS 'familiar', persf.cel celular, gf.`direccion`, sit.nom situacion, fac.mens, fac.`nro_rec`, fac.`monto`, fac.`monto_total`, fac.fec_pago, fac.banco, alu.id id_alu, fac.tip, mat.id_au_asi, fac.canc, per.id id_per\n" ; 
				sql += ",CONCAT(niv.nom,' - ', gra.nom,' ', au.secc) aula, niv.id id_niv, gra.id id_gra, au.secc\n" ;
				sql += " FROM `mat_matricula` mat\n" ;
				sql += " INNER JOIN fac_academico_pago fac ON mat.id=fac.`id_mat`\n" ; 
				sql += " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`\n" ; 
				sql += " INNER JOIN col_persona pers ON alu.id_per=pers.id\n" ; 
				sql += " INNER JOIN `alu_familiar` fam ON mat.id_fam=fam.id\n" ; 
				sql += " INNER JOIN `alu_gru_fam_familiar` agf ON fam.id=agf.`id_fam`\n" ; 
				sql += " INNER JOIN `alu_gru_fam` gf ON agf.`id_gpf`=gf.id\n" ;
				sql += " INNER JOIN `alu_gru_fam_alumno` agfa ON agfa.id_alu=alu.id AND agfa.id_gpf=gf.id \n";
				sql += " INNER JOIN col_persona persf ON fam.id_per=persf.id\n" ; 
				sql += " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\n" ;
				sql += " INNER JOIN `per_periodo` per ON per.`id`=mat.`id_per`\n" ; 
				sql += " INNER JOIN `cat_grad` gra ON au.id_grad=gra.id\n" ;
				sql += " INNER JOIN `cat_nivel` niv ON gra.id_nvl=niv.id\n" ; 
				sql += " LEFT JOIN `cat_col_situacion` sit ON mat.`id_sit`=sit.`id`";
				sql += " WHERE per.id_anio="+id_anio+" AND per.`id_tpe`=1 AND fac.tip='MEN' AND fac.canc=1 \n" ;
				sql += " AND fac.mens in (";
				if(mes==null) {
					for (int i=3; i<=12; i++) {
						/*if(mes==3) {
							sql += "3";
						} else {*/
							sql += ""+i+",";
						//}	
					}
				} else {
					for (int i=3; i<=mes; i++) {
						/*if(mes==3) {
							sql += "3";
						} else {*/
							sql += ""+i+",";
						//}	
					}
				}
				sql += "3)";
				if(id_niv!=null) {
					sql +=" AND per.id_niv="+id_niv;
				}
				if(id_gra!=null) {
					sql +=" AND au.id_grad="+id_gra;
				}
				if(id_au!=null) {
					sql +=" AND au.id="+id_au;
				}
				if(mat!=null) {
					if(mat.equals("S")) {
						sql +=" AND (mat.id_sit NOT IN (5) OR mat.id_sit IS NULL)";
					} else if(mat.equals("N")) {
						sql +=" AND mat.id_sit IN (5)";
					}
				}
				sql += " ) t ON t.id_alu = alu.id \n" ; 
				sql += " LEFT JOIN `mat_tarifas_emergencia` emer ON t.id_per=emer.id_per AND t.mens=emer.`mes`\n" ; 
				sql += " GROUP BY t.id_alu \n" ;
				sql += " ORDER BY";
				if(ord.equals("A"))
					sql +=" t.alumno";
				else if (ord.equals("AU")) {
					sql +=" t.id_niv, t.id_gra, t.secc , t.alumno";
				}
		logger.info(sql);
		return jdbcTemplate.queryForList(sql);	
	
	}
}
