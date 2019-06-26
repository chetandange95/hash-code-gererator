package com.qfix.controller;

import java.io.File;
import java.util.List;

import com.qfix.model.HashDetails;
import com.qfix.utility.HashUtility;

public class HashGeneratorController {

	public static String PACKAGE_URL = "/home/pc/Desktop/Angular js study/Eclipse_Projects_Study/test/src/com/cybersource";
	
	public static void main(String[] args) {
		HashUtility hashUtility = new HashUtility();
		File folder = new File(PACKAGE_URL);
		List<HashDetails> hashDetails = hashUtility.listFilesForFolder(folder);
		HashDetails header = new HashDetails();
		header.setFileName("File name");
		header.setSha1Code("Hash code");
		header.setCreatedDate("Created date");
		hashDetails.add(0, header);
		hashUtility.writeDataToExcel(hashDetails);
	}
}
