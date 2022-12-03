import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
/**
 * class responsible for selecting a card randomly in the round
 */
public class RandomSelectCard implements iSelectCard {
    //select a random card to lead
    public Card selectLeadCard(Hand hand, Whist.Suit trumps) {
        Card selected = Function.randomCard(hand);
        return selected;
    }
    //select a random card to follow
    public Card selectFollowCard(Hand hand, Whist.Suit lead, Card winningCard, Whist.Suit trumps){
        Card selected =Function.randomCard(hand);
        return selected;
    }
}
