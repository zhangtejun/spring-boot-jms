package com.example.demo;


import java.util.concurrent.*;

public class CallTest {
    ///http://www.importnew.com/25286.html
    public static void main(String[] args) throws InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new Task());
        //// 3. 新建Thread对象并启动
        Thread thread = new Thread(futureTask);
        thread.setName("Task thread");
        thread.start();

        Thread.sleep(1000);
        System.out.println("Thread [" + Thread.currentThread().getName() + "] is running");
        // 4. 调用isDone()判断任务是否结束
        if (!futureTask.isDone()){
            System.out.println("Task is not done");
            Thread.sleep(2000);
        }


        int result = 0;
        try {
            //futureTask.run();
            // 5. 调用get()方法获取任务结果,如果任务没有执行完成则阻塞等待
            result = futureTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("result is " + result);


    }
    static class Task implements Callable<Integer>{

        @Override
        public Integer call() throws Exception {
            System.out.println("----->Thread [" + Thread.currentThread().getName() + "] is running");
            int result = 0;
            for(int i = 0; i < 100;++i) {
                result += i;
            }

            Thread.sleep(3000);
            return result;
        }
    }
}
