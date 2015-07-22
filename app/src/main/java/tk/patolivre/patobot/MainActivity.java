package tk.patolivre.patobot;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tk.patolivre.patobot.webservice.clients.PatoClient;
import tk.patolivre.patobot.webservice.response.IGenericResult;

/**
 * Created by Ricardo on 11/07/2015.
 */
public class MainActivity extends AppCompatActivity {

    @Bind(R.id.buttonDown)
    ImageButton mButtonDown;
    @Bind(R.id.buttonUp)
    ImageButton mButtonUp;
    @Bind(R.id.buttonLeft)
    ImageButton mButtonLeft;
    @Bind(R.id.buttonRight)
    ImageButton mButtonRight;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private String mEnderecoServidor;
    private String mPrefixHttp = "http://";
    private AsyncHttpClient mClient;
    private ArrayList<String> enderecos;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("PatoBot");

        mClient = new AsyncHttpClient();
        mClient.setConnectTimeout(30 * 3000);
        mClient.setResponseTimeout(30 * 3000);

        enderecos = new ArrayList<>();

        showDialog();

    }

    private void showDialog() {
        Dialog.Builder builder = null;
        builder = new SimpleDialog.Builder(R.style.SimpleDialog) {
            AutoCompleteTextView editTextServidor;

            @Override
            protected void onBuildDone(Dialog dialog) {
                dialog.layoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                editTextServidor = (AutoCompleteTextView) dialog.findViewById(R.id.editTextServidor);
            }

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {

                mEnderecoServidor = mPrefixHttp + editTextServidor.getText().toString().trim();
                if (!enderecos.contains(mEnderecoServidor)){
                    enderecos.add(mEnderecoServidor.trim());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,enderecos);
                editTextServidor.setAdapter(adapter);

                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                super.onNegativeActionClicked(fragment);
            }
        };

        builder.title("PatoBot Servidor").positiveAction("OK").contentView(R.layout.layout_dialog_custom);

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    @OnClick(R.id.buttonLeft)
    void left() {
        String url = mEnderecoServidor + "/girar/esquerda";
        moverPato(url);
    }

    @OnClick(R.id.buttonUp)
    void up() {
        String url = mEnderecoServidor + "/andar/frente";
        moverPato(url);
    }

    @OnClick(R.id.buttonRight)
    void right() {
        String url = mEnderecoServidor + "/girar/direita";
        moverPato(url);
    }

    @OnClick(R.id.buttonDown)
    void down() {
        String url = mEnderecoServidor + "/andar/tras";
        moverPato(url);
    }

    private void moverPato(String url) {
        mClient.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                super.onPreProcessResponse(instance, response);
            }

            @Override
            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                super.onPostProcessResponse(instance, response);
            }

            @Override
            public void onStart() {
                // Initiated the request
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(MainActivity.this, "Pato se moveu", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Error code: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_atualizar:
                showDialog();
                return true;
            case R.id.action_sobre:
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isNetworkConnected() {
        try {
            return new CheckConnection().execute("").get();
        } catch (Exception e) {
            return false;
        }
    }

    class CheckConnection extends AsyncTask<String, Void, Boolean> {
        Boolean isConnected;

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                InetAddress ipAddr = InetAddress.getByName(mEnderecoServidor);
                if (ipAddr.equals("")) {
                    isConnected = false;
                } else {
                    isConnected = true;
                }
            } catch (Exception e) {
                return false;
            }
            return isConnected;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (!isConnected){
                Toast.makeText(MainActivity.this, "Servidor não encontrado, verifique!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(MainActivity.this, "Servidor não encontrado, verifique!", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(aBoolean);
        }
    }
}
