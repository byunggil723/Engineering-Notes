import java.io.*;
import java.util.*;

class Pair {
  int x, y;

  Pair(int x, int y) {
    this.x = x;
    this.y = y;
  }
}

public class JavaCheatSheet {
  static int n, m;
  static boolean[] visited;
  static List<Integer> selected;

  // 순열
  static void perm(int depth) {
    if (depth == m) {

      for (int i = 0; i < selected.size(); i++) {
        System.out.print(selected.get(i) + " ");
      }
      System.out.println();

      return;
    }

    for (int i = 1; i <= n; i++) {

      if (visited[i])
        continue;

      visited[i] = true;
      selected.add(i);

      perm(depth + 1);

      selected.remove(selected.size() - 1);
      visited[i] = false;
    }
  }

  // 조합
  static void comb(int depth, int start_point) {
    if (depth == m) {

      for (int i = 0; i < selected.size(); i++) {
        System.out.print(selected.get(i) + " ");
      }
      System.out.println();

      return;
    }

    for (int i = start_point; i <= n; i++) {
      selected.add(i);
      comb(depth + 1, i + 1);
      selected.remove(selected.size() - 1);
    }
  }

  // 중복 순열
  static void rep_perm(int depth) {
    if (depth == m) {

      for (int i = 0; i < selected.size(); i++) {
        System.out.print(selected.get(i) + " ");
      }
      System.out.println();

      return;
    }

    for (int i = 1; i <= n; i++) {
      selected.add(i);
      rep_perm(depth + 1);
      selected.remove(selected.size() - 1);
    }
  }

  // 중복 조합
  static void rep_comb(int depth, int start_point) {
    if (depth == m) {

      for (int i = 0; i < selected.size(); i++) {
        System.out.print(selected.get(i) + " ");
      }
      System.out.println();

      return;
    }

    for (int i = start_point; i <= n; i++) {
      selected.add(i);
      rep_comb(depth + 1, i);
      selected.remove(selected.size() - 1);
    }
  }

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st;

    // 아스키 코드
    System.out.println("# 아스키 코드");
    System.out.println();

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

    System.out.println();
    System.out.println("-----------------------------------------------");
    System.out.println();

    // 문자, 문자열 비교
    System.out.println("# 문자, 문자열 비교");
    System.out.println();

    char a = 'B';
    String s1 = "ABC";
    String s2 = "ABC";

    if (a == s1.charAt(1))
      System.out.println("Same!"); // Same!

    if (s1.equals(s2))
      System.out.println("Same!"); // Same!

    System.out.println();
    System.out.println("-----------------------------------------------");
    System.out.println();

    // 문자열 조작
    System.out.println("# 문자열 조작");
    System.out.println();

    // 1) trim
    System.out.println("1) trim");
    System.out.println();

    String s = "   hello world   ";
    System.out.println(s);
    System.out.println(s.trim());

    System.out.println();

    // 2) split
    System.out.println("2) split");
    System.out.println();

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
    System.out.println();

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

    System.out.println();
    System.out.println("-----------------------------------------------");
    System.out.println();

    // 문자열 정렬
    System.out.println("# 문자열 정렬");
    System.out.println();

    List<String> dict = new ArrayList<>(List.of("banana", "apple", "carrot"));

    dict.sort((str1, str2) -> str1.compareTo(str2)); // 오름차순
    System.out.println(dict);

    dict.sort((str1, str2) -> str2.compareTo(str1)); // 내림차순
    System.out.println(dict);

    System.out.println();
    System.out.println("-----------------------------------------------");
    System.out.println();

    // 문자 <-> 문자열 변환
    System.out.println("# 문자 <-> 문자열 변환");
    System.out.println();

    // char -> String
    char ch = 'A';
    String str_A = Character.toString(c); // "A"

    // String -> char
    String str = "ABC";
    char c_A = s.charAt(0); // 'A'

    System.out.println();
    System.out.println("-----------------------------------------------");
    System.out.println();

    // 배열 출력
    System.out.println("# 배열 출력");
    System.out.println();

    // 1) primitive 배열 (1차원)
    System.out.println("1) primitive 배열 (1차원)");
    System.out.println();

    int[] arr = new int[] { 2, 4, 3, 1, 5 };
    System.out.println(Arrays.toString(arr)); // [2, 4, 3, 1, 5]
    System.out.println();

    // 2) primitive 배열 (2차원 이상)
    System.out.println("2) primitive 배열 (2차원 이상)");
    System.out.println();

    int[][] arr2 = new int[][] { { 2, 4 }, { 3, 1, 5 } };
    System.out.println(Arrays.deepToString(arr2));
    System.out.println();

    // 3) Collection (1차원)
    System.out.println("3) Collection (1차원)");
    System.out.println();

    List<Integer> l = new ArrayList<>(List.of(3, 2, 4, 1, 5));

    /*
     * List.of(1, 2, 3, ...)은 수정이 불가능한 List 구현체(ImmutableCollections$ListN)
     * 를 반환한다. 하지만 이를 위와 같이 ArrayList의 초기값으로 사용하면,
     * 해당 원소들이 새로운 ArrayList에 복사되므로 수정 가능한 리스트를 생성할 수 있다.
     */

    l.add(6);
    System.out.println(l);
    l.set(2, 7);
    System.out.println(l);
    l.remove(0);
    System.out.println(l);
    System.out.println();

    // 4) Collection (2차원 이상)
    System.out.println("4) Collection (2차원 이상)");
    System.out.println();

    List<List<Integer>> l2 = new ArrayList<>();

    l2.add(new ArrayList<>(List.of(1, 2, 3)));
    l2.add(new ArrayList<>(List.of(4, 5, 6)));
    l2.add(new ArrayList<>(List.of(7, 8, 9)));

    System.out.println(l2);
    System.out.println();

    System.out.println();
    System.out.println("-----------------------------------------------");
    System.out.println();

    // 정렬
    System.out.println("# 정렬");
    System.out.println();

    // 1) primitive 배열 (내림차순 불가)
    System.out.println("1) primitive 배열 (내림차순 불가)");
    System.out.println();

    arr = new int[] { 2, 4, 3, 1, 5 };

    Arrays.sort(arr);
    System.out.println(Arrays.toString(arr));
    System.out.println();

    // 2) Collection
    System.out.println("2) Collection");
    System.out.println();

    l = new ArrayList<>(List.of(2, 4, 3, 1, 5));

    l.sort((e1, e2) -> {
      return Integer.compare(e2, e1); // 내림차순
    });
    System.out.println(l);
    System.out.println();

    // 3) Collection (Pair)
    System.out.println("3) Collection (Pair)");
    System.out.println();

    List<Pair> l3 = new ArrayList<>();
    l3 = new ArrayList<>(
        List.of(
            new Pair(1, 2),
            new Pair(4, 1),
            new Pair(2, 6),
            new Pair(2, 3)));

    l3.sort((e1, e2) -> {
      if (e2.x != e1.x) {
        return Integer.compare(e2.x, e1.x);
      }
      return Integer.compare(e1.y, e2.y);
    }); // 첫 번째 원소 - 내림차순, 두 번째 원소 - 오름차순

    for (int i = 0; i < l3.size(); i++) {
      System.out.println(l3.get(i).x + " " + l3.get(i).y);
    }

    System.out.println();
    System.out.println("-----------------------------------------------");
    System.out.println();

    // 스택, 큐, 덱
    System.out.println("# 스택, 큐, 덱");
    System.out.println();

    // 1) 스택
    Deque<Integer> stack = new ArrayDeque<>();

    System.out.print("스택: ");
    for (int i = 0; i < 10; i++) {
      stack.offerFirst(i);
    }

    Iterator<Integer> it = stack.iterator();
    for (int i = 0; i < stack.size(); i++) {
      int cur_v = it.next();

      if (cur_v == 5) {
        it.remove();
      }

      System.out.print(cur_v + " ");
    }
    System.out.println();

    System.out.println("poll: " + stack.poll());
    System.out.println("peek: " + stack.peek());
    System.out.println();

    // 2) 큐
    System.out.print("큐: ");
    Deque<Integer> queue = new ArrayDeque<>();

    for (int i = 0; i < 10; i++) {
      queue.offerLast(i);
    }

    it = queue.iterator();
    for (int i = 0; i < queue.size(); i++) {
      int cur_v = it.next();

      if (cur_v == 5) {
        it.remove();
      }

      System.out.print(cur_v + " ");
    }
    System.out.println();

    System.out.println("poll: " + queue.poll());
    System.out.println("peek: " + queue.peek());
    System.out.println();

    // 3) 덱
    System.out.print("덱: ");
    Deque<Integer> deque = new ArrayDeque<>();

    for (int i = 0; i < 10; i++) {
      deque.offerFirst(i);
    }
    for (int i = 0; i < 10; i++) {
      deque.offerLast(-1 * i);
    }

    it = deque.iterator();
    int d_size = deque.size();
    for (int i = 0; i < d_size; i++) {
      int cur_v = it.next();

      if (cur_v == 5) {
        it.remove();
        continue;
      }

      System.out.print(cur_v + " ");
    }

    System.out.println();
    System.out.println("pollFirst: " + deque.pollFirst());
    System.out.println("peekFirst: " + deque.peekFirst());
    System.out.println("pollLast: " + deque.pollLast());
    System.out.println("peekLast: " + deque.peekLast());

    System.out.println();
    System.out.println("-----------------------------------------------");
    System.out.println();

    System.out.print("n 입력: ");
    n = Integer.parseInt(br.readLine());

    System.out.print("m 입력: ");
    m = Integer.parseInt(br.readLine());

    System.out.println();

    // 순열
    System.out.println("[순열]");
    System.out.println();

    visited = new boolean[n + 1];
    selected = new ArrayList<>();
    for (int i = 0; i <= n; i++) {
      visited[i] = false;
    }

    perm(0);

    System.out.println();
    System.out.println("-----------------------------------------------");
    System.out.println();

    // 조합

    System.out.println("[조합]");
    System.out.println();

    visited = new boolean[n + 1];
    selected = new ArrayList<>();
    for (int i = 0; i <= n; i++) {
      visited[i] = false;
    }

    comb(0, 1);

    System.out.println();
    System.out.println("-----------------------------------------------");
    System.out.println();

    // 중복 순열

    System.out.println("[중복 순열]");
    System.out.println();

    visited = new boolean[n + 1];
    selected = new ArrayList<>();
    for (int i = 0; i <= n; i++) {
      visited[i] = false;
    }

    rep_perm(0);

    System.out.println();
    System.out.println("-----------------------------------------------");
    System.out.println();

    // 중복 조합

    System.out.println("[중복 조합]");
    System.out.println();

    visited = new boolean[n + 1];
    selected = new ArrayList<>();
    for (int i = 0; i <= n; i++) {
      visited[i] = false;
    }

    rep_comb(0, 1);

    System.out.println();
    System.out.println("-----------------------------------------------");
    System.out.println();
  }
}
