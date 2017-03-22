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

public class MenuAdapter extends BaseAdapter
{
    //Data members
    private Context context;
    private List<Menu> menus=new ArrayList<>();

    //Constructor
    public MenuAdapter(Context context, List<Menu> menus) {
        this.context = context;
        this.menus = menus;
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int i) {
        return menus.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        //Inflate layout
        View menuview=View.inflate(context, R.layout.menu_item,null);
        //Create handlers
        ImageView image=(ImageView)menuview.findViewById(R.id.menuitem_image);
        TextView text=(TextView)menuview.findViewById(R.id.menuitem_text);
        //Assign values
        text.setText(menus.get(i).getMenuitem());
        image.setImageResource(menus.get(i).getImage());
        //Return
        return menuview;
    }

}
