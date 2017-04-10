// PACKAGE NAME
package nasa.nearby;

// IMPORT OTHER PACKAGES
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
import nasa.support.ServerConnector;

// MAIN CLASS
public class LogInActivity extends Activity
{
    BootstrapButton login;
    BootstrapButton signup;
    BootstrapProgressBar progress;
    BootstrapEditText username,password;
    CheckBox staylogin;
    TextView opensettings;
    AppSettings settings;
    AlertDialog.Builder dialog;

    public void init()
    {
        login=(BootstrapButton)findViewById(R.id.login_loginbutton);
        signup=(BootstrapButton)findViewById(R.id.login_signupbutton);
        progress=(BootstrapProgressBar)findViewById(R.id.login_progressbar);
        username=(BootstrapEditText)findViewById(R.id.login_username);
        password=(BootstrapEditText)findViewById(R.id.login_password);
        staylogin=(CheckBox)findViewById(R.id.login_staylogin);
        opensettings=(TextView)findViewById(R.id.opensettings);
        settings=new AppSettings(getApplicationContext());
        dialog= new AlertDialog.Builder(LogInActivity.this);
    }

    public void correctURL()
    {
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
    }

    public void checkSettings()
    {
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
    }

    public void stayLoggedin()
    {
        if(staylogin.isChecked())
        {
            settings.saveSettings("staylogin","true");
        }
        else
        {
            settings.saveSettings("staylogin","false");
        }
    }

    public void login()
    {
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
            progress.setProgress(70);
            ServerConnector server=new ServerConnector(getApplicationContext());
            server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
            {
                @Override
                public void onServerResponded(final String responce)
                {
                    if (responce.length()==8)
                    {
                        dialog.setMessage("You are succesfully logged in");
                        dialog.setTitle("Information");
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                settings.saveSettings("username",username.getText().toString());
                                progress.setProgress(100);
                                Toast.makeText(getApplicationContext(), "LogIn succesfull. Account ID : "+responce, Toast.LENGTH_SHORT).show();
                                AppSettings settings=new AppSettings(getApplicationContext());
                                settings.saveSettings("id",responce);
                                Handler handler=new Handler();
                                handler.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Intent intent=new Intent(LogInActivity.this,ProductSearch.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                },1000);
                            }
                        });
                        dialog.setCancelable(false);
                        dialog.create().show();
                    }
                    else
                    {
                        progress.setProgress(100);
                        dialog.setMessage("LogIn attempt failed. Please check your username and password");
                        dialog.setTitle("Information");
                        dialog.setPositiveButton("OK", null);
                        dialog.setCancelable(true);
                        dialog.create().show();
                    }
                }

                @Override
                public void onServerRevoked()
                {
                    Toast.makeText(LogInActivity.this, settings.retriveSettings("serverurl")+"/login.php?username="+username.getText()+"&password="+password.getText(), Toast.LENGTH_SHORT).show();
                    dialog.setMessage("Unable to complete login. There might be a problem with the server or your internet connection");
                    dialog.setTitle("Information");
                    dialog.setPositiveButton("OK", null);
                    dialog.setCancelable(true);
                    dialog.create().show();
                    progress.setProgress(100);
                }
            });

            server.connectServer(settings.retriveSettings("serverurl")+"/login.php?username="+username.getText()+"&password="+password.getText());
        }
    }

    public void openSettings()
    {
        Intent i=new Intent(LogInActivity.this,SettingsActivity.class);
        startActivity(i);
    }

    public void signup()
    {
        Intent intent=new Intent(LogInActivity.this,SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        init();
        correctURL();
        checkSettings();

        opensettings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openSettings();
            }
        });

        staylogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {stayLoggedin();
            }
        });

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                login();
            }
        });

        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
            signup();
            }
        });

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        username.setText(settings.retriveSettings("username"));
        password.setText("");
    }
}
