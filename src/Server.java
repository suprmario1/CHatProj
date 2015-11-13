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
            for(int i=0;i<onlineNames.size();i++){
                friendList.add(onlineNames.get(i));
                output.writeUTF("@@@@@:ADDU:"+friendList.get(i));
                
            }
            //Testcode
            for(int j=0;j<onlineNames.size();j++){
                System.out.println("i:"+onlineNames.get(j));
            }
//            for(int k=0;k<friendList.size();k++){
//                output.writeUTF("@@@@@:ADDF:"+friendList.get(k));
//                System.out.println("j:"+friendList.get(k));
//            }
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
                  {
                    onlineNames.add(part[2]);
                    for(int i = 0; i < n; i++)
                //Testcode
                       clientHandler[i].output.writeUTF("@@@@@:ADDU:"+part[2]);
                               for(int j=0;j<onlineNames.size();j++){
                System.out.println("k:"+onlineNames.get(j));
            }
                  }
                  else if(part[1].equals("ADDF"))
                  {
                    friendList.add(part[2]);
                  }
                  else if(part[1].equals("REMOVEF")){
                    friendList.remove(part[2]);
                    output.writeUTF("@@@@@:REMOVEF:"+part[2]);

                  }
//                  else if(part[1].equals("REMOVEU")){
                      
//                  }

               }
               else{

                for(int i = 0; i < n; i++)

                   //if(clientHandler[i] != this)

                     clientHandler[i].output.writeUTF(message);
               }
            }
            catch(IOException x){};
         }
      }
   }

}