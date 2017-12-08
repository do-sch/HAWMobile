package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface EMailDao {

    @Query("SELECT * FROM email")
    List<EMail> getAllEmails();

    @Query("SELECT * FROM email WHERE foldername=:foldername")
    List<EMail> getAllEmailsFromFolder(String foldername);

    @Insert
    void insertAllEMails(EMail... emails);

    @Delete
    void deleteEMail(EMail email);

}
