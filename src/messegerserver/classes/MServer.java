/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package messegerserver.classes;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asus
 */
public class MServer {
    private ArrayList<OutputStream> clientStream;
    private int socketServer = -1;
    private int socketAccept = -1;
    private boolean isEnabled = true;
    private ArrayList<Socket> clientList = null;
    
    public MServer(int t,int p){
        socketServer = t;
        socketAccept = p;
    }
    
    public void start(){
        clientStream = new ArrayList<OutputStream>();
        listenUserLogin();
        connectUsers();
    }
    
    private void listenUserLogin(){
        Thread acp = new Thread(new UserAccept());
        acp.start();
    }
    
    private void connectUsers(){
        String message;
            try {
                ServerSocket servSoket = new ServerSocket(socketServer);
                clientList = new ArrayList<Socket>();
                while(isEnabled){
                    Socket clientSocket = servSoket.accept();
                    OutputStream writer = clientSocket.getOutputStream();
                    clientStream.add(writer);
                    
                    Thread t = new Thread(new MServer.ClientHandler(clientSocket));
                    t.start();
                    clientList.add(clientSocket);
                    System.out.println("got a connection");
                }
            }
            catch(Exception ex){ex.printStackTrace();}
    }
    
    //клас для провірки юзера в базі
    class UserAccept implements Runnable{
        
        @Override
        public void run(){
            try {
                ServerSocket servSoket = new ServerSocket(socketAccept);
                while(isEnabled){
                    Socket clientSocket = servSoket.accept();
                    new Thread(new ServerServis(clientSocket)).start();
                }
            }catch(Exception ex){ex.printStackTrace();}
        }
        
        class ServerServis implements Runnable{
            private final String REGISTRATION = "2";
            private final String CHECK_USER = "1";
            
            private Socket clientSocket = null;
            private PrintWriter writer = null;
            private BufferedReader reader = null;
            
            public ServerServis(Socket s){
                clientSocket = s;
            }
            
            public void run(){
                try {
                    writer = new PrintWriter(clientSocket.getOutputStream());
                    //InputStreamReader read = ;// потік виведення
                    reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String command = null;
                    command = reader.readLine();
                    switch(command){
                        case REGISTRATION:
                            registration();
                            break;
                        case CHECK_USER: 
                            checkUser();
                            break;
                    }
                    
                    
                } catch (IOException ex) {} finally {}
            }
            
            public void checkUser() throws IOException{
                String login;
                while((login = reader.readLine())!=null){
                    boolean isUser = UserBase.checkUser(login);
                    if(isUser){
                        writer.println("true");
                        writer.flush();
                        break;
                    }else{
                        writer.println("false");
                        writer.flush();
                        break;
                    }
                }
            }
            
            private void registration(){
                try {
                    String login;
                    login = reader.readLine();
                    boolean isUser = UserBase.checkUser(login);
                    if(isUser){
                        writer.println("false");
                        writer.flush();
                    }else{
                        UserBase.add(login);
                        writer.println("true");
                        writer.flush();
                    }
                } catch (IOException ex) {}
                
            }
        }
    }
    
    private class ClientHandler implements Runnable {
        InputStream reader;
        Socket socket;
        
        public ClientHandler(Socket clientSoket){
            try {
                socket = clientSoket;
                reader = socket.getInputStream();
            }
            catch(Exception ex){ex.printStackTrace();}
        }
        
        public void run(){
            try {
                int count;
                byte[] buffer = new byte[8192]; // or 4096, or more
                while ((count = reader.read(buffer)) > 0)
                {
                    tellEveryone(buffer, 0, count);
                }
            }
            catch(Exception ex){ex.printStackTrace();}
        }
        
        private void tellEveryone(byte[] buffer, int offset, int count){
            Iterator it = clientStream.iterator();
            while(it.hasNext()){
                try {
                    OutputStream writer = (OutputStream) it.next();
                    writer.write(buffer, offset, count);
                    writer.flush();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                    new String();
                    new String();
                }
            }
        }
        
    }
}
