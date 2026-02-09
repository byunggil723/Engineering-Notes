# 벨만-포드

### 시간 복잡도

$$
\text{주어진 정점 수가 V, 간선 수가 E라 할 때,}
$$

$$
O(VE)
$$

### 개요

음수 가중치가 존재하는 그래프에서 하나의 시작 정점으로부터 다른 모든 정점까지의 최단 거리를 구하는 알고리즘.<br>
이 알고리즘의 가장 큰 특징은 다익스트라 알고리즘과 달리, 음수 사이클의 존재 여부까지 판별할 수 있다는 점이다.

### 구현 예시

정점의 개수가 V개인 그래프에서, 모든 정점에 대해 현재까지 반영된 해당 정점의 거리 값을 바탕으로 인접한 정점들로의 거리를 반복적으로 갱신하는 과정을 전체 V−1번 수행한다고 하자.<br>
그러면 각 반복 과정에서 여러 길이의 경로가 갱신되겠지만, k번째 반복에 대해 간선이 k개인 경로 중에서는 최단 거리가 확정된 경로가 반드시 하나 이상 존재할 것이다.<br>
따라서 이후 반복에서 이러한 경로들이 기준이 되어 최단 거리가 점진적으로 전파되고, 정점의 개수가 V개인 그래프에서 사이클을 제외한 최단 경로는 최대 V−1개의 간선을 가지므로, V−1번째 반복이 끝나면 전체 최단 경로까지 모두 계산될 것이다.<br>
그런데 만약 모든 정점에 대해 인접한 정점으로의 거리 갱신 과정을 한 번 더 수행했을 때 여전히 값이 갱신된다면, 이는 최단 거리가 무한히 감소할 수 있는 음의 사이클이 존재함을 의미한다.<br>

```C++
#include <iostream>
#include <vector>

using namespace std;

int INF = 70000000;
int V = 0, E = 0;
vector<vector<pair<int, long long>>> graph; // (노드, 가중치)
vector<long long> cost;
bool is_cycle = false;

void search(int start_node)
{
  // 시작 노드를 0으로 설정
  cost[start_node] = 0;

  for (int i = 0; i < V - 1; i++)
  {
    for (int target_node = 1; target_node <= V; target_node++)
    {
      if (cost[target_node] == INF)
        continue;

      for (int j = 0; j < graph[target_node].size(); j++)
      {
        int next_node = graph[target_node][j].first;
        long long next_weight = graph[target_node][j].second;

        if (cost[next_node] > cost[target_node] + next_weight)
        {
          cost[next_node] = cost[target_node] + next_weight;
        }
      }
    }
  }

  // 음의 사이클 판별
  for (int target_node = 1; target_node <= V; target_node++)
  {
    if (cost[target_node] == INF)
      continue;

    for (int i = 0; i < graph[target_node].size(); i++)
    {
      int next_node = graph[target_node][i].first;
      long long next_weight = graph[target_node][i].second;

      if (cost[next_node] > cost[target_node] + next_weight)
      {
        is_cycle = true;
        return;
      }
    }
  }
}

int main()
{
  cin.tie(NULL);
  ios::sync_with_stdio(false);

  cin >> V >> E;

  graph.assign(V + 1, vector<pair<int, long long>>());
  cost.assign(V + 1, INF);

  int u = 0, v = 0, w = 0;
  for (int i = 0; i < E; i++)
  {
    cin >> u >> v >> w;

    graph[u].push_back({v, w});
  }

  search(1);

  if (is_cycle)
  {
    cout << -1;
  }
  else
  {
    for (int i = 2; i < cost.size(); i++)
    {
      if (cost[i] != INF)
      {
        cout << cost[i] << '\n';
      }
      else
      {
        cout << -1 << '\n';
      }
    }
  }
}
```

