import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.*;
import javax.swing.*;
import java.util.*;

import javax.swing.JButton;

public class CButton  extends JButton{
	private int selected=-1;
	private boolean action=true;
	private int nelements;
	private String[] titles;
	private Color[] backgrounds;
	private boolean darkstyle=MainWindow.darkMode;
	private double darken=0.3;
	private Font fon= new Font("Arial",Font.BOLD,17);
	
	public CButton(int nelem) {
		titles= new String[nelem];
		backgrounds=new Color[nelem];
		
		nelements=nelem;
		this.addMouseListener(new MouseAction());
		titles[0]="OP";
		titles[1]="SB";
		
		backgrounds[0]=Color.red;
		backgrounds[1]=Color.blue;
		super.setEnabled(false);
		
	}
	
	private Color textpick(Color bck) {
		double L=0;
		double r=bck.getRed()/255.0;
		if(r<=0.03928)r=r/12.92;
		else r=Math.pow(((r+0.055)/1.055),2.4);
		L+=0.2126*r;
		
		double g=bck.getGreen()/255.0;
		if(g<=0.03928)g=g/12.92;
		else g=Math.pow(((g+0.055)/1.055),2.4);
		L+=0.7152*g;
		
		double b=bck.getBlue()/255.0;
		if(r<=0.03928)b=b/12.92;
		else b=Math.pow(((b+0.055)/1.055),2.4);
		L+=0.0722*b;
		
		if(L>0.179)return new Color(0,0,0);
		else return new Color(255,255,255);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		int wid= getWidth();
		textpick(Color.red);
		int hei = getHeight();
		g.setFont(fon);
		Graphics2D g2d= (Graphics2D) g;
		FontMetrics fm = g.getFontMetrics();
		
		RoundRectangle2D rR = new RoundRectangle2D.Float(0, 0, (wid), hei, 10, 10);
		g2d.clip(rR);
		g2d.setColor(Color.black);
		
		
		for(int i=0;i<nelements;++i) {
			
			int x1=(int)Math.ceil(i*wid/(double)nelements);
			int x2=(int)Math.ceil(wid/(double)nelements);
			
			if(backgrounds[i]!=null) {
				g2d.setColor(backgrounds[i]);
			}
			else {
				g2d.setColor(Color.white);
				
			}
			
			if(selected!=i) {
				g2d.setColor(Color.LIGHT_GRAY);
				if(darkstyle) {
					g2d.setColor(Color.DARK_GRAY.darker());
				}
			}
			else{
				g2d.setColor(g2d.getColor());
				RadialGradientPaint rgp = new RadialGradientPaint(new Point2D.Float(x1+x2/2.0f,hei/2.0f),x2,new float[] {0.0f,0.3f,0.6f,0.7f,1.0f}, new Color[] {g2d.getColor().brighter(),g2d.getColor(),g2d.getColor().darker(),g2d.getColor().darker(),Color.black});
				g2d.setPaint(rgp);
			}
			
			
			
			
			g2d.fillRect(x1, 0, x2, hei);
			
			
			
			String s1=titles[i];
			if(titles[i]==null)s1="";
			
			while(fm.stringWidth(s1)>(x2)*0.8)s1=s1.substring(0, s1.length()-1);
			
			Color col=g2d.getColor();
			g2d.setColor(textpick(col));

			
			int x3=x1+x2/2-fm.stringWidth(s1)/2;
			g2d.drawString(s1, x3, hei/2+fm.getAscent()/2);
			
		}
		
	}
	
	
	public void setTitles(String...tit) {
		for(int i=0;i<titles.length;++i) {
			titles[i]=tit[i];
		}
	}
	
	public void setColor(Color...back) {
		for(int i=0;i<titles.length;++i) {
			backgrounds[i]=back[i];
		}
	}
	
	public void setEnabled(boolean b) {
		action=b;
	}
	public void setSelected(int selected) {
		this.selected=selected;
		repaint();
	}
	class MouseAction implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if(action==false) {
				e.consume();
				return;
			}
			Point p=e.getPoint();
			int n= p.x/(e.getComponent().getWidth()/nelements);

			if(((CButton)e.getComponent()).getActionListeners().length!=0) {
				((CButton)e.getComponent()).getActionListeners()[0].actionPerformed(new ActionEvent(this,n,"action"));
			}
			//selected=n;
			e.consume();
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) { }

		@Override
		public void mouseEntered(MouseEvent e) { }

		@Override
		public void mouseExited(MouseEvent e) { }
		
	}
}
