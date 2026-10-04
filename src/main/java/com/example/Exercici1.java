package com.example;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Exercici1 {
    // Crea una classe Java amb un mètode main.
    public static void main(String[] args) {

        // Definir el parking
        ParkingLot parking = new ParkingLot(2);



        // Defineix tasques (Runnable) que simulin l'entrada i sortida de cotxes a l'aparcament. 
        // Quan un cotxe entra, adquireix un permís, i quan surt, allibera un permís.
        Runnable cotxe = () -> {
            parking.entrar();

            try {
                Random random = new Random();
                int temps = random.nextInt(6000 - 2000 + 1) + 2000; // Num azar entre 2000 y 600

                System.out.println("El cotxe estara dins del parking " + TimeUnit.MILLISECONDS.toSeconds(temps) + " segons");
                System.out.println("");

                Thread.sleep(temps);
                
            } catch (InterruptedException e) {
                System.out.println("[cotxe] El fil del cotxe ha estat interromput.");
            }

            parking.sortir();
        };



        // Utilitza un ExecutorService amb un pool de fils per simular l'entrada i sortida de cotxes de manera concurrent.
        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.submit(cotxe);
        executor.submit(cotxe);
        executor.submit(cotxe);
        executor.submit(cotxe);
        executor.submit(cotxe);



        // Assegura't de tancar l'executor al final.
        executor.shutdown();
    }
}



// Defineix una classe ParkingLot que tingui un semàfor amb un nombre limitat de permisos (igual a la capacitat de l'aparcament).
class ParkingLot{
    private Semaphore semaphore;

    public ParkingLot(int capacitat) {
        semaphore = new Semaphore(capacitat);
    }

    public void entrar() {
        try {

            if (!semaphore.tryAcquire()){
                // Mostra a la consola cada vegada que un cotxe entra o surt de l'aparcament i 
                // quan un cotxe espera perquè l'aparcament està ple.
                System.out.println("El cotxe espera perque l'aparcament esta ple");
                System.out.println("");

                semaphore.acquire();
            }

            // Mostra a la consola cada vegada que un cotxe entra o surt de l'aparcament i 
            // quan un cotxe espera perquè l'aparcament està ple.
            System.out.println("Un cotxe ha entrat a l'aparcament");
            System.out.println("Places disponibles: " + semaphore.availablePermits());
            System.out.println("");

        } catch (InterruptedException e) {
            System.out.println("[ParkingLot.entrar] El fil que esperava entrar ha estat interromput.");
        }
    }

    public void sortir() {
        semaphore.release();
        
        // Mostra a la consola cada vegada que un cotxe entra o surt de l'aparcament i 
        // quan un cotxe espera perquè l'aparcament està ple.
        System.out.println("Un cotxe ha sortit de l'aparcament");
        System.out.println("Places disponibles: " + semaphore.availablePermits());
        System.out.println("");
    }
}