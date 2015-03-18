package cn.fdse.MultiFacetWebService.framework;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import cn.fdse.MultiFacetWebService.framework.util.InitAssistant;
import cn.fdse.codeSearch.openInterface.module.ClassificationList;
import cn.fdse.codeSearch.openInterface.module.ModuleProvider;
import cn.fdse.codeSearch.openInterface.searchInput.UserInput;
import cn.fdse.codeSearch.openInterface.searchResult.CodeResult;
import cn.fdse.codeSearch.openInterface.searchResult.SearchProvider;

public class FrameManager implements Constant{
	
	private List<Class<?>> searchers = null;
	private List<Class<?>> modules = null;
	private Map<String, Object> dataMap = 
			new HashMap<String, Object>();
	
	private static FrameManager singleton = null;
	public static FrameManager getSingleton(){
		
		return singleton;
	}
	
	public static FrameManager getSingleton(String configPath, String fileName){
		if(singleton != null){
		}else{
			singleton = new FrameManager();
			singleton.initModules(configPath, fileName);
		}
		return singleton;
	}
	
	private FrameManager(){
		searchers = new ArrayList<Class<?>>();
		modules = new ArrayList<Class<?>>();
	}
	
	public FrameSession run(UserInput ui){
		FrameSession fi = new FrameSession();
		fi.dataMap = dataMap;
		List<ClassificationList> ret = new ArrayList<ClassificationList>();
		List<CodeResult> resultList = new ArrayList<CodeResult>();
		try {
			for(Class<?> c:searchers){
				Object obj = c.newInstance();
				SearchProvider p = (SearchProvider) obj;
				resultList.addAll(p.getResultOf(ui, dataMap));
			}
			for(Class<?> c:modules){
				Object obj = c.newInstance();
				ModuleProvider p = (ModuleProvider) obj;
				fi.addModuleProvider(p);
				ret.add(p.analysis(resultList, dataMap));
			}
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FrameResult fr = new FrameResult();
		
		fr.facets = ret;
		fr.results = resultList;
		
		fi.setAllResult(resultList);
		fi.fr = fr;
		
		return fi;
	}
	
	public void initModules(String configPath, String fileName){
		//get path in configure file.
		String path = configPath+fileName;
		String modPath = configPath+"mods";
		
		List<String> pathList = new ArrayList<String>();
		pathList.add(modPath);
		
		String paths = InitAssistant.getInstance(path)
				.getValue(systemConfig, modPath, null);

		//
		initModsConfigure(configPath);
		
		if(paths!=null){
			String[] pathArray = paths.split(pathSplit);
			for(String p:pathArray){
				pathList.add(p);
			}
		}
		
		ModsLoader ml = new ModsLoader(pathList, this);
		ml.loadMods();
	}
	private void initModsConfigure(String sysPath) {
		dataMap.put(systemPath, sysPath);
		Map<String, String> properties = InitAssistant
				.getInstance().getValues(propertiesTage);
		for(Iterator<String> iter=properties.keySet().iterator();iter.hasNext();){
			String keyStr = iter.next();
			dataMap.put(keyStr, properties.get(keyStr));
		}
	}

	class ModsLoader {

		private List<String> loadPaths = null;
		private FrameManager fm = null;
		
		public ModsLoader(){
			
		}
		
		public ModsLoader(List<String> paths, FrameManager fm){
			loadPaths = paths;
			this.fm = fm;
		}
		
		public void loadStaticMods(){
//			Class<CodesearchDownloadManager> sc = 
//					CodesearchDownloadManager.class;
//			this.fm.searchers.add(sc);
//			Class<ClusterModule> mc = ClusterModule.class;
//			this.fm.modules.add(mc);
		}
		
		public void loadMods(){
			for(String path:loadPaths){
				
				File dir = new File(path);
				if(!dir.exists() || !dir.isDirectory())
					continue;
				
				// Get all the files in mod folder
			    File[] mods = dir.listFiles();

			    try{
				    for (int i=0; i<mods.length; i++){
				        // Skip if the file is not a jar
				        if (!mods[i].getName().endsWith(".jar"))
				            continue;
				        
				        // Create a JarFile
//				        @SuppressWarnings("resource")
						JarFile jarFile = new JarFile(mods[i]);
	
				        // Get the entries
				        Enumeration<?> e = jarFile.entries();
	
				        // Create a URL for the jar
				        URL[] urls = { new URL("jar:file:" + mods[i].getAbsolutePath() +"!/") };
				        URLClassLoader cl = URLClassLoader.newInstance(
				        		urls, Thread.currentThread().getContextClassLoader());
	
				        while (e.hasMoreElements()){
				            JarEntry je = (JarEntry) e.nextElement();
	
				            // Skip directories
				            if(je.isDirectory() || !je.getName().endsWith(".class")){
				                continue;
				            }
	
				            // -6 because of .class
				            String className = je.getName().substring(0,je.getName().length()-6);
				            className = className.replace('/', '.');
//	System.out.println(className);
				            
				            // Load the class
				            Class<?> c = cl.loadClass(className);
				            
				            check(c);
				        }
				        jarFile.close();
				    }
			    }catch(IOException e){
			    	e.printStackTrace();
			    } catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		private void check(Class<?> c) throws IllegalAccessException {
			
			if(c.isInterface())
				return;
			
			//check whether it is abstractive
			if(Modifier.isAbstract(c.getModifiers())){
				return;
			}
			
			if(SearchProvider.class.isAssignableFrom(c)){
				this.fm.searchers.add(c);
			}
			if(ModuleProvider.class.isAssignableFrom(c)){
				this.fm.modules.add(c);
			}
		}
	}

	public FrameSession run(String keywords) {
		FrameUserInput fui = new FrameUserInput(keywords);
		fui.addProperty("downloadLimit", "l");
		fui.addProperty("accountLimit", "20");
		return run(fui);
	}
	
}
