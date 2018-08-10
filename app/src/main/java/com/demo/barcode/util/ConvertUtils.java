package com.demo.barcode.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.Toast;

import com.demo.barcode.R;
import com.demo.barcode.app.CoreApplication;
import com.demo.barcode.constants.Constants;
import com.demo.barcode.manager.ServerManager;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

/**
 * Created by PC on 13-Apr-2018.
 */

public class ConvertUtils {

    public static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public static final String APP_DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";


    public static String ConvertStringToShortDate(String s) {
        Date date = null;
        String sDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(APP_DATETIME_FORMAT);
        String expectedPattern = "dd/MM/yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
        try {
            date = dateFormat.parse(s);
            sDate = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sDate;

    }

    public static String getDateTimeCurrent() {
        SimpleDateFormat formatter = new SimpleDateFormat(APP_DATETIME_FORMAT);
        String newFormat = formatter.format(new Date());
        return newFormat;
    }

    public static long getTimeMillis(){
        return System.currentTimeMillis();
    }

    public static String exportRealmFile() {
        final Realm realm = Realm.getDefaultInstance();
        String filePath = "";
        try {

             File fileMain = new File(Environment.getExternalStorageDirectory().getPath().concat(
                    CoreApplication.getInstance().getString(R.string.text_path_file)));
             if (!fileMain.exists()){
                 fileMain.mkdirs();
             }
            String nameDatabase = ServerManager.getInstance().getServer().equals(Constants.SERVER_MAIN) ? CoreApplication.getInstance()
                    .getString(R.string.text_name_database_main) : CoreApplication.getInstance()
                    .getString(R.string.text_name_database_test);
            final File file = new File(Environment.getExternalStorageDirectory().getPath().concat(
                    CoreApplication.getInstance().getString(R.string.text_path_file) + nameDatabase));
            if (file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }

            realm.writeCopyTo(file);
            filePath = file.getPath();
        } catch (Exception e) {
            realm.close();
            e.printStackTrace();
        }
        return filePath;
    }

        /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
        public static boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
              //  Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    return true;
                }
            }
            return false;
        }

}
