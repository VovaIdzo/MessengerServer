/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package messegerserver.classes;

/**
 *
 * @author vova
 */
public class Controller {
    private MServer server = null;
    private boolean isEnabled = false;
    private int socketServer = -1;
    private int socketAccept = -1;
    
    public Controller(int t,int p){
        socketServer = t;
        socketAccept = p;
    }
    
    private Controller(){}
    
    public void switchOn(){
        if(!this.isEnabled){
            server = new MServer(socketServer,socketAccept);
            server.start();
        }
    }
    
    public void switchOff(){
        if(this.isEnabled){
            //server.off();
        }
    }
    
    public void restart(){
        if(this.isEnabled){
            //server.off();
            //server = new ChatSerVer(socket);
            //server.go();
        }
    }
}
