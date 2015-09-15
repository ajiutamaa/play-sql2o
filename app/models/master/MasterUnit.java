package models.master;

import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.DB;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by lenovo on 9/15/2015.
 */
public class MasterUnit {
    @JsonProperty("unit_id")
    int id;
    @JsonProperty("unit_name")
    String name;

    public static List<MasterUnit> select(int seasonId) {
        try (Connection con = DB.sql2o.open()) {
            String sql =
                    "SELECT DISTINCT(id), name " +
                    "FROM master_unit_active_season uas, master_unit u " +
                    "WHERE (:seasonId = -1 OR uas.season_id = :seasonId) AND uas.unit_id = u.id;";
            return con
                    .createQuery(sql)
                    .addParameter("seasonId", seasonId)
                    .executeAndFetch(MasterUnit.class);
        }
    }
}
