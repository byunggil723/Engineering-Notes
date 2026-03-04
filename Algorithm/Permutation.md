# 순열

### 구현 예시

```C++
#include <iostream>
#include <vector>

using namespace std;

int N = 4;
int M = 2;

vector<int> arr = { 1, 2, 3, 4 };
vector<int> selected;
vector<bool> is_visited(4, false);

void perm(int depth) {
  if (depth == M) {
    for (int i = 0; i < selected.size(); i++)
      cout << selected[i] << ' '; 
    cout << "\n";
      
    return;
  }

  for (int i = 0; i < N; i++) {
    if (is_visited[i])
      continue;
    
    is_visited[i] = true;
    selected.push_back(arr[i]);
    
    perm(depth + 1);
    
    selected.pop_back();
    is_visited[i] = false;
  }
}

int main() {
  perm(0);
}
```