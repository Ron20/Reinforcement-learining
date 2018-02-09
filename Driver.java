
/* 
 * Author: Ronith Muttur
 */

public class Driver {
	
	public static void main(String[] args) {
		// Create an instance of the world
		World w = new World();

		// Create an instance of Tim to navigate
		// in the world 
		Tim t = new Tim(w);
		
		double i = 1;
		
		while(true) {
			w.dropDonut(); 			// Drop Donut				
		
			t.move(w);				// Move Tim
			
			if(i == 200 * 1000000) { 			// Break after 200 Mil iterations
				System.out.println("\n \n");
				t.printPolicy();
				t.printQ();
				break;
			}
			i++;
		}
	}

}
