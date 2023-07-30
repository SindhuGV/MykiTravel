package com.busfare.calculate;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.busfare.data.FileReadWrite;

@Service
public class FareProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(FareProcessor.class);

	@SuppressWarnings("removal")
	public void readTaps() {

		List<TapOnOff> tapOnOffList = new ArrayList<>();		
		List<TripResult> tripResultList = new ArrayList<>();
		LOGGER.debug("Loading  taps.csv file");

		Set<String> calculatedIndexSet = new  HashSet<>();

		try 
		{
			List<String[]> allRows = FileReadWrite.readCSV("taps.csv");
			if(allRows != null && !allRows.isEmpty()) {
				tapOnOffList = CSVUtility.processCSVData(allRows);
			}

			//sort the list based on dateTime parameter
			Collections.sort(tapOnOffList, new Comparator<TapOnOff>() {
				public int compare(final TapOnOff obj1, final TapOnOff obj2) {
					return obj1.getDateTime().compareTo(obj2.getDateTime());
				}
			});

			//calculate fare per ride
			if(tapOnOffList != null && !tapOnOffList.isEmpty()) 
			{
				boolean hangingTxn = false;
				for (TapOnOff tapOnOff : tapOnOffList) 
				{					
					TripResult tripResult = new TripResult();
					String panNo = tapOnOff.getPanNo().trim();
					String tapType = tapOnOff.getTapType().trim();
					Date taponTime = tapOnOff.getDateTime();

					if("ON".equalsIgnoreCase(tapType) ) 
					{
						boolean matchingFound = false;
						for (TapOnOff tapOnOffNext : tapOnOffList) 
						{							
							if(tapOnOffNext.getPanNo().trim().equals(panNo) && "OFF".equals(tapOnOffNext.getTapType().trim())
									&& (tapOnOffNext.getDateTime().compareTo(taponTime)) > 0 
									&& TimeDifferenceWithinRange(tapOnOffNext.getDateTime(), tapOnOffNext.getDateTime())) 
							{								
								tripResult = storeTripData(tapOnOffNext, taponTime, panNo, tapOnOff);

								// status				
								if(!tapOnOff.getStopId().equalsIgnoreCase(tapOnOffNext.getStopId())) {
									tripResult.setStatus("COMPLETED");
									// fetch price									
									Double fare = getFare(tapOnOff.getStopId(), tapOnOffNext.getStopId());
									tripResult.setTripFare(fare);
								}
								else {
									tripResult.setStatus("CANCELLED");
									tripResult.setTripFare(new Double(0));
								}
								matchingFound = true;
								tripResultList.add(tripResult);
								//calculatedIndexSet.add(bus.getId());
								//calculatedIndexSet.add(bus1.getId());
								break;
							}//end of inner if
						}//end of inner for loop
						if (!matchingFound)
						{
							//incomplete trip
							//if(!calculatedIndexSet.contains(tapOn.getId()))
							//{
							tripResult.setStartDate(taponTime);
							tripResult.setEndDate(tapOnOff.getDateTime());													
							tripResult.setDuration(Long.parseLong("0"));								
							tripResult.setFromStopId(tapOnOff.getStopId());
							tripResult.setEndStopId(tapOnOff.getStopId());
							tripResult.setCompanyId(tapOnOff.getCompanyId());
							tripResult.setBusID(tapOnOff.getBusId());
							tripResult.setPanNo(panNo);

							tripResult.setStatus("INCOMPLETE");
							// charge									
							tripResult.setChargeAmount(getMaxCharge(tapOnOff.getStopId()));						
							calculatedIndexSet.add(tapOnOff.getId());
							tripResultList.add(tripResult);
							//}
						}
					}// end of first if  tap type "ON" loop
				}//end of first for loop

			}//end of list if loop
			///write to cvs			
			CSVUtility.writeDataToCsv(tripResultList);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	private TripResult storeTripData(TapOnOff tapOnNext, Date tapOnTime, String panNo, TapOnOff tapOn)
	{
		TripResult tripResult = new TripResult();
		tripResult.setStartDate(tapOnTime);
		tripResult.setEndDate(tapOnNext.getDateTime());

		long secondsBetween = (tapOnNext.getDateTime().getTime() - tapOnTime.getTime()) / 1000;								
		tripResult.setDuration(secondsBetween);

		tripResult.setFromStopId(tapOn.getStopId());
		tripResult.setEndStopId(tapOnNext.getStopId());
		tripResult.setCompanyId(tapOnNext.getCompanyId());
		tripResult.setBusID(tapOnNext.getBusId());
		tripResult.setPanNo(panNo);

		return tripResult;
	}

	private boolean TimeDifferenceWithinRange (Date startDate, Date endDate) {
		LocalDateTime initialTime = LocalDateTime.ofInstant(startDate.toInstant(),
				ZoneId.systemDefault());
		LocalDateTime FinalTime = LocalDateTime.ofInstant(endDate.toInstant(),
				ZoneId.systemDefault());
		long difference = Duration.between(initialTime, FinalTime).getSeconds();
		if(difference <= 1200) {
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param stop1
	 * @param stop2
	 * @return
	 */
	private Double getFare(String beginStop, String endStop)
	{
		Double fare = 0.0;
		for(Charges charges : Charges.values())
		{
			if(beginStop.equalsIgnoreCase(charges.getBeginStop()) && endStop.equalsIgnoreCase(charges.getEndStop()))
			{
				fare = Double.parseDouble(charges.getFare()); 
			}
		}
		return fare;
	}

	/**
	 * 
	 * @param stopId
	 * @return
	 */
	private Double getMaxCharge(String stopId)
	{
		Double fare = 0.0;
		List<Double> chargeList = new ArrayList<>();

		for(Charges charges : Charges.values())
		{
			if(stopId.equalsIgnoreCase(charges.getBeginStop())) 
				chargeList.add(Double.parseDouble(charges.getFare()));
		}
		fare = Collections.max(chargeList);
		return fare;
	}

}
