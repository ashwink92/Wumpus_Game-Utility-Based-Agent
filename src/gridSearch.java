import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.lang.Math;

import static java.lang.Math.abs;

public class gridSearch {
    //wumpuscell[][] wumpuscells = new wumpuscell[4][4];
    List<utilitycell> utilitycells = new ArrayList<>();
    List<String> goalcells = new ArrayList<>();
    int destinationX;
    int destinationY;
    wumpusworld wumpusworldobj;
    int nomovementflag = 0;
    int unexploredcnt = 0;
    // Checks the utility score of each possible cell the agent can go to
    // Returns the cell with the highest utility score
    // If no cell has utility score > 0 then return current agent position itself indicating the agent to perform no-op
    List<Integer> search(List<newwumpusclass> possibleexploredcells, List<newwumpusclass> possibleunexploredcells, int agentPositionX, int agentPositionY, char direction, wumpusworld wumpusworldobj1) {
        unexploredcnt = 0;
        this.wumpusworldobj = wumpusworldobj1;
        // Collating a list of all possible goal cells
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((wumpusworldobj.wumpuscells[i][j].safeIndicator == 1 && wumpusworldobj.wumpuscells[i][j].alreadyvisited != 1)
                    || ((wumpusworldobj.wumpuscells[i][j].safeIndicator != -1
                        || (wumpusworldobj.wumpuscells[i][j].wumpusIndicator == 1
                        && wumpusworldobj.wumpuscells[i][j].pitindicator != 1))
                        && wumpusworldobj.wumpuscells[i][j].alreadyvisited != 1
                        && wumpusworldobj.wumpuscells[i][j].wumpusprobaility == 0 && wumpusworldobj.wumpuscells[i][j].pitprobability == 0)) {
                            goalcells.add(String.valueOf(i) + "-" + String.valueOf(j));
                }
            }
        }
        if(possibleunexploredcells.size() != 0){
            unexploredcnt = 1;
            for (newwumpusclass unexploredcell : possibleunexploredcells) {
                utilitycells.add(new utilitycell(unexploredcell.x, unexploredcell.y));
            }
        } else {
            for (newwumpusclass exploredcell : possibleexploredcells) {
                utilitycells.add(new utilitycell(exploredcell.x, exploredcell.y));
            }
        }
        int counter2 = 0;
        if(possibleunexploredcells.size() != 0) {
            // if direct explored safe cell is present next to the agent position and direction is the same
            // then return that cell itself as the cell with highest probability
            for (newwumpusclass unexploredcell : possibleunexploredcells) {
                if (direction == 'N' && unexploredcell.y == agentPositionY + 1 && unexploredcell.x == agentPositionX) {
                    destinationX = unexploredcell.x;
                    destinationY = unexploredcell.y;
                    counter2 += 1;
                    break;
                } else if (direction == 'E' && unexploredcell.y == agentPositionY && unexploredcell.x == agentPositionX + 1) {
                    destinationX = unexploredcell.x;
                    destinationY = unexploredcell.y;
                    counter2 += 1;
                    break;
                } else if (direction == 'S' && unexploredcell.y == agentPositionY - 1 && unexploredcell.x == agentPositionX) {
                    destinationX = unexploredcell.x;
                    destinationY = unexploredcell.y;
                    counter2 += 1;
                    break;
                } else if (direction == 'W' && unexploredcell.y == agentPositionY && unexploredcell.x == agentPositionX - 1) {
                    destinationX = unexploredcell.x;
                    destinationY = unexploredcell.y;
                    counter2 += 1;
                    break;
                }
            }
            // Call utility scoring function to return the cell with highest utility score
            if (counter2 == 0) {
                utilitycell finalcell = utilityScoring();
                destinationX = finalcell.x;
                destinationY = finalcell.y;
            }
        } else {
                utilitycell finalcell = utilityScoring();
                destinationX = finalcell.x;
                destinationY = finalcell.y;
        }
        List<Integer> result = new ArrayList<>();
        if(nomovementflag == 0) {
            result.add(destinationX);
            result.add(destinationY);
        } else {
            result.add(agentPositionX);
            result.add(agentPositionY);
        }

        return result;
    }
    utilitycell utilityScoring() {
        List<goalcell> allpossiblecells = new ArrayList<>();
        // Creating a list of all cells
        for(int j = 0; j < 4; j++)
            for(int k = 0; k < 4; k++)
                allpossiblecells.add(new goalcell(j,k));
        // Loop for each cell which can be reached from the agent's current position
        for (utilitycell cell:utilitycells) {
            List<String>  reachablecell = new ArrayList<>();
            // If cell has not been explored yet then give it a utility score of 1
            if(wumpusworldobj.wumpuscells[cell.x][cell.y].alreadyvisited == 0) {
                cell.utilityScore+=1;
            }
            for(goalcell goals:allpossiblecells) {
                goals.dist = abs(goals.x - cell.x) +  abs(goals.y - cell.y);
            }
            // For every other cell that can be reached from the utility cell along its path
            // add 1/dist from the utility cell as the utility score
            Collections.sort(allpossiblecells, new goalCellComparator());
            for(goalcell goals:allpossiblecells){
                if(goals.dist == 0) {
                    reachablecell.add(String.valueOf(goals.x)+"-"+String.valueOf(goals.y));
                } else {
                    int x1 = goals.x;
                    int y1 = goals.y;
                    if(reachablecell.contains(String.valueOf(x1-1)+"-"+String.valueOf(y1))
                        || reachablecell.contains(String.valueOf(x1+1)+"-"+String.valueOf(y1))
                        || reachablecell.contains(String.valueOf(x1)+"-"+String.valueOf(y1-1))
                        || reachablecell.contains(String.valueOf(x1)+"-"+String.valueOf(y1+1))) {
                        if (goalcells.contains(String.valueOf(x1)+"-"+String.valueOf(y1))) {
                            reachablecell.add(String.valueOf(goals.x)+"-"+String.valueOf(goals.y));
                            cell.utilityScore+=1/(double)goals.dist;
                        } else {
                            if((wumpusworldobj.wumpuscells[x1][y1].pitprobability == 0 && wumpusworldobj.wumpuscells[x1][y1].wumpusprobaility == 0
                                    && (wumpusworldobj.wumpuscells[x1][y1].safeIndicator != -1 || wumpusworldobj.wumpuscells[x1][y1].wumpusIndicator == 1 ))
                                || (wumpusworldobj.wumpuscells[x1][y1].safeIndicator == 1)) {
                                reachablecell.add(String.valueOf(goals.x)+"-"+String.valueOf(goals.y));
                            }
                        }
                    }
                }
            }
         }
        // Check for cell with highest utility score and return that
        Comparator<utilitycell> cmp = Comparator.comparing(utilitycell::getUtilityScore);
        utilitycell finalcell = Collections.max(utilitycells,cmp);
        // If no cell has utility score > 0 then set no movement flag = 1
        if(finalcell.utilityScore == 0) {
            nomovementflag = 1;
        }
        return finalcell;
    }

}

class utilitycell {
    int x,y;
    double utilityScore = 0.0;
    utilitycell(int x, int y){
        this.x = x;
        this.y = y;
    }
    double getUtilityScore() {
        return this.utilityScore;
    }
}
class goalcell {
    int x,y;
    int dist=0;
    goalcell(int x, int y){
        this.x = x;
        this.y = y;
    }
    int getdist() {
        return this.dist;
    }
}
class goalCellComparator implements Comparator<goalcell> {
    public int compare(goalcell goal1, goalcell goal2) {
        return goal1.getdist() - goal2.getdist();
    }
}