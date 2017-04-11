package nasa.nearby;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class AccountActivity extends Activity
{
    AlertDialog.Builder dialog;

    public void init()
    {
        dialog  = new AlertDialog.Builder(AccountActivity.this);
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
        setContentView(R.layout.activity_account);
        init();
    }
}
