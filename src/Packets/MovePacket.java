package Packets;

import java.util.UUID;

/**
 * Created by Andrew on 2/21/2015.
 */
public class MovePacket {
    String username;

    int x = 0;
    int y = 0;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
