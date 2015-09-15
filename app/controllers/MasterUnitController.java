package controllers;

import models.master.MasterUnit;
import play.mvc.*;
import static play.libs.Json.toJson;
import static play.mvc.Results.ok;

/**
 * Created by lenovo on 9/15/2015.
 */
public class MasterUnitController {
    public Result findUnit (int seasonId) {
        return ok(toJson(MasterUnit.select(seasonId)));
    }
}
