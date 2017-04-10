package nasa.nearby;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class BookingActivity extends Activity {
    double lat=0,lng=0;
    String telephone="0000000000";
    int shopid=00000000;
    List<Spec> specslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        final AppSettings settings=new AppSettings(getApplicationContext());

        Intent i=getIntent();
        final int search=i.getIntExtra("search",0);
        final TextView name=(TextView)findViewById(R.id.item_name);
        final TextView company=(TextView)findViewById(R.id.item_company);
        final TextView price=(TextView)findViewById(R.id.item_price);
        final TextView quantity=(TextView)findViewById(R.id.item_quantity);
        final TextView shopname=(TextView)findViewById(R.id.shopname);
        final TextView shoptype=(TextView)findViewById(R.id.shoptype);
        final TextView paytm=(TextView)findViewById(R.id.paytm);
        final TextView visa=(TextView)findViewById(R.id.visa);
        final Button changeshop=(Button)findViewById(R.id.changeshop);
        final Button gotoshop=(Button)findViewById(R.id.gotoshop);
        final Button callshop=(Button)findViewById(R.id.callshop);
        final Button shoponmap=(Button)findViewById(R.id.shoponmap);
        final Button specs=(Button)findViewById(R.id.specs);
        final TextView delevery=(TextView) findViewById(R.id.delevery);
        final Button book=(Button) findViewById(R.id.book);

        final ProgressDialog progress=new ProgressDialog(this);
        progress.setTitle("Please wait");
        progress.setMessage("Querying product informations...");
        progress.show();

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        shoponmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x=new Intent(BookingActivity.this,MapActivity.class);
                x.putExtra("lat",lat);
                x.putExtra("lng",lng);
                startActivity(x);
            }
        });

        specs.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Dialog options=new Dialog(BookingActivity.this);
                options.setContentView(R.layout.spec_sheet);
                options.setTitle("Product Specifications");
                SpecAdapter adapter=new SpecAdapter(getApplicationContext(),specslist);
                ListView specsheet=(ListView)options.findViewById(R.id.speclist);
                specsheet.setAdapter(adapter);
                // SHOW DIALOG
                options.show();
            }
        });

        callshop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
            ServerConnector server=new ServerConnector(getApplicationContext());
                server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
                {
                    @Override
                    public void onServerResponded(String responce)
                    {
                        if (responce.length()<5)
                        {
                            // SETUP ALERTBOX
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(BookingActivity.this);
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

                    @Override
                    public void onServerRevoked() {

                    }
                });
                server.connectServer(settings.retriveSettings("serverurl")+"/contactshop.php?id="+shopid);
            }
        });

        changeshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        gotoshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+lat+","+lng+""));
                startActivity(intent);
            }
        });

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
                        if (myproduct.getQuantity()<10)
                        {
                            quantity.setTextColor(getResources().getColor(R.color.red));
                        }
                        if (myproduct.getQuantity()>10 && myproduct.getQuantity()<25)
                        {
                            quantity.setTextColor(getResources().getColor(R.color.yellow));
                        }
                        if (myproduct.getQuantity()>25)
                        {
                            quantity.setTextColor(getResources().getColor(R.color.green));
                        }
                        quantity.setText(String.valueOf(myproduct.getQuantity())+" on stocks");
                        ImageView productimage=(ImageView)findViewById(R.id.productimage);
                        try
                        {
                            Picasso.with(getApplicationContext()).load(myproduct.getImage()).into(productimage);
                        }
                        catch (Exception e){}
                        //Display all specs
                        String[] Aspecs = myproduct.getSpecs().split(",");
                        specslist=new ArrayList<Spec>();
                        specslist.add(new Spec("PRODUCT NAME",myproduct.getProduct(),0));
                        specslist.add(new Spec("MANUFACTURER",myproduct.getCompany(),1));
                        specslist.add(new Spec("MRP (Excl tax)","₹ "+String.valueOf(myproduct.getPrice())+"/-",0));
                        specslist.add(new Spec("TAX OVERHEAD","₹ "+String.valueOf(myproduct.getTax())+"/-",1));
                        specslist.add(new Spec("IN STOCKS",String.valueOf(myproduct.getQuantity())+" Left in stocks",0));
                        specslist.add(new Spec("CATEGEORY",myproduct.getType().toUpperCase(),1));
                        // ADD extra SPECS
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
                                        try
                                        {
                                            Shop myshop = shops.get(0);
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
                                            try
                                            {
                                                Picasso.with(getApplicationContext()).load(myshop.getImageurl()).into(shopimage);
                                            }
                                            catch (Exception e) {}
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
}
