package cn.fdse.filter;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * �ִʳ�����
 * @author Angela
 */
public abstract class Segment {

    protected Set<String> stopwords;//ͣ�ô�
    /**
     * ���캯������ʼ����������
     */
    public Segment(){
        stopwords=new HashSet<String>();
    }
    /**
     * ���캯������ʼ���������ԣ���ʼ��ͣ�ôʼ�
     * @param stopwordPath ͣ�ô��ļ�·��
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
     * ���ַ������ݽ��зִ�
     * @param content ����
     * @return �ɿո����Ϊ�ָ����ķִʽ��String
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
