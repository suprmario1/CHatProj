//----------------------------------------------------------------------------//

import java.awt.*;
import java.awt.Dialog;
import java.awt.event.*;

import java.io.*;
import java.net.*;

//----------------------------------------------------------------------------//


public abstract class    GUIChat
              extends    AppletNLO
              implements ActionListener, Runnable
{
   TextArea   logTA     = new TextArea();
   TextField  messageTF = new TextField();
   TextField  nameTF    = new TextField();
   TextField  loginTF   = new TextField();
   Button     loginBN   = new Button("Login");
   Frame      loginFrame= new Frame();
   Dialog     loginDB   = new Dialog(loginFrame);
   
   //Button     submitBN  = new Button("Submit");

   List       onlineLT = new List();
   List       friendLT = new List();
   
   String screenName = "";

   DataInputStream  input;
   DataOutputStream output;


   //-------------------------------------------------------------------------//

   public GUIChat()
   {
      super();
   }

   //-------------------------------------------------------------------------//
   @Override
   public void init()
   {
      setupDialog();
      
      setupGUI();

      setupConnection();

      setupInputThread();
  }

  //-------------------------------------------------------------------------//
  public void setupDialog(){     

      loginTF.setBounds(10,30,100,20);
      loginBN.setBounds(10,60,100,20);
      loginBN.addActionListener(new LoginListener());
      
      
      loginDB.setLayout(null);
      loginDB.setSize(150,100);
      
      loginDB.add(loginTF);
      loginDB.add(loginBN);
      
      
      loginDB.setVisible(true);
      
      
      
  
  }

  public void setupGUI()
  {
      super.setLayout(null);
      add(logTA,      10,  10, 300, 200);
      add(messageTF,  10, 220, 240,  20, this);
      add(nameTF,    250, 220, 50,  20);
      //add(submitBN,  700, 420, 100,  20, this);

      add(onlineLT, 320, 10, 100, 100);
      add(friendLT, 320, 120, 100, 100);


      onlineLT.addItemListener(new OnlineListener());

      friendLT.addItemListener(new FriendListener());


  }

  //-------------------------------------------------------------------------//

  public void setupConnection()
  {
      try
      {
         Socket socket = connect();

         input  = new DataInputStream(socket.getInputStream());
         output = new DataOutputStream(socket.getOutputStream());
      }
      catch(IOException x){};
  }

  //-------------------------------------------------------------------------//

  public abstract Socket connect() throws IOException;

  //-------------------------------------------------------------------------//

  public void setupInputThread()
  {
     Thread t = new Thread(this);

     t.start();
  }

  //-------------------------------------------------------------------------//
  @Override
  public void run()
  {
     String message;
     String[] parts;

     while(true)
     {
        try
        {
           message = input.readUTF();
           parts=message.split(":");
              if(parts[0].equals("@@@@@"))
              {
                 if(parts[1].equals("ADDU"))
                 {
                     onlineLT.add(parts[2]);
                     
                 }
                 else if(parts[1].equals("ADDF"))
                 {
                     friendLT.add(parts[2]);
                     
                 }
              }

           logTA.append(message + "\n");
        }
        catch(IOException x){};
     }
  }

   //-------------------------------------------------------------------------//
   @Override
   public void actionPerformed(ActionEvent e)
   {
      try
      {
         output.writeUTF(nameTF.getText() + ": " + messageTF.getText());

         //logTA.appendText(nameTF.getText() + ": " + messageTF.getText() + "\n");

         messageTF.setText("");
      }
      catch(IOException x){};


   }
   
   public class LoginListener implements ActionListener{
       public void actionPerformed(ActionEvent e){
           screenName=loginTF.getText();
           try{
              output.writeUTF("@@@@@:ADDU:" + screenName);
           }
           catch(IOException x){};

           nameTF.setText(screenName);
           loginDB.dispose();

           
       }
   }

   //-------------------------------------------------------------------------//
   
   public class OnlineListener implements ItemListener
   {
      @Override
      public void itemStateChanged(ItemEvent e)
      {
         friendLT.add(onlineLT.getSelectedItem());
         try{
             output.writeUTF("@@@@@:ADDF:" + onlineLT.getSelectedItem());
         }
         catch(IOException x){};
      }
   }

   public class FriendListener implements ItemListener
   {
      @Override
      public void itemStateChanged(ItemEvent e)
      {
         friendLT.remove(friendLT.getSelectedIndex());
         try{
             output.writeUTF("@@@@@:REMOVEF:" + onlineLT.getSelectedItem());
         }
         catch(IOException x){};         
      }
   }

   //-------------------------------------------------------------------------//

}