package test1.nh.com.demos1.activities.post_file;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/24.
 */

public class SerializableManager {

    /**
     * Saves a serializable object.
     *
     * @param context The application context.
     * @param objectToSave The object to save.
     * @param fileName The name of the file.
     * @param <T> The type of the object.
     */
    protected static <T extends Serializable> void saveSerializable(Context context, T objectToSave, String fileName) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(objectToSave);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Loads a serializable object.
     *
     * @param context The application context.
     * @param fileName The filename.
     * @param <T> The object type.
     *
     * @return the serializable object.
     */
    protected static<T extends Serializable> T readSerializable(Context context, String fileName) {
        T objectToReturn = null;
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            objectToReturn = (T) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return objectToReturn;
    }


    /**
     * Removes a specified file.
     *
     * @param context The application context.
     * @param filename The name of the file.
     */
    protected static void removeSerializable(Context context, String filename) {
        context.deleteFile(filename);
    }











}
