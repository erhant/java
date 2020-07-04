import java.util.ArrayList;
import java.util.Random;

public class Lift extends Thread {

	public enum Direction {
	    UP, IDLE, DOWN
	}
	
	private int maxcap;
	private int curcap;
	private int maxfloor;
	private int curfloor;
	private int id;
	private boolean entry;
	private boolean halt = false;
	private boolean simulation;
	private Direction direction;
	private Building b;
	private LiftControl lcpanel;
	
	private ArrayList<Integer> goesUps = new ArrayList<Integer>(); // 1 means a guy at 1st floor wants to go up
	private ArrayList<Integer> goesDowns = new ArrayList<Integer>(); // 2 means a guy at 2nd floor wants to go down
	private ArrayList<Integer> wantsOuts = new ArrayList<Integer>(); // 5 means a guy wants to exit at 5th floor
	
	
	public Lift(int id, int maxcap, int maxfloor,Building b,LiftControl lcpanel, boolean simulation) {
		this.simulation = simulation;
		this.id = id;
		this.lcpanel = lcpanel;
		this.maxcap = maxcap;
		this.curcap = 0;
		this.maxfloor = maxfloor;
		this.curfloor = 0;
		this.direction = Direction.IDLE;
		this.b = b;
		start();
	}
	
	public void createDestination(int floorno) {
		wantsOuts.add(floorno);
		lcpanel.updateDestinations(wantsOuts.toString());			//b.updateDestinations(" TO -> " + wantsOuts.toString());
	}
	
	public void createRequest( boolean UP, int floorno ) {
		
		if ( UP ) {
			goesUps.add(floorno);
			//System.out.println("Adding UP at " + floorno );
		}
		else {
			goesDowns.add(floorno);
			//System.out.println("Adding DOWN at " + floorno );
		}
		lcpanel.updateRequests(goesUps.toString(), goesDowns.toString());			//b.updateRequests(" UP -> " + goesUps.toString() + "\n---\n" + " DOWN -> " + goesDowns.toString());
	}

	public void decideNextMove() {		
		switch ( this.direction ) {
			case UP :
				this.thisWhenGoingUp();
				break;
			case IDLE :
				this.thisWhenIdle();
				break;
			case DOWN : 
				this.thisWhenGoingDown();
				break;
		}
		// now the direction value is changed, change the floor accordingly
		switch ( this.direction ) {
			case UP :
				this.curfloor++;
				break;
			case IDLE :
				// do nothing
				break;
			case DOWN : 
				this.curfloor--;
				break;
		}
	}
	
	public void thisWhenIdle() {
		// see if there are any requests anywhere, go to the closest one without changing direction
		
		int ABOVE=0, BELOW=0, i;
		i=curfloor;
		while ( i<=maxfloor && !goesDowns.contains(i) && !goesUps.contains(i) ) {
			i++;
		}
		if ( i<=maxfloor ) {
			ABOVE=i-curfloor;
		}
		i=curfloor;
		while ( i>=0 && !goesDowns.contains(i) && !goesUps.contains(i) ) {
			i--;
		}
		if ( i>=0 ) {
			BELOW=curfloor-i;
		}
//		System.out.println("ABOVE = " + ABOVE + " , BELOW = " + BELOW);
		/*
		 * Now there are few cases
		 * A = 0, B = 0 -> NO REQUESTS		-> A+B = 0, ELSE
		 * A = X, B = 0 -> REQUEST ABOVE	-> A*B = 0
		 * A = 0, B = X -> REQUEST BELOW	-> A*B = 0 , ELSE
		 * A = X, B = Y -> CHOOSE CLOSEST	-> CHOOSE CLOSEST
		 */
		if ( !(ABOVE + BELOW == 0) ) {
			if ( ABOVE * BELOW == 0 ) {
				if ( ABOVE!=0 ) {
					this.curfloor++;
				}
				else {
					this.curfloor--;
				}
			}
			else {
				if ( ABOVE > BELOW ) {
					this.curfloor--;
				}
				else {
					this.curfloor++;
				}
			}
		}
	}
	
	public void thisWhenGoingUp() {
		// Check if someone above wants to go up, else stop
		boolean request = false;
		for ( int i=this.curfloor; i<=this.maxfloor; i++ ) {
			if ( wantsOuts.contains(i) ) {
				request=true;
			}
		}
		if ( request ) {
			this.direction = Direction.UP;
		}
		else {
			this.direction = Direction.IDLE;
		}
	}
	
	public void thisWhenGoingDown() {
		// Check if someone below wants to go down, else stop
		boolean request = false;
		for ( int i=this.curfloor; i>=0; i-- ) {
			if ( wantsOuts.contains(i) ) {
				request=true;
			}
		}
		if ( request ) {
			this.direction = Direction.DOWN;
		}
		else {
			this.direction = Direction.IDLE;
		}
	}
	
	public void dumpPeople() {
		// dump everyone that wanted to go to this floor
		//System.out.println("DUMPING PEOPLE");
		while ( wantsOuts.contains(this.curfloor) ) {
			this.curcap--;
			wantsOuts.remove((Object)this.curfloor);
		}
		lcpanel.updateDestinations(wantsOuts.toString());		//b.updateDestinations(" TO -> " + wantsOuts.toString());
	}
	
	public void askWhereToGo(Direction d) {
		this.direction = d;
		this.entry=false;
		if ( d == Direction.UP ) {
			lcpanel.enableEveryButtonAccordingly(true, curfloor);//b.enableDestinationMenu(curfloor, true);
		}
		else {
			lcpanel.enableEveryButtonAccordingly(false, curfloor);//b.enableDestinationMenu(curfloor, false);
		}
		while ( !entry ) { // waiting for entry to be made
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void takePeople() {
		// take people that want to go the same direction or when idle and on the same floor
		switch ( this.direction ) {
			case UP :
				while ( goesUps.contains(this.curfloor) ) {
					if ( this.curcap==this.maxcap ) {
						try {
							throw ( new ElevatorOverloadException() );
						} catch (ElevatorOverloadException e) {
							// TODO Auto-generated catch block
							break; // exit loop
						}
					} else {
						this.curcap++;
						goesUps.remove((Object)this.curfloor);	
						if ( !simulation ) {
							this.askWhereToGo(this.direction);
						}
						else {
							this.createRandomDestination(this.direction);
						}
						
					}				
				}
				break;
			case IDLE :
				if ( goesUps.contains(this.curfloor) ) {
					while ( goesUps.contains(this.curfloor) ) {
						if ( this.curcap==this.maxcap ) {
							try {
								throw ( new ElevatorOverloadException() );
							} catch (ElevatorOverloadException e) {
								// TODO Auto-generated catch block
								break; // exit loop
							}
						} else {
							this.curcap++;
							goesUps.remove((Object)this.curfloor);
							if ( !simulation ) {
								this.askWhereToGo(Direction.UP);
							}
							else {
								this.createRandomDestination(Direction.UP);
							}
						}		
					}
				}
				else {
					if ( goesDowns.contains(this.curfloor) ) {
						while ( goesDowns.contains(this.curfloor) ) {
							if ( this.curcap==this.maxcap ) {
								try {
									throw ( new ElevatorOverloadException() );
								} catch (ElevatorOverloadException e) {
									// TODO Auto-generated catch block
									break; // exit loop
								}
							} else {
								this.curcap++;
								goesDowns.remove((Object)this.curfloor);	
								if ( !simulation ) {
									this.askWhereToGo(Direction.DOWN);
								}
								else {
									this.createRandomDestination(Direction.DOWN);
								}
							}
						}
					}
				}
				break;
			case DOWN : 
				while ( goesDowns.contains(this.curfloor) ) {
					if ( this.curcap==this.maxcap ) {
						try {
							throw ( new ElevatorOverloadException() );
						} catch (ElevatorOverloadException e) {
							// TODO Auto-generated catch block
							break; // exit loop
						}
					} else {
						this.curcap++;
						goesDowns.remove((Object)this.curfloor);	
						if ( !simulation ) {
							this.askWhereToGo(this.direction);
						}
						else {
							this.createRandomDestination(this.direction);
						}
					}		
				}
				break;
		}
		lcpanel.updateRequests(goesUps.toString(), goesDowns.toString());	//b.updateRequests(" UP -> " + goesUps.toString() + "\n ---\n" + " DOWN -> " + goesDowns.toString());
	}
	
	public void createRandomDestination(Direction d) {
		Random r = new Random();
		this.direction = d;
		if ( d == Direction.UP ) { // UP
			int a = r.nextInt(maxfloor-curfloor+1)+curfloor;
			this.createDestination(a);
		}
		else { // DOWN
			int a = r.nextInt(curfloor);
			this.createDestination(a);
		}
		
	}

	
	public int getCurrentFloor() {
		return this.curfloor;
	}
	
	public void printRequests() {
		System.out.println(this.goesDowns + "\n" + this.goesUps + "\n --> " + this.wantsOuts);
	}
	
	public void setEntry(boolean t) {
		this.entry = t;
	}
	
	public boolean getEntry() {
		return this.entry;
	}
	
	public void run() {
		while ( true ) {
			try {
				sleep(750);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if ( !this.halt ) {
				
					dumpPeople(); // if there is any
					takePeople(); // if there is any
					decideNextMove();
					b.updateLiftPositions(id, curfloor);	//b.updateLiftPosition(this.curfloor);
					lcpanel.updateLiftStatus(curcap, maxcap, curfloor, this.direction.toString());		//b.updateLiftInfo(" Floor = " + curfloor + "\n Capacity = " + curcap + " / " + maxcap + "\n Direction = " + this.direction.toString() );
				
			}
			else {
				lcpanel.updateLiftStatus(curcap, maxcap, curfloor, "SYSTEM HALT");	//b.updateLiftInfo(" Floor = SYSTEM HALT\n Capacity = SYSTEM HALT \n Direction = SYSTEM HALT");
			}
		}
	}
	
	public void emergencyDump() {
		while ( !wantsOuts.isEmpty() ) {
			this.curcap--;
			wantsOuts.remove(0);
		}
		lcpanel.updateDestinations(wantsOuts.toString());		//b.updateDestinations(" TO -> " + wantsOuts.toString());
	}
	
	public void toggleHaltLift() {
		if ( this.halt ) {
			this.halt = false;
		}
		else {
			this.halt = true;			
		}
		
	}
	
}

