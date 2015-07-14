package tk.patolivre.patobot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tk.patolivre.patobot.webservice.clients.PatoClient;
import tk.patolivre.patobot.webservice.response.IGenericResult;

/**
 * Created by Ricardo on 11/07/2015.
 */
public class MainActivity extends AppCompatActivity {

    @Bind(R.id.buttonDown) ImageButton mButtonDown;
    @Bind(R.id.buttonUp) ImageButton mButtonUp;
    @Bind(R.id.buttonLeft) ImageButton mButtonLeft;
    @Bind(R.id.buttonRight) ImageButton mButtonRight;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    private String mEnderecoServidor = "http://";
    private AsyncHttpClient mClient;

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
        showDialog();

    }

    private void showDialog(){
        Dialog.Builder builder = null;
        builder = new SimpleDialog.Builder(R.style.SimpleDialog){

            @Override
            protected void onBuildDone(Dialog dialog) {
                dialog.layoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                EditText editTextServidor = (EditText)fragment.getDialog().findViewById(R.id.editTextServidor);
                mEnderecoServidor = mEnderecoServidor + editTextServidor.getText().toString();
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

    @OnClick(R.id.buttonLeft) void left() {
        String url = mEnderecoServidor + "/girar/esquerda";
       moverPato(url);
    }

    @OnClick(R.id.buttonUp) void up() {
        String url = mEnderecoServidor + "/andar/frente";
        moverPato(url);
    }

    @OnClick(R.id.buttonRight) void right() {
        String url = mEnderecoServidor + "/girar/direita";
        moverPato(url);
    }

    @OnClick(R.id.buttonDown) void down() {
        String url = mEnderecoServidor+ "/andar/tras";
        moverPato(url);
    }

    private void moverPato(String url) {
        mClient.get(url, new AsyncHttpResponseHandler() {
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
                // Response failed :(
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
