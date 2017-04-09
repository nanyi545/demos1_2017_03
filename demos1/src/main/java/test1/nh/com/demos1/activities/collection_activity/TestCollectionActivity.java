package test1.nh.com.demos1.activities.collection_activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

import test1.nh.com.demos1.R;

public class TestCollectionActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,TestCollectionActivity.class);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_collection);

        testHashMap();
        initShoppingCart();

    }



    private class Product{

        private int id;

        public Product(int id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public String toString() {
            return "productID:"+id;
        }
    }
    Product[] products;
    private void generateProducts(){
        products=new Product[6];
        products[0]=new Product(0);
        products[1]=new Product(1);
        products[2]=new Product(2);
        products[3]=new Product(3);
        products[4]=new Product(4);
        products[5]=new Product(5);
    }

    LinkedHashMap<Product,Integer> shoppingList;

    /**
     * shopping car ADT
     *
     * 1 add product   --> if already added, move it to the front, count +1
     *                   if not added, add it to the front, count = 1
     * 2 remove product--> remove it from  shopping car
     * 3 minus product --> count -1, if count ==0 , call remove product ...
     * 4 get products  --> return a list of products ( LIFO )
     *
     * use LinkedHashMap  ??...
     */
    private void initShoppingCart(){

        generateProducts();
        shoppingList=new LinkedHashMap();
        Button add0= (Button) findViewById(R.id.add0);
        Button add1= (Button) findViewById(R.id.add1);
        Button add2= (Button) findViewById(R.id.add2);
        Button add3= (Button) findViewById(R.id.add3);
        Button add4= (Button) findViewById(R.id.add4);
        Button add5= (Button) findViewById(R.id.add5);

        Button min0= (Button) findViewById(R.id.min0);
        Button min1= (Button) findViewById(R.id.min1);
        Button min2= (Button) findViewById(R.id.min2);
        Button min3= (Button) findViewById(R.id.min3);
        Button min4= (Button) findViewById(R.id.min4);
        Button min5= (Button) findViewById(R.id.min5);


        add0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct(0);
            }
        });
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct(1);
            }
        });
        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct(2);
            }
        });
        add3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct(3);
            }
        });
        add4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct(4);
            }
        });
        add5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct(5);
            }
        });


        min0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusProduct(0);
            }
        });
        min1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusProduct(1);
            }
        });
        min2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusProduct(2);
            }
        });
        min3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusProduct(3);
            }
        });
        min4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusProduct(4);
            }
        });
        min5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusProduct(5);
            }
        });


    }




    private void addProduct(int index){
        if(shoppingList.containsKey(products[index])){
            int tempCount=shoppingList.get(products[index]);
            shoppingList.remove(products[index]);
            shoppingList.put(products[index],tempCount+1);
            Log.i("ccc","index:"+index+"   count:"+(tempCount+1));
        } else {
            shoppingList.put(products[index],1);
            Log.i("ccc","index:"+index+"   count:"+1);
        }
        displayShoppingList();
    }

    private void minusProduct(int index){
        if(shoppingList.containsKey(products[index])){
            int tempCount=shoppingList.get(products[index]);
            if(tempCount>1){
                shoppingList.remove(products[index]);
                shoppingList.put(products[index],tempCount-1);
            } else {
                shoppingList.remove(products[index]);
            }
        } else {
            //  ....  no action ..
        }
        displayShoppingList();
    }


    private void displayShoppingList(){
        Set<Map.Entry<Product,Integer>> set=shoppingList.entrySet();
        Iterator<Map.Entry<Product,Integer>> it=set.iterator();
        StringBuilder displayInfo=new StringBuilder();

        while(it.hasNext()){
            Map.Entry<Product,Integer>  entry=it.next();
            displayInfo.insert(0,"id:"+entry.getKey().hashCode()+"  count:"+entry.getValue()+"\n");
        }
        displayInfo.insert(0,"购物车\n");
        TextView tv= (TextView) findViewById(R.id.gouwuche);
        tv.setText(displayInfo.toString());
    }



    private void testHashMap(){

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
                Log.i("ccc","i:"+i+"    "+table[i]+"   "+(table[i]!=null?table[i].getClass().getName():"null"));  //   ????  why some of the items are not in the table array
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        Log.i("ccc","b="+map1.get("b"));

        Set<Map.Entry<String,String>> set1= map1.entrySet();
        Iterator<Map.Entry<String,String>> it=set1.iterator();
        while (it.hasNext()){
            Map.Entry<String,String> temp=it.next();
            Log.i("ccc","entrySet():    key:"+temp.getKey()+"   value:"+temp.getValue());
        }

        Set<String> set2=map1.keySet();
        for (String temp:set2){
            Log.i("ccc","keyset():    key:"+temp+"   value:"+ map1.get(temp));
        }

    }






}
