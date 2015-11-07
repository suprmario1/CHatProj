import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class Server
{
   public static final int N = 10;
   public static int n = 0;

   ServerSocket server;

   ClientHandler[] clientHandler = new ClientHandler[N];
   ArrayList<String> onlineNames= new ArrayList();


   public static void main(String[] args)
   {
      Server server = new Server();

      server.init();
   }


   public void init()
   {
      try
      {
         server = new ServerSocket(8080);

         for(int i = 0; i < N; i++)
         {
            clientHandler[i] = new ClientHandler();

            Thread t = new Thread(clientHandler[i]);

            t.start();

            n++;
         }


      }
      catch(IOException x){};

   }


   public class ClientHandler implements Runnable
   {
      DataInputStream  input;
      DataOutputStream output;
      
      ArrayList<String> friendList= new ArrayList();


      public ClientHandler()
      {
         try
         {
            Socket socket = server.accept();

            input  = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
         }
         catch(IOException x){};
      }

      public void run()
      {
         String[] part;
         while(true)
         {
           try
            {
               String message = input.readUTF();

               part=message.split(":");
               if(part[0].equals("@@@@@")){
                  if(part[1].equals("ADDU"))
                    onlineNames.add(part[2]);
                  else if(part[1].equals("ADDF"))
                    friendList.add(part[2]);
                  else if(part[1].equals("REMOVEF"));
                  else if(part[1].equals("REMOVEU"));

                    
                  System.out.println(part[0]+" "+ part[1]+ " " +part[2]);
               }

               for(int i = 0; i < n; i++)

                  //if(clientHandler[i] != this)

                    clientHandler[i].output.writeUTF(message);
            }
            catch(IOException x){};
         }
      }
   }

}