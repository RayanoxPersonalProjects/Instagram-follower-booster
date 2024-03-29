"""
    instabot example
    Workflow:
        Follow user's followers by username.
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
parser.add_argument('-usernameTarget', type=str, help='usernameTarget')
args = parser.parse_args()

# print('args.u = ' + args.u);
# print('args.p = ' + args.p);
# print('args.usernameTarget = ' + args.usernameTarget);

bot = Bot()

# Change the file log name (because the default library implementation creates a new file for each bot, using the bot ID).
logFileName = "[instabot_{}]".format(args.u)
bot.api.logger = logging.getLogger(logFileName)

bot.login(username=args.u, password=args.p,
          proxy=args.proxy, use_cookie=True)

userID = bot.get_user_id_from_username(args.usernameTarget)

print('userID = ' + userID)
print('[[[' + userID + ']]]')

# print('Voila')

exit(0)