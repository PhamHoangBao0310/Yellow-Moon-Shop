/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.utils;

import baoph.tblUser.GoogleUser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.log4j.Logger;

/**
 *
 * @author DELL
 */
public class GoogleUltils {

    static Logger logger = Logger.getLogger(GoogleUltils.class);

    public static String getToken(String code) throws IOException {
        String response = Request.Post(GoogleHelper.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form().add("client_id", GoogleHelper.GOOGLE_CLIENT_ID)
                        .add("client_secret", GoogleHelper.GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", GoogleHelper.GOOGLE_REDIRECT_URI)
                        .add("code", code)
                        .add("grant_type", GoogleHelper.GOOGLE_GRANT_TYPE)
                        .build())
                .execute()
                .returnContent()
                .asString();
        logger.info("Response Token : " + response);
        JsonObject jsonObj = new Gson().fromJson(response, JsonObject.class);
        String accessToken = jsonObj.get("access_token").toString().replaceAll("\"", "");
        return accessToken;
    }

    public static GoogleUser getUserInfo(String accessToken) throws IOException {
        String link = GoogleHelper.GOOGLE_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        logger.info("Response User  " + response);
        GoogleUser user = new Gson().fromJson(response, GoogleUser.class);
        return user;
    }

}
