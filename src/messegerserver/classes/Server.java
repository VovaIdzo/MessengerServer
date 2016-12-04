/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package messegerserver.classes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vova
 */


public class Server implements Runnable{
    private ArrayList<Socket> clientList = new ArrayList<Socket>();
    private ServerSocket servSok = new ServerSocket(6868);
    private int servSocket = 6868;    
    
    public void run(){
        while(true){
                try{
                    Socket socket = listen();
                    clientList.add(socket);
                }catch(Exception e){}
            }
        }
        
        public Server() throws IOException{
            
        }
        
        public Socket listen() throws IOException{
            while(true){
                return servSok.accept();
            }
        }
}
