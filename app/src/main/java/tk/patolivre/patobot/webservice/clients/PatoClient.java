package tk.patolivre.patobot.webservice.clients;

import com.loopj.android.http.RequestParams;

import tk.patolivre.patobot.webservice.response.IGenericResult;

public class PatoClient extends GenericRestClient {

    public void moverPato(String url, final IGenericResult result) throws Exception {
        this.get(url, result);
    }
}
