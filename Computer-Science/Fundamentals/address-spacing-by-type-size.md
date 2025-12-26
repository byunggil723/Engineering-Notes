# 4바이트(int)와 8바이트(long long)

타입의 바이트 값에 따라 주소값의 간격이 달라지는 C++ 예시

```C
#include <iostream>

using namespace std;

int main()
{
  cin.tie(NULL);
  ios::sync_with_stdio(false);

  int x[3] = {0, 1, 2};
  long long y[3] = {3, 4, 5};

  cout << &x << ' ' << &x[1] << ' ' << &x[2] << '\n';
  cout << &y << ' ' << &y[1] << ' ' << &y[2] << '\n';
  cout << (uintptr_t)&x << ' ' << (uintptr_t)&x[1] << ' ' << (uintptr_t)&x[2] << '\n';
  cout << (uintptr_t)&y << ' ' << (uintptr_t)&y[1] << ' ' << (uintptr_t)&y[2] << '\n';
}
```
