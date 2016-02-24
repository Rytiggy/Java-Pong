
import java.io.Serializable;

/**
*  @author Ryan Mason, Bre Dionne ,Arther Burgin, Chris Dumlao
*  due Date: 4/4/14
*  @version: 1.0
*  Title: Pong getters and setters  
*  This Server will be used to accept a message OR a GameMessage from the client then apply game logic 
*/
public class GameMessage implements Serializable{

	//game variables 
	public int player1Y , player2Y , player1X , player2X , ballX , ballY , player ,player1Score , player2Score , winner;
	//Chat variables 
	public String message  , messageTime; 
	public int messagePlayer;
	
/**
*  Constructor of GameMessage class
*
*  @param   _player1Y   y-coordinates of first player
*  @param   _player2Y   y-coordinates of second player
*  @param   _player1X   x-coordinates of fist player
*  @param   _player2X   x-coordinates of second player
*  @param   _ballX   x-coordinates of ball
*  @param   _ballY   y-coordinates of ball
*  @param   _player  integer identifying player number
*  @param   _player1Score  score of first player
*  @param   _player2Score  score of second player
*/
	public  GameMessage( int _player1Y ,int _player2Y ,int _player1X ,int _player2X ,int _ballX ,int _ballY , int _player ,int _player1Score ,int _player2Score ){
		player1Y = _player1Y;
		player2Y = _player2Y;
		player1X = _player1X;
		player2X = _player2X;
		ballX = _ballX;
		ballY = _ballY;
		player = _player;
		player1Score = _player1Score;
		player2Score = _player2Score;
	}
   
   /**
   *  Returns winner of the game
   *
   *  @return  winner   integer identifying winner of the game
   */
	public int getWinner() {
		return winner;
	}
   
   /**
   *  Sets the winner of the game
   *
   *  @param   winner   integer identifying winner of the game
   */
	public void setWinner(int winner) {
		this.winner = winner;
	}
   
   /**
   *  Receives chat messages
   * 
   *  @return  message  message from chat
   */
	public String getMessage() {
		return message;
	}
   
   /**
   *  Sets message in text area
   *
   *  @param   message  string to be set in text area
   */
	public void setMessage(String message) {
		this.message = message;
	}
   
   /**
   *  Identifies player who sent a message
   *
   *  @return  messagePlayer  integer identifying player who sent a message
   */
	public int getMessagePlayer() {
		return messagePlayer;
	}
   
   /**
   *  Sets player who sent a message
   *
   *  @param   messagePlayer  sets integer identifying player who sent a message
   */
	public void setMessagePlayer(int messagePlayer) {
		this.messagePlayer = messagePlayer;
	}
   
   /**
   *  Returns time of message for chat display
   *
   *  @return  messageTime time at which a message is sent
   */
	public String getMessageTime() {
		return messageTime;
	}
   
   /**
   *  Sets string to display time of message
   *
   *  @param   messageTime string of time of message sent
   */
	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}
   
   /**
   *  Returns score of player 1
   *
   *  @return  player1Score   score of first player
   */
	public int getPlayer1Score() {
		return player1Score;
	}
   
   /**
   *  Sets the score of the first player
   *
   *  @param   player1Score   score of the first player
   */
	public void setPlayer1Score(int player1Score) {
		this.player1Score = player1Score;
	}
   
   /**
   *  Returns score of player 2
   *
   *  @return  player2Score   score of second player
   */
	public int getPlayer2Score() {
		return player2Score;
	}
   
   /**
   *  Sets score of player 2
   *
   *  @param   player2Score   score of second player
   */
	public void setPlayer2Score(int player2Score) {
		this.player2Score = player2Score;
	}
   
   /**
   *  Identifies player
   *  
   *  @return  player   integer identifying player
   */
	public int getPlayer() {
		return player;
	}
   
   /**
   *  Sets player
   *
   *  @param   player   integer indentifying player
   */
	public void setPlayer(int player) {
		this.player = player;
	}
   
   /**
   *  Returns y-coordinates of first player
   *
   *  @return  player1Y  y-coordinates of first player
   */
	public int getPlayer1Y() {
		return player1Y;
	}
   
   /**
   *  Sets y-coordinates of first player
   *
   *  @param   player1Y y-coordinates of first player
   */
	public void setPlayer1Y(int player1y) {
		player1Y = player1y;
	}
   
   /**
   *  Returns y-coordinates of second player
   *
   *  @return  player2Y y-coordinates of second player
   */
	public int getPlayer2Y() {
		return player2Y;
	}

   /**
   *  Sets y-coordinates of second player
   *
   *  @oaram   player2Y y-coorinates of second player
   */
	public void setPlayer2Y(int player2y) {
		player2Y = player2y;
	}
   
   /**
   *  Returns x-coordinates of first player
   *
   *  @return  player1X x-coordinates of first player
   */
	public int getPlayer1X() {
		return player1X;
	}
   
   /**
   *  Sets x-coordinates of first player
   *  
   *  @param   player1X x-coordinates of first player
   */
	public void setPlayer1X(int player1x) {
		player1X = player1x;
	}
   
   /**
   *  Returns x-coordinates of second player
   *
   *  @return  player2X x-coordinates of second player
   */
	public int getPlayer2X() {
		return player2X;
	}

   /**
   *  Sets x-coordinates of second player
   *
   *  @param player2X   x-coordinates of second player
   */
	public void setPlayer2X(int player2x) {
		player2X = player2x;
	}

   /**
   *  Returns x-coordinates of ball
   *
   *  @return ballX  x-coordinates of ball
   */
	public int getBallX() {
		return ballX;
	}
   
   /**
   *  Sets x-coordinates of ball
   *
   *  @param   ballX x-coordinates of ball
   */
	public void setBallX(int ballX) {
		this.ballX = ballX;
	}
   
   /**
   *  Gets y-coordinates of ball
   *
   *  @return  ballY y-coordinates of ball
   */
	public int getBallY() {
		return ballY;
	}

   /**
   *  Sets y-coordinates of ball
   *
   *  @param ballY   y-coordinates of ball
   */
	public void setBallY(int ballY) {
		this.ballY = ballY;
	}
	
	
}
