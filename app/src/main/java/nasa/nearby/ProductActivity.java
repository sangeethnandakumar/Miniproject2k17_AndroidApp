package nasa.nearby;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;
import java.util.List;

import nasa.support.AppSettings;
import nasa.support.JsonParser;
import nasa.support.Product;
import nasa.support.SearchAdapter;
import nasa.support.ServerConnector;

public class ProductActivity extends Activity
{
    String search;
    AppSettings settings;
    ListView searchlist;
    ProgressDialog progress;
    SearchAdapter adapter;

    public void init()
    {
        settings=new AppSettings(getApplicationContext());
        searchlist=(ListView)findViewById(R.id.searchlist);
        progress=new ProgressDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        init();
        Intent i=getIntent();
        search=i.getStringExtra("search");
        settings.saveSettings("search",search);
        progress.setTitle("Please wait");
        progress.setMessage("Looking for products...");
        progress.show();

        ServerConnector server=new ServerConnector(getApplicationContext());
        server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
        {
            @Override
            public void onServerResponded(String responce)
            {
                JsonParser parser=new JsonParser(getApplicationContext(),responce);
                parser.setOnJsonParseListner(new JsonParser.OnProductsParserListner()
                {
                    @Override
                    public void onProductsParsed(List<Product> products)
                    {
                        adapter=new SearchAdapter(getApplicationContext(),products);
                        searchlist.setDivider(null);
                        searchlist.setAdapter(adapter);
                        progress.dismiss();
                    }
                });
                parser.parseProducts();
            }

            @Override
            public void onServerRevoked()
            {
                Toast.makeText(ProductActivity.this, "Something error occured", Toast.LENGTH_SHORT).show();
            }
        });

        server.connectServer(settings.retriveSettings("serverurl")+"/searchresult.php?like="+search);

        searchlist.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent x=new Intent(ProductActivity.this,BookingActivity.class);
                x.putExtra("search",i);
                startActivity(x);
            }
        });
    }
}
