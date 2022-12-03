import ch.aplu.jcardgame.*;
/**
 * This class represent the original type of NPC given in the assignment
 */
public class OriginalNpc extends Player {

    private iSelectCard randomSelectCard = new RandomSelectCard();

    @Override
    public Card lead(Hand hand, Whist.Suit trumps) {
        Card selected = randomSelectCard.selectLeadCard(hand, trumps);
        return selected;
    }

    @Override
    public Card follow(Hand hand, Whist.Suit lead, Card winningCard, Whist.Suit trumps) {
        Card selected = randomSelectCard.selectFollowCard(hand, lead, winningCard, trumps);
        return selected;
    }
}
