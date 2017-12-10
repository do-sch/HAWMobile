package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ScheduleDao {

    @Query("SELECT * FROM ProfData")
    List<ProfData> getAllProfs();

    @Query("SELECT * FROM FaecherData WHERE studiengang=:studiengang")
    List<FaecherData> getFaecherByStudiengang(String studiengang);

    @Query("SELECT * FROM CustomTimetable")
    List<CustomTimetable>getTimetable();

    //Leerzeichen bei LIKE:fach raus oder rein, wegen fach erkennen, oder erkennt er dann leerzeichen??? a % , oder a%
    @Query("SELECT * FROM FaecherData WHERE studiengang=:studiengang AND fach LIKE:fach")
    List<FaecherData>getFaecherDataByChars(String studiengang,String fach);

    @Query("DELETE  FROM CustomTimetable")
    void deleteWholeCustomTimetable();

    @Insert
    void insertAlleProfs(ProfData... profs);

    @Insert
    void insertAlleFaecher(FaecherData... faecher);

    @Insert
    void insertEmptyTimetable(CustomTimetable...customtimetables);

    @Delete
    void deleteProf(ProfData prof);

    @Delete
    void deleteFach(FaecherData fach);
}
