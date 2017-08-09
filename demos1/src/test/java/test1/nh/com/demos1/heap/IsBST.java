package test1.nh.com.demos1.heap;

/**
 * Created by Administrator on 2017/6/26.
 */
import org.junit.Test;

public class IsBST {

    /**

     1 The left subtree of a node contains only nodes with keys less than the node's key.
     2 The right subtree of a node contains only nodes with keys greater than the node's key.
     3 Both the left and right subtrees must also be binary search trees

     */


    @Test
    public void testCheck(){
        int[] test=new int[]{1,-1,3,-2,0,2,7,-10,-2};
//        System.out.println(isFullRecursion(test,0));
        System.out.println(isValideBST(test,0,Integer.MIN_VALUE,Integer.MAX_VALUE));
        inOrderTraversal(test,0);
        System.out.println(validBST);
        System.out.println("--------------------------");
        int[] test1=new int[]{0,-20,20,-40,-18,18,50,-60,-21,-19,-17,17,19,49,61};
        System.out.println(isValideBST(test1,0,Integer.MIN_VALUE,Integer.MAX_VALUE));
        System.out.println(test1[findLCA(test1,-19,49,0)]);
    }

    /**
     *------------- Q1 check if a valid BST---------------
     */

    /**
     *   method 1
     *   start from the root, the lower/upper bounds are passed down layer by layer
     *
     */
    boolean isValideBST(int[]arr,int index,int min,int max){
        if (index>=arr.length) return true;  // equivalent to node is null..
        else return (arr[index]>=min)&&(arr[index]<=max)&&(isValideBST(arr,getLeft(index),min,arr[index]))&&(isValideBST(arr,getRight(index),arr[index],max));
    }


    /**
     *    method 2
     *    in-order traversal ...    if the result is monotonic , it is valid BST
     */
    void inOrderTraversal(int[] arr,int index){
        if (index>=arr.length) return;
        inOrderTraversal(arr,getLeft(index));
        boolean valid=visit(arr,index);
        if(!valid){
            validBST=false;
            return;
        }
        inOrderTraversal(arr,getRight(index));
    }
    boolean validBST=true;

    int previousValue=Integer.MIN_VALUE;

    boolean visit(int[] arr,int index){
//        System.out.println(arr[index]);
        boolean ret= arr[index]>=previousValue;
        previousValue=arr[index];
        return ret;
    }



    /**
     ------------- Q2 find LCA (lowest common ancestor)  BST ---------------

     solution :  while traversing from top to bottom, the first node n we encounter with value between n1 and n2 or same as one of the n1 or n2 ---> is LCA of n1 and n2   (this is only for BST)


     find LCA of n1 and n2 in a BST
     */
    int findLCA(int[]arr,int n1,int n2,int currentLCA){
        boolean n1Small=n1<=arr[currentLCA];
        boolean n2Small=n2<=arr[currentLCA];
        if(((n1Small&&(!n2Small)))||((!n1Small&&n2Small))){
            return currentLCA;
        }
        if (n1Small&&n2Small){
            return findLCA(arr,n1,n2,getLeft(currentLCA));
        }
        if (!n1Small&&!n2Small){
            return findLCA(arr,n1,n2,getRight(currentLCA));
        }
        return 0;
    }





    /**
     *         0
     *      1     2
     *    3   4 5   6
     *   7 8 ....
     */
    int getLeft(int parent){
        return parent*2+1;
    }

    int getRight(int parent){
        return parent*2+2;
    }

    int getParent(int child){
        int res=child%2;
        if(res==1){
            return child/2;
        }
        if (res==0){
            return child/2-1;
        }
        return 0;
    }




}
