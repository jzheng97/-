import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.shuffle;

public class TextStorage {
    public List<String> texts;
    public int numberOfRows;
    List<Integer> indexList;
    public int nextIndex = 0;
    //options
    boolean isLooping;
    boolean isRandom;

    public TextStorage(String lines, boolean isLooping, boolean isRandom) {
        String[] allLines = lines.split("\n");
        this.texts = new ArrayList<>();
        texts.addAll(Arrays.asList(allLines));
        if (texts.size() <= 1) {
            loadDefaultText();
        }
        this.numberOfRows = texts.size();
        this.isLooping = isLooping;
        this.isRandom = isRandom;
        indexList = new ArrayList<>();
        for (int i = 0; i < numberOfRows; i ++) {
            indexList.add(i);
        }
        if (isRandom) {
            shuffle(indexList);
        }
    }

    public String getNextLine(){
        if (isRandom && nextIndex >= texts.size() && isLooping) {
            shuffle(indexList);
            nextIndex = 0;
        }
        return isLooping ? texts.get(indexList.get(nextIndex ++ % numberOfRows)) :
                nextIndex < texts.size() ? texts.get(indexList.get(nextIndex ++)) : "";
    }

    public String getFirstLine(){
        return texts.size() >= 1 ? texts.get(indexList.get(0)) : "";
    }

    public void loadDefaultText() {
        String path = new File("/main/resources/default_text.txt").getAbsolutePath();
        this.texts = new ArrayList<>();
        if (path.length() > 0) {
            try {
                System.out.println(getClass());
                InputStream inputStream = getClass().getResourceAsStream("/main/resources/default_text.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line = br.readLine();
                while (line != null) {
                    texts.add(line);
                    line = br.readLine();
                }
            } catch (IOException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        }
    }

}
