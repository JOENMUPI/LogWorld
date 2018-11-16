package Visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import Visual.WebSI;

public class Log extends JPanel{
	private static final long serialVersionUID = 1L;
	private ArrayList<JPanel> p;
	private JTextArea ta;
	private JButton b;
	private ArrayList<JLabel> l;
	private ArrayList<JTextField> tf;
	private JScrollPane sp;
	private WebSI ws;
	
	public void components(){
		ta = new JTextArea(40, 100);
		//ta.setEditable(false);
		
		sp = new JScrollPane(ta);
		
		b = new JButton("Search");
		
		tf = new ArrayList<JTextField>();
		p = new ArrayList<JPanel>();
		l = new ArrayList<JLabel>();
		try {
			ws = Service.create(new URL("http://127.0.0.1:9000/kuku?wsdl"), new QName("http://LogWorld/", "WebSService")).getPort(new QName("http://LogWorld/","WebSPort"), WebSI.class);
		} catch (MalformedURLException e) { e.printStackTrace(); }
	}
	
	public void label(){
		for(int i = 0; i <= 6; i++){
			l.add(new JLabel());
			
			l.get(i).setFont(new Font("Console",Font.BOLD,15));
			l.get(i).setForeground(Color.DARK_GRAY);
		}
		
		l.get(0).setFont(new Font("Console",Font.BOLD,25));
		l.get(0).setForeground(Color.GRAY);
		
		l.get(0).setText("New student");
		l.get(1).setText("User:");
		l.get(2).setText("Host");
		l.get(3).setText("TypeComponent");
		l.get(4).setText("TypeMsg");
		l.get(5).setText("method");
		l.get(6).setText("Result");
	}
	
	public void text(){	
		for(int i = 0; i <= 4; i++){
			tf.add(new JTextField(15));
			tf.get(i).setText("");
			
			tf.get(i).setForeground(Color.GRAY);
			tf.get(i).setBorder(new LineBorder(Color.GRAY));
		}
		
		sp.setBorder(new LineBorder(Color.GRAY));
	}
	
	public void button(){ b.setBackground(Color.GRAY); }
	
	public void panel(){
		int j = 0;
		label();
		text();
		button();
		
		for(int i = 0; i <= 18; i++){
			p.add(new JPanel());
			p.get(i).setBackground(Color.LIGHT_GRAY);
			
			if(i <= 14){ p.get(i).setLayout(new FlowLayout()); }
			if(i > 15 && i <= 17){ p.get(i).setLayout(new BorderLayout()); }	
			if(i == 18){ p.get(i).setLayout(new GridLayout(10,1)); } 
			
			if(i <= 6){ p.get(i).add(l.get(i)); }
			if(i > 7  && i <= 12){ p.get(i).add(tf.get(j)); j++;}
		}
		
		p.get(13).add(b);
		p.get(14).add(sp);
		
		p.get(4).add(p.get(7));
		p.get(18).add(p.get(1));
		p.get(18).add(p.get(8));
		p.get(18).add(p.get(2));
		p.get(18).add(p.get(9));
		p.get(18).add(p.get(3));
		p.get(18).add(p.get(10));
		p.get(18).add(p.get(4));
		p.get(18).add(p.get(11));
		p.get(18).add(p.get(5));
		p.get(18).add(p.get(12));
		
		p.get(16).add(p.get(6),BorderLayout.NORTH);
		p.get(16).add(p.get(14),BorderLayout.WEST);
		
		p.get(17).add(p.get(0),BorderLayout.NORTH);
		p.get(17).add(p.get(18),BorderLayout.WEST);
		p.get(17).add(p.get(13),BorderLayout.SOUTH);
	}
	
	public void search() {
		try {
			ta.setText(
				ws.requestData(tf.get(0).getText(), tf.get(1).getText(), tf.get(2).getText(), tf.get(3).getText(), tf.get(4).getText()
			));
		} catch(Exception e) { System.out.println(e); ta.setText(""); }
	}
	
	public void action(){ this.b.addActionListener((e) -> { search(); }); }

	public Log(){
		components();
		panel();
		action();
		
		this.setBackground(Color.LIGHT_GRAY);
		this.add(p.get(17));
		this.add(p.get(16));
		ta.setText(ws.requestLastData());
	}
}