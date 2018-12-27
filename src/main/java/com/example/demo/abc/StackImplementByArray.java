package com.example.demo.abc;

import java.util.Arrays;

public class StackImplementByArray<T> {
    Object[] item;
    int num = 0;//容量

    public StackImplementByArray(int capacity){
        item = new Object[capacity];
    }
    public  void push(Object obj){
        if (num == item.length){//扩容为2倍
            Resize(2 * item.length);
        }
       item[num++] = obj;
        //System.err.println(Arrays.toString(item));
    }

    public Object pop(){
        Object temp = item[--num];
        item[num] = null;//出栈元素置为null
        // 容量计算
        // Pop的时候，当元素的个数小于当前容量的1/4的时候，我们将原数组的大小容量减少1/2
        if (num>0 && num == item.length/4) Resize(item.length/2);
        return temp;
    }
    public  int size(){
        return  num;
    }

    public void Resize(int newCapacity){
        Object[] temp = new Object[newCapacity];
        long l1 = System.currentTimeMillis();
        for (int i = 0;i<item.length ;i++){
            temp[i] = item[i];
        }
        long l2 = System.currentTimeMillis();
        Object[] temp1 = new Object[newCapacity];
        long l3 = System.currentTimeMillis();
        System.arraycopy(item,0,temp1,0,item.length);
        long l4 = System.currentTimeMillis();
        System.err.println(l2-l1);
        System.err.println(l4-l3);

        item = Arrays.copyOf(item,newCapacity);

        System.out.println(Arrays.toString(temp));
        System.out.println(Arrays.toString(item));
        //item = temp;
        System.out.println(0);
        System.out.println(1000-000-000);
        System.out.println(1000_000_000);
        System.out.println(1000*000*000);
    }

    public static void main(String[] args) {
        System.out.println();
        System.out.println(1000-000-000);
        System.out.println(1000_000_000);
        System.out.println(1000*000*000);
    }
}
