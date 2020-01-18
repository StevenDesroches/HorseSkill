package com.github.StevenDesroches.HorseSkill.util;

import java.io.*;

public class FileWriterUtils {

    private static void createFile(File file) throws IOException {
        if (!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }

    public static void saveFile(File file, String text){
        final FileWriter fw;

        try {

            createFile(file);
            fw = new FileWriter(file);
            fw.write(text);
            fw.flush();
            fw.close();

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static String loadFile(File file){
        String returnVariable = "";
        if(file.exists()) {
            try {

                final BufferedReader reader = new BufferedReader(new FileReader(file));
                final StringBuilder stringBuilder = new StringBuilder();

                String line;

                while((line = reader.readLine()) != null){
                    stringBuilder.append(line);
                }
                reader.close();

                returnVariable = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return returnVariable;
    }
}
