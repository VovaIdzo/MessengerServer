/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package messegerserver.classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asus
 */
public class UserBase {
    
        private UserBase(){

        }
        
        public static boolean checkUser(String user){
            if(checkFromBase(user)){
                return true;
            }else
                return false;
            
        }
        
        public static void add(String log){
            File sFile = new File("base.data");
            try {
                FileWriter f = new FileWriter(sFile,true);
                //BufferedWriter f = new BufferedWriter(fileWriter);
                f.write(log + "\n");
                f.close();
            } catch (IOException ex) {}
        }
        
        
        
        
        
        
        
        
        
        //------------------------------------------------------------------------
        private static boolean checkFromBase(String user){
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
