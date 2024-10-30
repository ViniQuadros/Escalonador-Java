import java.util.concurrent.*;;

public class Escalonador {
    public static void main(String[] args) {
        int poolSize = 2;

        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

        executorService.execute(new ThreadPrio("MyThread"));
    }
}
