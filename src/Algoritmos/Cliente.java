/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Algoritmos;

import java.rmi.Naming;
import java.util.Scanner;

public class Cliente {
    
        public void connect(String ip, Service service) {
        try {
            // Busca el objeto remoto

            service = (Service) Naming.lookup("rmi://" + ip + ":9000/CifradoRMI");

            ClientCallbackImpl clientCallback = new ClientCallbackImpl();
            service.registerClient(clientCallback);
            System.out.println("Conectao");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

