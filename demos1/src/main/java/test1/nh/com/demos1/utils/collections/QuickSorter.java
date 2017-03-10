package test1.nh.com.demos1.utils.collections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 16-1-28.
 */
public class QuickSorter {


    /**
     *  TO sort with quick sort algorithm
     * @param list,list to be sorted
     * @param c1, comparator
     * @param <E>, element type
     * @return
     */
    public static <E> void quickSort(List<E> list, Comparator<E> c1){
        if (list.size()<2) return;
        int length=list.size();
        E pivote=list.get(length-1);
        List<E> l_list=new ArrayList<E>();
        List<E> e_list=new ArrayList<E>();
        List<E> g_list=new ArrayList<E>();
        while(!list.isEmpty()){
            E element=list.get(list.size()-1);
            if (c1.compare(element,pivote)<0) l_list.add(list.remove(list.size()-1));
            else if (c1.compare(element,pivote)==0) e_list.add(list.remove(list.size()-1));
            else g_list.add(list.remove(list.size()-1));
        }
        quickSort(l_list,c1);
        quickSort(g_list,c1);
        while(!l_list.isEmpty()) list.add(l_list.remove(0));
        while(!e_list.isEmpty()) list.add(e_list.remove(0));
        while(!g_list.isEmpty()) list.add(g_list.remove(0));
        return;
    }


}
