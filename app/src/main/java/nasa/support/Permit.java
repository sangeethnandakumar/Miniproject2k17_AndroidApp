package nasa.support;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Sangeeth Nandakumar on 25-02-2017.
 */

public class Permit {
    //Private variables
    private Context context;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=0;
    private Activity activity;

    //Constructor
    public Permit(Context context,Activity activity) {
        this.context = context;
        this.activity=activity;
    }

    //Check permission status
    public boolean checkPermit(String permission)
    {
        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
        if(permissionCheck== PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //Request permission
    public void requestPermit(String[] permission)
    {
        ActivityCompat.requestPermissions(activity, permission, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
    }
}
