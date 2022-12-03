import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
/**
 * This interface represents card selection strategies to lead and follow in the round
 */
public interface iSelectCard {
    public Card selectLeadCard(Hand hand, Whist.Suit trumps);
    public Card selectFollowCard(Hand hand, Whist.Suit lead, Card winningCard, Whist.Suit trumps);
}
