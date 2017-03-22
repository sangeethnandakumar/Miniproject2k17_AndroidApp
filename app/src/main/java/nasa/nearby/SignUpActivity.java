package nasa.nearby;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapProgressBar;

import nasa.support.AppSettings;
import nasa.support.ServerConnector;

import static android.R.attr.visible;

public class SignUpActivity extends Activity {
    ServerConnector server;
    BootstrapProgressBar progress;
    BootstrapButton signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signup=(BootstrapButton)findViewById(R.id.signup_signupbutton);
        progress=(BootstrapProgressBar)findViewById(R.id.signup_progressbar);
        server=new ServerConnector(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BootstrapEditText fname=(BootstrapEditText)findViewById(R.id.signup_fname);
                BootstrapEditText lname=(BootstrapEditText)findViewById(R.id.signup_lname);
                final BootstrapEditText username=(BootstrapEditText)findViewById(R.id.signup_username);
                final BootstrapEditText password=(BootstrapEditText)findViewById(R.id.signup_password);

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
                    progress.setProgress(33);
                    signup.setEnabled(false);
                    String device= Build.MANUFACTURER;
                    server.setOnServerStatusListner(new ServerConnector.OnServerStatusListner() {
                        @Override
                        public void onServerResponded(String responce) {
                            progress.setProgress(100);
                            AppSettings settings=new AppSettings(getApplicationContext());
                            settings.saveSettings("staylogin","false");
                            Toast.makeText(SignUpActivity.this, "Account created. Account ID : "+responce, Toast.LENGTH_SHORT).show();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            },1000);
                        }
                        @Override
                        public void onServerRevoked() {
                            Toast.makeText(SignUpActivity.this, "Unable to create this account", Toast.LENGTH_SHORT).show();
                            progress.setProgress(100);
                        }
                    });
                    server.connectServer("http://amazinginside.esy.es/signup.php?fname="+fname.getText()+"&lname="+lname.getText()+"&username="+username.getText()+"&password="+password.getText()+"&device="+device);
                }
            }
        });

    }

}
