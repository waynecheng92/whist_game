// Whist.java
import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.awt.Font;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("serial")
public class Whist extends CardGame {

	public enum Suit
  {
    SPADES, HEARTS, DIAMONDS, CLUBS
  }

  public enum Rank
  {
    // Reverse order of rank importance (see rankGreater() below)
	// Order of cards is tied to card images
	ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO
  }
  
  final String trumpImage[] = {"bigspade.gif","bigheart.gif","bigdiamond.gif","bigclub.gif"};

  static Random random = ThreadLocalRandom.current();

  private final String version = "1.0";
  public int nbPlayers = 4;
  public int nbStartCards = 13;
  public int winningScore = 11;
  private final int handWidth = 400;
  private final int trickWidth = 40;
  private Player[] players;
  private PlayerFactory playerFactory = PlayerFactory.getInstance();
  private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
  private final Location[] handLocations = {
			  new Location(350, 625),
			  new Location(75, 350),
			  new Location(350, 75),
			  new Location(625, 350)
	  };
  private final Location[] scoreLocations = {
			  new Location(575, 675),
			  new Location(25, 575),
			  new Location(575, 25),
			  new Location(650, 575)
	  };
  private Actor[] scoreActors = {null, null, null, null };
  private final Location trickLocation = new Location(350, 350);
  private final Location textLocation = new Location(350, 450);
  private final int thinkingTime = 2000;
  private Hand[] hands;
  private Location hideLocation = new Location(-500, - 500);
  private Location trumpsActorLocation = new Location(50, 50);
  private boolean enforceRules=false; // check whether the robot should forcibly obey the rule

  public void setStatus(String string) { setStatusText(string); }
  
private int[] scores = new int[nbPlayers];

Font bigFont = new Font("Serif", Font.BOLD, 36);

private void initScore() {
         for (int i = 0; i < nbPlayers; i++) {
		 scores[i] = 0;
		 scoreActors[i] = new TextActor("0", Color.WHITE, bgColor, bigFont);
		 addActor(scoreActors[i], scoreLocations[i]);
	 }
  }

private void updateScore(int player) {
	removeActor(scoreActors[player]);
	scoreActors[player] = new TextActor(String.valueOf(scores[player]), Color.WHITE, bgColor, bigFont);
	addActor(scoreActors[player], scoreLocations[player]);
}

private Card selected;

private void initRound() {
		 hands = deck.dealingOut(nbPlayers, nbStartCards); // Last element of hands is leftover cards; these are ignored
		 for (int i = 0; i < nbPlayers; i++) {
			   hands[i].sort(Hand.SortType.SUITPRIORITY, true);
		 }
		 // Set up human player for interaction
		CardListener cardListener = new CardAdapter()  // Human Player plays card
			    {
			      public void leftDoubleClicked(Card card) { selected = card; hands[0].setTouchEnabled(false); }
			    };
		for(int i =0;i< nbPlayers;i++){
			if(players[i].getClass().getName().equals("HumanPlayer")){
				hands[i].addCardListener(cardListener);
			}
		}
		 // graphics
	    RowLayout[] layouts = new RowLayout[nbPlayers];
	    for (int i = 0; i < nbPlayers; i++) {
	      layouts[i] = new RowLayout(handLocations[i], handWidth);
	      layouts[i].setRotationAngle(90 * i);
	      // layouts[i].setStepDelay(10);
	      hands[i].setView(this, layouts[i]);
	      hands[i].setTargetArea(new TargetArea(trickLocation));
	      hands[i].draw();
	    }
//	    for (int i = 1; i < nbPlayers; i++)  // This code can be used to visually hide the cards in a hand (make them face down)
//	      hands[i].setVerso(true);
	    // End graphics
 }
 // Returns winner, if any
private Optional<Integer> playRound() {
	// Select and display trump suit
	final Suit trumps = Function.randomEnum(Suit.class);
	final Actor trumpsActor = new Actor("sprites/"+trumpImage[trumps.ordinal()]);
	addActor(trumpsActor, trumpsActorLocation);
	// End trump suit
	Hand trick;
	int winner;
	Card winningCard;
	Suit lead;
	int nextPlayer = random.nextInt(nbPlayers); // randomly select player to lead for this round
	for (int i = 0; i < nbStartCards; i++) {
		trick = new Hand(deck);
    	selected = null;
        if(players[nextPlayer].getClass().getName().equals("HumanPlayer")) {
			players[nextPlayer].lead(hands[nextPlayer], trumps);
			setStatus("Player 0 double-click on card to lead.");
			while (null == selected) delay(100);
		} else if (players[nextPlayer].getClass().getName().equals("LegalNpc")){
            setStatusText("Player " + nextPlayer + " thinking...");
            delay(thinkingTime);
            selected = players[nextPlayer].lead(hands[nextPlayer], trumps);
        }  else if (players[nextPlayer].getClass().getName().equals("SmartNpc")) {
            setStatusText("Player " + nextPlayer + " thinking...");
            delay(thinkingTime);
			selected = players[nextPlayer].lead(hands[nextPlayer], trumps);
		}else{
			setStatusText("Player " + nextPlayer + " thinking...");
			delay(thinkingTime);
			selected = players[nextPlayer].lead(hands[nextPlayer], trumps);
		}
        // Lead with selected card
	        trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
			trick.draw();
			selected.setVerso(false);
			// No restrictions on the card being lead
			lead = (Suit) selected.getSuit();
			selected.transfer(trick, true); // transfer to trick (includes graphic effect)
			winner = nextPlayer;
			winningCard = selected;
		// End Lead
		for (int j = 1; j < nbPlayers; j++) {
			if (++nextPlayer >= nbPlayers) nextPlayer = 0;  // From last back to first
			selected = null;
			if (players[nextPlayer].getClass().getName().equals("LegalNpc")){
                setStatusText("Player " + nextPlayer + " thinking...");
                delay(thinkingTime);
				selected = players[nextPlayer].follow(hands[nextPlayer],lead, winningCard,trumps);
			} else if(players[nextPlayer].getClass().getName().equals("HumanPlayer")) {
				players[nextPlayer].follow(hands[nextPlayer],lead, winningCard,trumps);
				setStatus("Player 0 double-click on card to follow.");
				while (null == selected) delay(100);
			} else if (players[nextPlayer].getClass().getName().equals("SmartNpc")) {
                setStatusText("Player " + nextPlayer + " thinking...");
                delay(thinkingTime);
				selected = players[nextPlayer].follow(hands[nextPlayer], lead, winningCard, trumps);
			}else{
				setStatusText("Player " + nextPlayer + " thinking...");
				delay(thinkingTime);
				selected = players[nextPlayer].follow(hands[nextPlayer],lead, winningCard,trumps);
			}

	        // Follow with selected card
		        trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
				trick.draw();
				selected.setVerso(false);  // In case it is upside down

				// Check: Following card must follow suit if possible
					if (selected.getSuit() != lead && hands[nextPlayer].getNumberOfCardsWithSuit(lead) > 0) {
						 // Rule violation
						 String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + selected;
						 System.out.println(violation);
						 if (enforceRules) 
							 try {
								 throw(new BrokeRuleException(violation));
								} catch (BrokeRuleException e) {
									e.printStackTrace();
									System.out.println("A cheating player spoiled the game!");
									System.exit(0);
								}  
					 }
				// End Check
				 selected.transfer(trick, true); // transfer to trick (includes graphic effect)
				 System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + winningCard.getRankId());
				 System.out.println(" played: suit = " +    selected.getSuit() + ", rank = " +    selected.getRankId());
				 if ( // beat current winner with higher card
					 (selected.getSuit() == winningCard.getSuit() && Function.rankGreater(selected, winningCard)) ||
					  // trumped when non-trump was winning
					 (selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
					 System.out.println("NEW WINNER");
					 winner = nextPlayer;
					 winningCard = selected;
				 }
			// End Follow
		}
		delay(600);
		trick.setView(this, new RowLayout(hideLocation, 0));
		trick.draw();		
		nextPlayer = winner;
		setStatusText("Player " + nextPlayer + " wins trick.");
		scores[nextPlayer]++;
		updateScore(nextPlayer);
		if (winningScore == scores[nextPlayer]) return Optional.of(nextPlayer);
	}
	removeActor(trumpsActor);
	return Optional.empty();
}

  public Whist() throws IOException {
    super(700, 700, 30);
    Properties whistProperties = new Properties();
    // default properties
    whistProperties.setProperty("seed", "30006");
    whistProperties.setProperty("NbPlayers", "4");
    whistProperties.setProperty("NbStartCards", "13");
    whistProperties.setProperty("WinningScore", "11");
    whistProperties.setProperty("player1", "HumanPlayer");
    whistProperties.setProperty("player2", "OriginalNpc");
    whistProperties.setProperty("player3", "OriginalNpc");
    whistProperties.setProperty("player4", "OriginalNpc");
    FileReader inStream = null;
    // Read properties
    try {
    	inStream = new FileReader("whist.properties");
    	whistProperties.load(inStream);
    } finally {
    	if (inStream != null) {
    		inStream.close();
    	}
    }
    // initialize whist settings from the property file
    long seed  = Long.parseLong(whistProperties.getProperty("seed"));
    nbPlayers = Integer.parseInt(whistProperties.getProperty("NbPlayers"));
    nbStartCards = Integer.parseInt(whistProperties.getProperty("NbStartCards"));
    winningScore = Integer.parseInt(whistProperties.getProperty("WinningScore"));
    String player1 =  whistProperties.getProperty("player1");
    String player2 =  whistProperties.getProperty("player2");
    String player3 =  whistProperties.getProperty("player3");
    String player4=  whistProperties.getProperty("player4");
    scores = new int[nbPlayers];
    this.random =  new Random(seed);
    Function.random = this.random;
    players = new Player[]{playerFactory.create(player1),playerFactory.create(player2),
							playerFactory.create(player3),playerFactory.create(player4)};

    setTitle("Whist (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
    setStatusText("Initializing...");
    initScore();
    Optional<Integer> winner;
    do { 
      initRound();
      winner = playRound();
    } while (!winner.isPresent());
    addActor(new Actor("sprites/gameover.gif"), textLocation);
    setStatusText("Game over. Winner is player: " + winner.get());
    refresh();
  }

  public static void main(String[] args) throws IOException {
	// System.out.println("Working Directory = " + System.getProperty("user.dir"));
    new Whist();
  }
}
