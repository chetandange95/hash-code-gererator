package com.qfix.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.qfix.model.HashDetails;

public class HashUtility {

	List<HashDetails> hashDetails= new ArrayList<HashDetails>();
	public List<HashDetails> listFilesForFolder(final File folder) {
		byte[] sha1 = null;
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
		String createdDate = dateFormat.format(date);
		
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	        	if(fileEntry.getName().endsWith(".java") || fileEntry.getName().endsWith(".html")
	        		|| fileEntry.getName().endsWith(".js") || fileEntry.getName().endsWith(".css"))
	        	{
	        		try {
	        			sha1 = createSha1(fileEntry);
					} catch (Exception e) {
						e.printStackTrace();
					}
	        		HashDetails fileDetail = new HashDetails();
	        		fileDetail.setFileName(fileEntry.getName());
	        		fileDetail.setSha1Code(Base64.getEncoder().encodeToString(sha1));
	        		fileDetail.setCreatedDate(createdDate);
					hashDetails.add(fileDetail);
		            System.out.println("File name : "+ fileEntry.getName() + " Hash bytes : " + Base64.getEncoder().encodeToString(sha1));
	        	}
	        }
	    }
	    
	    return hashDetails;
	}
	
	public byte[] createSha1(File file) throws Exception  {
	    MessageDigest digest = MessageDigest.getInstance("SHA-1");
	    InputStream fis = new FileInputStream(file);
	    int n = 0;
	    byte[] buffer = new byte[8192];
	    while (n != -1) {
	        n = fis.read(buffer);
	        if (n > 0) {
	            digest.update(buffer, 0, n);
	        }
	    }
	    return digest.digest();
	}
	
	public void writeDataToExcel(List<HashDetails> hashDetails) {
	        // Blank workbook 
	        XSSFWorkbook workbook = new XSSFWorkbook(); 
	  
	        // Create a blank sheet 
	        XSSFSheet sheet = workbook.createSheet("student Details"); 
    		
    		int rownum = 0; 
    		String allHashString = "";
    		HashUtility hashUtility = new HashUtility();
    		Row rowForGitCommitId = sheet.createRow(rownum++);
    		Cell cellForGitCommitId = rowForGitCommitId.createCell(rownum);
    		cellForGitCommitId.setCellValue(Constants.GIT_COMMIT_ID);
	        if(hashDetails != null && hashDetails.size() > 0)
	        {
	        	for(HashDetails hashDetail : hashDetails)
	        	{
	        		Row row = sheet.createRow(rownum++); 
	 	            int cellnum = 0; 
	 	            // this line creates a cell in the next column of that row 
 	                Cell cellFileName = row.createCell(cellnum++); 
 	                cellFileName.setCellValue(hashDetail.getFileName()); 
 	                Cell cellFileHash = row.createCell(cellnum++); 
 	                cellFileHash.setCellValue(hashDetail.getSha1Code()); 
 	                Cell cellCreatedDate = row.createCell(cellnum++); 
 	                cellCreatedDate.setCellValue(hashDetail.getCreatedDate()); 
 	                allHashString += hashDetail.getSha1Code();
	        	}
	        	
	        	Row row = sheet.createRow(rownum++); 
	        	Cell cellAllHashTitle = row.createCell(0); 
	        	cellAllHashTitle.setCellValue("All hash SHA-1 :");
	        	Cell cellAllHash = row.createCell(1); 
	        	cellAllHash.setCellValue(getSha1ForString(allHashString)); 
	        }
	        try { 
	            // this Writes the workbook gfgcontribute 
	            FileOutputStream out = new FileOutputStream(new File(Constants.EXPORT_FILE_PATH + "File hash details.xlsx")); 
	            workbook.write(out); 
	            out.close(); 
	            System.out.println("File hash details written successfully on disk."); 
	        } 
	        catch (Exception e) { 
	            e.printStackTrace(); 
	        } 
	}
	
	private String getSha1ForString(String value) {
		String sha1 = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
	        digest.reset();
	        digest.update(value.getBytes("utf8"));
	        sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
		} catch (Exception e){
			e.printStackTrace();
		}
		sha1 = org.apache.commons.codec.digest.DigestUtils.shaHex( value );
		return sha1;
	}
}
