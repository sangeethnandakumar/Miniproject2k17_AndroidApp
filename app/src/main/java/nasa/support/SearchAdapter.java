package nasa.support;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import nasa.nearby.R;

public class SearchAdapter extends BaseAdapter
{
    //Data members
    private Context context;
    private List<Product> products=new ArrayList<>();

    //Constructor
    public SearchAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        //Inflate layout
        View productview=View.inflate(context, R.layout.product_block,null);
        //Create handlers
        ImageView image=(ImageView)productview.findViewById(R.id.search_image);
        TextView name=(TextView)productview.findViewById(R.id.search_name);
        TextView rating=(TextView)productview.findViewById(R.id.search_rating);
        TextView company=(TextView)productview.findViewById(R.id.search_company);
        TextView price=(TextView)productview.findViewById(R.id.search_price);
        TextView type=(TextView)productview.findViewById(R.id.search_type);
        TextView quantity=(TextView)productview.findViewById(R.id.search_quantity);
        LinearLayout container=(LinearLayout)productview.findViewById(R.id.oneitem);
        ImageView round=(ImageView)productview.findViewById(R.id.round);
        TextView status=(TextView)productview.findViewById(R.id.status);

        String imageurl=products.get(i).getImage();
        if(!imageurl.equals(""))
        {
            Picasso.with(context).load(imageurl).into(image);
        }

        if (products.get(i).getQuantity()==0)
        {
            round.setBackgroundResource(R.drawable.round_red);
            status.setText("STOCK OUT");
        }
        if (products.get(i).getQuantity()>0 && products.get(i).getQuantity()<25)
        {
            round.setBackgroundResource(R.drawable.round_yellow);
            status.setText("LIMITED STOCK");
        }
        if (products.get(i).getQuantity()>=25)
        {
            round.setBackgroundResource(R.drawable.round_green);
            status.setText("AVAILABLE NOW");
        }

        name.setText(products.get(i).getCompany()+" "+products.get(i).getProduct());
        rating.setText("4.3 RATED");
        company.setText("Manufactured by "+products.get(i).getCompany());
        price.setText("₹ "+String.valueOf(products.get(i).getPrice())+"/- (+ ₹"+String.valueOf(products.get(i).getTax())+" Tax extra)");
        type.setText("Categorised under : "+products.get(i).getType());
        quantity.setText("Found "+String.valueOf(products.get(i).getQuantity())+" remaining on stores");
        return productview;
    }

}
