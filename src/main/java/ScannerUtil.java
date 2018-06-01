import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by İlker ÇATAK on 31.05.2018.
 */
public class ScannerUtil {

    public static  void cleanLineWhichContains(String containingWord, String rootFolderPath) {
        File rootFolder= new File(rootFolderPath);
        File[] files = rootFolder.listFiles();
        File tempFile = new File(rootFolderPath + ".tmp");
        for (File f:files) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

                List<String> lines = Files.readAllLines(Paths.get(f.getPath()), Charset.defaultCharset());
                StringBuilder sb= new StringBuilder();
                for (int i = 0; i < lines.size(); i++) {
                    if (!lines.get(i).contains(containingWord)) {
                        pw.println(lines.get(i));
                        pw.flush();
                    }
                }
                pw.close();
                br.close();
                f.delete();
                tempFile.renameTo(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch ( IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     *  Tüm klasör yanlışlıkla CopyLocationsEntityName klasörü oluşturmuştu. Bunları silmek için root directory ve hangi kelimeyle başladığını alıp
     * bunları silen bir metot
     * @param rootFolderPath
     * @param startsWith
     */
    public static  void deleteAllFoldersInGivenPathStartsWith(String rootFolderPath, String startsWith) {
        File rootFolder= new File(rootFolderPath);
        File[] files = rootFolder.listFiles();
        for (File f:files) {
            if(f.getName().startsWith(startsWith)){
                try {
                    FileUtils.deleteDirectory(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
