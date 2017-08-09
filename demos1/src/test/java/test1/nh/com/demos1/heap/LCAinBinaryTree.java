package test1.nh.com.demos1.heap;

/**
 * Created by Administrator on 2017/6/26.
 */

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LCAinBinaryTree {



    @Test
    public void testSort(){
        Tree t1=makeTestTree();
//        List<Node> path1=new ArrayList<>();
//        findNode(t1.root,6,path1);
//        print(path1);
        System.out.println(findLCA(8,5,t1));


        //--------- draw a tree by in-order traversal ------
        int lines=10;
        int cols=30;
        aa=new char[lines][cols];
        for (int i=0;i<lines;i++){
            for(int j=0;j<cols;j++){
                aa[i][j]=' ';
            }
        }
        inOrderTraversal(t1.root,0);
        print(aa);

    }



    /**
     * ----------- Q1 ---------- find LCA in a binary tree  (ordinary BT not BST)
     */


    /**
     *   method1 ,  find the paths to the 2 nodes separately
     *              then traverse the paths to find the last node that are the same
     */
    int findLCA(int n1,int n2, Tree t){
        List<Node> pathToN1=new ArrayList<>();
        findNode(t.root,n1,pathToN1);
        List<Node> pathToN2=new ArrayList<>();
        findNode(t.root,n2,pathToN2);
        int ind=0;
        while(pathToN1.get(ind+1).value==pathToN2.get(ind+1).value){
            ind+=1;
        }
        return pathToN1.get(ind).value;
    }

    /**
     *   find path to a Node with a given value v, Node n is the root of the binary tree,
     *
     *   by pre-order traversal  -->  first add the node to the path anyway,   if later found it does not contain the target, delete it from the path ...
     */
    boolean findNode(Node n,int v,List<Node> path){
        path.add(n);
        if(n.value==v) return true;
        if(n.left!=null){
            boolean inLeftSubTree = findNode(n.left,v,path);
            if(inLeftSubTree) return true;
        }
        if(n.right!=null){
            boolean inRightSubTree = findNode(n.right,v,path);
            if(inRightSubTree) return true;
        }
        path.remove(path.size()-1);
        return false;
    }



    /**
     * ----------- Q2 ---------- draw a binary tree
     *
     * solution : in order traversal
     */
    /**
     * drawing matrix aa,  it is assumed tree is within aa's initial size
     */
    char[][] aa;
    int y=0;
    void inOrderTraversal(Node n,int depth){
        if(n.left!=null){
            inOrderTraversal(n.left,depth+1);
        }
        visit(n,depth);
        if(n.right!=null){
            inOrderTraversal(n.right,depth+1);
        }
    }
    void visit(Node n,int depth){
        aa[depth][y]= (char) (n.value+48) ;   //  only for 0-9 to char
        y++;
    }







    class Node{
        int value;
        Node left,right;
        public Node(int value) {
            this.value = value;
        }
    }
    class Tree{
        Node root;
        public Tree(Node root) {
            this.root = root;
        }
    }
    Tree makeTestTree(){
        Node root=new Node(1);
        root.left=new Node(2);
        root.right=new Node(3);
        root.left.left=new Node(4);
        root.left.right=new Node(5);
        root.right.left=new Node(6);
        root.right.right=new Node(7);
        root.left.left.left=new Node(8);
        root.left.left.right=new Node(9);
        return new Tree(root);
    }





    private static void print(List<Node> arr){
        StringBuilder sb=new StringBuilder();
        for(Node temp:arr){
            sb.append("|"+temp.value);
        }
        System.out.println( sb.toString()) ;
    }


    private static void print(char[] line){
        StringBuilder sb=new StringBuilder();
        for(char temp:line){
            sb.append(temp);
        }
        System.out.println( sb.toString()) ;
    }
    private static void print(char[][] matric){
        for(char[] line:matric){
            print(line);
        }
    }




}
