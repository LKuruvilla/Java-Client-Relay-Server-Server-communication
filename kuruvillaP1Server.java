


import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;



interface rs{
    void connect(String a, String b);
    void confirm();
    void sendtoclient(String a);
    String recievefromclient();
    
    
}
abstract class r_s implements rs{
    
    public abstract void connect(String a, String b);
    
    public void confirm(String a, String b){};
    public abstract void sendtoclient(String a);
    public abstract String recievefromclient();
    
            
}

class rserver extends r_s{
    private int clientport;
    private String clientipaddress;
    private DataInputStream in_toClient;
    private DataOutputStream out_toClient;
    private Socket clientsock;
    private ServerSocket ss;
    
    
    private DataInputStream in_toServer;
    private DataOutputStream out_toServer;
    private Socket serversock;
    private int serverport;
    private String serveripaddress;

    public Socket getServersock() {
        return serversock;
    }

    public void setServersock(Socket serversock) {
        this.serversock = serversock;
    }

    public int getServerport() {
        return serverport;
    }

    public void setServerport(int serverport) {
        this.serverport = serverport;
    }

    public String getServeripaddress() {
        return serveripaddress;
    }

    public void setServeripaddress(String serveripaddress) {
        this.serveripaddress = serveripaddress;
    }
    
    

    public Socket getClientsock() {
        return clientsock;
    }

    public void setClientsock(Socket clientsock) {
        this.clientsock = clientsock;
    }
    
    public int getClientport() {
        return clientport;
    }

    public void setClientport(int clientport) {
        this.clientport = clientport;
    }

    public String getClientipaddress() {
        return clientipaddress;
    }

    public void setClientipaddress(String clientipaddress) {
        this.clientipaddress = clientipaddress;
    }
    
    
     public rserver(String b){
         
        try {
            //
            setClientport(Integer.parseInt(b));
            ss= new ServerSocket(getClientport());
  
        } catch (IOException ex) {
            System.out.println("Server socket failed "+ex.getMessage());
            
        }
        System.out.println("ServerSocket created... waiting for incoming connection \n\n\n\n");
        try {
            clientsock = ss.accept();
            in_toClient = new DataInputStream(clientsock.getInputStream()); 
            out_toClient = new DataOutputStream(clientsock.getOutputStream());
            System.out.println("Succesfully created client sock and attached streams");
        } catch (IOException ex) {
            System.out.println("Failed to connect with client socket");
        }
      
    
    };
     
     public rserver (String a, String b){
         setServerport (Integer.parseInt(b));
         setServeripaddress(a);
         
         try{
         serversock = new Socket (a,getServerport());
         in_toServer = new DataInputStream(serversock.getInputStream());
         out_toServer = new DataOutputStream(serversock.getOutputStream());
         }
         catch (IOException e){
             System.out.println("Failed to make server port "+e.getMessage());
         }
         System.out.println ("Connected to server successfully");
         
     }
   
    public void connect(String a, String b) {
        
        
    }

    @Override
    public void confirm() {
          //System.out_toClient.println("Socket created..at "+clientsock.getInetAddress().toString() + clientsock.getPort());
        
    }

    @Override
    public void sendtoclient(String x) {
        try {
            out_toClient.writeUTF(x);
        } catch (IOException ex) {
            System.out.println("Failed to send "+ex.getMessage());
        }
    }
    public void sendtoserver(String x) {
        try {
            out_toServer.writeUTF(x);
        } catch (IOException ex) {
            System.out.println("Failed to send "+ex.getMessage());
        }
    }
    public String recievefromserver() {
        String temp = null;
        try {
            temp = in_toServer.readUTF();
        } catch (IOException ex) {
            System.out.println("Failed to recieve "+ex.getMessage());
        }
        return temp;
    }
    public String recievefromclient (){
        String temp = null;
            try {
                temp = in_toClient.readUTF();
            } 
            catch (IOException ex) {
                System.out.println("Error reading data input "+ex.getMessage());
            }
        return temp;
    }

   

   
    
}


/**
 *
 * @author Desktop
 */
public class kuruvillaP1Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
       // rserver toServer = new rserver(args[0],args[1]);
        rserver toClient = new rserver(args[0]);//client
        List<rserver> server = new ArrayList<rserver>();
      //  server.add(toServer);
        
        
   
        authenticator(toClient);
        System.out.println("Exited authenticator");
        rserver current_server = reciever(toClient, server);
      
        
//        System.out.println("Current server ip and port"+ current_server.getServeripaddress() + current_server.getServerport());
        
        System.out.println("Starting transmission now \n\n");
        String val = "open";
        String close = "close";
        while(val.equals(close) ==false){
            
        
        transmit(toClient,current_server);
        val = toClient.recievefromclient();
        }
        current_server.sendtoserver(val);
        System.out.println("closing now");
        try {
            current_server.getServersock().close();
        } catch (IOException ex) {
            System.out.println("Server socket failed to close");
        }
        
        try {
            toClient.getClientsock().close();
        } catch (IOException ex) {
            System.out.println("Client socket failed to close");
        }
        
    }
    public static void transmit(rserver client, rserver server){
      
//        System.out.println("client ip + port "+ client.getClientipaddress() + " "+ client.getClientport());
//         System.out.println("server ip + port "+ server.getServeripaddress()+ " "+server.getServerport());
        
       String temp = client.recievefromclient();
        server.sendtoserver(temp);
        client.sendtoclient(temp = server.recievefromserver());
    }
    public static rserver reciever(rserver toClient, List server_list) throws FileNotFoundException{
        
        //File access for recieverlist
        File file = new File("receiverList.txt");
        RandomAccessFile rac = new RandomAccessFile (file, "r");
       
        boolean stat = false;
        
        while(stat ==false){
        String R_nameClient = toClient.recievefromclient(); //input
        System.out.println("client name recieved is "+R_nameClient);
        String Rname = null; 
        String Rip = null;
        String Rport = null;
             
        Scanner scan = new Scanner(file);
        boolean status = true;
        //clear till here
        
        while(scan.hasNext())
             {
               Rname= scan.next();
               
               if(Rname.equals(R_nameClient))
               {    Rip = scan.next();
                    Rport = scan.next();
                        
                        
                    System.out.println("reciever name: "+Rname +" + iP: "+ Rip+" + port: "+ Rport);
                        
                        for (Iterator it = server_list.iterator(); it.hasNext();) 
                        {
                            rserver s = (rserver) it.next();
                            System.out.println("Inside for loop "+ s.getServerport() + " "+s.getServeripaddress());
                                
                            if((s.getServeripaddress().equals(Rip))&&( Integer.toString(s.getServerport()).equals(Rport))){
                                System.out.println("Connections exists; proceed");
                                toClient.sendtoclient("match");
                                stat = true;
                                
                                return s;
                                
                            }
                            
                            
                        }
                        System.out.println("Establishing connection now");
                        toClient.sendtoclient("mismatch");
             
                         String newreciever = toClient.recievefromclient();
             
                         rserver x = new rserver(Rip, Rport);
                        server_list.add(x);
                        
                        return x;
                }
               else if(scan.hasNext() ==false){
                   System.out.println("Username not found");
                   toClient.sendtoclient("username not found");
                   
               }
               
                    
              }
    }
             
                 
             
             rserver x = null;
             return x;
    
        
    }
    public static void authenticator(rserver rs) throws FileNotFoundException{
        
        //opening file of type randomaccessfile
        File file = new File("userList.txt");
        RandomAccessFile rac = new RandomAccessFile(file,"r");
        
            
        boolean status = true;
        
        while(status==true){
            
            String clientusername = rs.recievefromclient();//
            Scanner scan = new Scanner(file);
            String temppassword = null;
            String filepassword = null;
            
            
            
            
            while(scan.hasNext()){
                
            String fileusername= scan.next();
                    
            if(fileusername.equals(clientusername)){
                        filepassword = scan.next();
                        rs.sendtoclient("match");
                        System.out.println(fileusername + " "+ filepassword);



                        temppassword = rs.recievefromclient();

                        if(filepassword.equals(temppassword)){
                            rs.sendtoclient("match");
                        }
                        else{
                            System.out.println("Username and password not found");
                            System.out.println("Entering username and password loop again");
                            rs.sendtoclient("mismatch");
                            break;
                        }

                        status = false;
                        break;

                    }
            else if(scan.hasNext() == false){
                rs.sendtoclient("mismatch");
            }
                
                }
            //rs.sendtoclient("mismatch");//end of inner while loop
           
            
        }
        
        
        
    }
    
}
