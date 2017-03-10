package test1.nh.com.demos1.activities.generic_test;

/**
 * Created by Administrator on 2016/12/19.
 */
public class BoundTypeParam {

    public static <T extends Comparable<T>> int countGreaterThan(T[] anArray, T elem) {
        int count = 0;
        for (T e : anArray)
            if (e.compareTo(elem) > 0)
                ++count;
        return count;
    }

}
