// PACKAGE NAME
package nasa.nearby;

// IMPORT PACKAGES
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;
import nasa.support.AppSettings;
import nasa.support.JsonParser;
import nasa.support.ServerConnector;
import nasa.support.Shop;

// MAIN CLASS - MapActivity WITH INTERFACE - OnMapReadyCallback
public class MapActivity extends Activity implements OnMapReadyCallback
{
    // INITIALISE VARIABLES
    private GoogleMap mMap;
    private JsonParser parser;
    private int i=0;

    // ACTIVITY CREATED
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // SUPER CONSTRUCTOR
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // FIND Google Maps
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        // GET MAP IN async THREAD TO AVOID HANGING
        mapFragment.getMapAsync(this);
    }

    // WHEN GoogleMap IS READY
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        // ASSIGN GMap
        mMap = googleMap;
        // ENABLE BUILDINGS
        mMap.setBuildingsEnabled(false);
        // ENABLE CURRENT LOCATION
        mMap.setMyLocationEnabled(true);
        // CALL FUNCTION - listAllShopsOnMap()
        listAllShopsOnMap();
    }

    // FUNCTION - listAllShopsOnMap
    public void listAllShopsOnMap()
    {
        // CREATE A PROGRESS DIALOG
        final ProgressDialog progress=new ProgressDialog(this);
        progress.setTitle("Please wait");
        progress.setMessage("Searching for all registred shops. This may take a while depending on your connection speed");
        // SHOW DIALOG
        progress.show();

        try
        {
            // SETUP SERVER CONNECTION
            ServerConnector server=new ServerConnector(getApplicationContext());
            // AFTER CONNECTION
            server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
            {
                // WHEN SERVER RESPONDED
                @Override
                public void onServerResponded(String responce)
                {
                    // CREATE NEW JSON PARSER, PASS responce
                    parser=new JsonParser(getApplicationContext(),responce);
                    // AFTER PARSING
                    parser.setOnJsonParseListner(new JsonParser.OnShopsParserListner()
                    {
                        // IF SUCCESS
                        @Override
                        public void onShopsParsed(final List<Shop> shops)
                        {
                            // FOR i=0 ----> SHOPS_SIZE
                            for(i=0;i<shops.size();i++)
                            {
                                // ADD EACH MARKER ON MAP
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(shops.get(i).getLattitude(),shops.get(i).getLongitude()))
                                        .title("    "+shops.get(i).getShopname().toUpperCase()+"    ")
                                        .snippet(String.valueOf(i))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                            }

                            // CLOSE PROGRESS VIEW
                            progress.dismiss();

                            // WHEN MARKER CLICKED
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                            {
                                @Override
                                public boolean onMarkerClick(final Marker marker)
                                {
                                    // USE HANDLER TO DELAY 1sec
                                    Handler handler=new Handler();
                                    handler.postDelayed(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            // SETUP OPTIONS DIALOG
                                            final Dialog options=new Dialog(MapActivity.this);
                                            options.setContentView(R.layout.pointeroptions);
                                            options.setTitle("Available Functions");
                                            // SHOW DIALOG
                                            options.show();
                                            // INITIALISE COMPONENTS
                                            BootstrapButton shopinfo=(BootstrapButton)options.findViewById(R.id.shopinfo);
                                            BootstrapButton planroute=(BootstrapButton)options.findViewById(R.id.planroute);
                                            BootstrapButton contact=(BootstrapButton)options.findViewById(R.id.contactshop);
                                            BootstrapButton rateandreview=(BootstrapButton)options.findViewById(R.id.rateandreview);
                                           // WHEN CONTACT-BUTTON CLICKED
                                            contact.setOnClickListener(new View.OnClickListener()
                                           {
                                               @Override
                                               public void onClick(View view)
                                               {
                                                   // SETUP PROGRESS-DIALOG
                                                   final ProgressDialog progress=new ProgressDialog(MapActivity.this);
                                                   progress.setTitle("Please wait");
                                                   progress.setMessage("Contacting server...");
                                                   // SHOW PROGRESS-DIALOG
                                                   progress.show();
                                                   // SETUP SERVER CONNECTION
                                                   ServerConnector gettel=new ServerConnector(getApplicationContext());
                                                   // AFTER CONNECTED
                                                   gettel.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
                                                   {
                                                       // IF SERVER RESPONDED
                                                       @Override
                                                       public void onServerResponded(String responce)
                                                       {
                                                           // CLOSE PROGRESS-DIALOG
                                                           progress.dismiss();
                                                           // IF SERVER-RESPONCE LENGTH > 5
                                                           if (responce.length()<5)
                                                           {
                                                               // SETUP ALERTBOX
                                                               AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MapActivity.this);
                                                               dlgAlert.setMessage("There is no contact information registred with this shop");
                                                               dlgAlert.setTitle("Information");
                                                               dlgAlert.setPositiveButton("OK",null);
                                                               dlgAlert.setCancelable(true);
                                                               //SHOW ALERTBOX
                                                               dlgAlert.create().show();
                                                           }
                                                           // ELSE
                                                           else
                                                           {
                                                               // OPEN DIALLER WITH PHONENO: TYPED
                                                               Intent intent = new Intent(Intent.ACTION_DIAL);
                                                               intent.setData(Uri.parse("tel:"+responce+""));
                                                               startActivity(intent);
                                                           }
                                                       }

                                                       // WHEN SERVER ERROR
                                                       @Override
                                                       public void onServerRevoked()
                                                       {
                                                           // CLOSE PROGRESS
                                                           progress.dismiss();
                                                       }
                                                   });

                                                   // SETUP APP SETTINGS
                                                   AppSettings settings=new AppSettings(getApplicationContext());
                                                   //CONNCT TO SERVER @ https://amazinginside.esy.es/contactshop.php?id=
                                                   gettel.connectServer(settings.retriveSettings("serverurl")+"/contactshop.php?id="+shops.get(Integer.parseInt(marker.getSnippet())).getId()+"");
                                               }
                                           });

                                            // IF plan-route BUTTON CLICKED
                                            planroute.setOnClickListener(new View.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(View view)
                                                {
                                                    // OPEN GoogleMaps WITH URL
                                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+shops.get(Integer.parseInt(marker.getSnippet())).getLattitude()+","+shops.get(Integer.parseInt(marker.getSnippet())).getLongitude()+""));
                                                    startActivity(intent);
                                                }
                                            });

                                            // IF shop-info CLICKED
                                            shopinfo.setOnClickListener(new View.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(View view)
                                                {
                                                    // CLOSE OPTIONS
                                                    options.dismiss();
                                                    // OPEN ShopInfoActivity
                                                    Intent intent=new Intent(MapActivity.this,ShopInfoActivity.class);
                                                    // WITH Shop Index AS ARGS
                                                    intent.putExtra("ShopObject",shops.get(Integer.parseInt(marker.getSnippet())));
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    },1000);

                                    // RETURN FALSE
                                    return false;
                                }
                            });
                        }
                    });

                    // PARSE ALL SHOPS
                    parser.parseShops();
                }

                @Override
                public void onServerRevoked()
                {
                    // DISPLAY ERROR
                }
            });

            // SETUP APP SETTINGS
            AppSettings settings=new AppSettings(getApplicationContext());
            // CONNECT TO SERVER @ https://amazinginside.esy.es/shoplist.php
            server.connectServer(settings.retriveSettings("serverurl")+"/shoplist.php");
        }
        // IF ERROR
        catch (Exception e)
        {
            // CLOSE PROGRESS-DIALOG
            progress.dismiss();
        }

    }

}
