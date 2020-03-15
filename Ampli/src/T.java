import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class T extends JFrame {
	public CProgressBar p;
    public T() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setLayout(null);
        this.setSize(350, 100);
        JPanel jp= new JPanel();
        jp.setLayout(new GridBagLayout());
        GridBagConstraints g= new GridBagConstraints();
        p = new CProgressBar(2);
        p.setFunction(x->{
        	if(x<0.5) {
        		return 2*x;
        	}
        	if(x<1) {
        		return 1.5*(x-0.5)+1;
        	}
        	else return x+1.75;
        });
        p.setBounds(15, 15, 300, 15);
        p.setSmooth(false);
        p.setVal(1.5);
        g.weightx=1;
        g.insets= new Insets(10,10,10,10);
        g.fill=GridBagConstraints.BOTH;
        jp.add(p,g);
        
        this.add(jp);
        
        this.setVisible(true);
        
    }

    public static void main(String[] args) {

        T d=new T();
        

            
    }
}