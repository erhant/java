import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Building extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2404654201917537939L;
	private int maxfloor;
	private JLabel[] liftlabels;
	
	
		
	// CONTROL PANEL FOR MONITORING LIFTS
	public Building(String buildingName, int liftCount, int maxfloor) {
		this.maxfloor = maxfloor;
		this.setTitle(buildingName);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(0, 0, 40+50*liftCount, 30+40*maxfloor+30);
		getContentPane().setLayout(null);
		
		JLabel floor = new JLabel("FLOORS");
		floor.setFont(new Font("Cambria Math", Font.BOLD, 12));
		floor.setBounds(20,20,100,20);
		getContentPane().add(floor);
		
	    for ( Integer i=maxfloor; i>=0; i-- ) { 
	    	floor = new JLabel((i).toString());
	    	floor.setBounds(30 , (maxfloor-i)*20 + 50, 20, 20);
	    	getContentPane().add(floor);
	    }
		
		this.liftlabels = new JLabel[liftCount];
		for ( int i=0; i<liftCount; i++ ) {
			liftlabels[i] = new JLabel("L");
			liftlabels[i].setBounds(50+i*20, maxfloor*20+50, 20, 20);
			liftlabels[i].setVisible(true);
			getContentPane().add(liftlabels[i]);
		}
	    
	}
	
	public void updateLiftPositions(int index, int floor) {
		liftlabels[index].setBounds(50+index*20, (maxfloor-floor)*20+50, 20, 20);
	}
		
}
