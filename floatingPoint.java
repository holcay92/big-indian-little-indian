import java.util.ArrayList;
import java.util.Hashtable;

public class FloatPoint extends Number{
	double number;
	String exact;
	String fraction;
	String signBit;
	String byteOrdering;
	int FPsize;
	Hashtable<Integer, Integer> byteExp;
	Hashtable<Integer, String> hexaNums;
	
	public FloatPoint(String numberRepresentation, String byteOrdering, int FPsize) {
		this.hexaNums = new Hashtable<Integer, String>();
		this.putDict();
		this.setNumberRepresentation(numberRepresentation);
		this.setByteOrdering(byteOrdering);
		this.number = Double.valueOf(this.getNumberRepresentation());
		this.byteOrdering = byteOrdering;
		this.FPsize = FPsize;
		this.exact = this.getNumberRepresentation().substring(0, this.getNumberRepresentation().indexOf("."));
		this.fraction = "0." + this.getNumberRepresentation().substring(this.getNumberRepresentation().indexOf(".") + 1, this.getNumberRepresentation().length());
		this.byteExp = new Hashtable<Integer, Integer>();
		this.signBit = null;
		this.checkSign();
		this.putDictByte();
		this.convertToBinary();
		this.convertToHexa(this.getBinaryRepresentation());
		this.createFinalHexa();
	}
	
	public void putDict() {
		for(int i = 0; i < 16; i++) {
			switch(i) {
				case 10:
					this.hexaNums.put(i, "A");
					break;
				case 11:
					this.hexaNums.put(i, "B");
					break;
				case 12:
					this.hexaNums.put(i, "C");
					break;
				case 13:
					this.hexaNums.put(i, "D");
					break;
				case 14:
					this.hexaNums.put(i, "E");
					break;
				case 15:
					this.hexaNums.put(i, "F");
					break;
				default:
					this.hexaNums.put(i, String.valueOf(i));
					break;
			}
		}
	}
	
	public void putDictByte() {
		int exp = 4;
		for(int i = 1; i < 5; i++) {
			this.byteExp.put(i, exp);
			exp += 2;
		}
	}
	
	public String lastCheck(String binary) {
		String newBinary = binary;
		newBinary = newBinary + "0";
		return newBinary;
	}
	
	public boolean checkSign() {
		if(this.number < 0) {
			this.signBit = "1";
			return true;
		}else {
			this.signBit = "0";
			return false;
		}
	}
	
	public void convertToBinary() {
		int num = Integer.valueOf(this.exact);
		if(this.checkSign()) {
			num = Integer.valueOf(String.valueOf(this.number).substring(0, String.valueOf(this.number).indexOf("."))) * -1;
		}
		while(num != 0) {	
			this.setBinaryRepresentation(String.valueOf(num % 2) + this.getBinaryRepresentation());
			num = num / 2;
		}
		String binary = "";
		double fraction = Double.valueOf(this.fraction);
		while(true) {
			if(fraction == 0.0) {
				binary = "0";
				break;
			}
			fraction *= 2;
			String fractionStr = String.valueOf(fraction);
			binary = binary + fractionStr.charAt(0);
			fraction = Double.valueOf(fractionStr);
			if(fraction == 1.0) {
				break;
			}if(fraction > 1.0) {
				fraction -= 1.0;
			}
		}
		if(this.getBinaryRepresentation().equals("")) {
			this.setBinaryRepresentation("0");
		}
		this.setBinaryRepresentation(this.getBinaryRepresentation() + "." + binary);
		if(Integer.valueOf(this.exact) == 0) {
			String mantissaDenorm = this.findDenormMantissa(binary);
			int EDenorm = (binary.indexOf("1") + 1) * -1;
			String exponentDenorm = this.findBinaryExp(EDenorm);
			String fractionBits = mantissaDenorm.substring(mantissaDenorm.indexOf('.') + 1, mantissaDenorm.length());
			String roundedFractionDenorm = this.roundToNearst(fractionBits, EDenorm + this.findBias());
			String finalBinaryDenorm = this.signBit + exponentDenorm + roundedFractionDenorm;
			finalBinaryDenorm = this.lastCheck(finalBinaryDenorm);
			this.setBinaryRepresentation(finalBinaryDenorm);
			return;
		}
		String mantissa = this.findMantissa(this.getBinaryRepresentation());
		int E = this.getBinaryRepresentation().indexOf('.') - 1;
		String exponent = this.findBinaryExp(E);
		String fractionBits = mantissa.substring(mantissa.indexOf('.') + 1, mantissa.length());
		String roundedFraction = this.roundToNearst(fractionBits, E + this.findBias());
		String finalBinary = this.signBit + exponent + roundedFraction;
		this.setBinaryRepresentation(finalBinary);
	}
	
	public String findDenormMantissa(String binary) {
		return "0." + binary.substring(binary.indexOf("1"), binary.length());
	}
	
	public String findMantissa(String binary) {
		String mantissa = "";
		for(int i = 0; i < binary.length(); i++) {
			if(i == 1) {
				mantissa = mantissa + "." + binary.charAt(i);
				continue;
			}else if('.' == binary.charAt(i)) {
				continue;
			}
			mantissa = mantissa + binary.charAt(i);
		}
		return mantissa;
	}
	
	public int findBias() {
		return this.pow(2, this.byteExp.get(this.FPsize) - 1) - 1;
	}
	
	public String findBinaryExp(int E) {
		String binary = "";
		int exp = E + this.findBias();
		if(exp == 0) {
			for(int i = 0; i < this.byteExp.get(this.FPsize); i++) {
				binary = binary + "0";
			}
			return binary;
		}
		while(exp != 0) {	
			binary = String.valueOf(exp % 2) + binary;
			exp = exp / 2;
		}
		return binary;
	}
	
	public String roundToNearst(String fraction, int exp) {
		String roundedFraction = "";
		switch(exp) {
			case 0:
				roundedFraction = this.denormalized(fraction);
				break;
			default:
				roundedFraction = this.round(fraction);
				break;
		}
		return roundedFraction;
	}
	
	public String denormalized(String fraction) {
		return fraction;
	}
	
	public String round(String fraction) {
		String roundedFraction = "";
		int requiredSizeFrac = (this.FPsize * 8) - (this.byteExp.get(this.FPsize) + 1);
		if((Double.valueOf(this.fraction) == 0.0) & (this.FPsize < 3)) {
			roundedFraction = fraction;
			if(roundedFraction.length() < requiredSizeFrac) {
				while(roundedFraction.length() < requiredSizeFrac) {
					roundedFraction = roundedFraction + "0";
				}
			}else if(roundedFraction.length() > requiredSizeFrac) {
				roundedFraction = roundedFraction.substring(0, requiredSizeFrac);
			}
			return roundedFraction;
		}
		if(Double.valueOf(this.fraction) == 0.0) {
			fraction = fraction.substring(0, fraction.length() - 1);
		}
		Hashtable<String, String> complements = new Hashtable<String, String>();
		complements.put("1", "0");
		complements.put("0", "1");
		ArrayList<String> binaryOfFraction = new ArrayList<String>();
		int length = fraction.length();
		for(int i = 0; i < length; i++) {
			binaryOfFraction.add(0, String.valueOf(fraction.charAt(i)));
		}
		boolean check = false;
		for(String i: binaryOfFraction) {
			if(check) {
				roundedFraction = i + roundedFraction;
				continue;
			}
			if(i.equals("0")) {
				check = true;
			}
			roundedFraction = complements.get(i) + roundedFraction;	
		}
		if(roundedFraction.length() < requiredSizeFrac) {
			while(roundedFraction.length() < requiredSizeFrac) {
				roundedFraction = roundedFraction + "0";
			}
		}else if(roundedFraction.length() > requiredSizeFrac) {
			roundedFraction = roundedFraction.substring(0, requiredSizeFrac);
		}
		return roundedFraction;
	}
	
	public void convertToHexa(String totalBinary) {
		String fourBit = "";
		int length = totalBinary.length();
		for(int i = 0; i < length; i++) {
			fourBit = totalBinary.charAt(i) + fourBit;
			if((i + 1) % 4 == 0) {
				this.setHexaRepresentation(this.getHexaRepresentation() + this.hexaNums.get(this.findFourBit(fourBit)));
				fourBit = "";
			}
		}
	}
	
	public double getNumber() {
		return this.number;
	}
	public void setNumber(double number) {
		this.number = number;
	}
}
