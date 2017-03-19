package Scraper;

import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;

import java.util.Scanner;

/**
 * Name: Alex Oladele
 * Date: 3/19/17
 * Assignment: LyricScraper
 */
public class ScrapeLyrics {

    public static void main(String[] args) {
//        Variables to be used
        String apiKey = "8519790b9053d022244a85651d42a976", trackArtist = "Frank Ocean", trackName;
        MusixMatch musixMatch = new MusixMatch(apiKey);

//        Get Song name form user
        System.out.print("Input Track Name: ");
        Scanner in = new Scanner(System.in);
        trackName = in.nextLine().trim();

//        Search For Track
        try {
            Track track = musixMatch.getMatchingTrack(trackName, trackArtist);
            TrackData data = track.getTrack();
            System.out.println(("Results: "));
            System.out.println(data.getTrackName());
            System.out.println(data.getAlbumName());
            System.out.println(data.getArtistName());
            System.out.println(data.getTrackId());
        } catch (MusixMatchException e) {
            e.printStackTrace();
        }

    }
}
