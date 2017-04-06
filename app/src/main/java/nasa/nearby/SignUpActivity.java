// PACKAGE NAME
package nasa.nearby;

// IMPORT OTHER PACKAGES
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import nasa.support.AppSettings;
import nasa.support.ServerConnector;

// MAIN CLASS - SignUpActivity
public class SignUpActivity extends Activity
{
    // INITIALISE CONTROLS
    ServerConnector server;
    BootstrapProgressBar progress;
    BootstrapButton signup;

    // WHEN ACTIVITY CREATED
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // SUPER CONSTRUCTOR
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // ASSIGN CONTROLS
        signup=(BootstrapButton)findViewById(R.id.signup_signupbutton);
        progress=(BootstrapProgressBar)findViewById(R.id.signup_progressbar);
        server=new ServerConnector(this);

        // WHEN signup BUTTON CLICKED
        signup.setOnClickListener(new View.OnClickListener()
        {
            // ON CLICK
            @Override
            public void onClick(View view)
            {
                // ASSIGN CONTROLS
                BootstrapEditText fname=(BootstrapEditText)findViewById(R.id.signup_fname);
                BootstrapEditText lname=(BootstrapEditText)findViewById(R.id.signup_lname);
                final BootstrapEditText username=(BootstrapEditText)findViewById(R.id.signup_username);
                final BootstrapEditText password=(BootstrapEditText)findViewById(R.id.signup_password);

                // VALIDATION
                if(fname.getText().length()<=1)
                {
                    fname.setError("Firstname should contain atleast 2 letters");
                }
                else if(lname.getText().length()<1)
                {
                    lname.setError("Lastname should contain atleast 1 letters");
                }
                else if(username.getText().length()<=1)
                {
                    username.setError("Username should contain atleast 2 letters");
                }
                else if(password.getText().length()<=1)
                {
                    password.setError("Password should contain atleast 2 letters");
                }
                else if(password.getText().length()>8)
                {
                    password.setError("Password should contain atmost 8 letters");
                }
                // ELSE
                else
                {
                    // SET PROGRESSBAR = 33
                    progress.setVisibility(View.VISIBLE);
                    progress.setProgress(33);
                    // DISABLE signup BUTTON
                    signup.setEnabled(false);
                    // GET DEVICE MAKER NAME
                    String device= Build.MANUFACTURER;
                    // SETUP SERVER CONNECTION
                    server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
                    {
                        // ON SERVER RESPONDED
                        @Override
                        public void onServerResponded(final String responce)
                        {
                            // SETUP MESSAGE DIALOG
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(SignUpActivity.this);
                            dlgAlert.setMessage("Your new account has been registred succesfully, Now you can login with new creditinals");
                            dlgAlert.setTitle("Information");
                            dlgAlert.setCancelable(false);
                            // WHEN "OK" CLICKED
                            dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    // SET PROGRESS = 100
                                    progress.setProgress(100);
                                    // SETUP APP SETTINGS
                                    AppSettings settings=new AppSettings(getApplicationContext());
                                    // SAVE SETTINGS -> staylogin = FALSE
                                    settings.saveSettings("staylogin","false");
                                    // MESSAGE
                                    Toast.makeText(SignUpActivity.this, "Account created. Account ID : "+responce, Toast.LENGTH_SHORT).show();
                                    // HANDLER TO DELAY 1sec
                                    Handler handler=new Handler();
                                    handler.postDelayed(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            // KILL THIS ACTIVITY
                                            finish();
                                        }
                                    },1000);
                                }
                            });
                            // SHOW MESSAGE DIALOG
                            dlgAlert.show();
                        }

                        // WHEN SERVER ERROR
                        @Override
                        public void onServerRevoked()
                        {
                            // MESSAGE
                            Toast.makeText(SignUpActivity.this, "Unable to create this account", Toast.LENGTH_SHORT).show();
                            // SET PROGRESS = 100
                            progress.setProgress(100);
                        }
                    });

                    // SETUP APP SETTINGS
                    AppSettings settings=new AppSettings(getApplicationContext());
                    // CONNECT TO SERVER @ https://amazinginside.esy.es/sighup.php?fname=&lname=&username=&password=&device=
                    server.connectServer(settings.retriveSettings("serverurl")+"/signup.php?fname="+fname.getText()+"&lname="+lname.getText()+"&username="+username.getText()+"&password="+password.getText()+"&device="+device);
                }
            }
        });

    }

}
