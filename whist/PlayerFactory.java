/**
 * This class is responsible for creating player objects
 */
public class PlayerFactory {
    private static PlayerFactory factory = null;
    //create a player based on the given name
    public static Player create(String name){
        if(name.equals("HumanPlayer")){
            return new HumanPlayer();
        }else if (name.equals("LegalNpc")){
            return new LegalNpc();
        }else if (name.equals("SmartNpc")){
            return new SmartNpc();
        }else{
            return new OriginalNpc();
        }
    }
    private PlayerFactory(){
    }
    // global access of the factory
    public static PlayerFactory getInstance(){
        if (factory == null){
            factory = new PlayerFactory();
        }
        return factory;
    }
}
