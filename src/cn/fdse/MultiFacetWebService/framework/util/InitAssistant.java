package cn.fdse.MultiFacetWebService.framework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class InitAssistant {
	private static final String FileName = "config/config.ini";
	private Map<String, Map<String, String>> map = new 
			HashMap<String, Map<String, String>>();

	private String fileName = null;
	
	private static InitAssistant singleton = null;

	public static InitAssistant getInstance() {
		if (singleton == null) {
			try {
				singleton = new InitAssistant(FileName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return singleton;
	}
	
	public static InitAssistant getInstance(String path) {
		if (singleton == null) {
			try {
				singleton = new InitAssistant(path);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return singleton;
	}

	private InitAssistant(String FileNameSend) throws Exception {
		fileName = FileNameSend;

		initFileContent();

	}

	private void initFileContent() throws Exception {
		File f = new File(fileName);
		if (!f.exists())
			throw new Exception("Can't find initial file: " + fileName);
		map.clear();
		
		FileReader FReadFile = null;
		BufferedReader BReadFile = null;
		try {

			String strFile;
			
			FReadFile = new FileReader(f);
			BReadFile = new BufferedReader(FReadFile);
			while (true) {
				strFile = BReadFile.readLine();
				if (strFile == null)
					break;
				add(strFile);
			}
			
			BReadFile.close();
			FReadFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Map<String, String> curMap = null;
	public void add(String strFile) {
		String str = strFile.trim();
		int n = str.length();
		if(str.charAt(0)=='[' && str.charAt(n-1)==']'){
			String title = str.substring(1, n-1);
			curMap = new HashMap<String, String>();
			map.put(title, curMap);
		}else{
			int split = str.indexOf("=");
			//configure string must be like "xxx=xxxx"
			if(split<1) return;
			curMap.put(str.substring(0, split), str.substring(split+1, n));
		}
	}

	//
	public String getValue(String SectionName_, String IdentName_,
			String DefaultValue_) {
		Map<String, String> tmpMap = map.get(SectionName_);
		if(tmpMap==null) return DefaultValue_;
		String ret = tmpMap.get(IdentName_);
		if(ret == null) ret = DefaultValue_;
		return ret;
	}
	
	public Map<String, String> getValues(String SectionName){
		return map.get(SectionName);
	}
	
}