package com.example;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Exercici0 {
    //Crea una classe Java amb un mètode main.
    public static void main(String[] args) {
        
        // Variables
        double[] dades = {89, 134, 13, 27, 65};
        ConcurrentHashMap<String, Double> resultats = new ConcurrentHashMap<>();



        // Utilitza una CyclicBarrier per sincronitzar les tres tasques,
        // de manera que només es mostrin els resultats finals quan totes hagin acabat.
        CyclicBarrier barrera = new CyclicBarrier(3);



        // Defineix tres tasques (Runnable) que facin els càlculs de la mitjana, 
        // la suma i la desviació estàndard d'un conjunt de dades.

        // Tasca 1: Mitjana
        Runnable mitjanaOperacio = () -> {
            double sumaMitjana = 0;
            for (int i = 0; i < dades.length; i++){
                sumaMitjana += dades[i];
            }

            double mitjana = sumaMitjana / dades.length;

            resultats.put("mitjana", mitjana);

            try {
                barrera.await();
            } catch (InterruptedException e) {
                System.out.println("[mitjanaOperacio] El fil que esperava ha estat interromput.");
            } catch (BrokenBarrierException e) {
                System.out.println("[mitjanaOperacio] La barrera s'ha trencat abans que tots els fils hi arribessin.");
            }
        };

        // Tasca 2: Suma
        Runnable sumaOperacio = () -> {
            double suma = 0;
            for (int i = 0; i < dades.length; i++){
                suma += dades[i];
            }

            resultats.put("suma", suma);

            try {
                barrera.await();
            } catch (InterruptedException e) {
                System.out.println("[sumaOperacio] El fil que esperava ha estat interromput.");
            } catch (BrokenBarrierException e) {
                System.out.println("[sumaOperacio] La barrera s'ha trencat abans que tots els fils hi arribessin.");
            }
        };

        // Tasca 3: Derivacio estandard
        Runnable derivacioOperacio = () -> {
            double sumaMitjana = 0;
            for (int i = 0; i < dades.length; i++){
                sumaMitjana += dades[i];
            }

            double mitjana = sumaMitjana / dades.length;

            double sumaQuadrats = 0;
            for (int i = 0; i < dades.length; i++){
                double diferencia = dades[i] - mitjana;
                sumaQuadrats += diferencia * diferencia;
            }

            double divisioQuadrats = sumaQuadrats / dades.length;
            double derivacio = Math.sqrt(divisioQuadrats);

            resultats.put("derivacio", derivacio);

            try {
                barrera.await();
            } catch (InterruptedException e) {
                System.out.println("[derivacioOperacio] El fil que esperava ha estat interromput.");
            } catch (BrokenBarrierException e) {
                System.out.println("[derivacioOperacio] La barrera s'ha trencat abans que tots els fils hi arribessin.");
            }
        };



        // Utilitza un ExecutorService amb un pool de fils per executar les tasques en paral·lel.
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(mitjanaOperacio);
        executor.submit(sumaOperacio);
        executor.submit(derivacioOperacio);
        


        // Quan tots els càlculs hagin completat el seu treball, mostra els resultats finals a la consola.
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("[executor] El fil principal ha estat interromput mentre esperava.");
        }
        System.out.println("Mitjana: " + resultats.get("mitjana"));
        System.out.println("Suma: " + resultats.get("suma"));
        System.out.println("Derivacio estandard: " + resultats.get("derivacio"));



        // Assegura't de tancar l'executor al final.
        executor.shutdown();
    }
}