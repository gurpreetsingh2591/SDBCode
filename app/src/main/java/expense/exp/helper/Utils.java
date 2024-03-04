package expense.exp.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    //Email Validation pattern
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    //Fragments Tags
    public static final String EditAds_Fragment = "EditAds_Fragment";
    public static final String PostAds_Fragment = "PostAds_Fragment";
    public static final String AdsDetails_Fragment = "AdsDetails_Fragment";
    public static final String Login_Fragment = "Login_Fragment";
    public static final String SignUp_Fragment = "SignUp_Fragment";
    public static final String ForgotPassword_Fragment = "ForgotPassword_Fragment";
    public static final String Browser_Fragment = "Browser_Fragment";
    public static final String Search_Fragment = "Search_Fragment";
    public static final String Profile_Fragment = "Profile_Fragment";
    public static final String AddIn_PlayLIst_Fragment = "AddIn_PlayLIst_Fragment";
    public static final String Commet_Fragment = "Commet_Fragment";
    public static final String Create_PlayLIst_Fragment = "Create_PlayLIst_Fragment";
    public static final String Edit_Profile_Fragment = "Edit_Profile_Fragment";
    public static final String FAVORITES_Fragment = "FAVORITES_Fragment";
    public static final String OFFLINE_Fragment = "OFFLINE_Fragment";
    public static final String Play_Song_Fragment = "Play_Song_Fragment";
    public static final String PLAYLISTS_Fragment = "PLAYLISTS_Fragment";
    public static final String Recently_Added_Fragment = "Recently_Added_Fragment";
    public static final String Top_Songs_Fragment = "Top_Songs_Fragment";
    public static final String Trending_Fragment = "Trending_Fragment";
    public static final String Play_Song_Info_Fargment = "Play_Song_Info_Fargment";
    public static final String Serach_My_Account_Fragment = "Serach_My_Account_Fragment";

    public static final String selected_year = "selected_year";
    public static String this_year;


    public static String imgURL = "http://expenses.topnotchhub.com/assets/uploads/docs/";

    public static boolean isAlphaNumeric(String str) {
        // Regex to check string is alphanumeric or not.
        String regex = "^(?=.*[a-zA-Z\\d].*)[a-zA-Z\\d!@#$%&*]{7,}$";
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }
        // Pattern class contains matcher() method
        // to find matching between given string
        // and regular expression.
        Matcher m = p.matcher(str);
        // Return if the string
        // matched the ReGex
        return m.matches();
    }

}
