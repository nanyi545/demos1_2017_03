package test1.nh.com.demos1.write2disk.mvp_arche;

import java.io.File;

/**
 * Created by Administrator on 16-2-15.
 */
public interface IModel {

    void appendInt_model(int a,File file);
    int[] getIntArray_model(File file);

}
