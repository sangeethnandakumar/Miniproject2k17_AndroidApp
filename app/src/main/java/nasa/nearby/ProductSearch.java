// PACKAGE NAME
package nasa.nearby;

// IMPORT PACKAGES
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

// MAIN CLASS - ProductSearch
public class ProductSearch extends Activity
{
    // PERMISSION OBJECT
    Permit permit;
    // SETUP APP SETTINGS
    AppSettings settings;
    // INITIALISE COMPONENTS
    BootstrapButton logout;
    BootstrapLabel accountname;
    ServerConnector server;
    GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // SUPER CONSTRUCTOR
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);
        // INITIALISE PERMIT CLASS
        permit=new Permit(getApplicationContext(),ProductSearch.this);
        // DEFINE PERMISSIONS
        permit.requestPermit(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION});
        // ASSIGN SETTINGS OBJECT
        settings=new AppSettings(getApplicationContext());
        grid=(GridView)findViewById(R.id.products_gridview);
        // ADD MENU ITEMS TO A LIST
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
        // CREATE AN ADAPTER FOR GRID
        MenuAdapter adapter=new MenuAdapter(getApplicationContext(),menus);
        // APPLY ADAPTER
        grid.setAdapter(adapter);
        // WHEN MENU CLICKED
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                // OPEN MapActivity WHEN i=0
                if(i==0)
                {
                    Intent intent=new Intent(ProductSearch.this,MapActivity.class);
                    startActivity(intent);
                }
                // OPEN CompareActivity WHEN i=1
                if(i==1)
                {
                    Intent x=new Intent(ProductSearch.this,CompareActivity.class);
                    startActivity(x);
                }
                // --
                if(i==2)
                {
                }
                // OPEN OffersActivity WHEN i=3
                if(i==3)
                {
                    Intent x=new Intent(ProductSearch.this,OffersActivity.class);
                    startActivity(x);
                }
                // OPEN WEBSITE WHEN i=4
                if(i==4)
                {
                    // OPEN LINK OPENING APPS
                    try
                    {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://amazinginside.esy.es"));
                        startActivity(myIntent);
                    }
                    catch (ActivityNotFoundException e)
                    {
                        // MESSAGE IF NO APPS FOUND
                        Toast.makeText(getApplicationContext(), "No application can handle this request.",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                // OPEN AccountActivity WHEN i=5
                if(i==5)
                {
                    Intent x=new Intent(ProductSearch.this,AccountActivity.class);
                    startActivity(x);
                }

                // OPEN SettingsActivity WHEN i=6
                if(i==6)
                {
                    Intent x=new Intent(ProductSearch.this,SettingsActivity.class);
                    startActivity(x);
                }

                // OPEN GitHub WEBSITE WHEN i=7
                if(i==7)
                {
                    // SHOW SELECTION BOX
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(ProductSearch.this);
                    builderSingle.setTitle("View SourceCode for");
                    // CREATE ARRAY ADAPTER
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProductSearch.this, android.R.layout.select_dialog_singlechoice);
                    arrayAdapter.add("Android app");
                    arrayAdapter.add("Desktop software");
                    arrayAdapter.add("Server Scripts");
                    // IF CANCEL CLICKED
                    builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // CLOSE DIALOG
                            dialog.dismiss();
                        }
                    });

                    // APPLY ADAPTER
                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener()
                    {
                        // WHEN AN OPTION CLICKED
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // OPEN GitHUB/Android WHEN item=0
                           if (which==0)
                           {
                               try
                               {
                                   // OPEN WEBSITE
                                   Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/sangeethnandakumar/Miniproject2k17_AndroidApp"));
                                   startActivity(myIntent);
                               }
                               catch (ActivityNotFoundException e)
                               {
                                   // MESSAGE IF NO APPS CAN OPEN WEBSITE
                                   Toast.makeText(getApplicationContext(), "No application can handle this request.",Toast.LENGTH_SHORT).show();
                                   e.printStackTrace();
                               }
                           }

                            // OPEN GitHUB/Server WHEN item=1
                            if (which==1)
                            {
                                try
                                {
                                    // OPEN WEBSITE
                                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/sangeethnandakumar/Miniproject2k17_ServerScript"));
                                    startActivity(myIntent);
                                }
                                catch (ActivityNotFoundException e)
                                {
                                    // MESSAGE IF NO APPS CAN OPEN WEBSITE
                                    Toast.makeText(getApplicationContext(), "No application can handle this request.",Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }

                            // OPEN GitHUB/Desktop WHEN item=0=2
                            if (which==2)
                            {
                                try
                                {
                                    // OPEN WEBSITE
                                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/sangeethnandakumar/Miniproject2k17_DesktopSoftware"));
                                    startActivity(myIntent);
                                }
                                catch (ActivityNotFoundException e)
                                {
                                    // MESSAGE IF NO APPS CAN OPEN
                                    Toast.makeText(getApplicationContext(), "No application can handle this request.",Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                    // SHOW DIALOG BOX
                    builderSingle.show();
                }

                // NOT DEFINED
                if(i==8)
                {
                }
                // NOT DEFINED
                if(i==9)
                {
                }
                // NOT DEFINED
                if(i==10)
                {
                    // MESSAGE ERROR
                    Toast.makeText(ProductSearch.this, "This feature is not yet implimented", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // INITIALISE logout BUTTON
        logout=(BootstrapButton)findViewById(R.id.products_logout);
        // WHEN logout CLICKED
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // SETUP MESSAGE BOX
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ProductSearch.this);
                dlgAlert.setMessage("Are you sure? Click YES to confirm that you will be logged out");
                dlgAlert.setTitle("Warning");
                //WHEN YES CLICKED
                dlgAlert.setPositiveButton("YES", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        // SAVE SETTINGS -> staylogin = FALSE
                        settings.saveSettings("staylogin","false");
                        // OPEN LogInActivity
                        Intent intent=new Intent(ProductSearch.this,LogInActivity.class);
                        startActivity(intent);
                        // KILL THIS ACTIVITY
                        finish();
                    }
                });
                dlgAlert.setNegativeButton("NO", null);
                dlgAlert.setCancelable(true);
                // SHOW MESSAGE-DIALOG
                dlgAlert.create().show();
            }
        });

        // GET ACCOUNT HOLDER NAME FROM SERVER
        accountname=(BootstrapLabel)findViewById(R.id.products_accountname);
        // SETUP SERVER CONNECTION
        server=new ServerConnector(getApplicationContext());
        // AFTER CONNECTING
        server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
        {
            // IF SERVER RESPONDED
            @Override
            public void onServerResponded(String responce)
            {
                // SETUP JSON PARSER, PASS responce
                JsonParser parser=new JsonParser(getApplicationContext(),responce);
                // PARSE CUSTOMERS
                parser.setOnJsonParseListner(new JsonParser.OnCustomersParserListner()
                {
                    // SUCCESSFUL PARSING
                    @Override
                    public void onCustomersParsed(List<Customer> customers)
                    {
                        try
                        {
                            // DISPLAY ACCOUNT NAME
                            accountname.setText(customers.get(0).getFirstname()+" "+customers.get(0).getLastname());
                        }
                        catch (Exception e)
                        {
                            // ERROR
                        }
                    }
                });
                // START PARSING
                parser.parseCustomers();
            }

            // WHEN SERVER ERROR
            @Override
            public void onServerRevoked()
            {
                // DISPLAY ERROR
                Toast.makeText(ProductSearch.this, "Server is revoking connection. Please LogIn again", Toast.LENGTH_SHORT).show();
            }
        });
        // CONNECT TO SERVER @ https://amazinginside.esy.es/customer_details.php?id=
        server.connectServer(settings.retriveSettings("serverurl")+"/customer_details.php?id="+settings.retriveSettings("id"));

        // SETUP CONNECTION  TO SERVER
        ServerConnector productfetching=new ServerConnector(getApplicationContext());
        // AFTER CONNECTING
        productfetching.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
        {
            // SERVER RESPONDED
            @Override
            public void onServerResponded(String responce)
            {
                // SETUP JSON PARSER
                JsonParser parser=new JsonParser(getApplicationContext(),responce);
                // PARSE PRODUCTS
                parser.setOnJsonParseListner(new JsonParser.OnProductsParserListner()
                {
                    // IF SUCCESS
                    @Override
                    public void onProductsParsed(List<Product> products)
                    {
                        // CREATE A NEW ARRAY LIST
                        List<String> productlist=new ArrayList<String>();
                        // FOR i=0 ------> PRODUCTS_SIZE
                        for (int i=0;i<products.size();i++)
                        {
                            // ADD TO NEW ARRAY LIST
                            productlist.add(products.get(i).getCompany()+" "+products.get(i).getProduct()+" in ["+products.get(i).getType().toUpperCase()+"] - "+String.valueOf(products.get(i).getId()));
                        }
                        // SETUP AUTO-COMPLETE BOX
                        AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.searchbox);
                        // CREATE NEW ARRAY ADAPTER
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.product_item,R.id.product_name, productlist);
                        search.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);
                        search.setDropDownBackgroundResource(R.color.black);
                        search.setThreshold(0);
                        // APPLY ADAPTER TO AUTOCOMPLETE BOX
                        search.setAdapter(adapter);
                        // WHEN ITEM CLICKED
                        search.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                            {
                                // OPEN ProductActivity
                                Intent x=new Intent(ProductSearch.this,ProductActivity.class);
                                startActivity(x);
                            }
                        });
                    }
                });

                // PARSE PRODUCTS
                parser.parseProducts();
            }

            // ON SERVER ERROR
            @Override
            public void onServerRevoked()
            {
                // SETUP MESSAGE BOX
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ProductSearch.this);
                dlgAlert.setMessage("Unfortunately, There is a problem while fetching products from the server");
                dlgAlert.setTitle("Information");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                // SHOW MESSAGE BOX
                dlgAlert.create().show();
            }
        });

        // CONNECT TO SERVER @ https://amazinginside.esy.es/productlist.php
        productfetching.connectServer(settings.retriveSettings("serverurl")+"/productlist.php");
    }




}
