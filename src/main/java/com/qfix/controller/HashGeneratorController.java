package com.qfix.controller;

import java.io.File;
import java.util.List;

import com.qfix.model.HashDetails;
import com.qfix.utility.Constants;
import com.qfix.utility.HashUtility;

public class HashGeneratorController {

	
	public static void main(String[] args) {
		HashUtility hashUtility = new HashUtility();
		File folder = new File(Constants.PACKAGE_URL);
		List<HashDetails> hashDetails = hashUtility.listFilesForFolder(folder);
		HashDetails header = new HashDetails();
		header.setFileName("File name");
		header.setSha1Code("Hash code");
		header.setCreatedDate("Created date");
		hashDetails.add(0, header);
		hashUtility.writeDataToExcel(hashDetails);
	}
}
