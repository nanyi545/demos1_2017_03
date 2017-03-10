package test1.nh.com.demos1.reflectionTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

import test1.nh.com.demos1.entities.testOveride.AA;
import test1.nh.com.demos1.entities.testOveride.B;
import test1.nh.com.demos1.entities.testOveride.IPrint;

/**
 * Created by Administrator on 15-12-30.
 */
public class ReflectionTest {

    Class c1=B.class;
    Class c3;

    @Before
    public void before(){
        try {
            c3=Class.forName("test1.nh.com.demos1.entities.testOveride.B");
//            c3=Class.forName("B");// -->no good
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @After
    public void after(){
    }

    @Test
    public void testClass(){
        try {
            B b1=(B)c1.newInstance();

            String className = c1.getName();
            System.out.println("className:"+className);
            String simpleClassName = c1.getSimpleName();
            System.out.println("simpleClassName:"+simpleClassName);

            int modifiers = c1.getModifiers();  // get modifiers
            System.out.println("class modifier:"+modifiers);   /** {@link Modifier}  for more details **/
//            Modifier.isAbstract(modifier);
//            Modifier.isFinal(modifier);
//            Modifier.isInterface(modifier);
//            Modifier.isNative(modifier);
//            Modifier.isPrivate(modifier);
//            Modifier.isProtected(modifier);
//            Modifier.isPublic(modifier);
//            Modifier.isStatic(modifier);
//            Modifier.isStrict(modifier);
//            Modifier.isSynchronized(modifier);
//            Modifier.isTransient(modifier);
//            Modifier.isVolatile(modifier);

            Package package1 = c1.getPackage();// get package
            System.out.println(package1.toString());

            Class superclass = c1.getSuperclass();// get superclass
            System.out.println(superclass.getName());


            //---- get implemented interfaces -------
            Class[] interfaces = c1.getInterfaces();
            for(int aa=0;aa<interfaces.length;aa++){System.out.println(interfaces[aa].getName());}

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void test1(){
        try {
            B b1=(B)c1.newInstance();
            b1.printThis();

            B b3=(B)c3.newInstance();
            b3.printThis();

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMethod(){
        try {
            B b1=(B)c1.newInstance();

            Method m1=c1.getMethod("printThis",(Class[])null);  // NoSuchMethodException  //getMethod --> public
            m1.invoke(b1,(Object[])null);

            Method m2=c1.getDeclaredMethod("printThis",new Class[]{String.class});  // any method declared by the class
            m2.setAccessible(true);// set true if it is
            m2.invoke(b1,new Object[]{"reflection"});

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testField(){
        try {
            B b1=(B)c1.newInstance();
//            Field field1=c1.getDeclaredField("a");
            Field field1=c1.getField("a");
            field1.set(b1,122);
            int a=(int)field1.get(b1);
            System.out.println("obtained:"+a);
            b1.printThis();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    public class MyInvocationHandler implements InvocationHandler {
        private Object obj;
        public MyInvocationHandler(Object obj){
            this.obj=obj;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result=method.invoke(obj,args);
            return result;
        }
    }


    @Test
    public void testProxy(){
//        Proxy.newProxyInstance();
        IPrint aa=new AA();
        InvocationHandler handler = new MyInvocationHandler(aa);
        ClassLoader loader=aa.getClass().getClassLoader();
        IPrint aaProxy=(IPrint) Proxy.newProxyInstance(loader,new Class[] { IPrint.class },handler);
        aaProxy.printThis("----dynamic proxy!----");
        //--- All calls to the proxy will be forwarded to the handler implementation
        //--- of the general InvocationHandler interface.
    }

}
