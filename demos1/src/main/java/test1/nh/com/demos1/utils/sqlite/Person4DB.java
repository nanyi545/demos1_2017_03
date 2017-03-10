package test1.nh.com.demos1.utils.sqlite;

/**
 * Created by Administrator on 15-12-28.
 */
public class Person4DB {

    private String name;
    private int age;

    public Person4DB(int age, String name) {
        this.age = age;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person4DB{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
