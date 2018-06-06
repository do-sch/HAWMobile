package de.haw_landshut.hawmobile.base;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class RaumData {
    public RaumData(String room,String coord1,String coord2){
        this.room=room;
        this.coord1=coord1;
        this.coord2=coord2;

    }
    private String room;
    private String coord1;
    private String coord2;

    @PrimaryKey(autoGenerate = true)
    private long roomkey;


    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getCoord1() {
        return coord1;
    }

    public void setCoord1(String coord1) {
        this.coord1 = coord1;
    }

    public String getCoord2() {
        return coord2;
    }

    public void setCoord2(String coord2) {
        this.coord2 = coord2;
    }

    public long getRoomkey() {
        return roomkey;
    }

    public void setRoomkey(long roomkey) {
        this.roomkey = roomkey;
    }

    public static RaumData[] populateRooms(){
        return new RaumData[]{
                new RaumData("BS001","48.5568648","12.1982666"),
                new RaumData("BS002","48.5567973","12.1981969"),
                new RaumData("BS003","48.5568949","12.1981379"),
                new RaumData("BS004","48.5569322","12.1980279"),
                new RaumData("BS005","48.5569801","12.1978938"),
                new RaumData("BS006","48.5570192","12.197816"),
                new RaumData("BS007","48.5570405","12.1977516"),
                new RaumData("BS008","48.5570813","12.1976658"),
                new RaumData("BS009","48.5571441","12.1979064"),
                new RaumData("BS010","48.5571441","12.1979064"),
                new RaumData("BS011","48.5571629","12.1979238"),
                new RaumData("BS012","48.5571816","12.1979411"),
                new RaumData("BS013","48.5572061","12.1979637"),
                new RaumData("BS014","48.5572375","12.1979929"),
                new RaumData("BS015","48.5572602","12.198014"),
                new RaumData("BS016","48.5573048","12.1979986"),
                new RaumData("BS017","48.5573048","12.1979986"),
                new RaumData("BS018","48.5572884","12.1980402"),
                new RaumData("BS019","48.5572585","12.1981228"),
                new RaumData("BS020","48.5572585","12.1981228"),
                new RaumData("BS021","48.5572474","12.1981536"),
                new RaumData("BS022","48.5571639","12.1980653"),
                new RaumData("BS023","48.5571639","12.1980653"),
                new RaumData("BS024","48.5571639","12.1980653"),
                new RaumData("BS025","48.5571639","12.1980653"),
                new RaumData("BS026","48.5571703","12.1979686"),
                new RaumData("BS027","48.5571703","12.1979686"),
                new RaumData("BS028","48.5571703","12.1979686"),
                new RaumData("BS029","48.5571191","12.1979355"),
                new RaumData("BS030","48.5571191","12.1979355"),
                new RaumData("BS031","48.5571191","12.1979355"),
                new RaumData("BS032","48.5570682","12.1979872"),
                new RaumData("BS033","48.5570472","12.198046"),
                new RaumData("BS034","48.5570472","12.198046"),
                new RaumData("BS035","48.5570472","12.198046"),
                new RaumData("BS036","48.5570147","12.198137"),
                new RaumData("BS037","48.5570147","12.198137"),
                new RaumData("BS038","48.5570147","12.198137"),
                new RaumData("BS039","48.5569841","12.1982228"),
                new RaumData("BS040","48.5569841","12.1982228"),
                new RaumData("BS041","48.5569496","12.1983691"),
                new RaumData("BS042","48.5569496","12.1983691"),
                new RaumData("BS043","48.5569496","12.1983691"),
                new RaumData("BS044","48.5569407","12.198395"),
                new RaumData("BS045","48.5569407","12.198395"),
                new RaumData("BS101","48.5568777","12.1982628"),
                new RaumData("BS102","48.556833","12.1982218"),
                new RaumData("BS103","48.5568997","12.1981749"),
                new RaumData("BS104","48.5569596","12.1979999"),
                new RaumData("BS105","48.5570248","12.1979364"),
                new RaumData("BS106","48.5570704","12.197813"),
                new RaumData("BS107","48.5571055","12.1977055"),
                new RaumData("BS108","48.5571088","12.1978737"),
                new RaumData("BS109","48.5570788","12.1979575"),
                new RaumData("BS110","48.5570472","12.198046"),
                new RaumData("BS111","48.5570472","12.198046"),
                new RaumData("BS112","48.5570041","12.1981667"),
                new RaumData("BS113","48.5570041","12.1981667"),
                new RaumData("BS114","48.5569687","12.1982659"),
                new RaumData("BS115","48.5569407","12.198395"),
                new RaumData("BS116","48.5569183","12.198458"),
                new RaumData("BS201","48.5568777","12.1982628"),
                new RaumData("BS202","48.5568997","12.1981749"),
                new RaumData("BS203","48.5569596","12.1979999"),
                new RaumData("BS204","48.5570118","12.1978806"),
                new RaumData("BS205","48.5570269","12.1978319"),
                new RaumData("BS206","48.5570557","12.1977605"),
                new RaumData("BS207","48.5571055","12.1977055"),
                new RaumData("BS208","48.5570276","12.1979879"),
                new RaumData("BS209","48.5570257","12.1980282"),
                new RaumData("BS210","48.5569855","12.1981073"),
                new RaumData("HS001","48.5565931","12.1980544"),
                new RaumData("HS002","48.5565624","12.1981318"),
                new RaumData("HS003","48.5565161","12.1980092"),
                new RaumData("HS004","48.5564782","12.1981177"),
                new RaumData("HS005","48.5563539","12.1979073"),
                new RaumData("HS006","48.5564807","12.1980663"),
                new RaumData("HS007","48.5562365","12.1977711"),
                new RaumData("HS008","48.5564807","12.1980663"),
                new RaumData("HS009","48.5561323","12.1976811"),
                new RaumData("HS010","48.5564807","12.1980663"),
                new RaumData("HS011","48.5560196","12.1975879"),
                new RaumData("HS012","48.5563351","12.1979547"),
                new RaumData("HS013","48.5558727","12.1975706"),
                new RaumData("HS014","48.5562973","12.197859"),
                new RaumData("HS015","48.5557636","12.1978185"),
                new RaumData("HS016","48.5562661","12.1978324"),
                new RaumData("HS017","48.5557281","12.1980179"),
                new RaumData("HS018","48.5561441","12.1978008"),
                new RaumData("HS019","48.5556821","12.1981507"),
                new RaumData("HS020","48.5561391","12.1977648"),
                new RaumData("HS021","48.5556552","12.1982255"),
                new RaumData("HS022","48.5560938","12.1977368"),
                new RaumData("HS023","48.5556552","12.1982255"),
                new RaumData("HS024","48.5560566","12.1977102"),
                new RaumData("HS025","48.5556304","12.1982862"),
                new RaumData("HS026","48.5558812","12.1977169"),
                new RaumData("HS027","48.5556721","12.1983461"),
                new RaumData("HS028","48.5558986","12.1977837"),
                new RaumData("HS029","48.5556669","12.1983591"),
                new RaumData("HS030","48.5558364","12.1978386"),
                new RaumData("HS031","48.5557339","12.1983366"),
                new RaumData("HS032","48.5558302","12.1979803"),
                new RaumData("HS033","48.5557612","12.1984195"),
                new RaumData("HS034","48.5558052","12.1979884"),
                new RaumData("HS035","48.5557928","12.198443"),
                new RaumData("HS036","48.5557819","12.1981138"),
                new RaumData("HS037","48.5558184","12.1984594"),
                new RaumData("HS038","48.5557635","12.1981612"),
                new RaumData("HS039","48.5558184","12.1984594"),
                new RaumData("HS040","48.5557793","12.1983761"),
                new RaumData("HS042","48.5558265","12.1983607"),
                new RaumData("HS044","48.5558505","12.1983807"),
                new RaumData("HS101","48.5565931","12.1980544"),
                new RaumData("HS102","48.5565624","12.1981318"),
                new RaumData("HS103","48.5565161","12.1980092"),
                new RaumData("HS104","48.5564782","12.1981177"),
                new RaumData("HS105","48.5565161","12.1980092"),
                new RaumData("HS106","48.5564807","12.1980663"),
                new RaumData("HS107","48.5563539","12.1979073"),
                new RaumData("HS108","48.5564807","12.1980663"),
                new RaumData("HS109","48.5570557","12.1977605"),
                new RaumData("HS110","48.5564807","12.1980663"),
                new RaumData("HS111","48.5561323","12.1976811"),
                new RaumData("HS112","48.5563351","12.1979547"),
                new RaumData("HS113","48.5560196","12.1975879"),
                new RaumData("HS114","48.5563351","12.1979547"),
                new RaumData("HS115","48.5559545","12.1975232"),
                new RaumData("HS116","48.5562973","12.197859"),
                new RaumData("HS117","48.5558765","12.1975738"),
                new RaumData("HS118","48.5562973","12.197859"),
                new RaumData("HS119","48.5557779","12.1978309"),
                new RaumData("HS120","48.5562497","12.1978778"),
                new RaumData("HS121","48.5557936","12.1979499"),
                new RaumData("HS122","48.5561633","12.1978244"),
                new RaumData("HS123","48.5557936","12.1979499"),
                new RaumData("HS124","48.5561441","12.1978008"),
                new RaumData("HS125","48.5557442","12.1980814"),
                new RaumData("HS126","48.5561441","12.1978008"),
                new RaumData("HS127","48.5557442","12.1980814"),
                new RaumData("HS128","48.5560938","12.1977368"),
                new RaumData("HS129","48.5557097","12.1981731"),
                new RaumData("HS130","48.5560566","12.1977102"),
                new RaumData("HS131","48.5556552","12.1982255"),
                new RaumData("HS132","48.5560002","12.1976671"),
                new RaumData("HS133","48.5556694","12.1982805"),
                new RaumData("HS134","48.5558813","12.1977168"),
                new RaumData("HS135","48.5556462","12.1983423"),
                new RaumData("HS136","48.5558986","12.1977837"),
                new RaumData("HS137","48.5556304","12.1982862"),
                new RaumData("HS138","48.5558371","12.1978366"),
                new RaumData("HS139","48.5556721","12.1983461"),
                new RaumData("HS140","48.5558302","12.1979803"),
                new RaumData("HS141","48.5556721","12.1983461"),
                new RaumData("HS142","48.5558302","12.1979803"),
                new RaumData("HS143","48.5557339","12.1983366"),
                new RaumData("HS144","48.5558302","12.1979803"),
                new RaumData("HS145","48.5557339","12.1983366"),
                new RaumData("HS146","48.5557819","12.1981138"),
                new RaumData("HS147","48.5557612","12.1984195"),
                new RaumData("HS148","48.5557819","12.1981138"),
                new RaumData("HS149","48.5557928","12.198443"),
                new RaumData("HS150","48.5557635","12.1981612"),
                new RaumData("HS151","48.5558184","12.1984594"),
                new RaumData("HS152","48.5557635","12.1981612"),
                new RaumData("HS154","48.5557793","12.1983761"),
                new RaumData("HS156","48.5558265","12.1983607"),
                new RaumData("HS158","48.5558505","12.1983807"),
                new RaumData("HS201","48.5566148","12.1981308"),
                new RaumData("HS202","48.5566148","12.1981308"),
                new RaumData("HS203","48.5565759","12.1980967"),
                new RaumData("HS204","48.5565759","12.1980967"),
                new RaumData("HS205","48.5565366","12.1980632"),
                new RaumData("HS206","48.5564948","12.1980275"),
                new RaumData("HS207","48.5564948","12.1980275"),
                new RaumData("HS208","48.5563539","12.1979073"),
                new RaumData("HS209","48.5563539","12.1979073"),
                new RaumData("HS210","48.556314","12.1978732"),
                new RaumData("HS211","48.556314","12.1978732"),
                new RaumData("HS212","48.5562661","12.1978324"),
                new RaumData("HS213","48.5562661","12.1978324"),
                new RaumData("HS214","48.5561862","12.1977642"),
                new RaumData("HS215","48.5561862","12.1977642"),
                new RaumData("HS216","48.5561862","12.1977642"),
                new RaumData("HS217","48.5561082","12.1976977"),
                new RaumData("HS218","48.5560736","12.1976682"),
                new RaumData("HS219","48.5560464","12.197645"),
                new RaumData("HS220","48.5560013","12.1976066"),
                new RaumData("HS221","48.5558989","12.1976699"),
                new RaumData("HS222","48.5558989","12.1976699"),
                new RaumData("HS223","48.5558662","12.1977569"),
                new RaumData("HS224","48.5558662","12.1977569"),
                new RaumData("HS225","48.5558375","12.1978356"),
                new RaumData("HS226","48.5558088","12.1979116"),
                new RaumData("HS227","48.5558088","12.1979116"),
                new RaumData("HS228","48.5558088","12.1979116"),
                new RaumData("HS229","48.5558088","12.1979116"),
                new RaumData("HS230","48.5557584","12.1980435"),
                new RaumData("HS231","48.5557097","12.1981731"),
                new RaumData("HS232","48.5557466","12.198305"),
                new RaumData("HS233","48.5557793","12.1983761"),
                new RaumData("BM","48.556183","12.1984689"),
                new RaumData("LW001","48.5567463","12.1972278"),
                new RaumData("LW002","48.556662","12.1971956"),
                new RaumData("LW003","48.5567836","12.1972814"),
                new RaumData("LW004","48.5566434","12.1972533"),
                new RaumData("LW005","48.5567339","12.1973257"),
                new RaumData("LW006","48.5566327","12.1973163"),
                new RaumData("LW007","48.5566984","12.1974169"),
                new RaumData("LW008","48.5565954","12.1973954"),
                new RaumData("LW009","48.5566753","12.1974813"),
                new RaumData("LW010","48.5565612","12.1974746"),
                new RaumData("LW011","48.5566522","12.1975349"),
                new RaumData("LW012","48.5564906","12.1974256"),
                new RaumData("LW013","48.5566668","12.1975502"),
                new RaumData("LW014","48.5564595","12.1973988"),
                new RaumData("LW015","48.556634","12.1976253"),
                new RaumData("LW016","48.5564538","12.1973834"),
                new RaumData("LW017","48.556634","12.1976253"),
                new RaumData("LW018","48.5564276","12.1973707"),
                new RaumData("LW019","48.5565843","12.1976233"),
                new RaumData("LW020","48.5564169","12.1973251"),
                new RaumData("LW021","48.5565843","12.1976233"),
                new RaumData("LW022","48.5564227","12.1972849"),
                new RaumData("LW023","48.5565532","12.1975482"),
                new RaumData("LW024","48.5564289","12.1971374"),
                new RaumData("LW025","48.5565057","12.1975167"),
                new RaumData("LW026","48.556345","12.1971233"),
                new RaumData("LW027","48.5564165","12.1974336"),
                new RaumData("LW028","48.5563326","12.1972118"),
                new RaumData("LW029","48.5563069","12.1973746"),
                new RaumData("LW030","48.5563233","12.1972768"),
                new RaumData("LW031","48.5562572","12.1972881"),
                new RaumData("LW032","48.5563055","12.197258"),
                new RaumData("LW033","48.5561906","12.1972271"),
                new RaumData("LW034","48.5562909","12.1972412"),
                new RaumData("LW035","48.5561595","12.1971976"),
                new RaumData("LW036","48.5562665","12.197215"),
                new RaumData("LW037","48.5560765","12.1971393"),
                new RaumData("LW038","48.5562203","12.1971721"),
                new RaumData("LW039","48.5560556","12.1971178"),
                new RaumData("LW040","48.556187","12.1970608"),
                new RaumData("LW041","48.5560392","12.197091"),
                new RaumData("LW042","48.5562451","12.1968898"),
                new RaumData("LW043","48.5561275","12.196971"),
                new RaumData("LW045","48.5561581","12.1968791"),
                new RaumData("LS001","48.555619","12.1971458"),
                new RaumData("LS002","48.555563","12.1972597"),
                new RaumData("LS003","48.5556731","12.1971927"),
                new RaumData("LS004","48.5556092","12.1973093"),
                new RaumData("LS005","48.5557042","12.1972276"),
                new RaumData("LS006","48.5556642","12.1973898"),
                new RaumData("LS007","48.5557672","12.1972705"),
                new RaumData("LS008","48.55565","12.1974153"),
                new RaumData("LS009","48.5557521","12.1973"),
                new RaumData("LS010","48.5556305","12.197473"),
                new RaumData("LS011","48.5557379","12.1973241"),
                new RaumData("LS012","48.5556039","12.1975347"),
                new RaumData("LS013","48.5556846","12.1975321"),
                new RaumData("LS014","48.5555915","12.197583"),
                new RaumData("LS015","48.5557112","12.1974342"),
                new RaumData("LS016","48.5555808","12.1976179"),
                new RaumData("LS017","48.5557494","12.1974691"),
                new RaumData("LS018","48.5554991","12.1975763"),
                new RaumData("LS019","48.5556854","12.1976421"),
                new RaumData("LS021","48.5556455","12.1976139"),
                new RaumData("LS023","48.5556552","12.1976703"),
                new RaumData("LS025","48.5556499","12.1977159"),
                new RaumData("LS027","48.5555914","12.197795"),
                new RaumData("LS029","48.5555097","12.1976917"),
                new RaumData("LS031","48.5554387","12.1976501"),
                new RaumData("LW101","48.5566024","12.1976648"),
                new RaumData("LW102","48.5564923","12.1975817"),
                new RaumData("LW103","48.5564674","12.1975388"),
                new RaumData("LW104","48.556407","12.1974986"),
                new RaumData("LW105","48.5563591","12.1974637"),
                new RaumData("LW106","48.5562535","12.1973671"),
                new RaumData("LW107","48.5562056","12.1973188"),
                new RaumData("LW108","48.5561737","12.1972893"),
                new RaumData("LW109","48.5561382","12.1972625"),
                new RaumData("LW110","48.5560601","12.1971646"),
                new RaumData("LW111","48.556029","12.1971324"),
                new RaumData("LW112","48.5559589","12.1970614"),
                new RaumData("LS101","48.5558444","12.1969984"),
                new RaumData("LS102","48.5558195","12.1970936"),
                new RaumData("LS103","48.5557955","12.1971781"),
                new RaumData("LS104","48.5557715","12.197284"),
                new RaumData("LS105","48.5557715","12.197284"),
                new RaumData("LS106","48.5557715","12.197284"),
                new RaumData("LS107","48.5557369","12.1974771"),
                new RaumData("LS108","48.5557103","12.1975602"),
                new RaumData("LS109","48.5556819","12.1976326"),
                new RaumData("LS110","48.5556615","12.1976983"),
                new RaumData("LS111","48.5556136","12.197811"),
                new RaumData("LS201","48.5558444","12.1969984"),
                new RaumData("ZH001","48.5558525","12.197049"),
                new RaumData("ZH002","48.5559457","12.196978"),
                new RaumData("ZH003","48.555983","12.1968519"),
                new RaumData("ZH004","48.5560176","12.1967245"),
                new RaumData("ZH005","48.5560469","12.1966011"),
                new RaumData("ZH006","48.5559608","12.1965837"),
                new RaumData("ZH007","48.5558569","12.1965099"),
                new RaumData("ZH008","48.5557948","12.1963503"),
                new RaumData("ZH009","48.5557646","12.1964281"),
                new RaumData("ZH010","48.5557291","12.1963262"),
                new RaumData("ZH011","48.5556936","12.1962833"),
                new RaumData("ZH012","48.5556874","12.1964536"),
                new RaumData("ZH013","48.5556483","12.1965622"),
                new RaumData("ZH014","48.5555604","12.1966239"),
                new RaumData("ZH015","48.555611","12.1966547"),
                new RaumData("ZH016","48.5555941","12.1967057"),
                new RaumData("ZH017","48.5556466","12.1969323"),
                new RaumData("ZH018","48.5557292","12.196994"),
                new RaumData("TI001","48.5552509","12.1975424"),
                new RaumData("TI002","48.5551417","12.1974365"),
                new RaumData("TI003","48.5552873","12.1974217"),
                new RaumData("TI004","48.5551932","12.1973117"),
                new RaumData("TI005","48.5553068","12.197356"),
                new RaumData("TI006","48.5552367","12.1972594"),
                new RaumData("TI007","48.555329","12.1973117"),
                new RaumData("TI008","48.5552527","12.197199"),
                new RaumData("TI009","48.5553503","12.1972406"),
                new RaumData("TI010","48.5552624","12.1971615"),
                new RaumData("TI011","48.555384","12.1971883"),
                new RaumData("TI012","48.5552872","12.1971387"),
                new RaumData("TI013","48.5554248","12.1971535"),
                new RaumData("TI014","48.5553094","12.1970972"),
                new RaumData("TI015","48.5553724","12.1971147"),
                new RaumData("TI016","48.5553249","12.1969777"),
                new RaumData("TI017","48.5552487","12.1975387"),
                new RaumData("TI018","48.5553524","12.1968946"),
                new RaumData("TI019","48.5554021","12.197022"),
                new RaumData("TI020","48.5553773","12.1968195"),
                new RaumData("TI021","48.5554154","12.1969871"),
                new RaumData("TI022","48.555419","12.196727"),
                new RaumData("TI023","48.5554651","12.1969496"),
                new RaumData("TI025","48.5554926","12.1968745"),
                new RaumData("TI027","48.5555104","12.1968289"),
                new RaumData("TI029","48.5555273","12.1967887"),
                new RaumData("IF001","48.5550412","12.1972518"),
                new RaumData("IF002","48.5549373","12.1971927"),
                new RaumData("IF003","48.5550253","12.1971796"),
                new RaumData("IF005","48.5550391","12.197136"),
                new RaumData("IF006","48.5549083","12.1971817"),
                new RaumData("IF007","48.5550485","12.1971131"),
                new RaumData("IF008","48.5549774","12.1971381"),
                new RaumData("IF009","48.5550745","12.197051"),
                new RaumData("IF010","48.5550158","12.1970471"),
                new RaumData("IF011","48.5550968","12.1969889"),
                new RaumData("IF012","48.5550647","12.1969962"),
                new RaumData("IF013","48.5551669","12.1968278"),
                new RaumData("IF014","48.555041","12.1969212"),
                new RaumData("IF015","48.5552067","12.1967101"),
                new RaumData("IF016","48.5551506","12.1966964"),
                new RaumData("IF017","48.5552236","12.196669"),
                new RaumData("IF018","48.5551919","12.1965962"),
                new RaumData("IF019","48.5552535","12.1965988"),
                new RaumData("IF020","48.555212","12.1965414"),
                new RaumData("IF021","48.5553263","12.1965356"),
                new RaumData("IF022","48.5552978","12.196511"),
                new RaumData("IF023","48.5552662","12.1964837"),
                new RaumData("IF024","48.5552397","12.1964601"),
                new RaumData("IF025","48.5552271","12.196406"),
                new RaumData("IF026","48.5551619","12.1963897"),
                new RaumData("TI101","48.5552435","12.1975587"),
                new RaumData("TI102","48.5551397","12.1974662"),
                new RaumData("TI103","48.5552613","12.1975051"),
                new RaumData("TI104","48.5551592","12.1974085"),
                new RaumData("TI105","48.5552977","12.1974045"),
                new RaumData("TI106","48.5552099","12.1973429"),
                new RaumData("TI107","48.5553199","12.1973321"),
                new RaumData("TI108","48.5552232","12.1973053"),
                new RaumData("TI109","48.5553519","12.1972235"),
                new RaumData("TI110","48.5552365","12.1972718"),
                new RaumData("TI111","48.5553839","12.1971243"),
                new RaumData("TI112","48.5552525","12.197245"),
                new RaumData("TI113","48.5554238","12.1971538"),
                new RaumData("TI114","48.5552614","12.1972088"),
                new RaumData("TI116","48.5552738","12.1971672"),
                new RaumData("TI117","48.5554451","12.1970546"),
                new RaumData("TI118","48.5553004","12.1971176"),
                new RaumData("TI119","48.5554522","12.1970197"),
                new RaumData("TI120","48.5553004","12.1971176"),
                new RaumData("TI121","48.5554691","12.1969459"),
                new RaumData("TI122","48.5553004","12.1971176"),
                new RaumData("TI123","48.5554922","12.1968829"),
                new RaumData("TI124","48.5553245","12.1969768"),
                new RaumData("TI125","48.5555064","12.196836"),
                new RaumData("TI126","48.5553547","12.196954"),
                new RaumData("TI127","48.5555313","12.1967689"),
                new RaumData("TI128","48.5553769","12.196895"),
                new RaumData("TI130","48.555392","12.1968615"),
                new RaumData("TI132","48.5554008","12.1968401"),
                new RaumData("TI134","48.5554088","12.1968146"),
                new RaumData("TI136","48.555423","12.1967797"),
                new RaumData("TI138","48.5554354","12.1967381"),
                new RaumData("TI140","48.5554532","12.1966912"),
                new RaumData("TI201","48.5551762","12.197465"),
                new RaumData("TI202","48.5552161","12.1973537"),
                new RaumData("TI203","48.5552436","12.1973028"),
                new RaumData("TI204","48.5552569","12.197272"),
                new RaumData("TI205","48.5552667","12.1972425"),
                new RaumData("TI206","48.5552871","12.1971982"),
                new RaumData("TI207","48.5553057","12.1971151"),
                new RaumData("TI208","48.5553057","12.1971151"),
                new RaumData("TI209","48.5553057","12.1971151"),
                new RaumData("TI210","48.5553687","12.1969649"),
                new RaumData("TI211","48.5553785","12.1969273"),
                new RaumData("TI212","48.5553989","12.1968817"),
                new RaumData("TI213","48.5554078","12.1968549"),
                new RaumData("TI214","48.555422","12.1968214"),
                new RaumData("TI215","48.5554317","12.1967879"),
                new RaumData("TI216","48.5554441","12.1967463"),
                new RaumData("TI217","48.5554619","12.1966927"),
                new RaumData("LW047","48.5562043","12.1968181"),
                new RaumData("LW049","48.5567463","12.1972277"),
                new RaumData("SC001","48.5567463","12.1972278"),
                new RaumData("SC002","48.5505791","12.1843076"),
                new RaumData("SC003","48.5505791","12.1843076"),
                new RaumData("SC004","48.5505791","12.1843076"),
                new RaumData("SC005","48.5505791","12.1843076"),

                new RaumData("A 001","48.5555267","12.1988182"),
                new RaumData("A 002","48.5555267","12.1988182"),
                new RaumData("A 003","48.5555267","12.1988182"),
                new RaumData("A 004","48.5555267","12.1988182"),
                new RaumData("A 005","48.5555267","12.1988182"),
                new RaumData("A 006","48.5555267","12.1988182"),
                new RaumData("A 007","48.5555267","12.1988182"),
                new RaumData("A 008","48.5555267","12.1988182"),
                new RaumData("A 009","48.5555267","12.1988182"),
                new RaumData("A 010","48.5555267","12.1988182"),
                new RaumData("A 011","48.5555267","12.1988182"),

                new RaumData("A 101","48.5555267","12.1988182"),
                new RaumData("A 102","48.5555267","12.1988182"),
                new RaumData("A 103","48.5555267","12.1988182"),
                new RaumData("A 104","48.5555267","12.1988182"),
                new RaumData("A 105","48.5555267","12.1988182"),
                new RaumData("A 106","48.5555267","12.1988182"),
                new RaumData("A 107","48.5555267","12.1988182"),
                new RaumData("A 108","48.5555267","12.1988182"),
                new RaumData("A 109","48.5555267","12.1988182"),
                new RaumData("A 110","48.5555267","12.1988182"),
                new RaumData("A 111","48.5555267","12.1988182"),
                new RaumData("A 112","48.5555267","12.1988182"),
                new RaumData("A 113","48.5555267","12.1988182"),
                new RaumData("A 114","48.5555267","12.1988182"),
                new RaumData("A 115","48.5555267","12.1988182"),
                new RaumData("A 116","48.5555267","12.1988182"),
                new RaumData("A 117","48.5555267","12.1988182"),

                new RaumData("A 201","48.5555267","12.1988182"),
                new RaumData("A 202","48.5555267","12.1988182"),
                new RaumData("A 203","48.5555267","12.1988182"),
                new RaumData("A 204","48.5555267","12.1988182"),
                new RaumData("A 205","48.5555267","12.1988182"),
                new RaumData("A 206","48.5555267","12.1988182"),
                new RaumData("A 207","48.5555267","12.1988182"),
                new RaumData("A 208","48.5555267","12.1988182"),
                new RaumData("A 209","48.5555267","12.1988182"),
                new RaumData("A 210","48.5555267","12.1988182"),
                new RaumData("A 211","48.5555267","12.1988182"),
                new RaumData("A 212","48.5555267","12.1988182"),
                new RaumData("A 213","48.5555267","12.1988182"),
                new RaumData("A 214","48.5555267","12.1988182"),
                new RaumData("A 215","48.5555267","12.1988182"),
                new RaumData("A 216","48.5555267","12.1988182"),
                new RaumData("A 217","48.5555267","12.1988182"),
                new RaumData("A 218","48.5555267","12.1988182"),
                new RaumData("A 219","48.5555267","12.1988182"),
                new RaumData("A 220","48.5555267","12.1988182"),
                new RaumData("A 221","48.5555267","12.1988182"),
                new RaumData("A 222","48.5555267","12.1988182"),
                new RaumData("A 223","48.5555267","12.1988182"),
                new RaumData("A 224","48.5555267","12.1988182"),
                new RaumData("A 225","48.5555267","12.1988182"),
                new RaumData("A 226","48.5555267","12.1988182"),
                new RaumData("A 227","48.5555267","12.1988182")
        };

    }
}
