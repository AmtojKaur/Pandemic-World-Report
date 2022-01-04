/* 
 * TCSS 143 - Autumn 2021
 * Instructor: Raghavi Sakhpal
 * Covid Case object program for Project 3
 */
 
import java.util.*;
import java.io.*;

/**
 * Covid case is an object program that recieves countries and covid
 * statistics from the driver program
 *
 * @author Amtoj Kaur amtojk@uw.edu
 * @version 26 November 2021
 */

public class CovidCase implements Comparable<CovidCase> {

   // Instance feilds
   private String country;
   private double totalCases;   // Compiler error for me as a double
   private double totalDeaths;  // Compiler for me as a double

   private double deathToCaseRatio;   // For MortalityRate
   
   /**
    * Constructor method instantiates a COVID case that
    * gets its country name along with its covid statistics
    * such as total cases and total deaths
    * 
    * @param country to set the name of country being searched
    * @param totalCases to set amount of cases in the country
    * @param totalDeaths to set amount of total death in the country
    */
   public CovidCase(String country, double totalCases, double totalDeaths) {
      this.country = country;
      this.totalCases = totalCases;
      this.totalDeaths = totalDeaths;
      
      // checks if either death or caes are zero is so set mort. rate to 0
      // else calculate ratio
      if(totalCases == 0 || totalDeaths == 0) {
         this.deathToCaseRatio = 0.0;
      } else {
         double mortality = totalDeaths / totalCases * 100;
         double mRatio = Math.round(mortality * 100.0) / 100.0;
         this.deathToCaseRatio = mRatio;
      }
   }
   
   // Now get methods because feilds are private
   
   /**
    * Gets country name
    * 
    * @return country name
    */
   public String getCountry() {
      return this.country;
   }
   
   /**
    * Gets total amount of cases in a country
    *  
    * @return total amount of cases
    */
   public double getCases() {
      return this.totalCases;
   }
   
   /**
    * Gets total amount of deaths in a country
    *  
    * @return total amount of deaths
    */
   public double getDeaths() {
      return this.totalDeaths;
   }
   
   /**
    * Gets mortality rate in a country
    *  
    * @return mortality rate
    */
   public double getMortalityRate() {
      return this.deathToCaseRatio;
   }
   
   /**
    * CompareTo() based on name of country in
    * descending alphabeticl order
    * 
    * @param CovidCase c CovidCase object
    * @return integer comparing the current CovidCase with 
    *         the one sent in parameter
    */
   public int compareTo(CovidCase c) {
      return this.getCountry().compareTo(c.getCountry());
   }
}

/**
 * covidCaseComparator compares # of covid cases in each country
 * and impelements Comparator<CovidCase>
 *
 * @author Amtoj Kaur
 * @version 26 November 2021
 */
class covidCaseComparator implements Comparator<CovidCase> {
   /**
    * Compares CovidCases case number to sort in descending order
    * 
    * @param CovidCase c1 to compare to the other CovidCase
    * @param CovidCase c2 to conpare to the first CovidCase
    * @return an integer comparing two CovidCases based on the
    *         total amount of covid cases
    */
   public int compare(CovidCase c1, CovidCase c2) {
      return Double.compare(c2.getCases(), c1.getCases());
   }
}

/**
 * covidDeathComparator compares the country's mortality rate and
 * impelements Comparator<CovidCase>
 *
 * @author Amtoj Kaur
 * @version 26 November 2021
 */
class covidDeathComparator implements Comparator<CovidCase> {
   /**
    * Compares CovidCases deaths to sort in descending order
    * 
    * @param CovidCase c1 to compare to the other CovidCase
    * @param CovidCase c2 to conpare to the first CovidCase
    * @return an integer comparing two CovidCases based on the
    *         amount of deaths from covid within a country
    */
   public int compare(CovidCase c1, CovidCase c2) {
      return Double.compare(c2.getDeaths(), c1.getDeaths());
   }
}

/**
 * CompareMortality compares the country's mortality rate
 * and implements Comparable<CovidCase>
 * @author Amtoj Kaur
 * @version 26 November 2021
 */
class covidMortalityComparator implements Comparator<CovidCase> {
    /**
     * compares death to rate ratio in each country
     *
     * @param CovidCase c1 to compare to the other covid case
     * @param CovidCase c2 to compare to first covid case
     * @return int value sorting the countries in decreasing order
     *          based off of the number of cases
     */
   public int compare(CovidCase c1, CovidCase c2) {
      return Double.compare(c2.getMortalityRate(), c1.getMortalityRate());
   }
}