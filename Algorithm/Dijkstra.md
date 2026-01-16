# 다엑스트라

### 시간 복잡도

1\) 인접 행렬 기준

$$
\text{주어진 정점 수가 V라 할 때,}
$$

$$
O(V^{2})
$$

2\) 인접 리스트 + 우선순위 큐 기준

$$
\text{주어진 정점 수가 V, 간선 수가 E라 할 때,}
$$

$$
O((V+E)log{V})
$$

### 개요

하나의 시작 정점으로부터 그래프의 모든 정점까지의 최단 거리를 구하는 알고리즘. 모든 간선 가중치가 음수가 아닐 때 사용한다.

구현 방식으로 인접 행렬 기반과 인접 리스트 + 우선순위 큐 기반이 있다.<br>
인접 행렬 방식은 매 단계마다 아직 최단 거리가 확정되지 않은 정점들 중에서 거리가 가장 짧은 노드를 선형 탐색으로 찾는다.<br>
이때 다익스트라는 간선의 가중치가 음수가 아니라는 전제가 있으므로, 해당 시점에 선택된 정점의 거리는 이후 어떤 경로를 거쳐도 더
짧아질 수 없어 최단 거리로 즉시 확정될 수 있다.<br>
반면 인접 리스트와 우선순위 큐를 사용하는 방식은 최단 거리 정점을 찾는 과정에 우선순위 큐를 도입하여 탐색 시간을 로그 단위로 단축한다.<br>
다만 거리 갱신 시 큐 내부의 기존 항목을 직접 수정하거나 삭제하지 못하고 새로운 값을 추가하기만 하는 구조적 특성상, 동일한 정점에 대한 과거의 낡은 데이터가 큐에 남게 된다.<br>
따라서 우선순위 큐에서 꺼낸 값이 현재 최신화된 최단 거리인지를 확인하는 필터링 절차를 반드시 거쳐야만 불필요한 연산을 방지하고 우선순위 큐의 효율을 제대로 얻을 수 있다.

### 구현 예시

1\) 인접 행렬 기반

```C++
#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int N = 0, M = 0;
long long INF = 1e10;
vector<vector<long long>> dist;
vector<long long> dp;
vector<bool> is_visited;

int main()
{
  cin.tie(NULL);
  ios::sync_with_stdio(false);

  cin >> N >> M;

  dist.assign(N + 1, vector<long long>(N + 1, INF));
  dp.assign(N + 1, INF);
  is_visited.assign(N + 1, false);

  int u = 0, v = 0;
  long long w = 0;
  for (int i = 0; i < M; i++)
  {
    cin >> u >> v >> w;

    dist[u][v] = min(dist[u][v], w);
  }

  for (int i = 1; i <= N; i++)
    dist[i][i] = 0;

  int start_node = 0, end_node = 0;

  cin >> start_node >> end_node;

  dp[start_node] = 0;
  for (int i = 1; i <= N; i++)
  {
    int current_node = 0;
    long long current_cost = INF;
    for (int j = 1; j <= N; j++)
    {
      if (!is_visited[j] && current_cost > dp[j])
      {
        current_node = j;
        current_cost = dp[j];
      }
    }

    if (current_node == 0)
      break;

    is_visited[current_node] = true;
    for (int j = 1; j <= N; j++)
    {
      if (is_visited[j])
        continue;
      if (dist[current_node][j] == INF)
        continue;

      if (dp[j] > current_cost + dist[current_node][j])
        dp[j] = current_cost + dist[current_node][j];
    }
  }

  cout << dp[end_node];
}
```

2\) 인접 리스트 + 우선순위 큐 기반

```C++
#include <iostream>
#include <vector>
#include <queue>
#include <functional>

using namespace std;

int N = 0, M = 0;
long long INF = 1e10;
vector<vector<pair<int, int>>> graph;
vector<long long> dist;
priority_queue<pair<long long, int>, vector<pair<long long, int>>, greater<pair<long long, int>>> pq;

int main()
{
  cin.tie(NULL);
  ios::sync_with_stdio(false);

  cin >> N >> M;

  graph.assign(N + 1, vector<pair<int, int>>());
  dist.assign(N + 1, INF);

  int u = 0, v = 0, w = 0;
  for (int i = 0; i < M; i++)
  {
    cin >> u >> v >> w;

    graph[u].push_back({v, w});
  }

  int start_node = 0, end_node = 0;

  cin >> start_node >> end_node;

  dist[start_node] = 0;
  pq.push({0, start_node});
  while (!pq.empty())
  {
    int current_node = pq.top().second;
    long long current_dist = pq.top().first;

    pq.pop();

    if (dist[current_node] != current_dist)
      continue;

    for (int i = 0; i < graph[current_node].size(); i++)
    {
      int next_node = graph[current_node][i].first;
      int cost = graph[current_node][i].second;

      if (dist[next_node] > current_dist + cost)
      {
        dist[next_node] = current_dist + cost;
        pq.push({dist[next_node], next_node});
      }
    }
  }

  cout << dist[end_node];
}
```
