"""
    instabot example
    Workflow:
        Follow user's followers by username.
"""

import argparse
import os
import sys
import json

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
bot.login(username=args.u, password=args.p,
          proxy=args.proxy, use_cookie=True)

userID = bot.get_user_id_from_username(args.usernameTarget)
userInfos = bot.get_user_info(userID)

# print('userID = ' + userID)
# print('userInfos = ' + json.dumps(userInfos))

print('Follower count = ' + str(userInfos.get("follower_count")))

print('[[[' + str(userInfos.get("follower_count")) + ']]]')

# print('Voila')

exit(0)