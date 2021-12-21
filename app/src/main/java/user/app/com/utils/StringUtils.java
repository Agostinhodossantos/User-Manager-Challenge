package user.app.com.utils;

public class StringUtils {
    public static boolean isEmpty(String value) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static String slice(String strValue, int start, int end) {
        if (!isEmpty(strValue) && strValue.length() >= end - 1) {
            return strValue.substring(start, end)+"...";
        }  else {
            return strValue;
        }
    }


}
