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
    public T() throws InterruptedException {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setLayout(null);
        this.setSize(350, 100);
        JPanel jp= new JPanel();
        jp.setLayout(new GridBagLayout());
        GridBagConstraints g= new GridBagConstraints();
        p = new CProgressBar(50);
        p.setBackground(Color.DARK_GRAY);
        p.setForeground(Color.WHITE);
        p.setVal(50);
        g.weightx=1;
        g.insets= new Insets(10,10,10,10);
        g.fill=GridBagConstraints.BOTH;
        jp.add(p,g);
        
        this.add(jp);
        
        this.setVisible(true);
        
        
        
    }

    public static void main(String[] args) throws InterruptedException {

        T d=new T();
        d.p.setSmooth(false);
        d.p.setVal(10);
        Thread.sleep(1000);
        System.out.println(40);
        d.p.setVal(40);
        Thread.sleep(1000);
        d.p.setVal(5);
        Thread.sleep(1000);
            
    }
}