# 플로이드-워셜 알고리즘

플로이드 워셜은 모든 노드 쌍 간 최단 거리(APSP, All Pairs Shortest Paths)가 필요할 때 사용하는 알고리즘이다.<br>
플로이드–워셜은 중간에 경유할 수 있는 노드 집합을 $\{1\}, \{1,2\}, ..., \{1...k\}$
처럼 단계적으로 확장해 가며 최단 거리를 확정하는 DP 방식으로 동작한다.<br>
k번째 노드를 새로운 경유지로 허용하여 판단할 때, i에서 j까지 최단 거리($dist[i][j]$)가 경유 가능한 노드 집합 $\{1...k−1\}$에 대해 확정되어 있어야 하며,<br>
그 확정된 값을 기반으로 k번째 노드를 경유하는 비용($dist[i][k] + dist[k][j]$)이 기존 경로($dist[i][j]$)와 비교해 더 작으면 갱신하는 방식으로 진행된다.<br>
따라서 경유 가능한 노드를 하나씩 확장하는 순서를 보장하기 위해 3중 반복문에서 반드시 k가 최외곽(for k → for i → for j)에 오도록 구현한다.<br>
알고리즘 수행이 끝난 뒤 어떤 정점 i에 대해 dist[i][i] < 0 인 경우, 정점 i에서 출발해 다시 i로 돌아오는 최소 비용이 음수라는 의미이므로, 이는 곧 음수 사이클이 존재한다는 것과 동치이다.

### 구현 예제 코드

```C++
#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int n = 0, m = 0;
long long INF = 1e12;
vector<vector<long long>> dist;

int main()
{
  cin.tie(NULL);
  ios::sync_with_stdio(false);

  cin >> n >> m;

  dist.assign(n + 1, vector<long long>(n + 1, INF));

  int a = 0, b = 0;
  long long c = 0;
  for (int i = 0; i < m; i++)
  {
    cin >> a >> b >> c;

    dist[a][b] = min(dist[a][b], c); // (long long, int)와 같이 두 대상의 타입이 다르면 비교 불가
  }

  for (int i = 1; i <= n; i++)
  {
    dist[i][i] = 0;
  }

  for (int k = 1; k <= n; k++)
  {
    for (int i = 1; i <= n; i++)
    {
      if (dist[i][k] == INF) // i에서 k로 가는 경로가 없는 경우
        continue;
      for (int j = 1; j <= n; j++)
      {
        if (dist[k][j] == INF) // k에서 j로 가는 경로가 없는 경우
          continue;

        if (dist[i][j] > dist[i][k] + dist[k][j])
        {
          dist[i][j] = dist[i][k] + dist[k][j];
        }
      }
    }
  }

  for (int i = 1; i <= n; i++)
  {
    for (int j = 1; j <= n; j++)
    {
      if (dist[i][j] == INF) // 최종적으로 i에서 j로 가는 경로가 없는 경우
        cout << 0 << ' ';
      else
        cout << dist[i][j] << ' ';
    }
    cout << '\n';
  }
}
```

### 관련 문제

\- [BOJ 11404 플로이드](/Algorithm/BOJ/11404.md)<br>
\- [BOJ 11780 플로이드 2](/Algorithm/BOJ/11780.md): 최단 거리의 경로를 복원해야 하는 문제
