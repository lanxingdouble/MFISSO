package cn.fdse.StackOverflow.jsonOperation;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.stream.Collectors;

public class jsonOperation {
    public static JsonObject readJson(String path) {
        //File file = new File(path + "synonyms.json");
        File file = new File(path);
        String cont = null;
        JsonParser jsonParser = new JsonParser();
        try {
            if (file.exists()) {
                cont = new BufferedReader(new FileReader(file)).lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cont == null || cont.equals("")) {
            cont = "{}";
        }
        return jsonParser.parse(cont).getAsJsonObject();
    }

}
