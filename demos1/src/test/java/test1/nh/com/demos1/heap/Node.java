package test1.nh.com.demos1.heap;

/**
 * Created by Administrator on 2017/6/23.
 */

public class Node {

    public Node parent,left,right;


    public Node(Node parent,int value) {
        this.parent = parent;
        this.value=value;
    }


    public Node(int value) {
        this.value=value;
    }


    public boolean isRoot(){
        return parent==null;
    }

    public int value;



}
