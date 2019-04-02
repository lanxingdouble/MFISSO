package cn.fdse.filter;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 分词抽象类
 * @author Angela
 */
public abstract class Segment {

    protected Set<String> stopwords;//停用词
    /**
     * 构造函数，初始化各个属性
     */
    public Segment(){
        stopwords=new HashSet<String>();
    }
    /**
     * 构造函数，初始化各个属性，初始化停用词集
     * @param stopwordPath 停用词文件路径
     */
    public Segment(String stopwordPath){
        try {
            BufferedReader stopWordFileBr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(stopwordPath+"stopWord.txt"))));
            String stopWord = null;
            for(;(stopWord = stopWordFileBr.readLine()) != null;)
            {
                stopwords.add(stopWord);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对字符串内容进行分词
     * @param content 内容
     * @return 由空格符作为分隔符的分词结果String
     */
    public abstract String segment(String content);

    /**
     * @return the stopwords
     */
    public Set<String> getStopwords() {
        return stopwords;
    }

    /**
     * @param stopwords the stopwords to set
     */
    public void setStopwords(Set<String> stopwords) {
        this.stopwords = stopwords;
    }
}
