package nasa.support;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import nasa.nearby.R;

/**
 * Created by Sangeeth Nandakumar on 14-02-2017.
 * MenuAdapter that drives menu into a gridview
 */

public class SpecAdapter extends BaseAdapter
{
    //Data members
    private Context context;
    private List<Spec> specs=new ArrayList<>();

    //Constructor
    public SpecAdapter(Context context, List<Spec> specs) {
        this.context = context;
        this.specs = specs;
    }

    @Override
    public int getCount() {
        return specs.size();
    }

    @Override
    public Object getItem(int i) {
        return specs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        //Inflate layout
        View specview=View.inflate(context, R.layout.compare_item,null);
        //Create handlers
        TextView property=(TextView)specview.findViewById(R.id.property);
        Button value=(Button) specview.findViewById(R.id.value);
        //Assign values
        property.setText(" "+specs.get(i).getProperty());
        value.setText(" ⚫"+specs.get(i).getValue());
        //Create colors
        if (specs.get(i).getColor()==0)
        {
            value.setBackgroundColor(context.getResources().getColor(R.color.Apallet1));
        }
        else if (specs.get(i).getColor()==1)
        {
            value.setBackgroundColor(context.getResources().getColor(R.color.Apallet2));
        }
        else
        {
            value.setBackgroundColor(context.getResources().getColor(R.color.Apallet3));
        }
        //Return
        return specview;
    }

}
