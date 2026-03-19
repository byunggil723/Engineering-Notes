# ArrayList

### 1차원 배열

```Java
import java.io.*;
import java.util.*;

public class Main {

  public static void main(String[] args) throws Exception {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    StringTokenizer st;

    // 입력: N
    st = new StringTokenizer(br.readLine());
    int N = Integer.parseInt(st.nextToken());

    // 1차원 ArrayList 선언
    ArrayList<Integer> list = new ArrayList<>(); // <> 내부 생략 가능

    // 입력: N개 값
    st = new StringTokenizer(br.readLine());
    for (int i = 0; i < N; i++) {
      list.add(Integer.parseInt(st.nextToken()));
      // (C++) v.push_back(N) = (Java) list.add(N)
      // (C++) v.pop_back() = (Java) list.remove(list.size() - 1)
    }

    // 출력
    for (int i = 0; i < list.size(); i++) {
      bw.write(list.get(i) + " ");
      // write(int)는 해당 값을 문자 코드로 해석하여 출력하고, write(String)은 문자열 그대로 출력한다.
      // 따라서 숫자를 그대로 출력하려면 문자열로 변환해야 한다.
    }
    bw.write("\n");

    bw.flush();
    bw.close();
    br.close();
  }
}
```

### 2차원 배열

```Java
import java.io.*;
import java.util.*;

public class Main {

  public static void main(String[] args) throws Exception {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    StringTokenizer st;

    // 입력: N M
    st = new StringTokenizer(br.readLine());
    int N = Integer.parseInt(st.nextToken());
    int M = Integer.parseInt(st.nextToken());

    // 2차원 ArrayList 선언
    ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();

    // 입력
    for (int i = 0; i < N; i++) {
      matrix.add(new ArrayList<>());

      st = new StringTokenizer(br.readLine());
      for (int j = 0; j < M; j++) {
        matrix.get(i).add(Integer.parseInt(st.nextToken()));
      }
    }

    // 출력
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++) {
        bw.write(matrix.get(i).get(j) + " ");
      }
      bw.write("\n");
    }

    bw.flush();
    bw.close();
    br.close();
  }
}
```

### 2차원 배열 - 입력값 사이에 공백이 없는 경우

```bash
# 입력 예시

5
10101
11110
11000
10101
01100
```

```Java
import java.io.*;
import java.util.*;

public class Main {

  public static void main(String[] args) throws Exception {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    int N = Integer.parseInt(br.readLine());

    ArrayList<ArrayList<Integer>> list = new ArrayList<>();

    for (int i = 0; i < N; i++) {
      list.add(new ArrayList<>());

      String line = br.readLine();

      for (int j = 0; j < N; j++) {
        list.get(i).add(line.charAt(j) - '0');
      }
    }

    // 출력
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        bw.write(list.get(i).get(j) + "");
      }
      bw.write("\n");
    }

    bw.flush();
    bw.close();
    br.close();
  }
}
```


```Java
import java.io.*;
import java.util.*;

public class Main {

  public static void main(String[] args) throws Exception {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    StringTokenizer st;

    // 입력: X Y Z
    st = new StringTokenizer(br.readLine());
    int X = Integer.parseInt(st.nextToken());
    int Y = Integer.parseInt(st.nextToken());
    int Z = Integer.parseInt(st.nextToken());

    // 3차원 ArrayList 선언
    ArrayList<ArrayList<ArrayList<Integer>>> cube = new ArrayList<>();

    // 입력
    for (int i = 0; i < X; i++) {
      cube.add(new ArrayList<>());

      for (int j = 0; j < Y; j++) {
        cube.get(i).add(new ArrayList<>());

        st = new StringTokenizer(br.readLine());
        for (int k = 0; k < Z; k++) {
          cube.get(i).get(j).add(Integer.parseInt(st.nextToken()));
        }
      }
    }

    // 출력
    for (int i = 0; i < X; i++) {
      for (int j = 0; j < Y; j++) {
        for (int k = 0; k < Z; k++) {
          bw.write(cube.get(i).get(j).get(k) + " ");
        }
        bw.write("\n");
      }
      bw.write("\n");
    }

    bw.flush();
    bw.close();
    br.close();
  }
}
```