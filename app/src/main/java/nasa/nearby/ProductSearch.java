package nasa.nearby;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapLabel;

import java.util.ArrayList;
import java.util.List;

import nasa.support.AppSettings;
import nasa.support.Customer;
import nasa.support.JsonParser;
import nasa.support.Menu;
import nasa.support.MenuAdapter;
import nasa.support.Permit;
import nasa.support.Product;
import nasa.support.ServerConnector;

public class ProductSearch extends Activity {

    Permit permit;
    AppSettings settings;
    BootstrapButton logout;
    BootstrapLabel accountname;
    ServerConnector server;
    GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //INITIALISE COMPONENTS
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);
        //ASK TO OBTAIN LOCATION PERMISSION
        permit=new Permit(getApplicationContext(),ProductSearch.this);
        permit.requestPermit(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION});
        //START MENU RENDERING
        settings=new AppSettings(getApplicationContext());
        grid=(GridView)findViewById(R.id.products_gridview);
        List<Menu> menus=new ArrayList<>();
        menus.add(new Menu("NearBy Shops",R.drawable.nearbyshops));
        menus.add(new Menu("Compare Products",R.drawable.compareproducts));
        menus.add(new Menu("My Orders",R.drawable.myorders));
        menus.add(new Menu("New Offers",R.drawable.newoffers));
        menus.add(new Menu("Website",R.drawable.website));
        menus.add(new Menu("My Account",R.drawable.myaccount));
        menus.add(new Menu("App Settings",R.drawable.appsettings));
        menus.add(new Menu("Source Code on GitHub",R.drawable.github));
        menus.add(new Menu("Download Billing software",R.drawable.billingsoftware));
        menus.add(new Menu("MiniProject 2K17",R.drawable.miniproject));
        menus.add(new Menu("Help & Support",R.drawable.helpdesk));
        MenuAdapter adapter=new MenuAdapter(getApplicationContext(),menus);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                //SHOW NEARBY SHOPS
                if(i==0)
                {
                    Intent intent=new Intent(ProductSearch.this,MapActivity.class);
                    startActivity(intent);
                }
                //PRODUCT COMPARISON
                if(i==1)
                {
                    Intent x=new Intent(ProductSearch.this,CompareActivity.class);
                    startActivity(x);
                }
                //SHOW ORDERS
                if(i==2)
                {
                }
                //NEW OFFERS
                if(i==3)
                {
                    Intent x=new Intent(ProductSearch.this,OffersActivity.class);
                    startActivity(x);
                }
                //OPEN WEBSITE
                if(i==4)
                {
                    try
                    {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://amazinginside.esy.es"));
                        startActivity(myIntent);
                    }
                    catch (ActivityNotFoundException e)
                    {
                        Toast.makeText(getApplicationContext(), "No application can handle this request.",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                //ACCOUNT MANAGEMENT
                if(i==5)
                {
                    Intent x=new Intent(ProductSearch.this,AccountActivity.class);
                    startActivity(x);
                }
                //APPLICATION SETTINGS
                if(i==6)
                {
                    Intent x=new Intent(ProductSearch.this,SettingsActivity.class);
                    startActivity(x);
                }
                //GITHUB SOURCECODE
                if(i==7)
                {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(ProductSearch.this);
                    builderSingle.setTitle("View SourceCode for");

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProductSearch.this, android.R.layout.select_dialog_singlechoice);
                    arrayAdapter.add("Android app");
                    arrayAdapter.add("Desktop software");
                    arrayAdapter.add("Server Scripts");

                    builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            //ANDROID APP
                           if (which==0)
                           {
                               try
                               {
                                   Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/sangeethnandakumar/Miniproject2k17_AndroidApp"));
                                   startActivity(myIntent);
                               }
                               catch (ActivityNotFoundException e)
                               {
                                   Toast.makeText(getApplicationContext(), "No application can handle this request.",Toast.LENGTH_SHORT).show();
                                   e.printStackTrace();
                               }
                           }
                            //SERVER SCRIPT
                            if (which==1)
                            {
                                try
                                {
                                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/sangeethnandakumar/Miniproject2k17_ServerScript"));
                                    startActivity(myIntent);
                                }
                                catch (ActivityNotFoundException e)
                                {
                                    Toast.makeText(getApplicationContext(), "No application can handle this request.",Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                            //SERVER SCRIPT
                            if (which==2)
                            {
                                try
                                {
                                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/sangeethnandakumar/Miniproject2k17_DesktopSoftware"));
                                    startActivity(myIntent);
                                }
                                catch (ActivityNotFoundException e)
                                {
                                    Toast.makeText(getApplicationContext(), "No application can handle this request.",Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    builderSingle.show();
                }
                //DOWNLOAD BILLING SOFTWARE
                if(i==8)
                {
                }
                //MINIPROJECT DEATAILS
                if(i==9)
                {
                }
                //HELP & SUPPORT
                if(i==10)
                {
                    Toast.makeText(ProductSearch.this, "This feature is not yet implimented", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //LOGOUT OPTION
        logout=(BootstrapButton)findViewById(R.id.products_logout);
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ProductSearch.this);
                dlgAlert.setMessage("Are you sure? Click YES to confirm that you will be logged out");
                dlgAlert.setTitle("Warning");
                dlgAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        settings.saveSettings("staylogin","false");
                        Intent intent=new Intent(ProductSearch.this,LogInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dlgAlert.setNegativeButton("NO", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        });
        //ACC HOLDER FETCHING
        accountname=(BootstrapLabel)findViewById(R.id.products_accountname);
        server=new ServerConnector(getApplicationContext());
        server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner() {
            @Override
            public void onServerResponded(String responce) {
                JsonParser parser=new JsonParser(getApplicationContext(),responce);
                parser.setOnJsonParseListner(new JsonParser.OnCustomersParserListner() {
                    @Override
                    public void onCustomersParsed(List<Customer> customers) {
                        try
                        {
                            accountname.setText(customers.get(0).getFirstname()+" "+customers.get(0).getLastname());
                        }
                        catch (Exception e)
                        {

                        }
                    }
                });
                parser.parseCustomers();
            }
            @Override
            public void onServerRevoked() {
                Toast.makeText(ProductSearch.this, "Server is revoking connection. Please LogIn again", Toast.LENGTH_SHORT).show();
            }
        });
        server.connectServer(settings.retriveSettings("serverurl")+"/customer_details.php?id="+settings.retriveSettings("id"));

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
                        AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.searchbox);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.product_item,R.id.product_name, productlist);
                        search.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);
                        search.setDropDownBackgroundResource(R.color.black);
                        search.setThreshold(0);
                        search.setAdapter(adapter);
                        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent x=new Intent(ProductSearch.this,ProductActivity.class);
                                startActivity(x);
                            }
                        });
                    }
                });
                parser.parseProducts();
            }

            @Override
            public void onServerRevoked()
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ProductSearch.this);
                dlgAlert.setMessage("Unfortunately, There is a problem while fetching products from the server");
                dlgAlert.setTitle("Information");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        });
        productfetching.connectServer(settings.retriveSettings("serverurl")+"/productlist.php");
    }




}
