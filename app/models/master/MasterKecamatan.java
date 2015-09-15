package models.master;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by lenovo on 9/15/2015.
 */
public class MasterKecamatan {
    @JsonProperty("kecamatan_id")
    int kecamatanId;
    @JsonProperty("kecamatan")
    String kecamatan;
    @JsonProperty("kecamatan_code")
    String kecamatanCode;

    public static List<MasterKecamatan> select(int kabId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "SELECT id as kecamatanId, name as kecamatan, code as kecamatanCode " +
                            "FROM master_kecamatan " +
                            "WHERE (:kabId = -1 or kabupaten_id = :kabId)";
            return con.createQuery(sql)
                    .addParameter("kabId", kabId)
                    .executeAndFetch(MasterKecamatan.class);
        }
    }
}