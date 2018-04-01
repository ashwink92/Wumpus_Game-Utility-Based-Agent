/*
 * Class that defines the agent function.
 *
 * Written by James P. Biagioni (jbiagi1@uic.edu)
 * for CS511 Artificial Intelligence II
 * at The University of Illinois at Chicago
 *
 * Last modified 2/19/07
 *
 * DISCLAIMER:
 * Elements of this application were borrowed from
 * the client-server implementation of the Wumpus
 * World Simulator written by Kruti Mehta at
 * The University of Texas at Arlington.
 *
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

@SuppressWarnings("ALL")
class AgentFunction {

	// string to store the agent's name
	// do not remove this variable
	private String agentName = "Agent Heisenberg";
	int agentPositionX;
	int agentPositionY;
	int initialhurdle1;
	char direction;
	wumpusworld wumpusworldobj;
	// all of these variables are created and used
	// for illustration purposes; you may delete them
	// when implementing your own intelligent agent
	private int[] actionTable;
	private boolean bump;
	private boolean glitter;
	private boolean breeze;
	private boolean stench;
	private boolean scream;
	private Random rand;

	public AgentFunction()
	{
		// for illustration purposes; you may delete all code
		// inside this constructor when implementing your
		// own intelligent agent

		// this integer array will store the agent actions
		agentPositionX = 0;
		agentPositionY = 0;
		direction = 'E';
		initialhurdle1 = 0;
		actionTable = new int[6];
		actionTable[0] = Action.GO_FORWARD;
		actionTable[1] = Action.TURN_RIGHT;
		actionTable[2] = Action.TURN_LEFT;
		actionTable[3] = Action.SHOOT;
		actionTable[4] = Action.GRAB;
		actionTable[5] = Action.NO_OP;
		wumpusworldobj = new wumpusworld();
		// new random number generator, for
		// randomly picking actions to execute
		rand = new Random();
		// new random number generator, for
		// randomly picking actions to execute
		//rand = new Random();
	}

	public int process(TransferPercept tp) {
		// To build your own intelligent agent, replace
		// all code below this comment block. You have
		// access to all percepts through the object
		// 'tp' as illustrated here:
		// read in the current percepts
		bump = tp.getBump();
		glitter = tp.getGlitter();
		breeze = tp.getBreeze();
		stench = tp.getStench();
		scream = tp.getScream();

		// To reduce cases of being stuck at initial position
		if(agentPositionX == 0 && agentPositionY == 0 && stench == true && breeze == false && glitter == false) {
			if(direction == 'E' && initialhurdle1 == 0) {
				initialhurdle1+=1;
				return actionTable[3];
			} else if(initialhurdle1 == 1) {
				wumpusworldobj.updatewumpusworld(agentPositionX, agentPositionY, 6);
				return actionTable[nextmove(wumpusworldobj)];
			}
		}
		if (glitter == true) {
			return actionTable[4];
		} else if (stench == false && breeze == false && scream == false) {
			wumpusworldobj.updatewumpusworld(agentPositionX, agentPositionY, 0);
			return actionTable[nextmove(wumpusworldobj)];
		} else if (stench == true && breeze == true) {
			wumpusworldobj.updatewumpusworld(agentPositionX, agentPositionY, 1);
			return actionTable[nextmove(wumpusworldobj)];
		} else if (stench == true && breeze == false) {
			wumpusworldobj.updatewumpusworld(agentPositionX, agentPositionY, 2);
			return actionTable[nextmove(wumpusworldobj)];
		} else if (stench == false && breeze == true && scream == false) {
			wumpusworldobj.updatewumpusworld(agentPositionX, agentPositionY, 3);
			return actionTable[nextmove(wumpusworldobj)];
		} else if (breeze == true && scream == true) {
			wumpusworldobj.updatewumpusworld(agentPositionX, agentPositionY, 4);
			return actionTable[nextmove(wumpusworldobj)];
		} else if (breeze == false && scream == true) {
			wumpusworldobj.updatewumpusworld(agentPositionX, agentPositionY, 5);
			return actionTable[nextmove(wumpusworldobj)];
		} else {
			return actionTable[nextmove(wumpusworldobj)];
		}
	}

    // Based on the current values of the model cells - determine the most viable cell destination
	// Based on model cells identify possible cells that the agent can go to
	// Then call gridsearch function to get the cell with the highest utility score
	int nextmove(wumpusworld wumpusworldobj) {
		List<newwumpusclass> possiblecells = new ArrayList();
		List<newwumpusclass> possibleunexploredcells = new ArrayList();
		List<newwumpusclass> possibleexploredcells = new ArrayList();
		List<newwumpusclass> possibleclosest = new ArrayList();
		List<Integer> result = new ArrayList<>();
		int destinationX = agentPositionX;
		int destinationY = agentPositionY;
		// Special cases needed to shoot the wumpus when encountered
		if (agentPositionY !=  3) {
			if (direction == 'N' && wumpusworldobj.wumpuscells[agentPositionX][agentPositionY + 1].wumpusIndicator == 1) {
				return 3;
			}
		}
		if (agentPositionY != 0) {
			if (direction == 'S' && wumpusworldobj.wumpuscells[agentPositionX][agentPositionY - 1].wumpusIndicator == 1) {
				return 3;
			}
		}
		if (agentPositionX != 3) {
			if (direction == 'E' && wumpusworldobj.wumpuscells[agentPositionX + 1][agentPositionY].wumpusIndicator == 1) {
				return 3;
			}
		}
		if (agentPositionX != 0) {
			if (direction == 'W' && wumpusworldobj.wumpuscells[agentPositionX - 1][agentPositionY].wumpusIndicator == 1) {
				return 3;
			}
		}
		// Identifying possible cells the agent can go to
		if(agentPositionX != 0) {
			if ((wumpusworldobj.wumpuscells[agentPositionX - 1][agentPositionY].safeIndicator != -1
					|| (wumpusworldobj.wumpuscells[agentPositionX - 1][agentPositionY].wumpusIndicator == 1
						&& wumpusworldobj.wumpuscells[agentPositionX - 1][agentPositionY].pitindicator != 1))
				&& (wumpusworldobj.wumpuscells[agentPositionX - 1][agentPositionY].safeIndicator == 1
					|| (wumpusworldobj.wumpuscells[agentPositionX - 1][agentPositionY].wumpusprobaility == 0
					&& wumpusworldobj.wumpuscells[agentPositionX - 1][agentPositionY].pitprobability == 0) )) {
				newwumpusclass newclass = new newwumpusclass();
				newclass.cell = wumpusworldobj.wumpuscells[agentPositionX - 1][agentPositionY];
				newclass.x = agentPositionX - 1;
				newclass.y = agentPositionY;
				possiblecells.add(newclass);
			}
		}
		if (agentPositionX != 3) {
			if ((wumpusworldobj.wumpuscells[agentPositionX + 1][agentPositionY].safeIndicator != -1
					|| (wumpusworldobj.wumpuscells[agentPositionX + 1][agentPositionY].wumpusIndicator == 1
					&& wumpusworldobj.wumpuscells[agentPositionX + 1][agentPositionY].pitindicator != 1))
					&& (wumpusworldobj.wumpuscells[agentPositionX + 1][agentPositionY].safeIndicator == 1
					|| (wumpusworldobj.wumpuscells[agentPositionX + 1][agentPositionY].wumpusprobaility == 0
					&& wumpusworldobj.wumpuscells[agentPositionX + 1][agentPositionY].pitprobability == 0) )) {
				newwumpusclass newclass1 = new newwumpusclass();
				newclass1.cell = wumpusworldobj.wumpuscells[agentPositionX + 1][agentPositionY];
				newclass1.x = agentPositionX + 1;
				newclass1.y = agentPositionY;
				possiblecells.add(newclass1);
			}
		}
		if(agentPositionY != 0) {
			if ((wumpusworldobj.wumpuscells[agentPositionX][agentPositionY - 1].safeIndicator != -1
					|| (wumpusworldobj.wumpuscells[agentPositionX][agentPositionY - 1].wumpusIndicator == 1
					&& wumpusworldobj.wumpuscells[agentPositionX][agentPositionY - 1].pitindicator != 1))
					&& (wumpusworldobj.wumpuscells[agentPositionX][agentPositionY - 1].safeIndicator == 1
					|| (wumpusworldobj.wumpuscells[agentPositionX][agentPositionY - 1].wumpusprobaility == 0
					&& wumpusworldobj.wumpuscells[agentPositionX][agentPositionY - 1].pitprobability == 0) )) {
				newwumpusclass newclass2 = new newwumpusclass();
				newclass2.cell = wumpusworldobj.wumpuscells[agentPositionX][agentPositionY - 1];
				newclass2.x = agentPositionX;
				newclass2.y = agentPositionY - 1;
				possiblecells.add(newclass2);
			}
		}
		if (agentPositionY != 3) {
			if ((wumpusworldobj.wumpuscells[agentPositionX][agentPositionY + 1].safeIndicator != -1
					|| (wumpusworldobj.wumpuscells[agentPositionX][agentPositionY + 1].wumpusIndicator == 1
					&& wumpusworldobj.wumpuscells[agentPositionX][agentPositionY + 1].pitindicator != 1))
					&& (wumpusworldobj.wumpuscells[agentPositionX][agentPositionY + 1].safeIndicator == 1
					|| (wumpusworldobj.wumpuscells[agentPositionX][agentPositionY + 1].wumpusprobaility == 0
					&& wumpusworldobj.wumpuscells[agentPositionX][agentPositionY + 1].pitprobability == 0) )) {
				newwumpusclass newclass3 = new newwumpusclass();
				newclass3.cell = wumpusworldobj.wumpuscells[agentPositionX][agentPositionY + 1];
				newclass3.x = agentPositionX;
				newclass3.y = agentPositionY + 1;
				possiblecells.add(newclass3);
			}
		}


		if (possiblecells.size() == 0) {
				destinationX =  agentPositionX;
				destinationY = agentPositionY;
		} else if (possiblecells.size() == 1) {
			for (newwumpusclass possiblecell : possiblecells) {
				destinationX = possiblecell.x;
				destinationY = possiblecell.y;
			}
		} else {
			for (newwumpusclass possiblecell : possiblecells) {
				if (possiblecell.cell.alreadyvisited == 1) {
					possibleexploredcells.add(possiblecell);
				} else {
					possibleunexploredcells.add(possiblecell);
				}
			}
			// Calling grid search function
			// It returns the best possible cell based on its utility score
			// If no cell has any utility returns the location of the current cell -indicating no op to be performed
			gridSearch search = new gridSearch();
			result =search.search(possibleexploredcells, possibleunexploredcells, agentPositionX, agentPositionY, direction, wumpusworldobj);
			destinationX = result.get(0);
			destinationY = result.get(1);
		}
		// Based on destination cell returned by gridsearch returns the appropriate action
		int check = nextMovement(destinationX, destinationY, wumpusworldobj);
		return check;
	}

	// Based on destination cell returned by gridsearch returns the appropriate action
	int nextMovement(int destinationX, int destinationY, wumpusworld wumpusworldobj) {
		if(agentPositionX < destinationX) {
			if (direction == 'E') {
				agentPositionX = destinationX;
				wumpusworldobj.wumpuscells[agentPositionX][agentPositionY].alreadyvisited = 1;
				wumpusworldobj.wumpuscells[agentPositionX][agentPositionY].safeIndicator = 1;
				return 0;
			} else if (direction == 'N') {
				direction = 'E';
				return 1;
			} else if (direction == 'S') {
				direction = 'E';
				return 2;
			} else {
				if(rand.nextInt(2) + 1 == 1) {
					direction = 'N';
					return 1;
				} else {
					direction = 'S';
					return 2;
				}
			}
		} else if(agentPositionX > destinationX) {
			if (direction == 'W') {
				agentPositionX = destinationX;
				wumpusworldobj.wumpuscells[agentPositionX][agentPositionY].alreadyvisited = 1;
				wumpusworldobj.wumpuscells[agentPositionX][agentPositionY].safeIndicator = 1;
				return 0;
			} else if (direction == 'N') {
				direction = 'W';
				return 2;
			} else if (direction == 'S') {
				direction = 'W';
				return 1;
			} else {
				if(rand.nextInt(2) + 1 == 1) {
					direction = 'S';
					return 1;
				} else {
					direction = 'N';
					return 2;
				}
			}
		} else {
			if(agentPositionY < destinationY) {
				if (direction == 'N') {
					agentPositionY = destinationY;
					wumpusworldobj.wumpuscells[agentPositionX][agentPositionY].alreadyvisited = 1;
					wumpusworldobj.wumpuscells[agentPositionX][agentPositionY].safeIndicator = 1;
					return 0;
				} else if (direction == 'W') {
					direction = 'N';
					return 1;
				} else if (direction == 'E') {
					direction = 'N';
					return 2;
				} else {
					if(rand.nextInt(2) + 1 == 1) {
						direction = 'W';
						return 1;
					} else {
						direction = 'E';
						return 2;
					}
				}
			} else if (agentPositionY > destinationY) {
				if (direction == 'S') {
					agentPositionY = destinationY;
					wumpusworldobj.wumpuscells[agentPositionX][agentPositionY].alreadyvisited = 1;
					wumpusworldobj.wumpuscells[agentPositionX][agentPositionY].safeIndicator = 1;
					return 0;
				} else if (direction == 'W') {
					direction = 'S';
					return 2;
				} else if (direction == 'E') {
					direction = 'S';
					return 1;
				} else {
					if(rand.nextInt(2) + 1 == 1) {
						direction = 'E';
						return 1;
					} else {
						direction = 'W';
						return 2;
					}
				}
			} else {
				return 5;
			}
		}
	}

	// public method to return the agent's name
	// do not remove this method
	public String getAgentName() {
		return agentName;
	}
}


class newwumpusclass {
	wumpuscell cell;
	int x;
	int y;
}

// Model of the wumpus world represented as 4 by 4 matrix with cell in the matrix having following variables
//	1. SafeIndicator - If the cell is safe or not
//  2. Pitprobability - Probability of pit being present in the cell
//  3. Wumpusprobability - Probability of wumpus being present in the cell
//  4. AlreadyVisited - If the cell has already been visited
//  5. PitChance - To indicate if there exists a pit in the cell
//  6. WumpusIndicator - To indicate if there exists a wumpus in the cell
//  7. PitIndicator - To indicate if there exists a pit in the cell
class wumpuscell {
	int safeIndicator = 0;
	float pitprobability = 0;
	float wumpusprobaility = 0;
	int alreadyvisited = 0;
	int pitchance = 1;
	int wumpusIndicator = 0;
	int pitindicator = 0;
}

// Model of the wumpus world represented as 4 by 4 matrix with cell in the matrix having following variables
//	1. SafeIndicator - If the cell is safe or not
//  2. Pitprobability - Probability of pit being present in the cell
//  3. Wumpusprobability - Probability of wumpus being present in the cell
//  4. AlreadyVisited - If the cell has already been visited
//  5. PitChance - To indicate if there exists a pit in the cell
//  6. WumpusIndicator - To indicate if there exists a wumpus in the cell

class wumpusworld {
	int wumpusidentified = 0;
	int pitidentified = 0;
	int pitx = 0;
	int pity = 0;
	int counter = 0;
	wumpuscell[][] wumpuscells = new wumpuscell[4][4];

	wumpusworld() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				wumpuscells[i][j] = new wumpuscell();
			}
		}
		wumpuscells[0][0].safeIndicator = 1;
		wumpuscells[0][0].alreadyvisited = 1;
	}

	// Finalize the presence of wumpus in the cell
	void finalizewumpus(){
		if (wumpusidentified == 0) {
			int maxpitprobability = 0;
			Stack<String> wumpusstack = new Stack();
			for (int i = 0; i <= 3; i++) {
				for (int j = 0; j <= 3; j++) {
					if (wumpuscells[i][j].wumpusprobaility > maxpitprobability  && wumpuscells[i][j].safeIndicator != 1) {
						wumpusstack.clear();
						wumpusstack.push(Integer.toString(i) + Integer.toString(j));
						maxpitprobability = (int) wumpuscells[i][j].wumpusprobaility;
					} else if (wumpuscells[i][j].wumpusprobaility == maxpitprobability  && wumpuscells[i][j].safeIndicator != 1) {
						wumpusstack.push(Integer.toString(i) + Integer.toString(j));
					}
				}
			}
			if (wumpusstack.size() == 1) {
				char position[] = wumpusstack.peek().toCharArray();
				wumpuscells[Character.getNumericValue(position[0])][Character.getNumericValue(position[1])].safeIndicator = -1;
				wumpuscells[Character.getNumericValue(position[0])][Character.getNumericValue(position[1])].wumpusIndicator = 1;
				wumpusidentified = 1;
			}
		}
		if (wumpusidentified == 1) {
			for (int i = 0; i <= 3; i++) {
				for (int j = 0; j <= 3; j++) {
					wumpuscells[i][j].wumpusprobaility = 0;
				}
			}
		}

	}

	//eliminating probability of wumpus in the cell
	void eliminatewumpus(int leftx, int rightx, int positionX, int topy, int bottomy, int positionY) {
		wumpuscells[leftx][positionY].wumpusprobaility = 0;
		wumpuscells[rightx][positionY].wumpusprobaility = 0;
		wumpuscells[positionX][topy].wumpusprobaility = 0;
		wumpuscells[positionX][bottomy].wumpusprobaility = 0;
		finalizewumpus();
	}

	// eliminating probability of pit in the cell
	void eliminatepit(int leftx, int rightx, int positionX, int topy, int bottomy, int positionY){
		wumpuscells[leftx][positionY].pitchance = 0;
		wumpuscells[rightx][positionY].pitchance = 0;
		wumpuscells[positionX][topy].pitchance = 0;
		wumpuscells[positionX][bottomy].pitchance = 0;
		wumpuscells[leftx][positionY].pitprobability = 0;
		wumpuscells[rightx][positionY].pitprobability = 0;
		wumpuscells[positionX][topy].pitprobability = 0;
		wumpuscells[positionX][bottomy].pitprobability = 0;
		finalizepit1();
	}
	// Finalize the presence of pit in the cell after elimination of pit chances in the previous cells
	void finalizepit1( ) {
		if (pitidentified < 2 ) {
			Stack<String> pitstack = new Stack();
			for(int i =0;i <4;i++) {
				for(int j = 0; j <4;j++) {
					if (wumpuscells[i][j].safeIndicator != 1 && wumpuscells[i][j].pitprobability != 0) {
						pitstack.push(Integer.toString(i) + Integer.toString(j));
					}
				}
			}
			if (pitstack.size() == 1) {
				char position[] = pitstack.peek().toCharArray();
				wumpuscells[Character.getNumericValue(position[0])][Character.getNumericValue(position[1])].safeIndicator = -1;
				wumpuscells[Character.getNumericValue(position[0])][Character.getNumericValue(position[1])].pitindicator = 1;
				wumpuscells[Character.getNumericValue(position[0])][Character.getNumericValue(position[1])].pitchance = 0;
				if(pitx == Character.getNumericValue(position[0]) && pity == Character.getNumericValue(position[1])) {

				} else {
					pitidentified += 1;
					pitx = Character.getNumericValue(position[0]);
					pity = Character.getNumericValue(position[1]);
				}
			}
		}
		if (pitidentified == 2) {
			for (int i = 0; i <= 3; i++) {
				for (int j = 0; j <= 3; j++) {
					wumpuscells[i][j].pitchance = 0;
					wumpuscells[i][j].pitprobability = 0;
				}
			}
		}

	}

	// Finalize presence of pit in the cell
	void finalizepit(int leftx, int rightx, int positionX, int topy, int bottomy, int positionY) {
			if (pitidentified < 2 ) {
				Stack<String> pitstack = new Stack();
				if ( wumpuscells[leftx][positionY].safeIndicator != 1 && wumpuscells[leftx][positionY].pitprobability != 0) {
					pitstack.push(Integer.toString(leftx) + Integer.toString(positionY));
				}
				if (wumpuscells[rightx][positionY].safeIndicator != 1 && wumpuscells[rightx][positionY].pitprobability != 0) {
					pitstack.push(Integer.toString(rightx) + Integer.toString(positionY));
				}
				if ( wumpuscells[positionX][topy].safeIndicator != 1 && wumpuscells[positionX][topy].pitprobability != 0) {
					pitstack.push(Integer.toString(positionX) + Integer.toString(topy));
				}
				if (wumpuscells[positionX][bottomy].safeIndicator != 1 && wumpuscells[positionX][bottomy].pitprobability != 0) {
					pitstack.push(Integer.toString(positionX) + Integer.toString(bottomy));
				}
				if (pitstack.size() == 1) {
					char position[] = pitstack.peek().toCharArray();
					wumpuscells[Character.getNumericValue(position[0])][Character.getNumericValue(position[1])].safeIndicator = -1;
					wumpuscells[Character.getNumericValue(position[0])][Character.getNumericValue(position[1])].pitindicator = 1;
					wumpuscells[Character.getNumericValue(position[0])][Character.getNumericValue(position[1])].pitchance = 0;
					if(pitx == Character.getNumericValue(position[0]) && pity == Character.getNumericValue(position[1])) {

					} else {
						pitidentified += 1;
						pitx = Character.getNumericValue(position[0]);
						pity = Character.getNumericValue(position[1]);
					}
				}
			}
			if (pitidentified == 2) {
				for (int i = 0; i <= 3; i++) {
					for (int j = 0; j <= 3; j++) {
						wumpuscells[i][j].pitchance = 0;
						wumpuscells[i][j].pitprobability = 0;
					}
				}
			}
		}

	// Update the wumpus world model based on the current percepts
	void updatewumpusworld(int positionX, int positionY, int option) {
		int leftx=0;
		int rightx=3;
		int bottomy=0;
		int topy=3;
		switch(option) {
			// For case 0 - No breeze or stench observed, Hence neighboring cells can be tagged as safe
			case 0:
				leftx = positionX-1 < leftx? leftx : positionX - 1;
				bottomy = positionY-1 < bottomy? bottomy : positionY - 1;
				rightx = positionX+1 > rightx? rightx : positionX + 1;
				topy = positionY+1 > topy? topy : positionY + 1;
				wumpuscells[leftx][positionY].safeIndicator = 1;
				wumpuscells[rightx][positionY].safeIndicator = 1;
				wumpuscells[positionX][topy].safeIndicator = 1;
				wumpuscells[positionX][bottomy].safeIndicator = 1;
				eliminatewumpus(leftx, rightx, positionX, topy, bottomy, positionY);
				break;
			// For case 1 - Both breeze and stench observed, Update wumpus and pit probabilities
			// Then based on Wummpus and pit Probabilities try to finalize the position of wumpus and pit positions
			// By calling finalizewumpus and finalizepit functions
			case 1:
				leftx = positionX-1 < leftx? leftx : positionX - 1;
				bottomy = positionY-1 < bottomy? bottomy : positionY - 1;
				rightx = positionX+1 > rightx? rightx : positionX + 1;
				topy = positionY+1 > topy? topy : positionY + 1;
				if (wumpuscells[leftx][positionY].pitchance == 1)
					wumpuscells[leftx][positionY].pitprobability += 1;
				if (wumpuscells[rightx][positionY].pitchance == 1)
					wumpuscells[rightx][positionY].pitprobability += 1;
				if (wumpuscells[positionX][topy].pitchance == 1)
					wumpuscells[positionX][topy].pitprobability += 1;
				if (wumpuscells[positionX][bottomy].pitchance == 1)
					wumpuscells[positionX][bottomy].pitprobability += 1;
				wumpuscells[leftx][positionY].wumpusprobaility += 1;
				wumpuscells[rightx][positionY].wumpusprobaility += 1;
				wumpuscells[positionX][topy].wumpusprobaility += 1;
				wumpuscells[positionX][bottomy].wumpusprobaility += 1;
				finalizewumpus();
				finalizepit(leftx, rightx, positionX, topy, bottomy, positionY);
				break;
			// For case 2 - Only stench observed, Update wumpus probabilities
			// Then based on Wummpus probability try to finalize the position of wumpus positions
			// By calling finalizewumpus function
			// Also since breeze not observed - eliminate possibility of breeze in neighboring cells
			// By  calling eliminatepit function
			case 2:
				leftx = positionX-1 < leftx? leftx : positionX - 1;
				bottomy = positionY-1 < bottomy? bottomy : positionY - 1;
				rightx = positionX+1 > rightx? rightx : positionX + 1;
				topy = positionY+1 > topy? topy : positionY + 1;
				wumpuscells[leftx][positionY].wumpusprobaility += 1;
				wumpuscells[rightx][positionY].wumpusprobaility += 1;
				wumpuscells[positionX][topy].wumpusprobaility += 1;
				wumpuscells[positionX][bottomy].wumpusprobaility += 1;
				finalizewumpus();
				eliminatepit(leftx, rightx, positionX, topy, bottomy, positionY);
				break;
			// For case 3 - Only stench observed, Update pit probabilities
			// Then based on Pit Probabilities try to finalize the position of pit
			// By calling finalizepit functions
			case 3:
				leftx = positionX-1 < leftx? leftx : positionX - 1;
				bottomy = positionY-1 < bottomy? bottomy : positionY - 1;
				rightx = positionX+1 > rightx? rightx : positionX + 1;
				topy = positionY+1 > topy? topy : positionY + 1;
				if (wumpuscells[leftx][positionY].pitchance == 1)
					wumpuscells[leftx][positionY].pitprobability += 1;
				if (wumpuscells[rightx][positionY].pitchance == 1)
					wumpuscells[rightx][positionY].pitprobability += 1;
				if (wumpuscells[positionX][topy].pitchance == 1)
					wumpuscells[positionX][topy].pitprobability += 1;
				if (wumpuscells[positionX][bottomy].pitchance == 1)
					wumpuscells[positionX][bottomy].pitprobability += 1;
				finalizepit(leftx, rightx, positionX, topy, bottomy, positionY);
				eliminatewumpus(leftx, rightx, positionX, topy, bottomy, positionY);
				break;
			// Case - 4: Scream Observed along with Breeze
			// Since wumpus is dead, mark all cells as wumpus free by changing wumpus probability to 0
			// Then since breeze is observed, update pit probabilities
			// and then try finalizing pit position
			// by calling finalizepit
			case 4:
				for (int i = 0; i <= 3; i++) {
					for (int j = 0; j <= 3; j++) {
						if(wumpuscells[i][j].wumpusIndicator == 1 && wumpuscells[i][j].pitindicator != 1) {
							wumpuscells[i][j].safeIndicator = 0;
						}
						wumpuscells[i][j].wumpusprobaility = 0;
						wumpuscells[i][j].wumpusIndicator = 0;
					}
				}
				leftx = positionX-1 < leftx? leftx : positionX - 1;
				bottomy = positionY-1 < bottomy? bottomy : positionY - 1;
				rightx = positionX+1 > rightx? rightx : positionX + 1;
				topy = positionY+1 > topy? topy : positionY + 1;
				if (wumpuscells[leftx][positionY].pitchance == 1)
					wumpuscells[leftx][positionY].pitprobability += 1;
				if (wumpuscells[rightx][positionY].pitchance == 1)
					wumpuscells[rightx][positionY].pitprobability += 1;
				if (wumpuscells[positionX][topy].pitchance == 1)
					wumpuscells[positionX][topy].pitprobability += 1;
				if (wumpuscells[positionX][bottomy].pitchance == 1)
					wumpuscells[positionX][bottomy].pitprobability += 1;
				finalizepit(leftx, rightx, positionX, topy, bottomy, positionY);
				break;
			// Case - 5: Only scream observed
			// Mark all cells as Wumpus free, since it is dead by making wumpus probability to 0
			// Since breeze is also not observed, mark neighboring cells as safe
			case 5:
				for (int i = 0; i <= 3; i++) {
					for (int j = 0; j <= 3; j++) {
						if(wumpuscells[i][j].wumpusIndicator == 1) {
							wumpuscells[i][j].safeIndicator = 0;
							wumpuscells[i][j].wumpusIndicator = 0;
						}
						wumpuscells[i][j].wumpusIndicator = 0;
						wumpuscells[i][j].wumpusprobaility = 0;
					}
				}
				leftx = positionX-1 < leftx? leftx : positionX - 1;
				bottomy = positionY-1 < bottomy? bottomy : positionY - 1;
				rightx = positionX+1 > rightx? rightx : positionX + 1;
				topy = positionY+1 > topy? topy : positionY + 1;
				eliminatepit(leftx, rightx, positionX, topy, bottomy, positionY);
				wumpuscells[leftx][positionY].safeIndicator = 1;
				wumpuscells[rightx][positionY].safeIndicator = 1;
				wumpuscells[positionX][topy].safeIndicator = 1;
				wumpuscells[positionX][bottomy].safeIndicator = 1;
				break;
			// Case - 6: In case the wumpus didnt die in the first cell after shooting arrow
			case 6:
				wumpuscells[0][1].safeIndicator = -1;
				wumpuscells[0][1].wumpusIndicator = 1;
				wumpuscells[0][1].pitchance = 0;
				wumpuscells[1][0].safeIndicator = 1;
				break;
		}
	}
}