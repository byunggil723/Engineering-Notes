# Vector 동적 할당

### **1차원**

1. 지역 변수 선언 시

   ```cpp
   #include <iostream>
   #include <vector>

   using namespace std;

   int n = 0;

   int main() {
     cin >> n;

     // 런타임에서 크기 할당
     vector<int> vec(n, 0); // n개의 0으로 초기화

     // 값 입력
     for (int i = 0; i < n; i++) {
       vec[i] = i + 1;
     }

     // 값 출력
     for (int x : vec) {
       cout << x << " ";
     }
     cout << endl;

     return 0;
   }
   ```

2. 전역 변수 선언 시

   ```cpp
   #include <iostream>
   #include <vector>
   using namespace std;

   int n;
   vector<int> arr; // 전역 변수 선언

   int main() {
     cin >> n;

     // 런타임에서 크기 할당
     arr.assign(n, 0); // n개의 0으로 초기화

     // 값 입력
     for (int i = 0; i < n; i++) {
         arr[i] = i + 1;
     }

     // 값 출력
     for (int x : arr) {
       cout << x << " ";
     }
     cout << endl;

     return 0
   }
   ```

   ```cpp
   #include <iostream>
   #include <vector>
   using namespace std;

   int n;
   vector<int>* arr; // 전역 포인터 선언

   int main() {
     cin >> n;

     // new로 동적 할당
     arr = new vector<int>(n, 0); // n개의 0으로 초기화

     // 값 입력
     for (int i = 0; i < n; i++) {
         (*arr)[i] = i + 1; // (중요!) 이 경우에는 반드시 역참조로 접근해야 함
     }

     // 값 출력
     for (int i = 0; i < n; i++) {
       cout << (*arr)[i] << " ";  // (중요!) 역시 역참조로 접근해야 함
     }
     cout << endl;

     // 메모리 해제
     delete arr;

     return 0;
   }
   ```

### **2차원**

1. 지역 변수 선언 시

   ```cpp
   #include <iostream>
   #include <vector>

   using namespace std;

   int rows = 0, cols = 0; // 행, 열

   int main() {
     cin >> rows >> cols;

     // 런타임에서 크기 할당: rows개의 벡터로 초기화 + 각 벡터는 cols개의 0으로 초기화
     vector<vector<int>> vec(rows, vector<int>(cols, 0));

     // 값 입력
     for (int i = 0; i < rows; i++) {
       for (int j = 0; j < cols; j++) {
           vec[i][j] = i * cols + j + 1;
       }
     }

     // 값 출력
     for (int i = 0; i < rows; i++) {
         for (int j = 0; j < cols; j++) {
             cout << vec[i][j] << " ";
         }
         cout << endl;
     }

     return 0;
   }
   ```

2. 전역 변수 선언 시

   ```cpp
   #include <iostream>
   #include <vector>

   using namespace std;

   int N, M;
   vector<vector<int>> grid; // 전역 변수 선언

   int main() {
     cin >> N >> M;

     // 런타임에서 크기 할당: N개의 벡터로 초기화 + 각 벡터는 M개의 0으로 초기화
     grid.assign(N, vector<int>(M, 0));

     // 값 입력
     for (int i = 0; i < N; i++) {
         for (int j = 0; j < M; j++) {
             grid[i][j] = i * M + j + 1;
         }
     }

     // 값 출력
     for (int i = 0; i < N; i++) {
         for (int j = 0; j < M; j++) {
             cout << grid[i][j] << " ";
         }
         cout << endl;
     }

     return 0;
   }

   ```

   ```cpp
   #include <iostream>
   #include <vector>

   using namespace std;

   int N, M;
   vector<vector<int>>* grid; // 전역 포인터 선언

   int main() {
     cin >> N >> M;

     // new 로 동적 할당: N개의 벡터로 초기화 + 각 벡터는 M개의 0으로 초기화
     grid = new vector<vector<int>>(
         N, vector<int>(M, 0)
     );

     // 값 입력
     for (int i = 0; i < N; i++) {
         for (int j = 0; j < M; j++) {
             (*grid)[i][j] = i * M + j + 1; // (중요!) 역시 역참조로 접근해야 함
         }
     }

     // 값 출력
     for (int i = 0; i < N; i++) {
         for (int j = 0; j < M; j++) {
             cout << (*grid)[i][j] << " ";
         }
         cout << endl;
     }

     // 메모리 해제
     delete grid;

     return 0;
   }
   ```

### **3차원**

1. 지역 변수 선언 시

   ```cpp
   #include <iostream>
   #include <vector>

   using namespace std;

   int H, N, M;

   int main() {
     cin >> H >> N >> M; // H층, N행, M열

     // 런타임에서 크기 할당
     vector<vector<vector<int>>> box(
       H, vector<vector<int>>(N, vector<int>(M, 0))
     );

     // 값 입력
     for (int h = 0; h < H; h++) {
       for (int n = 0; n < N; n++) {
         for (int m = 0; m < M; m++) {
           box[h][n][m] = h * 100 + n * 10 + m;
         }
       }
     }

     // 값 출력
     for (int h = 0; h < H; h++) {
       cout << "Layer " << h << ":\n";
         for (int n = 0; n < N; n++) {
           for (int m = 0; m < M; m++) {
             cout << box[h][n][m] << " ";
           }
           cout << endl;
         }
       cout << endl;
     }

     return 0;
   }

   ```

2. 전역 변수 선언 시

   ```cpp
   #include <iostream>
   #include <vector>

   using namespace std;

   int H, N, M;
   vector<vector<vector<int>>> box; // 전역 변수

   int main() {
     cin >> H >> N >> M;

     // 런타임에서 크기 할당
     box.assign(H, vector<vector<int>>(N, vector<int>(M, 0)));

     // 값 입력
     for (int h = 0; h < H; h++) {
       for (int n = 0; n < N; n++) {
         for (int m = 0; m < M; m++) {
           box[h][n][m] = h * 100 + n * 10 + m;
         }
       }
     }

     // 값 출력
     for (int h = 0; h < H; h++) {
       cout << "Layer " << h << ":\n";
       for (int n = 0; n < N; n++) {
         for (int m = 0; m < M; m++) {
           cout << box[h][n][m] << " ";
         }
         cout << "\n";
       }
       cout << "\n";
     }

     return 0;
   }
   ```

   ```cpp
   #include <iostream>
   #include <vector>

   using namespace std;

   int H, N, M;
   vector<vector<vector<int>>>* box; // 전역 포인터 선언

   int main() {
     cin >> H >> N >> M;

     // new로 동적 할당: H개의 2차원 벡터로 초기화
     box = new vector<vector<vector<int>>>(
       H, vector<vector<int>>(N, vector<int>(M, 0))
     );

     // 값 입력
     for (int h = 0; h < H; h++) {
       for (int n = 0; n < N; n++) {
         for (int m = 0; m < M; m++) {
           (*box)[h][n][m] = h * 100 + n * 10 + m;
         }
       }
     }

     // 값 출력
     for (int h = 0; h < H; h++) {
       cout << "Layer " << h << ":\n";
         for (int n = 0; n < N; n++) {
           for (int m = 0; m < M; m++) {
             cout << (*box)[h][n][m] << " ";
           }
           cout << endl;
         }
       cout << endl;
     }

     // 메모리 해제
     delete box;

     return 0;
   }
   ```
