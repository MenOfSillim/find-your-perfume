package com.springriders.perfume.utils;

import java.io.File;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {
	
	public static void makeFolder(String path) {
		File dir = new File(path);		
		if(!dir.exists()) {
			dir.mkdirs();
		}
	}
	
	public static String getExt(String fileNm) {
		return fileNm.substring(fileNm.lastIndexOf("."));
	}
	
	public static String getRandomUUID(MultipartFile mf) {
		String originFileNm = mf.getOriginalFilename();
		String ext = getExt(originFileNm);
		return UUID.randomUUID() + ext;
	}
	
	public static String saveFile(String path, MultipartFile mf) {
		if(mf.isEmpty()) { return null; }
		String saveFileNm = getRandomUUID(mf);
		
		try {
			mf.transferTo(new File(path + saveFileNm));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return saveFileNm;
	}
	
	public static boolean delFile(String path) {
		File file = new File(path);
		if(file.exists()) {
			return file.delete();
		}
		return false;
	}
	
	public static boolean deleteFilesRecursively(File rootFile) {
		File[] allFiles = rootFile.listFiles();
        if (allFiles != null) {
            for (File file : allFiles) {
                deleteFilesRecursively(file);
            }
        }
        System.out.println("Remove file: " + rootFile.getPath());
        return rootFile.delete();
	}
}