package com.cathaypacific.mmbbizrule;

public class Child1 extends Father {
//	public int i =2;
//	
//	public void print() {
//		System.out.println(i);
//	}
	
	
	

//    private int i = test1();
//    private static int j = method();
//    static {
//        System.out.print("6)");
//    }
    Child1(){
//        super(); //写不写都在，在子类构造器一定会调用父类的构造器
        System.out.print("7)");
    }
    
    Child1(int i) {
    	System.out.println("2222222222");
    }
//    {
//        System.out.print("8)");
//    }
// 
//    public int test1() {
//        System.out.print("9)");
//        return 1;
//    }
//    public static int method() {
//        System.out.print("10)");
//        return 1;
//    }
}
