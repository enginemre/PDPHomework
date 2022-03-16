package homework1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {
		readFile(args);
	}

	/**
	 * verilen .java dosyas�ndan heceler al�n�yor.
	 * 
	 * 
	 * @param args ilk eleman dosya ad� olmal�
	 * 
	 */
	public static void readFile(String[] arguments) {
		// Parametre say�s� kontrol ediliyor.
		if (arguments.length > 0) {
			try {
				String currentPath = System.getProperty("user.dir");
				String targetPath = currentPath + "/" + arguments[0];
				FileInputStream fileStream = new FileInputStream(targetPath);
				Scanner sc = new Scanner(fileStream);
				// Dosya sonuna kadar okuma yap�lyor.
				while (sc.hasNextLine()) {
					// sat�r�n ba��ndaki ve sonundali bo�luklar kald�r�l�yor.
					String line = sc.nextLine().strip();
					Lexical.analyzeLine(line);

				}
				sc.close();
				printResult();
			} catch (Exception e) {
				if (e instanceof FileNotFoundException) {
					System.out.println("Dosya Bulunamad�\nL�tfen dosyan�z� dist klas�r� i�erisine at�n�z.");
				} else
					e.printStackTrace();
			}
		} else
			System.out.println("L�tfen dosya ismini girdi�inizden emin olunuz.");
	}

	public static void printResult() {
		System.out.println("Operat�r Bilgisi : ");
		System.out.println("\tTekli Operat�r say�s� : " + Lexical.unaryOP);
		System.out.println("\t�kli Operat�r say�s� : " + Lexical.binaryOP);
		System.out.println("\tSay�sal Operat�r say�s� : " + Lexical.arithmeticOP);
		System.out.println("\t�li�kisel Operat�r say�s� : " + Lexical.relationalOP);
		System.out.println("\tMant�ksal Operat�r say�s� : " + Lexical.logicalOP);
		System.out.println("Operand Bilgisi : ");
		System.out.println("\tToplam Operand say�s� : " + Lexical.operands);
	}

}
