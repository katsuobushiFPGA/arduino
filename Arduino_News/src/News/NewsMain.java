package News;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class NewsMain {
	/** 後ろに偶数個の「"」が現れる「,」にマッチする正規表現 */
    static final String REGEX_CSV_COMMA = ",(?=(([^\"]*\"){2})*[^\"]*$)";

    /** 最初と最後の「"」にマッチする正規表現*/
    static final String REGEX_SURROUND_DOUBLEQUATATION = "^\"|\"$";

    /** 「""」にマッチする正規表現 */
    static final String REGEX_DOUBLEQUOATATION = "\"\"";

	public static void main(String[] args) throws Throwable {
		StringBuilder sm = new StringBuilder();
		initializeMap();//csvをmapに入れる

		URL url = new URL("http://feeds.reuters.com/reuters/JPBusinessNews?format=xml");
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.connect();

		Document doc = null;
		try {
			doc = getDocument(urlConn.getInputStream());
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		//ファイルパス
		FileWriter f=new FileWriter("C:\\test\\test2.ino");

		// ルートの要素名になっている子ノードを取得する
		Element root = doc.getDocumentElement();

		// 各ノードリストを取得する。
		NodeList nodeList = root.getElementsByTagName("channel");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element)nodeList.item(i);
			sm.append(getChildren(element, "title")+"\r\n");
			// 各ノードリストを取得
			NodeList list = element.getElementsByTagName("item");

			for (int j = 0; j< list.getLength(); j++) {
				Element element2 = (Element)list.item(j);
				sm.append(getChildren(element2, "title")+"\r\n");
			}
		}

		//ここで、ヘッドライン取得完了 StringBuilderのsmにはいってる。
		sm = convertToEm(sm.toString());

		System.out.print(sm.toString());

		//配列にヘッドライン格納
		String[] sl = oneLineSplitter(sm.toString());


		String userInput="";

//		ユーザー文字入力
//		System.out.println(connectionOutput(userInput, userInput.length()));
		int newsArg=Integer.parseInt(args[0]);

		//ヘッドライン用のプログラム出力
		System.out.println(connectionOutput(sl[6], sl[6].length()));
		f.write(connectionOutput(sl[newsArg],sl[newsArg].length()));



		urlConn.disconnect();
		f.close();
	}

	private static Document getDocument(InputStream in) throws SAXException,
	IOException, ParserConfigurationException {
		DocumentBuilder docbuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		return docbuilder.parse(in);
	}

	public static String getChildren(Element element, String tagName) {
		NodeList list = element.getElementsByTagName(tagName);
		Element cElement = (Element)list.item(0);
		return cElement.getFirstChild().getNodeValue();
	}
	public static String[] oneLineSplitter(String s){
			String[] b = s.split("\r\n");
		return b;
	}

	//半角を全角に変換するメソッド
	public static StringBuilder convertToEm(String s){
	StringBuilder sb = new StringBuilder();
		for(int i=0;i<s.length();i++){
			sb.append(toBigAsciiOne(s.charAt(i)));
		}
			return sb;
	}



	public static String arduinoHeadder(){
		StringBuilder sb = new StringBuilder();
		sb.append("#include <Adafruit_GFX.h>" + "\r\n");
		sb.append("#include <RGBmatrixPanel.h>"+"\r\n");
		sb.append("#define CLK 8" + "\r\n");
		sb.append("#define LAT A3"+"\r\n");
		sb.append("#define OE  9"+"\r\n");
		sb.append("#define A   A0"+"\r\n");
		sb.append("#define B   A1"+"\r\n");
		sb.append("#define C   A2"+"\r\n");
		sb.append("#define F2(progmem_ptr) (const __FlashStringHelper *)progmem_ptr" + "\r\n");
		sb.append("RGBmatrixPanel matrix(A, B, C, CLK, LAT, OE, false);" + "\r\n");
		return sb.toString();
	}
	public static String arduinoFormatter(String target) throws Throwable{
		Integer[] chara = new Integer[target.length()];
		for(int i=0;i<target.length();i++){
			chara[i]=i;
		}

		StringBuilder tmp=new StringBuilder();
		for(int i=0;i<target.length();i++){
			tmp.append("static unsigned char __attribute__ ((progmem)) ");
			tmp.append("chara"+ chara[i].toString() + "[]=");
			tmp.append(pickUpChar(String.valueOf(target.charAt(i))));
			tmp.append(";");
			tmp.append("\r\n");
		}
		return tmp.toString();
	}

	public static String arduinoSetVariable(){
		StringBuilder sb = new StringBuilder();
		sb.append(""+"\r\n");
		return sb.toString();
	}
	public static String arduinoSetUp(){
		StringBuilder sb = new StringBuilder();
		sb.append("void setup() {" + "\r\n");
		sb.append("matrix.begin();" +"\r\n");
		sb.append("matrix.setTextWrap(false);"+"\r\n");
		sb.append("}"+"\r\n");
		return sb.toString();
	}
	public static String arduinoLoop(int mojisu){
		StringBuilder sb=new StringBuilder();
		Integer[] chara= new Integer[mojisu];
		for(int i=0;i<chara.length;i++){
			chara[i]=i;
		}

		sb.append("void loop(){");
		sb.append("for(int i=64 ;i > " + (-16)*mojisu + ";i--){"+"\r\n");
		for(int i =0;i<mojisu;i++){
			sb.append("if(i >= " + -16*(i+1) + ")"+"\r\n");
			sb.append("matrix.drawBitmap(i + " + 16*i + " ,    0, chara"+chara[i].toString()+", 16, 16, matrix.Color333(0,1,1));"+"\r\n");
		}
		sb.append("delay(50);"+"\r\n");
		sb.append("matrix.fillScreen(0);"+"\r\n");
		sb.append("}"+"\r\n"+"}"+"\r\n");
		return sb.toString();
	}
	//結合出力用
	public static String connectionOutput(String target,int target_l) throws Throwable{
		StringBuilder sb=new StringBuilder();
		sb.append(arduinoHeadder());
		sb.append(arduinoFormatter(target));
		sb.append(arduinoSetVariable());
		sb.append(arduinoSetUp());
		sb.append(arduinoLoop(target_l));

		return sb.toString();
	}

    private static String[] splitLineWithComma(String line) {
        // 分割後の文字列配列
        String[] arr = null;

        try {
            // １、「"」で囲まれていない「,」で行を分割する。
            Pattern cPattern = Pattern.compile(REGEX_CSV_COMMA);
            String[] cols = cPattern.split(line, -1);

            arr = new String[cols.length];
            for (int i = 0, len = cols.length; i < len; i++) {
                String col = cols[i].trim();

                // ２、最初と最後に「"」があれば削除する。
                Pattern sdqPattern =
                    Pattern.compile(REGEX_SURROUND_DOUBLEQUATATION);
                Matcher matcher = sdqPattern.matcher(col);
                col = matcher.replaceAll("");

                // ３、エスケープされた「"」を戻す。
                Pattern dqPattern =
                    Pattern.compile(REGEX_DOUBLEQUOATATION);
                matcher = dqPattern.matcher(col);
                col = matcher.replaceAll("\"");

                arr[i] = col;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arr;
    }

    /**
     * 英語のみ、半角を全角に変換.
     */
	public static HashMap<String,String> hm = new HashMap<>();

    public static String toBigAsciiOne( char code ) {
        switch( code ){
            case '!'   : return  "！" ;
            case '\"'  : return  "”" ;
            case '#'   : return  "＃" ;
            case '$'   : return  "＄" ;
            case '\\'  : return  "￥" ;
            case '%'   : return  "％" ;
            case '&'   : return  "＆" ;
            case '\''  : return  "’" ;
            case '('   : return  "（" ;
            case ')'   : return  "）" ;
            case '*'   : return  "＊" ;
            case '+'   : return  "＋" ;
            case ','   : return  "，" ;
            case '-'   : return  "―" ;
            case '―'  : return "ー";
            case '.'   : return  "．" ;
            case '/'   : return  "／" ;
            case '０'   : return  "0" ;
            case '１'   : return  "1" ;
            case '２'   : return  "2" ;
            case '３'   : return  "3" ;
            case '４'   : return  "4" ;
            case '５'   : return  "5" ;
            case '６'   : return  "6" ;
            case '７'   : return  "7" ;
            case '８'   : return  "8" ;
            case '９'   : return  "9" ;
            case ':'   : return  "：" ;
            case ';'   : return  "；" ;
            case '<'   : return  "＜" ;
            case '='   : return  "＝" ;
            case '>'   : return  "＞" ;
            case '?'   : return  "？" ;
            case '@'   : return  "＠" ;
            case 'A'   : return  "Ａ" ;
            case 'B'   : return  "Ｂ" ;
            case 'C'   : return  "Ｃ" ;
            case 'D'   : return  "Ｄ" ;
            case 'E'   : return  "Ｅ" ;
            case 'F'   : return  "Ｆ" ;
            case 'G'   : return  "Ｇ" ;
            case 'H'   : return  "Ｈ" ;
            case 'I'   : return  "Ｉ" ;
            case 'J'   : return  "Ｊ" ;
            case 'K'   : return  "Ｋ" ;
            case 'L'   : return  "Ｌ" ;
            case 'M'   : return  "Ｍ" ;
            case 'N'   : return  "Ｎ" ;
            case 'O'   : return  "Ｏ" ;
            case 'P'   : return  "Ｐ" ;
            case 'Q'   : return  "Ｑ" ;
            case 'R'   : return  "Ｒ" ;
            case 'S'   : return  "Ｓ" ;
            case 'T'   : return  "Ｔ" ;
            case 'U'   : return  "Ｕ" ;
            case 'V'   : return  "Ｖ" ;
            case 'W'   : return  "Ｗ" ;
            case 'X'   : return  "Ｘ" ;
            case 'Y'   : return  "Ｙ" ;
            case 'Z'   : return  "Ｚ" ;
            case '^'   : return  "＾" ;
            case '_'   : return  "＿" ;
            case '`'   : return  "‘" ;
            case 'a'   : return  "ａ" ;
            case 'b'   : return  "ｂ" ;
            case 'c'   : return  "ｃ" ;
            case 'd'   : return  "ｄ" ;
            case 'e'   : return  "ｅ" ;
            case 'f'   : return  "ｆ" ;
            case 'g'   : return  "ｇ" ;
            case 'h'   : return  "ｈ" ;
            case 'i'   : return  "ｉ" ;
            case 'j'   : return  "ｊ" ;
            case 'k'   : return  "ｋ" ;
            case 'l'   : return  "ｌ" ;
            case 'm'   : return  "ｍ" ;
            case 'n'   : return  "ｎ" ;
            case 'o'   : return  "ｏ" ;
            case 'p'   : return  "ｐ" ;
            case 'q'   : return  "ｑ" ;
            case 'r'   : return  "ｒ" ;
            case 's'   : return  "ｓ" ;
            case 't'   : return  "ｔ" ;
            case 'u'   : return  "ｕ" ;
            case 'v'   : return  "ｖ" ;
            case 'w'   : return  "ｗ" ;
            case 'x'   : return  "ｘ" ;
            case 'y'   : return  "ｙ" ;
            case 'z'   : return  "ｚ" ;
            case '{'   : return  "｛" ;
            case '|'   : return  "｜" ;
            case '}'   : return  "｝" ;
            case '｡'   : return  "。" ;
            case '｢'   : return  "「" ;
            case '｣'   : return  "」" ;
            case '､'   : return  "、" ;
            case '･'   : return  "・" ;
            case ' '   : return  "　" ;
        }
        return new String( new char[]{ code } ) ;
    }

    private static void readCSV() throws Throwable{
    	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("FONT.csv"),"SJIS"));
        String s;
        // ファイルを行単位で読む
        while( (s = br.readLine()) != null ) {
            // 正規表現で分割する
            String[] array = splitLineWithComma(s);
            hm.put(array[0],array[1]);
        }
        br.close();
    }
    public static void initializeMap() throws Throwable{
    	readCSV();
    }
    //外部用
    public static String pickUpChar(String key) throws Throwable{
    	return hm.get(key);
    }
}