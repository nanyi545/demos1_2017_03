// IMyAidlInterface.aidl
package test1.nh.com.demos1.ipcDemo.aidl;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);


// same of test1.nh.com.demos1.ipcDemo.IipcWork
    void storeUser(int id,String user);

    String getUser(int id);


}
