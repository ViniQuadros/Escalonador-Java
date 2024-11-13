import java.util.List;

public class Escalonador {
    public static void main(String[] args) {
        ArquivoEntrada arquivoEntrada = new ArquivoEntrada();
        arquivoEntrada.escreverConteudo();
        List<ArquivoEntrada.Processo> processos = arquivoEntrada.getProcessos();

        ThreadPrio escalonador = new ThreadPrio(processos);
        Thread myThread = new Thread(escalonador);

        myThread.start();
    }
}

