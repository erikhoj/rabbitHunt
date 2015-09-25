
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

    public Rabbit(Model model, Position position) {
        super(model, position);
    }
    
    /**
     * Decides in which direction the rabbit wants to move.
     */
    @Override
    public Direction decideDirection() {
        Direction result = Direction.STAY;
        
        for(Direction d : Direction.allDirections()) {
            Class<?> c = look(d);
            
            if(c == Fox.class) {
                    result = d;
                    result = Direction.turn(result, 2);
                }
        }
        
        System.out.println(result);
        
        result = checkIfOccupied(result);
        
        System.out.println(result);
        System.out.println(distance(result));
        
        return result;
    }
    
    public Direction checkIfOccupied(Direction result) {
        if(distance(result) == 1) {
                        if(look(result) == Bush.class || look(result) == Edge.class) {
                            result = Direction.turn(result, -4);
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
