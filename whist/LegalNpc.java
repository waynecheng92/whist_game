import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
/**
 * This class is used to represent NPC who play the game without violating rules
 */
public class LegalNpc extends Player {
    private iSelectCard randomCard = new RandomSelectCard();
    @Override
    public Card lead(Hand hand, Whist.Suit trumps) {
        Card selected = randomCard.selectLeadCard(hand, trumps);
        return selected;
    }
    @Override
    public Card follow(Hand hand, Whist.Suit lead, Card winningCard, Whist.Suit trumps) {
        Card selected;
        do {
            selected = randomCard.selectFollowCard(hand, lead, winningCard, trumps);
        }
        while (selected.getSuit() != lead && hand.getNumberOfCardsWithSuit(lead) > 0);

        return selected;
    }
}
