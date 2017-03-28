package Tweeter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Name: Alex Oladele
 * Date: 3/19/17
 * Assignment: LyricScraper
 */
public class Tweet {
    public static void main(String[] args) {
//        +++++++++ VARIABLES +++++++++
        List<String> lyricsList = new ArrayList();

//        Compiles lyrics into one doc
//        mergeLyrics();
        populateArrayList(lyricsList);


    }

    static void populateArrayList(List<String> lyricsList) {
        //      Reads In Compiled Lyrics and puts them in ArrayList
        File compiledLyrics = new File("compiled_lyrics.txt"), outFile = null;
        String inLine = null;
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(compiledLyrics));

//            Reads in file line
            while ((inLine = bReader.readLine()) != null) {
//                Replaces END line with empty string
                inLine = inLine.trim().replace("~END~", "");

//              Adds Lyric to ArrayList as long as
                lyricsList.add(inLine);
            }

//          ArrayList Check
        /*    for (String s : lyricsList)
                System.out.println(s);*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void mergeLyrics() {
        //        Dir you're reading from
        File dir, file = null;
        BufferedWriter bWriter = null;
        BufferedReader bReader = null;
        String line = null;

//        Reading from the lyrics folder
        dir = new File("lyrics");

//        Get sub directories
        File[] subDirs = dir.listFiles();


        try {
//            Creates file that all lyrics will be written to
            file = new File("compiled_lyrics.txt");

//            Assigns bufferedWriter to write to above file
            bWriter = new BufferedWriter(new FileWriter(file));

//            Reads each file in the Lyrics Directory
            for (int i = 0; i < subDirs.length; i++) {
                File[] newFile = subDirs[i].listFiles();
                for (File f : newFile) {
                    System.out.println(f);
//                Assigns File to Buffered Reader to read in
                    bReader = new BufferedReader(new FileReader(f));

//                Reads in Lines from files
                    while ((line = bReader.readLine()) != null) {
                        line = line.trim();
//                    Makes sure that the line isn't already empty
                        if (!Objects.equals(line, "")) {

//                        Appends to file with lyric line
                            bWriter.append(line).append("\r\n");
                        } else if (line.equals("~END~")) {
                            bWriter.append("\r\n").append("\r\n");
                        } else {
                            bWriter.append("\r\n");
                        }
                    }
                }
            }
            bWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
