package Tweeter;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Name: Alex Oladele
 * Date: 3/19/17
 * Assignment: LyricScraper
 */
public class Tweet {
    public static void main(String[] args) {
//        Fill The DB with ALL lyrics in Lyrics Folder. [Use addSongToDB to add 1 song]
//        populateDBWithLyrics();

        Timer timer = new Timer();
        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                //        Get Random Row from DB
                String lyricToTweet = getLyricsFromDB();

                //        Post Tweet to Twitter
                try {
                    postTweet(lyricToTweet);
                } catch (IOException e) {
                    wasSuccessful(false);
                    System.out.println("Tweet Not Successfully posted!");
                    e.printStackTrace();
                }
            }
        };

// schedule the task to run starting now and then every hour...
        timer.schedule(hourlyTask, 0L, ((1000 * 60 * 60) * 2));


    }

    private static void postTweet(String lyric) throws IOException {
        BufferedReader twitterProp = new BufferedReader(new FileReader(new File("twitter4j.properties")));
        Twitter twitter = connectToTwitter(twitterProp).getInstance();
        Status status = null;
        try {
            status = twitter.updateStatus(lyric);
            System.out.println("Successfully updated the status to [" + status.getText() + "].");
        } catch (TwitterException e) {
            wasSuccessful(false);
            System.out.println("Tweet Not Successfully Posted!");
            e.printStackTrace();
        }
    }

    private static TwitterFactory connectToTwitter(BufferedReader bf) throws IOException {
        String debug = bf.readLine();
        String consumerKey = bf.readLine();
        String consumerSecret = bf.readLine();
        String accessToken = bf.readLine();
        String accessTokenSecret = bf.readLine();

        // The factory instance is re-useable and thread safe.
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf;
    }

    private static String getLyricsFromDB() {
        ResultSet rs = querySelect("SELECT TOP 1 lyricPair FROM lines ORDER BY NEWID()");
        try {
            String lyricString = null;

            if (rs != null) {
                while (rs.next()) {
                    lyricString = rs.getString("lyricPair");
                    System.out.println(lyricString);

                }
                return lyricString;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Connection connectToDB(Connection connection) throws ClassNotFoundException, FileNotFoundException {
        // TODO Change File to path that contains username and password to DB and remove "-sample' from twitter4j prop
//        File userNamePassword = new File("DBUserName_Password.txt");
        File userNamePassword = new File("DBUserName_Password-sample.txt");
        Scanner in = new Scanner(userNamePassword);
        String userName = null, password = null;

//        Assign Username and password from file to variables
        userName = in.nextLine();
        password = in.nextLine();

        String url = String.format(
                "jdbc:sqlserver://frankoceanbot.cystsh6pxla8.us-east-2.rds.amazonaws.com:1433;" +
                        "databaseName=Lyrics;user=%s;password=%s;", userName, password);
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            connection = DriverManager.getConnection(url);
            if (connection == null) {
                System.out.println("ERROR CONNECTING TO DATABASE");
                System.exit(0);
                return null;
            } else {
                String schema = connection.getSchema();
            }
//            System.out.println("Connected Successfully to DB");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static void runQuery(String query) {
        Statement statement;
        Connection connection = null;
        try {
            connection = connectToDB(connection);
            statement = connection.createStatement();
            if (statement != null) {
                int resultSet = statement.executeUpdate(query);
                wasSuccessful(true);
            }
        } catch (SQLException e) {
            wasSuccessful(false);
            e.printStackTrace();
            System.exit(0);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (NullPointerException e) {
            wasSuccessful(false);
            System.out.println("Some Variable was null! (Probs connection");
            e.printStackTrace();
            System.exit(0);
        } catch (FileNotFoundException e) {
            wasSuccessful(false);
            System.out.println("Could not find login Info");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static ResultSet querySelect(String query) {
        Statement statement;
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = connectToDB(connection);
            statement = connection.createStatement();
            if (statement != null) {
                resultSet = statement.executeQuery(query);
                wasSuccessful(true);
                return resultSet;
            }
        } catch (SQLException e) {
            wasSuccessful(false);
            e.printStackTrace();
            System.exit(0);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (NullPointerException e) {
            wasSuccessful(false);
            System.out.println("Some Variable was null! (Probs connection");
            e.printStackTrace();
            System.exit(0);
        } catch (FileNotFoundException e) {
            wasSuccessful(false);
            System.out.println("Could not find login Info");
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    private static void insertIntoDB(String lyrics) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = connectToDB(connection);

            if (connection != null) {
                PreparedStatement inputLyrics = connection.prepareStatement("INSERT INTO [dbo].[lines]([lyricPair]) VALUES (?);");
                inputLyrics.setString(1, lyrics);
                inputLyrics.executeUpdate();
                wasSuccessful(true);
            } else {
                System.out.println("Connection was null");
                System.exit(0);
            }
        } catch (SQLException e) {
            wasSuccessful(false);
            e.printStackTrace();
            System.exit(0);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (NullPointerException e) {
            wasSuccessful(false);
            System.out.println("Some Variable was null! (Probs connection");
            e.printStackTrace();
            System.exit(0);
        } catch (FileNotFoundException e) {
            wasSuccessful(false);
            System.out.println("Could not find login Info");
            e.printStackTrace();
            System.exit(0);
        }
    }

    private static void populateDBWithLyrics() {
        // Dir you're reading from
        File dir, file = null;
        BufferedWriter bWriter = null;
        BufferedReader bReader = null;


//        Reading from the lyrics folder
        dir = new File("lyrics");

//        Get sub directories
        File[] subDirs = dir.listFiles();

        try {
            long startTime = System.currentTimeMillis();
            // Reads each file in the Lyrics Directory
            if (subDirs != null) {
                for (File subDir : subDirs) {
                    File[] newFile = subDir.listFiles();
                    if (newFile != null) {
                        for (File f : newFile) {
                            System.out.println(f);
                            // Assigns File to Buffered Reader to read in
                            bReader = new BufferedReader(new FileReader(f));
                            String line = null, lyric1 = null, lyric2 = null, lyricPair = null;
                            addSongToDB(bReader, lyric1, lyric2);
                        }
                    }
                }
            }
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("All Lyrics Successfully Entered in DB!" + "(Total Time: " + (totalTime / 1000) + "s)");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addSongToDB(BufferedReader bReader, String lyric1, String lyric2) throws IOException {
        String line;
        String lyricPair;// Reads in Lines from files
        while ((line = bReader.readLine()) != null) {
            line = line.replace("~END~", "").trim();
            if (lyric1 == null) {
                lyric1 = line;
            } else if (lyric2 == null) {
                lyric2 = line;
            } else {
                lyricPair = lyric1 + '\n' + lyric2;
                lyricPair = lyricPair.trim();
                if (!lyricPair.isEmpty() && lyricPair != null && !lyricPair.equalsIgnoreCase("\n"))
                    insertIntoDB(lyricPair);
                lyric1 = null;
                lyric2 = null;
                lyricPair = null;
            }
        }
    }

    private static boolean wasSuccessful(boolean wasSuccessful) {
        if (wasSuccessful) {
//            System.out.println("Statement Executed Successfully!");
            return true;
        } else {
            System.out.println("Statement Not Executed Successfully, No Lines Have Been Updated");
            return false;
        }
    }

    //region MergeLyrics (Unused)
    /*static void mergeLyrics() {
        // Dir you're reading from
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
            for (File subDir : subDirs) {
                File[] newFile = subDir.listFiles();
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
    }*/
    //endregion
}
