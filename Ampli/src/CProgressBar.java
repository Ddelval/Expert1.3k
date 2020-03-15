import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class CProgressBar extends JProgressBar {
	private Font textFont;
	private Font titleFont;
	private Font valueFont;
	private double max=100;
	private double min=0;
	private double val=0;
	private double tmpval=1;
	private boolean smooth=true;
	private Function<Double,Double> p=null;
	
	public CProgressBar(int max) {
		super();
		textFont=new Font("Arial",Font.PLAIN,14);
		this.max=max;
	}
	public CProgressBar() {
		super();
		textFont=new Font("Arial",Font.PLAIN,14);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		int width=getWidth();
		int height= getHeight();
		Graphics2D g2d=(Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(textFont);
		FontMetrics fm=g.getFontMetrics();
		g2d.setColor(new Color(0,0,0));
		g2d.setFont(textFont);
		
		g2d.drawString(String.format("%.0f", min), 1, fm.getHeight());
		String pr=String.format("%.0f", max);
		g2d.drawString(pr, width-fm.stringWidth(pr), fm.getHeight());
		
		int n=width/80-2;
		//System.out.println(n);
		for(int i=1;i<n;++i) {
			String pr1=String.format("%.0f", min+i*max/n);
			if(max<n) pr1=String.format("%.2f", min+i*max/n);
			g2d.drawString(pr1, i*width/n-fm.stringWidth(pr1)/2, fm.getHeight());
		}
		
		double d;;
		if(p!=null)	d=Math.min(p.apply(val-min)/max,1);
		else 		d=Math.min((val-min)/max,1);
		Color c0=new Color(23,135,255);
		Color c1=new Color(240,240,20);
		Color c2=new Color(255,50,50);
		
		
		int rr=0,gg=0,bb=0;
		
		if(d>0.75) {
			rr=(int)Math.round(c2.getRed()*(d-0.75)/0.25	+c1.getRed()*(1-d)/0.25);
			gg=(int)Math.round(c2.getGreen()*(d-0.75)/0.25	+c1.getGreen()*(1-d)/0.25);
			bb=(int)Math.round(c2.getBlue()*(d-0.75)/0.25	+c1.getBlue()*(1-d)/0.25);
		}
		else {
			rr=c0.getRed();
			gg=c0.getGreen();
			bb=c0.getBlue();
		}
		
		Color res=new Color(rr,gg,bb);
		
		//g2d.fillRect(1, 20, width, height-20);
		
		RoundRectangle2D rR = new RoundRectangle2D.Float(1, 20, (width-3), height-20, 7, 7);
		g2d.setClip(rR);
		
		g2d.setColor(res);
		g2d.fillRect(1, 10, (int)(Math.round((width-1)*d)), height-10);
		g2d.setClip(null);
		g2d.setColor(new Color(150,150,150));
		g2d.setStroke(new BasicStroke(2));
		g2d.draw(rR);
		
		for(int i=1;i<n;++i) {
			g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 10.0f, new float[]{2.0f, 1.0f}, 0.0f));
			g2d.drawLine(i*width/n, 20, i*width/n, height);
		}
		
		g2d.setFont(titleFont);
		//g2d.fillRect(2, 21, (width-3), height-22);
		
		
		
	}
	public Dimension getPreferredSize() {
		Dimension d=super.getPreferredSize();
		d.height+=10;
		
		return d;
	}
	public void setMax(double max) {
		this.max=max;
		
	}
	public double getMax() {
		return max;
	}
	public void setVal(double val) {
		//System.out.println("modified "+this.toString());
		if(smooth&&tmpval==-1) {
			if(this.val!=0&&Math.abs(val-this.val)>this.max*0.1) {
				tmpval=val;
				return;
			}
			
		}
		else {
			tmpval=-1;
		}
		
		this.val=val;
		
		
		
	}
	public void setFunction(Function<Double,Double> d) {
		this.p=d;
	}
	public double getVal() {
		return this.val;
	}
	public void setMin(double min) {
		this.min=min;
	}
	public void setSmooth(boolean b) {
		smooth=b;
	}

}
