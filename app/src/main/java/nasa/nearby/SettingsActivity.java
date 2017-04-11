package nasa.nearby;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import nasa.support.AppSettings;

public class SettingsActivity extends Activity
{
    AppSettings settings;
    RadioButton satellite;
    RadioButton terrain;
    RadioButton normal;
    RadioButton hybrid;
    CheckBox buildings;
    AlertDialog.Builder dialog;
    TextView serverurl;
    CheckBox disableautologin;
    EditText lattitude;
    EditText longitude;


    public void init()
    {
        settings=new AppSettings(getApplicationContext());
        satellite=(RadioButton)findViewById(R.id.satellite);
        terrain=(RadioButton)findViewById(R.id.terrain);
        normal=(RadioButton)findViewById(R.id.normal);
        hybrid=(RadioButton)findViewById(R.id.hybrid);
        buildings=(CheckBox) findViewById(R.id.buildings);
        dialog  = new AlertDialog.Builder(SettingsActivity.this);
        serverurl=(TextView)findViewById(R.id.serverurl);
        disableautologin=(CheckBox)findViewById(R.id.disableautologin);
        lattitude=(EditText)findViewById(R.id.lattitude);
        longitude=(EditText)findViewById(R.id.longitude);
    }

    public void loadSettings()
    {
        try
        {
            //Location
            lattitude.setText(settings.retriveSettings("lattitude"));
            longitude.setText(settings.retriveSettings("longitude"));
            //Autologin
            if (settings.retriveSettings("disableautologin").equals("true"))
            {
                disableautologin.setChecked(true);
            }
            else
            {
                disableautologin.setChecked(false);
            }
            //Url
            serverurl.setText(settings.retriveSettings("serverurl"));
            //Map
            if (settings.retriveSettings("maptype").equals("satellite"))
            {
                satellite.setChecked(true);
            }
            else if (settings.retriveSettings("maptype").equals("terrain"))
            {
                terrain.setChecked(true);
            }
            else if (settings.retriveSettings("maptype").equals("normal"))
            {
                normal.setChecked(true);
            }
            else if (settings.retriveSettings("maptype").equals("hybrid"))
            {
                hybrid.setChecked(true);
            }
            else
            {
                normal.setChecked(true);
            }
            //Buildings
            if (settings.retriveSettings("showmapbuildings").equals("yes"))
            {
                buildings.setChecked(true);
            }
            else
            {
                buildings.setChecked(false);
            }
        }
        catch (Exception e)
        {}
    }

    public void saveSettings()
    {
        //Location
        settings.saveSettings("lattitude",lattitude.getText().toString());
        settings.saveSettings("longitude",longitude.getText().toString());
        //Url
        settings.saveSettings("serverurl",serverurl.getText().toString());
        //Autologin
        if (disableautologin.isChecked())
        {
            settings.saveSettings("disableautologin","true");
        }
        else
        {
            settings.saveSettings("disableautologin","false");
        }
        //Map
        if (satellite.isChecked())
        {
            settings.saveSettings("maptype","satellite");
        }
        else if (terrain.isChecked())
        {
            settings.saveSettings("maptype","terrain");
        }
        else if (normal.isChecked())
        {
            settings.saveSettings("maptype","normal");
        }
        else if (hybrid.isChecked())
        {
            settings.saveSettings("maptype","hybrid");
        }
        //Buildings
        if (buildings.isChecked())
        {
            settings.saveSettings("showmapbuildings","yes");
        }
        else
        {
            settings.saveSettings("showmapbuildings","no");
        }
    }

    @Override
    public void onBackPressed()
    {
        dialog.setMessage("Do you wan't to save this settings?");
        dialog.setTitle("Information");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                saveSettings();
                Toast.makeText(SettingsActivity.this, "Settings saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        dialog.setNegativeButton("Don't save", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                finish();
            }
        });
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
        loadSettings();
    }

}
