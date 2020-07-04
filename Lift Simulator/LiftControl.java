import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

// CONTROL PANEL OF A LIFT
public class LiftControl extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073204929782329946L;
	private int maxfloor;
	private Lift l;
	private JTextArea liftStatus,
					   destinations,
					   requests;
	
	private JButton[] floors;
	private boolean simulation;
	
	public LiftControl(String liftID, int maxfloor, boolean simulation ) {
		int 	WIDTH = 300,
				HEIGHT = 600,
				XOFFSET = 500,
				YOFFSET = 0;
		
		this.maxfloor = maxfloor;
		this.simulation = simulation;
		this.setBounds(XOFFSET, YOFFSET, WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		this.setTitle(liftID);
		getContentPane().setLayout(null);
		getContentPane().setBackground(SystemColor.LIGHT_GRAY);
		
		/*
		 *  |---------------------------|
		 *  |							|
		 *  |	LIFT STATUS HERE		|
		 *  |							|
		 *  |							|
		 *  |	6	5	4		EMGSTOP	|
		 *  |							|
		 *  |	3	...			SYSHALT	|
		 *  |							|
		 *  |---------------------------|
		 * 
		 */
		
		this.liftStatus = new JTextArea();
		liftStatus.setBackground(SystemColor.black);
		liftStatus.setForeground(SystemColor.green);
		liftStatus.setFont(new Font("Cambria Math", Font.BOLD, 12));
		liftStatus.setBounds(20,20,WIDTH-50,100);
		liftStatus.setEditable(false);
		getContentPane().add(liftStatus);		
		
		this.destinations = new JTextArea();
		destinations.setBackground(SystemColor.black);
		destinations.setForeground(SystemColor.green);
		destinations.setFont(new Font("Cambria Math", Font.BOLD, 12));
		destinations.setBounds(20,120,(WIDTH-50)/2,80);
		destinations.setEditable(false);
		destinations.setLineWrap(true);
		getContentPane().add(destinations);		 
		
		this.requests = new JTextArea();
		requests.setBackground(SystemColor.black);
		requests.setForeground(SystemColor.green);
		requests.setFont(new Font("Cambria Math", Font.BOLD, 12));
		requests.setBounds(20+(WIDTH-50)/2,120,(WIDTH-50)/2,80);
		requests.setEditable(false);
		requests.setLineWrap(true);
		getContentPane().add(requests);		
		
		this.floors = new JButton[maxfloor+1];
		Integer i = maxfloor;
		int floorxoff = 40,
			flooryoff = 230,
			floorw = 50,
			floorh = 20;
		while ( i>=0 ) {
			floors[i] = new JButton(i.toString());
			if ( floorxoff < 180 ) {
				floors[i].setBounds(floorxoff, flooryoff, floorw, floorh);
				floors[i].setVisible(true);
				if ( simulation ) {
					floors[i].setEnabled(false);
				}
				else {
					floors[i].setEnabled(true);
				}
				int a = i;
				floors[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						l.createDestination(a);
						l.setEntry(true);
						disableEveryButton();
					}
				});
				getContentPane().add(floors[i]);
				floorxoff += floorw + 10;
				i--;	
			}
			else {
				floorxoff = 40;
				flooryoff += floorh + 10;
			}					
		}
		
		JLabel emergencylbl = new JLabel();
		emergencylbl.setText("EMERGENCY BUTTON");
		emergencylbl.setBounds(40, flooryoff+40, WIDTH-60, 20);
		emergencylbl.setForeground(SystemColor.black);
		getContentPane().add(emergencylbl);
		
		JButton emergencybut = new JButton(); 
		emergencybut.setBounds(WIDTH - 90, flooryoff+40, 20, 20);
		emergencybut.setBackground(new Color(170, 0, 0));
		emergencybut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				l.emergencyDump();
			}
		});
		getContentPane().add(emergencybut);
		
		JLabel haltlbl = new JLabel();
		haltlbl.setText("SYSTEM HALT BUTTON");
		haltlbl.setBounds(40, flooryoff+80, WIDTH-60, 20);
		haltlbl.setForeground(SystemColor.black);
		getContentPane().add(haltlbl);
		
		JButton haltbut = new JButton(); 
		haltbut.setBounds(WIDTH - 90, flooryoff+80, 20, 20);
		haltbut.setBackground(new Color(0, 170, 0));
		haltbut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				l.toggleHaltLift();
			}
		});
		getContentPane().add(haltbut);
		this.disableEveryButton();
		getContentPane().repaint();
	}
	
	public void updateLiftStatus(int curcap, int maxcap, int curfloor, String direction) {
		liftStatus.setText("\tLIFT STATUS:\n - Capacity : " + curcap + " / " + maxcap + "\n - Current floor : " + curfloor + "\n - Direction : " + direction );
	}
	
	public void updateRequests(String upreq, String downreq) {
		requests.setText("   -REQUESTS-\n UP -> " + upreq + "\n --- \n DOWN -> " + downreq);
	}
	
	public void updateDestinations(String dest) {
		destinations.setText("  -DESTINATIONS-\n TO -> " + dest);
	}
	
	public void setLift(Lift l) {
		this.l = l;
	}
	
	public void disableEveryButton() {
		for ( int i=0; i<=maxfloor; i++ ) {
			floors[i].setEnabled(false);
		}
	}
	
	public void enableEveryButtonAccordingly(boolean UP, int floor) {
		if ( UP ) {
			for ( int i=floor+1; i<=maxfloor; i++ ) {
				floors[i].setEnabled(true);
			}
		}
		else {
			for ( int i=floor-1; i>=0; i-- ) {
				floors[i].setEnabled(true);
			}
		}
	}
	
	
}
