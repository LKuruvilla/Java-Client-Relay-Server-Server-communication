


import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
interface realserver{
    
    void send(String a);
    String recieve();
    
    
}
class server implements realserver{
    private ServerSocket s = null;
    private int port;
    private Socket rs_sock = null;
    private DataInputStream in;
    private DataOutputStream out;
    

    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    
    public ServerSocket getS() {
        return s;
    }
    public void setS(ServerSocket s) {
        this.s = s;
    }

    public Socket getRs_sock() {
        return rs_sock;
    }
    public void setRs_sock(Socket rs_sock) {
        this.rs_sock = rs_sock;
    }
    
    
    public server(String a) {
        setPort(Integer.parseInt(a));
        try {
            s = new ServerSocket(getPort());
            System.out.println("Server is running");
            rs_sock = s.accept();
            System.out.println("Connection accepted");
            
            
        } catch (IOException ex) {
            System.out.println("Failed to set up server socket. "+ex.getMessage());
        }
        
        try {
            in = new DataInputStream(rs_sock.getInputStream());
            out = new DataOutputStream(rs_sock.getOutputStream());
        } catch (IOException ex) {
            System.out.println("Failed to set up input and output streams "+ ex.getMessage());
        }
        
        
        
        
    }

    @Override
    public void send(String a) {
        try {
            out.writeUTF(a);
        } catch (IOException ex) {
            System.out.println("Failed to send message "+ ex.getMessage());
        }
        
    }

    @Override
    public String recieve() {
        String temp = null;
        try {
            temp = in.readUTF();
        } catch (IOException ex) {
            System.out.println("Failed to recieve message. "+ex.getMessage());
        }
        return temp;
    }

    
    
}


public class kuruvillaP1Receiver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        server s = new server (args[0]);
        System.out.println("Strating transmission now");
        
        String val = "open";
        String close = "close";
        while(val.equals(close) ==false){
        String str = transmit(s);
        s.send(str);
        val = s.recieve();
        }
        System.out.println("closing now");
        try {
            s.getS().close();
        } catch (IOException ex) {
            System.out.println("Relay socket failed to close");
        }
        // TODO code application logic here
    }
    
    public static String transmit (server s){
        
        String temp = s.recieve();
        System.out.println("Transmission: "+temp);
        
        
        String x = process(temp);
        return x;
    }
    
    public static String process(String s){
        System.out.println("Processing the string "+s+" now");
        String[] store = s.split(" ");
        
        String currenttemp = null;
       
        for (int i =0; i<store.length; i++){
            if(i==0){
                currenttemp = lcg(store[i],store[1]);
            }
            currenttemp = lcg(store[i],currenttemp);
        }
       return currenttemp;
       
        
      
        
        
    }
    
    public static String lcg(String S1, String S2){
    int Start = 0;
    int Max = 0;
    for (int i = 0; i < S1.length(); i++)
    {
        for (int j = 0; j < S2.length(); j++)
        {
            int x = 0;
            while (S1.charAt(i + x) == S2.charAt(j + x))
            {
                x++;
                if (((i + x) >= S1.length()) || ((j + x) >= S2.length())) break;
            }
            if (x > Max)
            {
                Max = x;
                Start = i;
            }
         }
    }
    return S1.substring(Start, (Start + Max));
}
}
