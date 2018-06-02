package de.haw_landshut.hawmobile.news;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.haw_landshut.hawmobile.MainActivity;
import de.haw_landshut.hawmobile.base.Appointment;
import de.haw_landshut.hawmobile.base.AppointmentDao;
import de.haw_landshut.hawmobile.base.HAWDatabase;

class LoadAppointmentsTask extends AsyncTask<Void, Integer, Void> {
    private String TAG = "LoadAppointmentsTask";
    private AppointmentDao dao;
    private WeakReference<Context> context;

    LoadAppointmentsTask(Context context){
        this.context = new WeakReference<>(context);

        HAWDatabase database = ((MainActivity) context).getDatabase();
        dao = database.appointmentDao();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Appointment appointment = null;

        //load appointments from Database
        Log.d(TAG, "Connect to database...");
        for (int i = 0; i < 5; i++) {
            if (dao == null) {
                Log.d(TAG, "Could not connect to dao. Retry...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "Get data from database...");

                //dao.deleteAllAppointments();    //Debug

                appointment = dao.getLastAppointment();
                break;
            }
        }

        if (appointment == null) {         //load appointments from Internet
            Log.d(TAG, "Database is empty.");
            Log.d(TAG, "Get data from internet...");

            if (extractAppointments(downloadAppointmentPDF(getAppointmentURL())))
                appointment = dao.getLastAppointment();
        }


        if (appointment == null) {         //loading failed
            Log.d(TAG, "No appointments found.");
            return null;
        } else {                                //loading success
            Log.d(TAG, "Appointments loaded.");
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    static int dateAsInt(String date){
        int result = -1;

        if(date != null) {
            Pattern pattern = Pattern.compile("(\\d\\d)\\.(\\d\\d)\\.(\\d\\d\\d\\d)");
            Matcher matcher = pattern.matcher(date);
            if (matcher.find())
                result = Integer.parseInt(matcher.group(3) + matcher.group(2) + matcher.group(1));
        }

        return result;
    }

    static String dateAsString(Integer date){
        String tmp = date.toString();
        if(tmp.length()==8)
            return tmp.substring(6,8) +"."+ tmp.substring(4,6) +"."+ tmp.substring(0,4);

        return null;
    }

    private File downloadAppointmentPDF(String path) {
        Log.d(TAG,"downloadAppointmentPDF()");
        URL url;
        File file;
        String fileName = "Termine.pdf";
        InputStream input;
        OutputStream output;
        HttpURLConnection connection;

        if (context.get() == null)
            return null;

        try {
            file = File.createTempFile(fileName, null, context.get().getCacheDir());

            url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "Connection failed!");
                return null;
            }
            Log.d(TAG, "Connected!");

            input = connection.getInputStream();
            output = new FileOutputStream(file, false);

            byte data[] = new byte[4096];
            while (input.read(data) != -1) {
                output.write(data);
            }

            output.close();
            input.close();
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return file;
    }

    private boolean extractAppointments(File file) {
        Log.d(TAG,"extractAppointments()");

        if (file == null) {
            Log.d(TAG, "file is null!");
            return false;
        }

        try {
            StringBuilder tmp = new StringBuilder("");
            PdfReader reader = new PdfReader(file.getAbsolutePath());
            int n = reader.getNumberOfPages();
            for (int i = 0; i < n; i++) {
                tmp.append(PdfTextExtractor.getTextFromPage(reader, i + 1, new SimpleTextExtractionStrategy()).trim()).append("\n");
            }
            String parsedText = tmp.toString();
            Pattern pattern;
            Matcher matcher;

            //Lösche Kopf und Fußzeilen
            pattern = Pattern.compile("(von\\s*bis((.|\\R)*Semesterende))");
            matcher = pattern.matcher(parsedText);
            while (matcher.find())
                parsedText = matcher.group(1);

            //Bereite Daten vor
            String year = SimpleDateFormat.getDateInstance().format(Calendar.getInstance().getTime()).substring(6);
            parsedText = parsedText.replaceAll("August", "01.08."+year);
            parsedText = parsedText.replaceAll("September", "30.09."+year);
            parsedText = parsedText.replaceAll("\\bab\\b|\\*", "");

            pattern = Pattern.compile("(\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d)");
            matcher = pattern.matcher(parsedText);
            while (matcher.find())
                parsedText = parsedText.replace(matcher.group(1), "<Date>" + matcher.group(1));

            //Extrahiere Daten
            pattern = Pattern.compile("[\\s-]*((<Date>)+\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d)[\\s-]*(((<Date>)+\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d)[\\s-]*)?(((<Date>)+\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d)[\\s-]*)?([^<]*)");
            matcher = pattern.matcher(parsedText);

            Log.d(TAG, "parse Appointments...");
            while (matcher.find()) {
                int date1 = dateAsInt((matcher.group(1)+"").replaceAll("<Date>",""));
                int date2 = dateAsInt((matcher.group(4)+"").replaceAll("<Date>",""));
                int date3 = dateAsInt((matcher.group(7)+"").replaceAll("<Date>",""));
                String appointment = matcher.group(9).replaceAll("\\R","");

                if(date1!=-1 && appointment!=null) {
                    if (date2 != -1 && date3 == -1)
                        dao.insertAppointment(new Appointment(date1, date2, appointment));

                    else if (date2 == -1 && date3 == -1)
                        dao.insertAppointment(new Appointment(date1, date1, appointment));

                    else
                        dao.insertAppointment(new Appointment(date1, date3, appointment));

                    Log.d(TAG,"Date 1: "+date1);
                    Log.d(TAG,"Date 2: "+date2);
                    Log.d(TAG,"Date 3: "+date3);
                    Log.d(TAG,"Text: "+appointment);
                }
            }
            //Debug
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            int tomorrow = LoadAppointmentsTask.dateAsInt(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
            dao.insertAppointment(new Appointment(tomorrow,tomorrow,"Test"));
            //End Debug
            Log.d(TAG, "parse Appointments... done!");

            reader.close();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            return false;
        }
        return true;
    }

    private String getAppointmentURL() {
        Log.d(TAG,"getAppointmentURL()");

        String result;
        int year, dayOfYear;
        Calendar calendar = Calendar.getInstance();
        Pattern pattern = Pattern.compile("(\\d\\d)\\.(\\d\\d)\\.(\\d\\d\\d\\d)");
        Matcher matcher = pattern.matcher(SimpleDateFormat.getDateInstance().format(calendar.getTime()));

        if(!matcher.find()){
            Log.d(TAG, "No matching date found");
            return null;
        }

        year = Integer.parseInt(matcher.group(3));
        dayOfYear = Integer.parseInt(matcher.group(2)+matcher.group(1));

        if(dayOfYear < 1001 && dayOfYear >= 315)
            result = "https://www.haw-landshut.de/fileadmin/Hochschule_Landshut_NEU/Ungeschuetzt/SSZ/Infos_Studierende_Studieninteressierte/Termine_SS_"+year+".pdf";
        else if(dayOfYear < 315)
            result = "https://www.haw-landshut.de/fileadmin/Hochschule_Landshut_NEU/Ungeschuetzt/SSZ/Infos_Studierende_Studieninteressierte/Termine_WS_"+(year-2001)+"-"+(year-2000)+".pdf";
        else
            result = "https://www.haw-landshut.de/fileadmin/Hochschule_Landshut_NEU/Ungeschuetzt/SSZ/Infos_Studierende_Studieninteressierte/Termine_WS_"+(year-2000)+"-"+(year-1999)+".pdf";

        Log.d(TAG, "URL: "+result);
        return result;
    }

}
