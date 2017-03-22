package nasa.nearby;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import nasa.support.Shop;

public class ShopInfoActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);
        Shop shop = (Shop) getIntent().getSerializableExtra("ShopObject");

        ImageView shopimage=(ImageView) findViewById(R.id.shopinfo_image);
        TextView shopname=(TextView)findViewById(R.id.shopinfo_shopname);
        TextView shopdesc=(TextView)findViewById(R.id.shopinfo_shopdesc);

        TextView shopid=(TextView)findViewById(R.id.shopinfo_id);
        TextView shoptype=(TextView)findViewById(R.id.shopinfo_type);
        TextView shoploc_lat=(TextView)findViewById(R.id.shopinfo_lattitude);
        TextView shoploc_lon=(TextView)findViewById(R.id.shopinfo_longitude);
        TextView shopowner=(TextView)findViewById(R.id.shopinfo_owner);
        TextView shoprating=(TextView)findViewById(R.id.shopinfo_rating);
        TextView debitcard=(TextView)findViewById(R.id.shopinfo_debitcard);
        TextView paytm=(TextView)findViewById(R.id.shopinfo_paytm);
        TextView shoptime_start=(TextView)findViewById(R.id.shopinfo_starttime);
        TextView shoptime_stop=(TextView)findViewById(R.id.shopinfo_stoptype);

        //Load image
        Picasso.with(getApplicationContext()).load(shop.getImageurl()).into(shopimage);
        shopname.setText(shop.getShopname().toUpperCase());
        shopdesc.setText(shop.getShopdesc());

        //Set text
        shopname.setText(shop.getShopname());
        shopdesc.setText(shop.getShopdesc());
        shopid.setText("RegID"+ shop.getId());
        shoptype.setText(shop.getShoptype()+ " type");
        shoploc_lat.setText("At lattitude: "+shop.getLattitude());
        shoploc_lon.setText("At longitude: "+shop.getLongitude());
        shopowner.setText("Owned by:"+shop.getOwner());
        shoprating.setText("Avg "+shop.getRating()+" out of 5");
        debitcard.setText(shop.getDebitcard());
        paytm.setText(shop.getPaytm());
        shoptime_start.setText("Opens at: "+shop.getOpenat());
        shoptime_stop.setText("Closes at: "+shop.getCloseat());



    }
}
