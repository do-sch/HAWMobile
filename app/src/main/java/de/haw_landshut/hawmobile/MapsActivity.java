package de.haw_landshut.hawmobile;

/**
 * Created by Fahed on 27/04/2018.
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener,

        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    String[][] matrix = {
            {"BS001", "48.5568648", "12.1982666"},
            {"BS002", "48.5567973", "12.1981969"},
            {"BS003", "48.5568949", "12.1981379"},
            {"BS004", "48.5569322", "12.1980279"},
            {"BS005", "48.5569801", "12.1978938"},
            {"BS006", "48.5570192", "12.197816"},
            {"BS007", "48.5570405", "12.1977516"},
            {"BS008", "48.5570813", "12.1976658"},
            {"BS009", "48.5571441", "12.1979064"},
            {"BS010", "48.5571441", "12.1979064"},
            {"BS011", "48.5571629", "12.1979238"},
            {"BS012", "48.5571816", "12.1979411"},
            {"BS013", "48.5572061", "12.1979637"},
            {"BS014", "48.5572375", "12.1979929"},
            {"BS015", "48.5572602", "12.198014"},
            {"BS016", "48.5573048", "12.1979986"},
            {"BS017", "48.5573048", "12.1979986"},
            {"BS018", "48.5572884", "12.1980402"},
            {"BS019", "48.5572585", "12.1981228"},
            {"BS020", "48.5572585", "12.1981228"},
            {"BS021", "48.5572474", "12.1981536"},
            {"BS022", "48.5571639", "12.1980653"},
            {"BS023", "48.5571639", "12.1980653"},
            {"BS024", "48.5571639", "12.1980653"},
            {"BS025", "48.5571639", "12.1980653"},
            {"BS026", "48.5571703", "12.1979686"},
            {"BS027", "48.5571703", "12.1979686"},
            {"BS028", "48.5571703", "12.1979686"},
            {"BS029", "48.5571191", "12.1979355"},
            {"BS030", "48.5571191", "12.1979355"},
            {"BS031", "48.5571191", "12.1979355"},
            {"BS032", "48.5570682", "12.1979872"},
            {"BS033", "48.5570472", "12.198046"},
            {"BS034", "48.5570472", "12.198046"},
            {"BS035", "48.5570472", "12.198046"},
            {"BS036", "48.5570147", "12.198137"},
            {"BS037", "48.5570147", "12.198137"},
            {"BS038", "48.5570147", "12.198137"},
            {"BS039", "48.5569841", "12.1982228"},
            {"BS040", "48.5569841", "12.1982228"},
            {"BS041", "48.5569496", "12.1983691"},
            {"BS042", "48.5569496", "12.1983691"},
            {"BS043", "48.5569496", "12.1983691"},
            {"BS044", "48.5569407", "12.198395"},
            {"BS045", "48.5569407", "12.198395"},
            {"BS101", "48.5568777", "12.1982628"},
            {"BS102", "48.556833", "12.1982218"},
            {"BS103", "48.5568997", "12.1981749"},
            {"BS104", "48.5569596", "12.1979999"},
            {"BS105", "48.5570248", "12.1979364"},
            {"BS106", "48.5570704", "12.197813"},
            {"BS107", "48.5571055", "12.1977055"},
            {"BS108", "48.5571088", "12.1978737"},
            {"BS109", "48.5570788", "12.1979575"},
            {"BS110", "48.5570472", "12.198046"},
            {"BS111", "48.5570472", "12.198046"},
            {"BS112", "48.5570041", "12.1981667"},
            {"BS113", "48.5570041", "12.1981667"},
            {"BS114", "48.5569687", "12.1982659"},
            {"BS115", "48.5569407", "12.198395"},
            {"BS116", "48.5569183", "12.198458"},
            {"BS201", "48.5568777", "12.1982628"},
            {"BS202", "48.5568997", "12.1981749"},
            {"BS203", "48.5569596", "12.1979999"},
            {"BS204", "48.5570118", "12.1978806"},
            {"BS205", "48.5570269", "12.1978319"},
            {"BS206", "48.5570557", "12.1977605"},
            {"BS207", "48.5571055", "12.1977055"},
            {"BS208", "48.5570276", "12.1979879"},
            {"BS209", "48.5570257", "12.1980282"},
            {"BS210", "48.5569855", "12.1981073"},
            {"HS001", "48.5565931", "12.1980544"},
            {"HS002", "48.5565624", "12.1981318"},
            {"HS003", "48.5565161", "12.1980092"},
            {"HS004", "48.5564782", "12.1981177"},
            {"HS005", "48.5563539", "12.1979073"},
            {"HS006", "48.5564807", "12.1980663"},
            {"HS007", "48.5562365", "12.1977711"},
            {"HS008", "48.5564807", "12.1980663"},
            {"HS009", "48.5561323", "12.1976811"},
            {"HS010", "48.5564807", "12.1980663"},
            {"HS011", "48.5560196", "12.1975879"},
            {"HS012", "48.5563351", "12.1979547"},
            {"HS013", "48.5558727", "12.1975706"},
            {"HS014", "48.5562973", "12.197859"},
            {"HS015", "48.5557636", "12.1978185"},
            {"HS016", "48.5562661", "12.1978324"},
            {"HS017", "48.5557281", "12.1980179"},
            {"HS018", "48.5561441", "12.1978008"},
            {"HS019", "48.5556821", "12.1981507"},
            {"HS020", "48.5561391", "12.1977648"},
            {"HS021", "48.5556552", "12.1982255"},
            {"HS022", "48.5560938", "12.1977368"},
            {"HS023", "48.5556552", "12.1982255"},
            {"HS024", "48.5560566", "12.1977102"},
            {"HS025", "48.5556304", "12.1982862"},
            {"HS026", "48.5558812", "12.1977169"},
            {"HS027", "48.5556721", "12.1983461"},
            {"HS028", "48.5558986", "12.1977837"},
            {"HS029", "48.5556669", "12.1983591"},
            {"HS030", "48.5558364", "12.1978386"},
            {"HS031", "48.5557339", "12.1983366"},
            {"HS032", "48.5558302", "12.1979803"},
            {"HS033", "48.5557612", "12.1984195"},
            {"HS034", "48.5558052", "12.1979884"},
            {"HS035", "48.5557928", "12.198443"},
            {"HS036", "48.5557819", "12.1981138"},
            {"HS037", "48.5558184", "12.1984594"},
            {"HS038", "48.5557635", "12.1981612"},
            {"HS039", "48.5558184", "12.1984594"},
            {"HS040", "48.5557793", "12.1983761"},
            {"HS042", "48.5558265", "12.1983607"},
            {"HS044", "48.5558505", "12.1983807"},
            {"HS101", "48.5565931", "12.1980544"},
            {"HS102", "48.5565624", "12.1981318"},
            {"HS103", "48.5565161", "12.1980092"},
            {"HS104", "48.5564782", "12.1981177"},
            {"HS105", "48.5565161", "12.1980092"},
            {"HS106", "48.5564807", "12.1980663"},
            {"HS107", "48.5563539", "12.1979073"},
            {"HS108", "48.5564807", "12.1980663"},
            {"HS109", "48.5570557", "12.1977605"},
            {"HS110", "48.5564807", "12.1980663"},
            {"HS111", "48.5561323", "12.1976811"},
            {"HS112", "48.5563351", "12.1979547"},
            {"HS113", "48.5560196", "12.1975879"},
            {"HS114", "48.5563351", "12.1979547"},
            {"HS115", "48.5559545", "12.1975232"},
            {"HS116", "48.5562973", "12.197859"},
            {"HS117", "48.5558765", "12.1975738"},
            {"HS118", "48.5562973", "12.197859"},
            {"HS119", "48.5557779", "12.1978309"},
            {"HS120", "48.5562497", "12.1978778"},
            {"HS121", "48.5557936", "12.1979499"},
            {"HS122", "48.5561633", "12.1978244"},
            {"HS123", "48.5557936", "12.1979499"},
            {"HS124", "48.5561441", "12.1978008"},
            {"HS125", "48.5557442", "12.1980814"},
            {"HS126", "48.5561441", "12.1978008"},
            {"HS127", "48.5557442", "12.1980814"},
            {"HS128", "48.5560938", "12.1977368"},
            {"HS129", "48.5557097", "12.1981731"},
            {"HS130", "48.5560566", "12.1977102"},
            {"HS131", "48.5556552", "12.1982255"},
            {"HS132", "48.5560002", "12.1976671"},
            {"HS133", "48.5556694", "12.1982805"},
            {"HS134", "48.5558813", "12.1977168"},
            {"HS135", "48.5556462", "12.1983423"},
            {"HS136", "48.5558986", "12.1977837"},
            {"HS137", "48.5556304", "12.1982862"},
            {"HS138", "48.5558371", "12.1978366"},
            {"HS139", "48.5556721", "12.1983461"},
            {"HS140", "48.5558302", "12.1979803"},
            {"HS141", "48.5556721", "12.1983461"},
            {"HS142", "48.5558302", "12.1979803"},
            {"HS143", "48.5557339", "12.1983366"},
            {"HS144", "48.5558302", "12.1979803"},
            {"HS145", "48.5557339", "12.1983366"},
            {"HS146", "48.5557819", "12.1981138"},
            {"HS147", "48.5557612", "12.1984195"},
            {"HS148", "48.5557819", "12.1981138"},
            {"HS149", "48.5557928", "12.198443"},
            {"HS150", "48.5557635", "12.1981612"},
            {"HS151", "48.5558184", "12.1984594"},
            {"HS152", "48.5557635", "12.1981612"},
            {"HS154", "48.5557793", "12.1983761"},
            {"HS156", "48.5558265", "12.1983607"},
            {"HS158", "48.5558505", "12.1983807"},
            {"HS201", "48.5566148", "12.1981308"},
            {"HS202", "48.5566148", "12.1981308"},
            {"HS203", "48.5565759", "12.1980967"},
            {"HS204", "48.5565759", "12.1980967"},
            {"HS205", "48.5565366", "12.1980632"},
            {"HS206", "48.5564948", "12.1980275"},
            {"HS207", "48.5564948", "12.1980275"},
            {"HS208", "48.5563539", "12.1979073"},
            {"HS209", "48.5563539", "12.1979073"},
            {"HS210", "48.556314", "12.1978732"},
            {"HS211", "48.556314", "12.1978732"},
            {"HS212", "48.5562661", "12.1978324"},
            {"HS213", "48.5562661", "12.1978324"},
            {"HS214", "48.5561862", "12.1977642"},
            {"HS215", "48.5561862", "12.1977642"},
            {"HS216", "48.5561862", "12.1977642"},
            {"HS217", "48.5561082", "12.1976977"},
            {"HS218", "48.5560736", "12.1976682"},
            {"HS219", "48.5560464", "12.197645"},
            {"HS220", "48.5560013", "12.1976066"},
            {"HS221", "48.5558989", "12.1976699"},
            {"HS222", "48.5558989", "12.1976699"},
            {"HS223", "48.5558662", "12.1977569"},
            {"HS224", "48.5558662", "12.1977569"},
            {"HS225", "48.5558375", "12.1978356"},
            {"HS226", "48.5558088", "12.1979116"},
            {"HS227", "48.5558088", "12.1979116"},
            {"HS228", "48.5558088", "12.1979116"},
            {"HS229", "48.5558088", "12.1979116"},
            {"HS230", "48.5557584", "12.1980435"},
            {"HS231", "48.5557097", "12.1981731"},
            {"HS232", "48.5557466", "12.198305"},
            {"HS233", "48.5557793", "12.1983761"},
            {"BM", "48.556183", "12.1984689"},
            {"LW001", "48.5567463", "12.1972278"},
            {"LW002", "48.556662", "12.1971956"},
            {"LW003", "48.5567836", "12.1972814"},
            {"LW004", "48.5566434", "12.1972533"},
            {"LW005", "48.5567339", "12.1973257"},
            {"LW006", "48.5566327", "12.1973163"},
            {"LW007", "48.5566984", "12.1974169"},
            {"LW008", "48.5565954", "12.1973954"},
            {"LW009", "48.5566753", "12.1974813"},
            {"LW010", "48.5565612", "12.1974746"},
            {"LW011", "48.5566522", "12.1975349"},
            {"LW012", "48.5564906", "12.1974256"},
            {"LW013", "48.5566668", "12.1975502"},
            {"LW014", "48.5564595", "12.1973988"},
            {"LW015", "48.556634", "12.1976253"},
            {"LW016", "48.5564538", "12.1973834"},
            {"LW017", "48.556634", "12.1976253"},
            {"LW018", "48.5564276", "12.1973707"},
            {"LW019", "48.5565843", "12.1976233"},
            {"LW020", "48.5564169", "12.1973251"},
            {"LW021", "48.5565843", "12.1976233"},
            {"LW022", "48.5564227", "12.1972849"},
            {"LW023", "48.5565532", "12.1975482"},
            {"LW024", "48.5564289", "12.1971374"},
            {"LW025", "48.5565057", "12.1975167"},
            {"LW026", "48.556345", "12.1971233"},
            {"LW027", "48.5564165", "12.1974336"},
            {"LW028", "48.5563326", "12.1972118"},
            {"LW029", "48.5563069", "12.1973746"},
            {"LW030", "48.5563233", "12.1972768"},
            {"LW031", "48.5562572", "12.1972881"},
            {"LW032", "48.5563055", "12.197258"},
            {"LW033", "48.5561906", "12.1972271"},
            {"LW034", "48.5562909", "12.1972412"},
            {"LW035", "48.5561595", "12.1971976"},
            {"LW036", "48.5562665", "12.197215"},
            {"LW037", "48.5560765", "12.1971393"},
            {"LW038", "48.5562203", "12.1971721"},
            {"LW039", "48.5560556", "12.1971178"},
            {"LW040", "48.556187", "12.1970608"},
            {"LW041", "48.5560392", "12.197091"},
            {"LW042", "48.5562451", "12.1968898"},
            {"LW043", "48.5561275", "12.196971"},
            {"LW045", "48.5561581", "12.1968791"},
            {"LS001", "48.555619", "12.1971458"},
            {"LS002", "48.555563", "12.1972597"},
            {"LS003", "48.5556731", "12.1971927"},
            {"LS004", "48.5556092", "12.1973093"},
            {"LS005", "48.5557042", "12.1972276"},
            {"LS006", "48.5556642", "12.1973898"},
            {"LS007", "48.5557672", "12.1972705"},
            {"LS008", "48.55565", "12.1974153"},
            {"LS009", "48.5557521", "12.1973"},
            {"LS010", "48.5556305", "12.197473"},
            {"LS011", "48.5557379", "12.1973241"},
            {"LS012", "48.5556039", "12.1975347"},
            {"LS013", "48.5556846", "12.1975321"},
            {"LS014", "48.5555915", "12.197583"},
            {"LS015", "48.5557112", "12.1974342"},
            {"LS016", "48.5555808", "12.1976179"},
            {"LS017", "48.5557494", "12.1974691"},
            {"LS018", "48.5554991", "12.1975763"},
            {"LS019", "48.5556854", "12.1976421"},
            {"LS021", "48.5556455", "12.1976139"},
            {"LS023", "48.5556552", "12.1976703"},
            {"LS025", "48.5556499", "12.1977159"},
            {"LS027", "48.5555914", "12.197795"},
            {"LS029", "48.5555097", "12.1976917"},
            {"LS031", "48.5554387", "12.1976501"},
            {"LW101", "48.5566024", "12.1976648"},
            {"LW102", "48.5564923", "12.1975817"},
            {"LW103", "48.5564674", "12.1975388"},
            {"LW104", "48.556407", "12.1974986"},
            {"LW105", "48.5563591", "12.1974637"},
            {"LW106", "48.5562535", "12.1973671"},
            {"LW107", "48.5562056", "12.1973188"},
            {"LW108", "48.5561737", "12.1972893"},
            {"LW109", "48.5561382", "12.1972625"},
            {"LW110", "48.5560601", "12.1971646"},
            {"LW111", "48.556029", "12.1971324"},
            {"LW112", "48.5559589", "12.1970614"},
            {"LS101", "48.5558444", "12.1969984"},
            {"LS102", "48.5558195", "12.1970936"},
            {"LS103", "48.5557955", "12.1971781"},
            {"LS104", "48.5557715", "12.197284"},
            {"LS105", "48.5557715", "12.197284"},
            {"LS106", "48.5557715", "12.197284"},
            {"LS107", "48.5557369", "12.1974771"},
            {"LS108", "48.5557103", "12.1975602"},
            {"LS109", "48.5556819", "12.1976326"},
            {"LS110", "48.5556615", "12.1976983"},
            {"LS111", "48.5556136", "12.197811"},
            {"LS201", "48.5558444", "12.1969984"},
            {"ZH001", "48.5558525", "12.197049"},
            {"ZH002", "48.5559457", "12.196978"},
            {"ZH003", "48.555983", "12.1968519"},
            {"ZH004", "48.5560176", "12.1967245"},
            {"ZH005", "48.5560469", "12.1966011"},
            {"ZH006", "48.5559608", "12.1965837"},
            {"ZH007", "48.5558569", "12.1965099"},
            {"ZH008", "48.5557948", "12.1963503"},
            {"ZH009", "48.5557646", "12.1964281"},
            {"ZH010", "48.5557291", "12.1963262"},
            {"ZH011", "48.5556936", "12.1962833"},
            {"ZH012", "48.5556874", "12.1964536"},
            {"ZH013", "48.5556483", "12.1965622"},
            {"ZH014", "48.5555604", "12.1966239"},
            {"ZH015", "48.555611", "12.1966547"},
            {"ZH016", "48.5555941", "12.1967057"},
            {"ZH017", "48.5556466", "12.1969323"},
            {"ZH018", "48.5557292", "12.196994"},
            {"TI001", "48.5552509", "12.1975424"},
            {"TI002", "48.5551417", "12.1974365"},
            {"TI003", "48.5552873", "12.1974217"},
            {"TI004", "48.5551932", "12.1973117"},
            {"TI005", "48.5553068", "12.197356"},
            {"TI006", "48.5552367", "12.1972594"},
            {"TI007", "48.555329", "12.1973117"},
            {"TI008", "48.5552527", "12.197199"},
            {"TI009", "48.5553503", "12.1972406"},
            {"TI010", "48.5552624", "12.1971615"},
            {"TI011", "48.555384", "12.1971883"},
            {"TI012", "48.5552872", "12.1971387"},
            {"TI013", "48.5554248", "12.1971535"},
            {"TI014", "48.5553094", "12.1970972"},
            {"TI015", "48.5553724", "12.1971147"},
            {"TI016", "48.5553249", "12.1969777"},
            {"TI017", "48.5552487", "12.1975387"},
            {"TI018", "48.5553524", "12.1968946"},
            {"TI019", "48.5554021", "12.197022"},
            {"TI020", "48.5553773", "12.1968195"},
            {"TI021", "48.5554154", "12.1969871"},
            {"TI022", "48.555419", "12.196727"},
            {"TI023", "48.5554651", "12.1969496"},
            {"TI025", "48.5554926", "12.1968745"},
            {"TI027", "48.5555104", "12.1968289"},
            {"TI029", "48.5555273", "12.1967887"},
            {"IF001", "48.5550412", "12.1972518"},
            {"IF002", "48.5549373", "12.1971927"},
            {"IF003", "48.5550253", "12.1971796"},
            {"IF005", "48.5550391", "12.197136"},
            {"IF006", "48.5549083", "12.1971817"},
            {"IF007", "48.5550485", "12.1971131"},
            {"IF008", "48.5549774", "12.1971381"},
            {"IF009", "48.5550745", "12.197051"},
            {"IF010", "48.5550158", "12.1970471"},
            {"IF011", "48.5550968", "12.1969889"},
            {"IF012", "48.5550647", "12.1969962"},
            {"IF013", "48.5551669", "12.1968278"},
            {"IF014", "48.555041", "12.1969212"},
            {"IF015", "48.5552067", "12.1967101"},
            {"IF016", "48.5551506", "12.1966964"},
            {"IF017", "48.5552236", "12.196669"},
            {"IF018", "48.5551919", "12.1965962"},
            {"IF019", "48.5552535", "12.1965988"},
            {"IF020", "48.555212", "12.1965414"},
            {"IF021", "48.5553263", "12.1965356"},
            {"IF022", "48.5552978", "12.196511"},
            {"IF023", "48.5552662", "12.1964837"},
            {"IF024", "48.5552397", "12.1964601"},
            {"IF025", "48.5552271", "12.196406"},
            {"IF026", "48.5551619", "12.1963897"},
            {"TI101", "48.5552435", "12.1975587"},
            {"TI102", "48.5551397", "12.1974662"},
            {"TI103", "48.5552613", "12.1975051"},
            {"TI104", "48.5551592", "12.1974085"},
            {"TI105", "48.5552977", "12.1974045"},
            {"TI106", "48.5552099", "12.1973429"},
            {"TI107", "48.5553199", "12.1973321"},
            {"TI108", "48.5552232", "12.1973053"},
            {"TI109", "48.5553519", "12.1972235"},
            {"TI110", "48.5552365", "12.1972718"},
            {"TI111", "48.5553839", "12.1971243"},
            {"TI112", "48.5552525", "12.197245"},
            {"TI113", "48.5554238", "12.1971538"},
            {"TI114", "48.5552614", "12.1972088"},
            {"TI116", "48.5552738", "12.1971672"},
            {"TI117", "48.5554451", "12.1970546"},
            {"TI118", "48.5553004", "12.1971176"},
            {"TI119", "48.5554522", "12.1970197"},
            {"TI120", "48.5553004", "12.1971176"},
            {"TI121", "48.5554691", "12.1969459"},
            {"TI122", "48.5553004", "12.1971176"},
            {"TI123", "48.5554922", "12.1968829"},
            {"TI124", "48.5553245", "12.1969768"},
            {"TI125", "48.5555064", "12.196836"},
            {"TI126", "48.5553547", "12.196954"},
            {"TI127", "48.5555313", "12.1967689"},
            {"TI128", "48.5553769", "12.196895"},
            {"TI130", "48.555392", "12.1968615"},
            {"TI132", "48.5554008", "12.1968401"},
            {"TI134", "48.5554088", "12.1968146"},
            {"TI136", "48.555423", "12.1967797"},
            {"TI138", "48.5554354", "12.1967381"},
            {"TI140", "48.5554532", "12.1966912"},
            {"TI201", "48.5551762", "12.197465"},
            {"TI202", "48.5552161", "12.1973537"},
            {"TI203", "48.5552436", "12.1973028"},
            {"TI204", "48.5552569", "12.197272"},
            {"TI205", "48.5552667", "12.1972425"},
            {"TI206", "48.5552871", "12.1971982"},
            {"TI207", "48.5553057", "12.1971151"},
            {"TI208", "48.5553057", "12.1971151"},
            {"TI209", "48.5553057", "12.1971151"},
            {"TI210", "48.5553687", "12.1969649"},
            {"TI211", "48.5553785", "12.1969273"},
            {"TI212", "48.5553989", "12.1968817"},
            {"TI213", "48.5554078", "12.1968549"},
            {"TI214", "48.555422", "12.1968214"},
            {"TI215", "48.5554317", "12.1967879"},
            {"TI216", "48.5554441", "12.1967463"},
            {"TI217", "48.5554619", "12.1966927"},
            {"LW047", "48.5562043", "12.1968181"},
            {"LW049", "48.5567463", "12.1972277"},
            {"SC001", "48.5567463", "12.1972278"},
            {"SC002", "48.5505791", "12.1843076"},
            {"SC003", "48.5505791", "12.1843076"},
            {"SC004", "48.5505791", "12.1843076"},
            {"SC005", "48.5505791", "12.1843076"},
          
{"A 001", "48.5555267", "12.1988182"},
{"A 002", "48.5555267", "12.1988182"},
{"A 003", "48.5555267", "12.1988182"},
{"A 004", "48.5555267", "12.1988182"},
{"A 005", "48.5555267", "12.1988182"},
{"A 006", "48.5555267", "12.1988182"},
{"A 007", "48.5555267", "12.1988182"},
{"A 008", "48.5555267", "12.1988182"},
{"A 009", "48.5555267", "12.1988182"},
{"A 010", "48.5555267", "12.1988182"},
{"A 011", "48.5555267", "12.1988182"},

{"A 101", "48.5555267", "12.1988182"},
{"A 102", "48.5555267", "12.1988182"},
{"A 103", "48.5555267", "12.1988182"},
{"A 104", "48.5555267", "12.1988182"},
{"A 105", "48.5555267", "12.1988182"},
{"A 106", "48.5555267", "12.1988182"},
{"A 107", "48.5555267", "12.1988182"},
{"A 108", "48.5555267", "12.1988182"},
{"A 109", "48.5555267", "12.1988182"},
{"A 110", "48.5555267", "12.1988182"},
{"A 111", "48.5555267", "12.1988182"},
{"A 112", "48.5555267", "12.1988182"},
{"A 113", "48.5555267", "12.1988182"},
{"A 114", "48.5555267", "12.1988182"},
{"A 115", "48.5555267", "12.1988182"},
{"A 116", "48.5555267", "12.1988182"},
{"A 117", "48.5555267", "12.1988182"},

{"A 201", "48.5555267", "12.1988182"},
{"A 202", "48.5555267", "12.1988182"},
{"A 203", "48.5555267", "12.1988182"},
{"A 204", "48.5555267", "12.1988182"},
{"A 205", "48.5555267", "12.1988182"},
{"A 206", "48.5555267", "12.1988182"},
{"A 207", "48.5555267", "12.1988182"},
{"A 208", "48.5555267", "12.1988182"},
{"A 209", "48.5555267", "12.1988182"},
{"A 210", "48.5555267", "12.1988182"},
{"A 211", "48.5555267", "12.1988182"},
{"A 212", "48.5555267", "12.1988182"},
{"A 213", "48.5555267", "12.1988182"},
{"A 214", "48.5555267", "12.1988182"},
{"A 215", "48.5555267", "12.1988182"},
{"A 216", "48.5555267", "12.1988182"},
{"A 217", "48.5555267", "12.1988182"},
{"A 218", "48.5555267", "12.1988182"},
{"A 219", "48.5555267", "12.1988182"},
{"A 220", "48.5555267", "12.1988182"},
{"A 221", "48.5555267", "12.1988182"},
{"A 222", "48.5555267", "12.1988182"},
{"A 223", "48.5555267", "12.1988182"},
{"A 224", "48.5555267", "12.1988182"},
{"A 225", "48.5555267", "12.1988182"},
{"A 226", "48.5555267", "12.1988182"},
{"A 227", "48.5555267", "12.1988182"}
    };

    private GoogleMap mMap;
    int pos;

    String[] salles;
    String title;
    //Andrew
    //int lati,lngi; float

    float lati, lngi;
    float[] lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTitle(R.string.MAP);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        int layoutItemId = android.R.layout.simple_dropdown_item_1line;
        // matrix contains : name | Lat | Long

        salles = new String[matrix.length];
        //Andrew float
        lat = new float[matrix.length];
        lng = new float[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            salles[i] = matrix[i][0];
            //Andrew
            //lat[i]= Integer.parseInt(matrix[i][1]);
            //lng[i]= Integer.parseInt(matrix[i][2]);

            lat[i] = Float.parseFloat(matrix[i][1]);
            lng[i] = Float.parseFloat(matrix[i][2]);
        }
        List<String> Classrooms = Arrays.asList(salles);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, layoutItemId, Classrooms);

        AutoCompleteTextView autocompleteView =
                (AutoCompleteTextView) findViewById(R.id.autocompleteView);

        autocompleteView.setAdapter(adapter);

        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MapsActivity.this,adapter.getItem(position).toString(),Toast.LENGTH_SHORT).show();
                pos = position;
                //onMapReady(mMap);
                String HallName = adapter.getItem(position).toString();
                CreateSuitableMarker(HallName);

                //AndrewModification2

                int HallNumber = -1;
                try {
                    String FloatString = HallName.substring(2, 3);
                    HallNumber = Integer.parseInt(FloatString);
                } catch (Exception e) {

                }

                if (HallNumber != -1) {

                    if (HallNumber == 0)
                        Toast.makeText(getApplicationContext(), "Raum " + HallName + " befindet sich im Erdgeschoss",
                                Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "Raum " + HallName + " befindet sich in der " + HallNumber + ". Etage",
                                Toast.LENGTH_LONG).show();


                }

            }
        });


    }

    Marker BuildingMarker;

    void CreateSuitableMarker(String a) {
        if (BuildingMarker != null)
            BuildingMarker.remove();
        title = a;
        for (int i = 0; i < matrix.length; i++)
            if (matrix[i][0].toString().equals(a)) {
                String b1 = matrix[i][1];
                String b2 = matrix[i][2];
                lati = Float.parseFloat(b1);
                lngi = Float.parseFloat(b2);
            }

        //adding marker
        BuildingMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lati, lngi))
                .title(title));
        //centering the camera
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lati, lngi))
                .zoom(20)
                .bearing(0)
                .tilt(0)
                .build();
//lati lngi
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //affeting clicked clasroom
        /*
        for(int i=0; i<salles.length;i++)
        {
            title = salles[pos];
            lati = lat[pos];
            lngi =lng[pos];

        }

        //adding marker
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lati, lngi))
                .title(title));
        //centering the camera
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lati, lngi))
                .zoom(15)
                .bearing(0)
                .tilt(45)
                .build();
//lati lngi
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
*/
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        if (googleApiready) {
            mGoogleApiClient.connect();
        }

    }


    GoogleApiClient mGoogleApiClient;

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        googleApiready=true;
    }


    boolean googleApiready=false;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;
    LatLng latLng;


    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            mMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    }


    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        // mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }

}
