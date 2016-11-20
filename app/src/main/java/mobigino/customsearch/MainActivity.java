package mobigino.customsearch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private EditText searchBoxTxt;
    private Button searchBtn;
    private TextView resultTxt;
    String searchStr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBoxTxt = (EditText) findViewById(R.id.searchBox);
        searchBtn = (Button) findViewById(R.id.btnSearch);
        resultTxt=(TextView)findViewById(R.id.result);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                searchStr = searchBoxTxt.getText().toString().replace(" ","+");
                try {
                    searchStr=URLEncoder.encode("Flowers shops in usa \"sales@*.com\"", "UTF-8");//searchBoxTxt.getText().toString()
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                new CustomSearchServiceExecuter().execute();
            }
        });
    }

    private class CustomSearchServiceExecuter extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        String searchQuery="https://www.googleapis.com/customsearch/v1?key=AIzaSyDXeC0zVYrxxzEc5OHWha6T-s4JfF1wAgk&cx=017576662512468239146:omuauf_lfve&q="+searchStr;

        public CustomSearchServiceExecuter() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null)
                progressDialog = ProgressDialog.show(MainActivity.this, "Custom Search", "Searching Data...", true, false);

        }

        @Override
        protected String doInBackground(String... urls) {
            System.out.println("========" + searchQuery);
            String data=getXmlFromUrl(searchQuery);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog = null;
            resultTxt.setText(result);
        }
    }

    public String getXmlFromUrl(String url) {
        String xml = null;

        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return xml;
    }
}
