import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;

public class ButtonStyled extends JButton {
	private Color borderColor;
	private boolean darkstyle=MainWindow.darkMode;
	private Color defaultBorder;
	
	public ButtonStyled(String s) {
		super(s);
		if(darkstyle) {
			setBackground(Color.DARK_GRAY.darker());
			setForeground(Color.WHITE);
			borderColor=getBackground();
			
		}
		else{
			setBackground(Color.white);
			setForeground(Color.black);
			borderColor=Color.LIGHT_GRAY;
		}
		
		
		defaultBorder=borderColor;
	}
	protected void paintComponent(Graphics g) {
		int wid=getWidth();
		int hei=getHeight();
		
		g.setFont(this.getFont());
		FontMetrics fm= g.getFontMetrics();
		Graphics2D g2d=(Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		
		RoundRectangle2D rR = new RoundRectangle2D.Float(1, 1, (wid-2), hei-2, 10, 10);
		g2d.setColor(getBackground());
		g2d.fill(rR);
		g2d.setColor(borderColor);
		g2d.setStroke(new BasicStroke(1));
		g2d.draw(rR);
		g2d.setColor(getForeground());
		Font f= this.getFont();
		String txt=this.getText();
		g2d.drawString(txt, wid/2-fm.stringWidth(txt)/2, hei/2+fm.getAscent()/2);
		
		
		
		
		
	}
	
	protected void processMouseEvent(MouseEvent e) {
		if(e.getID()==MouseEvent.MOUSE_ENTERED) {
			borderColor=new Color(90,150,250);
		}
		if(e.getID()==MouseEvent.MOUSE_EXITED) {
			borderColor=defaultBorder;
		}
		if(e.getID()==MouseEvent.MOUSE_PRESSED) {
			setBackground(getBackground().darker());
		}
		if(e.getID()==MouseEvent.MOUSE_RELEASED) {
			setBackground(getBackground().brighter());
		}
		repaint();
		if(this.getMouseListeners().length==0)return;
		switch(e.getID()) {
			case MouseEvent.MOUSE_CLICKED:
				this.getMouseListeners()[0].mouseClicked(e);
				break;
			case MouseEvent.MOUSE_ENTERED:
				this.getMouseListeners()[0].mouseEntered(e);
				break;
			case MouseEvent.MOUSE_PRESSED:
				this.getMouseListeners()[0].mousePressed(e);
				break;
			case MouseEvent.MOUSE_RELEASED:
				this.getMouseListeners()[0].mouseReleased(e);
				break;
			case MouseEvent.MOUSE_EXITED:
				this.getMouseListeners()[0].mouseExited(e);
				break;
		}
		
	}
}
