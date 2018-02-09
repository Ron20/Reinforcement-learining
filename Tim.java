/*
 * Author: Ronith Muttur
 */

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

public class Tim {

	// CONSTANTS
	double ALPHA = 0.1;
	double D_REWARD = 0.8;
	int STATES = 100;
	int ACTIONS = 4;

	// GLOBALS
	private int loc_y;
	private int loc_x;
	double Q[][] = new double[STATES][ACTIONS];  // states can be translated from 1oc_x & loc_y
	int donutCount = 0;

	/*
	 * Tim Constructor which initializes Tim at some pre-defined location in the world
	 * @param The world in which Tim is being placed
	 */
	public Tim(World w) {
		
		loc_y = 3;				// Place Tinny Tim in some random cell
		loc_x = 3;				// at the start
		
		w.world[loc_x][loc_y] = 'T';

		for(int i = 0; i < 100; i++){		// Init Q - matrix when Tim is initialized
			for(int j = 0; j < 4; j++) {
				Q[i][j] = 100000;				// Init Q to large values	
			}
		}
	}
	
	/*
	 * Method to return the index of max value in the array
	 * @param Takes array as input
	 * @return returns the index, an int
	 */
	public static int getMaxInd(double ar[]) {
		double highest = -Double.MAX_VALUE;
		int highestIDx = -1;
		
		for(int i = 0; i < ar.length; i++) {
			if(ar[i] > highest) {
				highest = ar[i];
				highestIDx = i;
			}
		}
		return highestIDx;
	}
	
	/*
	 * Method to process the moves of Tinny Tim
	 * @param The world 
	 */
	public void move(World w) {
		
		int reward = 0;							// Initial reward 
		int s = loc_x * 10 + loc_y;				// Initial state
		
		int action = getMaxInd(Q[s]);			// Gets the action to be performed using super Optimistic strategy  
		
		// Process 0.82 probability of desired move -----------------
		Random rand = new Random();
		int res = rand.nextInt(100);
		int num;
		if(res >= 82) {
			num = rand.nextInt(4);
			while(num == action) {
				num = rand.nextInt(4);
			}
			action = num;
		}
		// -------------------------------------------------------------
		int temp;
		
		switch (action) {
		
		case 0:										// Case 0: Move left
			temp = loc_y - 1;
			
			if (w.world[loc_x][temp] == 'X') 
				reward -= 1;
			if(w.world[loc_x][temp] != 'X') {
				if(w.world[loc_x][loc_y] != 'D')
					w.world[loc_x][loc_y] = '\0';
				loc_y--;
			}
			break;
			
		case 1:										// Case 1: Move Top
			temp = loc_x - 1;
			if (w.world[temp][loc_y] == 'X') 
				reward -= 1;
			if(w.world[temp][loc_y] != 'X') {
				if(w.world[loc_x][loc_y] != 'D')
					w.world[loc_x][loc_y] = '\0';
				loc_x--;
			}
			break;
			
		case 2:										// Case 2: Move down
			temp = loc_x + 1;
			if (w.world[temp][loc_y] == 'X') 
				reward -= 1;
			if(w.world[temp][loc_y] != 'X') {
				if(w.world[loc_x][loc_y] != 'D')
					w.world[loc_x][loc_y] = '\0';
				loc_x++;
			}
			break;
			
		case 3:										// Case 3: Move right
			temp = loc_y + 1;
			if (w.world[loc_x][temp] == 'X') 
				reward -= 1;
			
			if(w.world[loc_x][temp] != 'X') {
				if(w.world[loc_x][loc_y] != 'D')
					w.world[loc_x][loc_y] = '\0';
				loc_y++;
			}
			break;
		}
		
		if(w.world[loc_x][loc_y] == 'D') {			// Code to deal with Tim eating a Donut
			w.isDonuntPresent = false;
			donutCount++;
			reward += 10;
		}
		
		w.world[loc_x][loc_y] = 'T';					// Update Tim's new location
		
		reward -= this.checkTile(w); 				// Check if Tim moves into a Tile Threat
		int s_ = 10 * loc_x + loc_y;					// update the state
		
		this.updatePolicy(reward, s, s_, action);	// Update the Q matrix
	}

	/*
	 * Method to calculate reward when Tim moves into a potential "Tile Threat"
	 * @param The world
	 * @return reward value depending on the probability
	 */
	public int checkTile(World w) {

		if((loc_x == 1 && loc_y == 3) || (loc_x == 2 && loc_y == 7) || (loc_x == 3 && loc_y == 4) || (loc_x == 5 && loc_y == 2) || (loc_x == 6 && loc_y == 5) || (loc_x == 6 && loc_y == 7) || (loc_x == 7 && loc_y == 2)) {
			Random coin_toss = new Random();
			int res = coin_toss.nextInt(2);
			if(res == 1) 			// Tile hit
				return 10;			// return a reward of -10
			
		}
		return 0;	// return a reward of 0 otherwise
	}
	
	/*
	 * Method to update the Q matrix after a move is made and the reward is calculated
	 * @param Takes the reward obtained, the prev state s, the current state s_ and the action taken in s 
	 */
	public void updatePolicy(int reward, int s, int s_, int a) {
		double a_ = -Double.MAX_VALUE;
		
		for(int i=0; i < 4; i++) {
			if(Q[s_][i] > a_)
				a_ = Q[s_][i];
		}
		
		Q[s][a] = Q[s][a] + ALPHA * (reward + D_REWARD * a_ - Q[s][a]); // Update the Q matrix
	}
	
	/*
	 * Method to print the Q matrix
	 */
	public void printQ() {
		NumberFormat formatter = new DecimalFormat("#.####");
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				if( i == 0 || j == 0 || i == 9 || j == 9)
					System.out.print("_.____" + "   ");
				else if( (i == 6 && j == 4) || (i == 7 && j == 4) || (i == 8 && j == 4) || (i == 4 && j == 6) || (i == 5 && j == 6) || (i == 6 && j == 6) || (i == 2 && j == 4) || (i == 2 && j == 5) || (i == 2 && j == 6) || (i == 2 && j == 2) || (i == 3 && j == 2) || (i == 4 && j == 2) )
					System.out.print("_.____" + "   ");
				else {
					double max = -Double.MAX_VALUE;
					for(int k = 0; k < 4; k++) {
						
						if(Q[10 * i + j][k] > max)
							max = Q[10 * i + j][k];
					}
					System.out.print(formatter.format(max) + "   ");
				}
			}
			System.out.print("\n\n");
		}
	}
	
	/*
	 * Method to print the policy
	 */
	public void printPolicy() {
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				if( i == 0 || j == 0 || i == 9 || j == 9)
					System.out.print("X" + "   ");
				else if( (i == 6 && j == 4) || (i == 7 && j == 4) || (i == 8 && j == 4) || (i == 4 && j == 6) || (i == 5 && j == 6) || (i == 6 && j == 6) || (i == 2 && j == 4) || (i == 2 && j == 5) || (i == 2 && j == 6) || (i == 2 && j == 2) || (i == 3 && j == 2) || (i == 4 && j == 2) )
					System.out.print("X" + "   ");				
				else {
					switch (getMaxInd(Q[10 * i + j])) {
					
					case 0:
						System.out.print("<" + "   ");
						break;
						
					case 1:
						System.out.print("^" + "   ");
						break;
						
					case 2:
						System.out.print("v" + "   ");
						break;
						
					case 3:
						System.out.print(">" + "   ");
						break;
					}
				}				
			}
			System.out.print("\n\n");
		}
	}

}
