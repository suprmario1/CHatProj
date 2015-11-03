import java.applet.Applet;
import java.awt.Component;
import java.awt.TextField;
import java.awt.event.ActionListener;

public class AppletNLO extends Applet {
	   public AppletNLO()
	   {
	      super();
	   }
	   
	   public void add(Component c,int w, int x, int y, int z)
	   {
		   c.setBounds(w,x,y,z);
		   		   
	   }
	   
	   public void add(TextField c,int w, int x, int y, int z, ActionListener l)
	   {
		   c.setBounds(w,x,y,z);
		   c.addActionListener(l);
	   }

}
