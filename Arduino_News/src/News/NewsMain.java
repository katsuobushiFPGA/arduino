package News;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class NewsMain {


	public static void main(String[] args) throws Throwable {
		StringBuilder sm = new StringBuilder();
		News.Convert.initializeMap();//csvをmapに入れる

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

		//ヘッドライン用のプログラム出力
		System.out.println(connectionOutput(sl[6], sl[6].length()));
		f.write(connectionOutput(sl[8],sl[8].length()));



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
			sb.append(News.Convert.toBigAsciiOne(s.charAt(i)));
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
			tmp.append(News.Convert.pickUpChar(String.valueOf(target.charAt(i))));
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
}