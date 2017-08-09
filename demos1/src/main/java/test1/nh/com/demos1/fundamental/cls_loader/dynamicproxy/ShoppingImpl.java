package test1.nh.com.demos1.fundamental.cls_loader.dynamicproxy;

/**
 * Created by Administrator on 2017/6/5.
 */

public class ShoppingImpl implements Shopping {

    @Override
    public Object[] doShopping(long money) {
        System.out.println("逛淘宝 ,逛商场,买买买!!");
        System.out.println(String.format("花了%s块钱", money));
        return new Object[] { "鞋子", "衣服", "零食" };
    }

}
