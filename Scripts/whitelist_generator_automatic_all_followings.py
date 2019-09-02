"""
    instabot example
    Whitelist generator: generates a list of users which
    will not be unfollowed.
"""

import sys
import os
import random
import argparse
import time

sys.path.append(os.path.join(sys.path[0], '../../'))
from instabot import Bot


DELAY_TIME_BETWEEN_ADDS=1



parser = argparse.ArgumentParser(add_help=True)
parser.add_argument('-u', type=str, help="username")
parser.add_argument('-p', type=str, help="password")
args = parser.parse_args()


'''
username=args.u
password=args.p
print('Bien joue')
print('Argument 1, username = ' + username)
print('Argument 2, password = ' + password)

exit()
'''


bot = Bot()
bot.login(username=args.u, password=args.p, use_cookie=True)

print("This script will generate whitelist.txt file with users"
      "who will not be unfollowed by bot. "
      "Press Y to add user to whitelist. Ctrl + C to exit.")
your_following = bot.following
already_whitelisted = bot.read_list_from_file("whitelist.txt")
rest_users = list(set(your_following) - set(already_whitelisted))
random.shuffle(rest_users)
with open("whitelist.txt", "a") as f:
    for user_id in rest_users:
        # user_info = bot.get_user_info(user_id)
        # print(user_info["username"])
        # print(user_info["full_name"])

        f.write(str(user_id) + "\n")
        print("User with ID " + user_id + " ADDED.\r")

        time.sleep(DELAY_TIME_BETWEEN_ADDS)

exit(0)