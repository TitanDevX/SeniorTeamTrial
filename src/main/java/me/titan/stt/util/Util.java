package me.titan.stt.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.*;

public class Util {

	public static String colorize(String msg){
		return ChatColor.translateAlternateColorCodes('&',msg);
	}
	public static void tell(CommandSender s, String msg){
		s.sendMessage(colorize(msg));
	}
	public static void tell(CommandSender s, String... msgs){
		for (String msg : msgs) {
			s.sendMessage(colorize(msg));
		}

	}
	public static ByteArrayInputStream serialize(Object obj){
		try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
			try(ObjectOutputStream o = new ObjectOutputStream(b)){
				o.writeObject(obj);
			}
			return new ByteArrayInputStream(b.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static Object deserialize(InputStream is) throws IOException, ClassNotFoundException {
		try(ObjectInputStream o = new ObjectInputStream(is)){
			return o.readObject();
		}
	}

}
