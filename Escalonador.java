import java.util.concurrent.*;

public class Escalonador {
    public static void main(String[] args) {
        ArquivoEntrada arquivoEntrada = new ArquivoEntrada();
        arquivoEntrada.escreverConteudo();

        ExecutorService executorService = Executors.newFixedThreadPool(arquivoEntrada.getProcessos().size());

        arquivoEntrada.getProcessos().forEach(processo ->{
            executorService.execute(new ThreadPrio(String.format("THREAD-%d",processo.pid)));
        });
    }
}
