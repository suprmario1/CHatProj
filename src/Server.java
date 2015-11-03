import java.io.*;
import java.net.*;


public class Server
{
   public static final int N = 10;
   public static int n = 0;

   ServerSocket server;

   ClientHandler[] clientHandler = new ClientHandler[N];


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
         while(true)
         {
           try
            {
               String message = input.readUTF();

               for(int i = 0; i < n; i++)

                  //if(clientHandler[i] != this)

                     clientHandler[i].output.writeUTF(message);
            }
            catch(IOException x){};
         }
      }
   }

}