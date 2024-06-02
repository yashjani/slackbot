package com.openai.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileTypeChecker {

	private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "bmp", "gif", "tiff",
			"webp");
	private static final List<String> VIDEO_EXTENSIONS = Arrays.asList("mp4", "avi", "mkv", "mov", "wmv", "flv", "webm",
			"m4v");
	private static final List<String> AUDIO_EXTENSIONS = Arrays.asList("mp3", "wav", "aac", "flac", "ogg", "m4a");

	public enum FileType {
		IMAGE, VIDEO, AUDIO, UNKNOWN
	}

	public static FileType getFileType(String filePath) {
		String fileExtension = getFileExtension(filePath).toLowerCase();

		if (IMAGE_EXTENSIONS.contains(fileExtension)) {
			return FileType.IMAGE;
		} else if (VIDEO_EXTENSIONS.contains(fileExtension)) {
			return FileType.VIDEO;
		} else if (AUDIO_EXTENSIONS.contains(fileExtension)) {
			return FileType.AUDIO;
		} else {
			return FileType.UNKNOWN;
		}
	}

	private static String getFileExtension(String filePath) {
		String fileName = new File(filePath).getName();
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
			return fileName.substring(dotIndex + 1);
		}
		return "";
	}
}
