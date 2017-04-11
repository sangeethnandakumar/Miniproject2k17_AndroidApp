package nasa.nearby;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import nasa.support.AppSettings;
import nasa.support.ServerConnector;

public class SignUpActivity extends Activity
{
    BootstrapProgressBar progress;
    BootstrapButton signup;
    BootstrapEditText fname;
    BootstrapEditText lname;
    BootstrapEditText username;
    BootstrapEditText password;
    AlertDialog.Builder dialog;
    AppSettings settings;
    String device;

    public void init()
    {
        signup=(BootstrapButton)findViewById(R.id.signup_signupbutton);
        progress=(BootstrapProgressBar)findViewById(R.id.signup_progressbar);
        fname=(BootstrapEditText)findViewById(R.id.signup_fname);
        lname=(BootstrapEditText)findViewById(R.id.signup_lname);
        username=(BootstrapEditText)findViewById(R.id.signup_username);
        password=(BootstrapEditText)findViewById(R.id.signup_password);
        settings=new AppSettings(getApplicationContext());
        dialog= new AlertDialog.Builder(SignUpActivity.this);
    }

    public void signup()
    {
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
        else
        {
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(67);
            signup.setEnabled(false);
            device= Build.MANUFACTURER;
            ServerConnector server=new ServerConnector(getApplicationContext());
            server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner()
            {
                @Override
                public void onServerResponded(final String responce)
                {
                    dialog.setMessage("Your new account has been registred succesfully, Now you can login with new creditinals");
                    dialog.setTitle("Information");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            progress.setProgress(100);
                            settings.saveSettings("staylogin","false");
                            Toast.makeText(SignUpActivity.this, "Account created. Account ID : "+responce, Toast.LENGTH_SHORT).show();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    finish();
                                }
                            },1000);
                        }
                    });
                    dialog.show();
                }

                @Override
                public void onServerRevoked()
                {
                    Toast.makeText(SignUpActivity.this, "Unable to create this account", Toast.LENGTH_SHORT).show();
                    progress.setProgress(100);
                }
            });

            server.connectServer(settings.retriveSettings("serverurl")+"/signup.php?fname="+fname.getText()+"&lname="+lname.getText()+"&username="+username.getText()+"&password="+password.getText()+"&device="+device);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                catch (Exception e)
                {
                }
                signup();
            }
        });
    }

}
