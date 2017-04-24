package test1.nh.com.demos1.utils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/4/18.
 */

public class FileSizeUtil {


    private static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));  // base change for log
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


}
