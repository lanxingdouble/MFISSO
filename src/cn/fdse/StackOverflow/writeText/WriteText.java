package cn.fdse.StackOverflow.writeText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import cn.fdse.StackOverflow.searchModule.util.Global;

public class WriteText {
	
	public void writeText(String s)
	{
		try{
			System.out.println(Global.syspath);
			BufferedWriter bw = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (Global.syspath+"\\experiment.txt", true), "UTF-8"));
			bw.write (s);
			bw.close();
		}catch(Exception e){

      }
//		readTxtFile(Global.syspath);
		
	}
	
	public void readTxtFile(String filePath){
        try {
                String encoding="UTF-8";
                File file=new File(filePath+"\\experiment.txt");
                if(file.isFile() && file.exists()){ //判断文件是否存在
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine()) != null){
                        System.out.println("jqt:"+lineTxt);
                        //Keywords SelectFacet FacetItem Click Copy Suggestion
                    }
                    read.close();
        }else{
            System.out.println("找不到指定的文件");
        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
	}
   public static void main(String args[])
   {
	   WriteText wt = new WriteText();
	   
   }
}
