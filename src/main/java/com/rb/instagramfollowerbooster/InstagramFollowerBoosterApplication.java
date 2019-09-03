package com.rb.instagramfollowerbooster;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.rb.instagramfollowerbooster.core.Bot;
import com.rb.instagramfollowerbooster.dao.FileDataFacade;
import com.rb.instagramfollowerbooster.model.UserSession;
import com.rb.common.api.datafilestorage.DataStorage;
import com.rb.common.api.logging.LogLevel;
import com.rb.common.api.logging.LogManager;
import com.rb.common.api.logging.LoggingAction;
import com.rb.common.exceptions.BadFormatPropertyException;
import com.rb.common.exceptions.NotImplementedException;
import com.rb.common.utils.ErrorBuilder;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Namespace;


@ComponentScan(basePackages = {"com.rb.common.api", "com.rb.instagramfollowerbooster"})
@SpringBootApplication
public class InstagramFollowerBoosterApplication implements CommandLineRunner{

	private final static String ARG_NAME_USERNAME_START_FROM 					= "fromUser";
	private final static String ARG_NAME_TARGET_FOLLOWER_COUNT 					= "targetFollowers";
	private final static String ARG_NAME_FORCE_START_NEW_INSTANCE 				= "forceNewStart";
	private final static String ARG_NAME_MAIL_USERNAME 							= "mailUsername";
	private final static String ARG_NAME_MAIL_PASSWORD 							= "mailPassword";
	public final static String ARG_NAME_MAIL_RECIPIENTS 						= "mailRecipients";
	public final static String ARG_NAME_PYTHON_PATH 							= "pythonFolderPath";
	private final static String ARG_NAME_INSTAGRAM_USERNAME 					= "instaUsername";
	private final static String ARG_NAME_INSTAGRAM_PASSWORD 					= "instaPassword";
	private final static String ARG_NAME_SKIP_WHITELIST_GENERATION 				= "skipWhitelistGeneration";
//	public final static String ARG_NAME_MAIL_RECIPIENTS_ONLY_IMPORTANTS_MAILS 	= "mailRecipientsImportantsNotifs";
	
	
	@Autowired
	LogManager logger;
	
	@Autowired
	DataStorage dataStorage;
	
	@Autowired
	UserSession userSession;

	@Autowired
	private Bot bot;
	
	
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(InstagramFollowerBoosterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Arguments arguments = extractArguments(args);
		
		ProgramInitialisation(arguments);
		
		try {
			bot.StartBooster(arguments.usernameToStartFrom, arguments.targetFollowerCount, arguments.forceStartANewUserInstance, arguments.skipWhitelistGeneration);
		}catch(Exception e) {
			this.logger.log(String.format("An exception was catched at the root of the program ! Closing program.   \r\nException: \r\n" + ErrorBuilder.formatStringException(e)), LogLevel.ERROR, LoggingAction.All);
		}
	}
	
	
	
	
	private void ProgramInitialisation(Arguments arguments) throws Exception {
		if(logger.loadMailInfos() == null) {
			if(arguments.mailUsername == null || arguments.mailPassword == null || arguments.mailRecipients == null)
				throw new Exception(String.format("You must set the arguments %s, %s and %s for the first start of the program", ARG_NAME_MAIL_USERNAME, ARG_NAME_MAIL_PASSWORD, ARG_NAME_MAIL_RECIPIENTS));
			else {
				logger.registerMailCredentials(arguments.mailUsername, arguments.mailPassword, arguments.getRecipients(), "Insta-follower-booster");
				System.out.println("Mail logger registration -> OK");
			}
		}else 
			System.out.println("Mail logger well loaded.");
		
		if(arguments.pythonPath == null && dataStorage.getData(ARG_NAME_PYTHON_PATH, String.class) == null) {
			throw new Exception("The pythonPath argument must be set at least for the first execution. See the 'help' section for more details.");
		}else if(arguments.pythonPath != null) {
			if(arguments.pythonPath.endsWith(File.separator))
				arguments.pythonPath = arguments.pythonPath.substring(0, arguments.pythonPath.length()-1);
			dataStorage.setData(ARG_NAME_PYTHON_PATH, arguments.pythonPath);
		}
		
		// Session initialisation
		//userSession.set
		String userNameInsta;
		if(arguments.instaUsername != null) {
			dataStorage.setData(ARG_NAME_INSTAGRAM_USERNAME, arguments.instaUsername);
			userNameInsta = arguments.instaUsername;
		} else {
			userNameInsta = (String) dataStorage.getData(ARG_NAME_INSTAGRAM_USERNAME, String.class);
			if(userNameInsta == null)
				throw new Exception("The '" + ARG_NAME_INSTAGRAM_USERNAME + "' argument must be set at least for the first execution. See the 'help' section for more details.");
		}
		String passwordInsta;
		if(arguments.instaPassword != null) {
			dataStorage.setData(ARG_NAME_INSTAGRAM_PASSWORD, arguments.instaPassword);
			passwordInsta = arguments.instaPassword;
		} else {
			passwordInsta = (String) dataStorage.getData(ARG_NAME_INSTAGRAM_PASSWORD, String.class);
			if(passwordInsta == null)
				throw new Exception("The '" + ARG_NAME_INSTAGRAM_PASSWORD + "' argument must be set at least for the first execution. See the 'help' section for more details.");
		}
		this.userSession.setInstaUsername(userNameInsta);
		this.userSession.setInstaPassword(passwordInsta);
		
		// Notification mails
//		if(arguments.mailRecipientsImportantsNotifs != null) {
//			dataStorage.setData(ARG_NAME_MAIL_RECIPIENTS_ONLY_IMPORTANTS_MAILS, arguments.getRecipientsImportantsNotifs());
//		}
			
		
		
		this.logger.log("Program well initialized", LogLevel.INFO, LoggingAction.File, LoggingAction.Stdout);
	}

	
	private static Arguments extractArguments(String[] args) {
		ArgumentParser parser = ArgumentParsers.newFor("Instagram-follower-bot").build()
                .description("Create and run a Bot to increase your amount of followers");
        parser.addArgument("--" + ARG_NAME_USERNAME_START_FROM)
				.required(true)
                .help("ID of the account to start the massive follow from. (to find the ID from a username, use the tool there -> https://codeofaninja.com/tools/find-instagram-user-id)");
        parser.addArgument("--" + ARG_NAME_TARGET_FOLLOWER_COUNT)
        		.required(true)
                .help("The max follower count targeted before to stop the bot (if no other criteria is reached to stop the bot)");
        parser.addArgument("--" + ARG_NAME_FORCE_START_NEW_INSTANCE)
        		.setDefault(false)
                .help("If true, a new empty instance will be started, cleaning all the previous datas. If false, a new running of the program will just continue where it last ended.");
        parser.addArgument("--" + ARG_NAME_MAIL_USERNAME)
				.required(false)
		        .help("The username of the mail adress used to send logging mails.");
        parser.addArgument("--" + ARG_NAME_MAIL_PASSWORD)
				.required(false)
		        .help("The password of the mail adress used to send logging mails.");
        parser.addArgument("--" + ARG_NAME_MAIL_RECIPIENTS)
				.required(false)
		        .help("The recipients to send logging emails. If there are several adresses, they must be separated by commas.");
//        parser.addArgument("--" + ARG_NAME_MAIL_RECIPIENTS_ONLY_IMPORTANTS_MAILS)
//				.required(false)
//		        .help("The recipients to send only important notification emails. If there are several adresses, they must be separated by commas.");
        parser.addArgument("--" + ARG_NAME_PYTHON_PATH)
				.required(false)
		        .help("The path of the folder containing the 'python' executable. Please, the python version must be greater than 3.");
        parser.addArgument("--" + ARG_NAME_INSTAGRAM_USERNAME)
				.required(false)
		        .help("The login of the account you want to boost.");
        parser.addArgument("--" + ARG_NAME_INSTAGRAM_PASSWORD)
				.required(false)
		        .help("The pathword of the account you want to boost");
        parser.addArgument("--" + ARG_NAME_SKIP_WHITELIST_GENERATION)
        		.setDefault(false)
        		.required(false)
                .help("If true, the white list generation for a new bot instance will be skipped.");
        Namespace ns = null;
        
        ns = parser.parseArgsOrFail(args);
		String usernameToStartFrom = ns.getString(ARG_NAME_USERNAME_START_FROM);
		int targetFollowers = Integer.parseInt(ns.getString(ARG_NAME_TARGET_FOLLOWER_COUNT));
		boolean forceNewStart = Boolean.parseBoolean(ns.getString(ARG_NAME_FORCE_START_NEW_INSTANCE));
		String mailUsername = ns.getString(ARG_NAME_MAIL_USERNAME);
		String mailPassword = ns.getString(ARG_NAME_MAIL_PASSWORD);
		String mailRecipients = ns.getString(ARG_NAME_MAIL_RECIPIENTS);
//		String mailRecipientsImportantsNotifs = ns.getString(ARG_NAME_MAIL_RECIPIENTS_ONLY_IMPORTANTS_MAILS);
		String pythonPath = ns.getString(ARG_NAME_PYTHON_PATH);
		String instaUsername = ns.getString(ARG_NAME_INSTAGRAM_USERNAME);
		String instaPassword = ns.getString(ARG_NAME_INSTAGRAM_PASSWORD);
		boolean skipWhitelistGeneration = Boolean.parseBoolean(ns.getString(ARG_NAME_SKIP_WHITELIST_GENERATION));
		
		return new Arguments(usernameToStartFrom, targetFollowers, forceNewStart, mailUsername, /*mailRecipientsImportantsNotifs, */mailPassword, mailRecipients, pythonPath, instaUsername, instaPassword, skipWhitelistGeneration);
	}

	
	private static class Arguments{
		public Arguments(String linkUserToStartFrom, int targetFollowerCount, boolean forceStartANewUserInstance, String mailUsername, String mailPassword, String mailRecipients, String pythonPath, String instaUsername, String instaPassword/*, String mailRecipientsImportantsNotifs*/, boolean skipWhitelistGeneration) {
			this.usernameToStartFrom = linkUserToStartFrom;
			this.targetFollowerCount = targetFollowerCount;
			this.forceStartANewUserInstance = forceStartANewUserInstance;
			this.mailUsername = mailUsername;
			this.mailPassword = mailPassword;
			this.mailRecipients = mailRecipients;
			this.pythonPath = pythonPath;
			this.instaUsername = instaUsername;
			this.instaPassword = instaPassword;
			this.skipWhitelistGeneration = skipWhitelistGeneration;
//			this.mailRecipientsImportantsNotifs = mailRecipientsImportantsNotifs;
		}
		public String usernameToStartFrom; 
		public int targetFollowerCount;
		public boolean forceStartANewUserInstance;
		public String mailUsername;
		public String mailPassword;
		public String mailRecipients;
		public String pythonPath;
		public String instaUsername;
		public String instaPassword;
		public boolean skipWhitelistGeneration;
//		public String mailRecipientsImportantsNotifs;
		
		public ArrayList<String> getRecipients() {
			return new ArrayList<String>(Arrays.asList(this.mailRecipients.split(",")));
		}

//		public ArrayList<String> getRecipientsImportantsNotifs() {
//			return new ArrayList<String>(Arrays.asList(this.mailRecipientsImportantsNotifs.split(",")));
//		}		
	}
}
