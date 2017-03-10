package test1.nh.com.demos1.utils.fileOperation;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 16-1-17.
 */
public class FileOperation {


    /**
     * delete all files under a directory or delete a file
     * @param file
     */
    public static void deleteFiles(File file){
        if (file.isFile()){file.delete();}
        else {
            File[] files=file.listFiles();
            for (File file1:files){
                FileOperation.deleteFiles(file1);
            }
        }
    }

    /**
     * print all files in the dir
     * --> print not a folder if it is not a folder
     * --> print is empty is there is no files in the folder
     * @param dir
     */
    public static void printFiles(File dir){
        if (dir.isFile()) {
            Log.i("AAA", "this is not a folder");
            return;
        }
        else {
            File[] files = dir.listFiles();
            if (files.length > 0) {
                int filesNum=0;
                for (File file1 : files) {
                    filesNum=filesNum+1;
                    Log.i("AAA", "" + file1.getAbsolutePath() + "---"+filesNum+ "th---" + file1.getName());
                }
            } else {
                Log.i("AAA", "the current folder is empty");
            }
        }
    }
    /**
     * print in the dir all files that starts with the startString
     * @param dir
     */
    public static void printFiles(File dir,String startString){
        if (dir.isFile()) {
            Log.i("AAA", "this is not a folder");
            return;
        }
        else {
            Pattern myPattern=Pattern.compile(startString);
            File[] files = dir.listFiles();
            if (files.length > 0) {
                int filesNum=0;
                for (File file1 : files) {
                    Matcher matcher = myPattern.matcher(file1.getName());
                    if (matcher.lookingAt()) {
                        filesNum = filesNum + 1;
                        Log.i("AAA", "" + file1.getAbsolutePath() + "---" + filesNum + "th---" + file1.getName());
                    }
                }
            } else {
                Log.i("AAA", "the current folder is empty");
            }
        }
    }

    /**
     * return files in a folder and with names start with the startString
     * @param dir        : folder directory
     * @param startString: the starting string
     * @return
     */
    public static List<File> getFiles(File dir,String startString){
        if (dir.isFile()) {
            return null;
        }
        else {
            List<File> fileList=new ArrayList<>();
            Pattern myPattern=Pattern.compile(startString);
            File[] files = dir.listFiles();
            if (files.length > 0) {
                int filesNum=0;
                for (File file1 : files) {
                    Matcher matcher = myPattern.matcher(file1.getName());
                    if (matcher.lookingAt()) {
                        filesNum = filesNum + 1;
                        fileList.add(file1);
                    }
                }
                return fileList;
            } else {
                return null;
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
