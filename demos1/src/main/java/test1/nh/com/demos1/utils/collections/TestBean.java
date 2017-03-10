package test1.nh.com.demos1.utils.collections;

import java.util.Comparator;

/**
 * Created by Administrator on 16-1-28.
 */
public class TestBean{
    public int size;
    public TestBean(int size_){
        this.size=size_;
    }
    public TestBean(){
        this.size=27;
    }

    @Override
    public String toString() {
        return "TestBean"+size;
    }


    public static class TestBeanComparator implements Comparator<TestBean> {
        @Override
        public int compare(TestBean lhs, TestBean rhs) {
            return lhs.size-rhs.size;
        }
    }


}
