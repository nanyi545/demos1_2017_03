package test1.nh.com.demos1.utils.genericTest;

/**
 * Created by Administrator on 16-1-28.
 */
public class GenericFactory<T> {

    Class theClass = null;

    public GenericFactory(Class theClass) {
        this.theClass = theClass;
    }

    public T createInstance()  {
        try {
            return (T) this.theClass.newInstance();    // only works for empty constructor
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }



}
