public class ThreadPrio implements Runnable{
    private String threadName;

    public ThreadPrio(String threadName)
    {
        this.threadName = threadName;
    }

    @Override
    public void run()
    {
        System.out.println(this.threadName + " começou...");
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(this.threadName + " finalizou...");
    }
}