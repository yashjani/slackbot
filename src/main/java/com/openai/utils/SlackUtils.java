package com.openai.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.slack.api.model.Message;

public class SlackUtils {

	public static List<String> getChunkFromMessages(List<Message> messages) {
		List<String> chunks = new ArrayList<>();
		StringBuilder message = new StringBuilder();
		messages.stream().map(s -> cleanUpString(s)).forEach(s -> {
			if (s.length() > 4000) {
				getChunk(s, chunks);
			} else {
				if (message.length() + s.length() > 4000) {
					chunks.add(message.toString());
					message.setLength(0);
				}
				message.append(s + "\n");
			}
		});
		if (message.length() > 4000) {
			getChunk(message.toString(), chunks);
		} else {
			chunks.add(message.toString());
		}
		return chunks;
	}

	public static List<String> getFileUrls(List<Message> messages) {
		List<String> fileUrls = new ArrayList<>();
		for(Message message : messages) {
			if(message.getFiles() != null && !message.getFiles().isEmpty()) {
				message.getFiles().forEach(item -> fileUrls.add(item.getUrlPrivate()));
			}
		}
		return fileUrls;
	}
	
	public static void getChunk(String message, List<String> chunks) {
		if (message.length() > 0) {
			int index = 0;
			while (index < message.length()) {
				int endIndex = index + 4000;
				if (endIndex >= message.length()) {
					chunks.add(message.substring(index));
				} else {
					chunks.add(message.substring(index, endIndex));
				}
				index = endIndex;
			}
		}
	}

	public static String cleanUpString(Message message) {
		String input = getBotAndUserId(message) + ": " + message.getText();
		Pattern pattern = Pattern.compile("<@\\w+>");
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			String userTag = matcher.group();
			String userId = replaceUserTagsWithId(userTag);
			input = input.replaceAll(userTag, userId);
		}
		return removeUrl(input + " ");

	}

	public static String removeUrl(String input) {
		String pattern = "<[^>]+\\|([^>]+)>"; // regex pattern to match the text between '<' and '>' and after '|'
		String replacement = "$1"; // replacement string
		return input.replaceAll(pattern, replacement).trim().replaceAll("\n", " ");
	}

	public static Set<String> getUsersFromMessages(List<Message> messages) {
		Set<String> userIdSet = new HashSet<>();
		Pattern pattern = Pattern.compile("<@\\w+>");
		for (Message message : messages) {
			userIdSet.add(replaceUserTagsWithId(getBotAndUserId(message)));
			Matcher matcher = pattern.matcher(message.getText());
			while (matcher.find()) {
				userIdSet.add(replaceUserTagsWithId(matcher.group()));
			}
		}
		return userIdSet;
	}

	public static String replaceUserIdWithTags(String output, Set<String> userIdSet) {
		for (String userId : userIdSet) {
			if (userId == null)
				continue;
			output = output.replaceAll(userId, "<@" + userId + ">");
		}
		return output;
	}

	public static String replaceUserTagsWithId(String userTag) {
		String userId = userTag;
		return userId != null && !userId.isEmpty() ? userId.replaceAll("<@", "").replace(">", "") : userId;
	}

	public static String getBotAndUserId(Message messsage) {
		return messsage.getUser() == null ? messsage.getBotId() : messsage.getUser();
	}

	public static List<String> stringToList(String input) {
		List<String> result = Arrays.asList(input.substring(1, input.length() - 1).split(","));
		return result;
	}

	public static boolean verifySlackRequest(String slackSec, String timeStamp, String request, String slackSign) {

		long timestamp = Long.parseLong(timeStamp);
		long currentTime = System.currentTimeMillis() / 1000;
		if (Math.abs(currentTime - timestamp) > 60 * 5) {
			return false; 
		}
		String sigBasestring = "v0:" + timestamp + ':' + request;
		String key = slackSec.getBytes(StandardCharsets.UTF_8).toString();
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		Mac hmac = null;
		try {
			hmac = Mac.getInstance("HmacSHA256");
			hmac.init(secretKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] hashBytes = hmac.doFinal(sigBasestring.getBytes(StandardCharsets.UTF_8));
		String mySignature = "v0=" + bytesToHex(hashBytes).toLowerCase();
		return slackSign.compareTo(mySignature) == 0;
	}
	
	public static String bytesToHex(byte[] bytes) {
	    StringBuilder hex = new StringBuilder();
	    for (byte b : bytes) {
	        hex.append(String.format("%02x", b));
	    }
	    return hex.toString();
	}
}