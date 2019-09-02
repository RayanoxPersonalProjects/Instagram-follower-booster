"""
    instabot example
    Workflow:
        Follow user's followers by username.
"""

import argparse
import os
import sys

sys.path.append(os.path.join(sys.path[0], '../'))
from instabot import Bot

parser = argparse.ArgumentParser(add_help=True)
parser.add_argument('-u', type=str, help="username")
parser.add_argument('-p', type=str, help="password")
parser.add_argument('-proxy', type=str, help="proxy")
parser.add_argument('-users', type=str, nargs='+', help='users')
args = parser.parse_args()

bot = Bot()
bot.login(username=args.u, password=args.p,
          proxy=args.proxy, use_cookie=True)


bot.max_following_to_followers_ratio = 200 # Enable to follow a user who has until 200 times more followings than followers.
bot.max_followers_to_following_ratio = 200
bot.filter_users_without_profile_photo=False
bot.filter_private_users=False
bot.filter_previously_followed=True

	  
for username in args.users:
    bot.follow_followers(username)



exit(0)