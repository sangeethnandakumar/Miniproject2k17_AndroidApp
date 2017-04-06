// PACKAGE NAME
package nasa.nearby;

// IMPORT PACKAGE
import android.app.Activity;
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

// MAIN CLASS - ProductActivity
public class ProductActivity extends Activity
{
    // AFTER ACTIVITY CREATED
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // SUPER CONSTRUCTOR
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        // RECIVE INTENT
        Intent i=getIntent();
        // GET product-id FROM INTENT
        String productid=i.getStringExtra("productid");
        // MESSAGE productid
        Toast.makeText(this, productid, Toast.LENGTH_SHORT).show();
        // SETUP FILTER BUTTON
        BootstrapButton filter=(BootstrapButton)findViewById(R.id.filterbutton);
        // WHEN FILTER CLICKED
        filter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // OPEN DIALOG
                final Dialog dialog = new Dialog(ProductActivity.this);
                dialog.setContentView(R.layout.filterdialog);
                dialog.setTitle("Filter Shops");
                // SHOW DIALOG
                dialog.show();
                // INITIALISE COMPONENTS
                final TextView value=(TextView)dialog.findViewById(R.id.seekvalue);
                SeekBar seek=(SeekBar)dialog.findViewById(R.id.seekbar);
                // WHEN SEEKBAR CHANGED
                seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b)
                    {
                        // UPDATE SEEKBAR VALUE
                        value.setText(seekBar.getProgress()+1 +" Shops atmost");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // NO USE
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // NO USE
                    }
                });

                // SETUP CANCEL BUTTON
                BootstrapButton cancel=(BootstrapButton)dialog.findViewById(R.id.cancel);
                // WHEN CANCEL BUTTON CLICKED
                cancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        // CLOSE DIALOG
                        dialog.dismiss();
                    }
                });

                // INITIALISE COMPONENTS
                final RadioButton sortprice=(RadioButton)dialog.findViewById(R.id.sortprice);
                final RadioButton sortdistance=(RadioButton)dialog.findViewById(R.id.sortdistance);
                final RadioButton sortrating=(RadioButton)dialog.findViewById(R.id.sortrating);
                final CheckBox checkoutofstock=(CheckBox)dialog.findViewById(R.id.checkoutofstock);
                final CheckBox checkunrated=(CheckBox)dialog.findViewById(R.id.checkunrated);
                BootstrapButton filter=(BootstrapButton)dialog.findViewById(R.id.filter);
                // WHEN FILTER BUTTON CLICKED INSIDE DIALOG
                filter.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        // ARGS=NULL
                        String args="";
                        // SETIUP PROGRESS
                        ProgressDialog dlgAlert  = new ProgressDialog(ProductActivity.this);
                        dlgAlert.setMessage("Applying filters. Connecting to server...");
                        dlgAlert.setCancelable(true);
                        dlgAlert.setTitle("Please wait");
                        // SHOW PROGRESS
                        dlgAlert.show();
                        // UPDATE ARGUMENTS
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
                        // MESSAGE ARGS
                        Toast.makeText(ProductActivity.this, args, Toast.LENGTH_SHORT).show();
                        // SETUP SERVER CONNECTION
                        ServerConnector loadshops=new ServerConnector(getApplicationContext());
                        // AFTER CONNECTION
                        loadshops.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
                        {
                            // ON SERVER RESPONDED
                            @Override
                            public void onServerResponded(String responce)
                            {
                                // MESSAGE responce
                                Toast.makeText(ProductActivity.this, responce, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onServerRevoked()
                            {
                                // MESSAGE ERROR
                            }
                        });

                        //CONNECT TO URL @ null
                        loadshops.connectServer("");
                    }
                });
            }
        });
    }
}
