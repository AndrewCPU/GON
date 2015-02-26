package Server;

import Packets.*;
import Utilities.Player;
import Utilities.PlayerType;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Client.GameClient;

/**
 * Created by Andrew on 2/21/2015.
 */
public class GameServer extends JFrame{
    public static void main(String[] args) {
        new GameServer();


        //  open up standard input

    }
    public void askForInput()
    {
        new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                System.out.print("> ");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                String txt = null;

                //  read the username from the command-line; need to use try/catch with the
                //  readLine() method
                try {
                    txt = br.readLine();
                } catch (IOException ioe) {
                    System.out.println("IO error trying to read your name!");
                    System.exit(1);
                }
                String[] args = txt.split(" ");
                if(args[0].equalsIgnoreCase("restart"))
                {
                    System.exit(0);
                    new GameServer();
                }
                if(args[0].equalsIgnoreCase("say"))
                {
                    String message = "";
                    if(args.length>1)
                    {
                        for(int i = 1; i<args.length; i++)
                        {
                            message+=args[i] + " ";
                        }
                        MessagePacket packet = new MessagePacket();
                        packet.setMessage(message);
                        packet.setName("[Server]");
                        server.sendToAllTCP(packet);
                    }


                }

                if(args[0].equalsIgnoreCase("tp"))
                {
                    String p1 = args[1];
                    String p2 = args[2];
                    MovePacket movePacket = new MovePacket();
                    Player player2 = getPlayer(p2);
                    movePacket.setUsername(getPlayer(p1).getName());
                    movePacket.setX(player2.getX());
                    movePacket.setY(player2.getY());

                    Player p = getPlayer(p1);
                    playerMap.remove(p.getId());
                    p.setX(player2.getX());
                    p.setY(player2.getY());
                    playerMap.put(p.getId(),p);

                    server.sendToAllTCP(movePacket);
                }

                if(txt.equalsIgnoreCase("stop"))
                {
                    MessagePacket packet = new MessagePacket();
                    packet.setMessage("Server has shut down / crashed... Please restart the game...");
                    packet.setName("[Crash Report]");
                    server.sendToAllTCP(packet);

                    server.stop();
                    System.out.print("[INFO] Server shutting down...\n");

                    System.exit(0);

                }
                else
                {
                    askForInput();
                }

            }
        }).run();

       // System.out.println("Thanks for the name, " + userName);
    }

    JButton stop = new JButton("STOP");
    Server server =null;

    HashMap<Integer, Player> playerMap = new HashMap<Integer, Player>();
    HashMap<Integer,BallPacket> pointHashMap = new HashMap<Integer, BallPacket>();
    public Player getPlayer(String name)
    {
        for(Player p : playerMap.values())
        {
            if(p.getName().equalsIgnoreCase(name))
            {
                return p;
            }
        }
        return null;
    }
    public int getNextId()
    {
        int biggest = 0;
        for(int i : playerMap.keySet())
        {
            if(i>biggest)
            {
                biggest = i;
            }
        }
        return biggest + 1;
    }
    int boxX = 250;
    int boxY = 100;
    public boolean isMoveable(int x, int y)
    {
        boolean  movable = true;
        for(Rectangle rectangle : blocks)
        {


            if(rectangle.getY() >= y) {
                if (rectangle.getY() + rectangle.getHeight() <= y)
                {
                    if(rectangle.getX() >=x)
                    {
                        if(rectangle.getX()  + rectangle.getWidth() <= x)
                        {
                            movable = false;
                        }
                    }
                }
            }
            if(rectangle.contains(x, y+ 100 + 1))
            {
                movable = false;
            }
            if(rectangle.contains(x + 101, y))
            {
                movable = false;
            }
            if(rectangle.contains(x,y + 1))
            {
                movable = false;
            }


            if(rectangle.contains(x + 100 + 1, y))
            {
                movable = false;
            }

            if(rectangle.contains(x+ 100, y + 100))
            {
                movable= false;
            }

            if(rectangle.contains(x, y + 100 + 1))
            {
                movable = false;
            }
            if(rectangle.contains(x, y))
            {
                movable = false;
            }
            // if(rectangle.contains(x, pad))
            if(rectangle.contains(x + 100, y))
            {
                movable = false;
            }

            for(int i = 1; i<=100; i++)
            {
                if(rectangle.contains(x + (100 / i), y))
                {
                    movable = false;
                }
                if(rectangle.contains(x , y + (100 / i)))
                {
                    movable = false;
                }
                if(rectangle.contains(x + 100, y + (100 / i)))
                {
                    movable = false;
                }



                if(rectangle.contains(x+ 100, y + 100))
                {
                    movable= false;
                }
                if(rectangle.contains(x+ (100/i), y + (100/i)))
                {
                    movable= false;
                }


            }



            if(rectangle.contains(x + 100, y + (100)))
            {
                movable = false;
            }
            if(rectangle.contains(x , y + (100)))
            {
                movable = false;
            }
            if(rectangle.contains(x + 100, y))
            {
                movable = false;
            }

            if(x >= rectangle.getX() && x <= rectangle.getX() + rectangle.getWidth())
            {
             //   System.out.print(">=X\n");
                if(y >= rectangle.getY() && y<=rectangle.getY() + rectangle.getHeight())
                {
                 ///   System.out.print(">=Y\n");
                    movable = false;
                }
            }



        }
        return movable;
    }
    
    
    
    
    
    
    
    
    
    public boolean isMoveable(Player p, MovePacket packet)
    {
        boolean  movable = true;
        for(Rectangle rectangle : blocks)
        {


            if(rectangle.getY() >= packet.getY()) {
                if (rectangle.getY() + rectangle.getHeight() <= packet.getY())
                {
                    if(rectangle.getX() >=packet.getX())
                    {
                        if(rectangle.getX()  + rectangle.getWidth() <= packet.getX())
                        {
                            movable = false;
                        }
                    }
                }
            }
            if(rectangle.contains(packet.getX(), packet.getY()+ 100 + 1))
            {
                movable = false;
            }
            if(rectangle.contains(packet.getX() + 101, packet.getY()))
            {
                movable = false;
            }
            if(rectangle.contains(packet.getX(),packet.getY() + 1))
            {
                movable = false;
            }

            if(rectangle.contains(p.getPlayerImage().getBounds()))
            {
                movable = false;
            }
            if(rectangle.contains(packet.getX() + p.getPlayerImage().getWidth() + 1, packet.getY()))
            {
                movable = false;
            }
            if(rectangle.intersects(p.getPlayerImage().getBounds()))
            {
                movable = false;
            }
            if(rectangle.contains(packet.getX()+ p.getPlayerImage().getWidth(), packet.getY() + p.getPlayerImage().getHeight()))
            {
                movable= false;
            }

            if(rectangle.contains(packet.getX(), packet.getY() + p.getPlayerImage().getHeight() + 1))
            {
                movable = false;
            }
            if(rectangle.contains(packet.getX(), packet.getY()))
            {
                movable = false;
            }
            // if(rectangle.contains(packet.getX(), pad))
            if(rectangle.contains(packet.getX() + p.getPlayerImage().getWidth(), packet.getY()))
            {
                movable = false;
            }

            for(int i = 1; i<=100; i++)
            {
                if(rectangle.contains(packet.getX() + (p.getPlayerImage().getWidth() / i), packet.getY()))
                {
                    movable = false;
                }
                if(rectangle.contains(packet.getX() , packet.getY() + (p.getPlayerImage().getHeight() / i)))
                {
                    movable = false;
                }
                if(rectangle.contains(packet.getX() + p.getPlayerImage().getWidth(), packet.getY() + (p.getPlayerImage().getHeight() / i)))
                {
                    movable = false;
                }



                if(rectangle.contains(packet.getX()+ p.getPlayerImage().getWidth(), packet.getY() + p.getPlayerImage().getHeight()))
                {
                    movable= false;
                }
                if(rectangle.contains(packet.getX()+ (p.getPlayerImage().getWidth()/i), packet.getY() + (p.getPlayerImage().getHeight()/i)))
                {
                    movable= false;
                }


            }

            for(int i = (int)rectangle.getX(); i<=rectangle.getX() + rectangle.getWidth() + 1; i++)
            {
                if(packet.getX()==i && packet.getY() + p.getPlayerImage().getHeight() + 1 >= rectangle.getY() && p.getPlayerImage().getHeight() + p.getY() + 1 <= rectangle.getY() + rectangle.getHeight())
                {
                    movable = false;
                }
            }

            if(rectangle.contains(packet.getX() + p.getPlayerImage().getWidth(), packet.getY() + (p.getPlayerImage().getHeight())))
            {
                movable = false;
            }
            if(rectangle.contains(packet.getX() , packet.getY() + (p.getPlayerImage().getHeight())))
            {
                movable = false;
            }
            if(rectangle.contains(packet.getX() + p.getPlayerImage().getWidth(), packet.getY()))
            {
                movable = false;
            }

            if(packet.getX() >= rectangle.getX() && packet.getX() <= rectangle.getX() + rectangle.getWidth())
            {
                //System.out.print(">=X\n");
                if(packet.getY() >= rectangle.getY() && packet.getY()<=rectangle.getY() + rectangle.getHeight())
                {
                   // System.out.print(">=Y\n");
                    movable = false;
                }
            }



        }
        return movable;
    }

    List<Rectangle> blocks = new ArrayList<Rectangle>();
    long sM = 0;
    public void startServer()
    {
        sM = System.currentTimeMillis();
        server= new Server();
        server.start();
        System.out.print("[INFO] Starting server...\n");
        try
        {
            server.bind(45666,45777);
            System.out.print("[INFO] Binding port 45666 / 45777\n");
        }catch(Exception ex){
            System.out.print("[ERROR] Failed to bind port. Is it already in use?\n");
        }
        System.out.print("[INFO] Registering Listeners\n");
        server.addListener(new Listener() {

            public void received(Connection connection, Object object) {


                if (System.currentTimeMillis() - 20 > milliseconds) {
                    scheduler.scheduleAtFixedRate(new Runnable() {
                        public void run() {
//                score++;
//                System.out.print("TICK\n");
//                ScorePacket scorePacket = new ScorePacket();
//                scorePacket.setScore(score+1);
//
//                server.sendToAllTCP(scorePacket);
//                updateGame();
                         //   //  gameTick();
                           // updateGame();


                        }
                    }, 5, 5, TimeUnit.MILLISECONDS);
                }

                if (object instanceof BlockBreakPacket) {

                    if (score - 1 >= 0) {
                        BlockBreakPacket blockPacket = (BlockBreakPacket) object;
                        server.sendToAllTCP(blockPacket);
                        Rectangle bounds = null;
                        for (Rectangle rectangle : blocks) {
                            if (rectangle.contains(new Point(blockPacket.getX1(), blockPacket.getY1()))) {
                                bounds = rectangle;
                            }
                        }

                        blocks.remove(bounds);

                        ScorePacket scorePacket = new ScorePacket();
                        scorePacket.setScore(score);
                        server.sendToAllTCP(scorePacket);
                    }


                }

//                if(object instanceof ScorePacket)
//                {
//                    ScorePacket packet = new ScorePacket();
//                    score = packet.getScore();
//                    server.sendToAllTCP(packet);
//                }


                if (object instanceof BlockPacket) {
                    BlockPacket blockPacket = (BlockPacket) object;
                    Rectangle bounds = new Rectangle(new Point(blockPacket.getX(), blockPacket.getY()));
                    bounds.add(new Point(blockPacket.getX2(), blockPacket.getY2()));
                    blocks.add(bounds);


                    server.sendToAllTCP(object);
                }
                if (object instanceof MessagePacket) {
                    MessagePacket messagePacket = (MessagePacket) object;
                    System.out.print("[" + new Date().toString() + "] " + messagePacket.getName() + ": " + messagePacket.getMessage());
                    server.sendToAllTCP((MessagePacket) object);
                }
                if (object instanceof JoinPacket) {
                    JoinPacket joinPacket = (JoinPacket) object;
                    MessagePacket packet = new MessagePacket();
                    packet.setMessage(joinPacket.getName() + " has joined the server...");
                    packet.setName("Server");
                    // packet.setUuid(UUID.randomUUID());
                    server.sendToAllTCP(packet);
                    // System.out.print(joinPacket.getName());

                    PlayerPacket playerPacket = new PlayerPacket();
                    // playerPacket.setUuid(UUID.randomUUID());
                    playerPacket.setUsername(joinPacket.getName());
                    playerPacket.setId(getNextId());
                    playerPacket.setX(250);
                    playerPacket.setY(250);
                    int num = new Random().nextInt(12 - 1) + 1;
                    while (num == 0) {
                        num = new Random().nextInt(12 - 1) + 1;
                    }
                    playerPacket.setPlayerNumber(num);

                    server.sendToAllTCP(playerPacket);
                    Player p = new Player();
                    p.setName(joinPacket.getName());
                    p.setPlayerNumber(playerPacket.getPlayerNumber());
                    p.setX(250);
                    p.setY(250);
                    p.setId(playerPacket.getId());


                    playerMap.put(playerPacket.getId(), p);

                    BoxPacket boxPacket = new BoxPacket();
                    boxPacket.setHeight(100);
                    boxPacket.setWidth(100);
                    boxPacket.setX(boxX);
                    boxPacket.setY(boxY);
                    server.sendToTCP(connection.getID(), boxPacket);
                    for (Player player : playerMap.values()) {
                        PlayerPacket pp = new PlayerPacket();
                        pp.setId(player.getId());
                        pp.setUsername(player.getName());
                        pp.setX(player.getX());
                        pp.setY(player.getY());
                        pp.setPlayerNumber(player.getPlayerNumber());
                        server.sendToTCP(connection.getID(), pp);
                    }
                    for (Rectangle rectangle : blocks) {
                        BlockPacket blockPacket = new BlockPacket();
                        blockPacket.setX((int) rectangle.getX());
                        blockPacket.setY((int) rectangle.getY());
                        blockPacket.setX2((int) rectangle.getX() + (int) rectangle.getWidth());
                        blockPacket.setY2((int) rectangle.getY() + (int) rectangle.getHeight());
                        server.sendToTCP(connection.getID(), blockPacket);
                    }

                    ScorePacket scorePacket = new ScorePacket();
                    scorePacket.setScore(score);
                    server.sendToTCP(connection.getID(), scorePacket);
                    System.out.print("[" + new Date().toString() + "] " + packet.getMessage() + "\n");


                }
                if (object instanceof BallPacket) {
                    server.sendToAllTCP(object);
                    BallPacket packet = (BallPacket) object;

                    ((BallPacket) object).setId(((BallPacket) object).getSize());
                    pointHashMap.put(((BallPacket) object).getId(), packet);

                }
                if (object instanceof LeavePacket) {
                    LeavePacket joinPacket = (LeavePacket) object;
                    MessagePacket packet = new MessagePacket();
                    packet.setMessage(joinPacket.getUsername() + " has left the server...");
                    Player p = getPlayer(joinPacket.getUsername());
//                    remove(p.getPlayerImage());
                    if (p != null) {
                        playerMap.remove(p.getId());
                    }


                    packet.setName("Server");
                    server.sendToAllTCP(joinPacket);
                    // packet.setUuid(UUID.randomUUID());
                    server.sendToAllTCP(packet);
                    System.out.print("[" + new Date().toString() + "] " + packet.getMessage() + "\n");
                }
                if (object instanceof MovePacket) {
                    MovePacket packet = (MovePacket) object;
                    //    System.out.print(packet.getUsername() + "=================\n");
                    Player p = getPlayer(packet.getUsername());

//                    MessagePacket messagePacket = new MessagePacket();
//                    messagePacket.setName(packet.getUsername());
//                    messagePacket.setMessage("X: " + packet.getX() + " Y: " + packet.getY());
//                    server.sendToAllTCP(messagePacket);
                    boolean movable = isMoveable(p, packet);

                    if ((movable)) {
                        p.setX(packet.getX());
                        p.setY(packet.getY());
                        playerMap.remove(p.getId());
                        playerMap.put(p.getId(), p);
                        //    System.out.print(packet.getUsername() + "::" + packet.getX() + "::" + packet.getY() + "\n");
                        server.sendToAllTCP(packet);
                        Rectangle boxRect = new Rectangle(boxX, boxY, 100, 100);
                        Rectangle playerRect = new Rectangle(packet.getX(), packet.getY(), 100, 100);
                        if (target != null) {
                            if (p.getName().equalsIgnoreCase(target.getName())) {
                                target = p;
                            }
                        }

                        if (boxRect.intersects(playerRect)) {
                            target = p;
                            Random r = new Random();
                            boxX = r.nextInt(1001 - 1) + 1;
                            boxY = r.nextInt(101 - 1) + 1;


                            BoxPacket boxPacket = new BoxPacket();
                            boxPacket.setHeight(100);
                            boxPacket.setWidth(100);
                            boxPacket.setX(boxX);
                            boxPacket.setY(boxY);
                            server.sendToAllTCP(boxPacket);
                            score++;
                            ScorePacket scorePacket = new ScorePacket();
                            scorePacket.setScore(score);
                            server.sendToAllTCP(scorePacket);


//                        Player player = new Player();
//                        player.setPlayerNumber(new Random().nextInt(12 - 1) + 1);
//                        java.util.List<String> firstName = new ArrayList<String>();
//                        firstName.add("Big");
//                        firstName.add("Small");
//                        firstName.add("Tiny");
//                        firstName.add("Huge");
//                        firstName.add("Angry");
//
//                        List<String> lastName = new ArrayList<String>();
//                        lastName.add("Meany");
//                        lastName.add("Fry");
//                        lastName.add("Burger");
//                        lastName.add("Kyle");
//
//
//                        String first = firstName.get(new Random().nextInt(firstName.size() - 1) + 1);
//                        String last= lastName.get(new Random().nextInt(lastName.size() - 1) + 1);
//
//
//                        player.setName(first + " " + last);
//                        player.setX(new Random().nextInt(1001 - 1) + 1);
//                        player.setY(new Random().nextInt(751 - 1) + 1);
//                        player.setPlayerType(PlayerType.MOB);
//                        player.setTarget(p);
//                        player.setId(getNextId());
//                        PlayerPacket playerPacket = new PlayerPacket();
//                        playerPacket.setId(player.getId());
//                        playerPacket.setPlayerNumber(player.getPlayerNumber());
//                        playerPacket.setX(player.getX());
//                        playerPacket.setY(player.getY());
//                        playerPacket.setUsername(player.getName());
//                        playerPacket.setType(player.getPlayerType().toString());
//                        playerMap.put(player.getId(), player);
//                        server.sendToAllTCP(playerPacket);
                        }
                    }


                }


            }


        });
        Kryo kryo = server.getKryo();

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
        System.out.print("[INFO] Server online... (" + (System.currentTimeMillis() - sM) + "ms)\n");

    }
    int score = 0;
    long milliseconds = System.currentTimeMillis();
    Player target ;
    public void updateGame()
    {
        milliseconds = System.currentTimeMillis();
        List<BallPacket> remove = new ArrayList<BallPacket>();
        for(BallPacket packet : pointHashMap.values())
        {
            int id = -1;
            for(int i : pointHashMap.keySet())
            {
                if(pointHashMap.get(i)==packet)
                {
                    id = i;
                }
            }
            if(id!=-1)
            {
                if(isMoveable(packet.getX(), packet.getY() + 1))
                {
                    packet.setY(packet.getY() + 1);
                    packet.setX(packet.getX());
                    remove.add(packet);

                }
            }

        }
        for(BallPacket packet : remove)
        {
            pointHashMap.remove(packet.getId());
            pointHashMap.put(packet.getId(), packet);
            server.sendToAllTCP(packet);
        }


        if(boxY<floor.getY())
        {
            if(isMoveable(boxX,boxY+1))
            {
                boxY++;
                BoxPacket boxPacket = new BoxPacket();
                boxPacket.setHeight(100);
                boxPacket.setWidth(100);
                boxPacket.setY(boxY);
                boxPacket.setX(boxX);
                server.sendToAllTCP(boxPacket);
            }

        }
        List<Player> players = new ArrayList<Player>();
        for(Player p : playerMap.values())
        {
            if(p.getY()<floor.getY())
            {
                MovePacket movePacket = new MovePacket();
                movePacket.setX(p.getX());
                movePacket.setY(p.getY() + 1);
                if(isMoveable(p,movePacket))
                {
                    p.setY(p.getY()+1);
                    players.add(p);
                }

            }
        }
        for(Player p : players)
        {

            playerMap.remove(getPlayer(p.getName()));
            playerMap.put(p.getId(),p);
            MovePacket movePacket = new MovePacket();
            movePacket.setX(p.getX());
            movePacket.setY(p.getY());
            movePacket.setUsername(p.getName());
            server.sendToAllTCP(movePacket);

        }

    }
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public GameServer()
    {

        setVisible(true);
        startServer();
        setBounds(0, 0, 200, 200);

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MessagePacket packet = new MessagePacket();
                packet.setMessage("Server has shut down / crashed... Please restart the game...");
                packet.setName("[Crash Report ]");
                server.sendToAllTCP(packet);

                server.stop();

                System.exit(0);
            }
        });
        add(stop);
        blocks.add(floor);
        repaint();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
//                score++;
//                System.out.print("TICK\n");
//                ScorePacket scorePacket = new ScorePacket();
//                scorePacket.setScore(score+1);
//
//                server.sendToAllTCP(scorePacket);
//                updateGame();
              //  gameTick();
             //  updateGame();




            }
        }, 5, 5, TimeUnit.MILLISECONDS);
        askForInput();
       // System.out.print(server.getConnections());
      //  new GameClient();
    }
    Rectangle floor = new Rectangle(0,700,5000,100);


}
