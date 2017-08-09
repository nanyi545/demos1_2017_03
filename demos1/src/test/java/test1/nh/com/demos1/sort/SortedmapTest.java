package test1.nh.com.demos1.sort;

/**
 * Created by Administrator on 2017/6/28.
 */
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class SortedmapTest {


    /**

     Given a non-empty array of integers, return the k most frequent elements.

     For example,
     Given [1,1,1,2,2,3] and k = 2, return [1,2].


     */

    @Test
    public void test1(){
        int[] arr={1,1,1,2,2,3};


        System.out.println(topKFrequent(arr,2));



    }

    @Test
    public void test2(){

    }




    public List<Integer> topKFrequent(int[] nums,int k){
        HashMap<Integer,Integer> counter=new HashMap();


        List<Integer> ret=new ArrayList();
        for (int temp:nums){
            if (counter.containsKey(temp)){
                int oldCount=counter.get(temp);
                counter.put(temp,oldCount+1);
            } else {
                counter.put(temp, 1);
            }
        }
        Set<Map.Entry<Integer,Integer>> entries=counter.entrySet();
        List<Map.Entry<Integer,Integer>> list=new ArrayList(entries);
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o2.getValue()-o1.getValue();
            }
        });

        for (int i=0;i<k;i++){
            ret.add(list.get(i).getKey());
        }

        return ret;
    }




}
