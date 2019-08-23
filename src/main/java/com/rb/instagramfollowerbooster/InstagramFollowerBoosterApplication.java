package com.rb.instagramfollowerbooster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.rb.instagramfollowerbooster.core.Bot;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Namespace;


@SpringBootApplication
public class InstagramFollowerBoosterApplication implements CommandLineRunner{

	private final static String ARG_NAME_LINK_USER_ID_START_FROM = "fromUser";
	private final static String ARG_NAME_TARGET_FOLLOWER_COUNT = "targetFollowers";
	private final static String ARG_NAME_FORCE_START_NEW_INSTANCE = "forceNewStart";
	

	@Autowired
	private Bot bot;
	
	
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(InstagramFollowerBoosterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Arguments arguments = extractArguments(args);
		bot.StartBooster(arguments.userIdToStartFrom, arguments.targetFollowerCount, arguments.forceStartANewUserInstance);
	}
	
	
	
	
	private static Arguments extractArguments(String[] args) {
		ArgumentParser parser = ArgumentParsers.newFor("Instagram-follower-bot").build()
                .description("Create and run a Bot to increase your amount of followers");
        parser.addArgument("--" + ARG_NAME_LINK_USER_ID_START_FROM)
				.required(true)
                .help("ID of the account to start the massive follow from. (to find the ID from a username, use the tool there -> https://codeofaninja.com/tools/find-instagram-user-id)");
        parser.addArgument("--" + ARG_NAME_TARGET_FOLLOWER_COUNT)
        		.required(true)
                .help("The max follower count targeted before to stop the bot (if no other criteria is reached to stop the bot)");
        parser.addArgument("--" + ARG_NAME_FORCE_START_NEW_INSTANCE)
        		.setDefault(false)
                .help("If true, a new empty instance will be started, cleaning all the previous datas. If false, a new running of the program will just continue where it last ended.");
        Namespace ns = null;
        
        ns = parser.parseArgsOrFail(args);
		int linkUserToStartFrom = Integer.parseInt(ns.getString(ARG_NAME_LINK_USER_ID_START_FROM));
		int targetFollowers = Integer.parseInt(ns.getString(ARG_NAME_TARGET_FOLLOWER_COUNT));
		boolean forceNewStart = Boolean.parseBoolean(ns.getString(ARG_NAME_FORCE_START_NEW_INSTANCE));
		
		return new Arguments(linkUserToStartFrom, targetFollowers, forceNewStart);
	}

	
	private static class Arguments{
		public Arguments(int linkUserToStartFrom, int targetFollowerCount, boolean forceStartANewUserInstance) {
			this.userIdToStartFrom = linkUserToStartFrom;
			this.targetFollowerCount = targetFollowerCount;
			this.forceStartANewUserInstance = forceStartANewUserInstance;
		}
		public int userIdToStartFrom; 
		public int targetFollowerCount;
		public boolean forceStartANewUserInstance;
	}
}
