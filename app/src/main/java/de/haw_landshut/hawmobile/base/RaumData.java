package de.haw_landshut.hawmobile.base;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class RaumData {
    public RaumData(String room,String coord1,String coord2){
        this.room=room;
        this.coord1=coord1;
        this.coord2=coord2;

    }
    private String room;
    private String coord1;
    private String coord2;

    @PrimaryKey(autoGenerate = true)
    private long roomkey;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getCoord1() {
        return coord1;
    }

    public void setCoord1(String coord1) {
        this.coord1 = coord1;
    }

    public String getCoord2() {
        return coord2;
    }

    public void setCoord2(String coord2) {
        this.coord2 = coord2;
    }

    public long getRoomkey() {
        return roomkey;
    }

    public void setRoomkey(long roomkey) {
        this.roomkey = roomkey;
    }
}
