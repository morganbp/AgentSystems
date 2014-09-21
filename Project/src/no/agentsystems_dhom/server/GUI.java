package no.agentsystems_dhom.server;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GUI extends JFrame{
	 private static final long serialVersionUID = 1L;

	   protected Container c;
	   protected JTextArea output = new JTextArea ();
	   
	   public GUI(String title) {
			super(title);
			setSize(500, 300);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			output.setEditable (false); 
			c = getContentPane();
			c.add (output, BorderLayout.CENTER);
			JScrollPane scr = new JScrollPane(output);
			c.add(scr);
			scr.setAutoscrolls(true);
			setVisible(true);
	   }
	   
	   public void setText(String str) {
		   output.setText(str);
	   }
	   
	   public void append(String str) {
		   output.append(str);
//		   output.setCaretPosition(output.getDocument().getLength());
	   }
}
