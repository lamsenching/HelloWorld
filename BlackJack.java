/********************************************************
  Name of Source File: 	LamSenChingITP3914assignment.java
  
  Student Name (ID): 	Lam Sen Ching (170027747)
  
  Course Name (Code):	HD in Software Engineering 
  						(IT114105)
  Program Description:
	This program is simulation of 
	the game of blackjack.
  ********************************************************/ 
import java.util.Scanner;
public class BlackJack{
	public static Scanner input=new Scanner(System.in);
	public static String[]cards=new String[52];
	public static int drawedCards=0,
					  players=0;
	public static int[] cardsOrder=new int[52],
						cardsInHand,
						dealersCardsArray;
	public static int[][] playersCardsArray;
	public static boolean[] standed;
	public static void main(String[]args){
		int cardIndex=0; 
		boolean noDealersRound=true,finalGameChk=false;
		for(int i=0;i<4;i++){	
			for(int j=0;j<13;j++){
				switch (i){
					case 0: cards[cardIndex]="Spade:";	break;
					case 1: cards[cardIndex]="Heart:";	break;
					case 2: cards[cardIndex]="Club:";	break;
					case 3: cards[cardIndex]="Diamond:";break;
				}
				switch (j){
					case 0:	cards[cardIndex]+="Ace";	break;
					case 1:	cards[cardIndex]+="2";		break;
					case 2:	cards[cardIndex]+="3";		break;
					case 3:	cards[cardIndex]+="4";		break;
					case 4:	cards[cardIndex]+="5";		break;
					case 5:	cards[cardIndex]+="6";		break;
					case 6:	cards[cardIndex]+="7";		break;
					case 7:	cards[cardIndex]+="8";		break;
					case 8:	cards[cardIndex]+="9";		break;
					case 9:	cards[cardIndex]+="10";		break;
					case 10:cards[cardIndex]+="Jack";	break;
					case 11:cards[cardIndex]+="Queen";	break;
					case 12:cards[cardIndex]+="King";	break;
				}
				cardIndex++;
			}
		}
		//Test mode
		int testMode=2;
		while(testMode==2){
		int card=1,i=0,n=1;
		System.out.printf("Go to Test Mode (0-No, 1-Yes): "); 
		testMode=input.nextInt();
		switch(testMode){
		case 0:	break;
		case 1:	while(card>0&&card<=52){
				
				System.out.printf("Input Card%d in your deck (0 to end): ",n++);
				card=input.nextInt();
				cardsOrder[i++]=card-1;
				}
				break;
		default:System.out.println("You must input 0 or 1!");
				testMode=2;
				break;
			}
		}
		//Game feature start
		while(players==0){
		System.out.print("How many players? ");
		players=input.nextInt();
		if(players<1||players>3){
			players=0;
			System.out.println("Number of players must between 1 and 3!");
			}
		}
		cardsInHand=new int[players+1];
		playersCardsArray=new int[players][11];
		dealersCardsArray=new int[11];
		standed=new boolean[players];
		if(testMode!=1)
			cardsOrder=randomGenCard(cards);
		
		System.out.printf("Game started! (%d players) \n",players);
		System.out.println("======================================");
		for(int i=0;i<standed.length;i++)
			standed[i]=false;
		//everyone draw two cards
		for(int i=0;i<2;i++){
			for(int j=0;j<players;j++)
				drawCard(j);
			
			dealerDrawCard();
			}
		for(int i=0;i<players;i++){
			System.out.print("Player "+(i+1)+"'s Hand: [ ");
				for(int j=0;j<cardsInHand[i];j++){
						if(j==0){System.out.print("Unknown ");
						}else{
						System.out.print(cards[playersCardsArray[i][j]]+" ");
						}
					}
				
				System.out.println("] ");}
		
		System.out.print("Dealer's Hand: [ ");
		for(int i=0;i<cardsInHand[cardsInHand.length-1];i++){
			if(i==0){System.out.print("Unknown ");
			}else {
			System.out.print(cards[dealersCardsArray[i]]+" ");
			}
		}
		System.out.println("] ");
		
		while(!gameFinish()){
			System.out.printf("\nPlayers' Round (%d players)\n",players);
			System.out.println("======================================");
			for(int i=0;i<players;i++){
				printCards(i);
				chkStatus(i,finalGameChk);
				int desition=2;
				while(desition==2 && !standed[i] &&
						!blackJackOrBust(i)){
					System.out.printf("Player %d, do you want to Stand or Hit (0-Stand, 1-Hit)? ",i+1); 
					desition=input.nextInt();
					switch(desition){
					case 0:	standed[i]=true;break;
					case 1:	drawCard(i);
							printCards(i);
							chkStatus(i,finalGameChk);
							break;
					default:System.out.println("You must input 0 or 1!");
							desition=2;
							break;
					}
				}
			}
			//dealer's round
			System.out.printf("\nDealers' Round (%d players)\n",players);
			System.out.println("======================================");
			for(int i=0;i<players;i++)
				noDealersRound&=blackJackOrBust(i);
				
			if(noDealersRound){
				System.out.println("All players have won or lost the game!");}
			else{
				printDealersCards();
				if(getPoints(dealersCardsArray,cardsInHand[cardsInHand.length-1])<17){
					System.out.println("Lower than 17, add new cards!");
					dealerDrawCard();
					printDealersCards();
				}else if(getPoints(dealersCardsArray,cardsInHand[cardsInHand.length-1])<=21	){
					System.out.println("Higher than or equal to 17, stop add new cards!");
				}
			}
		}
			//Final Result
			finalGameChk=true;
			System.out.printf("\nFinal Result (%d players)\n",players);
			System.out.println("======================================");
			for(int i=0;i<players;i++){
				printCards(i);
				chkStatus(i,finalGameChk);
			}
			printDealersCards();
	}
	public static void printDealersCards(){
		System.out.print("Dealer's Hand: [ ");
		for(int i=0;i<cardsInHand[cardsInHand.length-1];i++)
			System.out.print(cards[dealersCardsArray[i]]+" ");
		
		System.out.println("] ");
	}
	public static void printCards(int i){
		System.out.print("Player "+(i+1)+"'s Hand: [ ");
		for(int j=0;j<cardsInHand[i];j++)
			System.out.print(cards[playersCardsArray[i][j]]+" ");
		
		System.out.print("] ");
	}
	public static void dealerDrawCard(){
		dealersCardsArray[cardsInHand[cardsInHand.length-1]++]=cardsOrder[drawedCards++];
	}
	public static void drawCard(int i){
		playersCardsArray[i][cardsInHand[i]++]=cardsOrder[drawedCards++];
	}
	public static int[] randomGenCard(String[]cardName){
		boolean[]	genedCards	=new boolean[52];
		int[]		cardsOrder	=new int[52],
					cards		=new int[52];
		for(int i=0;i<52;i++)
			cards[i]=i;
		for(int i=0;i<52;i++){
			int cardIndex=cards[(int)(Math.random()*(52-i))];
			genedCards[cardIndex]=true;
			cardsOrder[i]=cardIndex;
			int k=0;
			for(int j=0;j<52;j++){
				if(!genedCards[j])
				cards[k++]=j;
			}
		}	
		return cardsOrder;
	}
	
	public static boolean gameFinish(){
		for(int i=0;i<players;i++){
			if(!standed[i]&&!blackJackOrBust(i)){
				return false;}
			}
		return true;}
	public static boolean maxPointPlayer(int player){
		for(int i=0;i<players;i++){
			if(getPoints(playersCardsArray[i],cardsInHand[i])<=21&&
					getPoints(playersCardsArray[i],cardsInHand[i])>
					getPoints(playersCardsArray[player],cardsInHand[player])){
				return false;}
			}
		return true;}
	public static void chkStatus(int i){
		if(getPoints(playersCardsArray[i],cardsInHand[i])>21){
			System.out.println("Bust!");
		}else if(getPoints(playersCardsArray[i],cardsInHand[i])==21){
				if(getPoints(dealersCardsArray,cardsInHand[cardsInHand.length-1])==21){
					System.out.println("Push!");
				}else {
					System.out.println("BlackJack!");
				}
			}else System.out.println();
		}
	public static boolean blackJackOrBust(int i){
		return getPoints(playersCardsArray[i],cardsInHand[i])<21?false:true;
	}
	public static void chkStatus(int i,boolean finalGameChk){ 
		if(!blackJackOrBust(i)&&finalGameChk){
			if(maxPointPlayer(i)){
				if(getPoints(dealersCardsArray,cardsInHand[cardsInHand.length-1])<=21){
					if(getPoints(dealersCardsArray,cardsInHand[cardsInHand.length-1])<
							getPoints(playersCardsArray[i],cardsInHand[i])){
							System.out.println("Win!");
						}else if(getPoints(dealersCardsArray,cardsInHand[cardsInHand.length-1])==
								getPoints(playersCardsArray[i],cardsInHand[i])){
							System.out.println("Push!");
					}else System.out.println("Lose!");
				}else System.out.println("Win!");
			}else{
			System.out.println("Lose!");
			}
		}
		chkStatus(i);
	}
	public static int getPoints(int[]playersArray,int cardsInHand){
		int pointWithOutFirstAce=0,point=0;
		boolean haveAce=false,firstAceAdded=false;
		for(int i=0;i<cardsInHand;i++)
			switch (playersArray[i]%13){
			case 0: if(haveAce){
						pointWithOutFirstAce+=1;
					}else{
						haveAce=true;}			break;
			case 1: pointWithOutFirstAce+=2;	break;
			case 2: pointWithOutFirstAce+=3;	break;
			case 3: pointWithOutFirstAce+=4;	break;
			case 4: pointWithOutFirstAce+=5;	break;
			case 5: pointWithOutFirstAce+=6;	break;
			case 6: pointWithOutFirstAce+=7;	break;
			case 7: pointWithOutFirstAce+=8;	break;
			case 8: pointWithOutFirstAce+=9;	break;
			default:pointWithOutFirstAce+=10;	break;
			}
		
		for(int i=0;i<cardsInHand;i++)
			switch (playersArray[i]%13){
			case 0: if(firstAceAdded){
				point+=1;
				}else{
					point+=pointWithOutFirstAce>10?1:11;
					firstAceAdded=true;}
				break;
			case 1: point+=2;	break;
			case 2: point+=3;	break;
			case 3: point+=4;	break;
			case 4: point+=5;	break;
			case 5: point+=6;	break;
			case 6: point+=7;	break;
			case 7: point+=8;	break;
			case 8: point+=9;	break;
			default:point+=10;	break;
			}
		return point;
	}
}