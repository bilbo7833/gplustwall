package de.devfest.gplustwall;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusRequest;

import de.devfest.gplustwall.utils.AuthUtils;

public final class PlusWrap {

    private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private final Plus plus;

    public PlusWrap(final Context ctx) {
        final SharedPreferences prefs = ctx.getSharedPreferences(AuthUtils.PREFS_NAME, 0);
        final String accessToken = prefs.getString("accessToken", null);
        final GoogleAccessProtectedResource protectedResource = new GoogleAccessProtectedResource(accessToken);

        plus = Plus.builder(HTTP_TRANSPORT, JSON_FACTORY)
           .setApplicationName("G+TWall")
           .setHttpRequestInitializer(protectedResource)
           .setJsonHttpRequestInitializer(new JsonHttpRequestInitializer() {
               @Override
               public void initialize(JsonHttpRequest request) {
                 PlusRequest plusRequest = (PlusRequest) request;
                 plusRequest.setKey("AIzaSyCzqPvA7jc4jSLUKe_iYSa81EdPSfF6GgA");
               }
           })
           .build();
      }

      public Plus get() {
        return plus;
      }

}
