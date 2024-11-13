import java.util.*;

class ThreadPrio implements Runnable {
    private List<ArquivoEntrada.Processo> processos;
    private PriorityQueue<ArquivoEntrada.Processo> filaProntos;

    public ThreadPrio(List<ArquivoEntrada.Processo> processos) {
        // Criar a fila de prioridade baseada na prioridade (menor valor = maior
        // prioridade)
        this.filaProntos = new PriorityQueue<>(Comparator
                .comparingInt((ArquivoEntrada.Processo p) -> p.prioridade)
                .thenComparingInt(p -> p.tempoChegada)); // Se as prioridades forem iguais, ordenar por tempo de chegada
        this.processos = processos;
    }

    @Override
    public synchronized void run() {
        System.out.println("Escalonador iniciou...");

        long tempoAtual = 0; // Controla o tempo de execução
        List<ArquivoEntrada.Processo> filaProcessos = new ArrayList<>(); // Fila de prontos
        List<String> eventos = new ArrayList<>(); // Lista para armazenar os eventos de execução

        // Adiciona os processos na fila inicial
        filaProcessos.addAll(processos);
        filaProcessos.sort(Comparator.comparingInt(p -> p.tempoChegada)); // Organiza pela chegada

        // Processos prontos para execução
        Queue<ArquivoEntrada.Processo> filaProntos = new PriorityQueue<>(Comparator.comparingInt(p -> p.prioridade));

        while (!filaProcessos.isEmpty() || !filaProntos.isEmpty()) {
            // Adiciona os processos que chegaram no tempo atual
            while (!filaProcessos.isEmpty() && filaProcessos.get(0).tempoChegada <= tempoAtual) {
                filaProntos.add(filaProcessos.remove(0));
            }

            // Se a fila de prontos não estiver vazia, executa o processo de maior
            // prioridade
            if (!filaProntos.isEmpty()) {
                ArquivoEntrada.Processo processoAtual = filaProntos.poll();

                // Executa o processo conforme o tempo de burst
                while (processoAtual.burst > 0) {
                    // Adiciona o evento de execução
                    eventos.add(String.format("Tempo %d ms: Executando processo PID=%d (Prioridade=%d, Burst=%d)",
                            tempoAtual, processoAtual.pid, processoAtual.prioridade, processoAtual.burst));

                    // Diminui o burst
                    processoAtual.burst--;
                    tempoAtual++; // Avança o tempo

                    // Preempção: Verifica se há algum processo com prioridade maior
                    for (ArquivoEntrada.Processo processo : filaProntos) {
                        if (processo.prioridade < processoAtual.prioridade) {
                            // Se um processo de maior prioridade chegar, preempta o processo atual
                            eventos.add(String.format(
                                    "Tempo %d ms: Troca de tarefa! Preemptando o processo PID=%d para executar PID=%d",
                                    tempoAtual, processoAtual.pid, processo.pid));
                            filaProntos.add(processoAtual); // Coloca o processo atual de volta na fila
                            processoAtual = processo; // Troca para o novo processo
                            filaProntos.remove(processo); // Remove o processo de maior prioridade da fila
                            break;
                        }
                    }

                    try {
                        Thread.sleep(1); // Simula a execução do processo
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Após o processo terminar, exibe sua finalização
                eventos.add(String.format("Tempo %d ms: Processo PID=%d finalizado", tempoAtual, processoAtual.pid));
            }

            // Se não houver mais processos para executar, apenas avança o tempo
            if (filaProntos.isEmpty() && !filaProcessos.isEmpty()) {
                tempoAtual = filaProcessos.get(0).tempoChegada;
            }
        }

        // Ordena os eventos por tempo (mesmo que haja repetições de tempo)
        eventos.sort(Comparator.comparingInt(event -> Integer.parseInt(event.split(" ")[1].replace("ms:", ""))));

        // Imprime os eventos ordenados
        for (String evento : eventos) {
            System.out.println(evento);
        }

        System.out.println("Escalonador finalizou.");
    }

}