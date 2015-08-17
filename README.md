[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

# instagram-slackbot

A clojure project to run a bot that recieves instagram notifications and
forwards them to slack.

## How to run?

You need:
 - *POST_URL* - The URL to post stuff to slack (an incoming web-hook).
 - *CLIENT_ID* - Your Instagram client id. Visit [this page](https://instagram.com/developer/clients/manage/) to obtain yours.

The easiest way to get going is to push the Heroku button above. This will prompt you for the environment variables and start the app running on Heroku.

If you have the jar file, just do:

    POST_URL=<post url> CLIENT_ID=<client id> java -jar clj-slackbot.jar

Or can checkout the source and run:

    POST_URL=<post url> CLIENT_ID=<client id> lein run


## Recieving Instagram notifications

Check out the Instagram [docs](https://instagram.com/developer/realtime/) for information on how to do this.
The `callback_url` that should be supplied to instagram is `http://yourapp.com/handle-subscription`.

## Slack Configuration
Create an integration in the Slack web interface:

 - Incoming Webhook - Create a new Incoming Webhook, notedown its POST_URL.


## License

Copyright © 2015 Kári Tristan Helgason.  Licensed under the same terms as Clojure (EPL).
