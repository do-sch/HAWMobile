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

    @Query("SELECT * FROM email WHERE foldername=:foldername AND uid < :maxuid")
    List<EMail> getAllEmailsFromFolder(String foldername, long maxuid);

    @Query("SELECT uidvalidaty FROM emailfolder WHERE name=:foldername")
    long getFolderUIDValidaty(String foldername);

    @Query("SELECT nextuid FROM emailfolder WHERE name=:foldername")
    long getFolderNextuid(String foldername);

    @Query("SELECT COUNT(*) FROM email WHERE foldername=:foldername")
    long getMessageCountInFolder(String foldername);

    @Query("DELETE FROM email WHERE foldername=:foldername")
    void deleteAllEMailsFromFolder(String foldername);

    @Insert
    void insertAllEMails(EMail... emails);

    @Insert
    void insertAllFolders(EMailFolder... folders);

    @Delete
    void deleteEMail(EMail email);



}
