public class ThreadPrio implements Runnable{
    private String threadName;
    private CPU cpu;

    public ThreadPrio(String threadName, CPU cpu)
    {
        this.threadName = threadName;
        this.cpu = cpu;
    }

    @Override
    public synchronized void run()
    {
        System.out.println(this.threadName + " começou...");
        try {
            cpu.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(this.threadName + " finalizou...");
    }
}