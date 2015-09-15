package models.master;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by lenovo on 9/15/2015.
 */
public class MasterKabupaten {
    @JsonProperty("kabupaten_id")
    int kabupatenId;
    @JsonProperty("kabupaten")
    String kabupaten;
    @JsonProperty("kabupaten_code")
    String kabupatenCode;

    public static List<MasterKabupaten> select(int provId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "SELECT id as kabupatenId, name as kabupaten, code as kabupatenCode " +
                            "FROM master_kabupaten " +
                            "WHERE (:provId = -1 or provinsi_id = :provId)";
            return con.createQuery(sql)
                    .addParameter("provId", provId)
                    .executeAndFetch(MasterKabupaten.class);
        }
    }
}