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

    @Insert
    void insertAlleProfs(ProfData... profs);

    @Insert
    void insertAlleFaecher(FaecherData... faecher);

    @Delete
    void deleteProf(ProfData prof);

    @Delete
    void deleteFach(FaecherData fach);
}
