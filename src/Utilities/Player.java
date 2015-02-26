package Utilities;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Andrew on 2/21/2015.
 */
public class Player {
    int id;
    String name;
    int x;
    int y;
    JLabel playerImage = new JLabel();
    JLabel nameTag = new JLabel();
    int playerNumber;
    PlayerType playerType = PlayerType.PLAYER;
    Player target;

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public JLabel getNameTag() {

        nameTag.setFont(Font.getFont(Font.MONOSPACED));
        nameTag.setOpaque(true);
        Color gray = new Color(184,184,184);
        nameTag.setBackground(gray);
        nameTag.setForeground(Color.WHITE);
//        Rectangle bounds = nameTag.getBounds();
//        if(bounds!=null && nameTag!=null)
//        {
//           int width =
//                    nameTag.
//                    getFontMetrics
//                            (nameTag
//                                    .getFont())
//                    .stringWidth(getName());
//
//            bounds = new Rectangle(nameTag.getX(),nameTag.getY(), width, nameTag.getHeight());
//            nameTag.setBounds(bounds);
//
//        }


        return nameTag;
    }

    public void setNameTag(JLabel nameTag) {
        this.nameTag = nameTag;
    }

    public JLabel getPlayerImage() {
        return playerImage;
    }

    public void setPlayerImage(JLabel playerImage) {
        this.playerImage = playerImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        nameTag.setText("<html><center>" + getName() + "</center></html>");

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        getPlayerImage().setLocation(getX(),getY());
        getNameTag().setLocation(getX() + (getPlayerImage().getWidth() / 2) - getNameTag().getWidth() / 3,getY()  - (getPlayerImage().getHeight() / 2) - 10);

    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        getPlayerImage().setLocation(getX(),getY());
        getNameTag().setLocation(getX() + (getPlayerImage().getWidth() / 2) - getNameTag().getWidth() / 3,getY()  - (getPlayerImage().getHeight() / 2) - 10);
    }
}
