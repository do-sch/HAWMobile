package de.haw_landshut.hawmobile.base;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

@Database(entities = {EMail.class, EMailFolder.class, Contact.class, ProfData.class,FaecherData.class,CustomTimetable.class,Appointment.class,RaumData.class}, version = 1, exportSchema = false)
@TypeConverters({EMailConverters.class, ScheduleConverters.class})
public abstract class HAWDatabase extends RoomDatabase {
    private static HAWDatabase INSTANCE;

    public abstract EMailDao eMailDao();
    public abstract ScheduleDao scheduleDao();
    public abstract AppointmentDao appointmentDao();


    public synchronized static HAWDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static HAWDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                HAWDatabase.class,
                "haw")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                final ScheduleDao dao=getInstance(context).scheduleDao();
                                dao.insertAllRooms(RaumData.populateRooms());
                                dao.insertAlleFaecher(FaecherData.populateSubjects());
                                dao.insertAlleProfs(ProfData.populateProfs());
                            }
                        });
                    }
                })
                .build();
    }
}
