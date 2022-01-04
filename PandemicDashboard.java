/* 
 * TCSS 143 - Autumn 2021
 * Instructor: Raghavi Sakhpal
 * Driver program that displays information about Covid
 */
 
import java.util.*;
import java.io.*;

/**
 * This program reads countries from cvs file and the data for 
 * each country regarding covid it then provides options for the user
 * to choose from in terms of what information they want displayed.
 * Finally it displays the asked for information
 *
 * @author Amtoj Kaur amtojk@uw.edu
 * @version 26 November 2021
 */

public class PandemicDashboard {

   /**
    * Driver method for the Pandemic Dashboard, it creates a list for
    * covid data given by the cvs file, reads in the file, and then
    * implements methods to make the program runnable.
    * 
    * @param String[] theArgs is a default parameter for the main method.
    */
   public static void main(String[] theArgs) throws FileNotFoundException{
      
      //List to store covid cases from cvs file 
      //Filled using fillCovidCaseList() method
      List<CovidCase> caseList = new ArrayList<CovidCase>(fillCovidCaseList("owid-covid-data.csv"));
      
      boolean gameCont = true;  //game is continuing.
      
      while (gameCont) {
         displayDashboard();
         Scanner console = new Scanner(System.in);
         int in = console.nextInt();
         
         if (in == 8) {      //ends program and stops loop
            System.out.println("You have chosen to exsit the program.");
            System.out.println("Program Ended.");
            gameCont = false;
         } else {
            if (in == 1) {    //Covid Case by Countries
               Collections.sort(caseList, new covidCaseComparator());
               displayCovidCases(caseList);
            } else if (in == 2) {      //Covid Case by Deaths
               Collections.sort(caseList, new covidDeathComparator());
               displayCovidCases(caseList);
            } else if (in == 3) {      //Covid Cases by total cases
               Collections.sort(caseList, new covidCaseComparator());
               displayCovidCases(caseList);
            } else if (in == 4) {      //min/max of total deaths
               Collections.sort(caseList, new covidDeathComparator());
               displayMinMaxCases(caseList);
            } else if (in == 5) {      //min/max of total cases
               Collections.sort(caseList, new covidCaseComparator());
               displayMinMaxDeaths(caseList);
               
            } else if (in == 6) {      //search country/report mortality rate   
               System.out.println("Enter a country: ");
               Collections.sort(caseList);
               String outCountry = console.next();
               //bianary search
               int i = Collections.binarySearch(caseList,
                                          new CovidCase(outCountry, 0.0, 0.0));
               if(i >= 0) {  
                  System.out.println("Covid Case Data:");
                  System.out.println("Country " + outCountry);
                  System.out.println("Total cases: " + 
                                  caseList.get(i).getCases());
                  System.out.println("Total deaths: " + 
                                  caseList.get(i).getDeaths());
                  System.out.format("\nMortality Rate: %.2f%c \n", 
                                  caseList.get(i).getMortalityRate(), 
                                  '%');
               } else {
                  System.out.println("The country you input doesn't exist.");
               }
               
            } else if (in == 7) {      //top 10 country mortality rate
               Collections.sort(caseList, new covidMortalityComparator());
               System.out.format("%-40S%-30S%-20S%S%n","COUNTRY", 
                              "TOTAL CASES", "TOTAL DEATHS", "MORTALITY");
               System.out.println("--------------------------------" +
                         "-----------------------------------------" +
                         "--------------------------");
               displayMortRate(caseList, 0);
            }   
         }
      }
   }
   
   /**
    * fillCovidCaseList reads data from the file and creates
    * instance of CovidCase to add to List, throws exception if
    * file not found
    *
    * @param String fileName
    * @return List<CovidCase> caseList
    */
   public static List<CovidCase> fillCovidCaseList(String fileName) 
                                       throws FileNotFoundException {
      List<CovidCase> caseList = new ArrayList<CovidCase>();
      Scanner sc = new Scanner(new File(fileName));
      sc.nextLine();
      String date = "2021-11-04";
      while(sc.hasNextLine()) {
         String s = sc.nextLine();
         String[] data = s.split(",");
         double totalCases = 0.0;
         double totalDeaths = 0.0;
         if(data[3].equals(date)) {
            String country = data[2];
            if(!(country.equalsIgnoreCase("World")) && 
               !(country.equalsIgnoreCase("Asia")) &&
               !(country.equalsIgnoreCase("Upper Middle Income")) &&
               !(country.equalsIgnoreCase("Lower Middle Income")) &&
               !(country.equalsIgnoreCase("Europe")) &&
               !(country.equalsIgnoreCase("South America")) &&
               !(country.equalsIgnoreCase("North America")) &&
               !(country.equalsIgnoreCase("European Union")) &&
               !(country.equalsIgnoreCase("High Income"))) {
               
               if(!data[4].isEmpty()){ 
                  totalCases = Double.parseDouble(data[4]);
               }
               if(!data[7].isEmpty()){
                  totalDeaths = Double.parseDouble(data[7]);
               }
               caseList.add(new CovidCase(country, totalCases, totalDeaths));
            }
         }
      }
      return caseList;   
   }

   /**
    * DisplayDashboard displays options to the dashboard after
    * each loop throught the while loop.
    */
   public static void displayDashboard() {
      System.out.println("Dashboard for Reporting Covid Cases");
      System.out.println("-----------------------------------");
      System.out.println("Select one of the following options:");
      System.out.println("1.Report (Display) Covid Cases by" +
                     "Countries.");
      System.out.println("2.Report (Display) Covid Cases by Total" +
                     "Deaths (decreasing order).");
      System.out.println("3.Report (Display) Covid Cases by Total" +
                     "Cases (decreasing order).");
      System.out.println("4.Report Countries with minimum and" +
                     "maximum number of Total Deaths.");
      System.out.println("5.Report Countries with minimum and" +
                     "maximum number of Total Cases.");
      System.out.println("6.Search for a Country and report their" +
                     "Mortality Rate (death-to-case %).");
      System.out.println("7.Report (Display) top 10 countries by" +
                     "their Mortality Rate (decreasing order).");
      System.out.println("8.Exit from the program!");
   }
   
   /**
    * displayCovidCase displays covid cases in sorted order
    * 
    * @param List<CovidCase> caseList
    */
   public static void displayCovidCases(List<CovidCase> caseList) {
      System.out.format("%-40S%-30S%-20S\n","COUNTRY", 
                              "TOTAL CASES", "TOTAL DEATHS");
      System.out.println("------------------------------------" +
                         "-------------------------------------" +
                         "--------------------------");
      for(int i = 0; i < caseList.size(); i++) {
         System.out.format("%-40S%-30S%-20S\n", caseList.get(i).getCountry(),
                            caseList.get(i).getCases(),
                            caseList.get(i).getDeaths());
      }

   }
   /**
    * displayMinMaxCases displays the min and max number of cases 
    * based on the total cases
    * 
    * @param List<CovidCase> caseList
    */
   public static void displayMinMaxCases(List<CovidCase> caseList) {
      System.out.println("Countries with min cases: ");
      double d = caseList.get(caseList.size() - 1).getCases();
      double min = locateMinCase(caseList, caseList.size(), d);
      printMinCases(caseList, caseList.size(), min);
      
      System.out.println("Countries with max cases: ");
      double max = locateMaxCase(caseList, caseList.size(), d);
      printMaxDeaths(caseList, caseList.size(), max);
   }
   
   /**
    * displayMinMaxCases displays the min and max number of cases 
    * based on the total cases
    * 
    * @param List<CovidCase> caseList
    */
   public static void displayMinMaxDeaths(List<CovidCase> caseList) {
      System.out.println("Countries with min deaths: ");
      double d = caseList.get(caseList.size() - 1).getDeaths();
      double min = locateMinDeaths(caseList, caseList.size(), d);
      printMinDeaths(caseList, caseList.size(), min);
      
      System.out.println("Countries with max deaths: ");
      double max = locateMaxDeaths(caseList, caseList.size(), d);
      printMaxDeaths(caseList, caseList.size(), max);
   }
   
   /**
    * displayMortRate displays (recursively) the information about
    * top ten countries with the highest mortality rate due to covid.
    *
    * @param List<CovidCase> caseList
    * @param int i which is an index starting at 0 and decrementing
    */
   public static void displayMortRate(List<CovidCase> caseList, int i) {
      if(i > 9) {
         System.out.println();
      } else {
         System.out.format("%-40S%-30S%-20S%.2f%c%n",
                        caseList.get(i).getCountry(),
                        caseList.get(i).getCases(),
                        caseList.get(i).getDeaths(),
                        caseList.get(i).getMortalityRate(),
                        '%');
         displayMortRate(caseList, i + 1);
      }
   }
   
   /**
    * locateMinCase is a private method to locate the min number
    * of cases of all the countries available
    * 
    * @ List<CovidCase> caseList
    * @ param int i starts at end of list and deccrements w/recursion
    * @ param double min updates constantly
    * @ return double min
    */
   private static double locateMinCase(List<CovidCase> caseList, 
                                 int i, double min) {
      if(i < 1) {
         return min;
      } else { 
         if(caseList.get(i - 1).getCases() < min) {
            min = caseList.get(i - 1).getCases();
         }
         return locateMinCase(caseList, i - 1, min);
      }
   }
   
   /**
    * locateMinDeaths is a private method to locate the min number
    * of deaths of all the countries available
    * 
    * @ List<CovidCase> caseList
    * @ param int i starts at end of list and deccrements w/recursion
    * @ param double min updates constantly
    * @ return double min
    */
   private static double locateMinDeaths(List<CovidCase> caseList,
                                    int i, double min) {
      if(i < 1) {
         return min;
      } else { 
         if(caseList.get(i - 1).getDeaths() < min) {
            min = caseList.get(i - 1).getDeaths();
         }
         return locateMinDeaths(caseList, i - 1, min);
      }
   }
   
   /**
    * locateMaxCases is a private method to locate the max number
    * of cases of all the countries available
    * 
    * @ List<CovidCase> caseList
    * @ param int i starts at end of list and deccrements w/recursion
    * @ param double max updates constantly
    * @ return double max
    */
   private static double locateMaxCase(List<CovidCase> caseList, 
                                 int i, double max) {
      if(i < 1) {
         return max;
      } else { 
         if(caseList.get(i - 1).getCases() > max) {
            max = caseList.get(i - 1).getCases();
         }
         return locateMaxCase(caseList, i - 1, max);
      }
   }
   
   /**
    * locateMaxDeaths is a private method to locate the max number
    * of deaths of all the countries available
    * 
    * @ List<CovidCase> caseList
    * @ param int i starts at end of list and deccrements w/recursion
    * @ param double max updates constantly
    * @ return double max
    */
   private static double locateMaxDeaths(List<CovidCase> caseList,
                                 int i, double max) {
      if(i < 1) {
         return max;
      } else { 
         if(caseList.get(i - 1).getDeaths() > max) {
            max = caseList.get(i - 1).getDeaths();
         }
      return locateMaxDeaths(caseList, i - 1, max);
      }
   }
   
   /**
    * printMinCase is a private method iterates through the list
    * to display countries w/ min number of cases
    * 
    * @ List<CovidCase> caseList
    * @ param int i starts at end of list and deccrements w/recursion
    * @ param double min updates constantly
    * @ return double min
    */
   private static void printMinCases(List<CovidCase> caseList, int i,
                              double min) {
      if(i < 1) {
         System.out.println();
      } else {
         if(caseList.get(i - 1).getCases() == min) {
            System.out.format("Country: %s\nTotalCases: %s\n" +
                           "TotalDeaths: %s\n",
                           caseList.get(i - 1).getCountry(),
                           caseList.get(i - 1).getCases(),
                           caseList.get(i - 1).getDeaths());
         }
         printMinCases(caseList, i - 1, min);
      }
   }
   
   /**
    * printMinDeaths is a private method iterates through the list
    * to display countries w/ min number of death
    * 
    * @ List<CovidCase> caseList
    * @ param int i starts at end of list and deccrements w/recursion
    * @ param double min updates constantly
    * @ return double min
    */
   private static void printMinDeaths(List<CovidCase> caseList, int i,
                              double min) {
      if(i < 1) {
         System.out.println();
      } else {
         if(caseList.get(i - 1).getDeaths() == min) {
            System.out.format("Country: %s\nTotalCases: %s\n" +
                           "TotalDeaths: %s\n",
                           caseList.get(i - 1).getCountry(),
                           caseList.get(i - 1).getCases(),
                           caseList.get(i - 1).getDeaths());
         }
         printMinDeaths(caseList, i - 1, min);
      }
   }
   
   /**
    * printMaxCases is a private method iterates through the list
    * to display countries w/ max number of cases
    * 
    * @ List<CovidCase> caseList
    * @ param int i starts at end of list and deccrements w/recursion
    * @ param double max updates constantly
    * @ return double max
    */
   private static void printMaxCases(List<CovidCase> caseList, int i,
                              double max) {
      if( i < 1) {
         System.out.println();
      } else {
         if(caseList.get(i - 1).getCases() == max) {
            System.out.format("Country: %s\nTotalCases: %s\n" +
                           "TotalDeaths: %s\n",
                           caseList.get(i - 1).getCountry(),
                           caseList.get(i - 1).getCases(),
                           caseList.get(i - 1).getDeaths());
         }
         printMaxCases(caseList, i - 1, max);
      }
   }
   
    /**
    * printMaxDeaths is a private method iterates through the list
    * to display countries w/ max number of deaths
    * 
    * @ List<CovidCase> caseList
    * @ param int i starts at end of list and deccrements w/recursion
    * @ param double max updates constantly
    * @ return double max
    */
   private static void printMaxDeaths(List<CovidCase> caseList, int i,
                              double max) {
      if( i < 1) {
         System.out.println();
      } else {
         if(caseList.get(i - 1).getDeaths() == max) {
            System.out.format("Country: %s\nTotalCases: %s\n" +
                           "TotalDeaths: %s\n",
                           caseList.get(i - 1).getCountry(),
                           caseList.get(i - 1).getCases(),
                           caseList.get(i - 1).getDeaths());
         }
         printMaxDeaths(caseList, i - 1, max);
      }
   }
}
