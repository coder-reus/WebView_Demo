package android_h5_proj.zhiyi.com.android_h5_proj;

/**
 * Created by Jacen on 2016/9/6.
 */
public class Runnable_demo implements Runnable {
    private int ticket = 10;

    public Runnable_demo() {

    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            if (this.ticket > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.sale();
            }
        }
    }

    public synchronized void sale() {
        if (this.ticket>0){
            System.out.println(Thread.currentThread().getName() + "号窗口卖出了：" + this.ticket-- + "号票");
        }
    }

    public static void main(String args[]) {
        Runnable_demo demo = new Runnable_demo();
        new Thread(demo, "a").start();
        new Thread(demo, "b").start();
        new Thread(demo, "c").start();
    }
}
