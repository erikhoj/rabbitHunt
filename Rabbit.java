
/**
 * Write a description of class Rabbit here.
 * 
 * @author Morten D. Bech
 * @version September 8, 2015
 */

import java.awt.Point;
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
    private ArrayList<Point> foxes;
    private ArrayList<Point> bushes;
    private ArrayList<Point> carrots;
    private ArrayList<Point> edges;
    
    private ArrayList<Integer> foxesTimer;
    
    public Rabbit(Model model, Position position) {
        super(model, position);
        moveStraight = false;
        result = Direction.STAY;
        
        foxes = new ArrayList<Point>();
        bushes = new ArrayList<Point>();
        carrots = new ArrayList<Point>();
        edges = new ArrayList<Point>();
        
        foxesTimer = new ArrayList<Integer>();
    }
    
    /**
     * Decides in which direction the rabbit wants to move.
     */
    @Override
    
    public Direction decideDirection() {       
        return decideDirection2();
    }
    
    private void getAngle(Direction d, Point oPos) {
        
    }
    
    private Point directionToVector(Direction d) {
        if(d == Direction.STAY) {
            return null;
        }
        else if(d == Direction.N) {
            return new Point(0,1);
        }
        else if(d == Direction.NE) {
            return new Point(1,1);
        }
        else if(d == Direction.E) {
            return new Point(1,0);
        }
        else if(d == Direction.SE) {
            return new Point(1,-1);
        }
        else if(d == Direction.S) {
            return new Point(0,-1);
        }
        else if(d == Direction.SW) {
            return new Point(-1,-1);
        }
        else if(d == Direction.W) {
            return new Point(-1,0);
        }
        else if(d == Direction.NW) {
            return new Point(-1,1);
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
