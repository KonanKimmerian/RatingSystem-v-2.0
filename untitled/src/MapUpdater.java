import java.io.*;
import java.util.HashMap;

public class MapUpdater {
    public static void upload(HashMap<Integer, String> hm) throws IOException {
        File file = new File("map.txt");
        boolean newFile = file.createNewFile();
        System.out.println(newFile);
        FileWriter fw = new FileWriter(file);
        for (Integer i:hm.keySet()) {
            fw.append(String.valueOf(i)).append(" ").append(hm.get(i));
            fw.append("\n");
        }
        fw.flush();
        fw.close();
    }

    public static HashMap<Integer,String> download() throws IOException {
        HashMap<Integer,String>hm=null;
        FileReader fr = new FileReader("map.txt");
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        while(line != null){
            if (hm == null) hm = new HashMap<>();

            String[] keyNWord = line.split(" ");
            hm.put(strtoint(keyNWord[0]),keyNWord[1]);
            line = reader.readLine();
        }
        return hm;
    }

    public static boolean isdownloaded() throws IOException {
        HashMap<Integer,String> hm = download();
        return hm != null;
    }

    private static int strtoint(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
