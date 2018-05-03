package de.haw_landshut.hawmobile.base;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
@Entity
public class ProfData{

    public ProfData(String firstName,String lastName){
        this.firstName=firstName;
        this.lastName=lastName;
    }

    private String firstName;

    @PrimaryKey(autoGenerate = true)
    private long profkey;


    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getProfkey() {
        return profkey;
    }

    public void setProfkey(long profkey) {
        this.profkey = profkey;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static ProfData[] populateProfs(){
        return
                new ProfData[]{

                        new ProfData("Johannes", "Busse"),
                        new ProfData("Matthias", "Dorfner"),
                        new ProfData("Ludwig", "Griebl"),
                        new ProfData("Peter", "Hartmann"),
                        new ProfData("Wolfgang", "Jürgensen"),
                        new ProfData("Abdelmajid", "Khelil"),
                        new ProfData("Monika", "Messerer"),
                        new ProfData("Markus", "Mock"),
                        new ProfData("Dieter", "Nazareth"),
                        new ProfData("Martin", "Pellkofer"),
                        new ProfData("Gudrun", "Schiedermeier"),
                        new ProfData("Peter", "Scholz"),
                        new ProfData("Christian", "See"),
                        new ProfData("Andreas", "Siebert"),
                        new ProfData("Johann", "Uhrmann"),
                        new ProfData("Jürgen", "Wunderlich"),
                        new ProfData("Thomas", "Franzke"),
                        new ProfData("Michael", "Bürker"),
                        new ProfData("Patrick", "Dieses"),
                        new ProfData("Marcus", "Fischer"),
                        new ProfData("Dieter", "Greipl"),
                        new ProfData("Sandra", "Gronover"),
                        new ProfData("Michael", "Gumbsheimer"),
                        new ProfData("Burkhard", "Jaeger"),
                        new ProfData("Alexander", "Kumpf"),
                        new ProfData("Michael", "Leckebusch"),
                        new ProfData("Maren", "Martens"),
                        new ProfData("Bernd", "Mühlfriedel"),
                        new ProfData("Martin", "Prasch"),
                        new ProfData("Heinz-Werner", "Schuster"),
                        new ProfData("Hanns", "Robby"),
                        new ProfData("Valentina", "Speidel"),
                        new ProfData("Thomas", "Stauffert"),
                        new ProfData("Karl", "Stoffel"),
                        new ProfData("Manuel", "Strunz"),
                        new ProfData("Thomas", "Zinser"),
                        new ProfData("Stefan-Alexander", "Arlt"),
                        new ProfData("Andrea", "Badura"),
                        new ProfData("Andreas", "Breidenassel"),
                        new ProfData("Petra", "Denk"),
                        new ProfData("Andreas", "Dieterle"),
                        new ProfData("Guido", "Dietl"),
                        new ProfData("Armin", "Englmaier"),
                        new ProfData("Christian", "Faber"),
                        new ProfData("Thomas", "Faldum"),
                        new ProfData("Jürgen", "Gebert"),
                        new ProfData("Jürgen", "Giersch"),
                        new ProfData("Michaela", "Gruber"),
                        new ProfData("Artem", "Ivanov"),
                        new ProfData("Johann", "Jaud"),
                        new ProfData("Benedict", "Kemmerer"),
                        new ProfData("Alexander", "Kleimaier"),
                        new ProfData("Carl-Gustaf", "Kligge"),
                        new ProfData("Dieter", "Koller"),
                        new ProfData("Raimund", "Kreis"),
                        new ProfData("Jörg", "Mareczek"),
                        new ProfData("Sebastian", "Meißner"),
                        new ProfData("Fritz", "Pörnbacher"),
                        new ProfData("Mathias", "Rausch"),
                        new ProfData("Stefanie", "Remmele"),
                        new ProfData("Goetz", "Roderer"),
                        new ProfData("Carsten", "Röh"),
                        new ProfData("Magda", "Schiegl"),
                        new ProfData("Markus", "Schmitt"),
                        new ProfData("Markus", "Schneider"),
                        new ProfData("Martin", "Soika"),
                        new ProfData("Peter", "Spindler"),
                        new ProfData("Reimer", "Studt"),
                        new ProfData("Holger", "Timinger"),
                        new ProfData("Klaus", "Timmer"),
                        new ProfData("Petra", "Tippmann-Krayer"),
                        new ProfData("Hubertus", "C."),
                        new ProfData("Jürgen", "Welter"),
                        new ProfData("Thomas", "Wolf"),
                        new ProfData("Norbert", "Babel"),
                        new ProfData("Walter", "Fischer"),
                        new ProfData("Martin", "Förg"),
                        new ProfData("Bernhard", "Gubanka"),
                        new ProfData("Diana", "Hehenberger-Risse"),
                        new ProfData("Josef", "Hofmann"),
                        new ProfData("Peter", "Holbein"),
                        new ProfData("Barbara", "Höling"),
                        new ProfData("Otto", "Huber"),
                        new ProfData("Marcus", "Jautze"),
                        new ProfData("Hubert", "Klaus"),
                        new ProfData("Jan", "Köll"),
                        new ProfData("Detlev", "Maurer"),
                        new ProfData("Karl-Heinz", "Pettinger"),
                        new ProfData("Franz", "Prexler"),
                        new ProfData("Ralph", "Pütz"),
                        new ProfData("Karl", "Reiling"),
                        new ProfData("Wolfgang", "Reimann"),
                        new ProfData("Tim", "Rödiger"),
                        new ProfData("Sven", "Roeren"),
                        new ProfData("Holger", "Saage"),
                        new ProfData("Manfred", "Strohe"),
                        new ProfData("Volker", "Weinbrenner"),
                        new ProfData("Sigrid", "A."),
                        new ProfData("Hubert", "Beste"),
                        new ProfData("Stefan", "Borrmann"),
                        new ProfData("Clemens", "Dannenbeck"),
                        new ProfData("Christoph", "Fedke"),
                        new ProfData("Bettina", "Kühbeck"),
                        new ProfData("Katrin", "Liel"),
                        new ProfData("Johannes", "Lohner"),
                        new ProfData("Dominique", "Moisl"),
                        new ProfData("Karin", "E."),
                        new ProfData("Maria", "Ohling"),
                        new ProfData("Mihri", "Özdoğan"),
                        new ProfData("Andreas", "Panitz"),
                        new ProfData("Barbara", "Thiessen"),
                        new ProfData("Ralph", "Viehhauser"),
                        new ProfData("Mechthild", "Wolff"),
                        new ProfData("Eva", "Wunderer")

                };


    }
}