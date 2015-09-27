
/**
 * Write a description of class Rabbit here.
 * 
 * @author Morten D. Bech
 * @version September 8, 2015
 */

import java.lang.Math;
import java.util.ArrayList;

public class Rabbit extends Animal {
    /**
     * In order to create a new Rabbit we need to provide a
     * model og and position. Do not change the signature or
     * first line of the construction. Appending code after
     * the first line is allowed.
     */
    
    private boolean moveStraight;
    private Direction result;
    private Direction resultTask2;
    private Position foxPosition;
    private ArrayList<Position> bushPositions;
    private ArrayList<Position> carrotPositions;
    
    private int foxTimer;
    
    private double[] dScores;
    
    private static final double FOX_SCORE = 250;
    private static final double BUSH_SCORE = 0;
    private static final double CARROT_SCORE = 200;
    private static final double EDGE_SCORE = 50;
    private static final double RABBIT_SCORE = 0;
    
    private boolean isBerserk;
    private boolean standStill;
    
    public Rabbit(Model model, Position position) {
        super(model, position);
        moveStraight = false;
        result = Direction.STAY;
        resultTask2 = Direction.STAY;
        
        foxPosition = null;
        bushPositions = new ArrayList<Position>();
        carrotPositions = new ArrayList<Position>();
        isBerserk = false;
        standStill = false;
        
        foxTimer = -1;
        dScores = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    }
    
    /**
     * Decides in which direction the rabbit wants to move.
     */
    @Override
    
    public Direction decideDirection() {
        dScores = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        standStill = false;
        
        lookAround();
        
        carrotScore();
        bushScore();
        edgeScore();
        foxScore();
        
        System.out.println("Distance: " + distToEdge(2));
        
        isBerserk = isBeserk();
        
        System.out.println("New run");
        for(Direction d : Direction.allDirections()) {
            System.out.println(d + ": " + dScores[getIndex(d)]);
        }
        System.out.println("Fox: " + foxPosition);
        System.out.println("FoxTimer: " + foxTimer);
        
        result = findBestDirection();
        
        
        
        if(standStill == true) {
            result = Direction.STAY;
        }
        return result;
    }
    
    /**
     * Finds the best direction based on score.
     */
    private Direction findBestDirection() {
        double bestScore = 0;
        Direction result = Direction.STAY;
        
        for(Direction d : Direction.allDirections()) {
            int index = getIndex(d);
            if(dScores[index] > bestScore) {
                bestScore = dScores[index];
                result = d;
            }
        }
        
        return result;
    }
    
    /**
     * Looks around and adds whatever objects the rabbit can see to the position ArrayLists.
     */
    
    private void lookAround() {
        for(Direction d : Direction.allDirections()) {
            Class<?> c = look(d);
            if(c == Fox.class) {
               Position foxPos = getObjectPos(d);
               foxPosition = foxPos;
               foxTimer = 1;
            }
            else if(c == Carrot.class) {
                Position carrotPos = getObjectPos(d);
                if(!carrotPositions.contains(carrotPos)) {
                    carrotPositions.add(carrotPos);
                }
            }
            else if(c == Bush.class) {
                Position bushPos = getObjectPos(d);
                if(!bushPositions.contains(bushPos)) {
                    bushPositions.add(bushPos);
                }
            }
        }
    }
    
    /**
     * Returns the position of the object in direction d.
     */
    
    private Position getObjectPos(Direction d) {
        Position rPos = this.getPosition();
        Position dPos = directionToPosition(d);

        int newColumn = rPos.getColumn() + (distance(d) * dPos.getColumn());
        int newRow = rPos.getRow() + (distance(d) * dPos.getRow());
        
        Position newPos = new Position(newColumn, newRow);
        
        return newPos;
    }
    
    /**
     * Adds score to dScores[] based on the position of the foxes.
     * If the rabbit is berserk, move towards, and kill, the fox.
     */
    private void foxScore() {
        if(foxPosition != null) {
            for(Direction d : Direction.allDirections()) {
                int index = getIndex(d);
                double rad = getAngle(d, foxPosition);
                
                double dist = getDistance(foxPosition);
                
                if(isBerserk) {
                    if(dist < 2 && rad == 0) {
                        dScores[index]+= 10000;
                    }
                    else if(dist < 3 && rad == 0) {
                        standStill = true;
                    }
                    else if(rad == 0) {
                        dScores[index]+= 5000;
                    }
                    
                    if(rad < Math.PI/2) {
                        rad = Math.PI/2 - rad;
                    }
                }

                else{
                    if(dist < 2 && rad == Math.PI) {
                        int lLiveZone = getIndex(Direction.turn(d,-1));
                        int rLiveZone = getIndex(Direction.turn(d,1));
                        dScores[lLiveZone]+= 5000;
                        dScores[rLiveZone]+= 5000;  
                    }
                
                    if(rad >= Math.PI/2) {
                        rad = Math.PI - rad;
                    }
                    else if(rad <= Math.PI/4) {
                        rad = 0;
                    }
                }
                
                dScores[index] = dScores[index] + (rad * FOX_SCORE)/(foxTimer * dist);
            }
            foxTimer++;
            
            if(foxTimer >= 10) {
                foxTimer = 0;
                foxPosition = null;
            }
        }
            
        }
        
        /**
         * Adds score to dScores[] based on the position of the bushes.
         */
    private void bushScore() {
        for(Position bPos : bushPositions) {
            for(Direction d : Direction.allDirections()) {
                int index = getIndex(d);
                double rad = getAngle(d, bPos);
                
                double dist = getDistance(bPos);
                
                dScores[index] = dScores[index] + (rad * BUSH_SCORE)/dist;
                
                if(dist < 2 && rad == 0) {
                    dScores[index] = -10000;
                }
            }
        }
    }
    
    /**
     * Adds score to dScores[] based on the position of the carrots.
     */
    
    private void carrotScore() {
        for(Position cPos : carrotPositions) {
            Position rPos = getPosition();
                if(rPos.getColumn() == cPos.getColumn() && rPos.getRow() == cPos.getRow()) {
                    carrotPositions.remove(cPos);
                    break;
                }
            
            for(Direction d : Direction.allDirections()) {
                
                
                int index = getIndex(d);
                double rad = getAngle(d, cPos);
                
                rad = Math.PI - rad;
                
                double dist = getDistance(cPos);
                
                dScores[index] = dScores[index] + (rad * CARROT_SCORE)/dist;
            }
        }
    }
    
    /**
     * Adds score to dScores[] based on the position of the rabbit compared to the edges.
     */
    
    private void edgeScore() {
        for(int i = 0; i < 4; i++) {
            Direction d = Direction.STAY;
            
            if(i == 0) {
                d = Direction.N;
            }
            else if(i == 1) {
                d = Direction.E;
            }
            else if(i == 2) {
               d = Direction.S; 
            }
            else if(i == 3) {
               d = Direction.W;
            }
            
            int dist = distToEdge(i);
        
            if(dist < 2) {
                dScores[getIndex(d)] = -10000;
                dScores[getIndex(Direction.turn(d, -1))] = -10000; 
                dScores[getIndex(Direction.turn(d, 1))] = -10000;
            }
        
            dScores[getIndex(Direction.turn(d, 4))]+= (1.5*EDGE_SCORE)/dist;
            dScores[getIndex(Direction.turn(d, 3))]+= EDGE_SCORE/dist;
            dScores[getIndex(Direction.turn(d, -3))]+= EDGE_SCORE/dist;
            dScores[getIndex(Direction.turn(d, 2))]+= (0.5*EDGE_SCORE)/dist;
            dScores[getIndex(Direction.turn(d, -2))]+= (0.5*EDGE_SCORE)/dist;
        }
        
    }
    
    /**
     * Returns the distance to an edge.
     */
    
    private int distToEdge(int edge) {
        // 0 == north, 1 == east, 2 == south, 3 == west
        Position rPos = this.getPosition();
        int rX = rPos.getColumn();
        int rY = rPos.getRow();
        
        int result = 0;
        
        if(edge == 0) {
            result = -(-1 - rY);
        }
        else if(edge == 1) {
            result = 20 - rX;
        }
        else if(edge == 2) {
            result = 20 - rY;
        }
        else if(edge == 3) {
            result = -(-1 - rX);
        }
        
        return result;
    }
    
    /**
     * Returns the distance between the rabbit and a Position.
     */
    
    private double getDistance(Position oPos) {
        Position vecPos = getVectorRO(oPos);
        double vecX = vecPos.getColumn();
        double vecY = vecPos.getRow();
        
        double result = Math.sqrt((vecX*vecX) + (vecY*vecY));
        
        if(result == 0) {
        result = 1;
        }
        
        return result;
    }
    
    /**
     * Returns the "vector" Rabbit-Object.
     */
    private Position getVectorRO(Position oPos) {
        Position rPos = this.getPosition();
        double rX = rPos.getColumn();
        double rY = rPos.getRow();
        
        double oX = oPos.getColumn();
        double oY = oPos.getRow();
        
        int vecX = (int)(oX - rX);
        int vecY = (int)(oY - rY);
        
        Position vecPos = new Position(vecX, vecY);
        return vecPos;
    }
    
    /**
     * Returns the index of direction d. This allows the program to associate the index of dScores[] and directions.
     */
    private int getIndex(Direction d) {
        ArrayList<Direction> allDirections = new ArrayList<Direction>();
        for(Direction dir : Direction.allDirections()) {
            allDirections.add(dir);
        }
        return allDirections.indexOf(d);
    }
    
    /**
     * Returns the angle between a direction and the vector from the rabbit to an object.
     */
    private double getAngle(Direction d, Position oPos) {
        double result = 0;
        
        Position dPos = directionToPosition(d);
        double dX = dPos.getColumn();
        double dY = dPos.getRow();
        
        Position vecPos = getVectorRO(oPos);
        
        double vecX = vecPos.getColumn();
        double vecY = vecPos.getRow();
        
        result = Math.atan2(dY,dX) - Math.atan2(vecY,vecX);
        
        if(result < 0) {
            result = -1 * result;
        }
        
        if(result > Math.PI) {
            result = 2*Math.PI - result;
        }
        
        return result;
    }
    
    /**
     * Converts a direction into its "vector".
     */
    private Position directionToPosition(Direction d) {
        if(d == Direction.STAY) {
            return null;
        }
        else if(d == Direction.N) {
            return new Position(0,-1);
        }
        else if(d == Direction.NE) {
            return new Position(1,-1);
        }
        else if(d == Direction.E) {
            return new Position(1,0);
        }
        else if(d == Direction.SE) {
            return new Position(1,1);
        }
        else if(d == Direction.S) {
            return new Position(0,1);
        }
        else if(d == Direction.SW) {
            return new Position(-1,1);
        }
        else if(d == Direction.W) {
            return new Position(-1,0);
        }
        else if(d == Direction.NW) {
            return new Position(-1,-1);
        }
        
        return null;
        }
    
    /**
     * The function from task 2.
     */
    private Direction decideDirection2() {
        for(Direction d : Direction.allDirections()) {
            Class<?> c = look(d);
            
            if(c == Fox.class) {
                if(distance(d) == 1 && moveStraight == false) {
                    resultTask2 = Direction.turn(d, 5);
                    if(!canMove(resultTask2)) {
                        result = Direction.turn(resultTask2, -10);
                        }
                    moveStraight = true;
                }
                else if(moveStraight == true) {
                    moveStraight = false;
                }
                }
        }
      
        return resultTask2;
    }
    
    /**
     * The function from task 1.
     */
    
    private Direction decideDirection1() {
        for(Direction d : Direction.allDirections()) {
            Class<?> c = look(d);
            
            if(c == Fox.class) {
                    resultTask2 = Direction.turn(d, 2);
                }
                }
      
        if(!canMove(resultTask2)) {
            resultTask2 = Direction.turn(result, -2); 
        }
        
        return resultTask2;
    }
    
    /**
     * This method is used to retrieve who the authors are.
     */
    public String getCreator() {
        return "Gruppe DA2 - 05 Erik Høj Petersen 201507288, Jeppe Løvstad 201506166";
    }
}
