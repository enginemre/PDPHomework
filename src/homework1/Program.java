package homework1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {
		readFile(args);
	}

	/**
	 * verilen .java dosyasýndan heceler alýnýyor.
	 * 
	 * 
	 * @param args ilk eleman dosya adý olmalý
	 * 
	 */
	public static void readFile(String[] arguments) {
		// Parametre sayýsý kontrol ediliyor.
		if (arguments.length > 0) {
			try {
				String currentPath = System.getProperty("user.dir");
				String targetPath = currentPath + "/" + arguments[0];
				FileInputStream fileStream = new FileInputStream(targetPath);
				Scanner sc = new Scanner(fileStream);
				// Dosya sonuna kadar okuma yapýlyor.
				while (sc.hasNextLine()) {
					// satýrýn baþýndaki ve sonundali boþluklar kaldýrýlýyor.
					String line = sc.nextLine().strip();
					Lexical.analyzeLine(line);

				}
				sc.close();
				printResult();
			} catch (Exception e) {
				if (e instanceof FileNotFoundException) {
					System.out.println("Dosya Bulunamadý\nLütfen dosyanýzý dist klasörü içerisine atýnýz.");
				} else
					e.printStackTrace();
			}
		} else
			System.out.println("Lütfen dosya ismini girdiðinizden emin olunuz.");
	}

	public static void printResult() {
		System.out.println("Operatör Bilgisi : ");
		System.out.println("\tTekli Operatör sayýsý : " + Lexical.unaryOP);
		System.out.println("\tÝkli Operatör sayýsý : " + Lexical.binaryOP);
		System.out.println("\tSayýsal Operatör sayýsý : " + Lexical.arithmeticOP);
		System.out.println("\tÝliþkisel Operatör sayýsý : " + Lexical.relationalOP);
		System.out.println("\tMantýksal Operatör sayýsý : " + Lexical.logicalOP);
		System.out.println("Operand Bilgisi : ");
		System.out.println("\tToplam Operand sayýsý : " + Lexical.operands);
	}

}
