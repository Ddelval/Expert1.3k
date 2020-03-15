import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class MainWindow extends JFrame{
	private CProgressBar[] lev_prog;
	private JLabel[] lev_tit;
	private JLabel[][] status;
	private CButton[] options;
	
	private long lastmax;
	
	public MainWindow() {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel base= new JPanel(new GridBagLayout());
		GridBagConstraints gbc= new GridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
		gbc.insets=new Insets(10,10,0,10);
		gbc.gridwidth=2;
		base.add(formatTitle("Levels:"),gbc);
		gbc.insets=new Insets(0,10,0,10);
		gbc.weightx=1;
		gbc.weighty=1;
		gbc.gridy=1;
		gbc.anchor=GridBagConstraints.NORTHWEST;
		base.add(levels(),gbc);
		gbc.gridwidth=1;
		
		gbc.gridx=0;
		gbc.gridy=2;
		gbc.weighty=0;
		gbc.insets=new Insets(20,10,0,10);
		base.add(formatTitle("Status"),gbc);
		gbc.gridx=1;
		base.add(formatTitle("Controls"),gbc);
		gbc.gridx=0;
		gbc.gridy=3;
		gbc.weighty=0.5;
		gbc.weightx=0.3;
		gbc.insets=new Insets(0,10,0,10);
		base.add(informationGrid(),gbc);
		gbc.gridx=1;
		gbc.weightx=1;
		

		JPanel jp = new JPanel(new GridBagLayout());
		GridBagConstraints g= new GridBagConstraints();
		g.fill=GridBagConstraints.BOTH;
		g.weightx=1;
		g.gridy=0;
		g.insets=new Insets(10,10,10,10);
		
		g.weighty=1;
		g.insets=new Insets(10,0,0,10);
		jp.add(leftGrid(),g);

		
		
		base.add(jp,gbc);
		
		add(base);
		setSize(750,700);
		setLocationRelativeTo(null);
		
		setVisible(true);
		
		lastmax=0;
		
	}
	public void updateValues(StatusWord sw) {
		if(sw.operate!=null)	options[1].setSelected((sw.operate.equals("Operative")? 1:0));
		if(sw.transmit!=null)	options[0].setSelected((sw.transmit.equals("RX")? 0:1));
		options[4].setSelected(sw.input-1);

		options[5].setSelected(sw.txAntenna-1);
		if(sw.memoryBank!=null)options[3].setSelected(sw.memoryBank.equals("A")? 0:1);

		double maxpower=lev_prog[0].getMax();
		if(sw.powerLevel>0&&sw.powerLevel<4) {
			options[2].setSelected(sw.powerLevel-1);
			switch(sw.powerLevel) {
				case 1:
					status[6][1].setText("Low"); 
					maxpower= 500;
					break;
				case 2:
					status[6][1].setText("Medium");
					maxpower= 1000;
					break;
				case 3:
					status[6][1].setText("High");
					maxpower= 1500;
					break;
			}
		}
		if(sw.operate!=null&&sw.operate.equals("Standby"))maxpower=200;
		if(sw.temp!=-1) {
			status[7][1].setText(Integer.toString(sw.temp)+" ºC");
		}
		
		
		
		
		if(lev_prog[0].getMax()!=maxpower) {
			lev_prog[0].setMax(maxpower);
			lev_prog[1].setMax(maxpower);
		}
		
		
		lev_prog[0].setVal(sw.outputPower);
		lev_prog[2].setVal(sw.I_PA);
		
		long tmpdat= System.currentTimeMillis();
		
		if(tmpdat-lastmax>2000) {
			lev_prog[1].setVal(lev_prog[0].getVal());
			lastmax=tmpdat;
			
		}
		else {
			lev_prog[1].setVal(Math.max(lev_prog[0].getVal(),lev_prog[1].getVal()));
		}
		
		lev_prog[3].setVal((sw.SWR_ATU));
		lev_prog[4].setVal((sw.SWR_ANT));
		lev_prog[5].setVal(sw.temp);
		
		
		lev_tit[0].setText(String.format("%5d  W",(int)lev_prog[0].getVal() ));
		lev_tit[1].setText(String.format("%5d  W",(int)lev_prog[1].getVal() ));
		lev_tit[2].setText(renderDouble(lev_prog[2].getVal() ));
		
		
		
		lev_tit[3].setText(String.format("%6.2f   ", lev_prog[3].getVal()));
		lev_tit[4].setText(String.format("%6.2f   ", lev_prog[4].getVal()));

		lev_tit[5].setText(String.format("%6.0f ºC", lev_prog[5].getVal()));
		
		this.repaint();
	}
	
	private String renderDouble(double d) {
		String s= String.format("%.2f", d);
		while(s.length()<5)s="0"+s;
		return s+"  A";
	}
	
	
	private JLabel formatTitle(String s) {
		JLabel jl = new JLabel(s);
		jl.setFont(new Font("Arial", Font.BOLD, 20));
		jl.setHorizontalAlignment(SwingConstants.LEFT);
		return jl;
	}
	
	private JLabel formatLeft(String s) {
		JLabel jl = new JLabel(s+":  ");
		jl.setFont(new Font("Arial", Font.BOLD, 15));
		jl.setHorizontalAlignment(SwingConstants.LEFT);
		return jl;
	}
	
	private JLabel formatRight(String s) {
		JLabel jl = new JLabel(s);
		jl.setFont(new Font("Arial", Font.PLAIN, 15));
		return jl;
	}
	
	private JLabel formatlevtit(String s) {
		JLabel jl = new JLabel(s);
		jl.setFont(new Font("Courier",Font.PLAIN, 20));
		jl.setVerticalAlignment(SwingConstants.BOTTOM);
		jl.setHorizontalAlignment(SwingConstants.RIGHT);
		return jl;
	}
	
	private JLabel formatlevtit2(String s) {
		JLabel jl = new JLabel(s);
		jl.setFont(new Font("Arial", Font.PLAIN, 15));
		jl.setVerticalAlignment(SwingConstants.BOTTOM);
		jl.setHorizontalAlignment(SwingConstants.RIGHT);
		return jl;
	}
	private CProgressBar formatlevprog () {
		CProgressBar jpb= new CProgressBar();
		
		return jpb;
	}
	private JPanel levels() {
		JPanel res= new JPanel(new GridBagLayout ());
		lev_tit = new JLabel[6];
		JLabel[] lt= new JLabel[6];
		lev_prog = new CProgressBar[6];
		
		lt [0]= formatlevtit2("OP:");
		lev_tit [0]= formatlevtit("    0 W");
		lev_prog[0]= formatlevprog();
		lev_prog[0].setSmooth(false);
		
		lt [1]= formatlevtit2("POP:" );
		lev_tit [1]= formatlevtit("    0 W");
		lev_prog[1]= formatlevprog();
		lev_prog[1].setSmooth(false);
		
		lt [2]= formatlevtit2("IPA:");
		lev_tit [2]= formatlevtit(" 0.00 A");
		lev_prog[2]= formatlevprog();
		lev_prog[2].setMax(50);
		
		lt [3]= formatlevtit2("SWR-TX:");
		lev_tit [3]= formatlevtit(" 0.00  ");
		lev_prog[3]= formatlevprog();
		lev_prog[3].setMax(2);
		lev_prog[3].setMin(1);
		
		lt [4]= formatlevtit2("SWR-ANT:");
		lev_tit [4]= formatlevtit(" 0.00  ");
		lev_prog[4]= formatlevprog();
		lev_prog[4].setMax(2);
		lev_prog[4].setMin(1);
		
		lt [5]= formatlevtit2("TEMP:");
		lev_tit [5]= formatlevtit(" 0.00  ");
		lev_prog[5]= formatlevprog();
		lev_prog[5].setMax(100);
		
		GridBagConstraints gb= new GridBagConstraints();
		gb.fill=GridBagConstraints.BOTH;
		gb.anchor=GridBagConstraints.SOUTHWEST;
		gb.weighty=1;
		for(int i=0;i<6;++i) {
			gb.gridy=i;
			gb.gridx=0;
			gb.weightx=0;
			gb.weighty=0;
			res.add(lt[i],gb);

			gb.insets=new Insets(10,15,0,10);
			gb.gridx=1;
			gb.weightx=1;
			gb.weighty=1;
			res.add(lev_prog[i],gb);
			
			gb.insets=new Insets(0,0,0,0);
			gb.gridx=2;
			gb.weightx=0;
			gb.weighty=0;
			res.add(lev_tit[i],gb);
		}
		
		
		return res;
	}
	
	private JPanel informationGrid() {
		JPanel res=new JPanel(new GridBagLayout());
		status = new JLabel[8][2];
		
		status[0][0]=formatLeft("Mode");
		status[0][1]=formatRight("Standby RX");
		
		status[1][0]=formatLeft("Memory bank");
		status[1][1]=formatRight("1");
		
		status[2][0]=formatLeft("Input");
		status[2][1]=formatRight("1");
		
		status[3][0]=formatLeft("Band");
		status[3][1]=formatRight("160 m");
		
		status[4][0]=formatLeft("TX antenna");
		status[4][1]=formatRight("1");
		
		status[5][0]=formatLeft("ATU status");
		status[5][1]=formatRight("C");
		
		status[6][0]=formatLeft("Power Level");
		status[6][1]=formatRight("Low");
		
		status[7][0]=formatLeft("Temperature");
		status[7][1]=formatRight("25 ºC");
		
		JLabel[] tits;
		tits= new JLabel[4];
		tits[0]=formatLeft("Mode");
		tits[1]=formatLeft("Power Level");
		tits[2]=formatLeft("Input");
		tits[3]=formatLeft("Antenna");
		
		
		options=new CButton[6];
		Color sel = new Color(0,134,255);
		
		options[0]= new CButton(2);
		options[0].setTitles("RX","TX");
		options[0].setColor(new Color(50,200,50),new Color(200,50,50));
		
		
		options[1]= new CButton(2);
		options[1].setTitles("Standby","Operate");
		options[1].setColor(new Color(50,200,50),new Color(200,50,50));
		
		
		options[2]= new CButton(3);
		options[2].setColor(new Color(0,130,250),new Color(50,200,50),new Color(200,50,50));
		options[2].setTitles("L","M","H");
		options[2].setToolTipText("Power");
		
		options[3] = new CButton(2);
		options[3].setColor(sel,sel);
		options[3].setTitles("A","B");
		options[3].setToolTipText("Bank");
		
		options[4]= new CButton(2);
		options[4].setColor(sel,sel);
		options[4].setTitles("1","2");
		options[4].setToolTipText("Input");
		
		
		options[5] = new CButton(4);
		options[5].setColor(sel,sel,sel,sel);
		options[5].setTitles("1","2","3","4");
		options[5].setToolTipText("Output");
		
		
		GridBagConstraints gb= new GridBagConstraints();
		gb.fill=GridBagConstraints.BOTH;
		gb.anchor=GridBagConstraints.NORTHWEST;
		gb.weightx=2;
		gb.weighty=2;
		gb.gridwidth=2;
		gb.gridx=0;
		gb.gridy=0;
		gb.insets=new Insets(5,0,5,0);
		//gb.insets=new Insets(10,0,5,0);
		//res.add(tits[0],gb);
		//gb.gridy+=1;
		gb.insets=new Insets(0,0,5,0);
		res.add(options[0],gb);
		gb.gridy+=1;
		res.add(options[1],gb);
		gb.gridy+=1;
		
		//res.add(tits[1],gb);
		//gb.gridy+=1;
		res.add(options[2],gb);
		gb.gridy+=1;
		
		//res.add(tits[2],gb);
		//gb.gridy+=1;
		res.add(options[3],gb);
		gb.gridy+=1;
		
		//res.add(tits[3],gb);
		//gb.gridy+=1;
		res.add(options[4],gb);
		gb.gridy+=1;
		
		res.add(options[5],gb);
		gb.gridy+=1;
		
		
		return res;
	}
	private JPanel leftGrid() {
		JButton[][] but= new JButton[3][3];
		
		but[0][0]=new JButton ("Input");
		but[0][0].addActionListener(e->{Main.q.add((byte)0x01);});
		but[0][1]=new JButton ("<- L");
		but[0][1].addActionListener(e->{Main.q.add((byte)0x05);});
		but[0][2]=new JButton ("L ->");
		but[0][2].addActionListener(e->{Main.q.add((byte)0x06);});
		but[1][0]=new JButton ("Antenna");
		but[1][0].addActionListener(e->{Main.q.add((byte)0x04);});
		but[1][1]=new JButton ("<- C");
		but[1][1].addActionListener(e->{Main.q.add((byte)0x07);});
		but[1][2]=new JButton ("C ->");
		but[1][2].addActionListener(e->{Main.q.add((byte)0x08);});
		but[2][0]=new JButton ("<- Band");
		but[2][0].addActionListener(e->{Main.q.add((byte)0x02);});
		but[2][1]=new JButton ("Band ->");
		but[2][1].addActionListener(e->{Main.q.add((byte)0x03);});
		but[2][2]=new JButton ("Tune");
		but[2][2].addActionListener(e->{Main.q.add((byte)0x09);});
		
		JPanel jp= new JPanel(new GridLayout(6,3,5,5));


		for(int i=0;i<3;++i) {
			for(int j=0;j<3;++j) {
				jp.add(but[i][j]);
			}
		}
		
		
		JComponent[][] but2= new JComponent[3][3];
		
		but2[0][0]=new JButton ("<--");
		((JButton)but2[0][0]).addActionListener(e->{Main.q.add((byte)0x0F);});
		but2[0][1]=new JButton ("-->");
		((JButton)but2[0][1]).addActionListener(e->{Main.q.add((byte)0x10);});
		but2[0][2]=new JLabel("");

		but2[1][0]=new JButton ("Cat");
		((JButton)but2[1][0]).addActionListener(e->{Main.q.add((byte)0x0E);});
		but2[1][1]=new JButton ("Display");
		((JButton)but2[1][1]).addActionListener(e->{Main.q.add((byte)0x0C);});
		but2[1][2]=new JButton ("OFF");
		((JButton)but2[1][2]).addActionListener(e->{Main.q.add((byte)0x0A);});
		but2[2][0]=new JButton ("Set");
		((JButton)but2[2][0]).addActionListener(e->{Main.q.add((byte)0x11);});
		but2[2][1]=new JButton ("Power");
		((JButton)but2[2][1]).addActionListener(e->{Main.q.add((byte)0x0B);});
		but2[2][2]=new JButton ("Operate");
		((JButton)but2[2][2]).addActionListener(e->{Main.q.add((byte)0x0D);});
		
		
		for(int i=0;i<3;++i) {
			for(int j=0;j<3;++j) {
				jp.add(but2[i][j]);
			}
		}
		return jp;
	}
	
	public static void main() {
		MainWindow win= new MainWindow();
	}
	
	
	
}

