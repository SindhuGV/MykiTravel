package com.busfare.data;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.busfare.calculate.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

public class FileReadWrite {

	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static List<String[]> readCSV(String fileName) throws IOException {
		Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/taps.csv"));

		CSVReader csvReader = new CSVReaderBuilder(reader).withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
				// to skip the header
				.withSkipLines(1)
				.build();
		// to read all rows at once
		return csvReader.readAll();
	}

	/**
	 * 
	 * @param allRows
	 * @return
	 */
	public static List<BusFare> processCSVData(List<String[]> allRows) {
		List<BusFare> busFareList = new ArrayList<>();
		// Read CSV line by line and set the Bus Fare object using the string array
		for (String[] row : allRows) {

			BusFare busFare = new BusFare();
			busFare.setId(row[0]);
			busFare.setDateTime(formatDateTime(row[1].trim()));
			busFare.setTapType(row[2]);
			busFare.setStopId(row[3]);
			busFare.setCompanyId(row[4]);
			busFare.setBusID(row[5]);
			busFare.setPanNo(row[6]);

			busFareList.add(busFare);
		}
		return busFareList;
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private static Date formatDateTime(String date) {
		final SimpleDateFormat dateFormat =  new SimpleDateFormat("dd-mm-YYYY HH:mm:ss");
		Date time = null;
		try {
			time = dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}
	
	/**
	 * 
	 * @param customersList
	 */
	public static void writeDataToCsv(List<BusFee> customersList) {
		
		String[] CSV_HEADER = { "Started", "Finished", "Duration","FromStopId", 
				"ToStopId","ChargeAmount","CompanyId","BusID","PAN","Status" };

		try (
				Writer writer = Files.newBufferedWriter(Paths.get("src/main/resources/trips.csv"));	
				
				CSVWriter csvWriter = new CSVWriter(writer,
			                CSVWriter.DEFAULT_SEPARATOR,
			                CSVWriter.NO_QUOTE_CHARACTER,
			                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
			                CSVWriter.DEFAULT_LINE_END);
			)
		{			
			csvWriter.writeNext(CSV_HEADER);
 
			for (BusFee customer : customersList) {
				String[] data = {
						customer.getStartDate().toString(),
						customer.getEndDate().toString(),
						customer.getDuration().toString(), customer.getFromStopId(), customer.getEndStopId(),
						String.valueOf(customer.getChargeAmount()), customer.getCompanyId(), customer.getBusID(),
						customer.getPanNo(), customer.getStatus().toUpperCase()
						};
				
				csvWriter.writeNext(data);
			}			
			System.out.println("Write CSV using CSVWriter successfully!");
		}catch (Exception e) {
			System.out.println("Writing CSV error!");
			e.printStackTrace();
		}
	}
}
