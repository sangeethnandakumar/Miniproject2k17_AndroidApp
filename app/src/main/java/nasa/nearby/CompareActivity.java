package nasa.nearby;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.view.ProgressView;

import java.util.ArrayList;
import java.util.List;

import nasa.support.AppSettings;
import nasa.support.JsonParser;
import nasa.support.Product;
import nasa.support.ServerConnector;

public class CompareActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);


        // PRODUCT DATABASE MAPPING
        ServerConnector productfetching=new ServerConnector(getApplicationContext());
        productfetching.setOnServerStatusListner(new ServerConnector.OnServerStatusListner() {
            @Override
            public void onServerResponded(String responce)
            {
                JsonParser parser=new JsonParser(getApplicationContext(),responce);
                parser.setOnJsonParseListner(new JsonParser.OnProductsParserListner()
                {
                    @Override
                    public void onProductsParsed(List<Product> products)
                    {
                        List<String> productlist=new ArrayList<String>();
                        for (int i=0;i<products.size();i++)
                        {
                            productlist.add(products.get(i).getCompany()+" "+products.get(i).getProduct()+" in ["+products.get(i).getType().toUpperCase()+"] - "+String.valueOf(products.get(i).getId()));
                        }
                        AutoCompleteTextView searchA = (AutoCompleteTextView) findViewById(R.id.searchboxa);
                        AutoCompleteTextView searchB = (AutoCompleteTextView) findViewById(R.id.searchboxb);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.product_item,R.id.product_name, productlist);
                        searchA.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);
                        searchA.setDropDownBackgroundResource(R.color.black);
                        searchA.setThreshold(0);
                        searchA.setAdapter(adapter);
                        searchB.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);
                        searchB.setDropDownBackgroundResource(R.color.black);
                        searchB.setThreshold(0);
                        searchB.setAdapter(adapter);
                    }
                });
                parser.parseProducts();
            }

            @Override
            public void onServerRevoked() {
                Toast.makeText(CompareActivity.this, "Some connectivity error occured", Toast.LENGTH_SHORT).show();
            }
        });
        AppSettings settings=new AppSettings(getApplicationContext());
        productfetching.connectServer(settings.retriveSettings("serverurl")+"/productlist.php");


        BootstrapButton compare=(BootstrapButton)findViewById(R.id.compare);
        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dlgAlert  = new ProgressDialog(CompareActivity.this);
                dlgAlert.setMessage("Comparing products. Connecting to server...");
                dlgAlert.setCancelable(true);
                dlgAlert.setTitle("Please wait");
                dlgAlert.show();
                ServerConnector server=new ServerConnector(getApplicationContext());
                server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner() {
                    @Override
                    public void onServerResponded(String responce) {
                        dlgAlert.dismiss();
                        JsonParser parser=new JsonParser(getApplicationContext(),responce);
                        parser.setOnJsonParseListner(new JsonParser.OnProductsParserListner() {
                            @Override
                            public void onProductsParsed(List<Product> products) {
                                LinearLayout compareboard=(LinearLayout)findViewById(R.id.compareboard);
                                compareboard.setVisibility(View.VISIBLE);

                                TextView id1=(TextView)findViewById(R.id.id1);
                                TextView id2=(TextView)findViewById(R.id.id2);
                                TextView shop1=(TextView)findViewById(R.id.shop1);
                                TextView shop2=(TextView)findViewById(R.id.shop2);
                                TextView product1=(TextView)findViewById(R.id.product1);
                                TextView product2=(TextView)findViewById(R.id.product2);
                                TextView company1=(TextView)findViewById(R.id.company1);
                                TextView company2=(TextView)findViewById(R.id.company2);
                                TextView price1=(TextView)findViewById(R.id.price1);
                                TextView price2=(TextView)findViewById(R.id.price2);
                                TextView type1=(TextView)findViewById(R.id.type1);
                                TextView type2=(TextView)findViewById(R.id.type2);

                                id1.setText(String.valueOf(products.get(0).getId()));
                                shop1.setText(String.valueOf(products.get(0).getShopid()));
                                product1.setText(products.get(0).getProduct());
                                company1.setText(products.get(0).getCompany());
                                type1.setText(products.get(0).getType());

                                price1.setText(String.valueOf(products.get(0).getPrice())+"Rs /-");
                                price2.setText(String.valueOf(products.get(1).getPrice())+"Rs /-");
                                
                                id2.setText(String.valueOf(products.get(1).getId()));
                                shop2.setText(String.valueOf(products.get(1).getShopid()));
                                product2.setText(products.get(1).getProduct());
                                company2.setText(products.get(1).getCompany());
                                type2.setText(products.get(1).getType());


                                ListView listViewA = (ListView) findViewById(R.id.proA);
                                ListView listViewB = (ListView) findViewById(R.id.proB);
                                List<String> Alist=new ArrayList<String>();
                                List<String> Blist=new ArrayList<String>();
                                String[] Aspecs=products.get(0).getSpecs().split(",");
                                String[] Bspecs=products.get(1).getSpecs().split(",");

                                for(int j=0;j<Aspecs.length;j++)
                                {
                                    String[] Aparts=Aspecs[j].split("=");
                                    String property=Aparts[0].toUpperCase();
                                    String value=Aparts[1];
                                    Alist.add(property+" : "+value);
                                }
                                ArrayAdapter adapterA = new ArrayAdapter<String>(getApplicationContext(),R.layout.property_a,R.id.propA, Alist);
                                listViewA.setAdapter(adapterA);

                                for(int j=0;j<Bspecs.length;j++)
                                {
                                    String[] Bparts=Bspecs[j].split("=");
                                    String property=Bparts[0].toUpperCase();
                                    String value=Bparts[1];
                                    Blist.add(property+" : "+value);
                                }
                                ArrayAdapter adapterB = new ArrayAdapter<String>(getApplicationContext(),R.layout.property_b,R.id.propB, Blist);
                                listViewB.setAdapter(adapterB);

                            }
                        });
                        parser.parseProducts();
                    }

                    @Override
                    public void onServerRevoked() {

                    }
                });
                AutoCompleteTextView searchA = (AutoCompleteTextView) findViewById(R.id.searchboxa);
                AutoCompleteTextView searchB = (AutoCompleteTextView) findViewById(R.id.searchboxb);
                String[] id1=searchA.getText().toString().split(" - ");
                String[] id2=searchB.getText().toString().split(" - ");
                AppSettings settings=new AppSettings(getApplicationContext());
                server.connectServer(settings.retriveSettings("serverurl")+"/productcomparison.php?id1="+id1[1]+"&id2="+id2[1]+"");
            }
        });


        }
}
