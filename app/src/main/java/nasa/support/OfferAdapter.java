package nasa.support;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import nasa.nearby.R;

/**
 * Created by Sangeeth Nandakumar on 14-02-2017.
 * MenuAdapter that drives menu into a gridview
 */

public class OfferAdapter extends BaseAdapter
{
    //Data members
    private Context context;
    private List<Offer> offers=new ArrayList<>();

    //Constructor
    public OfferAdapter(Context context, List<Offer> offers) {
        this.context = context;
        this.offers = offers;
    }

    @Override
    public int getCount() {
        return offers.size();
    }

    @Override
    public Object getItem(int i) {
        return offers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        //Inflate layout
        View offerView=View.inflate(context, R.layout.offercard,null);
        //Create handlers
        TextView id=(TextView)offerView.findViewById(R.id.offerid);
        TextView shop=(TextView)offerView.findViewById(R.id.offeredshop);
        TextView validfrom=(TextView)offerView.findViewById(R.id.offerstart);
        TextView validthrough=(TextView)offerView.findViewById(R.id.offerend);
        TextView offer=(TextView)offerView.findViewById(R.id.offertitle);
        TextView desc=(TextView)offerView.findViewById(R.id.offerdesc);
        //Assign values
        id.setText("Offer Identifier : "+String.valueOf(offers.get(i).getId()).toString());
        shop.setText("Offered Shop : "+String.valueOf(offers.get(i).getShopid()).toString());
        validfrom.setText("Offer Valid From : "+offers.get(i).getStart());
        validthrough.setText("Offer Valid Until : "+offers.get(i).getStop());
        offer.setText("' "+offers.get(i).getOffer()+" '");
        desc.setText(offers.get(i).getDesc());
        //Return
        return offerView;
    }

}
