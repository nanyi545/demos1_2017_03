package test1.nh.com.demos1;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Administrator on 16-1-16.
 */
public class CollectionTest {

    @Test
    public void testArrayList(){
        List<Integer> intArray=new ArrayList<Integer>();
        intArray.add(new Integer(1));
        intArray.add(new Integer(2));
        intArray.add(new Integer(3));
        System.out.println(""+intArray);
        Integer a1=new Integer(1);
        a1=intArray.get(2);
        System.out.println("a1:"+a1);
        a1=5;
        System.out.println("a1 after reset:"+a1);
        System.out.println("array after reset:"+intArray);  // the original array list is not altered
    }


    @Test
    public void testArrayListRemove(){
        List<Integer> intArray=new ArrayList<Integer>();
        intArray.add(new Integer(1));
        intArray.add(new Integer(2));
        intArray.add(new Integer(3));
        intArray.add(new Integer(1));
        intArray.add(new Integer(2));
        intArray.add(new Integer(3));

        Iterator<Integer> it=intArray.iterator();
        while (it.hasNext()){
            Integer tempInt=it.next();
            if (tempInt==1){
                it.remove();
            }
        }

        System.out.println("array after reset:"+intArray);  // the original array list is not altered
    }



    @Test
    public void queueTest1(){
        Queue<String> queueA = new LinkedList<String>();
        queueA.add("element 1");
        queueA.add("element 2");
        queueA.add("element 3");
        System.out.println("peek1:"+queueA.peek()+"  peek2:"+queueA.peek()+"  peak3:"+queueA.peek()+"  peak4"+queueA.peek());
        // peak only return the head of the queue but not remove it from the queue

        System.out.println("poll1:"+queueA.poll()+"  poll2:"+queueA.poll()+"  poll3:"+queueA.poll()+"  poll4"+queueA.poll());
        //poll return null if queue is empty
    }

    @Test
    public void queueTest12(){
        Queue<String> queueA = new PriorityQueue<String>(3);
        queueA.add("element 1");
        System.out.println("size:"+queueA.size());
        queueA.add("element 2");
        System.out.println("size:"+queueA.size());
        queueA.add("element 3");
        System.out.println("size:"+queueA.size());
        queueA.add("element 4");
        System.out.println("size:"+queueA.size());

        System.out.println("poll1:"+queueA.poll()+"  poll2:"+queueA.poll()+"  poll3:"+queueA.poll()+"  poll4"+queueA.poll());

    }


    @Test
    public void mapTest1(){
        HashMap<String ,String > map1=new HashMap();
        map1.put("a","1");
        map1.put("b","2");
        map1.put("c","3");
        map1.put("d","4");
        map1.put("dd","4");
        map1.put("dddd","4");
        map1.put("dddddd","4");
        map1.put("ddddddddd","4");

        try {
            Field tableField=HashMap.class.getDeclaredField("table");
            tableField.setAccessible(true);
            Map.Entry[] table= (Map.Entry[]) tableField.get(map1);

            int size=table.length;
            for (int i=0;i<size;i++){
                System.out.println("i:"+i+"    "+table[i]+"   "+(table[i]!=null?table[i].getClass().getName():"null"));
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void setTest1(){
        HashSet set=new HashSet();
        Object a1=new Object();
        Object a2=new Object();
        Object a3=new Object();
        Object a4=new Object();
        set.add(a1);
        set.add(a2);
        set.add(a3);
        System.out.println("a4:"+set.remove(a4) );
    }





    @Test
    public void test2(){
        generate(2);
    }


    public List<List<Integer>> generate(int numRows) {


        List<List<Integer>> ret=new ArrayList();
        if (numRows==0) return ret;

        List<Integer> l0=new ArrayList();
        l0.add(1);
        ret.add(l0);

        if (numRows==1) return ret;
        else {


            for (int i=1;i<numRows;i++){
                ret.add(add1(ret.get(i-1)));
            }

        }
        return ret;

    }


    List<Integer> add1(List<Integer> list) {

        List<Integer> ret=new ArrayList(list.size()+1);
        ret.add(1);

        if (list.size()==0) return ret;
        if (list.size()>1) {
            for (int i=0;i<list.size()-1;i++) ret.add(list.get(i)+list.get(i+1));
        }
        ret.add(1);

        return ret;

    }






}
