package News;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Convert {
	/** 後ろに偶数個の「"」が現れる「,」にマッチする正規表現 */
    static final String REGEX_CSV_COMMA = ",(?=(([^\"]*\"){2})*[^\"]*$)";

    /** 最初と最後の「"」にマッチする正規表現*/
    static final String REGEX_SURROUND_DOUBLEQUATATION = "^\"|\"$";

    /** 「""」にマッチする正規表現 */
    static final String REGEX_DOUBLEQUOATATION = "\"\"";

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
//    public static void main(String args[]) throws Throwable{
//    	initializeMap();
//    	System.out.println(pickUpChar("秋"));
//    }
}