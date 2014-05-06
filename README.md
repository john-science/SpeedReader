# SpeedReader
#### Stop reading the old-fashioned way

## The Goal
I use this desktop app on a daily basis to improve my reading comprension and speed. 

Regular text stays in place and your eyes have to scan over the lines. But if the words pop up one at a time on the screen, your eyes have to move less. And you don't lose time searching for the beginning of the next line.

This also allows you to set the number of words/minute you want to read. Which is ideal if you want to build up to reading faster.

## The Program
No frills. I wrote this little app because I needed it.

Just copy your text into the window, hit LOAD, then PLAY.

You can pause at any time, or even change the play speed during run time.

To run, you Scala RTE installed, compile in the commandline:

    scalac SpeedReader.scala

and then run:

    scala SpeedReader

## The TODO List
* Split words longer than 13 characters long, but do it smartly.
* Build better support for math
* Build better support for HTML addresses
* Verify the text-entry is UTF-8 compliant
* Split ALL words with hyphens? Or just some?
* Add a help menu?
* Prettify the UI

