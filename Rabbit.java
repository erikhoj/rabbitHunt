
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
        return Direction.STAY;
    }
    
    /**
     * This method is used to retrieve who the authors are.
     */
    public String getCreator() {
        return "Unknown";
    }
}
