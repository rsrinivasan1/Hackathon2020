import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.io.File;

public class Parse {
    public static void main(String[] args) {
		//required readers to parse text file data
        FileReader fReader;
		BufferedReader bReader = null;
		//variable for each line in text file
        String nextLine = null;

		//create new file if it does not already exist
		File newData = new File("positive.json");
		File newData2 = new File("deceased.json");
		if (!newData.exists()) {
			try {
				newData.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!newData2.exists()) {
			try {
				newData2.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		FileWriter fWriter;
		FileWriter fWriter2;
		BufferedWriter bWriter = null;
		BufferedWriter bWriter2 = null;
		int totalBlackCases = 0;
		int totalWhiteCases = 0;
		int totalAsianCases = 0;
		int totalHispanicCases = 0;
		int totalBlackDeaths = 0;
		int totalWhiteDeaths = 0;
		int totalAsianDeaths = 0;
		int totalHispanicDeaths = 0;

		//initialize statistics variables
		int mostDeaths = 0;
		int mostCases = 0;
		int mostBlackDeaths = 0;
		int mostWhiteDeaths = 0;
		int mostBlackCases = 0;
		int mostWhiteCases = 0;
		String stateMostDeaths = null;
		String stateMostCases = null;
		String stateMostBlackDeaths = null;
		String stateMostWhiteDeaths = null;
		String stateMostBlackCases = null;
		String stateMostWhiteCases = null;

		//try-catch statement for possible errors
		try {
			fReader = new FileReader("race.csv");
			bReader = new BufferedReader(fReader);
			fWriter = new FileWriter(newData);
			fWriter2 = new FileWriter(newData2);
			bWriter = new BufferedWriter(fWriter);
			bWriter2 = new BufferedWriter(fWriter2);

			//start writing to json file
			bWriter.write("[" + "\n");
			bWriter2.write("[" + "\n");
			//skips over first non-data line
			bReader.readLine();

			//iterates through all lines and splits by comma
			while ((nextLine = bReader.readLine()) != null) {
				String[] items = nextLine.split(",", -1);
				
				//if a field is empty, insert N/A in its place
				for (int i = 0; i < items.length; i++) {
					if (items[i].equals(""))
						items[i] = "0";
				}

				//initialize all important information in variables
				String state = items[1];
				String posWhite = items[9];
				String posBlack = items[10];
				String posAsian = items[12];
				String posMultiracial = items[15];
				String posHispanic = items[19];
				String posUnknown = items[21];
				String posTotal = items[8];
				String decWhite = items[24];
				String decBlack = items[25];
				String decAsian = items[27];
				String decMultiracial = items[30];
				String decHispanic = items[34];
				String decUnknown = items[36];
				String decTotal = items[23];

				totalWhiteCases += Integer.parseInt(posWhite);
				totalBlackCases += Integer.parseInt(posBlack);
				totalAsianCases += Integer.parseInt(posAsian);
				totalHispanicCases += Integer.parseInt(posHispanic);
				totalWhiteDeaths += Integer.parseInt(decWhite);
				totalBlackDeaths += Integer.parseInt(decBlack);
				totalAsianDeaths += Integer.parseInt(decAsian);
				totalHispanicDeaths += Integer.parseInt(decHispanic);
				
				//update statistics variables with every iteration
				if (!posTotal.equals("0")) {
					if (Integer.parseInt(posTotal) > mostCases) {
						mostCases = Integer.parseInt(posTotal);
						stateMostCases = state;
					}
				}
				if (!decTotal.equals("0")) {
					if (Integer.parseInt(decTotal) > mostDeaths) {
						mostDeaths = Integer.parseInt(decTotal);
						stateMostDeaths = state;
					}
				}
				if (!posWhite.equals("0")) {
					if (Integer.parseInt(posWhite) > mostWhiteCases) {
						mostWhiteCases = Integer.parseInt(posWhite);
						stateMostWhiteCases = state;
					}
				}
				if (!posBlack.equals("0")) {
					if (Integer.parseInt(posBlack) > mostBlackCases) {
						mostBlackCases = Integer.parseInt(posBlack);
						stateMostBlackCases = state;
					}
				}
				if (!decWhite.equals("0")) {
					if (Integer.parseInt(decWhite) > mostWhiteDeaths) {
						mostWhiteDeaths = Integer.parseInt(decWhite);
						stateMostWhiteDeaths = state;
					}
				}
				if (!decBlack.equals("0")) {
					if (Integer.parseInt(decBlack) > mostBlackDeaths) {
						mostBlackDeaths = Integer.parseInt(decBlack);
						stateMostBlackDeaths = state;
					}
				}
				
				//write info to json
				bWriter.write("  {" + "\n");
				bWriter.write("    \"state\": \"" + state + "\",\n");
				bWriter.write("    \"white\": " + posWhite + ",\n");
				bWriter.write("    \"black\": " + posBlack + ",\n");
				bWriter.write("    \"asian\": " + posAsian + ",\n");
				bWriter.write("    \"multiracial\": " + posMultiracial + ",\n");
				bWriter.write("    \"hispanic\": " + posHispanic + ",\n");
				bWriter.write("    \"unknown\": " + posUnknown + ",\n");
				bWriter.write("    \"total\": " + posTotal + "\n");
				bWriter.write("  }," + "\n");				
				bWriter2.write("  {" + "\n");
				bWriter2.write("    \"state\": \"" + state + "\",\n");
				bWriter2.write("    \"white\": " + decWhite + ",\n");
				bWriter2.write("    \"black\": " + decBlack + ",\n");
				bWriter2.write("    \"asian\": " + decAsian + ",\n");
				bWriter2.write("    \"multiracial\": " + decMultiracial + ",\n");
				bWriter2.write("    \"hispanic\": " + decHispanic + ",\n");
				bWriter2.write("    \"unknown\": " + decUnknown + ",\n");
				bWriter2.write("    \"total\": " + decTotal + "\n");
				bWriter2.write("  }," + "\n");
			}
			//finish json
			bWriter.write("]");
			bWriter2.write("]");

			//close the reader and writer
			bReader.close();
			bWriter.close();
			bWriter2.close();

			//print statistics
			System.out.println("The state with the most total recorded cases was " + stateMostCases + " with " + mostCases + " cases");
			System.out.println("The state with the most total recorded deaths was " + stateMostDeaths + " with " + mostDeaths + " deaths");
			System.out.println("The state with the most total recorded cases of white people was " + stateMostWhiteCases + " with " + mostWhiteCases + " cases");
			System.out.println("The state with the most total recorded deaths of white people was " + stateMostWhiteDeaths + " with " + mostWhiteDeaths + " deaths");
			System.out.println("The state with the most total recorded cases of black people was " + stateMostBlackCases + " with " + mostBlackCases + " cases");
			System.out.println("The state with the most total recorded deaths of black people was " + stateMostBlackDeaths + " with " + mostBlackDeaths + " deaths");

			System.out.println("Proportion of white people who had corona and died: " + ((1.0 * totalWhiteDeaths) / totalWhiteCases));
			System.out.println("Proportion of black people who had corona and died: " + ((1.0 * totalBlackDeaths) / totalBlackCases));
			System.out.println("Proportion of Asian people who had corona and died: " + ((1.0 * totalAsianDeaths) / totalAsianCases));
			System.out.println("Proportion of Hispanic people who had corona and died: " + ((1.0 * totalHispanicDeaths) / totalHispanicCases));

		} catch (Exception e) {
            e.printStackTrace();
		}
    }
}
