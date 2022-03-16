package homework1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexical {
	static int relationalOP = 0;
	static int arithmeticOP = 0;
	static int logicalOP = 0;
	static int unaryOP = 0;
	static int binaryOP = 0;
	static int operands = 0;

	public enum Type {
		RELATIONAL, ARITHMETIC, LOGICAL, UNARY, BINARY
	}

	static ArrayList<String> relationalOperators = new ArrayList<String>(
			Arrays.asList("==", "!=", ">=", "<=", "<", ">"));
	static ArrayList<String> logicalOperators = new ArrayList<String>(Arrays.asList("&&", "||"));
	static ArrayList<String> binaryOperators = new ArrayList<String>(
			Arrays.asList("=", "+", "-", "*", "/", "%", "^=", "|=", "&=", "%=", "*=", "/=", "-=", "+=", "^", "|", "&"));
	static ArrayList<String> unaryOperators = new ArrayList<String>(Arrays.asList("!", "+", "-", "++", "--"));

	/**
	 * Verilen satýrýn yorum satýrý olup olmadýðýný kontrol eder
	 * 
	 * @param line incelenecek satýr
	 * @return true yorum satýrý durumu
	 */
	static boolean isComment(String line) {
		if (line.startsWith("//") || line.startsWith("/*") || line.startsWith("*") || line.endsWith("*/")
				|| line.contains("//")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Satýr içerisindeki operatörleri ve operandlarý bulur.
	 * 
	 * @param line analiz edilecek satýr
	 */
	public static void analyzeLine(String line) {
		if (!isComment(line)) {
			countUnaryOP(line);
			countBinaryOP(line);
			countLogicalOP(line);
			countRelationalOP(line);
			List<String> tokens = tokenize(line, false);
			for (String token : tokens) {

				System.out.println(token);
			}
		}

	}

	/**
	 * Verilen satýrý hecelere ayýrýr
	 * 
	 * @param line            heceleþtirilecek satýr
	 * @param isSplitOperator Operatörlerde ayrýlacaksa true yapýlmalýdýr.
	 * @return satýrda bulunan heceleri liste tipinde döndürür.
	 */
	static List<String> tokenize(String line, boolean isSplitOperator) {
		ArrayList<String> tokens = new ArrayList<String>();
		boolean isOp = false;
		StringTokenizer st = new StringTokenizer(line, " ;{}[](),", true);
		// Ayraçlara göre ayrýmlar yapýlýyor.
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.equals(" "))
				continue;
			if (isSplitOperator) {
				if (token.length() == 1) {
					tokens.add(token);
				} else {
					for (String op : binaryOperators) {
						if (token.contains(op)) {
							isOp = true;
							StringTokenizer stOp = new StringTokenizer(token, "+-*%=/ ", true);
							while (stOp.hasMoreTokens()) {
								String item = stOp.nextToken();
								if (item.equals(" "))
									continue;
								tokens.add(item);
							}

							break;
						}
					}
					if (!isOp) {
						tokens.add(token);
					}

				}
			} else {
				tokens.add(token);
			}

		}
		return tokens;
	}

	/**
	 * 
	 * Satýrda bulunanan iliþkisel operatörleri sayar
	 * 
	 * @param token satýr içerisindeki hecelerden biri.
	 */
	static void countRelationalOP(String line) {
		// ([<>]=?|[!=]=)

		for (String op : relationalOperators) {
			if (line.contains(op)) {
				if (op.equals("==") || op.equals("!=")) {
					final Pattern relotionalPattern = Pattern.compile("(?:==|!=)");
					final Matcher relotionalMatcher = relotionalPattern.matcher(line);
					while (relotionalMatcher.find()) {
						relationalOP++;
						operands += 2;
					}
				} else if (op.equals("<") || op.equals(">")) {

					final Pattern relotionalPattern = Pattern.compile("[<>][^=]");
					final Matcher relotionalMatcher = relotionalPattern.matcher(line);
					while (relotionalMatcher.find()) {
						relationalOP++;
						operands += 2;
					}

				} else {
					final Pattern relotionalPattern = Pattern.compile("(?:<=?|>=)");
					final Matcher relotionalMatcher = relotionalPattern.matcher(line);
					while (relotionalMatcher.find()) {
						relationalOP++;
						operands += 2;
					}
				}

			}

		}

	}

	/**
	 * 
	 * Satýrda bulunanan mantýksal operatörleri sayar.
	 * 
	 * @param token satýr içerisindeki hecelerden biri.
	 */
	static void countLogicalOP(String line) {
		for (String op : logicalOperators) {
			if (line.contains(op)) {
				if(op.equals("||")) {
					final Pattern relotionalPattern = Pattern.compile("(?:\\|{2})");
					final Matcher relotionalMatcher = relotionalPattern.matcher(line);
					while (relotionalMatcher.find()) {
						logicalOP++;
						operands += 2;
					}
				}else {
					final Pattern relotionalPattern = Pattern.compile("(?:\\&{2})");
					final Matcher relotionalMatcher = relotionalPattern.matcher(line);
					while (relotionalMatcher.find()) {
						logicalOP++;
						operands += 2;
					}
				}
				
			}
		}
	}

	

	// ^[\!][0-9a-zA-Z]+(.)* find boolean unary op
	// ^[\~][a-zA-Z]+(.)* bitwise unary operator
	// ^[\-\+][0-9a-zA-Z]+(.)* finding unary operands
	// (^[\+\-]{2}[a-zA-Z]+)(.)*|([a-zA-Z]+[\+\-]{2}$(.)*) finding increament and
	// decreament ops
	/**
	 * Satýrda bulunan tekil operatörleri sayar
	 * 
	 * @param line heceleþtirilecek satýr
	 */
	static void countUnaryOP(String line) {
		List<String> tokens = tokenize(line, false);
		// heceler içerisinde tekil operatörler aranýyor.
		for (String token : tokens) {
			for (String op : unaryOperators) {
				if (token.contains(op)) {
					// Hece tek parça deðilse Regex kullanýlarak çözüme gidiliyor.
					if (token.length() == 1) {
						int indexOfToken = tokens.indexOf(token);
						// operatör ilk eleman da olup olmadýðý kontrol ediliyor.
						if (!(indexOfToken == 0)) {
							// Tek parça olan heceler için önceki hece kontrol ediliyor
							if (tokens.get(indexOfToken - 1).contains("=")
									&& tokens.get(indexOfToken - 1).endsWith("=")) {
								unaryOP++;
								if (op.equals("!"))
									logicalOP++;
								else
									arithmeticOP++;
								operands++;
							}
						}
					} else {
						// Birleþik heceler için operatörlere göre Regex desenleri çalýþtýrýlýyor.
						if (op.equals("+") || op.equals("-")) {

							int indexOfToken = tokens.indexOf(token);
							if (!(indexOfToken == 0)) {
								// Binary olan +- ile arasýnda ayrým yapmak için = sembolu kullanýlýyor.
								if (!(tokens.get(indexOfToken - 1).equals("=")) && !(token.contains("=")))
									continue;
								else {
									final Pattern upmOpPattern = Pattern.compile("^\\=?[\\-\\+][0-9a-zA-Z]+(.)*");
									final Matcher upmOpMatcher = upmOpPattern.matcher(token);
									while (upmOpMatcher.find()) {
										unaryOP++;
										arithmeticOP++;
										operands++;
									}
								}
							}

						} else if (op.equals("++") || op.equals("--")) {
							// Olasý boþluklar için önceki ve sonraki elemanlar kontrol ediliyor.
							int indexOfToken = tokens.indexOf(token);
							// Bulunan hecenin sýnýrlarý kontrol ediliyor ve dizi dýþýna taþma engelleniyor.
							if (indexOfToken > 0 || indexOfToken + 1 < tokens.size()) {
								String beforeToken = tokens.get(indexOfToken - 1);
								String afterToken = tokens.get(indexOfToken + 1);
								final Pattern strPattern = Pattern.compile("[a-zA-Z]+");
								final Matcher strMatcher;
								// çevresindeki operatörler kontrol ediliyor = operatöri ayraç olarak
								// kullanýlýyor.
								if (beforeToken.equals("="))
									strMatcher = strPattern.matcher(afterToken);
								else
									strMatcher = strPattern.matcher(beforeToken);
								while (strMatcher.find()) {
									unaryOP++;
									arithmeticOP++;
									operands++;
								}
							}
							final Pattern idOpPattern = Pattern
									.compile("(^[\\+\\-]{2}[a-zA-Z]+)(.)*|([a-zA-Z]+[\\+\\-]{2}$(.)*)");
							final Matcher idOpMatcher = idOpPattern.matcher(token);
							while (idOpMatcher.find()) {
								unaryOP++;
								arithmeticOP++;
								operands++;
							}

						} else if (op.equals("!")) {
							final Pattern boolOpPattern = Pattern.compile("^\\=?[\\!][0-9a-zA-Z]+(.)*");
							final Matcher boolOpMatcher = boolOpPattern.matcher(token);
							while (boolOpMatcher.find()) {
								unaryOP++;
								logicalOP++;
								operands++;
							}
						} /*
							 * else if(op.equals("~")) { final Pattern bitwiseOpPattern =
							 * Pattern.compile("^[\\~][a-zA-Z]+(.)*"); final Matcher bitwiseOpMatcher =
							 * bitwiseOpPattern.matcher(token); while (bitwiseOpMatcher.find()) { unaryOP++;
							 * operands++; } }
							 */

					}
				}
			}
		}
	}

	static void countBinaryOP(String line) {
		List<String> tokens = tokenize(line, true);
		for (String token : tokens) {
			for (String op : binaryOperators) {
				if (token.contains(op)) {
					if (token.length() == 1) {
						int indexOfToken = tokens.indexOf(token);
						// hece listesinin dýþýnda arama yapýlmasý engelleniyor.
						if (indexOfToken != -1 && indexOfToken != 0) {
							String beforeToken = tokens.get(indexOfToken - 1);
							// hece listesinin dýþýnda arama yapýlmasý engelleniyor.
							if (indexOfToken + 1 < tokens.size()) {
								String afterToken = tokens.get(indexOfToken + 1);
								// Tekli operatörlerin sayýlmasý engelleniyor bir önceki ve bir sonraki hecelere
								// bakýlarak tespit yapýlýyor.
								if (afterToken.equals("=") || (afterToken.equals("+") && !token.equals("="))
										|| beforeToken.equals("+") || (afterToken.equals("-") && !token.equals("="))
										|| beforeToken.equals("-"))
									break;
							}
							final Pattern artOpPattern = Pattern.compile("^[0-9a-zA-Z\\)\\]\\(\\[]+\\s*");
							final Matcher artOpMatcher = artOpPattern.matcher(beforeToken);
							while (artOpMatcher.find()) {
								binaryOP++;
								arithmeticOP++;
								operands += 2;
							}
						}

					}
					/*
					 * else { int index = tokens.indexOf(token); tokens.remove(index); //TODO burada
					 * birleþik operatörleri ayýrma iþlemini yap. StringTokenizer st = new
					 * StringTokenizer(token,"+-*%=/ ",true); while(st.hasMoreTokens()) { String
					 * item = st.nextToken(); if(item.equals(" ")) continue; tokens.add(item); } for
					 * (String combinedToken: combinedTokens) { int indexOfToken =
					 * combinedTokens.indexOf(combinedToken); String beforeToken =
					 * tokens.get(indexOfToken-1); final Pattern artOpPattern =
					 * Pattern.compile("^[0-9a-zA-Z\\)\\]\\(\\[]+\\s*"); final Matcher artOpMatcher
					 * = artOpPattern.matcher(beforeToken); while(artOpMatcher.find()) { binaryOP++;
					 * arithmeticOP++; operands+=2; } }
					 * 
					 * 
					 * }
					 */
				}
			}
		}
	}

	// [0-9a-zA-Z\)\]\(\[]*\s*[\+\-\*\/\%\=]\s*[0-9a-zA-Z\(\[\)\]]+
	static final HashSet<SymbolTableItem> operators = new HashSet<SymbolTableItem>();

	static void initiliazeOperators() {
		operators.add(new SymbolTableItem("+", Type.ARITHMETIC));
		operators.add(new SymbolTableItem("+", Type.UNARY));
		operators.add(new SymbolTableItem("-", Type.ARITHMETIC));
		operators.add(new SymbolTableItem("-", Type.UNARY));
		operators.add(new SymbolTableItem("++", Type.UNARY));
		operators.add(new SymbolTableItem("--", Type.UNARY));
		operators.add(new SymbolTableItem("!", Type.UNARY));
		operators.add(new SymbolTableItem("*", Type.ARITHMETIC));
		operators.add(new SymbolTableItem("/", Type.ARITHMETIC));
		operators.add(new SymbolTableItem("%", Type.ARITHMETIC));
		operators.add(new SymbolTableItem("==", Type.RELATIONAL));
		operators.add(new SymbolTableItem("!=", Type.RELATIONAL));
		operators.add(new SymbolTableItem(">", Type.RELATIONAL));
		operators.add(new SymbolTableItem("<", Type.RELATIONAL));
		operators.add(new SymbolTableItem(">=", Type.RELATIONAL));
		operators.add(new SymbolTableItem("<=", Type.RELATIONAL));
		operators.add(new SymbolTableItem("!", Type.LOGICAL));
		operators.add(new SymbolTableItem("&&", Type.LOGICAL));
		operators.add(new SymbolTableItem("&", Type.LOGICAL));
		operators.add(new SymbolTableItem("||", Type.LOGICAL));
		operators.add(new SymbolTableItem("|", Type.LOGICAL));
		operators.add(new SymbolTableItem("^", Type.LOGICAL));
		operators.add(new SymbolTableItem("&=", Type.LOGICAL));
		operators.add(new SymbolTableItem("|=", Type.LOGICAL));
		operators.add(new SymbolTableItem("^=", Type.LOGICAL));
		operators.add(new SymbolTableItem("+", Type.ARITHMETIC));
		operators.add(new SymbolTableItem("+", Type.ARITHMETIC));
	}

}
