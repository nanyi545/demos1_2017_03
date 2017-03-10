package com.webcon.untils.fileOperation;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by Administrator on 16-1-17.
 */
public class FileOperation {

    /**
     * print all files in the dir
     * --> print not a folder if it is not a folder
     * --> print is empty is there is no files in the folder
     * @param dir
     */
    public static void printFiles(File dir,String TAG){
        if (dir.isFile()) {
            Log.i(TAG, "this is not a folder");
            return;
        }
        else {
            File[] files = dir.listFiles();
            if (files.length > 0) {
                int filesNum=0;
                for (File file1 : files) {
                    filesNum=filesNum+1;
                    Log.i(TAG, "" + file1.getAbsolutePath() + "---"+filesNum+ "th---" + file1.getName());
                }
            } else {
                Log.i(TAG, "the current folder is empty");
            }
        }
    }


    // print a File array
    public static void printFiles(File[] dirs){
        int filesNum=0;
        for (File file1 : dirs) {
            filesNum=filesNum+1;
            Log.i("AAA", "" + file1.getAbsolutePath() + "---"+filesNum+ "th---" + file1.getName());
        }
    }



    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }






}
