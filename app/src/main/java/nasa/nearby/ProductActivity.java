package nasa.nearby;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import nasa.support.ServerConnector;

public class ProductActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Intent i=getIntent();
        String productid=i.getStringExtra("productid");
        Toast.makeText(this, productid, Toast.LENGTH_SHORT).show();
        BootstrapButton filter=(BootstrapButton)findViewById(R.id.filterbutton);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final Dialog dialog = new Dialog(ProductActivity.this);
                dialog.setContentView(R.layout.filterdialog);
                dialog.setTitle("Filter Shops");
                dialog.show();
                final TextView value=(TextView)dialog.findViewById(R.id.seekvalue);
                SeekBar seek=(SeekBar)dialog.findViewById(R.id.seekbar);
                seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        value.setText(seekBar.getProgress()+1 +" Shops atmost");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                BootstrapButton cancel=(BootstrapButton)dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                final RadioButton sortprice=(RadioButton)dialog.findViewById(R.id.sortprice);
                final RadioButton sortdistance=(RadioButton)dialog.findViewById(R.id.sortdistance);
                final RadioButton sortrating=(RadioButton)dialog.findViewById(R.id.sortrating);
                final CheckBox checkoutofstock=(CheckBox)dialog.findViewById(R.id.checkoutofstock);
                final CheckBox checkunrated=(CheckBox)dialog.findViewById(R.id.checkunrated);
                BootstrapButton filter=(BootstrapButton)dialog.findViewById(R.id.filter);
                filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        String args="";
                        ProgressDialog dlgAlert  = new ProgressDialog(ProductActivity.this);
                        dlgAlert.setMessage("Applying filters. Connecting to server...");
                        dlgAlert.setCancelable(true);
                        dlgAlert.setTitle("Please wait");
                        dlgAlert.show();
                        if (sortprice.isChecked())
                        {
                            args+="price_";
                        }
                        else
                        {
                            args+="null_";
                        }
                        if (sortdistance.isChecked())
                        {
                            args+="distance_";
                        }
                        else
                        {
                            args+="null_";
                        }
                        if (sortrating.isChecked())
                        {
                            args+="rating_";
                        }
                        else
                        {
                            args+="null_";
                        }
                        if (checkoutofstock.isChecked())
                        {
                            args+="out_";
                        }
                        if (checkunrated.isChecked())
                        {
                            args+="unrated_";
                        }
                        else
                        {
                            args+="null_";
                        }
                        Toast.makeText(ProductActivity.this, args, Toast.LENGTH_SHORT).show();
                        ServerConnector loadshops=new ServerConnector(getApplicationContext());
                        loadshops.setOnServerStatusListner(new ServerConnector.OnServerStatusListner() {
                            @Override
                            public void onServerResponded(String responce) {
                                Toast.makeText(ProductActivity.this, responce, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onServerRevoked() {

                            }
                        });
                        loadshops.connectServer("");
                    }
                });
            }
        });
    }
}
