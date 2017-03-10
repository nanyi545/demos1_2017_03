package com.webcon.untils.fileOperation;

import android.content.Context;

import com.webcon.untils.BreathConstants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 16-2-17.
 */
public class RecordsLocator {


    private Calendar startTime;
    private Context context;
    private File recordFile;  // file to store the breath signal record
    private boolean isRecord;  // true:actual record file,  false: test file

    private File dayFolder;  // directory to hold all records in a day, name pattern: yyyy_mm_dd
    private File totalFolder; // directory to hold all day folders, at context.getFilesDir(), folder name specified by BreathConstants.RECORD_FILE_FOLDER
    public File getTotalFolder(){ totalFolder=context.getFilesDir();return totalFolder;}

    public RecordsLocator(Calendar startTime,Context context,boolean isr){
        this.startTime=startTime;
        this.context=context;
        this.isRecord=isr;
        createFile();
    }

    public RecordsLocator(Calendar startTime,Context context){
        this.startTime=startTime;
        this.context=context;
        this.isRecord=true;
        createFile();
    }


    public String getRecordFolderName(){
        SimpleDateFormat timeFormat=new SimpleDateFormat("yyyy_MM_dd");
        return (timeFormat.format(startTime.getTime()));
    }

    public String getRecordName(){
        SimpleDateFormat timeFormat=new SimpleDateFormat("HH_mm");
        if (isRecord) return (timeFormat.format(startTime.getTime())+ BreathConstants.RECORD_FILE_SUFFIX);
        else return (timeFormat.format(startTime.getTime())+ BreathConstants.RECORD_FILE_test_SUFFIX);
    }

    // create File to write stuff 
    public File createFile(){
        File dir=new File(new File(context.getFilesDir(),BreathConstants.RECORD_FILE_FOLDER),getRecordFolderName());
        recordFile=new File(dir,getRecordName());
        recordFile.getParentFile().mkdirs();  // in case of file not found exception

        dayFolder=recordFile.getParentFile();
        totalFolder=recordFile.getParentFile().getParentFile();
        return recordFile;
    }


    public File getRecFile(){
//        FileOperation.printFiles(recordFile.getParentFile()); // show files in a day
//        FileOperation.printFiles(recordFile.getParentFile().getParentFile()); // show day-folders
//        Log.i("AAA","-getRecFile-"+recordFile.getAbsolutePath()+"   ?"+recordFile.exists());  // file only exists after write to it
        if (null==recordFile) return createFile();
        else return recordFile;
    }


    // check if a file is a record file(if it ends with the proper suffix)
    public boolean isValidRec(){
        String fileName=recordFile.getName();
        String pattern = ".*"+BreathConstants.RECORD_FILE_SUFFIX;
        return Pattern.matches(pattern, fileName);
    }

    /**
     * check if a dayFolder is before the particular day specified by int[] ymd
     * @param ymd  ymd[0]:year, ymd[1] month, ymd[2] day
     * @param dayFolderFile a particular dayFolder
     * @return
     */
    private boolean beforeDate(int[] ymd,File dayFolderFile){
        String folderName=dayFolderFile.getName();
//        Log.i("AAA","before"+folderName);
        int year=Integer.parseInt(folderName.substring(0,4));  //
        int month=Integer.parseInt(folderName.substring(5,7));
        int day=Integer.parseInt(folderName.substring(8,10));
//        Log.i("AAA","before"+year+"-"+month+"-"+day);
        if  (ymd[0]>year) return true;
        else if ((ymd[0]==year)&(ymd[1]>month)) return true;
        else if ((ymd[0]==year)&(ymd[1]==month)&(ymd[2]>day)) return true;
        else return false;
    }

    /**
     * check if a dayFolder is after the particular day specified by int[] ymd
     * @param ymd  ymd[0]:year, ymd[1] month, ymd[2] day
     * @param dayFolderFile  a particular dayFolder
     * @return
     */
    private boolean afterDate(int[] ymd,File dayFolderFile){
        String folderName=dayFolderFile.getName();
//        Log.i("AAA","after---"+folderName);
//        Log.i("AAA","after---"+folderName+  "---"+folderName.substring(5,7));
        int year=Integer.parseInt(folderName.substring(0,4));  //
        int month=Integer.parseInt(folderName.substring(5,7));
        int day=Integer.parseInt(folderName.substring(8,10));
//        Log.i("AAA","after"+year+"-"+month+"-"+day);
        if  (ymd[0]<year) return true;
        else if ((ymd[0]==year)&(ymd[1]<month)) return true;
        else if ((ymd[0]==year)&(ymd[1]==month)&(ymd[2]<day)) return true;
        else return false;
    }


    public List<File> getDayFolders(int[] ymd_start_date,int[] ymd_end_date){
        File[] dayFolders=totalFolder.listFiles();
        List<File> files=new ArrayList();
        int a=dayFolders.length;
        for (int ii=0;ii<a;ii++){
//            Log.i("AAA","after:"+afterDate(ymd_start_date,dayFolders[ii])+"before"+beforeDate(ymd_end_date,dayFolders[ii]));
            if ((afterDate(ymd_start_date,dayFolders[ii]))&(beforeDate(ymd_end_date,dayFolders[ii]))){
                files.add(dayFolders[ii]);
            }
        }
        return files;
    }








}
