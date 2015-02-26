package Packets;

import java.util.UUID;

/**
 * Created by Andrew on 2/21/2015.
 */
public class MessagePacket {

    String name;
    String message;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
