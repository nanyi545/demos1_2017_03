package test1.nh.com.demos1.utils.rxtest;

/**
 * Created by Administrator on 15-12-18.
 */
public class Student {

    public Student(String name) {
        this.name = name;
    }

    private Course[] courses;
    private String name;

    public Course[] getCourses() {
        return courses;
    }

    public void setCourses(Course[] courses) {
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
