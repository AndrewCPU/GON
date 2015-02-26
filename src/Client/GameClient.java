package Client;

import Packets.*;
import Server.GameServer;
import Utilities.Player;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import javax.sound.sampled.*;
import javax.sound.sampled.spi.AudioFileReader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by Andrew on 2/21/2015.
 */
public class GameClient extends JFrame implements KeyListener,MouseListener {
    boolean isHolding = false;
    Point point1;
    Point point2;
    public void mouseEntered(MouseEvent event)
    {

    }
    public void mouseExited(MouseEvent event)
    {

    }
    public void mousePressed(MouseEvent event)
    {
        isHolding = true;
        point1 = event.getPoint();
        BallPacket ballPacket = new BallPacket();
        ballPacket.setY((int)event.getPoint().getY());
        ballPacket.setX((int)event.getPoint().getX()+ 20);
        ballPacket.setId(0);
        ballPacket.setSize(new Random().nextInt(11 - 1) + 1);
        client.sendTCP(ballPacket);
    }
    public void mouseClicked(MouseEvent event)
    {

    }

    public void mouseReleased(MouseEvent event)
    {
        isHolding = false;
        point2 = event.getPoint();
        if(username.equalsIgnoreCase("Andrewcpu"))
        {
            BlockPacket blockPacket = new BlockPacket();
            blockPacket.setX((int)point1.getX());
            blockPacket.setY((int) point1.getY());

            blockPacket.setX2((int) point2.getX());
            blockPacket.setY2((int) point2.getY());
            client.sendTCP(blockPacket);
        }

    }

    public void keyReleased(KeyEvent event) {

    }

    public void keyPressed(KeyEvent event) {
        MovePacket packet = new MovePacket();
        packet.setUsername(username);
        int x = getPlayer(username).getX();
        int y = getPlayer(username).getY();

        if (event.getKeyChar() == 'a') {
            x -= 10;
        }
        if (event.getKeyChar() == 'd') {
            x += 10;
        }
        if (event.getKeyChar() == 'w') {
            y -= 10;
        }
        if (event.getKeyChar() == 's') {
            y += 10;
        }
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            final JFrame yesno = new JFrame("Are you sure you want to exit?");
            JButton yes = new JButton("YES");
            JButton no = new JButton("NO");
            yesno.setBounds(200,200,500,500);
            no.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    yesno.dispose();
                }
            });
            yes.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            yesno.setLayout(null);
            yes.setBounds(0,100,yesno.getWidth() / 2, 100);
            no.setBounds(yesno.getWidth() / 2 ,100,yesno.getWidth() / 2, 100);
           yesno.add(new JLabel("Are you sure you want to exit?"));
            yesno.add(yes);
            yesno.add(no);
            yesno.setVisible(true);


        }


        if (event.getKeyChar() == 't') {
            add(messageBox);
            messageBox.setVisible(true);
            repaint();

        }
        if(event.getKeyCode()==KeyEvent.VK_UP)
        {
            if(messageAt - 1 >=0)
            {
                chat.setText("(" + (messageAt - 1) + "/" + previousMessages.size() + ") " + previousMessages.get(messageAt - 1));
                messageAt--;
            }
        }
        if(event.getKeyCode()==KeyEvent.VK_DOWN)
        {
            if(messageAt + 1 <=previousMessages.size())
            {
                chat.setText("(" + (messageAt + 1 )+ "/" + previousMessages.size() + ") " + previousMessages.get(messageAt + 1));
                messageAt++;
            }
        }
        if (getPlayer(username) == null) {
            System.out.print("ERROR!");
        } else {
            repaint();

            packet.setX(x);
            packet.setY(y);
            client.sendTCP(packet);
            //  System.out.print("SENT");
        }


    }
    int messageAt = 0;

    public void keyTyped(KeyEvent event) {

    }

    public static void main(String[] args) {



        if(args.length>0)
        {
            new GameServer();
        }
        else
        {
            new GameClient();
        //
         }



    }

    HashMap<Integer, Player> playerMap = new HashMap<Integer, Player>();
    Client client = new Client();
    List<String> previousMessages = new ArrayList<String>();
    public Player getPlayer(String name) {
        for (Player p : playerMap.values()) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public int getAmount(String name) {
        int x = 0;
        for (Player p : playerMap.values()) {
            if (p.getName().equalsIgnoreCase(name)) {
                x++;
            }
        }


        return x;
    }

    JLabel box = new JLabel("");
    ImageIcon coinstats = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/coinstats.png")));
    ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/Coin.png")));
    ImageIcon player1 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/player1.png")));
    ImageIcon player2 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/player2.png")));
    ImageIcon player3 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/player3.png")));
    ImageIcon player4 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/player4.png")));
    ImageIcon player5 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/player5.png")));
    ImageIcon player6 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/player6.png")));
    ImageIcon player7 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/player7.png")));
    ImageIcon player8 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/player8.png")));
    ImageIcon player9 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/player9.png")));
    ImageIcon player10 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/player10.png")));
    ImageIcon player11 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/player11.png")));


    ImageIcon coinIcon = new ImageIcon(icon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
    public void beginMusic() throws Exception
    {

        AudioPlayer player = AudioPlayer.player;
        AudioStream stream;
        AudioData data;
        ContinuousAudioDataStream loop = null;
        InputStream input = null;
        try
        {
            input = new FileInputStream("C:\\Users\\Andrew\\AppData\\Roaming\\GON\\music.wav");
            stream = new AudioStream(input);
            data = stream.getData();
            loop = new ContinuousAudioDataStream(data);
            player.start(loop);
        }
        catch(IOException err)
        {
            err.printStackTrace();
            System.out.println("fle not found");
        }

    }
    public void startClient() {




        client.start();
        try {
            //173.56.93.54
            client.connect(5000, "173.56.93.54", 45666, 45777);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if(object instanceof BlockPacket)
                {
                    BlockPacket blockPacket = (BlockPacket)object;
                    JLabel block = new JLabel();
                    block.setOpaque(true);
                    block.setBackground(Color.BLACK);
                    Rectangle bounds = new Rectangle(new Point(blockPacket.getX(),blockPacket.getY()));
                    bounds.add(new Point(blockPacket.getX2(),blockPacket.getY2()));
                    block.setBounds(bounds);
                    add(block);
                    blocks.add(block);
                    repaint();
                }
                if(object instanceof BallPacket)
                {
                    int id = ((BallPacket)object).getId();
                    int y = ((BallPacket)object).getY();

                    if(balls.containsKey(id))
                    {
                        JLabel label = balls.get(id);
                        label.setOpaque(true);
                        label.setBackground(Color.BLUE);
                        label.setBounds(0,0,((BallPacket)object).getSize(), ((BallPacket)object).getSize());
                        label.setLocation(label.getX(),y);
                        repaint();
                    }
                    else
                    {
                        JLabel label = new JLabel();
                        label.setBounds(((BallPacket)object).getX(), y, ((BallPacket)object).getSize(), ((BallPacket)object).getSize());
                        add(label);
                        balls.put(id ,label);
                    }


                }
                if(object instanceof BlockBreakPacket)
                {
                    //BlockBreakPacket blockBreakPacket = (BlockBreakPacket)object;
                    BlockBreakPacket blockPacket = (BlockBreakPacket)object;
                   // client.se(blockPacket);
                 //   Rectangle bounds = new Rectangle();

                    JLabel label = null;
                    for(JLabel b : blocks)
                    {
                        if(b.getBounds().contains(new Point(blockPacket.getX1(),blockPacket.getY1())))
                        {
                            label = b;
                        }
                    }
                    remove(label);
                    blocks.remove(label);

                    //blocks.remove(label);
                }
                if (object instanceof ScorePacket) {
                    ScorePacket packet = (ScorePacket) object;
                    score = packet.getScore();
                    updateScoreLabel();
                }
                if (object instanceof BoxPacket) {
                    Random random = new Random();

                    // box.setbou
                    box.setOpaque(false);

                    //box.setBackground(new Color(random.nextInt(256 - 100) + 100,random.nextInt(256 - 100) + 100,random.nextInt(256 - 100) + 100 ));
                    box.setIcon(coinIcon);
                    BoxPacket boxPacket = (BoxPacket) object;
                    box.setBounds(boxPacket.getX(), boxPacket.getY(), boxPacket.getWidth(), boxPacket.getHeight());
                    repaint();
                   // System.out.print("BOXPACKET\n");
                    add(box);
                }

                if (object instanceof PlayerPacket) {
                    PlayerPacket playerPacket = (PlayerPacket) object;

                    if (getAmount(playerPacket.getUsername()) >= 1) {

                    } else {
                        Player p = new Player();
                        p.setId(playerPacket.getId());
                        p.setY(playerPacket.getY());
                        p.setX(playerPacket.getX());
                        p.setName(playerPacket.getUsername());
                        p.setPlayerNumber(playerPacket.getPlayerNumber());
                        p.getPlayerImage().setBounds(playerPacket.getX(), playerPacket.getY(), 100, 100);
                        p.getNameTag().setBounds(playerPacket.getX(), playerPacket.getY(), 100, 20);
                        p.getPlayerImage().setOpaque(false);
                        // p.getPlayerImage().setBorder(BorderFactory.createLineBorder(Color.BLACK,10));
                  //      System.out.print(p.getPlayerNumber() + "\n");
                        if (playerPacket.getPlayerNumber() == 1) {
                            p.getPlayerImage().setIcon(new ImageIcon(player1.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
                        }
                        if (playerPacket.getPlayerNumber() == 2) {
                            p.getPlayerImage().setIcon(new ImageIcon(player2.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
                        }

                        if (playerPacket.getPlayerNumber() == 3) {
                            p.getPlayerImage().setIcon(new ImageIcon(player3.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
                        }

                        if (playerPacket.getPlayerNumber() == 4) {
                            p.getPlayerImage().setIcon(new ImageIcon(player4.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
                        }

                        if (playerPacket.getPlayerNumber() == 5) {
                            p.getPlayerImage().setIcon(new ImageIcon(player5.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
                        }

                        if (playerPacket.getPlayerNumber() == 6) {
                            p.getPlayerImage().setIcon(new ImageIcon(player6.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
                        }

                        if (playerPacket.getPlayerNumber() == 7) {
                            p.getPlayerImage().setIcon(new ImageIcon(player7.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
                        }

                        if (playerPacket.getPlayerNumber() == 8) {
                            p.getPlayerImage().setIcon(new ImageIcon(player8.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
                        }

                        if (playerPacket.getPlayerNumber() == 9) {
                            p.getPlayerImage().setIcon(new ImageIcon(player9.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
                        }

                        if (playerPacket.getPlayerNumber() == 10) {
                            p.getPlayerImage().setIcon(new ImageIcon(player10.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
                        }

                        if (playerPacket.getPlayerNumber() == 11) {
                            p.getPlayerImage().setIcon(new ImageIcon(player11.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
                        }


                        //p.getPlayerImage().setIcon(new ImageIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/player" + p.getPlayerNumber() + ".png"))).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
                        // p.getPlayerImage().setBackground(new Color(255,0,0));
                        add(p.getPlayerImage());
                        add(p.getNameTag());
                        playerMap.put(playerPacket.getId(), p);
                       // System.out.print("PLAYERPACKET\n");
                        System.out.print(p.getName() + "::" + p.getId() + "::" + p.getX() + ":" + p.getY() + "\n");
                    }


                }
                if (object instanceof LeavePacket) {
                    LeavePacket packet = (LeavePacket) object;
                    Player p = getPlayer(packet.getUsername());

                    //if()
                    remove(p.getPlayerImage());
                    remove(p.getNameTag());
                    playerMap.remove(p.getId());
                    //System.out.print("LEAVEPACKET\n");
                    repaint();
                }
                if (object instanceof MessagePacket) {
                    MessagePacket packet = (MessagePacket) object;
                    chat.setText(packet.getName() + ": " + packet.getMessage());
                    previousMessages.add(packet.getName() + ": " + packet.getMessage());
                    messageAt = previousMessages.size();


                    repaint();
//                    JFrame frame = new JFrame(packet.getName() + " says...");
//                    frame.add(new JLabel(packet.getMessage()));
//                    frame.setBounds(0,0,100,100);
//                    frame.setVisible(true);
//                    System.out.print("MESSAGEPACKET\n");
                }
                if (object instanceof MovePacket) {
                    MovePacket packet = (MovePacket) object;
                    Player p = getPlayer(packet.getUsername());
                    if (p != null) {
                        p.setX(packet.getX());
                        p.setY(packet.getY());
                        //       p.getPlayerImage().setOpaque(true);
                        //     p.getPlayerImage().setBackground(new Color(250, 0,0));
                        //  System.out.print("MOVEPACKET\n");
                    }

                }
            }
        });
        Kryo kryo = client.getKryo();
        kryo.register(JoinPacket.class);
        kryo.register(LeavePacket.class);
        kryo.register(MessagePacket.class);
        kryo.register(MovePacket.class);
        kryo.register(PlayerPacket.class);
        kryo.register(BoxPacket.class);
        kryo.register(ScorePacket.class);
        kryo.register(BlockPacket.class);
        kryo.register(BlockBreakPacket.class);
        kryo.register(BallPacket.class);
    }

    JLabel scoreLabel = new JLabel();
    String username = new Random().nextInt(100) + "Player";
    JLabel chat = new JLabel("No messages yet...");
    JTextField messageBox = new JTextField();
    final JFrame usernameChoice = new JFrame("Choose your username... [ Press Enter ]");
    final JTextField usernameBox = new JTextField();
    // JButton openChat = new JButton("Open Chat");
    java.util.List<JLabel> blocks = new ArrayList<JLabel>();
    HashMap<Integer,JLabel> balls = new HashMap<Integer, JLabel>();
    JLabel background = new JLabel();
    String version = "GON v1.0.0 [BETA]";
    public GameClient() {

        add(new JFXPanel());
        try
        {
            beginMusic();
        }catch(Exception ex){ex.printStackTrace();}



        addMouseListener(this);
        setTitle("Game-Over Network");


        background.setLayout(new FlowLayout());
        background.setIcon(new ImageIcon((Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/bg.png")))));
        background.setBounds(0, 0, getWidth(), getHeight());
        setContentPane(background);


        add(chat);


        // messageBox.setEnabled(false);
        messageBox.setVisible(false);
        add(messageBox);
        chat.addKeyListener(this);
        box.addKeyListener(this);


        //add(openChat);
        //   messageBox.addKeyListener(this);
        messageBox.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!messageBox.getText().equalsIgnoreCase("")) {
                        MessagePacket packet = new MessagePacket();
                        packet.setName(username);
                        packet.setMessage(messageBox.getText());
                        client.sendTCP(packet);
                        messageBox.setText("");
                        messageBox.setVisible(false);
                        repaint();
                        setVisible(false);
                        setVisible(true);
                    }

                }
            }
        });

        setIconImage(coinstats.getImage());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LeavePacket leavePacket = new LeavePacket();
                leavePacket.setUsername(username);
                client.sendTCP(leavePacket);
            }


        });
        addKeyListener(this);
        getContentPane().addKeyListener(this);

        setLayout(null);


        setBounds(0, 0, 500, 500);
        messageBox.setBounds(0, 50, getWidth(), 50);

        // openChat.setBounds(getWidth() - 100, getHeight() - 100, 100, 100);
        // getGraphics().drawImage(background, 0, 0, null);

        usernameChoice.setBounds(0, 0, 1000, 800);
        usernameChoice.setLayout(null);
        JLabel bg = new JLabel();
        bg.setIcon(new ImageIcon((Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/bg.png")))));
        bg.setBounds(0,0,usernameChoice.getWidth(),usernameChoice.getHeight());
       usernameChoice. setContentPane(bg);

        usernameBox.setBounds(usernameChoice.getWidth() / 2, usernameChoice.getHeight() - 100, usernameChoice.getWidth() / 2, 50);
        JLabel version = new JLabel();
        version.setText(this.version);
        version.setOpaque(true);
        version.setBackground(Color.GRAY);
        version.setBounds(0,usernameChoice.getHeight() - 100, usernameChoice.getWidth() / 2, 50);
        usernameChoice.add(version);
        usernameChoice.add(usernameBox);
        usernameChoice.setVisible(true);
        usernameBox.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (usernameBox.getText().length() >= 17) {
                    start();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        usernameBox.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    start();
                }
            }
        });
        chat.setBounds(0, getHeight() - 200, getWidth(), 50);
        chat.setOpaque(true);
        chat.setBackground(Color.WHITE);
        updateScoreLabel();
        add(scoreLabel);
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateScoreLabel();
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        repaint();


    }

    public void start()
    {
        username = usernameBox.getText();
        startClient();
        JoinPacket packet = new JoinPacket();
        //  packet.setUuid(UUID.randomUUID());
        packet.setName(usernameBox.getText());

        client.sendTCP(packet);
        usernameChoice.dispose();
        setVisible(true);
    }


    int score= 0;
    public void updateScoreLabel()
    {
        scoreLabel.setText(score + "");
        scoreLabel.setBounds(getWidth()/2, 50, getWidth(), 10);
        messageBox.setBounds(0,getHeight() - 150,getWidth(),50);

        chat.setBounds(0,getHeight() - 100, getWidth(), 50);
        boolean contains = false;
        for(KeyListener listener : scoreLabel.getKeyListeners())
        {
            if(listener == this)
            {
                contains = true;
            }
        }
        if(!contains)
        {
            scoreLabel.addKeyListener(this);
        }

     //   scoreLabel
    }


}
