
/*
 * Author: Ronith Muttur
 */

import java.util.Random;

public class World {
	
	// GLOBALS
	char world[][];
	boolean isDonuntPresent;
	
	/*
	 * World constructor
	 */
	public World() {
		
		world = new char[10][10]; 				// Initialize 10 X 10 grid
		isDonuntPresent = false;					// Donut not present initially
		
		
		// Mark boundaries
		
		for (int i = 0; i < world[0].length; i++) {
			world[0][i] = 'X';
		}
		
		for (int i = 0; i < world[0].length; i++) {
			world[9][i] = 'X';
		}
		
		for (int i = 0; i < world[0].length; i++) {
			world[i][0] = 'X';
		}
		
		for (int i = 0; i < world[0].length; i++) {
			world[i][9] = 'X';
		}
		
		//Place walls
		
		world[6][4] = 'X';		world[4][6] = 'X';		world[2][4] = 'X';		world[2][2] = 'X';
		world[7][4] = 'X';		world[5][6] = 'X';		world[2][5] = 'X';		world[3][2] = 'X';
		world[8][4] = 'X';		world[6][6] = 'X';		world[2][6] = 'X';		world[4][2] = 'X';
		
	}
	
	/*
	 * Method to process the donut dropping mechanism 
	 */
	public void dropDonut() {
		
		if(isDonuntPresent) 
			return;
		
		Random rand = new Random();
		int shouldRenderDonut = rand.nextInt(4);
		
		if(shouldRenderDonut == 1) {
	
			int pos = rand.nextInt(4);		// Place donut in one of the four cells 

			switch (pos) {
			case 0:
				world[1][1] = 'D';
				break;
				
			case 1:
				world[1][8] = 'D';
				break;
				
			case 2:
				world[8][1] = 'D';
				break;
				
			case 3:
				world[8][8] = 'D';
				break;
				
			}
			
			isDonuntPresent = true;
		}
	}
	
	/*
	 * Method to draw world
	 */
	public void render() {
		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if(world[i][j] == '\0')
					System.out.print(" ");
				System.out.print(world[i][j] + "   ");
			}
			System.out.print("\n\n");
		}
	}
}
