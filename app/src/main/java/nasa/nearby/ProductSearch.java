package nasa.nearby;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import nasa.support.SearchItem;
import nasa.support.ServerConnector;

public class ProductSearch extends Activity
{
    boolean mainmenu=true;
    Permit permit;
    AppSettings settings;
    BootstrapButton logout;
    BootstrapLabel accountname;
    GridView grid;
    ProgressDialog progress;
    BootstrapButton search;
    AutoCompleteTextView searchbox;
    int menuindex;
    AlertDialog.Builder dialog;
    ArrayAdapter<String> arrayAdapter;
    AutoCompleteTextView searchfield;

    public void init()
    {
        permit=new Permit(getApplicationContext(),ProductSearch.this);
        settings=new AppSettings(getApplicationContext());
        grid=(GridView)findViewById(R.id.products_gridview);
        progress=new ProgressDialog(this);
        search=(BootstrapButton)findViewById(R.id.products_search);
        searchbox=(AutoCompleteTextView)findViewById(R.id.searchbox);
        menuindex=0;
        dialog = new AlertDialog.Builder(ProductSearch.this);
        arrayAdapter = new ArrayAdapter<String>(ProductSearch.this, android.R.layout.select_dialog_singlechoice);
        logout=(BootstrapButton)findViewById(R.id.products_logout);
        accountname=(BootstrapLabel)findViewById(R.id.products_accountname);
        searchfield=(AutoCompleteTextView) findViewById(R.id.searchbox);
    }

    public void aboutMenu()
    {
        mainmenu=false;
        grid=(GridView)findViewById(R.id.products_gridview);
        List<Menu> menus=new ArrayList<>();
        menus.add(new Menu("Source Code on GitHub",R.drawable.github));
        menus.add(new Menu("Download Billing Software",R.drawable.billingsoftware));
        menus.add(new Menu("MiniProject 2K17",R.drawable.miniproject));
        menus.add(new Menu("Help & Support",R.drawable.helpdesk));
        MenuAdapter adapter=new MenuAdapter(getApplicationContext(),menus);
        grid.setAdapter(adapter);
    }

    public void mainMenu()
    {
        mainmenu=true;
        grid=(GridView)findViewById(R.id.products_gridview);
        List<Menu> menus=new ArrayList<>();
        menus.add(new Menu("Locate NearBy Shops",R.drawable.nearbyshops));
        menus.add(new Menu("Compare Products",R.drawable.compareproducts));
        menus.add(new Menu("My Orders",R.drawable.myorders));
        menus.add(new Menu("New Offers",R.drawable.newoffers));
        menus.add(new Menu("Open Web Service",R.drawable.website));
        menus.add(new Menu("Manage Account",R.drawable.myaccount));
        menus.add(new Menu("App Settings",R.drawable.appsettings));
        menus.add(new Menu("About Project",R.drawable.about));
        MenuAdapter adapter=new MenuAdapter(getApplicationContext(),menus);
        grid.setAdapter(adapter);
    }
    
    public void productFetching()
    {
        ServerConnector productfetching=new ServerConnector(getApplicationContext());
        productfetching.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
        {
            @Override
            public void onServerResponded(String responce)
            {
                JsonParser parser=new JsonParser(getApplicationContext(),responce);
                parser.setOnJsonParseListner(new JsonParser.OnSearchesParserListner()
                {
                    @Override
                    public void onSearchesParsed(List<SearchItem> searches)
                    {
                        List<String> products=new ArrayList<String>();
                        for (int i=0;i<searches.size();i++)
                        {
                            products.add(searches.get(i).getName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.product_item,R.id.product_name, products);
                        searchfield.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);
                        searchfield.setDropDownBackgroundResource(R.color.black);
                        searchfield.setThreshold(0);
                        searchfield.setAdapter(adapter);
                        searchfield.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                            {
                                Intent x=new Intent(ProductSearch.this,ProductActivity.class);
                                x.putExtra("search",searchfield.getText().toString());
                                startActivity(x);
                            }
                        });
                        progress.dismiss();
                    }
                });
                parser.parseSearches();
            }

            @Override
            public void onServerRevoked()
            {
                dialog.setMessage("Unfortunately, There is a problem while fetching products from the server");
                dialog.setTitle("Information");
                dialog.setPositiveButton("OK", null);
                dialog.setCancelable(true);
                dialog.create().show();
            }
        });

        productfetching.connectServer(settings.retriveSettings("serverurl")+"/searchlist.php");
    }

    public void fetchAccountName()
    {
        ServerConnector server=new ServerConnector(getApplicationContext());
        server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
        {
            @Override
            public void onServerResponded(String responce)
            {
                JsonParser parser=new JsonParser(getApplicationContext(),responce);
                parser.setOnJsonParseListner(new JsonParser.OnCustomersParserListner()
                {
                    @Override
                    public void onCustomersParsed(List<Customer> customers)
                    {
                        try
                        {
                            accountname.setText(customers.get(0).getFirstname()+" "+customers.get(0).getLastname());
                        }
                        catch (Exception e)
                        {}
                    }
                });
                parser.parseCustomers();
            }

            @Override
            public void onServerRevoked()
            {
                Toast.makeText(ProductSearch.this, "Server is revoking connection. Please LogIn again", Toast.LENGTH_SHORT).show();
            }
        });
        server.connectServer(settings.retriveSettings("serverurl")+"/customer_details.php?id="+settings.retriveSettings("id"));
    }

    public void logout()
    {
        dialog.setMessage("Are you sure? Click YES to confirm that you will be logged out");
        dialog.setTitle("Warning");
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                settings.saveSettings("staylogin","false");
                Intent intent=new Intent(ProductSearch.this,LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.setNegativeButton("NO", null);
        dialog.setCancelable(true);
        dialog.create().show();
    }

    public void openCompareActivity()
    {
        if (mainmenu)
        {
            Intent x = new Intent(ProductSearch.this, CompareActivity.class);
            startActivity(x);
        }
        else
        {
            Toast.makeText(ProductSearch.this, "Available soon", Toast.LENGTH_SHORT).show();
        }
    }

    public void openOffersActivity()
    {
        if (mainmenu)
        {
            Intent x = new Intent(ProductSearch.this, OffersActivity.class);
            startActivity(x);
        }
        else
        {
            Toast.makeText(ProductSearch.this, "Help will be added soon", Toast.LENGTH_SHORT).show();
        }
    }

    public void openAccountActivity()
    {
        Intent x=new Intent(ProductSearch.this,AccountActivity.class);
        startActivity(x);
    }

    public void openSettingsActivity()
    {
        Intent x=new Intent(ProductSearch.this,SettingsActivity.class);
        startActivity(x);
    }

    public void openWebsite(String url)
    {
        try
        {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(myIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(getApplicationContext(), "No application can handle this request.",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void askPermission()
    {
        permit.requestPermit(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION});
    }

    public void searchProducts()
    {
        if (searchbox.getText().toString().equals(""))
        {
            Toast.makeText(ProductSearch.this, "Please search any products, companys, tags, properties, prices, types etc..", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent x = new Intent(ProductSearch.this, ProductActivity.class);
            x.putExtra("search", searchbox.getText().toString());
            startActivity(x);
        }
    }

    public void menuClick()
    {
        if(menuindex==0)
        {
            if (mainmenu)
            {
                Intent intent=new Intent(ProductSearch.this,MapActivity.class);
                startActivity(intent);
            }
            else
            {
                dialog.setTitle("View SourceCode for");
                arrayAdapter.add("Android app");
                arrayAdapter.add("Desktop software");
                arrayAdapter.add("Server Scripts");
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                dialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (which==0)
                        {
                            openWebsite("https://github.com/sangeethnandakumar/Miniproject2k17_AndroidApp");
                        }
                        if (which==1)
                        {
                            openWebsite("https://github.com/sangeethnandakumar/Miniproject2k17_ServerScript");
                        }
                        if (which==2)
                        {
                            openWebsite("https://github.com/sangeethnandakumar/Miniproject2k17_DesktopSoftware");
                        }
                    }
                });
                dialog.show();
            }
        }
        if(menuindex==1)
        {
            openCompareActivity();
        }
        if(menuindex==2)
        {}
        if(menuindex==3)
        {
            openOffersActivity();
        }
        if(menuindex==4)
        {
            openWebsite("http://amazinginside.esy.es");
        }
        if(menuindex==5)
        {
            openAccountActivity();
        }
        if(menuindex==6)
        {
            openSettingsActivity();
        }
        if(menuindex==7) {
            aboutMenu();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);
        init();
        askPermission();
        mainMenu();
        progress.setTitle("Please wait");
        progress.setMessage("Syncing products...");
        progress.show();
        fetchAccountName();
        productFetching();

        search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                searchProducts();
            }
        });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                menuindex=i;
                menuClick();
            }
        });

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                logout();
            }
        });
    }


    @Override
    public void onBackPressed()
    {
        if (mainmenu)
        {
            super.onBackPressed();
        }
        else
        {
            mainMenu();
        }
    }

}
