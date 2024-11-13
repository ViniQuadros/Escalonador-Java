import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArquivoEntrada {
    private final String nomeArquivo = "entrada.txt";
    private List<ArquivoEntrada.Processo> processos;

    public ArquivoEntrada() {
        processos = realizarLeitura();
    }

    public void escreverConteudo() {
        processos.forEach(processo -> System.out.println(processo.toString()));
    }

    public List<ArquivoEntrada.Processo> getProcessos(){
        return processos;
    }

    private List<ArquivoEntrada.Processo> realizarLeitura() {
        List<ArquivoEntrada.Processo> resultado = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                resultado.add(new ArquivoEntrada.Processo(linha));
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        return resultado;
    }

    public class Processo {
        protected int pid;
        protected int tempoChegada;
        protected int burst;
        protected int prioridade;

        private Processo(String linhaArquivoEntrada) {
            String[] linhaRepartida = linhaArquivoEntrada.split(";");

            this.pid = Integer.parseInt(linhaRepartida[0]);
            this.tempoChegada = Integer.parseInt(linhaRepartida[1]);
            this.burst = Integer.parseInt(linhaRepartida[2]);
            this.prioridade = Integer.parseInt(linhaRepartida[3]);
        }

        @Override
        public String toString() {
            return String.format("PID=%d|TEMPO DE CHEGADA=%d|BURST=%s|PRIORIDADE=%d", pid, tempoChegada, burst,
                    prioridade);
        }
    }

}