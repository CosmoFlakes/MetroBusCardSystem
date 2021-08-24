package com.BusSmartCardSystem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CardBalanceCalculator {

	double initialBalance;
	int startPoint;
	int stopPoint;
	String entryTime;
	String dayOfTravel;

	public static boolean validateJourneyPoints(int start, int stop) {
		if (!(start >= 1 && start <= 15 && stop >= 1 && stop <= 15 && start != stop)) {
			return false;
		}
		return true;
	}

	public static boolean validateCardInitialBalance(double initBal) {
		return initBal >= 10.0;
	}

	public static double calculateFareRate(String time) {
		SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
		Date dayStarts, nightStarts, givenTime;
		try {
			dayStarts = parser.parse("6:00");
			nightStarts = parser.parse("23:00");
			givenTime = parser.parse(time);
			if (givenTime.after(nightStarts) || givenTime.before(dayStarts)) {
				return 0.60;
			} else if (givenTime.after(dayStarts) && givenTime.before(nightStarts)) {
				return 0.80;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean isWeekend(int day) {
		if (day >= 1 && day <= 5) {
			return false;
		}
		return true;
	}

	public static double calculateCardBalance(double initialBal, int start, int stop, int day, String time) {
		double finalBalance = 0.0, fare = 0.0, discountedPrice = 0.0;
		if (validateJourneyPoints(start, stop) && validateCardInitialBalance(initialBal)) {

			int distance = Math.abs(stop - start);

			if (distance > 5) {
				fare = 5.0 * calculateFareRate(time);
				discountedPrice = ((distance - 5) * calculateFareRate(time)) * 0.8;
				fare += discountedPrice;
			} else {
				fare = distance * calculateFareRate(time);
			}

			if (isWeekend(day)) {
				fare = fare * 0.90;
			}

			if (fare > 10.0) {
				return -1;
			}
			finalBalance = initialBal - fare;
			return finalBalance;
		}
		return -1;
	}

	public static void main(String args[]) throws Exception {
		double initialBal = Double.parseDouble(args[0]);
		int start, stop, day;
		start = Integer.parseInt(args[1]);
		stop = Integer.parseInt(args[2]);
		day = Integer.parseInt(args[3]);
		String time = args[4];
		double cardBalance = calculateCardBalance(initialBal, start, stop, day, time);
		if (Double.compare(cardBalance, -1.0) != 0)
			System.out.println(cardBalance);
		else
			System.out.println("One or more validations failed!");
	}

}
