

import java.net.*;
import java.io.*;
import java.util.Scanner;



interface s{
    void connect(String a, String b);
    void confirm();
    void send(String a);
    String recieve();
    
    
}
abstract class sends implements s{
    
    public abstract void connect(String a, String b);
    
    public void confirm(String a, String b){};
    public abstract void send(String a);
    public abstract String recieve();
    
            
}

class client extends sends{
    private int port;
    private String ipaddress;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket s;

    public Socket getS() {
        return s;
    }

    public void setS(Socket s) {
        this.s = s;
    }
    
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }
    
    
     public client(String a, String b){
        
        setPort(Integer.parseInt(b));
        setIpaddress( a);
        System.out.println(b + " "+a);
        try{
        s = new Socket (ipaddress,port);
       in = new DataInputStream(s.getInputStream()); 
      out = new DataOutputStream(s.getOutputStream());
        
        }
        
        catch (Exception e){
            System.out.println("Error creating socket "+e.getMessage());
        }
        
        System.out.println("Socket created with input and outputsreams");
        
    
    };
   
    public void connect(String a, String b) {
        
        
    }

    @Override
    public void confirm() {
          //System.out.println("Socket created..at "+s.getInetAddress().toString() + s.getPort());
        
    }

    @Override
    public void send(String x) {
        try {
            out.writeUTF(x);
        } catch (IOException ex) {
            System.out.println("Failed to send");
        }
       
    }
    
    
    public String recieve (){
        String temp = null;
            try {
                temp = in.readUTF();
            } 
            catch (IOException ex) {
                System.out.println("Error reading data input "+ex.getMessage());
            }
        return temp;
        
    }
   

   
    
}
public class kuruvillaP1Sender {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        Scanner x = new Scanner (System.in);
        client c = new client(args[0],args[1]);

        boolean var = false;
      
        while(var ==false){
            var = authentication(c);
        };
        
        System.out.println("Exited authenticator");
        recieverlist(c,x);
        System.out.println("Starting transmission now");
        
        
        transmit(c,x);
        int choice = 1;
        System.out.println("Do you want to send any more strings? enter 1 to quit or 0 to continue");
       
        while(choice==1 ){
            choice =x.nextInt();
            switch (choice){
                case 0:{
                    System.out.println("tra");
                    transmit(c,x);
                    
                }
                case 1:{
                    
                    c.send("close");
                    
                try {
                    c.getS().close();
                } catch (IOException ex) {
                    System.out.println("Client socket failed to close");
                }
                System.out.println("closed");
                    
                }
                
            }
        }
        
        
        
        
        // TODO code application logic here
    }
    public static void transmit(client c,Scanner x){
        System.out.println("Please enter different type strings");
        String store = x.nextLine();
        int i =0;
        String temp;
        System.out.println("Do you have anymore strings to enter? Enter 0 for yes and 1 for no");
        while(i ==0){
            
                    i = x.nextInt();
            
            switch(i){
                case 0:
                { System.out.println("Please enter the remaining strings now");
                temp = x.nextLine();
                store.concat(temp);
                System.out.println("Do you have anymore strings to enter? Enter 0 for yes and 1 for no");
                    break;
                }
                case 1:
                {
                    System.out.println("Sending strings now!");
                    c.send(store);
                    break;
                    
                }
            }
            
        }
        System.out.println("The longest substring was "+c.recieve());
        
        
        
        
        
    }
    public static void recieverlist(client c, Scanner x){
        while(true){
        System.out.println("Please enter the name of the reciever:");
        String temp = x.nextLine();
        c.send(temp);//send
        
        
        String t =c.recieve();
        System.out.println(" t is "+t);
        System.out.println("temp recieved from server is "+t);
        
        if(t.equals("match")){
             System.out.println("connection established!");
            return;}
        else if(t.equals("mismatch")){
            System.out.println("reciever exists, but no connection has been established yet");
            System.out.println("Please enter the receiever name again to establish connection");
            c.send(temp = x.nextLine());
            return;
        }
        else{
            System.out.println("Username not found");
        }
        
       
        
        
        
    }}
    public static boolean authentication (client c){
        boolean xy = false;
        Scanner x = new Scanner (System.in);
        String confirm = "match";
        String temp = null;
        
        System.out.println("Please enter your username: ");
            String username = x.nextLine();
            c.send(username);
            temp = c.recieve();
            
            if(temp.equals(confirm)==false){
                System.out.println("No username found..please authenticate again");
                return xy;
            }
            
        System.out.println("Please enter your password: ");
            String password = x.nextLine();
            c.send(password);
            temp = c.recieve();
            System.out.println("temp is "+temp);
            
            
            
            if(temp.equals(confirm) == true){
                System.out.println("User authenticated.");
                xy = true;
                return xy;
            }
        return xy;    
                
            
               
             
    }
}
