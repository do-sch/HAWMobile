package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.*;

import java.util.List;

@Dao
public interface EMailDao {

//    @Query("SELECT * FROM email")
//    List<EMail> getAllEmails();

    @Query("SELECT * FROM email WHERE foldername=:foldername ORDER BY uid DESC")
    List<EMail> getAllEmailsFromFolder(String foldername);

//    @Query("SELECT * FROM email WHERE foldername=:foldername AND uid < :maxuid")
//    List<EMail> getAllEmailsFromFolder(String foldername, long maxuid);

    @Query("SELECT * FROM emailfolder")
    List<EMailFolder> getAllEmailFolders();

    @Query("SELECT * FROM email WHERE uid=:uid AND foldername=:foldername")
    EMail getEmailFromUidAndFolder(long uid, String foldername);

    @Query("SELECT uidvalidaty FROM emailfolder WHERE name=:foldername")
    long getFolderUIDValidaty(String foldername);

    @Query("SELECT nextuid FROM emailfolder WHERE name=:foldername")
    long getFolderNextuid(String foldername);

    @Query("SELECT messageCount FROM emailfolder WHERE name=:foldername")
    int getFolderMessageCount(String foldername);

    @Query("DELETE FROM email WHERE foldername=:foldername AND uid NOT IN (SELECT uid FROM email WHERE foldername=:foldername ORDER BY uid DESC LIMIT :savecount)")
    void deleteLowestUIDMailsFromFolder(String foldername, int savecount);

//    @Query("SELECT COUNT(*) FROM email WHERE foldername=:foldername")
//    long getMessageCountInFolder(String foldername);

//    @Query("SELECT name FROM contact WHERE address=:address")
//    String getNameFromEMail(String address);

    @Query("UPDATE email SET foldername=:newfolder,uid=:newuid WHERE uid=:olduid AND foldername=:oldfolder")
    void moveEMailToNewFolder(String newfolder, String oldfolder, long olduid, long newuid);

    @Query("UPDATE email SET seen=:seen WHERE foldername=:foldername AND uid=:uid")
    void setEMailSeen(long uid, String foldername, boolean seen);

    @Query("UPDATE emailfolder SET nextuid=:nextuid, uidvalidaty=:uidvalidaty, messageCount=:messageCount WHERE name=:foldername")
    void updateFolderStuff(String foldername, long nextuid, long uidvalidaty, int messageCount);

    @Query("DELETE FROM email WHERE foldername=:foldername")
    void deleteAllEMailsFromFolder(String foldername);

    @Query("DELETE FROM email WHERE foldername=:foldername AND uid=:uid")
    void deleteEMail(String foldername, long uid);

    @Insert
    void insertAllEMails(EMail... emails);

    @Insert
    void insertAllEMails(List<EMail> eMails);

    @Insert
    void insertAllFolders(List<EMailFolder> folders);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertContacts(Contact... contacts);

//    @Update
//    void updateEMails(EMail... eMail);
//
//    @Delete
//    void deleteEMail(EMail email);
//
//    @Delete
//    void deleteContact(Contact... contacts);



}
