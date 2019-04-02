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
        //it.handleInput("�㷨�ɴ��·�Ϊ�����㷨�����ݽṹ���㷨�������㷨�����㼸�ε��㷨��ͼ���㷨����̬�滮�Լ���ֵ�����������㷨�������㷨�������㷨��������㷨�������㷨�����ױ���ģ�͡����ɭ���㷨��");
        it.handleInput("Scroll to position of a recyclerview in android activity.");
        it.handleInput(keywords);
        String s = it.handleInput(inut_without_stop_words);
    }
}
