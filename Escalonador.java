import java.util.concurrent.*;

public class Escalonador {
    public static void main(String[] args) {
        ArquivoEntrada arquivoEntrada = new ArquivoEntrada();
        arquivoEntrada.escreverConteudo();

        CPU cpu = new CPU(arquivoEntrada.getProcessos());

        ExecutorService executorService = Executors.newFixedThreadPool(arquivoEntrada.getProcessos().size());

        arquivoEntrada.getProcessos().forEach(processo ->{
            executorService.execute(new ThreadPrio(String.format("THREAD-%d",processo.pid), cpu));
        });

        executorService.shutdown();

        try{
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("Escalonamento concluído.");
    }
}