/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Algoritmos;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.crypto.spec.SecretKeySpec;

public interface Service extends Remote {
    void cifrarPorHilos(ArrayList<String> texto, String secretKey) throws RemoteException;
    void registerClient(ClientCallback client) throws RemoteException;    
    void sendResult(String[] message, int identifier)throws RemoteException;
}

