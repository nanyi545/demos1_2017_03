package test1.nh.com.demos1.heap;

/**
 * Created by Administrator on 2017/6/28.
 */
import android.annotation.TargetApi;
import android.os.Build;

import org.junit.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

public class PriorityQueueDemo {

    /**
     *  find medium of data stream
     *
     *
     */

    Comparator<Integer> comparatorLarger=new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return (o1-o2);
        }
    };
    Comparator<Integer> comparatorSmaller=new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return -(o1-o2);
        }
    };
    PriorityQueue<Integer> larger=new PriorityQueue<>(10,comparatorLarger);
    PriorityQueue<Integer> smaller=new PriorityQueue<>(10,comparatorSmaller);

    void add(int data){
        larger.add(data);
        smaller.add(larger.poll());
        if (larger.size() < smaller.size())
            larger.add(smaller.poll());
    }

    void printData(){
        Iterator<Integer> largerIter=larger.iterator();
        Iterator<Integer> smallerIter=smaller.iterator();
        StringBuilder sb1=new StringBuilder();
        StringBuilder sb2=new StringBuilder();
        while (largerIter.hasNext()){
            sb1.append("|"+largerIter.next());
        }
        while (smallerIter.hasNext()){
            sb2.append("|"+smallerIter.next());
        }
        System.out.println("larger:"+sb1.toString());
        System.out.println("smaller:"+sb2.toString());
    }


    @Test
    public void testMedian(){
        add(9);add(3);add(7);add(1);
        printData();
        add(11);add(13);add(17);add(19);
        printData();
    }



    /**
     *  priority queue does not have search api  ( like map.get(key) ) ...
     *
     *
     *  poll()   --> get the smallest and remove it
     *  peak()   --> get the smallest without remove it
     *
     *
     */

    @Test
    public void test1(){
        Comparator<Integer> comparator=new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (o1-o2);
            }
        };
        PriorityQueue<Integer> queue=new PriorityQueue<>(10,comparator);
        queue.add(6);
        queue.add(12);
        queue.add(1);
        queue.add(30);
        System.out.println("poll:"+queue.poll()+"    poll:"+queue.poll()+"    poll:"+queue.poll()+"    poll:"+queue.poll());
    }


    @Test
    public void test2(){
        Comparator<Entry> comparator=new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                return (o1.key-o2.key);
            }
        };

        PriorityQueue<Entry> queue=new PriorityQueue<>(10,comparator);
        queue.add(new Entry(6,0));
        queue.add(new Entry(12,0));
        queue.add(new Entry(1,0));
        queue.add(new Entry(30,0));

        System.out.println(  queue.contains(new Entry(12,0)) );  //  false
        System.out.println(  queue.contains(new Entry(11,100)) ); //  false
        //  priority queue does not have search api  ( like map.get(key) ) ...

//        System.out.println("poll:"+queue.poll()+"    poll:"+queue.poll()+"    poll:"+queue.poll()+"    poll:"+queue.poll());

    }


    class Entry{
        int key;
        int val;
        public Entry(int key, int val) {
            this.key = key;
            this.val = val;
        }
        @Override
        public String toString() {
            return "Entry{" +
                    "key=" + key +
                    ", val=" + val +
                    '}';
        }
    }




}
