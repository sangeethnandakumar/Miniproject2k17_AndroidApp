package nasa.nearby;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapProgressBar;

import nasa.support.AppSettings;
import nasa.support.Permit;
import nasa.support.ServerConnector;

public class LogInActivity extends Activity
{
    BootstrapButton login;
    BootstrapButton signup;
    BootstrapProgressBar progress;
    BootstrapEditText username,password;
    CheckBox staylogin;
    AppSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        login=(BootstrapButton)findViewById(R.id.login_loginbutton);
        signup=(BootstrapButton)findViewById(R.id.login_signupbutton);
        progress=(BootstrapProgressBar)findViewById(R.id.login_progressbar);
        username=(BootstrapEditText)findViewById(R.id.login_username);
        password=(BootstrapEditText)findViewById(R.id.login_password);
        staylogin=(CheckBox)findViewById(R.id.login_staylogin);
        settings=new AppSettings(getApplicationContext());

        try
        {
            if (!settings.retriveSettings("serverurl").startsWith("http"))
            {
                Toast.makeText(this, "Server connection URL reseted", Toast.LENGTH_SHORT).show();
                settings.saveSettings("serverurl","http://amazinginside.esy.es");
            }
        }
        catch (Exception e)
        {}

        TextView opensettings=(TextView)findViewById(R.id.opensettings);
        opensettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LogInActivity.this,SettingsActivity.class);
                startActivity(i);
            }
        });

        staylogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(staylogin.isChecked())
                {
                    settings.saveSettings("staylogin","true");
                }
                else
                {
                    settings.saveSettings("staylogin","false");
                }
            }
        });

        try
        {
            if (settings.retriveSettings("disableautologin").equals("false"))
            {
                if(settings.retriveSettings("staylogin").equals("true"))
                {
                    staylogin.setChecked(true);
                    Intent intent=new Intent(LogInActivity.this,ProductSearch.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    staylogin.setChecked(false);
                }
            }
        }
        catch (Exception e)
        {}



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().length()<1)
                {
                    username.setError("Invalid username");
                }
                else if(password.getText().length()<1)
                {
                    password.setError("Invalid password");
                }
                else
                {
                    progress.setVisibility(View.VISIBLE);
                    progress.setProgress(13);
                    ServerConnector server=new ServerConnector(getApplicationContext());
                    server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner() {
                        @Override
                        public void onServerResponded(final String responce) {
                            if (responce.length()==8)
                            {
                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(LogInActivity.this);
                                dlgAlert.setMessage("You are succesfully logged in");
                                dlgAlert.setTitle("Information");
                                dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        settings.saveSettings("username",username.getText().toString());
                                        progress.setProgress(100);
                                        Toast.makeText(LogInActivity.this, "LogIn succesfull. Account ID : "+responce, Toast.LENGTH_SHORT).show();
                                        AppSettings settings=new AppSettings(getApplicationContext());
                                        settings.saveSettings("id",responce);
                                        Handler handler=new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent=new Intent(LogInActivity.this,ProductSearch.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        },1000);
                                    }
                                });
                                dlgAlert.setCancelable(false);
                                dlgAlert.create().show();
                            }
                            else
                            {
                                progress.setProgress(100);
                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(LogInActivity.this);
                                dlgAlert.setMessage("LogIn attempt failed. Please check your username and password");
                                dlgAlert.setTitle("Information");
                                dlgAlert.setPositiveButton("OK", null);
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();
                            }
                        }

                        @Override
                        public void onServerRevoked() {
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(LogInActivity.this);
                            dlgAlert.setMessage("Unable to complete login. There might be a problem with the server or your internet connection");
                            dlgAlert.setTitle("Information");
                            dlgAlert.setPositiveButton("OK", null);
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();
                            progress.setProgress(100);
                        }
                    });
                    server.connectServer(settings.retriveSettings("serverurl")+"/login.php?username="+username.getText()+"&password="+password.getText()+"");
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LogInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppSettings settings=new AppSettings(getApplicationContext());
        username.setText(settings.retriveSettings("username"));
        password.setText("");
    }
}
