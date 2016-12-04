/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package messegerserver.classes;

import java.awt.BorderLayout;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Vovan
 */
public class ChatSerVer {
    private ArrayList clientStream;
    private int socketNumber = -1;
    private boolean isEnabled = true;
    private ArrayList<Thread> clientList = null;
    
    public ChatSerVer(int t){
        socketNumber = t;
    }
    
    
    private class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket socket;
        
        public ClientHandler(Socket clientSoket){
            try {
                socket = clientSoket;
                InputStreamReader read = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(read);
            }
            catch(Exception ex){ex.printStackTrace();}
        }
        
        public void run(){
            String message;
            try {
                if(checkUser(reader.readLine())){
                    PrintWriter t = (PrintWriter)clientStream.get(clientStream.size()-1);
                    t.print("true");
                    while ((message = reader.readLine()) != null){
                        System.out.println("read" + message);
                        tellEveryone(message);
                    }
                }else{
                    PrintWriter t = (PrintWriter)clientStream.get(clientStream.size()-1);
                    t.print("false");
                }
            }
            catch(Exception ex){ex.printStackTrace();}
        }
        
        private boolean checkUser(String user){
            if(checkFromBase(user)){
                return true;
            }else
                return false;
        }
        private boolean checkFromBase(String user){
            File sFile = new File("base.data");
            FileReader fileReader1;
            BufferedReader reader1;
            try {
                fileReader1 = new FileReader(sFile);
                reader1 = new BufferedReader(fileReader1);
                String line = null;
                while((line = reader1.readLine()) != null){
                    if(line.equalsIgnoreCase(user)){
                        return true;
                    }
                }
            } catch (FileNotFoundException ex){
                return false;
            } catch (IOException ex) {
                return false;
            } 
            return false;
        }
    }
    /**
     * @param args the command line arguments
     */
    
    public void go(){
        setParam();
        clientStream = new ArrayList();
         String message;
            try {
                ServerSocket servSoket = new ServerSocket(socketNumber);
                clientList = new ArrayList<Thread>();
                while(isEnabled){
                    Socket clientSocket = servSoket.accept();
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                    clientStream.add(writer);
                    
                    Thread t = new Thread(new ClientHandler(clientSocket));
                    t.start();
                    clientList.add(t);
                    System.out.println("got a connection");
                }
            }
            catch(Exception ex){ex.printStackTrace();}
    }
    
    private void setParam(){
        
    }
    
    private void tellEveryone(String message){
        Iterator it = clientStream.iterator();
        
        while(it.hasNext()){
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
                
            }
            catch(Exception ex){ex.printStackTrace();}
        }
    }
    public void off(){
        for(int i  = 0; i < clientList.size(); i++){
            clientList.get(i).destroy();
        }
        clientList.clear();
        
        isEnabled = false;
        for(int i = 0; i < clientStream.size(); i++){
            clientStream.clear();
        }
    }
    class LoginAccept implements Runnable{
        public void run(){
            //ServerSocket servSoket = new ServerSocket(socketNumber);
        }
    }
    
}
