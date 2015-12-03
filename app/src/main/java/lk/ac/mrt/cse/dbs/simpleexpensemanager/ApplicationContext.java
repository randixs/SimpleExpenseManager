package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by Dulaj on 03-Dec-15.
 */
public class ApplicationContext extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Context", "Executed >>>>>>>>>>>>>>");
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
