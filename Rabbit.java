
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
    private Position foxPosition;
    private ArrayList<Position> bushPositions;
    private ArrayList<Position> carrotPositions;
    private ArrayList<Position> edgePositions;
    
    private int foxTimer;
    
    private double[] dScores;
    
    public Rabbit(Model model, Position position) {
        super(model, position);
        moveStraight = false;
        result = Direction.STAY;
        
        foxPosition = null;
        bushPositions = new ArrayList<Position>();
        carrotPositions = new ArrayList<Position>();
        edgePositions = new ArrayList<Position>();
        
        foxTimer = -1;
        dScores = new double[]{0, 0, 0, 0, 0, 0, 0, 0};
    }
    
    /**
     * Decides in which direction the rabbit wants to move.
     */
    @Override
    
    public Direction decideDirection() {
        dScores = new double[]{0, 0, 0, 0, 0, 0, 0, 0};
        
        lookAround();
        
        foxScore();
        
        System.out.println("New run");
        
        for(Direction d : Direction.allDirections()) {
            System.out.println(d);
            int index = getIndex(d);
            System.out.println(dScores[index]);
        }

        return decideDirection1();
    }
    
    private void lookAround() {
        for(Direction d : Direction.allDirections()) {
            Class<?> c = look(d);
            if(c == Fox.class) {
               Position foxPos = getObjectPos(d);
               foxPosition = foxPos;
               foxTimer = 1;
            }
        }
    }
    
    private Position getObjectPos(Direction d) {
        Position rPos = this.getPosition();
        Position dPos = directionToPosition(d);

        int newColumn = rPos.getColumn() + (distance(d) * dPos.getColumn());
        int newRow = rPos.getRow() + (distance(d) * dPos.getRow());
        
        Position newPos = new Position(newColumn, newRow);
        
        return newPos;
    }
    
    private void foxScore() {
        if(foxPosition != null) {
            for(Direction d : Direction.allDirections()) {
                int index = getIndex(d);
                double rad = getAngle(d, foxPosition);
                
                dScores[index] = (dScores[index] + rad)/foxTimer;
            }
            foxTimer++;
                
            if(foxTimer >= 10) {
                foxTimer = 0;
                foxPosition = null;
            }
        }
            
        }
    
    private int getIndex(Direction d) {
        ArrayList<Direction> allDirections = new ArrayList<Direction>();
        for(Direction dir : Direction.allDirections()) {
            allDirections.add(dir);
        }
        return allDirections.indexOf(d);
    }
    
    private double getAngle(Direction d, Position oPos) {
        double result = 0;
        
        Position dPos = directionToPosition(d);
        double dX = dPos.getColumn();
        double dY = dPos.getRow();
        double oX = oPos.getColumn();
        double oY = oPos.getRow();
        
        Position rPos = this.getPosition();
        double rX = rPos.getColumn();
        double rY = rPos.getRow();
        
        double oRVecX = oX - rX;
        double oRVecY = oY - rY;
        
        result = Math.atan2(dY,dX) - Math.atan2(oRVecY,oRVecX);
        
        if(result < 0) {
            result = -1 * result;
        }
        
        if(result > Math.PI) {
            result = 2*Math.PI - result;
        }
        
        return result;
    }
    
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
    
    
    private Direction decideDirection2() {
        for(Direction d : Direction.allDirections()) {
            Class<?> c = look(d);
            
            if(c == Fox.class) {
                if(distance(d) == 1 && moveStraight == false) {
                    result = Direction.turn(d, 5);
                    if(isOccupied(result)) {
                        result = Direction.turn(result, -10);
                        }
                    moveStraight = true;
                }
                else if(moveStraight == true) {
                    moveStraight = false;
                }
                }
        }
      
        
        return result;
    }
    
    private Direction decideDirection1() {
        for(Direction d : Direction.allDirections()) {
            Class<?> c = look(d);
            
            if(c == Fox.class) {
                    result = Direction.turn(d, 2);
                }
                }
      
        if(isOccupied(result)) {
            result = Direction.turn(result, -2); 
        }
        
        return result;
    }
    
    private Boolean isOccupied(Direction d) {
        boolean result = false;
        
        if(distance(d) == 1) {
                        if(look(d) == Bush.class || 
                        look(d) == Edge.class || 
                        look(d) == Fox.class || 
                        look(d) == Rabbit.class) {
                            result = true;
                        }
                    }
        return result;
    }
    
    /**
     * This method is used to retrieve who the authors are.
     */
    public String getCreator() {
        return "Unknown";
    }
}
