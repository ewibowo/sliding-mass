/**
 * @(#)BlockMassSlide.java
 *
 * BlockMassSlide Applet application
 *
 * @author Edi Wibowo
 * @version 1.00 2010/5/6
 */
 
import java.awt.*;
import java.applet.*;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.math.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;	

public class BlockMassSlide extends JApplet implements ChangeListener {
	private SlideCanvas cnsSlide;
	private JLabel lblAngle;
	private int intAngle;
	private final int initAngle = 20;

	public void init() {
		setLayout(new BorderLayout());

		// Canvas panel to draw a block of mass on an inclined plane
		cnsSlide = new SlideCanvas(initAngle);
		lblAngle = new JLabel(String.valueOf(initAngle));
		add(cnsSlide, BorderLayout.CENTER);

		// Control panel to change the sliding angle
		Panel pnlControl = new Panel();
		pnlControl.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblChangeAngle = new JLabel();
		lblChangeAngle.setText("Sliding Angle");
		pnlControl.add(lblChangeAngle);      
		// Sliding angle range: 0-90 degrees with init value initAngle
		JSlider angleSlider = new JSlider(JSlider.VERTICAL,0, 90, initAngle);
        	angleSlider.setMinorTickSpacing(1);
        	angleSlider.setMajorTickSpacing(10);
        	angleSlider.setPaintTicks(true);
        	angleSlider.setPaintLabels(true);
        	angleSlider.addChangeListener(this);
		pnlControl.add(angleSlider);  
		add(pnlControl, BorderLayout.WEST);

	}

    public void stateChanged(ChangeEvent e) {
        JSlider slider = (JSlider)e.getSource();
        cnsSlide.setBaseAngle(slider.getValue());
        lblAngle.setText(String.valueOf(slider.getValue()));
        cnsSlide.repaint();
    }

	public void paint(Graphics g) {
		// paint method is delegated to SlideCanvas
	}

}

class SlideCanvas extends Canvas {
	private int baseAngle;
	double baseAngleRad;
	// Coordinates for the block of mass and its inclined platform
	private double p[] = {	
               40.0,20.0,	
               40.0,-20.0,	
               -40.0,-20.0,	
               -40.0,20.0,
               140.0,20.0 };
    // Coordinates for the gravitational force                       
	private double fg[] = {
				0.0,0.0,
				0.0,80.0,
				10.0,70.0
				};
	// Coordinates for the effective force			
	private double fe[] = {
				0.0,0.0,
				80.0,0.0,
				70.0,-10.0
				};
				
	// Constructor
	public SlideCanvas(int initAngle) {
			// Initialise the sliding angle
			setBaseAngle(initAngle);
	}

	// Set the sliding angle
	public void setBaseAngle(int baseAngle) {
			this.baseAngle = baseAngle;
			baseAngleRad = (double) baseAngle/180*Math.PI;
	}

	// Draw the block of mass, platform and forces
	public void paint(Graphics g) {
		// Transformed coordinates
		double pa[] = new double[p.length];	
		double fga[] = new double[fg.length];
		double fea[] = new double[fe.length];
	
    	AffineTransform at = new AffineTransform();	
    	
    	// Move the position of the coordinates 
    	at.setToTranslation(100.0,70.0);	
    	at.transform(p,0,pa,0,p.length/2);	
    	at.transform(fg,0,fga,0,fg.length/2);
    	at.transform(fe,0,fea,0,fe.length/2);
    
    	// Rotate the block of mass and its platform depending on the sliding angle
    	at.rotate(baseAngleRad);	
    	at.transform(p,0,pa,0,p.length/2);
    	at.transform(fe,0,fea,0,fe.length/2);
    
    	// Change the length of effective force
    	at.scale(Math.sin(baseAngleRad),Math.sin(baseAngleRad));
    	at.transform(fe,0,fea,0,fe.length/2);
    
    	// Draw the block of mass
    	for(int i=3; i< pa.length; i+=2)	
               g.drawLine((int)pa[i-3],(int)pa[i-2],	
                    (int)pa[i-1],(int)pa[i]);
    
    	// Draw the gravitational force
        g.setColor(Color.BLUE);
    	for(int i=3; i< fga.length; i+=2)	
               g.drawLine((int)fga[i-3],(int)fga[i-2],	
                    (int)fga[i-1],(int)fga[i]);
        
        // Draw the effective force        
		g.setColor(Color.RED);
    	for(int i=3; i< fea.length; i+=2)	
               g.drawLine((int)fea[i-3],(int)fea[i-2],	
                    (int)fea[i-1],(int)fea[i]);
    	
    	// Draw a perpendicular line from the effective force
    	// to show that the effective is the sinus component 
    	// of the gravitational force
    	g.setColor(Color.GRAY);        
    	g.drawLine((int)fea[2],(int)fea[3],(int)fga[2],(int)fga[3]);
    
    	// Draw the base of the platform
    	g.setColor(Color.BLACK);        
    	g.drawLine((int)pa[6],(int)pa[9],(int)pa[8],(int)pa[9]);
    	
    	// Draw the value of sliding angle
    	g.drawString(String.valueOf(baseAngle)+'\u00b0',(int)(pa[8]+10),(int)(pa[9]+10));

}

}
