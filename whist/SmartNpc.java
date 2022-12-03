import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
/**
 * This class represents NPC who play the game in a smart and legal way
 */
public class SmartNpc extends Player {

    private iSelectCard smartSelectCard = new SmartlySelectCard();

    @Override
    public Card lead(Hand hand, Whist.Suit trumps) {
        Card selected = smartSelectCard.selectLeadCard(hand, trumps);
        return selected;
    }

    @Override
    public Card follow(Hand hand, Whist.Suit lead, Card winningCard, Whist.Suit trumps) {
        Card selected = smartSelectCard.selectFollowCard(hand, lead, winningCard, trumps);
        return selected;
    }

}
