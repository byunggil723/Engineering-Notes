# #include <algorithm\>

### 1. sort()

(1) 단일 원소 정렬

```cpp
#include <iostream>
#include <vector>
#include <algorithm>   // sort
#include <functional>  // less, greater

int main() {
  std::vector<int> v = {5, 2, 8, 1, 3};

  // 1. 기본 오름차순 (less와 동일)
  std::vector<int> v1 = v; // std::vector, std::string 등 표준 컨테이너는 깊은 복사 지원.
  std::sort(v1.begin(), v1.end());
  std::cout << "기본 오름차순: ";
  for (int x : v1) std::cout << x << " "; // 기본 오름차순: 1 2 3 5 8
  /*
  range-based for loop, 표준 컨테이너 vector, array, list, deque, set, map, unordered_map ... 전부 지원

  std::vector<int> v = {1,2,3};
  // 복사
  for (int x : v) x = 99;  // v = {1,2,3} (변화 없음)
  // 참조
  for (int &x : v) x = 99;  // v = {99,99,99}
  */
  std::cout << "\n";

  // 2. std::less 사용 (오름차순)
  std::vector<int> v2 = v;
  std::sort(v2.begin(), v2.end(), std::less<int>());
  std::cout << "less 오름차순: ";
  for (int x : v2) std::cout << x << " "; //less 오름차순: 1 2 3 5 8
  std::cout << "\n";

  // 3. std::greater 사용 (내림차순)
  std::vector<int> v3 = v;
  std::sort(v3.begin(), v3.end(), std::greater<int>());
  std::cout << "greater 내림차순: "; // greater 내림차순: 8 5 3 2 1
  for (int x : v3) std::cout << x << " ";
  std::cout << "\n";

  // 4. 람다 커스텀: 오름차순
  auto cmp = [](int a, int b) -> bool
  {
    return a < b; // 오름차순
  };
  std::vector<int> v4 = v;
  std::sort(v4.begin(), v4.end(), cmp);
  std::cout << "람다 오름차순: "; // 람다 오름차순: 1 2 3 5 8
  for (int x : v4) std::cout << x << " ";
  std::cout << "\n";

  // 5. 람다 커스텀: 내림차순
  std::vector<int> v5 = v;
  std::sort(v5.begin(), v5.end(), [](int a, int b) {
    return a > b;   // 내림차순
  });
  std::cout << "람다 내림차순: "; // 람다 내림차순: 8 5 3 2 1
  for (int x : v5) std::cout << x << " ";
  std::cout << "\n";

  return 0;
}
```

(2) pair 정렬

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <functional>
#include <utility>     // pair

using namespace std;

int main() {
  vector<pair<int,int>> v = {
    {2,5}, {1,7}, {2,3}, {1,9}
  };

  // 1. 기본 오름차순 (first 오름차순, 같으면 second 오름차순)
  vector<pair<int,int>> v1 = v;
  sort(v1.begin(), v1.end());
  cout << "기본 오름차순:\n";
  for (auto &p : v1) cout << "(" << p.first << "," << p.second << ") ";
  cout << "\n"; // (1,7) (1,9) (2,3) (2,5)

  // 2. 전체 내림차순
  vector<pair<int,int>> v2 = v;
  sort(v2.begin(), v2.end(), greater<pair<int,int>>());
  cout << "greater 내림차순:\n";
  for (auto &p : v2) cout << "(" << p.first << "," << p.second << ") ";
  cout << "\n"; // (2,5) (2,3) (1,9) (1,7)

  // 3. 커스텀: first만 오름차순
  vector<pair<int,int>> v3 = v;
  sort(v3.begin(), v3.end(), [](const pair<int,int> &a, const pair<int,int> &b) {
    return a.first < b.first;
  });
  cout << "first 오름차순:\n";
  for (auto &p : v3) cout << "(" << p.first << "," << p.second << ") ";
  cout << "\n"; // (1,7) (1,9) (2,3) (2,5)

  // 4. 커스텀: first 오름차순, 같으면 second 내림차순
  vector<pair<int,int>> v4 = v;
  sort(v4.begin(), v4.end(), [](const pair<int,int> &a, const pair<int,int> &b) {
    if (a.first == b.first) return a.second > b.second;
      return a.first < b.first;
  });
  cout << "first 오름차순, 같으면 second 내림차순:\n";
  for (auto &p : v4) cout << "(" << p.first << "," << p.second << ") ";
  cout << "\n"; // (1,9) (1,7) (2,5) (2,3)

  return 0;
}

```

(3) tuple 정렬

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <tuple>

using namespace std;

int main()
{
  vector<tuple<int, int, int>> v = {
    {1, 2, 3}, {3, 1, 4}, {5, 3, 1}, {4, 8, 9}, {4, 1, 3}, {1, 3, 2}, {3, 4, 5}, {1, 1, 3}, {1, 1, 7}};

  auto cmp = [](const tuple<int, int, int> &a, const tuple<int, int, int> &b)
  {
    if (get<0>(a) == get<0>(b))
    {
      if (get<1>(a) == get<1>(b))
      {
        return get<2>(a) < get<2>(b);
      }
      return get<1>(a) > get<1>(b);
    }
    return get<0>(a) < get<0>(b);
  }; // 첫 번째 요소: 오름차순, 두 번째 요소: 내림차순, 세 번째 요소: 오름차순 정렬

  sort(v.begin(), v.end(), cmp);

  for (const auto &x : v)
  {
    cout << "(" << get<0>(x) << ", " << get<1>(x) << ", " << get<2>(x) << ")" << '\n';
  }
  /*
  (1, 3, 2)
  (1, 2, 3)
  (1, 1, 3)
  (1, 1, 7)
  (3, 4, 5)
  (3, 1, 4)
  (4, 8, 9)
  (4, 1, 3)
  (5, 3, 1)
  */

  return 0;
}
```

### 2. binary_search

구간 내, 해당 원소의 존재 여부를 반환한다.

```cpp
#include <iostream>
#include <vector>
#include <algorithm>

int main()
{
  vector<int> v = {1, 2, 2, 3, 5, 7};
  bool found = binary_search(v.begin(), v.end(), 3);
  cout << (found ? "있음" : "없음") << endl;  // 있음

  return 0;
}
```

### 3. lower_bound, upper_bound

두 함수의 내부는 이분 탐색 기반으로 구현되어 있으며, 대상 배열은 반드시 오름차순으로 정렬되어 있어야 한다.

```cpp
#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;
int main()
{
  vector<int> v = {1, 2, 2, 3, 4, 9, 10, 12};

  auto lb = lower_bound(v.begin(), v.end(), 5); // 5 이상이 되는 첫 번째 원소의 위치를 반환
  auto ub = upper_bound(v.begin(), v.end(), 5); // 5 초과가 되는 첫 번째 원소의 위치를 반환
  // iterator를 반환

  cout << "lower_bound(5): " << "index = " << (lb - v.begin()) << ", " << "value = " << *lb << endl;
  cout << "upper_bound(5): " << "index = " << (ub - v.begin()) << ", " << "value = " << *ub << endl;

  /*
  lower_bound(5): index = 5, value = 9
  upper_bound(5): index = 5, value = 9
  */

  return 0;
}
```

### 4. equal_range

```cpp
#include <iostream>
#include <vector>
#include <algorithm>

int main()
{
  vector<int> v = {1, 2, 2, 2, 3, 5, 7};
  auto range = equal_range(v.begin(), v.end(), 2);

  cout << "2가 시작되는 인덱스: " << (range.first - v.begin()) << endl;		// 1
  cout << "2가 끝난 직후의 인덱스: " << (range.second - v.begin()) << endl;		// 4

  return 0;
}
```
