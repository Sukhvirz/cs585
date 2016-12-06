package cs585_prj;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class StaticCodeAnalyzer {

	public static void main(String[] args) {
		System.out.print("Enter the path of the folder :");
		Scanner sc = new Scanner(System.in);
		 String path = sc.nextLine();
		File folderPath = new File(path);
		StaticCodeAnalyzer analyzer = new StaticCodeAnalyzer();
		try {
			String[] filesPath = analyzer.retriveAllJavaFiles(folderPath);
			analyzer.parseJavaFiles(filesPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String[] retriveAllJavaFiles(File folderPath) throws IOException {

		File[] listOfFiles = folderPath.listFiles();
		String line;
		System.out.println("Total Number of Java Files are:");
		int javaFilesCount = 0;
		String[] pathOfFiles = new String[listOfFiles.length];

		for (File file : listOfFiles) {
			if (file.isFile()) {
				if (file.getName().contains(".java")) {
					pathOfFiles[javaFilesCount] = file.getPath();
					javaFilesCount++;
					System.out.println(file.getName() + " " + file.getPath());

				}
			}
		}

		System.out.println("Total java files are =" + " " + javaFilesCount);
		System.out.println();
		return pathOfFiles;
	}

	void parseJavaFiles(String files[]) throws IOException {
		StringBuffer stringBuffer = new StringBuffer();
		int totalclasses=0,totalImports=0,totalFunctions=0, totalLines=0;

		for (String filePath : files) {
			if (filePath == null)
				continue;
			else {
				
				String getLine;
				String[] splitFilePath = filePath.split("[\\W]");
				System.out.println("Class name is =" + splitFilePath[splitFilePath.length - 2]);
				stringBuffer.append("Class name is =" + splitFilePath[splitFilePath.length - 2]);
				FileReader fileReader = new FileReader(filePath);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				int classCountInAFile = 0, functionCountInClass = 0, interfaceCount = 0, numberOfLines = 0,
						unusedVariableCount = 0, packageCountInAFile = 0, interfaceFunctionCount = 0;
				HashMap<String, Integer> variableDetails = new HashMap<String, Integer>();

				while ((getLine = bufferedReader.readLine()) != null) {
					numberOfLines++;
					List<Character> listOfCharctersInLine = new ArrayList<Character>();
					List<String> listOfWordsInLine = new ArrayList<String>();
					String[] lineToWords = getLine.split("\\W");
					char[] lineToCharacter = getLine.toCharArray();
					if (lineToCharacter != null) {
						for (char c : lineToCharacter) {

							if (c == ' ') {
								continue;
							} else {
								listOfCharctersInLine.add(c);
							}
						}
					}

					if (lineToWords != null) {
						for (String word : lineToWords) {
							if (variableDetails.containsKey(word)) {
								variableDetails.put(word, variableDetails.get(word) + 1);
							}

							listOfWordsInLine.add(word);

						}

					}
					if (listOfWordsInLine.contains("import")) {
						packageCountInAFile++;
					}
					if (listOfWordsInLine.contains("class")) {
						classCountInAFile++;
					}
					if (listOfWordsInLine.contains("interface")) {
						interfaceCount++;
					}

					for (String word : lineToWords) {
						if ((word.equals("int") || word.equals("double") || word.equals("float") || word.equals("long")
								|| word.equals("short") || word.equals("char") || word.equalsIgnoreCase("boolean")
								|| word.equals("String")) && listOfCharctersInLine.contains(';')) {
							String variableName = listOfWordsInLine.get(listOfWordsInLine.indexOf(word) + 1);
							if (variableDetails.containsKey(variableName)) {
								variableDetails.put(variableName, variableDetails.get(variableName) + 1);
							} else {
								variableDetails.put(variableName, 1);
							}

						}

					}

					if (listOfCharctersInLine.size() > 1)
						if (((listOfCharctersInLine.get(listOfCharctersInLine.size() - 1) == '{')
								&& (listOfCharctersInLine.get(listOfCharctersInLine.size() - 2) == ')')
								&& (!listOfCharctersInLine.contains(';'))&& (!listOfWordsInLine.contains("catch")))
								|| ((listOfCharctersInLine.get(listOfCharctersInLine.size() - 1) == ')')
										&& (!listOfCharctersInLine.contains(';'))&& (!listOfWordsInLine.contains("catch")))
								|| ((listOfCharctersInLine.contains(')')) && (listOfCharctersInLine.contains('('))
										&& (!listOfWordsInLine.contains("catch"))
										&& (listOfWordsInLine.contains("throws"))
										&& (!listOfCharctersInLine.contains(';')))) {
							if (!listOfWordsInLine.contains("if") && !listOfWordsInLine.contains("while")
									&& !listOfWordsInLine.contains("for")) {
								functionCountInClass++;
							}

						}
				

					if (interfaceCount > 0) {
						if (listOfCharctersInLine.contains(';') && listOfCharctersInLine.contains(')')
								&& listOfCharctersInLine.contains('(')) {
							interfaceFunctionCount++;
						}
					}

				}
				totalclasses=totalclasses+classCountInAFile;
				totalFunctions=totalFunctions+functionCountInClass;
				totalImports=totalImports+packageCountInAFile;
				totalLines=totalLines+numberOfLines;
				if (interfaceCount > 0) {
					stringBuffer.append("\n");
					System.out.println("Interface are " + interfaceCount);
					System.out.println("Number of functions in an interface are " + interfaceFunctionCount);
					stringBuffer.append("Interface are "+interfaceCount);
					stringBuffer.append("\n");
					stringBuffer.append("Number of functions in an interface are " + interfaceFunctionCount);
					stringBuffer.append("\n");
					System.out.println("Number of Lines Of Code " + numberOfLines);
					stringBuffer.append("Number of Lines Of Code " + numberOfLines);
					stringBuffer.append("\n");
					System.out.println("Number of Packages imported " + packageCountInAFile);
					stringBuffer.append("Number of Packages imported " + packageCountInAFile);
					stringBuffer.append("\n");
					stringBuffer.append("\n");
					
				}

				if (classCountInAFile > 0) {

					System.out.println();
					stringBuffer.append("\n");
					System.out.println("Classes are = " + classCountInAFile);
					stringBuffer.append("Classes are = " + classCountInAFile);
					stringBuffer.append("\n");
					System.out.println("Functions are =" + functionCountInClass);
					stringBuffer.append("Functions are = " + functionCountInClass);
					stringBuffer.append("\n");
					System.out.println("Number of Lines Of Code =" + numberOfLines);
					stringBuffer.append("Number of Lines Of Code =" + numberOfLines);
					stringBuffer.append("\n");
					System.out.println("Number of Packages imported = " + packageCountInAFile);
					stringBuffer.append("Number of Packages imported = " + packageCountInAFile);
					stringBuffer.append("\n");
					System.out.print("Unused variables are =");
					stringBuffer.append("Unused variables are =");
					
					for (String string : variableDetails.keySet()) {
						if (variableDetails.get(string) == 1) {
							
							System.out.print(string + ",");
							stringBuffer.append(string+ " ,");
							//stringBuffer.append("\n");

						}

					}
					stringBuffer.append("\n");
					stringBuffer.append("\n");
					System.out.println();
					System.out.println();
				}
				if (classCountInAFile == 0 && interfaceCount == 0) {
					stringBuffer.append("\n");
					System.out.println("invalid class");
					stringBuffer.append("invalid class !!! ");
					stringBuffer.append("\n\n");
				}

			}
			
			
		}
		System.out.println("total classes are ="+totalclasses);
		stringBuffer.append("total classes are ="+totalclasses);
		stringBuffer.append("\n");
		System.out.println("totalFunctions are ="+totalFunctions);
		stringBuffer.append("totalFunctions are ="+totalFunctions);
		stringBuffer.append("\n");
		System.out.println("total Package Imports are ="+ totalImports);
		stringBuffer.append("total Package Imports are ="+ totalImports);
		stringBuffer.append("\n");
		System.out.println("Total Number of Lines are ="+ totalLines);
		stringBuffer.append("Total Number of Lines are ="+ totalLines);
		stringBuffer.append("\n");
		System.out.println(totalImports);
		System.out.println(totalLines);
		String data=stringBuffer.toString();
		System.out.println(data);
		try{
				String filename = "C:/cs585/" + "report.txt";
		File file = new File(filename);
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(data);
		fileWriter.flush();
		fileWriter.close();
		}
		catch(FileNotFoundException fnfe){
			fnfe.printStackTrace();
		}
	}
}
