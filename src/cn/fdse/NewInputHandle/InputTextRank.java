package cn.fdse.NewInputHandle;

import cn.fdse.StackOverflow.searchModule.util.Global;
import cn.fdse.codeSearch.openInterface.searchInput.UserInput;
import cn.fdse.filter.FilterStopWord;
import com.hankcs.hanlp.HanLP;

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

    public String handleInput(String s) {
        String a = null;
        try {
            List<String> sentenceList = HanLP.extractKeyword(s, 20);
            a = String.join(",", sentenceList);
        } catch (Exception var6) {
            var6.printStackTrace();
        }
        if (a.length() == 0) {
            return s;
        }
        return a;
    }

    public static void main(String args[]) {
        InputTextRank it = new InputTextRank();
        String keywords = "I'm pretty new to android dev. I'm quite confused on how to use the Zxing PDF417. Is there any reference/source code you can provide for PDF417 generator and scanner? Thank you";
        keywords.replace("\n", " ");
        System.out.println(keywords);
        FilterStopWord fsw = new FilterStopWord(Global.root_path);
        String inut_without_stop_words = fsw.getStringWithoutStopWord(keywords);
        System.out.println("------------------------");
        System.out.println(inut_without_stop_words);
        System.out.println("------------------------");
        //it.handleInput("算法可大致分为基本算法、数据结构的算法、数论算法、计算几何的算法、图的算法、动态规划以及数值分析、加密算法、排序算法、检索算法、随机化算法、并行算法、厄米变形模型、随机森林算法。");
        it.handleInput("Scroll to position of a recyclerview in android activity.");
        it.handleInput(keywords);
        String s = it.handleInput(inut_without_stop_words);
    }
}
