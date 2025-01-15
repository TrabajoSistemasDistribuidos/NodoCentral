package com.proyecto.nodocentral;

import org.springframework.stereotype.Service;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class NodoCentralService {

    //Variables de configuración
    private static final int NUM_WORKERS = 4;
    private static final int NUMBERS_COUNT = 1000;
    private static final int NUMBERS_PER_WORKER = NUMBERS_COUNT / NUM_WORKERS;

    public void start() {
        try (ZContext context = new ZContext()) {

            //Creamos un socket PUSH para enviar los numeros aleatorios a los sockets PULL de los nodos trabajadores
            ZMQ.Socket ventilator = context.createSocket(ZMQ.PUSH);
            //Enlazamos el socket al puerto 5557 para enviar los números a los nodos trabajadores
            ventilator.bind("tcp://*:5557");

            //Creamos un socket PULL para recibir los resultados de los sockets PUSH de los nodos trabajadores
            ZMQ.Socket sink = context.createSocket(ZMQ.PULL);
            //Enlazamos el socket al puerto 5558 para recibir los resultados de los nodos trabajadores
            sink.bind("tcp://*:5558");

            //Asociamos la lista de numeros aleatorios a una variable
            List<Integer> numbers = generarNumerosAleatorios(NUMBERS_COUNT);

            //Creamos un pool de hilos para enviar los números a los nodos trabajadores en paralelo
            ExecutorService executorService = Executors.newFixedThreadPool(NUM_WORKERS);

            //Bucle para enviar los números a los nodos trabajadores
            for (int i = 0; i < NUM_WORKERS; i++) {
                final int workerId = i;
                executorService.submit(() -> {
                    System.out.println("Enviando numeros al NodoTrabajador" + (workerId + 1));
                    for (int j = 0; j < NUMBERS_PER_WORKER; j++) {
                        int number = numbers.get(workerId * NUMBERS_PER_WORKER + j);
                        ventilator.send(String.valueOf(number));
                    }
                });
            }

            //Cerramos el pool de hilos
            executorService.shutdown();

            //Contador de numeros primos
            int numeroPrimos = 0;

            //Recibimos los resultados de los nodos trabajadores y contamos los numeros primos
            for (int i = 0; i < NUMBERS_COUNT; i++) {
                String resultado = sink.recvStr();
                if (Boolean.parseBoolean(resultado)) {
                    numeroPrimos++;
                }
            }

            //Mostramos el total de numeros primos
            System.out.println("Total de números primos: " + numeroPrimos);
        }
    }

    //Metodo que genera una lista de numeros aleatorios
    private List<Integer> generarNumerosAleatorios(int count) {
        List<Integer> numbers = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            numbers.add(random.nextInt(10000));
        }
        return numbers;
    }
}