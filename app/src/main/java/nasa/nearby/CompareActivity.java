package nasa.nearby;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.view.ProgressView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import nasa.support.AppSettings;
import nasa.support.JsonParser;
import nasa.support.Product;
import nasa.support.ServerConnector;
import nasa.support.Spec;
import nasa.support.SpecAdapter;

public class CompareActivity extends Activity
{
    List<Spec> Aspecslist;
    List<Spec> Bspecslist;
    ListView Alist,Blist;
    SpecAdapter Aadapter;
    SpecAdapter Badapter;
    ProgressDialog progress;
    AppSettings settings;

    public void init()
    {
        progress  = new ProgressDialog(CompareActivity.this);
        Aspecslist=new ArrayList<>();
        Bspecslist=new ArrayList<>();
        Aadapter=new SpecAdapter(getApplicationContext(),Aspecslist);
        Badapter=new SpecAdapter(getApplicationContext(),Bspecslist);
        settings=new AppSettings(getApplicationContext());
    }

    public void downloadProductList()
    {
        ServerConnector productfetching=new ServerConnector(getApplicationContext());
        productfetching.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
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
                        progress.dismiss();
                    }
                });
                parser.parseProducts();
            }

            @Override
            public void onServerRevoked()
            {
                Toast.makeText(CompareActivity.this, "Some connectivity error occured", Toast.LENGTH_SHORT).show();
            }
        });
        progress.setMessage("Fetching global stocks...");
        progress.setCancelable(false);
        progress.setTitle("Please wait");
        progress.show();
        productfetching.connectServer(settings.retriveSettings("serverurl")+"/productlist.php");
    }

    public void compareNow()
    {
        Aspecslist.clear();
        Bspecslist.clear();
        progress.setMessage("Comparing products. Connecting to server...");
        progress.setCancelable(false);
        progress.setTitle("Please wait");
        progress.show();
        AutoCompleteTextView boxa=(AutoCompleteTextView)findViewById(R.id.searchboxa);
        AutoCompleteTextView boxb=(AutoCompleteTextView)findViewById(R.id.searchboxb);
        ServerConnector server = new ServerConnector(getApplicationContext());
        if (boxa.getText().toString().equals(boxb.getText().toString()))
        {
            Toast.makeText(CompareActivity.this, "You can't compare same products", Toast.LENGTH_SHORT).show();
            progress.dismiss();
            Toast.makeText(CompareActivity.this, "Choose 2 different products", Toast.LENGTH_SHORT).show();
        }
        else
        {
            server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
            {
                @Override
                public void onServerResponded(String responce)
                {
                    progress.dismiss();
                    JsonParser parser = new JsonParser(getApplicationContext(), responce);
                    parser.setOnJsonParseListner(new JsonParser.OnProductsParserListner()
                    {
                        @Override
                        public void onProductsParsed(List<Product> products)
                        {
                            String[] Aspecs = products.get(0).getSpecs().split(",");
                            String[] Bspecs = products.get(1).getSpecs().split(",");
                            if (!products.get(0).getImage().equals(""))
                            {
                                ImageView imageA=(ImageView)findViewById(R.id.imageA);
                                Picasso.with(getApplicationContext()).load(products.get(0).getImage()).into(imageA);
                            }
                            if (!products.get(1).getImage().equals(""))
                            {
                                ImageView imageB=(ImageView)findViewById(R.id.imageB);
                                Picasso.with(getApplicationContext()).load(products.get(1).getImage()).into(imageB);
                            }
                            Aspecslist.add(new Spec("PRODUCT NAME",products.get(0).getProduct(),1));
                            Aspecslist.add(new Spec("MANUFACTURER",products.get(0).getCompany(),0));
                            Aspecslist.add(new Spec("MRP (Excl tax)","₹ "+String.valueOf(products.get(0).getPrice())+"/-",1));
                            Aspecslist.add(new Spec("TAX OVERHEAD","₹ "+String.valueOf(products.get(0).getTax())+"/-",0));
                            Aspecslist.add(new Spec("IN STOCKS",String.valueOf(products.get(0).getQuantity())+" Left in stocks",1));
                            Aspecslist.add(new Spec("CATEGEORY",products.get(0).getType().toUpperCase(),0));
                            Bspecslist.add(new Spec("PRODUCT NAME",products.get(1).getProduct(),1));
                            Bspecslist.add(new Spec("MANUFACTURER",products.get(1).getCompany(),0));
                            Bspecslist.add(new Spec("MRP (Excl tax)","₹ "+String.valueOf(products.get(1).getPrice())+"/-",1));
                            Bspecslist.add(new Spec("TAX OVERHEAD","₹ "+String.valueOf(products.get(1).getTax())+"/-",0));
                            Bspecslist.add(new Spec("IN STOCKS",String.valueOf(products.get(1).getQuantity())+" Left in stocks",1));
                            Bspecslist.add(new Spec("CATEGEORY",products.get(1).getType().toUpperCase(),0));
                            for (int i=0;i<Aspecs.length;i++)
                            {
                                String[] Aspec=Aspecs[i].split("=");
                                Aspecslist.add(new Spec(Aspec[0],Aspec[1],3));
                            }
                            for (int i=0;i<Bspecs.length;i++)
                            {
                                String[] Bspec=Bspecs[i].split("=");
                                Bspecslist.add(new Spec(Bspec[0],Bspec[1],3));
                            }
                            Alist=(ListView)findViewById(R.id.proA);
                            Blist=(ListView)findViewById(R.id.proB);
                            Alist.setAdapter(Aadapter);
                            Blist.setAdapter(Badapter);
                        }
                    });
                    parser.parseProducts();
                }

                @Override
                public void onServerRevoked()
                {
                    Toast.makeText(CompareActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
            AutoCompleteTextView searchA = (AutoCompleteTextView) findViewById(R.id.searchboxa);
            AutoCompleteTextView searchB = (AutoCompleteTextView) findViewById(R.id.searchboxb);
            String[] id1 = searchA.getText().toString().split(" - ");
            String[] id2 = searchB.getText().toString().split(" - ");
            AppSettings settings = new AppSettings(getApplicationContext());
            server.connectServer(settings.retriveSettings("serverurl") + "/productcomparison.php?id1=" + id1[1] + "&id2=" + id2[1] + "");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        init();
        downloadProductList();


        BootstrapButton compare=(BootstrapButton)findViewById(R.id.compare);
        compare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                catch (Exception e)
                {
                }
                compareNow();
            }
        });
    }
}
