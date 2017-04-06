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

// MAIN CLASS LogInActivity()
public class LogInActivity extends Activity
{
    // INITIALISE CONTROLS
    BootstrapButton login;
    BootstrapButton signup;
    BootstrapProgressBar progress;
    BootstrapEditText username,password;
    CheckBox staylogin;
    // INITIALISE APP SETTINGS
    AppSettings settings;

    // WHEN ACTIVITY CREATED
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // SUPER CONSTRUCTOR
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // ASSIGN CONTROLS
        login=(BootstrapButton)findViewById(R.id.login_loginbutton);
        signup=(BootstrapButton)findViewById(R.id.login_signupbutton);
        progress=(BootstrapProgressBar)findViewById(R.id.login_progressbar);
        username=(BootstrapEditText)findViewById(R.id.login_username);
        password=(BootstrapEditText)findViewById(R.id.login_password);
        staylogin=(CheckBox)findViewById(R.id.login_staylogin);
        settings=new AppSettings(getApplicationContext());

        // TRY
        try
        {
            // IF SETTINGS -> serverurl STARTS WITH "http" THEN
            if (!settings.retriveSettings("serverurl").startsWith("http"))
            {
                // DISPLAY MESSAGE
                Toast.makeText(this, "Server connection URL reseted", Toast.LENGTH_SHORT).show();
                // CHANGE SETTINGS -> serverurl TO DEFAULT URL
                settings.saveSettings("serverurl","http://amazinginside.esy.es");
            }
        }
        // CATCH ERROR
        catch (Exception e)
        {}

        // WHEN OPEN-SETTINGS CLICKED
        TextView opensettings=(TextView)findViewById(R.id.opensettings);
        opensettings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // START SettingsActivity()
                Intent i=new Intent(LogInActivity.this,SettingsActivity.class);
                startActivity(i);
            }
        });

        // WHEN STAY-LOGIN CHANGED
        staylogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                // IF CHECKED
                if(staylogin.isChecked())
                {
                    // SAVE SETTINGS -> staylogin = TRUE
                    settings.saveSettings("staylogin","true");
                }
                else
                {
                    // SAVE SETTINGS -> staylogin = FALSE
                    settings.saveSettings("staylogin","false");
                }
            }
        });

        // TRY
        try
        {
            // IF SETTINGS -> disableautologin = FALSE
            if (settings.retriveSettings("disableautologin").equals("false"))
            {
                // IF SETTINGS -> staylogin = TRUE
                if(settings.retriveSettings("staylogin").equals("true"))
                {
                    // TICK staylogin CHECKBOX
                    staylogin.setChecked(true);
                    //START ProductSearch ACTIVITY
                    Intent intent=new Intent(LogInActivity.this,ProductSearch.class);
                    startActivity(intent);
                    // KILL THIS ACTIVITY
                    finish();
                }
                //ELSE
                else
                {
                    // UNTICK staylogin CHECKBOX
                    staylogin.setChecked(false);
                }
            }
        }
        // CATCH EXCEPTION
        catch (Exception e)
        {}


        // WHEN LogIn BUTTON CLICKED
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // VALIDATION
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
                    // INCREMENT PROGRESSBAR TO 13
                    progress.setVisibility(View.VISIBLE);
                    progress.setProgress(13);
                    // SETUP CONNECTION TO SERVER
                    ServerConnector server=new ServerConnector(getApplicationContext());
                    server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
                    {
                        // WHEN SERVER-RESPONDED
                        @Override
                        public void onServerResponded(final String responce)
                        {
                            // IF RESPONCE-LENGTH = 8 THEN
                            if (responce.length()==8)
                            {
                                // SETUP MESSAGE DIALOG
                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(LogInActivity.this);
                                dlgAlert.setMessage("You are succesfully logged in");
                                dlgAlert.setTitle("Information");
                                dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                {
                                    // WHEN "OK" CLICKED
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        // SAVE username TO SETTINGS -> username
                                        settings.saveSettings("username",username.getText().toString());
                                        // SET PROGRESSBAR = 100
                                        progress.setProgress(100);
                                        // SHOW MESSAGE
                                        Toast.makeText(LogInActivity.this, "LogIn succesfull. Account ID : "+responce, Toast.LENGTH_SHORT).show();
                                        // SAVE SETTINGS -> id = SERVER-RESPONCE
                                        AppSettings settings=new AppSettings(getApplicationContext());
                                        settings.saveSettings("id",responce);
                                        // CREATE A HANDLER TO DELAY 1sec
                                        Handler handler=new Handler();
                                        handler.postDelayed(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                // START ProductSearch ACTIVITY
                                                Intent intent=new Intent(LogInActivity.this,ProductSearch.class);
                                                startActivity(intent);
                                                // KILL THIS ACTIVITY
                                                finish();
                                            }
                                        },1000);
                                    }
                                });
                                // SHOW MESSAGE-DIALOG
                                dlgAlert.setCancelable(false);
                                dlgAlert.create().show();
                            }
                            //ELSE
                            else
                            {
                                // SET PROGRESSBAR = 100
                                progress.setProgress(100);
                                // SETUP MESSAGE DIALOG
                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(LogInActivity.this);
                                dlgAlert.setMessage("LogIn attempt failed. Please check your username and password");
                                dlgAlert.setTitle("Information");
                                dlgAlert.setPositiveButton("OK", null);
                                dlgAlert.setCancelable(true);
                                // SHOW MESSAGE DIALOG
                                dlgAlert.create().show();
                            }
                        }

                        // WHILE SERVER ERROR
                        @Override
                        public void onServerRevoked()
                        {
                            // SETUP MESSAGE DIALOG
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(LogInActivity.this);
                            dlgAlert.setMessage("Unable to complete login. There might be a problem with the server or your internet connection");
                            dlgAlert.setTitle("Information");
                            dlgAlert.setPositiveButton("OK", null);
                            dlgAlert.setCancelable(true);
                            // SHOW MESSAGE DIALOG
                            dlgAlert.create().show();
                            // SET PROGRESSBAR = 100
                            progress.setProgress(100);
                        }
                    });

                    // CONNECT TO SERVER @ https://amazinginside.esy.es/login.php?username=''&password=''
                    server.connectServer(settings.retriveSettings("serverurl")+"/login.php?username="+username.getText()+"&password="+password.getText()+"");
                }
            }
        });

        // WHEN SIGNUP BUTTON CLICKED
        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // START SignUpActivity
                Intent intent=new Intent(LogInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    // WHEN APP RESUMED
    @Override
    protected void onResume()
    {
        // CONSTRUCTOR
        super.onResume();
        // GET SETTINGS -> username TO USERNAME BOX
        AppSettings settings=new AppSettings(getApplicationContext());
        username.setText(settings.retriveSettings("username"));
        // CLEAR PASSWORD BOX
        password.setText("");
    }
}
