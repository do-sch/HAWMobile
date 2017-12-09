package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

@Entity
public class Contact {

    @PrimaryKey
    @NonNull
    private String address;

    private String name;

    public Contact(){

    }

    @Ignore
    public Contact(final Address address){
        final InternetAddress internetAddress = ((InternetAddress) address);

        setAddress(internetAddress.getAddress());
        setName(internetAddress.getPersonal());
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
