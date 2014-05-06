# SpeedReader
#### Stop reading the old-fashioned way

## The Goal
I use this desktop app on a daily basis to help myself read, and read faster.

Regular text stays in place and your eyes have to scan the lines. But if the words just pop up one at-a-time in front of you, you read more efficiently. You don't skip lines and futz around trying to find the start of the next one. It really helps. Also, this method allows you to set the exact number of words per minute that you want to read. And THAT is step one to training yourself to read faster.

I also find reading like this improves my focus and retention.

## The Program
No frills. I wrote this little app because I needed it, not because I was interested in UI. But it works.

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

