//----------------------------------------------------------------------------//

import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;

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
   //Button     submitBN  = new Button("Submit");

   List       onlineLT = new List();
   List       friendLT = new List();

   DataInputStream  input;
   DataOutputStream output;


   //-------------------------------------------------------------------------//

   public GUIChat()
   {
      super();
   }

   //-------------------------------------------------------------------------//

   public void init()
   {
      setupGUI();

      setupConnection();

      setupInputThread();
  }

  //-------------------------------------------------------------------------//

  public void setupGUI()
  {
      add(logTA,      10,  10, 300, 200);
      add(messageTF,  10, 220, 240,  20, this);
      add(nameTF,    250, 220, 50,  20);
      //add(submitBN,  700, 420, 100,  20, this);

      add(onlineLT, 320, 10, 100, 100);
      add(friendLT, 320, 120, 100, 100);


      onlineLT.add("Pedro");
      onlineLT.add("Aysmel");
      onlineLT.add("Brian");

      onlineLT.addItemListener(new OnlineListener());

      friendLT.addItemListener(new FriendListener());
      friendLT.add("Pedro");
      friendLT.add("Aysmel");

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

  public void run()
  {
     String message;

     while(true)
     {
        try
        {
           message = input.readUTF();

           logTA.append(message + "\n");
        }
        catch(IOException x){};
     }
  }

   //-------------------------------------------------------------------------//

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

   //-------------------------------------------------------------------------//

   public class OnlineListener implements ItemListener
   {

      public void itemStateChanged(ItemEvent e)
      {
         friendLT.add(onlineLT.getSelectedItem());
      }
   }

   public class FriendListener implements ItemListener
   {

      public void itemStateChanged(ItemEvent e)
      {
         friendLT.remove(friendLT.getSelectedIndex());
      }
   }

   //-------------------------------------------------------------------------//

}