package models.master;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by lenovo on 9/15/2015.
 */
public class MasterProvinsi {
    @JsonProperty("provinsi_id")
    int provinsiId;
    @JsonProperty("provinsi")
    String provinsi;
    @JsonProperty("provinsi_code")
    String provinsiCode;

    public static List<MasterProvinsi> select() {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "SELECT id as provinsiId, name as provinsi, code as provinsiCode " +
                    "FROM master_provinsi";
            return con.createQuery(sql).executeAndFetch(MasterProvinsi.class);
        }
    }
}
