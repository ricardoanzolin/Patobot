package tk.patolivre.patobot.webservice.clients;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import tk.patolivre.patobot.webservice.response.IGenericResult;

public abstract class GenericRestClient {
    private AsyncHttpClient mHTTPClient;
    public Gson mGson;

    public GenericRestClient() {
        mHTTPClient = new AsyncHttpClient();
        mHTTPClient.setConnectTimeout(30 * 1000);
        mHTTPClient.setResponseTimeout(30 * 1000);

        mGson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
    }



    protected void get(String url, final IGenericResult resultHandler) {
        mHTTPClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                resultHandler.onResult(statusCode, headers, responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                resultHandler.onFailure(statusCode, headers, responseBody, error);
            }
        });
        /**mHTTPClient.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                resultHandler.onResult(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                resultHandler.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });*/
    }

}
