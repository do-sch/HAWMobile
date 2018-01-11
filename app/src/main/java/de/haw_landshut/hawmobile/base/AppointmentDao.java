package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by X on 11.01.2018.
 */

@Dao
public interface AppointmentDao {
    @Insert
    public void insertAppointment(Appointment appointment);

    @Delete
    public void deleteAppointment(Appointment appointment);

    @Query("SELECT * FROM appointment")
    public List<Appointment> getAllAppointments();

    @Query("SELECT * FROM appointment WHERE date = :date")
    public List<Appointment> getAppointmentByDate(String date);

    @Query("DELETE FROM appointment")
    public void deleteAllAppointments();
}
