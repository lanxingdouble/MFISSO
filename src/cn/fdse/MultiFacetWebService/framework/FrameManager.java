//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.fdse.MultiFacetWebService.framework;

import cn.fdse.MultiFacetWebService.framework.util.InitAssistant;
import cn.fdse.StackOverflow.searchModule.util.Global;
import cn.fdse.codeSearch.openInterface.module.ClassificationList;
import cn.fdse.codeSearch.openInterface.module.ModuleProvider;
import cn.fdse.codeSearch.openInterface.searchInput.UserInput;
import cn.fdse.codeSearch.openInterface.searchResult.SearchProvider;
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

public class FrameManager implements Constant {
	private List<Class<?>> searchers = null;
	private List<Class<?>> modules = null;
	private Map<String, Object> dataMap = new HashMap();
	private static FrameManager singleton = null;

	public static FrameManager getSingleton() {
		return singleton;
	}

	public static FrameManager getSingleton(String configPath, String fileName) {
		if (singleton == null) {
			singleton = new FrameManager();
			singleton.initModules(configPath, fileName);
		}

		return singleton;
	}

	private FrameManager() {
		this.searchers = new ArrayList();
		this.modules = new ArrayList();
	}

	public FrameSession run(UserInput ui) {
		FrameSession fi = new FrameSession();
		fi.dataMap = this.dataMap;
		List<ClassificationList> ret = new ArrayList();
		ArrayList resultList = new ArrayList();

		try {
			Iterator var6 = this.searchers.iterator();
			while (var6.hasNext()) {
				Class<?> c = (Class) var6.next();
				Object obj = c.newInstance();
				SearchProvider p = (SearchProvider) obj;
				resultList.addAll(p.getResultOf(ui, this.dataMap));
			}

			int id = 0;
			Global.idFacetName.clear();
			Iterator var16 = this.modules.iterator();
			System.out.println("modules: "+modules);
			while (var16.hasNext()) {
				Class<?> c = (Class) var16.next();
				Object obj = c.newInstance();
				ModuleProvider p = (ModuleProvider) obj;
				fi.addModuleProvider(p);
                System.out.println("**********************************9");
				ClassificationList classification = p.analysis(resultList, this.dataMap);
				System.out.println("Facet:" + id+" "+classification.getTitle());
				Global.idFacetName.put("Facet:" + id, classification.getTitle());
				++id;
				ret.add(classification);
			}
			System.out.println("4444444444444444");
			System.out.println(ret);
		} catch (InstantiationException var11) {
			var11.printStackTrace();
		} catch (IllegalAccessException var12) {
			var12.printStackTrace();
		}

		FrameResult fr = new FrameResult();
		fr.facets = ret;
		fr.results = resultList;
		fi.setAllResult(resultList);
		fi.fr = fr;
		return fi;
	}

	public void initModules(String configPath, String fileName) {
		String path = configPath + fileName;
		String modPath = configPath + "mods";
		List<String> pathList = new ArrayList();
		pathList.add(modPath);
		String paths = InitAssistant.getInstance(path).getValue("system", modPath, (String) null);
		this.initModsConfigure(configPath);
		if (paths != null) {
			String[] pathArray = paths.split(";");
			String[] var11 = pathArray;
			int var10 = pathArray.length;

			for (int var9 = 0; var9 < var10; ++var9) {
				String p = var11[var9];
				pathList.add(p);
			}
		}

		FrameManager.ModsLoader ml = new FrameManager.ModsLoader(pathList, this);
		ml.loadMods();
	}

	private void initModsConfigure(String sysPath) {
		this.dataMap.put("frame.workDir", sysPath);
		Map<String, String> properties = InitAssistant.getInstance().getValues("mod_properties");
		Iterator iter = properties.keySet().iterator();

		while (iter.hasNext()) {
			String keyStr = (String) iter.next();
			this.dataMap.put(keyStr, properties.get(keyStr));
		}

	}

	public FrameSession run(String keywords) {
		FrameUserInput fui = new FrameUserInput(keywords);
		fui.addProperty("downloadLimit", "l");
		fui.addProperty("accountLimit", "20");
		return this.run((UserInput) fui);
	}

	class ModsLoader {
		private List<String> loadPaths = null;
		private FrameManager fm = null;

		public ModsLoader() {
		}

		public ModsLoader(List<String> paths, FrameManager fm) {
			this.loadPaths = paths;
			this.fm = fm;
		}

		public void loadStaticMods() {
		}

		public void loadMods() {
			Iterator var2 = this.loadPaths.iterator();

			while (true) {
				File dir;
				do {
					do {
						if (!var2.hasNext()) {
							return;
						}

						String path = (String) var2.next();
						dir = new File(path);
					} while (!dir.exists());
				} while (!dir.isDirectory());

				File[] mods = dir.listFiles();

				try {
					for (int i = 0; i < mods.length; ++i) {
						if (mods[i].getName().endsWith(".jar")) {
							JarFile jarFile = new JarFile(mods[i]);
							Enumeration<?> e = jarFile.entries();
							URL[] urls = new URL[]{new URL("jar:file:" + mods[i].getAbsolutePath() + "!/")};
							URLClassLoader cl = URLClassLoader.newInstance(urls, Thread.currentThread().getContextClassLoader());

							while (e.hasMoreElements()) {
								JarEntry je = (JarEntry) e.nextElement();
								if (!je.isDirectory() && je.getName().endsWith(".class")) {
									String className = je.getName().substring(0, je.getName().length() - 6);
									className = className.replace('/', '.');
									Class<?> c = cl.loadClass(className);
									this.check(c);
								}
							}

							jarFile.close();
						}
					}
				} catch (IOException var13) {
					var13.printStackTrace();
				} catch (ClassNotFoundException var14) {
					var14.printStackTrace();
				} catch (IllegalAccessException var15) {
					var15.printStackTrace();
				}
			}
		}

		private void check(Class<?> c) throws IllegalAccessException {
			if (!c.isInterface()) {
				if (!Modifier.isAbstract(c.getModifiers())) {
					if (SearchProvider.class.isAssignableFrom(c)) {
						this.fm.searchers.add(c);
					}

					if (ModuleProvider.class.isAssignableFrom(c)) {
						this.fm.modules.add(c);
					}

				}
			}
		}
	}
}