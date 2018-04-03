package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AppointmentDao {
    @Insert
    void insertAppointment(Appointment appointment);

    @Query("SELECT * FROM appointment")
    List<Appointment> getAllAppointments();

    @Query("SELECT * FROM appointment WHERE start >= :date AND `end` <= :date")
    List<Appointment> getRecentAppointment(int date);

    @Query("SELECT * FROM appointment WHERE start = :date")
    List<Appointment> getAppointmentByStartDate(int date);

    @Query("DELETE FROM appointment")
    void deleteAllAppointments();

    @Query("SELECT * FROM appointment ORDER BY start DESC LIMIT 1")
    Appointment getLastAppointment();
}
