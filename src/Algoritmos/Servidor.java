/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Algoritmos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.spec.SecretKeySpec;


public class Servidor extends UnicastRemoteObject implements Service {
    public Map<Integer, String[]> resultadosClientes = new HashMap<>();
    public Map<Integer, ClientCallback> clientes;
    private int contadorClientes;
    
    
    public Servidor() throws RemoteException {
        clientes = new HashMap<>();
        contadorClientes = 0;    
    }

    @Override
    public void registerClient(ClientCallback cliente) throws RemoteException {
    contadorClientes++;
    clientes.put(contadorClientes, cliente);
    
    // Imprimir el array de clientes
    System.out.println("Clientes registrados: " + Arrays.toString(clientes.keySet().toArray()));
    
    System.out.println("Cliente registrado: " + cliente.toString() + " - ID: " + contadorClientes);
    
}
    public void cifrarPorHilos(ArrayList<String> texto, String secretKey) throws RemoteException {
    System.out.println(clientes.size());

    int numClientes = clientes.size();
    int palabrasPorCliente = texto.size() / numClientes;
    int palabrasRestantes = texto.size() % numClientes;
    int inicio = 0;

    System.out.println("Num clientes: " + numClientes);
    System.out.println("Palabras por cliente: " + palabrasPorCliente);
    System.out.println("Palabras restantes: " + palabrasRestantes);

    for (int i = 0; i < numClientes; i++) {
        int fin = inicio + palabrasPorCliente + (i < palabrasRestantes ? 1 : 0);
        List<String> parteTextoList = texto.subList(inicio, fin);

        try {
            clientes.get(i + 1).receiveMessage(new ArrayList<>(parteTextoList), this,secretKey, i);
        } catch (InterruptedException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

        inicio = fin;
    }


    }

    @Override
public void sendResult(String[] resultado, int clientId) {
        resultadosClientes.put(clientId, resultado);

        // Verifica si todos los clientes han enviado sus resultados
        if (resultadosClientes.size() == clientes.size()) {
            ArrayList<String> finalTextList = new ArrayList<>();

            // Procesa los resultados en el orden correcto
            for (int i = 0; i <= clientes.size()-1; i++) {
                String[] resultadoCliente = resultadosClientes.get(i);
                // Agrega los elementos del resultado del cliente a la lista
                Collections.addAll(finalTextList, resultadoCliente);
                // Procesa el resultado como desees
                System.out.println("Resultado del Cliente " + i + ": " + Arrays.toString(resultadoCliente));
            }

            // Convierte la lista a un array
            String[] finalText = finalTextList.toArray(new String[0]);

            int count = 0;
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Dell\\Desktop\\PrubasArchivosGuardados\\Cifrado_RMI.txt", true));
                writer.write(finalTextList.toString() + " ");
                writer.close();

            
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
   
            
        }

}


   public Service conectar(String Ip, Servidor server){
               try {
            LocateRegistry.createRegistry(9000);

            Service service = server;


            java.rmi.Naming.rebind("rmi://" + Ip + ":9000/CifradoRMI", service);

            System.out.println("Servidor de RMI listo.");
            return service;

        } catch (Exception e) {
            e.printStackTrace();
        }
      return null;         
   }

}
