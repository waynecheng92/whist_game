import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
/**
 * abstract class represents the general type of players and actions in the game
 */
public abstract class Player{

    public Player(){}

    public abstract Card lead(Hand hand, Whist.Suit trumps);
    public abstract Card follow(Hand hand, Whist.Suit lead, Card winningCard, Whist.Suit trumps);
}
