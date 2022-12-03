import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import java.util.ArrayList;

/**
 * class responsible for smart card selection in the round
 */
public class SmartlySelectCard implements iSelectCard{

    //select an optimal card to lead this round
    public Card selectLeadCard(Hand hand, Whist.Suit trumps){
        Card selected = hand.getFirst();
        for(int i = 1; i< hand.getNumberOfCards(); i++){
            if(// beat current winner with higher card
                    (selected.getSuit() != trumps && hand.get(i).getSuit()!= trumps
                            && Function.rankGreater(hand.get(i), selected)) ||
                            // trumped when non-trump was winning
                            (hand.get(i).getSuit() == trumps && selected.getSuit() != trumps)){

                selected = hand.get(i);
            }
        }
        return selected;
    }
    //select an optimal card to follow
    public Card selectFollowCard(Hand hand, Whist.Suit lead, Card winningCard, Whist.Suit trumps){
        Card selected;
        Card maximumCard = hand.getFirst();
        Card minimumCard = hand.getFirst();
        ArrayList<Card> cards = hand.getCardsWithSuit(winningCard.getSuit());
        System.out.println(cards);
        // check if there are cards having the same suit with the winningCard played
        if(cards.size() != 0) {
            // check whether the smart Npc owns a card beats the currently winningCard
            // if not, select the smallest card that follows the rules
            if (cards.get(0).getRankId() < winningCard.getRankId()) {
                selected = cards.get(0);
                return selected;
            } else {
                selected = cards.get(cards.size() - 1);
                return selected;
            }
        }
        //get the maximum card and the minimum card in current hand
        for(int i = 1; i< hand.getNumberOfCards(); i++){

            if(
                    (maximumCard.getSuit() == hand.get(i).getSuit() && Function.rankGreater(hand.get(i), maximumCard)) ||

                            (hand.get(i).getSuit() == trumps && maximumCard.getSuit() != trumps)){

                maximumCard = hand.get(i);
            }
            if(
                    (minimumCard.getSuit()!= trumps && hand.get(i).getSuit() != trumps
                            && Function.rankGreater(minimumCard, hand.get(i))) ||

                            (minimumCard.getSuit() == trumps && hand.get(i).getSuit() != trumps)){

                minimumCard = hand.get(i);
            }
        }
        //play the largest card in hand if it is greater than the current winner card
        //if not, play the smallest card
        if ( (maximumCard.getSuit() == trumps && winningCard.getSuit() != trumps) ||
                (maximumCard.getSuit() == trumps && winningCard.getSuit() == trumps
                        && Function.rankGreater(maximumCard,winningCard))) {
            selected = maximumCard;
        } else {
            selected = minimumCard;
        }
        return selected;
    }
}
