import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
/**
 * class responsible for manual card selection in the round
 */
public class ManuallySelectCard implements iSelectCard {
    //manually select a card to lead
    public Card selectLeadCard(Hand hand, Whist.Suit trumps) {
        hand.setTouchEnabled(true);
        return null;
    }
    //manually select a card to follow
    public Card selectFollowCard(Hand hand, Whist.Suit lead, Card winningCard, Whist.Suit trumps){
        hand.setTouchEnabled(true);
        return null;
    }
}
