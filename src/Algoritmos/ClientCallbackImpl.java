/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Algoritmos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.crypto.Cipher;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.spec.SecretKeySpec;

public class ClientCallbackImpl extends UnicastRemoteObject implements ClientCallback {
    protected ClientCallbackImpl() throws RemoteException {
    }

    @Override
public void receiveMessage(ArrayList<String> message, Service service, String secretKey, int identifier) throws RemoteException, InterruptedException {                Concurrente concurrent;
        System.out.println("Texto recibido: " +message);
        
        List<byte[]> cifradoList = Concurrente.cifrarTextosConcurrente(message, new SecretKeySpec(secretKey.getBytes(), "AES"), identifier);
        List<String> textoCifradoList = new ArrayList<>();
for (byte[] byteArray : cifradoList) {
    String text = new String(byteArray, StandardCharsets.UTF_8);
    textoCifradoList.add(text);
}

// Convert List<String> to String[]
String[] textoCifrado = textoCifradoList.toArray(new String[0]);
        System.out.println(Arrays.toString(textoCifrado));
        service.sendResult(textoCifrado, identifier);

        }
}
