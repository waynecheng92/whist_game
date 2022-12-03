import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
/**
 * This class represents the human player in the game
 */
public class HumanPlayer extends Player{
    iSelectCard manuallySelect = new ManuallySelectCard();
    public HumanPlayer(){}
    @Override
    public Card lead(Hand hand, Whist.Suit trumps) {
        manuallySelect.selectLeadCard(hand, trumps);
        return null;
    }
    @Override
    public Card follow(Hand hand, Whist.Suit lead, Card winningCard, Whist.Suit trumps) {
        manuallySelect.selectFollowCard(hand, lead, winningCard, trumps);
        return null;
    }
}
