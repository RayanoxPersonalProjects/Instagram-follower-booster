import logging

def initLogging(bot):
    # Setup logging
    log_filename = "instabot.log"

    bot.api.logger = logging.getLogger(log_filename)

    fh = logging.FileHandler(filename=log_filename)
    fh.setLevel(logging.INFO)
    fh.setFormatter(logging.Formatter("%(asctime)s %(message)s"))

    bot.api.logger.addHandler(fh)

    ch = logging.StreamHandler()
    ch.setLevel(logging.DEBUG)
    ch.setFormatter(logging.Formatter("%(asctime)s - %(levelname)s - %(message)s"))

    bot.api.logger.addHandler(ch)
    bot.api.logger.setLevel(logging.DEBUG)

    bot.logger = bot.api.logger