package expense.exp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;



import expense.exp.R;
import expense.exp.databinding.ActivityCropBinding;
import expense.exp.helper.Pref;
import expense.exp.helper.SharedPrefManager;
import expense.exp.helper.Utils;

import java.util.Calendar;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPrefManager = new SharedPrefManager(this);
        Utils.this_year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String year = Pref.getStringValue(this, Utils.selected_year, "");
        if (year.equals("")) {
            Pref.setStringValue(this, Utils.selected_year, Utils.this_year);
        }
        Log.e("onCreate: ", Utils.this_year);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                if (sharedPrefManager.isFBLogin()) {
                    Intent i = new Intent(SplashActivity.this, Home_Activity.class);

                    startActivity(i);

                } else {
                    Intent i = new Intent(SplashActivity.this, Login_activity.class);
                    startActivity(i);
                }


                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
