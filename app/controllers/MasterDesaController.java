package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.master.MasterDesaDetails;
import models.master.MasterKabupaten;
import models.master.MasterKecamatan;
import models.master.MasterProvinsi;
import play.mvc.*;

import static play.libs.Json.toJson;

public class MasterDesaController extends Controller {

    public Result findDesa(int provId, int kabId, int kecId, int desaId, int page) {
        try {
            return ok(toJson(MasterDesaDetails.select(provId, kabId, kecId, desaId, page)));
        } catch (Exception e) {
            return internalServerError("Database connection error");
        }
    }

    public Result findProvinsi() {
        return ok(toJson(MasterProvinsi.select()));
    }

    public Result findKabupaten(int provinsiId) {
        return ok(toJson(MasterKabupaten.select(provinsiId)));
    }

    public Result assignDesasToUnit() {
        try {
            JsonNode json = Controller.request().body().asJson();
            int unitId = json.get("unit_id").asInt();
            int seasonId = json.get("season_id").asInt();
            for (JsonNode nodeDesaId : json.get("list_desa")) {
                int desaId = nodeDesaId.asInt();
                MasterDesaDetails.setUnitOfDesa(desaId, unitId, seasonId);
            }
        } catch (Exception e) {
            return badRequest("your request doesn't match the requirement format");
        }
        return ok("unit assigned successfully");
    }

    public Result findKecamatan(int kabupatenId) {
        return ok(toJson(MasterKecamatan.select(kabupatenId)));
    }
}
