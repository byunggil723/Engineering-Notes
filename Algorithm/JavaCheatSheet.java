import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class JavaCheatSheet {
  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    // 아스키 코드
    System.out.println("# 아스키 코드");

    int x;

    x = 'A';
    System.out.println("A: " + x); // A: 65

    x = 'a';
    System.out.println("a: " + x); // a: 97

    x = '0';
    System.out.println("0: " + x); // 0: 48

    char c;

    c = 65;
    System.out.println("65: " + c); // 65: A

    c = 97;
    System.out.println("97: " + c); // 97: a

    c = 48;
    System.out.println("48: " + c); // 48: 0

    c = '7';
    System.out.println(c - '0' + 10); // 17

    System.out.println("-----------------------------------------------");

    // 문자, 문자열 비교
    System.out.println("# 문자, 문자열 비교");

    char a = 'B';
    String s1 = "ABC";
    String s2 = "ABC";

    if (a == s1.charAt(1))
      System.out.println("Same!"); // Same!

    if (s1.equals(s2))
      System.out.println("Same!"); // Same!

    System.out.println("-----------------------------------------------");

    // 문자열 조작
    System.out.println("# 문자열 조작");

    // 1) trim
    System.out.println("1) trim");

    String s = "   hello world   ";
    System.out.println(s);
    System.out.println(s.trim());

    System.out.println();

    // 2) split
    System.out.println("2) split");

    s = "[a, b, c, d]";
    String substr = s.substring(1, s.length() - 1); // a, b, c, d
    String[] result = substr.split(", ");
    for (int i = 0; i < result.length; i++) {
      System.out.println(result[i] + " ");
    }
    /*
     * a
     * b
     * c
     * d
     */
    System.out.println(Arrays.toString(result) + " (size: " + result.length + ")");
    // [a, b, c, d] (size: 4)

    System.out.println();

    s = "[a, b, c, d]";
    substr = s.substring(1, s.length() - 1); // a, b, c, d
    result = substr.split(", ", 2);
    for (int i = 0; i < result.length; i++) {
      System.out.println(result[i] + " ");
    }
    /*
     * a
     * b, c, d
     */
    System.out.println(Arrays.toString(result) + " (size: " + result.length + ")");
    // [a, b, c, d] (size: 2)

    System.out.println();

    // 3) StringBuilder, 문자열 조립
    System.out.println("3) StringBuilder, 문자열 조립");

    StringBuilder sb = new StringBuilder("");

    // 추가
    sb.append("a");
    System.out.println(sb);
    sb.append("pple");
    System.out.println(sb);

    // 수정
    sb.setCharAt(1, 'X'); // 한 단어만 수정 가능하여 반드시 char 타입을 인자로 전달
    System.out.println(sb);

    // 삭제
    sb.deleteCharAt(2);
    System.out.println(sb);

    // 반전
    sb.reverse();
    System.out.println(sb);

    // 최종 String 변환
    s = sb.toString();
    System.out.println("result: " + s);

    System.out.println("-----------------------------------------------");

    // 정렬
    System.out.println("# 정렬");

  }
}
