import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Escalonador {

    private static int verificarPidQueDeveSerProcessado(List<ArquivoEntrada.Processo> processos,
            int tempoDecorrido) {

        List<ArquivoEntrada.Processo> processosOrdenadosPorPrioridade = new ArrayList<>(processos);
        processosOrdenadosPorPrioridade.sort((p1, p2) -> Integer.compare(p1.getPrioridade(), p2.getPrioridade()));
        for (int i = 0; i < processosOrdenadosPorPrioridade.size(); i++) {
            ArquivoEntrada.Processo processo = processosOrdenadosPorPrioridade.get(i);

            if ((processo.getTempoChegada() > tempoDecorrido)
                    || (processo.getTempoExecutado() >= processo.getBurst())) {
                continue;
            } else {
                return processo.getPid();
            }
        }

        return -1;
    }

    private static void calcularTME(List<ArquivoEntrada.Processo> processos) {
        System.out.println("\nTEMPOS DE ESPERA:\n");
        processos.forEach(processo -> System.out.println(String.format("PID=%d,TEMPO_ESPERA=%d", processo.getPid(),processo.getTempoEspera())));
        float tme = processos.stream().map(processo -> processo.getTempoEspera()).reduce(0, Integer::sum)/processos.size();
        System.out.println("TME = " + tme);
    }

    private static void simularPreemptivoComPrioridade(List<ArquivoEntrada.Processo> processos) {
        int tempoDecorrido = 0;
        ArquivoEntrada.Processo ultimoProcessoExecutado = null;
        int tempoChegadaPrimeiroProcesso = processos.get(0).getTempoChegada();

        while (true) {
            int pidProcessoEmExecucao = verificarPidQueDeveSerProcessado(processos, tempoDecorrido);

            if (pidProcessoEmExecucao == -1 && tempoDecorrido > tempoChegadaPrimeiroProcesso) {
                break;
            } else if (pidProcessoEmExecucao != -1) {
                ArquivoEntrada.Processo processoEmExecucao = processos.stream()
                        .filter(processo -> processo.getPid() == pidProcessoEmExecucao)
                        .findFirst()
                        .orElse(null);

                if (tempoDecorrido == tempoChegadaPrimeiroProcesso) {
                    System.out.println(String.format(
                            "\n\nO ESCALONADOR FOI INICIADO\n\nTEMPO: %d\nPROCESSO_NOVO   => PID=%d|BURST=%d|PRIORIDADE=%d",
                            tempoDecorrido,
                            processoEmExecucao.getPid(),
                            processoEmExecucao.getBurst(),
                            processoEmExecucao.getPrioridade()));
                }

                if (processoEmExecucao.getTempoExecutado() < processoEmExecucao.getBurst()) {
                    processoEmExecucao.setTempoExecutado(processoEmExecucao.getTempoExecutado() + 1);
                }

                if (Objects.nonNull(ultimoProcessoExecutado)
                        && (ultimoProcessoExecutado.getPid() != processoEmExecucao.getPid())) {
                            int tempoRestanteUltimoProcessoExecutado = ultimoProcessoExecutado.getBurst() - ultimoProcessoExecutado.getTempoExecutado();
                    System.out.println(String.format(
                            "\nTEMPO: %d\nPROCESSO_ANTIGO => PID=%d|BURST=%d|TEMPO_RESTANTE=%d\nPROCESSO_NOVO   => PID=%d|BURST=%d|PRIORIDADE=%d",
                            tempoDecorrido,
                            ultimoProcessoExecutado.getPid(),
                            ultimoProcessoExecutado.getBurst(),
                            tempoRestanteUltimoProcessoExecutado,
                            processoEmExecucao.getPid(),
                            processoEmExecucao.getBurst(),
                            processoEmExecucao.getPrioridade()));
                    processoEmExecucao.setTempoEspera(processoEmExecucao.getTempoEspera() + (tempoDecorrido - processoEmExecucao.getTempoChegada() - processoEmExecucao.getTempoEmQueFoiParado() + 1));
                    ultimoProcessoExecutado.setTeEmQueFoiParado(tempoDecorrido);
                }

                tempoDecorrido++;
                ultimoProcessoExecutado = processoEmExecucao;
            } else {
                tempoDecorrido++;
            }
        }

        System.out.println("\n\nO ESCALONADOR FOI FINALIZADO\n");
    }

    public static void main(String[] args) {
        ArquivoEntrada arquivoEntrada = new ArquivoEntrada();
        arquivoEntrada.escreverConteudo();
        List<ArquivoEntrada.Processo> processos = arquivoEntrada.getProcessos();

        processos.sort((p1, p2) -> Integer.compare(p1.getTempoChegada(), p2.getTempoChegada()));

        simularPreemptivoComPrioridade(processos);
        calcularTME(processos);
    }
}
