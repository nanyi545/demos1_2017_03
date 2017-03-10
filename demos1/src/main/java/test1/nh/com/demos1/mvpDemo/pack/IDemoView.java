package test1.nh.com.demos1.mvpDemo.pack;

/**
 * Created by Administrator on 16-1-3.
 */
public interface IDemoView {

    void setLastName_view (String lastName);
    void setFirstName_view (String firstName);
    void setId_view(int id);

    String getLastName_view();
    String getFirstName_view();
    int getId_view();

}
