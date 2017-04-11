//PACKAGE
package nasa.nearby;

//IMPORT PACKAGES
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import nasa.support.AppSettings;
import nasa.support.JsonParser;
import nasa.support.Product;
import nasa.support.ServerConnector;
import nasa.support.Shop;
import nasa.support.Spec;
import nasa.support.SpecAdapter;

public class BookingActivity extends Activity
{
    double lat=0,lng=0;
    String telephone="0000000000";
    int shopid=00000000;
    List<Spec> specslist;
    AppSettings settings;
    TextView name;
    TextView company;
    TextView price;
    TextView quantity;
    TextView shopname;
    TextView shoptype;
    TextView paytm;
    TextView visa;
    Button changeshop;
    Button gotoshop;
    Button callshop;
    Button shoponmap;
    Button specs;
    TextView delevery;
    Button book;
    ProgressDialog progress;
    Dialog dialog;
    AlertDialog.Builder alert;
    ImageView round;
    TextView status;
    int search;
    TextView distance;

    public void init()
    {
        settings=new AppSettings(getApplicationContext());
        name=(TextView)findViewById(R.id.item_name);
        company=(TextView)findViewById(R.id.item_company);
        price=(TextView)findViewById(R.id.item_price);
        quantity=(TextView)findViewById(R.id.item_quantity);
        shopname=(TextView)findViewById(R.id.shopname);
        shoptype=(TextView)findViewById(R.id.shoptype);
        paytm=(TextView)findViewById(R.id.paytm);
        visa=(TextView)findViewById(R.id.visa);
        changeshop=(Button)findViewById(R.id.changeshop);
        gotoshop=(Button)findViewById(R.id.gotoshop);
        callshop=(Button)findViewById(R.id.callshop);
        shoponmap=(Button)findViewById(R.id.shoponmap);
        specs=(Button)findViewById(R.id.specs);
        delevery=(TextView) findViewById(R.id.delevery);
        book=(Button) findViewById(R.id.book);
        progress=new ProgressDialog(this);
        dialog=new Dialog(BookingActivity.this);
        alert  = new AlertDialog.Builder(BookingActivity.this);
        round=(ImageView) findViewById(R.id.round);
        status=(TextView) findViewById(R.id.status);
        distance=(TextView) findViewById(R.id.distance);
    }

    public void showOnMap()
    {
        Intent x=new Intent(BookingActivity.this,MapActivity.class);
        x.putExtra("lat",lat);
        x.putExtra("lng",lng);
        startActivity(x);
    }

    public void openSpecs()
    {
        dialog.setContentView(R.layout.spec_sheet);
        dialog.setTitle("Product Specifications");
        SpecAdapter adapter=new SpecAdapter(getApplicationContext(),specslist);
        ListView specsheet=(ListView)dialog.findViewById(R.id.speclist);
        specsheet.setAdapter(adapter);
        dialog.show();
    }

    public void callToShop()
    {
        ServerConnector server=new ServerConnector(getApplicationContext());
        server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
        {
            @Override
            public void onServerResponded(String responce)
            {
                if (responce.length()<5)
                {
                    alert.setMessage("There is no contact information registred with this shop");
                    alert.setTitle("Information");
                    alert.setPositiveButton("OK",null);
                    alert.setCancelable(true);
                    alert.create().show();
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
                Toast.makeText(BookingActivity.this, "Unstable server responce or internet", Toast.LENGTH_SHORT).show();
            }
        });
        server.connectServer(settings.retriveSettings("serverurl")+"/contactshop.php?id="+shopid);
    }

    public void gotoShop()
    {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+lat+","+lng+""));
        startActivity(intent);
    }

    public void fetchDetails()
    {
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
                        final Product myproduct=products.get(search);
                        name.setText(myproduct.getCompany()+" "+myproduct.getProduct());
                        company.setText("Offered By "+myproduct.getCompany());
                        price.setText("₹ "+String.valueOf(myproduct.getPrice())+"/-");
                        if (myproduct.getQuantity()==0)
                        {
                            quantity.setTextColor(getResources().getColor(R.color.red));
                            round.setBackgroundResource(R.drawable.round_red);
                            status.setText("PRODUCT OUT OF STOCK");
                        }
                        if (myproduct.getQuantity()>0 && myproduct.getQuantity()<25)
                        {
                            quantity.setTextColor(getResources().getColor(R.color.yellow));
                            round.setBackgroundResource(R.drawable.round_yellow);
                            status.setText("LIMITED PRODUCTS ON STOCK");
                        }
                        if (myproduct.getQuantity()>25)
                        {
                            quantity.setTextColor(getResources().getColor(R.color.green));
                            round.setBackgroundResource(R.drawable.round_green);
                            status.setText("PRODUCT UNDER STOCK");
                        }
                        quantity.setText(String.valueOf(myproduct.getQuantity())+" on stocks");
                        ImageView productimage=(ImageView)findViewById(R.id.productimage);
                        Picasso.with(getApplicationContext()).load(myproduct.getImage()).into(productimage);
                        String[] Aspecs = myproduct.getSpecs().split(",");
                        specslist=new ArrayList<Spec>();
                        specslist.add(new Spec("PRODUCT NAME",myproduct.getProduct(),0));
                        specslist.add(new Spec("MANUFACTURER",myproduct.getCompany(),1));
                        specslist.add(new Spec("MRP (Excl tax)","₹ "+String.valueOf(myproduct.getPrice())+"/-",0));
                        specslist.add(new Spec("TAX OVERHEAD","₹ "+String.valueOf(myproduct.getTax())+"/-",1));
                        specslist.add(new Spec("IN STOCKS",String.valueOf(myproduct.getQuantity())+" Left in stocks",0));
                        specslist.add(new Spec("CATEGEORY",myproduct.getType().toUpperCase(),1));
                        for (int i=0;i<Aspecs.length;i++)
                        {
                            String[] Aspec=Aspecs[i].split("=");
                            specslist.add(new Spec(Aspec[0],Aspec[1],3));
                        }
                        ServerConnector shopdetect=new ServerConnector(getApplicationContext());
                        shopdetect.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
                        {
                            @Override
                            public void onServerResponded(String responce)
                            {
                                JsonParser parser=new JsonParser(getApplicationContext(),responce);
                                parser.setOnJsonParseListner(new JsonParser.OnShopsParserListner()
                                {
                                    @Override
                                    public void onShopsParsed(List<Shop> shops)
                                    {
                                        progress.dismiss();
                                        Shop myshop = shops.get(0);
                                        try
                                        {
                                            Location locationA = new Location("Customer");
                                            double Alat=Double.parseDouble(settings.retriveSettings("lattitude"));
                                            double Alng=Double.parseDouble(settings.retriveSettings("longitude"));
                                            locationA.setLatitude(Alat);
                                            locationA.setLongitude(Alng);

                                            Location locationB = new Location("Shop");
                                            double Blat=myshop.getLattitude();
                                            double Blng=myshop.getLattitude();
                                            locationB.setLatitude(Blat);
                                            locationB.setLongitude(Blng);

                                            float kms = Math.round(locationA.distanceTo(locationB)/1000);

                                            distance.setText(String.valueOf(kms)+" Kms");
                                        }
                                        catch (Exception e)
                                        {

                                        }
                                        try
                                        {
                                            delevery.setText(myshop.getDelevery().toUpperCase());
                                            if (delevery.getText().toString().toUpperCase().startsWith("NOT"))
                                            {
                                                book.setBackgroundColor(getResources().getColor(R.color.gray));
                                                Toast.makeText(BookingActivity.this, "You can't order this product because this shop is not offering home delevery", Toast.LENGTH_SHORT).show();
                                            }
                                            quantity.append(" at ⚫"+myshop.getShopname());
                                            shopname.setText(myshop.getShopname());
                                            shoptype.setText(myshop.getShoptype()+" Shop");
                                            paytm.setText(myshop.getPaytm());
                                            visa.setText(myshop.getDebitcard());
                                            lat=myshop.getLattitude();
                                            lng=myshop.getLongitude();
                                            telephone=myshop.getPhone();
                                            shopid=myshop.getId();
                                            ImageView shopimage = (ImageView) findViewById(R.id.shopimage);
                                            Picasso.with(getApplicationContext()).load(myshop.getImageurl()).into(shopimage);
                                        }
                                        catch (Exception e)
                                        {
                                            Toast.makeText(BookingActivity.this, "Its an error.!, The shop registered with this product might be removed/modified/deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                parser.parseShops();
                            }

                            @Override
                            public void onServerRevoked()
                            {
                                Toast.makeText(BookingActivity.this, "Unable to fetch shop info", Toast.LENGTH_SHORT).show();
                            }
                        });
                        shopdetect.connectServer(settings.retriveSettings("serverurl")+"/getshopfromid.php?id="+myproduct.getShopid());
                    }
                });
               parser.parseProducts();
            }

            @Override
            public void onServerRevoked()
            {
                Toast.makeText(BookingActivity.this, "Unable to fetch product info", Toast.LENGTH_SHORT).show();
            }
        });
        server.connectServer(settings.retriveSettings("serverurl")+"/searchresult.php?like="+settings.retriveSettings("search"));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        init();
        Intent i=getIntent();
        search=i.getIntExtra("search",0);
        progress.setTitle("Please wait");
        progress.setMessage("Querying product informations...");
        progress.show();
        fetchDetails();

        book.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Book button clicked
            }
        });

        shoponmap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showOnMap();
            }
        });

        specs.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openSpecs();
            }
        });

        callshop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                callToShop();
            }
        });

        changeshop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        gotoshop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                gotoShop();
            }
        });
    }

}
