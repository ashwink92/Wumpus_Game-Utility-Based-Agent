# Wumpus_Game-Utility-Based-Agent

Wumpus Game is a partially observable and non-deterministic game
  Goal is to keep avoiding the pit and Wumpus and try to reach the gold
  Utility Based Agent 
    1. keeps track of the possible state of the environment based on the sequence of percepts observed till present
    2. Then based on the information obtained about the environment performs a grid search on all possible actions that can be performed
    3. For each possible action it then calculates a corresponding suitable utility score based on consequences and suitable actions that can follow the action
    4. Then Performs the suitable action which maximises the utility score which is basically the expectation of finding gold
    
    Utility Score = Reward value based on the presence of gold * Probability of finding gold based on the action

Model of the wumpus world represented as 4 by 4 matrix with cell in the matrix having following variables
			1. SafeIndicator - If the cell is safe or not
			2. Pitprobability - Probability of pit being present in the cell
			3. Wumpusprobability - Probability of wumpus being present in the cell
			4. AlreadyVisited - If the cell has already been visited
			5. PitChance - To indicate if there exists a pit in the cell
			6. WumpusIndicator - To indicate if there exists a wumpus in the cell

Conditions for different percepts
			
			
      For case 1 - Both breeze and stench observed, Update wumpus and pit probabilities
			  Then based on Wummpus and pit Probabilities try to finalize the position of wumpus and pit positions
			  By calling finalizewumpus and finalizepit functions
			
			For case 2 - Only stench observed, Update wumpus probabilities
			  Then based on Wummpus probability try to finalize the position of wumpus positions
			  By calling finalizewumpus function
			  Also since breeze not observed - eliminate possibility of breeze in neighboring cells
			  By  calling eliminatepit function

			For case 3 - Only stench observed, Update pit probabilities
			  Then based on Pit Probabilities try to finalize the position of pit
			  By calling finalizepit functions

			For case 4 - Scream Observed along with Breeze
			  Since wumpus is dead, mark all cells as wumpus free by changing wumpus probability to 0
			  Then since breeze is observed, update pit probabilities
			  and then try finalizing pit position by calling finalizepit

			For case 5 - Only scream observed
			  Mark all cells as Wumpus free, since it is dead by making wumpus probability to 0
			  Since breeze is also not observed, mark neighboring cells as safe
        
Action to perform based on model current state			
			Based on the current values of the model cells - determine the most viable cell destination
			Based on the destination cell chosen - output the corresponding action
