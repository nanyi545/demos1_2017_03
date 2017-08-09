package test1.nh.com.demos1.activities.staggered_lo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */

public class RvItem {

    public RvItem(int height, String txt) {
        this.height = height;
        this.txt = txt;
    }

    int height;//  height in DP, width is half of the RV

    String txt;// display txt

    public int getHeight() {
        return height;
    }

    public String getTxt() {
        return txt;
    }



    public static List<RvItem> getTestData(){
        List<RvItem> list=new ArrayList<>();
        for (int i=10;i<100;i+=10){
            list.add(new RvItem(i,"haha"+i));
        }
        return list;
    }

}
