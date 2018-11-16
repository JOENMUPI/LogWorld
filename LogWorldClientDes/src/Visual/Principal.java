package Visual;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

public class Principal extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public void kuku() {
		this.add(new Log());
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Console Log WS");
		this.setVisible(true);
	}
	
	public Principal(){
		 if(SystemTray.isSupported()){
			 JPopupMenu pop = new JPopupMenu();
			 JMenuItem menu = new JMenuItem("Abrir");
			 menu.addActionListener(new ActionListener(){
				 public void actionPerformed(ActionEvent e) {
					 kuku();
					 pop.setVisible(false);
				 }
			 });
			 
			 pop.add(menu);
			 JMenuItem exit = new JMenuItem("exit");
			 exit.addActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent e) { System.exit(0); }
			 });
			 
			 pop.addSeparator();
			 pop.add(exit);
			 TrayIcon trayicon = new TrayIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + "icons\\home-icon.png")).getImage(), "WS", null);
			 trayicon.addMouseListener(new MouseAdapter(){
				 public void mouseReleased(MouseEvent e) {
					 pop.setVisible(true);
					 pop.setLocation(e.getX(), e.getY()-80);
				 }
			 });
   
			 try { SystemTray.getSystemTray().add(trayicon); } 
			 catch (AWTException e1) { e1.printStackTrace(); }
		 } 
		 
		 else { JOptionPane.showMessageDialog(null, "no soporta barra de tareas"); }
	 }
 
	 public static void main(String arg[]){ new Principal().kuku(); }
}