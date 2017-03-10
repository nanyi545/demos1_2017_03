package test1.nh.com.demos1.mvpSQL.pack.views;

import java.util.List;

import test1.nh.com.demos1.utils.sqlite.Person4DB;

/**
 * Created by Administrator on 16-1-4.
 */
public interface IViewSQL {

    void refreshPersons(List<Person4DB> list);
    void setLoading(boolean isLoading);

    Person4DB getPerson();
    void setPersonLoading(boolean isLoading);




}
