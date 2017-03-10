package test1.nh.com.demos1.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.io.FileUtils;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import test1.nh.com.demos1.R;
import test1.nh.com.demos1.utils.fileOperation.ByteOperations;
import test1.nh.com.demos1.utils.fileOperation.FileOperation;
import test1.nh.com.demos1.utils.fileOperation.ioManagement.IOmanager;

public class Email_IO_Activity extends AppCompatActivity {

    public static void start(Context context) {
        final Intent intent = new Intent(context, Email_IO_Activity.class);
        context.startActivity(intent);
    }

    // 保存文件的根目录
    public static final String BASE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + File.separator
            + "demos1" + File.separator;

    // 保存临时视频文件的路径
    public static final String TEMP_VIDEO_PATH = BASE_PATH + "temp"
            + File.separator + "video" + File.separator;

    // 保存临时错误信息文件的路径
    public static final String TEMP_LOG_PATH = BASE_PATH + "temp"
            + File.separator + "Log" + File.separator;


    // 截屏路径-->coolpad
    public static final String SCREEN_SHOTS = "/storage/sdcard1/Coolpad/Screenshots/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);


        File f1=new File(getFilesDir(),"testIO_Email_IO_Activity");
        Log.i("AAA",f1.exists()+"  --before write");

        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(f1));
            dos.writeInt(12);
            Log.i("AAA",f1.exists()+"  --after write");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //--does not work... do not know why...
    public void sendSimpleEmailApache(View view) throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName("smtp.163.com"); // 163 smtp address
        email.setSmtpPort(25);// 163 smtp port
        email.setAuthenticator(new org.apache.commons.mail.DefaultAuthenticator("hcholding_log@163.com", "android_log"));
        email.setSSLOnConnect(true);
        email.setFrom("hcholding_log@163.com");
        email.setSubject("testEmail");
        email.setMsg("testEmail");
        email.addTo("hcholding_log@163.com");
        email.send();
    }


    public void sendSimpleEmail(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Properties props = new Properties();   //设置邮箱服务器
                props.put("mail.smtp.protocol", "smtp");
                props.put("mail.smtp.auth", "true"); // 设置要验证
                props.put("mail.smtp.host", "smtp.163.com"); // 设置host
                props.put("mail.smtp.port", "25"); // 设置端口

                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("hcholding_log@163.com", "androidlog1");  //设置用户名密码     要使用 pop3授权码: androidlog1，而不是登陆密码 。。。
                    }
                }); // 获取验证会话

                try {
                    // 配置发送及接收邮箱
                    InternetAddress fromAddress, toAddress;
                    /* TODO ##这个地方需要改成自己的邮箱 */
                    fromAddress = new InternetAddress("hcholding_log@163.com");
                    toAddress = new InternetAddress("hcholding_log@163.com");

                    // 配置发送信息
                    MimeMessage message = new MimeMessage(session);
                    message.setSubject("错误日志");
                    message.setFrom(fromAddress);
                    message.addRecipient(javax.mail.Message.RecipientType.TO, toAddress);
                    message.setText("hello");
                    message.saveChanges();
                    Transport.send(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    public void sendEmailWithAttach(final View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Properties props = new Properties();   //设置邮箱服务器
                props.put("mail.smtp.protocol", "smtp");
                props.put("mail.smtp.auth", "true"); // 设置要验证
                props.put("mail.smtp.host", "smtp.163.com"); // 设置host
                props.put("mail.smtp.port", "25"); // 设置端口

                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("webcon_log@163.com", "webcon-log");  //设置用户名密码
                    }
                }); // 获取验证会话

                try {
                    // 配置发送及接收邮箱
                    InternetAddress fromAddress, toAddress;
                    /* TODO ##这个地方需要改成自己的邮箱 */
                    fromAddress = new InternetAddress("webcon_log@163.com");
                    toAddress = new InternetAddress("webcon_log@163.com");

                    MimeMultipart allMultipart = new MimeMultipart("mixed"); // 附件
                    // 设置文件到MimeMultipart
                    MimeBodyPart contentPart;
                    if (view.getId() == R.id.button7) {
                        contentPart = createContentPart("错误日志", 1);
                    }  // button7 发log文件 --->"错误日志" 文字内容
                    else {
                        contentPart = createContentPart("照片", 2);
                    } // button8 发jpg  --->"照片" 文字内容

                    allMultipart.addBodyPart(contentPart);


                    // 配置发送信息
                    MimeMessage message = new MimeMessage(session);
                    message.setSubject("错误日志+附件");  // 邮件大标题
                    message.setFrom(fromAddress);
                    message.addRecipient(javax.mail.Message.RecipientType.TO, toAddress);
                    message.setText("hello");   // 邮件文字内容-->被message.setContent(allMultipart)中的覆盖
                    message.setContent(allMultipart); // 发邮件时添加附件
                    message.saveChanges();
                    Transport.send(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }

    /**
     * 发送带附件的邮件
     */
    private MimeBodyPart createContentPart(String bodyStr, int type)
            throws Exception {

        File dir = null;
        if (type == 1) {
            dir = new File(TEMP_LOG_PATH);
        } else if (type == 2) {
            dir = new File(SCREEN_SHOTS);
        }

        File[] files = dir.listFiles();

        // 用于保存最终正文部分
        MimeBodyPart contentBody = new MimeBodyPart();
        // 用于组合文本和图片，"related"型的MimeMultipart对象
        MimeMultipart contentMulti = new MimeMultipart("related");

        // 正文的文本部分
        MimeBodyPart textBody = new MimeBodyPart();
        textBody.setContent(bodyStr, "text/html;charset=utf-8");
        contentMulti.addBodyPart(textBody);

        /**
         * 一下内容是：发送邮件时添加附件
         */
        MimeBodyPart attachPart = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(files[0]); // 打开要发送的文件
        if (fds.getFile().length() > 0) {
            attachPart.setDataHandler(new DataHandler(fds));
            attachPart.setFileName(fds.getName());
            contentMulti.addBodyPart(attachPart);
        }

        // 将上面"related"型的 MimeMultipart 对象作为邮件的正文
        contentBody.setContent(contentMulti);
        return contentBody;
    }


    public void encodingDemo(View view) throws Exception {
        Log.i("AAA", "byte array length:DEFAULT" + "Hello World飞机".getBytes().length);// default = utf-8
        Log.i("AAA", "byte array length:UTF-8" + "Hello World飞机".getBytes("UTF-8").length);//utf-8 : 英文1个byte，中文3个byte
        Log.i("AAA", "byte array length:GBK" + "Hello World飞机".getBytes("GBK").length);//GBK : 英文1个byte，中文2个byte

//// ------------byte is signed---------------
//        // byte -128~127,  256 in total-->   256=0xFF
//        byte[] byte_array=new byte[256];
//        for(int a=0;a<256;a++) byte_array[a]=(byte)a;
//        for (byte b1 : byte_array) {
//            Log.i("AAA",""+b1+"---"+Integer.toHexString(b1&0xFF)+"");  // byte
//        }

        // int to byte array
//        ByteOperations.dumpByteArray(ByteOperations.intToByteArray1(1785));
//        ByteOperations.dumpByteArray(ByteOperations.intToByteArray2(1785));
        int[] aa = {79, -4985, 1};
        ByteOperations.dumpByteArray(ByteOperations.intToByteArray2(aa));
    }


    public void randomAccessFileDemo(View view) {
        File dir = getExternalCacheDir();
        File fileTemp = new File(dir, "rafile.dat");
        try {
            // write to RandomAccessFile
            RandomAccessFile rafile = new RandomAccessFile(fileTemp, "rw");
            rafile.write("大炮Hello World飞机".getBytes());// test of writing a string

            Log.i("AAA", "byte array length" + "大炮Hello World飞机".getBytes().length);
            Log.i("AAA", "before seek" + rafile.length()); // length of the file which has nothing to-do with pointer position
//            rafile.close();
            rafile.seek(4);   // change pointer position  --> set the pointer to the 4th byte
            Log.i("AAA", "after seek" + rafile.length());


            // read from RandomAccessFile
            byte[] buffer = new byte[100];
            int len = 0;
            while ((len = rafile.read(buffer, 0, 100)) != -1) {
                Log.i("AAA", "length:" + len + "---" + (new String(buffer, 0, len)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void createFileInCache(View view) {
        File dir = getExternalCacheDir();
        FileOperation.deleteFiles(dir); // delete all the files under this directory .........
        FileOperation.printFiles(dir);// empty
        File saveFile = new File(dir, "TEST1.data");
        FileOperation.printFiles(dir);// empty
        Log.i("AAA", "file created?" + saveFile.exists());  // false
        try {
            FileOutputStream outStream = new FileOutputStream(saveFile);  //  new FileOutputStream(saveFile,true)  -->  append
            outStream.write("this is a test string".getBytes());
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOperation.printFiles(dir);//
        Log.i("AAA", "file created?" + saveFile.exists());  //
    }


    public void writeToFile_byteBased(View view) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        long timestamp = System.currentTimeMillis();
        String time = formatter.format(new Date());
        String fileName = "test-" + time + "-" + timestamp + ".log";  // get the filename

        if (Environment.getExternalStorageState().equals(   //检查是否有ExternalStorage
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(TEMP_LOG_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File saveFile = new File(TEMP_LOG_PATH, fileName);
            Log.i("AAA", "write-bytebased FILE PATH:" + TEMP_LOG_PATH);  //  this is "external storage?" --->  /storage/emulated/0/demos1/temp/Log/

            try {
//                saveFile.createNewFile();
                FileOutputStream outStream = new FileOutputStream(saveFile);  //  new FileOutputStream(saveFile,true)  -->  append
                outStream.write("this is a test string".getBytes());
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void writeToFile_charBased(View view) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        long timestamp = System.currentTimeMillis();
        String time = formatter.format(new Date());
        String fileName = "test-" + time + "-" + timestamp + ".log";  // get the filename

        if (Environment.getExternalStorageState().equals(   //检查是否有ExternalStorage
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(TEMP_LOG_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File saveFile = new File(TEMP_LOG_PATH, fileName);
            Log.i("AAA", "write-charbased  FILE PATH:" + TEMP_LOG_PATH);  //  this is "external storage?" --->  /storage/emulated/0/demos1/temp/Log/

            try {
//                saveFile.createNewFile();
                FileWriter fwriter = new FileWriter(saveFile, true);  //  new FileWriter(saveFile,true);  -->  append
                fwriter.write("this is a test string");
                fwriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * test to write to SD card
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            File folder = getExternalCacheDirs()[1]; //  /storage/sdcard1/Android/data/test1.nh.com.demos1/cache
            File sdFile = new File(folder, "1.txt");
            try {
                sdFile.createNewFile();
                FileWriter fwriter = new FileWriter(sdFile, true);  //  new FileWriter(saveFile,true);  -->  append
                fwriter.write("this is another test string");
                fwriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void readFiles_inDirectory(View view) {
        if (Environment.getExternalStorageState().equals(   //检查是否有ExternalStorage???
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(TEMP_LOG_PATH);
            File[] files = dir.listFiles();
//            Log.i("AAA", "??"+(null!=files)+"---"+files.length);//  true , 0
            if (files.length > 0) {
                for (File file1 : files) {
                    Log.i("AAA", "" + file1.getAbsolutePath() + "-----" + file1.getName());
                    // .getAbsolutePath() -->   /mnt/sdcard/demos1/temp/Log/test-2015-12-16_15-10-45-1450249845140.log   (Lenova API15)
                    // .getAbsolutePath() -->   /storage/emulated/0/demos1/temp/Log/test-2015-12-16_15-10-45-1450249845140.log (Coolpad API19)
                }
            } else {
                Log.i("AAA", "the current folder is empty");
            }
        }
    }


    /***
     * --------------------------------------------------------------
     * these are methods to get internal storage /external storage directories
     *
     * @param view
     */
    public void printStorageDirs(View view) {
        Log.i("AAA", "getCacheDir()+absPath:" + getCacheDir().getAbsolutePath());//  /data/data/test1.nh.com.demos1/cache
        Log.i("AAA", "getDir()+absPath:" + getDir("aaa1", Context.MODE_PRIVATE).getAbsolutePath()); // /data/data/test1.nh.com.demos1/app_aaa1
        Log.i("AAA", "getDatabasePath()+absPath:" + getDatabasePath("aaa2").getAbsolutePath()); // /data/data/test1.nh.com.demos1/databases/aaa2
        Log.i("AAA", "getFilesDir()+absPath:" + getFilesDir().getAbsolutePath());  //   /data/data/test1.nh.com.demos1/files
        Log.i("AAA", "Environment.getExternalStorageDirectory()+absPath:" + Environment.getExternalStorageDirectory().getAbsolutePath());
        //  /mnt/sdcard   (Lenova API15)
        //  /storage/emulated/0  (Coolpad API19)
        // ----------- first check the state before IO operations -----------------
        Log.i("AAA", "Environment.getExternalStorageState():" + Environment.getExternalStorageState());  // mounted or not or ...
        // Environment.MEDIA_MOUNTED--> read/write      Environment.MEDIA_MOUNTED_READ_ONLY --> read
        Log.i("AAA", "Environment.isExternalStorageRemovable():" + Environment.isExternalStorageRemovable());  // true --> SD card  false --> built-in
        Log.i("AAA", "Environment.getExternalStorageDirectory()" + Environment.getExternalStorageDirectory().getAbsolutePath());//  API16:  /storage/sdcard0


        // get app-private directories.
        Log.i("AAA", "getExternalCacheDir()" + getExternalCacheDir().getAbsolutePath());  //  --> getExternalCacheDirs()[0]

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.i("AAA", "getExternalFileDir()" + getExternalFilesDirs("AA"));  //@TargetApi(Build.VERSION_CODES.KITKAT)
            FileOperation.printFiles(getExternalFilesDirs("AA")); //@TargetApi(Build.VERSION_CODES.KITKAT)
        }

        //  public files shared with other apps  -->  DIRECTORY_MUSIC, DIRECTORY_PICTURES, DIRECTORY_RINGTONES
        Log.i("AAA", "Environment.getExternalStoragePublicDirectory(type...)" +
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());  //    /storage/sdcard0/Pictures  -->


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int file_num = 0;
            File[] files = getExternalFilesDirs(null);  // this require API 19
            if (files.length > 0) {
                for (File file_dummy : files) {
                    file_num = file_num + 1;
                    Log.i("AAA", "getExternalFilesDirs" + file_num + file_dummy.getAbsolutePath());
                    //  (Coolpad API19)
                    //  /storage/emulated/0/Android/data/test1.nh.com.demos1/files
                    //  /storage/sdcard1/Android/data/test1.nh.com.demos1/files  --> sd card
                }
            }

            file_num = 0;
            files = getExternalCacheDirs();  // this require API 19??
            if (files.length > 0) {
                for (File file_dummy : files) {
                    file_num = file_num + 1;
                    Log.i("AAA", "getExternalCacheDirs" + file_num + file_dummy.getAbsolutePath());
                    //  (Coolpad API19)
                    //  /storage/emulated/0/Android/data/test1.nh.com.demos1/cache
                    //  /storage/sdcard1/Android/data/test1.nh.com.demos1/cache
                }
            }
        }
    }


    // create a file and write to this file on android sd card
    public void wirte2sdCard(View view) {
//        Log.i("AAA", "Environment.getExternalStorageDirectory()"+ Environment.getExternalStorageDirectory().getAbsolutePath());//  API16:  /storage/sdcard0
//        File sd_dir = Environment.getExternalStorageDirectory();
//        FileOperation.printFiles(sd_dir);  // gives all the files in the ROM   /storage/sdcard0/
        File sd_dir = new File("/storage/sdcard1/");
        FileOperation.printFiles(sd_dir);
        // use reflection to access Environment.UserEnvironment  ???????-----------........
//        try {
//            Field field=Environment.class.getDeclaredField("UserEnvironment");
//            field.setAccessible(true);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
    }


    public void writeIntArray(View view) {
        int[] aaa = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,19999};

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
//        long timestamp = System.currentTimeMillis();
        String timestamp = "";

        String time = formatter.format(new Date());
        String fileName = "test-" + time + "-" + timestamp + ".data";  // get the filename

        if (Environment.getExternalStorageState().equals(   //检查是否有ExternalStorage
                Environment.MEDIA_MOUNTED)) {
            File dir = getFilesDir();  // files directory      // File sd_dir = new File("/storage/sdcard1/");
//            File dir =new File("/storage/sdcard0/");

            if (!dir.exists()) {
                dir.mkdirs();   // 建立folder
            }
            File saveFile = new File(dir, fileName);

            try {
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(saveFile));

                int index = 0;
                while (index < aaa.length) {
                    dos.writeInt(aaa[index]);
                    index++;
                    Log.i("AAA", "writing..." + index);
                }


                DataInputStream dis = new DataInputStream(new FileInputStream(saveFile));
                int totalBytes = dis.available();
                while (totalBytes > 0) {
                    Log.i("AAA", "read recording..." + dis.readInt());
                    totalBytes = dis.available();
                }

                long crc32test=FileUtils.checksumCRC32(saveFile);
                Log.i("AAA", "crc32: " + crc32test);

                FileOperation.printFiles(dir,"test"); // print files whose names start with "test"


            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }



    public void writeIntArrayRX(View view) {
        IOmanager ioManager=new IOmanager();
        int[] aaa=new int[5000];
        for (int a=0;a<5000;a++){
            aaa[a]=a;
        }

        Observer observer=new Observer() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(Object o) {
                Log.i("AAA","on next"+Thread.currentThread().getName());
            }
        };

        Observable observable=ioManager.getWriteObservable(this,aaa,new GregorianCalendar());
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(observer); // observe on UI thread

    }


}
