package test1.nh.com.demos1.mvpDemo.pack;

/**
 * Created by Administrator on 16-1-3.
 */
public interface IDemoModel {

    void saveLastName (String lastName);
    String getLastName();

    void saveFirstName (String firstName);
    String getFirstName();

    void saveId(int id);
    int get_Id();

}
