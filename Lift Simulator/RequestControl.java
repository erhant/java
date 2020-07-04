//import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

// CONTROL PANEL FOR CALLING LIFTS
public class RequestControl extends JFrame {
//
//	private int liftCount;
//	private int maxfloor;
//	private ArrayList<Lift> lifts = new ArrayList<Lift>();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8674932911741947154L;

	private boolean simulation;
	
	public RequestControl(int liftCount, int maxfloor, ArrayList<Lift> lifts, boolean simulation ) {		
		super("Request Control Panel");
		this.simulation = simulation;
//		this.lifts = lifts;
//		this.liftCount = liftCount;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(200, 0, 100*liftCount + 120, 40+30*maxfloor+40+30 );
		this.setVisible(true);
		this.setResizable(false);
		getContentPane().setLayout(null);
		int labelxoffset = 30;
		
		JLabel floor = new JLabel("FLOORS");
		floor.setFont(new Font("Cambria Math", Font.BOLD, 12));
		floor.setBounds(labelxoffset/2,20,100,20);
		getContentPane().add(floor);
		
		
		
	    JLabel[] floors = new JLabel[maxfloor+1];
	    for ( Integer i=maxfloor; i>=0; i-- ) { 
	    	floors[i] = new JLabel((i).toString());
	    	floors[i].setBounds(labelxoffset , (maxfloor-i)*20 + 50, 20, 20);
	    	getContentPane().add(floors[i]);
	    }
	    
	    JButton b;
	    JLabel lift;
	    for ( int i=0; i<liftCount; i++ ) {
	    	lift = new JLabel("LIFT " + (i+1));
	    	lift.setFont(new Font("Cambria Math", Font.BOLD, 12));
	    	lift.setBounds(85+i*100,20,100,20);
			getContentPane().add(lift);
	    	for ( int j=maxfloor; j>=0; j-- ) {
	    		int ai = i;
		    	int aj = j;
	    		b = new JButton("v");
		    	b.setBounds(labelxoffset+20+100*i, (maxfloor-j)*20 + 50, 45, 20);
		    	b.setBackground(SystemColor.menu);
		    	if ( simulation ) {
		    		b.setEnabled(false);	
		    	}
		    	else {
		    		b.setEnabled(true);	
		    	}		    		    	
		    	b.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						lifts.get(ai).createRequest(false, aj);
					}		    		
		    	});
		    	getContentPane().add(b);
		    	if ( b.getText()=="v" && j==0 ) { // bad coding :/
		    		b.setVisible(false);
		    	}
		    	b = new JButton("^");
		    	b.setBounds(labelxoffset+20+50+100*i, (maxfloor-j)*20 + 50, 45, 20);
		    	b.setBackground(SystemColor.menu);
		    	if ( simulation ) {
		    		b.setEnabled(false);	
		    	}
		    	else {
		    		b.setEnabled(true);	
		    	}	
		    	b.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						lifts.get(ai).createRequest(true, aj);
					}		    		
		    	});
		    	if ( b.getText()=="^" && j==maxfloor ) { // clumsy coding again :(
		    		b.setVisible(false);
		    	}
		    	getContentPane().add(b);

	    	}
	    	
	    }
	    
	    if ( simulation ) {
	    	JButton simul = new JButton("RANDOM");
	    	Random r = new Random();
	    	simul.setBounds(labelxoffset , 30+maxfloor*20 + 50, 100, 20);
	    	simul.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					boolean up = r.nextBoolean();
					int floorno = r.nextInt(maxfloor+1);
					if ( ( up && floorno == maxfloor ) || ( !up && floorno == 0 ) ) {
						this.actionPerformed(null); // act like press again
					}
					else {
						lifts.get(r.nextInt(liftCount)).createRequest(up, floorno);;
					}
					
					
				}
	    		
	    	});
	    	getContentPane().add(simul);
	    }
	    
	    
	    getContentPane().repaint();
	}
	
}
