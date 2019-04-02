package cn.fdse.NewInputHandle;

import cn.fdse.StackOverflow.searchModule.util.Global;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class InputIDF {
//    Connection conn = (Connection) DB_Helper.getConnection();
//    //获取idf
//    public double getIDF(String keyword) throws SQLException {
//        String sql = "select idf from idf where text=?";
//        PreparedStatement psql = conn.prepareStatement(sql);
//        psql.setString(1, keyword);
//        ResultSet rs = psql.executeQuery();
//        while (rs.next()) {
//            double idf = rs.getDouble(1);
//            return idf;
//        }
//        rs.close();
//        return 1;
//    }

    //获取idf
    Integer maxDoc;
    IndexReader indexreader;
    IndexSearcher indexSearcher;

    public double getIDF(String keyword) {
        try {
            Integer docfre=indexreader.docFreq(new Term("Title", keyword));
            if(docfre==0){
                docfre=indexreader.docFreq(new Term("Body", keyword));
            }
            if (docfre==0){
                return 5.0;
            }
            return Math.log10(maxDoc * 1.0 / docfre);
        } catch (IOException e) {
            e.printStackTrace();
            return 5.0;
        }
    }

    //在所有title中没有出现过的词，其IDF=5,
    public String handleInput(String s, Integer limit) throws IOException {
        Directory directroy = null;
        directroy = FSDirectory.open(new File(Global.facet_index_path));
        indexreader = DirectoryReader.open(directroy);
        indexSearcher = new IndexSearcher(indexreader);
        maxDoc = indexreader.maxDoc();
        String[] keywords = s.split(",");
        if (keywords.length == 0) {
            keywords = s.split(" ");
        }
        List<KeyWords> listA = new ArrayList<KeyWords>();
        double idf = 0;
        Map<String, Integer> tfmap = new HashMap<String, Integer>();
        List<String> keywords_unique = new ArrayList<String>();
        for (int i = 0; i < keywords.length; i++) {
            if (tfmap.get(keywords[i]) == null) {
                tfmap.put(keywords[i], 1);
                keywords_unique.add(keywords[i]);
            } else {
                int frequency = tfmap.get(keywords[i]);
                tfmap.put(keywords[i], ++frequency);
            }
        }
//        //计算每个词的tf-idf
//        for (String keyword : keywords_unique) {
//            try {
//                idf = getIDF(keyword);
//                listA.add(new KeyWords(keyword, idf * tfmap.get(keyword)));
//            } catch (SQLException e) {
//                System.out.printf("get idf of %s failed!", keyword);
//                e.printStackTrace();
//            }
//        }

        //计算每个词的tf-idf
        for (String keyword : keywords_unique) {
            idf = getIDF(keyword);
            listA.add(new KeyWords(keyword, idf * tfmap.get(keyword)));
        }

        //按照tf-idf值对词进行排序
        Collections.sort(listA);
        for (KeyWords p : listA) {
            System.out.println(p.getKeyword() + ": " + p.getValue());
        }
        String re;
        List<String> re_keyword = new ArrayList<String>();
        if (limit > listA.size()) {
            limit = listA.size();
        }
        for (int i = 0; i < limit; i++) {
            re_keyword.add(listA.get(i).getKeyword());
        }
        re = String.join(",", re_keyword);
        System.out.println(re);
        return re;
    }


    public static void main(String[] args) throws SQLException, IOException {
        InputIDF inputidf = new InputIDF();
//        System.out.println(inputidf.getIDF("browserdetail"));
//        System.out.println(inputidf.getIDF("java"));
        inputidf.handleInput("how,to,make,textview,bold,adsxe", 2);
    }


    public class KeyWords implements Comparable<KeyWords> {
        private String keyword;
        private double value;

        public KeyWords(String keyword, double value) {
            this.keyword = keyword;
            this.value = value;
        }

        /**
         * @return the keyword
         */
        public String getKeyword() {
            return keyword;
        }

        /**
         * @param keyword the keyword to set
         */
        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        /**
         * @return the value
         */
        public Double getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(double value) {
            this.value = value;
        }

        @Override
        public int compareTo(KeyWords arg0) {
            return arg0.getValue().compareTo(this.getValue());      //这里定义你排序的规则。
        }

    }
}


