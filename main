import java.io.BufferedReader; 
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class erdempehlivanlar_haldunhalilolcay_borankanat {
	
	public static Number createObject(String number, String byteOrdering, int FPsize) {
		if(number.contains(".")) {
			return new FloatPoint(number, byteOrdering, FPsize);
		}else if(number.contains("u")) {
			return new UnsignedInt(number, byteOrdering);
		}else {
			return new SignedInt(number, byteOrdering);
		}
	}
	
	public static void main(String[] args) throws IOException{
		Scanner input = new Scanner(System.in);
		ArrayList<String> numbers = new ArrayList<String>();
		String newLine;
		BufferedReader read = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\src\\input.txt"));
		while (( newLine= read.readLine()) != null) {
			numbers.add(newLine);
		}
		read.close();
		
		ArrayList<Number> objNumbers = new ArrayList<Number>();
		System.out.println("Byte ordering : ");
		String byteOrdering = input.nextLine();
		System.out.println("Floating point size : ");
		int FPsize = input.nextInt();
		input.close();
		
		for(String number: numbers) {
			objNumbers.add(createObject(number, byteOrdering, FPsize));
		}
		
        File file = new File(System.getProperty("user.dir") + "\\src\\output.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file, false);
        BufferedWriter bWriter = new BufferedWriter(fileWriter);
        
		for(Number number: objNumbers) {
			bWriter.write(number.getFinalHexa() + "\n");
		}
		bWriter.close();
	}
}
