package util;

public class Calculator {
	final String compute;

	public Calculator(String compute) {
		this.compute = compute;
	}

	public String compute() throws Exception {
		// remove all white space from String
		String computeCopy = remvWhiteSpace(compute);

		if (!isValid(computeCopy)) {
			throw new Exception();
		}

		String result = null;

		return result;
	}

	private boolean isValid(String s) {
		// TODO
		boolean valid = true;
		int openCount;
		openCount = 0;
		char[] ch = s.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if (ch[i] == '(') {
				openCount++;
			}

			if (ch[i] == ')') {
				openCount--;
			}

			if (ch[i] == '*' || ch[i] == '/' || ch[i] == '.') {
				int j = i - 1, k = i + 1;

				if (j < 0 || k >= ch.length) {
					return false;
				}

				if (ch[j] == '-' || ch[j] == '+' || ch[j] == '*' || ch[j] == '/' || ch[j] == '.') {
					return false;
				}
				if (!Character.isDigit(ch[k])) {
					return false;
				}
			}

		}

		if (openCount != 0) {
			valid = false;
		}
		return valid;
	}

	private static String remvWhiteSpace(String s) {
		return s.replaceAll("\\s+", "");
	}

	private static String handleBraces(String s) {
		int firstIndex = s.indexOf("(");

		if (!(firstIndex < 0)) {
			int openCount, lastIndex;
			openCount = 0;
			boolean found = false;
			char[] ch = s.toCharArray();
			for (int i = 0; i < ch.length; i++) {
				if (ch[i] == '(') {
					openCount++;
					if (!found) {
						System.out.print(i + " - ");
						firstIndex = i;
					}
					found = true;

				} else if (ch[i] == ')') {
					openCount--;
				}
				if (openCount == 0 && found) {
					lastIndex = i;
					String sub = s.substring(firstIndex + 1, lastIndex);
					System.out.println(sub);
					String v = handleBraces(sub);
					found = false;
					return handleBraces(v);
				}
			}
		}

		boolean containDivision = s.contains("/");
		boolean containMultiplication = s.contains("*");
		boolean containAddition = s.contains("+") || s.contains("-");
		String ss = s;
		if (containDivision) {
			ss = doDivision(ss);
		}
		if (containMultiplication) {

			ss = doMultiplication(ss);
		}
		if (containAddition) {
			ss = doAddition(ss);
		}

		return ss;
	}

	private static String doAddition(String ss) {
		// TODO Auto-generated method stub
		return "A";
	}

	private static String doDivision(String ss) {
		// TODO Auto-generated method stub
		char[] ch = ss.toCharArray();
		int i = ss.indexOf('/');
		if(!(i < 0)){
			String right = "", left = "";
			int l, r;
			l = r = i;
			while (l >= 0) {

				l--;
				if (ch[l] == '.') {
					continue;
				} else if (!Character.isDigit(ch[l])) {
					break;
				}
			}

			while (r < ch.length - 1) {
				r++;
				if (ch[r] == '.' ||ch[r] == '-'||ch[r] == '+') {
					continue;
				}
				if (!Character.isDigit(ch[r])) {
					break;
				}
			}

			left = ss.substring(l + 1, i);
			right = ss.substring(i + 1, r);
			
			Double rhs = Double.parseDouble(right + "D");
			Double lhs = Double.parseDouble(left + "D");

			double result = lhs / rhs;
			String sub = ss.substring(l + 1, r);
			System.out.println(left);
			System.out.println(right);
			System.out.println(String.valueOf(result));
			ss = ss.replace(sub, String.valueOf(result));
			System.out.println(String.valueOf(ss));
			return doDivision(ss);
		}
		
		return ss;	
	}

	private static String doMultiplication(String ss) {
		// TODO Auto-generated method stub
		char[] ch = ss.toCharArray();
		int i = ss.indexOf('*');
		if(!(i < 0)){
			String right = "", left = "";
			int l, r;
			l = r = i;
			while (l >= 0) {

				l--;
				if (ch[l] == '.') {
					continue;
				} else if (!Character.isDigit(ch[l])) {
					break;
				}
			}

			while (r < ch.length - 1) {
				r++;
				if (ch[r] == '.' ||ch[r] == '-'||ch[r] == '+') {
					continue;
				}
				if (!Character.isDigit(ch[r])) {
					break;
				}
			}

			left = ss.substring(l + 1, i);
			right = ss.substring(i + 1, r);
			
			Double rhs = Double.parseDouble(right + "D");
			Double lhs = Double.parseDouble(left + "D");

			double result = lhs * rhs;
			
			String sub = ss.substring(l + 1, r);
			System.out.println(left);
			System.out.println(right);
			System.out.println(String.valueOf(result));
			ss = ss.replace(sub, String.valueOf(result));
			System.out.println(String.valueOf(ss));
			return doDivision(ss);
		}
		
		return ss;	

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// String testCase1 = "1 + 1";
		String testCase2 = "(2 + 2 - (2 + 3.23) *(3.232/123.44 * 667)) - 1 + (2 * -3 + - 4 / -3)";
		// String testCase3 = "3 - 2 + 4";
		// String testCase4 = "";
		// String testCase5 = "";

		testCase2 = remvWhiteSpace(testCase2);
		System.out.println(testCase2);
		doMultiplication(doDivision(testCase2));
		// handleBraces(testCase2);
	}

}
