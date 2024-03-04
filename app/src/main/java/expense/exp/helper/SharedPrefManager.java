package expense.exp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import expense.exp.internet.model.User;
import expense.exp.model_class.StudentDataModal;

import com.google.gson.Gson;

/**
 * Created by admin on 10-07-2018.
 */

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String SHARED_PREF_NAME = "EXPENSEMANAGER";
    private static final String std_info = "Std_info";
    private static final String User_info = "User_info";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";


    public SharedPrefManager(Context context) {
        mCtx = context;
        pref = mCtx.getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void setString(String year, String value) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(year, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(year, value);
        editor.commit();
        editor.apply();

    }

    public void setImageUrl(String value) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("img_url", value);
        editor.commit();
        editor.apply();
    }

    public void setUserReferalLink(String referalLink){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("referal_url", referalLink);
        editor.commit();
        editor.apply();
    }

    public String getReferalLink() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("referal_url", "");
    }

    public String getImageUrl() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("img_url", "");
    }


    public void serUser_info(User data_user) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(data_user);
        editor.putString(User_info, json);
        editor.commit();
        editor.apply();


    }

    public void setStudent_info(StudentDataModal data_user) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(data_user);
        editor.putString(std_info, json);
        editor.commit();
        editor.apply();


    }

    public String getString(String year, String value) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(year, Context.MODE_PRIVATE);
        String result = sharedPreferences.getString(year, value);
        return result;

    }

    public void setacc_id(int i) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("acc_id", i);
        editor.commit();
        editor.apply();


    }

    public void removeacc_id() {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("acc_id");
        editor.commit();
    }

    public int getacc_id() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        int i = sharedPreferences.getInt("acc_id", -1);

        return i;

    }


    public User getuserinfo() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(User_info, "");
        User obj = gson.fromJson(json, User.class);
        if (obj != null) {
            return obj;

        } else {
            return null;
        }

    }

    public StudentDataModal getStudentinfo() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(std_info, "");
        StudentDataModal obj = gson.fromJson(json, StudentDataModal.class);
        if (obj != null) {
            return obj;
        } else {
            return null;
        }

    }

    public void setLogiWithF(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFBLogin() {

        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, false);
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }


}
