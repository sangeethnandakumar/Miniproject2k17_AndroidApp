package nasa.nearby;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import nasa.support.AppSettings;

public class SettingsActivity extends Activity {
    AppSettings settings;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(SettingsActivity.this);
        dlgAlert.setMessage("Do you wan't to save this settings?");
        dlgAlert.setTitle("Information");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView serverurl=(TextView)findViewById(R.id.serverurl);
                CheckBox disableautologin=(CheckBox)findViewById(R.id.disableautologin);
                settings.saveSettings("serverurl",serverurl.getText().toString());
                        if (disableautologin.isChecked())
                        {
                            settings.saveSettings("disableautologin","true");
                        }
                        else
                        {
                            settings.saveSettings("disableautologin","false");
                        }
                Toast.makeText(SettingsActivity.this, "Settings saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        dlgAlert.setNegativeButton("Don't save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dlgAlert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settings=new AppSettings(getApplicationContext());
        CheckBox disableautologin=(CheckBox)findViewById(R.id.disableautologin);
        TextView serverurl=(TextView)findViewById(R.id.serverurl);
        try
        {
            if (settings.retriveSettings("disableautologin").equals("true"))
            {
                disableautologin.setChecked(true);
            }
            else
            {
                disableautologin.setChecked(false);
            }
            serverurl.setText(settings.retriveSettings("serverurl"));
        }
        catch (Exception e)
        {}


    }

}
