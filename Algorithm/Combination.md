# 조합

### 구현 예시

```C++
#include <iostream>
#include <vector>

using namespace std;

int N = 4;
int M = 2;

vector<int> arr = { 1, 2, 3, 4 };
vector<int> selected;

void comb(int depth, int start_point) {
  if (depth == M) {
    for (int i = 0; i < selected.size(); i++)
      cout << selected[i] << ' '; 
    cout << "\n";
      
    return;
  }

  for (int i = start_point; i < N; i++) {
    selected.push_back(arr[i]);

    comb(depth + 1, i + 1);

    selected.pop_back();
  }
}

int main() {
  comb(0, 0);
}
```