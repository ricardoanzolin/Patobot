package tk.patolivre.patobot.webservice.response;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by developer on 22/01/15.
 */
public interface IGenericResult {
    public void onResult(JSONObject json);
    public void onResult(int statusCode, Header[] headers, byte[] responseBody);
    public void onFailure(String responseString, Throwable throwable);
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse);
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error);
}
