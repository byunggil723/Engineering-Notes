# OSI 7-Layer

| OSI 계층 | 이름             | 주요 프로토콜 / 예시                | 설명                      |
| -------- | ---------------- | ----------------------------------- | ------------------------- |
| 7        | 응용 계층        | HTTP, HTTPS, SMTP, WebSocket, JWT   | 사용자 요청/응답 처리     |
| 6        | 표현 계층        | JSON, Base64, Base64URL, JWT 인코딩 | 데이터 인코딩/압축/형식화 |
| 5        | 세션 계층        | TLS 세션, SSH                       | 세션 관리, 연결 유지      |
| 4        | 전송 계층        | TCP, UDP                            | 신뢰성·순서 보장          |
| 3        | 네트워크 계층    | IP, ICMP, 라우터                    | 주소 지정, 경로 설정      |
| 2        | 데이터 링크 계층 | Ethernet, Wi-Fi, MAC                | 프레임 전송, 오류 검출    |
| 1        | 물리 계층        | 케이블, 신호, 전파                  | 비트를 실제 신호로 변환   |

#### 브라우저에서 데이터가 전송되는 과정을 통해 OSI 7-Layer에 대해 살펴 보자.

OSI 계층은 통신 과정에서 수행되는 작업의 목적을 기준으로 분류한다.

#### 1. 응용 계층 (L7)

1\) 브라우저(응용 프로그램) 내부에서 fetch API 호출을 통해 HTTP 요청 생성이 시작된다.

```js
fetch("/api/login", {
  method: "POST",
  body: JSON.stringify({ id: "user1" }),
});
```

2\) 브라우저는 위 호출을 바탕으로 HTTP 요청 메세지를 구성한다.

```http
POST /api/login HTTP/1.1
Host: example.com
Content-Type: application/json
Content-Length: 14

{"id":"user1"}
```

3\) 브라우저는 서버와 TCP 연결을 맺기 위해 도메인 이름(example.com)을 IP 주소로 변환한다.
필요한 경우 OS의 Name Resolver를 호출하여 DNS Resolution을 수행한다.

\* TCP(Transmission Control Protocol): 전송 제어 프로토콜

```C
getaddrinfo("example.com", "443", ...);

/*
1. /etc/hosts 확인

127.0.0.1     localhost
192.168.0.10  myserver

2. 없으면 로컬 캐시 확인 (Name Resolver 관여)

3. 로컬 캐시에도 없으면 DNS 서버에 질의 (DNS Resolution)
/*
```

→ 최종적으로 목적지 서버의 IP 주소 및 포트 정보 획득

4\) 소켓 객체 생성

소켓(Socket)은 IP 주소, 포트 번호, 연결 상태, 송·수신 버퍼 등 네트워크 통신에 필요한 상태 정보를 관리하는 커널 객체이다.<br>
브라우저 내부에는 네트워크 요청을 관리하기 위한 네이티브 모듈(클래스)이 존재하며,
이 모듈은 커널이 생성한 소켓 객체를 파일 디스크립터(File Descriptor, fd)를 통해 참조한다.<br>
즉, 브라우저는 소켓을 직접 구현하는 것이 아니라, 커널이 제공하는 소켓을 시스템 콜을 통해 생성·사용한다.

```C
int fd = socket(
  AF_INET,        // 주소 체계: IPv4
  SOCK_STREAM,    // 전송 의미: 바이트 스트림 (연결 지향)
  IPPROTO_TCP     // 전송 프로토콜: TCP or UDP
);
```

네트워크 요청(fetch)이 발생하면, 브라우저는 무조건 새 소켓을 생성하지 않는다.
대신 브라우저(사용자 공간)에서 먼저 기존 소켓의 재사용 가능 여부를 판단한다.

```
요청 발생 (fetch)
↓
[Connection Pool 조회]
↓
[같은 scheme + host + port]
├─ 사용 가능한 연결 있음 → 재사용
└─ 없음 → socket() 호출
```

socket() 시스템 콜이 성공하면 커널은 새로운 소켓 객체를 생성하고,
이를 참조하기 위한 파일 디스크립터(fd)를 브라우저 프로세스에 반환한다.<br>
브라우저는 반환받은 fd를 저장한 뒤,
connect, send, recv 등의 시스템 콜을 호출할 때 해당 fd를 인자로 전달하여
동일한 커널 소켓 객체를 지속적으로 사용한다.<br>

소켓 객체에는 대략 다음과 같은 상태 정보를 저장하게 된다.

- 로컬 IP / 포트 (초기에는 미정일 수 있으며, connect() 또는 bind() 이후 확정)
- 원격 IP / 포트 (연결 수립 후 확정)
- TCP 상태 (예: CLOSED → SYN_SENT → ESTABLISHED)
- 송신 버퍼 / 수신 버퍼
- 재전송 큐
- 시퀀스 번호
- 흐름 제어 및 혼잡 제어 상태 등

이러한 정보는 모두 커널 공간에서 관리되며, 사용자 공간에서는 직접 접근할 수 없다.

\* 파일 디스크립터(File Descriptor): Unix 계열 운영체제에서 파일, 네트워크 소켓, 파이프 등 각종 입출력 리소스에 접근하기 위해 사용되는 정수형 식별자를 말한다.

#### 2. 표현 계층 (L6)

HTTP 메시지를 **전송 가능한 바이트열** 로 만든다 (직렬화 & 인코딩)

```
7B 20 22 69 64 22 3A 20 22 75 73 65 72 31 22 20 7D
```

문자열 → 바이트열 변환은 보통 **브라우저(유저 공간)** 내부에서 수행된다.
커널은 이미 만들어진 바이트열을 소켓 버퍼로 받아 네트워크로 흘려보내는 역할이 핵심이다.

#### 3. 세션 계층 (L5)

**세션(session)** 이란 두 통신 주체가 한 번의 대화(통신)를 지속적으로 이어가기 위해 공유하는 논리적 상태(state)와 그 유지 기간을 말한다.

전송 계층에서 TCP 연결이 수립된 뒤, 보안 세션 설정을 위해 수행되는 TLS 핸드셰이크가 일반적으로 세션 계층(또는 표현 계층)으로 분류된다.<br>
또한 JWT, WebSocket 등의 기술들도 통신을 세션 단위로 유지·관리하는 기능을 포함하므로, 실제 구현에서는 다른 계층의 기능과 결합된 형태로 세션 계층의 역할을 일부 담당한다고 볼 수 있다.

#### 4. 전송 계층 (L4)

1\) TCP 연결 (TCP 3-way handshake)

```C
connect(fd, ip, port);
```

이 호출로 커널은 목적지 IP:PORT(예: 93.184.216.34:443)로 TCP 연결을 수립한다.<br>
성공적으로 연결되면, 커널 내부의 이 fd 소켓의 상태는 ESTABLISHED가 된다.

TCP 3-way handshake 수행 과정<br>
(3-way Handshake, Synchronize -> Acknowledgment, Synchronize -> Acknowledgment)

```bash
(1) Client → Server : [TCP Header (SYN(x))]               // payload 없음(또는 0)
(2) Server → Client : [TCP Header (SYN(y) + ACK(x + 1))]  // payload 없음(또는 0)
(3) Client → Server : [TCP Header (ACK(y + 1))]           // payload 없음(또는 0)
```

예를 들어, 클라이언트가 x = 1000 으로 시작했다 하면,
(1) SYN: Seq = 1000
(2) SYN-ACK: Seq = 50000, Ack = 1001
(3) ACK: Seq = 1001, Ack = 50001
이 상태에서 클라이언트가 사진 데이터 1460바이트를 첫 전송하면,
Seq = 1001
payload 길이 = 1460
그러면 다음 세그먼트 Seq는: Seq = 1001 + 1460 = 2461

→ 패킷 순서 보장 기반 구축

2\) 보안 세션 생성 (TLS handshake)

TLS 핸드셰이크는 보통 커널이 아니라 브라우저(사용자 공간)의 TLS 스택이 수행한다.
커널은 TLS를 알지 못하며, TLS 메시지도 그냥 TCP 위를 흐르는 바이트로 취급된다.

1. 브라우저 → ClientHello 전송: 브라우저가 서버에게 TLS 핸드셰이크 시작을 요청

   ```C
   // [TLS Record] ClientHello 메세지 (cipher suites, random, SNI 등 포함)
   send(fd, client_hello_bytes, client_hello_len, 0);
   ```

   \* [TLS Record]

   ```C
   [TCP Header | TCP Payload]
                      ↑
                     이 곳에 [TLS Record Header | TLS Payload(Handshake message)] 가 들어감
   ```

2. 서버 → ServerHello + Certificate 수신: 인증서 + 공개키(암호화 전용 키) 전달

   ```C
   // [TLS Record] ServerHello 메세지 수신
   recv(fd, server_hello_bytes, server_hello_len, 0);

   // [TLS Record] Certificate 수신 (서버 인증서 체인 포함)
   recv(fd, cert_bytes, cert_len, 0);
   ```

3. 브라우저 → 인증서(CA) 검증

   ```C
   // 브라우저 자체 작업
   verify_certificate(cert_bytes);
   ```

4. 브라우저 → Pre-Master Secret 생성

   ```C
   // 브라우저 자체 작업
   pre_master_secret = generate_random_bytes();
   ```

5. 브라우저 → Pre-Master Secret을 서버 공개키로 암호화해 서버로 전송

   ```C
   // 브라우저 자체 작업
   encrypted_pms = RSA_encrypt(server_public_key, pre_master_secret);

   // [TLS Record] ClientKeyExchange에 encrypted_pms를 담아 전송
   send(fd, client_key_exchange_bytes(encrypted_pms),
       client_key_exchange_len, 0);
   ```

6. 서버 → 개인키(복화화 전용 키)로 복호화

   ```C
   // 서버 내부 작업
   pre_master_secret = RSA_decrypt(server_private_key, encrypted_pms);
   ```

7. 결과적으로 브라우저와 서버 둘만 알게 되는 유일한 Session Key를 서로 갖게 됨

8. Finished 교환 (이제부터 메시지는 암호화되기 시작)

   브라우저 → 서버

   ```C
   // (유저 공간 내부 처리)
   // 이제 session_key를 알고 있으니 Finished 메시지를 암호화해서 보낼 수 있음
   send(fd, finished_from_client_encrypted, finished_client_len, 0);
   ```

   서버 → 브라우저

   ```C
   recv(fd, finished_from_server_encrypted, finished_server_len, 0);
   ```

   \* 참고

   ```C
   send(fd, buf, len, 0)
   ```

   buf: 커널로 넘길 바이트 배열의 시작 주소(pointer)<br>
   len: 그 바이트 배열의 길이(몇 바이트 보낼지)

→ TLS 세션 구축 완료 이후, 애플리케이션 데이터(HTTP 평문)는 TLS 세션키(대칭키)로 암호화되어 TLS Record의 Payload에 담겨 전송되며, 수신 측은 동일한 키로 TLS Payload를 복호화한다.

```C
[TCP Header | TCP Payload]
                    ↑
            [TLS Record Header | TLS Payload(암호화된 HTTP)]
```

#### 5. 네트워크 계층 (L3)

네트워크 계층에서는 전송 계층에서 생성된 TCP 세그먼트가 IP 헤더로 캡슐화되어 IP 패킷(IP datagram)이 되며, 이후 목적지 IP를 기준으로 라우팅되어 다음 홉으로 전달된다.

```
[IP Header | TCP Header | TCP Payload]
```

#### 6. 데이터 링크 계층 (L2)

네트워크 계층에서 만들어진 IP 패킷(IP 데이터그램)은 데이터 링크 계층에서 링크 기술에 맞는 프레임(frame)으로 캡슐화되어 물리 매체로 전송된다.<br>
데이터 링크 계층의 기술은 이더넷/와이파이뿐만 아니라 PPP/PPPoE, LTE/5G, DOCSIS, MPLS 등 각 통신 환경(유선, 무선, 통신사망 등)에 따라 매우 다양하다.<br>
다만 일반적인 LAN 환경에서 가장 대표적으로 사용되는 L2 기술은 유선 이더넷(IEEE 802.3) 과 무선 Wi-Fi(IEEE 802.11) 이다.

유선(이더넷 링크)로 보낼 때:<br>

```
[Ethernet Header | IP Header | TCP/UDP Header | TCP/UDP Payload(= Data) | FCS]
```

무선(와이파이 링크)로 보낼 때:<br>

```
[802.11 Header | IP Header | TCP/UDP Header | TCP/UDP Payload(= Data) | (무선 FCS)]
```

#### 7. 물리 계층 (L1)

이터 링크 계층에서 생성된 이더넷 프레임은 물리 계층에서 전기 신호(UTP), 빛 신호(광섬유), 전파(무선) 등의 형태로 변환되어 실제 전송 매체를 통해 전달된다.<br>
물리 계층은 비트(bit) 단위의 전송을 담당하며, 프레임의 의미나 주소(MAC/IP), 신뢰성/재전송과 같은 논리적 처리는 수행하지 않는다.
