package expense.exp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {
    public static boolean isValidPhone(String phone) {

        String regex = "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$";
        Pattern pattern = Pattern.compile(regex);
        if (phone == null)
            return false;
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static String addChar(String str, char ch, int position) {
        return str.substring(0, position) + ch + str.substring(position);
    }
}
