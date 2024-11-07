import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class CPU {
    private PriorityQueue<ArquivoEntrada.Processo> filaProntos;

    public CPU(List<ArquivoEntrada.Processo> processos) {
        filaProntos = new PriorityQueue<>(Comparator
                .comparingInt((ArquivoEntrada.Processo p) -> p.prioridade)
                .thenComparingInt(p -> p.tempoChegada));

        filaProntos.addAll(processos);
    }

    public void execute() {
        int tempoAtual = 0;
        //System.out.println("Iniciando escalonamento preemptivo de prioridade...");

        while (!filaProntos.isEmpty()) {
            ArquivoEntrada.Processo processoAtual = filaProntos.poll();

            if (processoAtual.tempoChegada > tempoAtual) {
                tempoAtual = processoAtual.tempoChegada;
            }

            System.out.printf("Tempo %d: Executando processo PID=%d (Prioridade=%d, Burst=%d)\n",
                    tempoAtual, processoAtual.pid, processoAtual.prioridade, processoAtual.burst);

            while (processoAtual.burst > 0) {
                processoAtual.burst--;
                tempoAtual++;

                for (ArquivoEntrada.Processo processo : filaProntos) {
                    if (processo.tempoChegada <= tempoAtual && processo.prioridade < processoAtual.prioridade) {
                        System.out.printf("Tempo %d: Preempção! Parando o processo PID=%d para executar o PID=%d\n",
                                tempoAtual, processoAtual.pid, processo.pid);
                        filaProntos.add(processoAtual);
                        processoAtual = processo; 
                        filaProntos.remove(processo);
                        break;
                    }
                }
            }

            System.out.printf("Tempo %d: Processo PID=%d finalizado\n", tempoAtual, processoAtual.pid);
        }

        System.out.println("Escalonamento concluído.");
    }
}