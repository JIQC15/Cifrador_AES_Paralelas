/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Algoritmos;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.crypto.spec.SecretKeySpec;

public interface ClientCallback extends Remote {
    void receiveMessage(ArrayList<String> message, Service servidor, String secretKey, int identifier) throws RemoteException, InterruptedException;
}

