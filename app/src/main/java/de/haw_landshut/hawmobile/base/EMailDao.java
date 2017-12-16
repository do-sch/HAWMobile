package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.*;

import java.util.List;

@Dao
public interface EMailDao {

    @Query("SELECT * FROM email")
    List<EMail> getAllEmails();

    @Query("SELECT * FROM email WHERE foldername=:foldername")
    List<EMail> getAllEmailsFromFolder(String foldername);

    @Query("SELECT * FROM email WHERE foldername=:foldername AND uid < :maxuid")
    List<EMail> getAllEmailsFromFolder(String foldername, long maxuid);

    @Query("SELECT * FROM emailfolder")
    List<EMailFolder> getAllEmailFolders();

    @Query("SELECT * FROM email WHERE uid=:uid AND foldername=:foldername")
    EMail getEmailFromUidAndFolder(long uid, String foldername);

    @Query("SELECT uidvalidaty FROM emailfolder WHERE name=:foldername")
    long getFolderUIDValidaty(String foldername);

    @Query("SELECT nextuid FROM emailfolder WHERE name=:foldername")
    long getFolderNextuid(String foldername);

    @Query("SELECT COUNT(*) FROM email WHERE foldername=:foldername")
    long getMessageCountInFolder(String foldername);

    @Query("SELECT name FROM contact WHERE address=:address")
    String getNameFromEMail(String address);

    @Query("UPDATE email SET foldername=:newfolder,uid=:newuid WHERE uid=:olduid AND foldername=:oldfolder")
    void moveEMailToNewFolder(String newfolder, String oldfolder, long olduid, long newuid);

    @Query("DELETE FROM email WHERE foldername=:foldername")
    void deleteAllEMailsFromFolder(String foldername);

    @Insert
    void insertAllEMails(EMail... emails);

    @Insert
    void insertAllFolders(EMailFolder... folders);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertContacts(Contact... contacts);

    @Update
    void updateEMails(EMail... eMail);

    @Delete
    void deleteEMail(EMail email);

    @Delete
    void deleteContact(Contact... contacts);



}
