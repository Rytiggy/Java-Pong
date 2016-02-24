
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

/**
*  @author Ryan Mason, Bre Dionne ,Arther Burgin, Chris Dumlao
*  due Date: 4/4/14
*  @version: 1.0
*  Title: Pong Server
*  This Server will be used to accept a message OR a GameMessage from the client then apply game logic 
*/

public class PongServer{
	final int PORT = 16789;
	ServerSocket ss = null;
	Object message;
   ArrayList<Integer> connected = new ArrayList<Integer>();
   int num;
   int playernumodd = 0;
   int playernumeven = 0;
   int gamenum;
   JTextArea textArea;
   JPanel middle;


   /**
   *  Constructs GUI for server, displaying IP address of host, also constructs input/output streams and server sockets, etc.
   */
	public PongServer(){

		//server GUI
		JFrame window = new JFrame();
		middle = new JPanel(new GridLayout(2,1));

		JLabel jtField = new  JLabel(); 
      textArea = new JTextArea();
      JScrollPane ScrollTA = new JScrollPane(textArea);
      
      middle.scrollRectToVisible(ScrollTA.getBounds());
      textArea.setCaretPosition(textArea.getText().length());


		try  {

			jtField.setText("This IP is: "+ InetAddress.getLocalHost());
		}
		catch(UnknownHostException uhe){
			System.err.println(uhe.getMessage());
		}   
		middle.add(jtField);
      textArea.setEditable(false);
      middle.add(ScrollTA);
		window.add(middle);
		window.setVisible(true);
      window.setSize(355,450);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//end of gui




		try{
			ss = new ServerSocket(16789);

			// waits for client to connect, starts thread, 
         
			while(true){	
				Socket cs1 = null, cs2 = null;
				System.out.println("Waiting For Player");

				try {
					cs1 = ss.accept();
               if(playernumodd == 0){
                 playernumodd++;
                 connected.add(playernumodd);
                 append("Player " + playernumodd + " is currently connected \n");
               }
               else{
                 playernumodd = playernumodd+2;
                 connected.add(playernumodd);
                 append("\n");
                 append("Player " + playernumodd + " is currently connected \n");
               }
				} catch (IOException e) {
					e.printStackTrace();
				}
				ObjectInputStream in1 = null, in2 = null;
				ObjectOutputStream out1 = null, out2 = null;

				try{
					// takes in the string from the client Socket
					in1 = new ObjectInputStream(cs1.getInputStream());  
					out1 = new ObjectOutputStream(cs1.getOutputStream()); 
               
				}
				catch(IOException ioe){
					System.err.println("IOException");
					ioe.printStackTrace();
				}

				// print "player 1 connected, waiting for player 2"
				try {
					cs2 = ss.accept();
               playernumeven = playernumeven+2;
               connected.add(playernumeven);
               append("Player " + playernumeven + " is currently connected \n");
				} catch (IOException e) {
					e.printStackTrace();
				}
            
				// print "both players connected, starting game"
				// create new Game, add both player threads

				try{
					// takes in the string from the client Socket
					in2 = new ObjectInputStream(cs2.getInputStream());  
					out2 = new ObjectOutputStream(cs2.getOutputStream()); 
				}
				catch(IOException ioe){
					System.err.println("IOException");
					ioe.printStackTrace();
				}

				// Save this to an object and put in some kind of collection for later
		
					Game g	= new Game(cs1 , cs2, in1 , in2 ,out1, out2);
					g.start();	
			}
		}
		catch(IOException ioe){
			System.err.println(ioe.getMessage());
		}

	}
   
   //append method for chat
   public void append(String s)
   {
      textArea.append(s + "\n");
      middle.scrollRectToVisible(textArea.getBounds());
      textArea.setCaretPosition(textArea.getText().length());
   } 
   
   /**
   *  Main method
   */
	public static void main(String[] args){
		new PongServer();
	}

   /**
   *  Implements game logic
   */
	class ClientThread extends Thread {
		Socket cs;
		GameMessage game;
		ObjectInputStream in1;
		ObjectOutputStream out1;
		ObjectInputStream in2;
		ObjectOutputStream out2;
      int threadgame;
		public int player;
		public Object obj;
		public GameMessage clientGame;
      
   /**
   *  ClientThread constructor sets initial variables
   *
   *  @param   _cs   client socket
   *  @param   _in1  object input stream that collects information and requests from first client
   *  @param   _out1 object output stream that sends information to first client
   *  @param   _in2  object input stream that collects information and requests from second client
   *  @param   _out2 object output stream that sends information to second client
   *  @param   _game object of GameMessage class
   *  @param   threadname name of thread giving client thread a unique id
   */
		public ClientThread(Socket _cs , ObjectInputStream _in1, ObjectOutputStream _out1, ObjectInputStream _in2, ObjectOutputStream _out2 , GameMessage _game, int threadgame){
			game = _game;
			cs = _cs;
			in1 = _in1;
			out1 = _out1;
			in2 = _in2;
			out2 = _out2;
         this.threadgame = threadgame;

			player = game.getPlayer();

			updatePlayers(game);


		}
      /**
      *  Starts player and chat threads and checks for exceptions (disconnects, no player, etc.)
      */
		public void run() {
			try{

				//clients.add(out);

				int clientY = 0, serverY = 0;

				//print it to the client socket

				while(true){    
					//System.out.println("WHILE TRUE!");
					// read the objects from ois to obj
					if(player %2 != 0){
						obj = in1.readObject(); 
					}
					else if(player %2 == 0){
						obj = in2.readObject(); 
					}




					//System.out.println("RECEIVED OBJECT FROM CLIENT");
					// Determine what kind of object we got
					if(obj instanceof String){
						System.out.println("Got String");
						message = (String)obj;
						//set the message properties 
						game.setMessage((String)message);
						game.setMessagePlayer(player);
						game.setMessageTime(new SimpleDateFormat("hh:mm:ss").format(Calendar.getInstance().getTime()));

						//sends the message to all the clients
						updatePlayers(game);
						game.setMessage("");
						game.setMessagePlayer(0);
						game.setMessageTime("");

					}
					else if(obj instanceof GameMessage){

						clientGame = null;
						clientGame = (GameMessage)obj;

						boolean havePlayer = false;
						//Checks to see which player

						if(player %2 != 0){
							clientY = clientGame.getPlayer1Y();
							serverY = game.getPlayer1Y();
							havePlayer = true;
						}else if(player %2 == 0){
							clientY = clientGame.getPlayer2Y();
							serverY = game.getPlayer2Y();
							havePlayer = true;
						}




						if(havePlayer){
							// Decide if this is a valid request 

							// Check if (it should be + or - 10 because a client can only move 10 at a time)
							if(clientY == (serverY + 5) || clientY == (serverY - 5)){
								// Check to make sure paddles are in bounds
								if((clientY > 0) && clientY < 415 ){

									// if valid, then update our (server's) game object with new value
									if(player %2 != 0){
										game.setPlayer1Y(clientY);
									}
									else if(player %2 == 0){
										game.setPlayer2Y(clientY);
									}
								}else{
								}


								//System.out.println("Sending to client");

								if(player %2 != 0){
									game.setPlayer(playernumodd);
								}
								else if(player %2 == 0){
									game.setPlayer(playernumeven);
								}

								// Send object to clients
								//updatePlayers(game);
							}
						}
						else{
							System.out.println("ERROR: Player = 0");
						}

					}
				}
			}
			catch(ClassNotFoundException cnfe){
				System.err.println("ClassNotFoundException");
				cnfe.printStackTrace();
			}
			catch(IOException ioe){
				game.setMessage("NOTICE: Player Disconnected... Game Over!");
				updatePlayers(game);
				this.interrupt();
			}
		}


      /**
      *  Sends message to client whenever there is a change in paddle or ball position
      *
      *  @param   game  object of GameMessage class that includes accessors and mutators
      */
		public void updatePlayers(GameMessage game){
			try {
				game.setPlayer(playernumodd);
				out1.writeObject(game);
            
            
            game.setPlayer(playernumeven);
				out2.writeObject(game);
            
				out1.flush();
				out2.flush();
            
            out1.reset();
				out2.reset();
        

			} catch (IOException e) {
            for(int i = 0; i < connected.size(); i++){
               if(connected.get(i) == threadgame){
                  if(threadgame %2 != 0){
                     int temp = connected.indexOf(threadgame);
                     connected.remove(temp);
                     connected.remove(temp);
                     append("\n");
                     append("Disconnecting Player "+ threadgame + " from Server \n");
                     append("Disconnecting Player " + (threadgame+1) + " from Server \n");
                  }
                  else{
                     int temp2 = connected.indexOf(threadgame);
                     connected.remove(temp2);
                     connected.remove(temp2-1);
                     append("\n");
                     append("Disconnecting Player "+ threadgame + " from Server \n");
                     append("Disconnecting Player "+ (threadgame-1) + " from Server \n");
                  }
                  if(connected.size() > 0){
                     for(int j = 0; j < connected.size(); j++){
                        append("Player " + connected.get(j) + " is currently connected \n");
                     }
                  }
                  else{
                     append("There are no more players connected to the server \n");
                  }
               }
            }
			}
		}
	} 
   /**
   *  Returns IP address to display in server GUI and to be used by clients to connect
   *
   *  @param   s  Socket to get IP address from
   */
	public String getIP(Socket s){
		String ip = s.getInetAddress().toString();
		ip = ip.substring(1,ip.length());

		return ip;
	}





   /**
   *  Sets up the game at default beginning positions
   */
	public class Game extends Thread{
		private int  player1X = 10, player1Y = 210, player2X = 725, player2Y = 210, ballX = 370 , ballY=225 , player1Score , player2Score;
		private GameMessage game = new GameMessage(player1X , player1Y , player2X , player2Y , ballX ,  ballY , 0 , player1Score , player2Score);

		private Socket cs1 , cs2;

		ObjectOutputStream out1 , out2;
		ObjectInputStream in1  , in2; 

   /**
   *  Constructs Game
   *
   *  @param   _cs1  first client socket
   *  @param   _cs2  second client socket
   *  @param   _in1  object input stream for first client
   *  @param   _in2  object input stream for second client
   *  @param   _out1 object output stream for first client
   *  @param   _out2 object output stream for second client
   */
		public Game( Socket _cs1, Socket _cs2 , ObjectInputStream _in1 , ObjectInputStream _in2 , ObjectOutputStream _out1, ObjectOutputStream _out2){


			cs1 = _cs1;
			cs2 = _cs2;
			out1 =_out1;
			out2 = _out2;
			in1 = _in1;
			in2 = _in2;
		}
      
		@Override
      /**
      *  Sets the initial state of the board
      */
			public void run(){
				game.setPlayer1X(player1X);
				game.setPlayer1Y(player1Y);
				game.setPlayer2X(player2X);
				game.setPlayer2Y(player2Y);
				game.setBallX(ballX);
				game.setBallY(ballY);

            gamenum++;
            
				game.setPlayer(playernumodd);
				ClientThread ct1 = new ClientThread(cs1 , in1 , out1, in2 , out2 ,game, playernumodd);
				ct1.start();
            
				game.setPlayer(playernumeven);
				ClientThread ct2 = new ClientThread(cs2 ,in1 , out1, in2 , out2, game, playernumeven);
				ct2.start();
            
				append("Stating Game " + gamenum + " With: Player " +playernumodd + " and Player " + playernumeven);
            

				BallThread bt = new BallThread(out1, out2);
				bt.start();
			

		}

      /**
      *  Constantly updates the ball, its position, and its speed
      */
		class BallThread extends Thread {

			ObjectOutputStream outl;
			ObjectOutputStream out2;

			//set how fast the ball moves 
			int ballMovementSpeed = 2;
			//set the winning score number
			int winningScore = 5;
         
      /**
      *  Constructs thread for ball and sets output streams
      *
      *  @param   _out1 object output stream for first client
      *  @param   _out2 object output stream for second client
      */
			public BallThread(ObjectOutputStream _outl , ObjectOutputStream _out2){
				outl = _outl;
				out2 = _out2;

			}

         /**
         *  Runs ball thread and its movement, etc.
         */
			@Override
			public void run() {

				int counterP1 = 0 , counterP2 = 0;

				boolean y = true, x = true;
				// Update ball location and send updated board to players

				while(!Thread.currentThread().isInterrupted()){
					//	Y Ball movement 
					// if y is true, y value is going ++
					// if Y is false, y value is going --
					if(y){
						game.setBallY(game.getBallY() - ballMovementSpeed);
						//top wall
						if(game.getBallY() <= 0){
							y = false;
						}
					} else {
						game.setBallY(game.getBallY()+ ballMovementSpeed);
						//Bottom wall
						if( game.getBallY() >= 450){
							y = true;

						}
					}


					//	X Ball movement 	
					// if x is true, x value is going ++ towards player 1
					// if x is false, x value is going -- towards player 2
					if(x){
						game.setBallX(game.getBallX() - ballMovementSpeed);
						if(game.getBallX() <= game.getPlayer1X() + 10){
							if(game.getBallY() <= game.getPlayer1Y()+45 && game.getBallY() >= game.getPlayer1Y()){
								//System.out.println("Setting x to false. going -->");
								x = false;
							}
						}
						//if the ball hits player 1 back wall
						if(game.getBallX() <= 0){
							x = false;
							counterP1 ++;
							game.setPlayer2Score(counterP1);
							//System.out.println("Y: "+  game.getBallY() + " X : " +  game.getBallX() + "counterP1: " +counterP1);

							//when player gets to a certain score 
							if(counterP1 >= winningScore){
								ballMovementSpeed = 0;
								game.setWinner(1);
							}


						}
					} else {
						game.setBallX(game.getBallX()+ ballMovementSpeed);

						//checks that the ball is hitting the paddle
						if(game.getBallX() >= game.getPlayer2X() - 10){
							if(game.getBallY() <= game.getPlayer2Y()+45 && game.getBallY() >= game.getPlayer2Y()){
								//System.out.println("Setting x to false. going <--");
								x = true;
								
							}
						}

						//if the ball hits player 2 back wall
						if( game.getBallX() >= 735){
							x = true;
							counterP2++;
							game.setPlayer1Score(counterP2);
							//System.out.println("Y: "+  game.getBallY() + " X : " +  game.getBallX() + "counterP2: "+  counterP2);

							//when player gets to a certain score 
							if(counterP2 >= winningScore){
								ballMovementSpeed = 0;
								game.setWinner(2);
							}

						}
					}
					try {

						game.setPlayer(playernumodd);
						outl.writeObject(game);

						game.setPlayer(playernumeven);
						out2.writeObject(game);

						out1.flush();
						out2.flush();
                    //reseting the cache becasue same obj is being sent 
						out1.reset();
						out2.reset();

					} catch (IOException e) {
						game.setMessage("NOTICE: Player Disconnected... Game Over!");
						try {
							outl.writeObject(game);
							out2.writeObject(game);
						} catch (IOException e1) {}
						Thread.currentThread().interrupt();


					}
					try {
						sleep(10);
					} catch (InterruptedException e) {
					Thread.currentThread().interrupt();

					}
				}
			}
		}
	}
}
