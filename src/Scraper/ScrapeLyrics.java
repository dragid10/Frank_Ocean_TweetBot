package Scraper;

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Name: Alex Oladele
 * Date: 3/19/17
 * Assignment: LyricScraper
 */
public class ScrapeLyrics {

    public static void main(String[] args) throws IOException {
//        Variables to be used
        String apiKey = "8519790b9053d022244a85651d42a976", trackArtist = "Frank Ocean", trackName;
        MusixMatch musixMatch = new MusixMatch(apiKey);
        List<String> trackNameList = new ArrayList<>();
        final File FILE = new File("song-lyrics.txt");
        BufferedWriter bWriter = null;

//        Get Song name form user
        System.out.print("Input Track Name: ");
        Scanner in = new Scanner(System.in);
        trackName = in.nextLine().trim();

//        Search For Track
        try {
//            Create Buffered Writer
            bWriter = new BufferedWriter(new FileWriter(FILE));

            Track track = musixMatch.getMatchingTrack(trackName, trackArtist);
            TrackData data = track.getTrack();
            bWriter.flush();

            if (data != null) {
                int trackID = data.getTrackId();
                Lyrics lyrics = musixMatch.getLyrics(trackID);
                bWriter.append(lyrics.getLyricsBody());

                // TODO Finish getting full lyrics

            } else {
                System.out.println("Data was NULL! Track Probably not found");
            }
        } catch (MusixMatchException | IOException e) {
            e.printStackTrace();
        } finally {
            if (bWriter != null) {
                bWriter.close();
                System.out.println("Lyrics Successfully got!");
            }
        }

    }
}
