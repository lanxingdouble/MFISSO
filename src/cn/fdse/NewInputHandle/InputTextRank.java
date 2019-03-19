package cn.fdse.NewInputHandle;
import cn.fdse.StackOverflow.searchModule.util.Global;
import cn.fdse.codeSearch.openInterface.searchInput.UserInput;
import cn.fdse.filter.FilterStopWord;
import com.hankcs.hanlp.HanLP;

import java.io.File;
import java.util.List;

public class InputTextRank implements UserInput {


    @Override
    public String getKeyWords() {
        return null;
    }

    @Override
    public String getProperties(String p) {
        return null;
    }

    public String handleInput(String s){
        List<String> sentenceList = HanLP.extractKeyword(s, 10);
        String a=String.join(",", sentenceList);
        System.out.println(a);
        return a;
    }

    public static void main(String args[])
    {
        InputTextRank it=new InputTextRank();
        String keywords="Python Selenium Edge Driver.I created a script in Python, which scraps the Altium's website and gathers information regarding license usage. At this moment, I am using ChromeDriver, but I sometimes get errors due to the network being slow at different times of the day. I used the same script using the MicrosoftWebDriver (Edge) on my Personal Computer and I received no errors. When you launch the MicrosoftWebDriver.exe (downloaded from their website) it should open Edge, but when I use my company's laptop, nothing happens (see attached picture).\n" +
                "\n" + "Is there any chance I can fix this? Is this happening as a result of the port being blocked?\n" +
                "\n" + "This is the code I am using for selecting the webdriver:\n" +
                "\n" + "browser = webdriver.Edge(r'C:\\ALTIUM_WORK\\Altium_Python\\MicrosoftWebDriver.exe')\n" +
                "And this is the \"error\" I get:";
        keywords.replace("\n"," ");
        System.out.println(keywords);
        FilterStopWord fsw = new FilterStopWord(Global.root_path);
        String inut_without_stop_words = fsw.getStringWithoutStopWord(keywords);
        System.out.println("------------------------");
        System.out.println(inut_without_stop_words);
        System.out.println("------------------------");
        it.handleInput("算法可大致分为基本算法、数据结构的算法、数论算法、计算几何的算法、图的算法、动态规划以及数值分析、加密算法、排序算法、检索算法、随机化算法、并行算法、厄米变形模型、随机森林算法。");
        it.handleInput("Scroll to position of a recyclerview in android activity.");
        it.handleInput(keywords);
        String s=it.handleInput(inut_without_stop_words);
    }
}
