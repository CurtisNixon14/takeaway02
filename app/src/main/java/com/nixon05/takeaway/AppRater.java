package com.nixon05.takeaway;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by Curtismacpro on 5/23/17.
 */

public class AppRater {

    private final static String APP_TITLE = "TakeAway";
    private final static String APP_PACKAGE_NAME = "com.nixon05.takeaway";


    private final static int DAYS_UNTIL_PROMPT = 0;
    private final static int LAUNCH_UNTIL_PROMPT = 3;

    public static void app_launched(Context context){
        SharedPreferences prefs = context.getSharedPreferences("rate_app", 0);
        if(prefs.getBoolean("dontshowagain", false)){
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        Long date_firstLaunch = prefs.getLong("date_first_lauch", 0);
        if (date_firstLaunch == 0) {

            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_first_launch", date_firstLaunch);
        }

        if(launch_count >= LAUNCH_UNTIL_PROMPT){

            if(System.currentTimeMillis() >= date_firstLaunch){
                showRateDialog(context, editor);
            }
        }
        editor.commit();
    }

    public static void showRateDialog(final Context context, final SharedPreferences.Editor editor){

        Dialog dialog = new Dialog(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        String message = "If you enjoy using " + APP_TITLE + ", please take a moment to rate the app." + " Thank you fo your support!";

        builder.setMessage(message)
                .setTitle("Rate " + APP_TITLE).setIcon(context.getApplicationInfo().icon).setCancelable(false).setPositiveButton("Rate Now", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){
                editor.putBoolean("dontshowagain", true);
                editor.commit();


                //if the app isnt on the marketplace than this dialog will show

                try{
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PACKAGE_NAME)));
                }catch(ActivityNotFoundException e){
                    Toast.makeText(context, "You have pressed Rate Now button", Toast.LENGTH_LONG).show();
                }

                dialog.dismiss();
            }
        })
        .setNeutralButton("Later", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "You have pressed Later button", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        })
        .setNegativeButton("No, Thanks", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog , int which){
                if(editor != null){
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }

                Toast.makeText(context, "You have pressed the No, Thanks Button", Toast.LENGTH_LONG).show();

                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
    }
}
