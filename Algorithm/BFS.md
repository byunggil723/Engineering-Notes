# 너비 우선 탐색(Breadth First Search)

### 시간 복잡도

1\) 인접 리스트 (Adjacency List) 기준

$$
\text{주어진 정점 수가 V, 간선 수가 E라 할 때,}
$$

$$
O(V+E)
$$

2\) 인접 행렬 (Adjacency Matrix) 기준

$$
\text{주어진 정점 수가 V라 할 때,}
$$

$$
O(V^{2})
$$

### 개요

BFS는 DFS와 달리 한 번의 BFS 함수 호출로 끝낸다.
단, BFS가 구현되게 하는 핵심 알고리즘이 함수 내부에 while 반복문으로 설계되어 있다.

### 공통된 로직 순서

1. DFS든, BFS든 시작할 노드를 받을 수 있게끔 짠다.
2. 현재 방문 중인 노드의 방문 처리부터 한다.
3. 수행해야 할 작업을 처리한다.
4. 그 다음으로 방문할 노드를 선택한다.

### 구현 예시

```C++
#include <iostream>
#include <vector>
#include <queue>

using namespace std;

int N = 0;
vector<vector<int>> graph;
vector<bool> isVisited;

void bfs(int startNode)
{
  queue<int> q;
  q.push(startNode);
  isVisited[startNode] = true; // 초기 작업

  // 탐색 시작
  while (!q.empty())
  {
    int node = q.front(); // 1. 가장 먼저 들어 온 노드부터 탐색, 현재 노드로 지정
    q.pop();
    cout << node << " "; // 2. 해당 노드에서 수행할 작업

    // 3. 작업 수행이 끝난 후, 다음으로 갈 노드 탐색
    for (int i = 0; i < graph[node].size(); i++)
    {
      int nextNode = graph[node][i]; // 4. 다음 노드 지정
      if (!isVisited[nextNode])
      {
        isVisited[nextNode] = true;
        q.push(nextNode);
        // 5. 미방문 노드 시, 방문 처리 후 큐에 삽입
        // (만약 삽입 바로 전에 방문 처리를 하지 않는다면, 노드가 중복되어 큐에 들어 갈 수 있다.)
        // DFS와 달리 바로 진입하지 않고, 후보 노드를 큐에 넣어 두기 때문에,
        // 큐의 FIFO 특성 상, 자연스러운 너비 우선 탐색이 가능해 진다.
      }
    }
    // for 문은 현재 노드에서 갈 수 있는 다음 노드를 탐색하는 과정인데,
    // BFS에서 for 문이 종료되었다는 것은 현재 노드에서 갈 수 있는 노드는 모두 큐에 넣어 둔 것
  }

  return;
}

int main()
{
  cin.tie(NULL);
  ios::sync_with_stdio(false);

  cin >> N;

  graph.assign(N + 1, vector<int>());
  // 노드의 번호가 1 ~ N인 경우 편의를 위해 N + 1개 추가
  // 각 노드(벡터)별로 담길 값은, 각 노드가 갈 수 있는 그 다음 노드 번호들

  isVisited.assign(N + 1, false);

  graph[1].push_back(2);
  graph[1].push_back(3);
  graph[2].push_back(1);
  graph[2].push_back(4);
  graph[2].push_back(5);
  graph[3].push_back(1);
  graph[3].push_back(5);
  graph[4].push_back(2);
  graph[4].push_back(6);
  graph[5].push_back(2);
  graph[5].push_back(3);
  graph[5].push_back(6);
  graph[6].push_back(4);
  graph[6].push_back(5);

  bfs(1);
}
```
