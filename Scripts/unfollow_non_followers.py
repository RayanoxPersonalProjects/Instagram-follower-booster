"""
    instabot example
    Workflow:
        1) unfollows every from your account.
"""

import argparse
import os
import sys
import logging

sys.path.append(os.path.join(sys.path[0], '../'))
from instabot import Bot


parser = argparse.ArgumentParser(add_help=True)
parser.add_argument('-u', type=str, help="username")
parser.add_argument('-p', type=str, help="password")
parser.add_argument('-proxy', type=str, help="proxy")
args = parser.parse_args()

bot = Bot()

# Change the file log name (because the default library implementation creates a new file for each bot, using the bot ID).
logFileName = "[instabot_{}]".format(args.u)
bot.api.logger = logging.getLogger(logFileName)

bot.login(username=args.u, password=args.p,
          proxy=args.proxy, use_cookie=True)
		  
bot.max_unfollows_per_day = 2000
		  
bot.unfollow_non_followers()

exit(0)