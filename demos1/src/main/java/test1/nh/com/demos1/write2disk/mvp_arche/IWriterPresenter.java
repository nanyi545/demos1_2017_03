package test1.nh.com.demos1.write2disk.mvp_arche;

import java.io.File;

/**
 * Created by Administrator on 16-2-15.
 */
public interface IWriterPresenter {


    void appendInt2File(File file);
    void readIntArrayFromFile(File file);

}
