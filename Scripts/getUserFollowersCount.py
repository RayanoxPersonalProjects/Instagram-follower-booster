"""
    instabot example
    Workflow:
        Follow user's followers by username.
"""

import argparse
import os
import sys
import json
import logging

###########################
# OWN FUNCTIONS
###########################

def convert_to_username(x):
    x = str(x)
    if x.isdigit():
        # Cas d'un ID numerique
        x = bot.get_username_from_user_id(x)
    return x

###########################
# END OWN FUNCTIONS
###########################

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

usernameTarget = convert_to_username(args.usernameTarget)
print("convert_to_username = " + usernameTarget)

userID = bot.get_user_id_from_username(usernameTarget)
userInfos = bot.get_user_info(userID)

# print('userID = ' + userID)
print('userInfos = ' + json.dumps(bot.api.last_json))

print('Follower count = ' + str(userInfos.get("follower_count")))

print('[[[' + str(userInfos.get("follower_count")) + ']]]')

# print('Voila')

exit(0)