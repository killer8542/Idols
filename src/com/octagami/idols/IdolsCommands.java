package com.octagami.idols;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class IdolsCommands {

	public static class IdolCommand {

		public IdolCommand(String name, ArrayList<String> aliases, String defaultPermission, String description, ArrayList<String> commandArgs) {

			this.name = name;
			this.description = description;
			this.aliases = aliases;
			this.permission = IdolsPlugin.PERMISSION_ROOT + name;
			this.defaultPermission = defaultPermission;
			this.commandArgs = commandArgs;

		}

		public String				name;
		public String				description;
		public ArrayList<String>	aliases;
		public String				permission;
		public String				defaultPermission;
		public ArrayList<String>	commandArgs;

	}

	//@formatter:off
    public static final IdolCommand[] playerCommands = {
    		
			new IdolCommand("cake",   new ArrayList<String>(Arrays.asList("c", "cakeme")), 
							"false",  "Gives the player some cake",        new ArrayList<String>(Arrays.asList("player"))),
    		new IdolCommand("homie",  new ArrayList<String>(), 
			        		"false",  "Spawns a homie",                    new ArrayList<String>()),
    		new IdolCommand("radar",  new ArrayList<String>(), 
			        		"false",  "Detects nearby ore",                new ArrayList<String>()),
    		new IdolCommand("potion", new ArrayList<String>(), 
	        		        "false",  "Creates potions",                   new ArrayList<String>()),			        		
    	    new IdolCommand("poke",   new ArrayList<String>(), 
    				        "false",  "Pokes another player",              new ArrayList<String>(Arrays.asList("player")))				

    };
    
    public static final IdolCommand[] adminCommands = {
		
		new IdolCommand("history",     new ArrayList<String>(), 
				        "op",          "Information about an offline player",  new ArrayList<String>(Arrays.asList("player"))),
		new IdolCommand("spawners",    new ArrayList<String>(), 
						"op",          "Turns mob spawners off and on",        new ArrayList<String>(Arrays.asList("on/off"))),				        
		new IdolCommand("idols",       new ArrayList<String>(), 
		                "op",          "Shows help and allows a reload",       new ArrayList<String>())
    };
    //@formatter:on

	//@formatter:off
	public static void main(String[] args) {

		System.out.print("Generating plugin.yml...");
		
		// Header section
		String header =  "name: "        + IdolsPlugin.NAME                   + "\n" +
				         "main: "        + IdolsPlugin.MAIN_CLASS             + "\n" +
				         "version: "     + IdolsPlugin.VERSION                + "\n";
		
		if (IdolsPlugin.DESCRIPTION.length() > 0) 
			   header += "description: " + IdolsPlugin.DESCRIPTION            + "\n";
		
		if (IdolsPlugin.AUTHORS.length > 0) 
			   header += "authors: "     + arrayToString(IdolsPlugin.AUTHORS) + "\n";
		       
		if (IdolsPlugin.WEBSITE.length() > 0)
			   header += "website: "     + IdolsPlugin.WEBSITE                + "\n";
		       
		if (IdolsPlugin.DATABASE)
			   header += "database: "    + "true"                       + "\n";           
		           
		if (IdolsPlugin.DEPENDENCIES.length > 0) 
			   header += "depend: "      + arrayToString(IdolsPlugin.DEPENDENCIES)      + "\n";
		
		if (IdolsPlugin.SOFT_DEPENDENCIES.length > 0) 
			   header += "softdepend: "  + arrayToString(IdolsPlugin.SOFT_DEPENDENCIES) + "\n";
		
		// Command section
		String commandSection = "commands:\n";
		
		commandSection += processCommandsSection(playerCommands);
		commandSection += processCommandsSection(adminCommands);
		
		// Permissions section
		String allGroup =           "    " + IdolsPlugin.PERMISSION_ROOT + "*:\n" +
				                    "        description: Gives access to all " + IdolsPlugin.NAME + " commands\n" +
				                    "        children:\n";
		
    	for (IdolCommand command : playerCommands) allGroup +=    "            " + command.permission + ": true\n";;
    	for (IdolCommand command : adminCommands)  allGroup +=    "            " + command.permission + ": true\n";;
    	
    	
		String playerGroup =        "    " + IdolsPlugin.PERMISSION_ROOT + "players.*:\n" +
                                    "        description: Gives access to all player level Idols commands\n" +
                                    "        children:\n";

		for (IdolCommand command : playerCommands) playerGroup += "            " + command.permission + ": true\n";
		
		
		String adminGroup =         "    " + IdolsPlugin.PERMISSION_ROOT + "admin.*:\n" +
                                    "        description: Gives access to all admin level Idols commands\n" +
                                    "        children:\n";

		for (IdolCommand command : adminCommands) adminGroup +=   "            " + command.permission + ": true\n";;
			
        String permissionsSection = "permissions:\n";

        permissionsSection += allGroup;
        permissionsSection += playerGroup;
        permissionsSection += adminGroup;

        permissionsSection += processPermissionsSection(playerCommands);
        permissionsSection += processPermissionsSection(adminCommands);

        // Combine sections
        String fullText = header + "\n" + commandSection + "\n" + permissionsSection;

        // Write file
        try {

            File newTextFile = new File("plugin.yml");

            FileWriter fw = new FileWriter(newTextFile);
            fw.write(fullText);
            fw.close();

            System.out.print(" Complete.");

        } catch (IOException iox) {

            System.out.print(" Failed!!!");
            iox.printStackTrace();
        }
        
    }
	//@formatter:on

	//@formatter:off
	private static String processCommandsSection(IdolCommand[] commands) {

		String section = "";

		for (IdolCommand command : commands) {

			section += "    " + command.name + ":\n";
			section += "        " + "description: " + command.description + "\n";
			
			if  (command.aliases.size() > 0 ) {
				

				section += "        " + "aliases: ";
				
				section += command.aliases.toString() + "\n";
				
			}
			section += "        " + "permission: " + command.permission + "\n";
			//section += "        " + "permission-message: You don't have <permission>\n";
			
			section += "        " + "usage: /<command>";

			for (String commandArg : command.commandArgs) {

				section += " [" + commandArg + "]";
			}

			section += "\n";
		}

		return section;
	}
	//@formatter:on

	//@formatter:off
    private static String processPermissionsSection(IdolCommand[] commands) {

        String section = "";

        for (IdolCommand command : commands) {

            section += "    " + IdolsPlugin.PERMISSION_ROOT + command.name + ":\n";
            section += "        description: " + command.description + "\n";
            section += "        default: " + command.defaultPermission + "\n";
        }

        return section;
    }
    //@formatter:on

	private static String arrayToString(String[] array) {

		String string = "[";

		for (int i = 0; i < array.length; i++) {

			string += array[i];
			if (i < array.length - 1)
				string += ", ";
		}

		return string += "]";
	}
	

}
