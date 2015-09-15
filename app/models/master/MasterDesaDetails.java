package models.master;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;
import org.sql2o.Query;
import play.Logger;

import java.util.List;

/**
 * Created by lenovo on 9/13/2015.
 */
public class MasterDesaDetails {
    private static final int PAGE_NUMBER = 50;

    @JsonProperty("desa_id")
    int desaId;
    @JsonProperty("desa")
    String desa;
    @JsonProperty("desa_code")
    String desaCode;
    @JsonProperty("kecamatan")
    String kecamatan;
    @JsonProperty("kecamatan_code")
    String kecamatanCode;
    @JsonProperty("kabupaten")
    String kabupaten;
    @JsonProperty("kabupaten_code")
    String kabupatenCode;
    @JsonProperty("provinsi")
    String provinsi;
    @JsonProperty("provinsi_code")
    String provinsiCode;

    public int getDesaId() {
        return desaId;
    }
    public String getDesa() {
        return desa;
    }
    public String getDesaCode() {
        return desaCode;
    }
    public String getKecamatan() {
        return kecamatan;
    }
    public String getKecamatanCode() {
        return kecamatanCode;
    }
    public String getKabupaten() {
        return kabupaten;
    }
    public String getKabupatenCode() {
        return kabupatenCode;
    }
    public String getProvinsi() {
        return provinsi;
    }
    public String getProvinsiCode() {
        return provinsiCode;
    }

    public static List<MasterDesaDetails> select(int provId, int kabId, int kecId, int desaId, int page) {
        Logger.debug(provId + " " + kabId + " " + kecId + " " + desaId + " page: " + page);
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "SELECT " +
                    "ds.id as desaId, ds.name as desa, ds.code as desacode, " +
                    "kc.name as kecamatan, kc.code as kecamatancode, " +
                    "kb.name as kabupaten, kb.code as kabupatencode, " +
                    "pv.name as provinsi, pv.code as provinsicode " +
                    "FROM" +
                    "(SELECT id, name, code " +
                    "FROM master_provinsi WHERE (:provId = -1 or id = :provId)) AS pv, " +
                    "(SELECT id, name, code, provinsi_id " +
                    "FROM master_kabupaten WHERE (:kabId = -1 or id = :kabId)) AS kb, " +
                    "(SELECT id, name, code, kabupaten_id " +
                    "FROM master_kecamatan WHERE (:kecId = -1 or id = :kecId)) AS kc, " +
                    "(SELECT id, name, code, kecamatan_id " +
                    "FROM master_desa WHERE (:desaId = -1 or id = :desaId)) AS ds " +
                    "WHERE " +
                    "pv.id = kb.provinsi_id AND " +
                    "kb.id = kc.kabupaten_id AND " +
                    "kc.id = ds.kecamatan_id ";

            if (page > 0) {
                sql = sql + "OFFSET :offset LIMIT :limit;";
                return con.createQuery(sql)
                        .addParameter("provId", provId)
                        .addParameter("kabId", kabId)
                        .addParameter("kecId", kecId)
                        .addParameter("desaId", desaId)
                        .addParameter("offset", (page - 1) * PAGE_NUMBER)
                        .addParameter("limit", PAGE_NUMBER)
                        .executeAndFetch(MasterDesaDetails.class);
            } else {
                return con.createQuery(sql)
                        .addParameter("provId", provId)
                        .addParameter("kabId", kabId)
                        .addParameter("kecId", kecId)
                        .addParameter("desaId", desaId)
                        .executeAndFetch(MasterDesaDetails.class);
            }
        }
    }

    public static int setUnitOfDesa (int desaId, int unitId, int seasonId) {
        try (Connection con = DB.sql2o.open()) {
            String checkValidUnitSeasonSql =
                    "SELECT unit_active_season_id FROM master_unit_active_season " +
                    "WHERE unit_id = :unitId AND season_id = :seasonId";


            Integer unitActiveSeasonId = con.createQuery(checkValidUnitSeasonSql)
                                        .addParameter("unitId", unitId)
                                        .addParameter("seasonId", seasonId)
                                        .executeScalar(Integer.class);

            // case: invalid unit and season
            // e.g. unit is inactive during the given season
            if (unitActiveSeasonId == null) return -1;

            String assignUnitInsertSql =
                    "INSERT INTO master_desa_unit_assignment (desa_id, unit_active_season_id) " +
                    "VALUES (:desaId, :unitActiveSeasonId)";

            String assignUnitUpdateSql =
                    "UPDATE master_desa_unit_assignment SET unit_active_season_id = :unitActiveSeasonId " +
                    "WHERE desa_id = :desaId";

            try {
                return con.createQuery(assignUnitInsertSql)
                        .addParameter("desaId", desaId)
                        .addParameter("unitActiveSeasonId", unitActiveSeasonId)
                        .executeUpdate()
                        .getResult();
            } catch (Exception e) {
                // case when desa already assigned to a unit
                return con.createQuery(assignUnitUpdateSql)
                        .addParameter("desaId", desaId)
                        .addParameter("unitActiveSeasonId", unitActiveSeasonId)
                        .executeUpdate()
                        .getResult();
            }
        }
    }
}
