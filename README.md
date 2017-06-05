# Frank Ocean Twitter Bot #
*(Note: Probably won't be updating this repo much as I'd rather keep up the Kotlin version)*

This is a bot that tweets Random Frank Ocean lyrics on a two-hour time interval.  
The lyrics are updated as new songs come out.

Follow it on [Twitter](https://twitter.com/FrankOceanBot)  
Project re-created in [Kotlin](https://github.com/dragid10/FrankOceanBot_Kotlin)
___

#### Twitter Connection #### 
The program can connect to Twitter in 1 of 2 ways:

1. Directly through The twitter4J.properties file (sample provided [here](https://github.com/dragid10/Frank_Ocean_TweetBot/blob/master/twitter4j-sample.properties))
2. Programmatically, which is done by default.

##### twitter4J.properties:  
There is a sample of twitter4J supplied in the repo. The only step neccessary is to comment out the lines that connects it programmatically, and fill in with your own info. The file is placed in the root project folder.

##### Programmatic Connection:  
The default connection method used is connecting to twitter via the connectToTwitter method. In order to use this, you must have a stripped down version of twitter4J.properties in your root folder.
e.g.:   
(Good Version)
            
                true
                *********************
                ******************************************
                **************************************************
                ******************************************
                
vs:  
(Bad Version)

                debug=true
                oauth.consumerKey=*********************
                oauth.consumerSecret=******************************************
                oauth.accessToken=**************************************************
                oauth.accessTokenSecret=******************************************



____
![Happy Franking!](https://consequenceofsound.files.wordpress.com/2016/11/frank-ocean-new-york-times-interview.png "Frank Ocean")