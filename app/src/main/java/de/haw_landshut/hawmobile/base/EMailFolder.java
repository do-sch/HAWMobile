package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class EMailFolder {

    @PrimaryKey
    @NonNull
    private String name;

    private long uidvalidaty;

    private long nextuid;

    public EMailFolder(){

    }

    @Ignore
    public EMailFolder(final String name, final long uidvalidaty, final long nextuid){
        this.name = name;
        this.uidvalidaty = uidvalidaty;
        this.nextuid = nextuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUidvalidaty() {
        return uidvalidaty;
    }

    public void setUidvalidaty(long uidvalidaty) {
        this.uidvalidaty = uidvalidaty;
    }

    public long getNextuid() {
        return nextuid;
    }

    public void setNextuid(long nextuid) {
        this.nextuid = nextuid;
    }
}
