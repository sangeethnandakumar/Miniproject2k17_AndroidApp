// App package
package nasa.nearby;

// Import Statements
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import nasa.support.AppSettings;
import nasa.support.JsonParser;
import nasa.support.ServerConnector;
import nasa.support.Shop;

// Map Activity
public class MapActivity extends Activity implements OnMapReadyCallback
{
    // Class variables
    private GoogleMap mMap;
    private JsonParser parser;
    private int i=0;

    // onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // When Google Map is ready
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        // Create a map handler
        mMap = googleMap;
        // Enable Buildings
        mMap.setBuildingsEnabled(false);
        //Enable my location
        mMap.setMyLocationEnabled(true);
        //List all shops on map
        listAllShopsOnMap();
    }

    //Listing mechanic
    public void listAllShopsOnMap()
    {
        //PROGRESS...
        final ProgressDialog progress=new ProgressDialog(this);
        progress.setTitle("Please wait");
        progress.setMessage("Searching for all registred shops. This may take a while depending on your connection speed");
        progress.show();

        try
        {
            ServerConnector server=new ServerConnector(getApplicationContext());
            server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
            {
                @Override
                public void onServerResponded(String responce)
                {
                    parser=new JsonParser(getApplicationContext(),responce);
                    parser.setOnJsonParseListner(new JsonParser.OnShopsParserListner()
                    {
                        @Override
                        public void onShopsParsed(final List<Shop> shops)
                        {
                            //MARK ALL SHOPS
                            for(i=0;i<shops.size();i++)
                            {
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(shops.get(i).getLattitude(),shops.get(i).getLongitude()))
                                        .title("    "+shops.get(i).getShopname().toUpperCase()+"    ")
                                        .snippet(String.valueOf(i))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                            }
                            //DISMISS PROGRESS VIEW
                            progress.dismiss();
                            //ON PIN CLICK
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                            {
                                @Override
                                public boolean onMarkerClick(final Marker marker)
                                {
                                    Handler handler=new Handler();
                                    handler.postDelayed(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            final Dialog options=new Dialog(MapActivity.this);
                                            options.setContentView(R.layout.pointeroptions);
                                            options.setTitle("Available Functions");
                                            options.show();
                                            BootstrapButton shopinfo=(BootstrapButton)options.findViewById(R.id.shopinfo);
                                            BootstrapButton planroute=(BootstrapButton)options.findViewById(R.id.planroute);
                                            BootstrapButton contact=(BootstrapButton)options.findViewById(R.id.contactshop);
                                            BootstrapButton rateandreview=(BootstrapButton)options.findViewById(R.id.rateandreview);
                                           contact.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   final ProgressDialog progress=new ProgressDialog(MapActivity.this);
                                                   progress.setTitle("Please wait");
                                                   progress.setMessage("Contacting server...");
                                                   progress.show();
                                                   ServerConnector gettel=new ServerConnector(getApplicationContext());
                                                   gettel.setOnServerStatusListner(new ServerConnector.OnServerStatusListner() {
                                                       @Override
                                                       public void onServerResponded(String responce)
                                                       {
                                                           progress.dismiss();
                                                           if (responce.length()<5)
                                                           {
                                                               AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MapActivity.this);
                                                               dlgAlert.setMessage("There is no contact information registred with this shop");
                                                               dlgAlert.setTitle("Information");
                                                               dlgAlert.setPositiveButton("OK",null);
                                                               dlgAlert.setCancelable(true);
                                                               dlgAlert.create().show();
                                                           }
                                                           else
                                                           {
                                                               Intent intent = new Intent(Intent.ACTION_DIAL);
                                                               intent.setData(Uri.parse("tel:"+responce+""));
                                                               startActivity(intent);
                                                           }
                                                       }

                                                       @Override
                                                       public void onServerRevoked()
                                                       {
                                                           progress.dismiss();
                                                       }
                                                   });
                                                   AppSettings settings=new AppSettings(getApplicationContext());
                                                   gettel.connectServer(settings.retriveSettings("serverurl")+"/contactshop.php?id="+shops.get(Integer.parseInt(marker.getSnippet())).getId()+"");
                                               }
                                           });

                                            planroute.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+shops.get(Integer.parseInt(marker.getSnippet())).getLattitude()+","+shops.get(Integer.parseInt(marker.getSnippet())).getLongitude()+""));
                                                    startActivity(intent);
                                                }
                                            });

                                            shopinfo.setOnClickListener(new View.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(View view)
                                                {
                                                    options.dismiss();
                                                    Intent intent=new Intent(MapActivity.this,ShopInfoActivity.class);
                                                    intent.putExtra("ShopObject",shops.get(Integer.parseInt(marker.getSnippet())));
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    },1000);

                                    return false;
                                }
                            });
                        }
                    });
                    //PARSE ALL SHOPS
                    parser.parseShops();
                }

                @Override
                public void onServerRevoked()
                {

                }
            });
            AppSettings settings=new AppSettings(getApplicationContext());
            server.connectServer(settings.retriveSettings("serverurl")+"/shoplist.php");
        }
        catch (Exception e)
        {
            progress.dismiss();
        }

    }

}
