package Algoritmos;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Concurrente {
    
    private static int NumeroHilos = 0;

    public static void main(String[] args) {
        try {
            String key = "ClaveSecreta1234"; // Clave de 128 bits
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");

            List<String> words = cargarPalabrasDesdeArchivo("src/ArchivosTXT/SPIDER-MAN.txt");

            int iteraciones = 1;
            // Iniciar el cronómetro
            long startTime = System.currentTimeMillis();

            // Cifrar el texto con el número de iteraciones deseado
            List<byte[]> ciphertexts = cifrarTextosConcurrente(words, secretKey, iteraciones);

            // Detener el cronómetro
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            // Mostrar los textos cifrados en formato hexadecimal
            for (byte[] ciphertext : ciphertexts) {
                System.out.println("Texto cifrado en hexadecimal: " + bytesToHex(ciphertext));

            }

            // Mostrar el tiempo de ejecución
            System.out.println("\nCIFRADO CONCURRENTE...");
            System.out.println("Tiempo de ejecución: " + duration + " milisegundos");
            System.out.println("Total de palabras en el texto: " + words.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static int getNumeroHilos(){
        return NumeroHilos;
    }

    public static List<String> cargarPalabrasDesdeArchivo(String rutaArchivo) {
        List<String> palabras = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                // Dividir la línea en palabras utilizando espacios en blanco como separadores
                String[] palabrasEnLinea = linea.split("\\s+");
                palabras.addAll(Arrays.asList(palabrasEnLinea));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return palabras;
    }

    public static List<byte[]> cifrarTextosConcurrente(List<String> words, SecretKeySpec secretKey, int iteraciones) throws InterruptedException {

        int numThreads = Runtime.getRuntime().availableProcessors();//Agarra la cantidad de subprocesadores que tengo en la computadora
//        int numThreads = 32;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);//ThreadPool
        NumeroHilos = numThreads;

        List<Future<byte[]>> futures = new ArrayList<>();

        for (String word : words) {
            futures.add(executor.submit(() -> cifrarTextoConIteracionesConcurrente(word, secretKey, iteraciones)));//Usamos lambdas
        }

        List<byte[]> ciphertexts = new ArrayList<>();

        for (Future<byte[]> future : futures) {
            try {
                byte[] result = future.get();
                ciphertexts.add(result);
                System.out.println("Texto cifrado en hexadecimal: " + bytesToHex(result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Numero de hilos: " + numThreads);

        executor.shutdown();//Apaga a los hilos cuando terminan completamente su tarea
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        return ciphertexts;
    }

    public static List<String> descifrarTextosConcurrente(List<byte[]> ciphertexts, SecretKeySpec secretKey) throws InterruptedException {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        List<Future<String>> futures = new ArrayList<>();

        for (byte[] ciphertext : ciphertexts) {
            futures.add(executor.submit(() -> descifrarTextoConIteracionesConcurrente(ciphertext, secretKey)));
        }

        List<String> decryptedTexts = new ArrayList<>();

        for (Future<String> future : futures) {
            try {
                String result = future.get();
                decryptedTexts.add(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        return decryptedTexts;
    }

    public static byte[] cifrarTextoConIteracionesConcurrente(String texto, SecretKeySpec secretKey, int iteraciones) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] plaintextBytes = texto.getBytes();

            for (int i = 0; i < iteraciones; i++) {
                plaintextBytes = cipher.doFinal(plaintextBytes); // Cifrar con resultado intermedio
            }

            return plaintextBytes; // Devuelve el resultado intermedio después de las iteraciones
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String descifrarTextoConIteracionesConcurrente(byte[] ciphertext, SecretKeySpec secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(ciphertext);

            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }
}
