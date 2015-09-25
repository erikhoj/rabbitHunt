
/**
 * Write a description of class Rabbit here.
 * 
 * @author Morten D. Bech
 * @version September 8, 2015
 */
public class Rabbit extends Animal {
    /**
     * In order to create a new Rabbit we need to provide a
     * model og and position. Do not change the signature or
     * first line of the construction. Appending code after
     * the first line is allowed.
     */
    
    private boolean moveStraight;
    private Direction result;
    
    public Rabbit(Model model, Position position) {
        super(model, position);
        moveStraight = false;
        result = Direction.STAY;
    }
    
    /**
     * Decides in which direction the rabbit wants to move.
     */
    @Override
    
    public Direction decideDirection() {
        return result;
    }
    
    public Direction decideDirection2() {
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
    
    public Direction decideDirection1() {
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
    
    public Boolean isOccupied(Direction d) {
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
