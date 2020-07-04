import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LoginMenu extends JFrame {

	private byte done = 0;
	private boolean simulation;
	public LoginMenu() {
		super("Launcher");
		setSize(200,220);
		setResizable(false);
		setLayout(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel welcome = new JLabel("Welcome");
		welcome.setBounds(10,5,90,20);
		getContentPane().add(welcome);
		
		JTextField liftcount = new JTextField();
		liftcount.setBounds(10, 30, 20, 20);
		JTextField maxfloor = new JTextField();
		maxfloor.setBounds(10, 60, 20, 20);
		JTextField liftcap = new JTextField();
		liftcap.setBounds(10, 90, 20, 20);
		getContentPane().add(liftcount);
		getContentPane().add(maxfloor);
		getContentPane().add(liftcap);		
		
		JLabel lco = new JLabel("Lift Count");
		lco.setBounds(40,30,90,20);		
		JLabel mfl = new JLabel("Max Floor");
		mfl.setBounds(40, 60, 90, 20);
		JLabel lca = new JLabel("Lift Capacity");
		lca.setBounds(40, 90, 90, 20);
		getContentPane().add(lco);
		getContentPane().add(lca);
		getContentPane().add(mfl);
		
		JRadioButton simul = new JRadioButton("Simulation");
		simul.setBounds(10,120,90,20);
		getContentPane().add(simul);
		
		JButton acceptb = new JButton("Start");
		acceptb.setBounds(10, 150, 90, 20);
		acceptb.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int lifco = 0, lifca = 0, maxfl = 0;
				try {
					liftcount.setBackground(new Color(255, 255, 255));
					lifco = Integer.parseInt(liftcount.getText());
					done++;
				}
				catch(NumberFormatException ev) {
					done=0;
					liftcount.setBackground(new Color(255, 51, 51));
				}
				try {
					maxfloor.setBackground(new Color(255, 255, 255));
					maxfl = Integer.parseInt(maxfloor.getText());		
					done++;
				}
				catch(NumberFormatException ed) {
					done=0;
					maxfloor.setBackground(new Color(255, 51, 51));
				}
				try {
					liftcap.setBackground(new Color(255, 255, 255));
					lifca = Integer.parseInt(liftcap.getText());
					done++;
				}
				catch(NumberFormatException ef) {
					done=0;
					liftcap.setBackground(new Color(255, 51, 51));
				}
				if ( done==3 ) {
					simulation = simul.isSelected();
					ArrayList<LiftControl> liftControls = new ArrayList<LiftControl>(lifco); // control panels
					ArrayList<Lift> lifts = new ArrayList<Lift>(lifco); // actual lifts		
					Building b = new Building("MyBuilding", lifco, maxfl);					
					for ( Integer i=0; i<lifco; i++) {
						Integer x = i+1;
						liftControls.add(new LiftControl("LIFT "+x.toString(), maxfl, simulation));
						lifts.add(new Lift(i, lifca, maxfl, b, liftControls.get(i), simulation));
						liftControls.get(i).setLift(lifts.get(i));
					}
					new RequestControl(lifco, maxfl, lifts, simulation);
					dispose();
				}				
			}
			
		});
		getContentPane().add(acceptb);
		
		getContentPane().repaint();
	}
	
	public static void main(String []args) {
		LoginMenu lm = new LoginMenu();
	}
}
