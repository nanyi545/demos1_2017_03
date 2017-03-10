package test1.nh.com.demos1.activities.patterns;

/**
 * Created by Administrator on 2017/1/20.
 */
public class ProtoTypePattern implements Cloneable{

    private String str;
    private int testInt;

    public void setStr(String str) {
        this.str = str;
    }

    public void setTestInt(int testInt) {
        this.testInt = testInt;
    }

    @Override
    public String toString() {
        return "ProtoTypePattern{" +
                "str='" + str + '\'' +
                ", testInt=" + testInt +
                '}';
    }


    @Override
    public ProtoTypePattern clone() {
        ProtoTypePattern pattern= null;
        try {
            pattern = (ProtoTypePattern) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        pattern.setStr("---");
        pattern.setTestInt(88);
        return pattern;
    }

}
