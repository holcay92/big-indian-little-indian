import java.util.ArrayList;
import java.util.Hashtable;

public abstract class Number {
	private String numberRepresentation;
	private String byteOrdering;
	private String binaryRepresentation;
	private String hexaRepresentation;
	private String finalHexa;
	
	public Number() {
		this.numberRepresentation = null;
		this.byteOrdering = null;
		this.binaryRepresentation = "";
		this.hexaRepresentation = "0x";
		this.finalHexa = "";
	}
	
	public int pow(int base, int power) {
		int result = 1;
		if(power == 0) {
			return 1;
		}
		for(int i = 0; i < power; i++) {
			result *= base;
		}
		return result;
	}
	
	public int findFourBit(String fourBit) {
		int total = 0;
		for(int i = 0; i < 4; i++) {
			String bit = String.valueOf(fourBit.charAt(i));
			total = total + (Integer.valueOf(bit) * this.pow(2, i));
		}
		return total;
	}
	
	public String twosComplement() {
		Hashtable<String, String> complements = new Hashtable<String, String>();
		complements.put("1", "0");
		complements.put("0", "1");
		ArrayList<String> binary = new ArrayList<String>();
		String newBinary = "";
		int length = this.getBinaryRepresentation().length();
		for(int i = 0; i < length; i++) {
			binary.add(0, complements.get(String.valueOf(this.getBinaryRepresentation().charAt(i))));
		}
		boolean check = false;
		for(String i: binary) {
			if(check) {
				newBinary = i + newBinary;
				continue;
			}
			if(i.equals("0")) {
				check = true;
			}
			newBinary = complements.get(i) + newBinary;	
		}
		return newBinary;
	}
	
	public void createFinalHexa() {
		ArrayList<String> hexa = new ArrayList<String>();	
		String twoBits = "";
		int counter = 0;
		for(int i = 2; i < (this.getHexaRepresentation().length()); i++) {
			twoBits = twoBits + String.valueOf(this.getHexaRepresentation().charAt(i));
			counter++;
			if(counter == 2) {
				if(this.byteOrdering.equals("l")) {
					hexa.add(0, twoBits);
				}else if(this.byteOrdering.equals("b")) {
					hexa.add(twoBits);
				}
				twoBits = "";
				counter = 0;
			}
		}
		for(String i: hexa) {
			this.setFinalHexa(this.getFinalHexa() + i + " ");
		}
	}

	public String getNumberRepresentation() {
		return numberRepresentation;
	}

	public void setNumberRepresentation(String numberRepresentation) {
		this.numberRepresentation = numberRepresentation;
	}

	public String getByteOrdering() {
		return byteOrdering;
	}

	public void setByteOrdering(String byteOrdering) {
		this.byteOrdering = byteOrdering;
	}

	public String getBinaryRepresentation() {
		return binaryRepresentation;
	}

	public void setBinaryRepresentation(String binaryRepresentation) {
		this.binaryRepresentation = binaryRepresentation;
	}

	public String getHexaRepresentation() {
		return hexaRepresentation;
	}

	public void setHexaRepresentation(String hexaRepresentation) {
		this.hexaRepresentation = hexaRepresentation;
	}

	public String getFinalHexa() {
		return finalHexa;
	}

	public void setFinalHexa(String finalHexa) {
		this.finalHexa = finalHexa;
	}
}
