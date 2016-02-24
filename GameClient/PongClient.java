import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;


/**
*  @author Ryan Mason, Bre Dionne ,Arther Burgin, Chris Dumlao
*  due Date: 4/4/14
*  @version: 1.0
*  Title: Pong Client
*  This Server will be used to accept a message OR a GameMessage from the client then apply game logic 
*/
/**
* This class is the client code for the Pong game. It creates the GUI and connects to the server.  
* The client conducts some of the game logic and runs most of the aesthetics.
*/
public class PongClient {

	//veribles 
   String serverIP; 
   private final int PORT = 16789;
   final JTextArea LOG;
   final JTextField INPUT;
   JPanel panel;
   Socket s;
   public OutputStream os;
   public ObjectOutputStream out;
   public InputStream is;
   public ObjectInputStream ois;
                              // player1Y player2Y player1X player2XballX ballY player player1Score player2Score 
   public GameMessage game = new GameMessage(210 , 210 , 10 , 725 , 370 ,  225 , 0 , 0 , 0);
   public int player;

/**
* This class is the client code for the Pong game. It creates the GUI and connects to the server.  
* The client conducts some of the game logic and runs most of the aesthetics.
*/

/**
*  Main method
*/
   public static void main(String[] args){
      new PongClient();
   }
   
   public PongClient(){
   
   	
   	
   	//Creation of the GUI
      JFrame frame = new JFrame("Epic Pong");
   
   	//creation of the gui
      JFrame window = new JFrame();
      panel = new JPanel(new BorderLayout());
      JButton sendButton = new JButton("Send");
      sendButton.setEnabled(false);
   
   	//frame is the game JFrame and window is the chat JFrame
   	//=============================================================================================================================================================
      JMenuBar menuBar = new JMenuBar();
   
      JMenu fileMenu = new JMenu("File");
      menuBar.add(fileMenu);
   
      JMenuItem exitMenu = new JMenuItem("Exit");
      fileMenu.add(exitMenu);
   
      window.setJMenuBar(menuBar);
   
   
   
   
   
   
   	// Log (main text on top)
      LOG = new JTextArea();
      JScrollPane scrollPaneLOG = new JScrollPane(LOG);
      LOG.setRows(25);
      LOG.setEditable(false);
   
   	// INPUT (user input on bottom)
      INPUT = new JTextField();
      JScrollPane scrollPaneINPUT = new JScrollPane(INPUT);
      INPUT.setMinimumSize(new Dimension(250, 10));
      INPUT.setPreferredSize(new Dimension(250, 10));   
   
      panel.add(scrollPaneLOG , BorderLayout.NORTH);
      panel.add(scrollPaneINPUT , BorderLayout.CENTER);
      panel.add(sendButton , BorderLayout.EAST);
   
   	//settings for the chat 
   
   	//gets the center of the screen
      int screenHgight=((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2);
      int screenWidth=((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2);
   
   
   	//settings 
      window.add(panel);
      window.setSize(335,500);	
      window.setLocation(screenWidth + 376, screenHgight-270);
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.setVisible(true);
   
   	//defaults to localhost, rules , and input for server ip
      serverIP = JOptionPane.showInputDialog("Pong Rules: \n 1) Use your up and down arrows to move. \n 2) Do not let the ball hit your wall. \n 3) First player to 10 wins."+
         	"\n--------------------------------------------------------\n Please enter the server's IP address: ");
   
   	//=============================================================================================================================================================
   
   //output stream
      try{
         s = new Socket(serverIP , PORT);
         os = s.getOutputStream();
      
         sendButton.setEnabled(true);
      }
      catch(UnknownHostException ukh){
      }
      catch(IOException ioe){
      }
   
   
   	// Create output stream
      try {
         out = new ObjectOutputStream(os);
      
      	// Create input stream
         is = s.getInputStream();
         ois = new ObjectInputStream(is); 
      
      } 
      catch (IOException /*|NullPointerException*/ npe) {
         System.exit(0);
      }
   
   
   	//setting for the game
      Painting p = new Painting(out , ois);
      p.setFocusable( true );
      frame.add(p);
   	//paddle thread
      Thread pThread = new Thread(p);
      pThread.start();
   	//frame.add(gamePanel);
      frame.setResizable(false);
      frame.setSize(751, 491);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   
   	//buttons with action listeners 
      sendButton.addActionListener(
            new ActionListener(){
               /**
            *  Calls sendMessages method when send button is clicked
            *
            *  @param   ae the action event that occurs when send button is clicked
            */
               public void actionPerformed(ActionEvent ae){
                  sendMessage();
               
               }
            });
   
      exitMenu.addActionListener(
            new ActionListener(){
               /**
            *  Exits system when exit button is clicked
            *
            *  @param   ae action event when exit button is clicked
            */
               public void actionPerformed(ActionEvent ae){
                  System.exit(0);
               }
            });
   
      INPUT.addKeyListener(
            new KeyAdapter(){
               /**
            *  Action occuring when key is typed
            *
            *  @param   e  the key event that occurs when key is typed
            */
               public void keyTyped(KeyEvent e){
               //System.out.println(e.getKeyCode());
               }
               /**
            *  Sets keyCode equal to key event key code
            *  If user presses enter, message will send
            *
            *  @param   e  the key event that occurs when key is pressed
            */
               public void keyPressed(KeyEvent e){
               //System.out.println(e.getKeyCode());
                  int keyCode = e.getKeyCode();
               
               //if they press the enter key
                  if(keyCode == KeyEvent.VK_ENTER){
                     sendMessage();	
                  }
               }
            });
   
   
   }


   /**
* Writes the senderMsg String object to the output stream, sending the message to the other clients.
*/
   public void sendMessage(){
   	// TEXT_INPUT: takes in the users input and sends it to the server\
      String senderMsg = INPUT.getText();
      try {
         out.writeObject(senderMsg);
         out.flush();							
         INPUT.setText("");  
      }
      catch(UnknownHostException uhe) {
         append("Unable to connect to host.");
      
      }
      catch(IOException ie) {   
         ie.printStackTrace();
      }
   
   }

   /**
*  Appends the chat messages in the text area, adding it to the previous shown messages.
*  @param   s  the accumlating String of client messages
*/
   public void append(String s)
   {
      LOG.append(s + "\n");
      panel.scrollRectToVisible(LOG.getBounds());
      LOG.setCaretPosition(LOG.getText().length());
   }   
   
   /**
*  The Painting class includes the aesthetics and requests the information from the server.
*/
   class Painting extends JPanel implements Runnable {
   	//x and y for paddles p1 and p2 and the ball
      ObjectOutputStream out;
      ObjectInputStream in;
   
      public Painting( ObjectOutputStream _out , ObjectInputStream _in){
         out = _out;
         in = _in;
      
      	
      	
      
         addKeyListener(
               new KeyAdapter(){
                  /**
               *  Executes no action when key is typed
               *
               *  @param   e  event that occurs when key is typed
               */
                  public void keyTyped(KeyEvent e){
                  //System.out.println(e.getKeyCode());
                  }
               
               
                  /**
               *  Controls operation of arrow keys, whick control the paddles hitting the ball.
               *
               *  @param   e  key pressed is down arrow or up arrow, client's paddle will move likewise
               */
                  public void keyPressed(KeyEvent e){
                  //System.out.println(e.getKeyCode());
                     int keyCode = e.getKeyCode();
                  
                     if(keyCode == KeyEvent.VK_DOWN){
                        if(player == 1){
                           game.setPlayer1Y(game.getPlayer1Y() + 5);
                        }
                        else if(player == 2){
                           game.setPlayer2Y(game.getPlayer2Y() + 5);
                        }
                     }
                     else if(keyCode == KeyEvent.VK_UP){
                        if(player == 1){
                           game.setPlayer1Y(game.getPlayer1Y() - 5);
                        }
                        else if(player == 2){
                           game.setPlayer2Y(game.getPlayer2Y() - 5);
                        }
                     }
                  
                  //send to server x and y to server "player1Y"
                     try {
                     //System.out.println("P1: " +game.getPlayer1Y() + "P2: "+ game.getPlayer2Y());
                        out.writeObject(game);
                        out.flush();
                     //To Clear the objectOutputStram cache. 
                        out.reset();
                     
                     } 
                     catch (IOException e1) {
                        e1.printStackTrace();
                     }
                  
                  
                  }
                  
                  /**
               *  No action occurs upon key released
               *
               *  @param   e  event that occurs upon key release
               */                  public void keyReleased(KeyEvent e){
                  //System.out.println(e.getKeyCode());
                  }
               
               
               });
      }
   	
      /**
   *  Employs Graphics class to paint the board, paddles, and ball
   *
   *  @param   g  object of the Graphics class that enables painting
   */
      public void paintComponent(Graphics g){
         super.paintComponent(g);
      
      	//BackGound
         g.fillRect(0,0,751, 491);
         g.setColor(Color.cyan);
         g.drawLine(374, 1, 374, 490);
         g.drawLine(375, 1, 375, 490);
         g.drawLine(376, 1, 376, 490);
         
         //ball
         g.setColor(Color.yellow);
         g.fillOval(game.getBallX(), game.getBallY(), 10,10);
         repaint();
      
      	
      	//repaints the score 
         int tmpScoreP1 = game.getPlayer1Score();
         int tmpScoreP2 = game.getPlayer2Score();
         g.setColor(Color.green);
         g.drawString("Player 1 Score: < " + tmpScoreP1 +" >", 50,10);
         g.drawString("Player 2 Score: < " + tmpScoreP2 +" >", 600,10);
      	
      }
   	
   	
   
      @Override
      /*
      *  Constantly accepts information from the server and sets the information to the board using the GameMessage class.
      */
      public void run() {
      	//Receive and repaint here using the new values 
         Object obj;
      	
         boolean keepGoing = true;
      	
         while(keepGoing == true){
         	
            try {
            	//System.out.println("Waiting for message from server...");
               obj = in.readObject();
            
               if( obj instanceof GameMessage){
                  game = (GameMessage)obj;
                  player = game.getPlayer();
                  try{
                     if(game.getMessage().length() > 0){
                        String tempSender;
                        if(game.getMessagePlayer() == 0){
                           tempSender = " Server: ";
                        }
                        else{
                           tempSender =" Player: "+ game.getMessagePlayer()+": " ;
                        }
                        append( "["+game.getMessageTime()+"]" +tempSender +game.getMessage());
                        game.setMessage("");
                        game.setMessagePlayer(0);
                        game.setMessageTime("");
                     }
                  }
                  catch(NullPointerException npe){
                  	
                  }
               }
               
               else{
                  System.out.println("This is not a GameMessage");
               }
            } 
            catch (ClassNotFoundException e) {
               append("Server: Player Disconnected. Game Over!");
               Thread.currentThread().interrupt();
            }
            catch(NullPointerException npe){
            	
            }
            catch(IOException ioe){
               append("Server: Player Disconnected. Game Over!");
               Thread.currentThread().interrupt();
            }
         
         	//System.out.println("Got Object from server: " + game.getPlayer1Y());
            
            repaint();
         
            if(game.getWinner() == 1){
               keepGoing = false;
               JOptionPane.showMessageDialog(null, "Player 2 wins!");

            	System.exit(0);
            	
            	
            }
            else if(game.getWinner() ==2 ){
               keepGoing = false;
               JOptionPane.showMessageDialog(null, "Player 1 wins!");
                  System.exit(0);
               }
         
         	
         }
      }
      
      /**
   *  Paints the ball to flash random colors during gameplay
   *
   *  @param   g  object of Graphics class used to paint ball
   */
      public void paint(Graphics g){
         super.paint(g);
         Random random = new Random();
         final float hue = random.nextFloat();
         final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
         final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
         //Color.getHSBColor(hue, saturation, luminance);
         //paddles 
         g.setColor(Color.getHSBColor(hue, saturation, luminance));
         g.fillRect(game.getPlayer1X(), game.getPlayer1Y(), 10, 45);
         g.fillRect(game.getPlayer2X(), game.getPlayer2Y(), 10, 45);
         repaint();
      }
   }
   
}

