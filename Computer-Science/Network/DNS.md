# DNS Record

### DNS Record 필드

| 레코드 | 이름               | 역할            | 상세 설명                                                  | 예시                                                      |
| ------ | ------------------ | --------------- | ---------------------------------------------------------- | --------------------------------------------------------- |
| A      | Address            | 도메인 → IPv4   | 도메인 이름을 IPv4 주소로 변환하여 웹 서버에 연결한다.     | example.com → 93.184.216.34                               |
| AAAA   | Address v6         | 도메인 → IPv6   | 도메인 이름을 IPv6 주소로 변환한다.                        | example.com → 2001:db8::1                                 |
| CNAME  | Canonical Name     | 도메인 → 도메인 | 하나의 도메인을 다른 도메인의 별명으로 연결한다.           | www.example.com → example.com                             |
| MX     | Mail Exchange      | 메일 서버 지정  | 해당 도메인의 메일을 처리할 서버를 우선순위로 지정한다.    | example.com → mail.example.com (10)                       |
| TXT    | Text               | 인증/설정 정보  | 도메인 소유 인증, 보안 정책(SPF, DKIM 등)을 저장한다.      | v=spf1 include:\_spf.google.com ~all                      |
| SOA    | Start of Authority | Zone 관리       | Zone의 관리자 정보와 동기화 기준을 정의한다.               | example.com. IN SOA ns1.example.com admin.example.com ... |
| NS     | Name Server        | DNS 관리        | 해당 도메인을 관리하는 DNS 서버를 지정한다.                | example.com → ns1.example.com                             |
| PTR    | Pointer            | IP → 도메인     | IP 주소를 도메인 이름으로 변환하는 역방향 조회에 사용된다. | 8.8.8.8 → dns.google                                      |
| SRV    | Service            | 서비스 위치     | 특정 서비스의 서버 주소와 포트를 지정한다.                 | \_sip.\_tcp.example.com → sip.example.com:5060            |
| UINFO  | User Info          | 사용자 정보     | 사용자 관련 정보를 저장한다 (현재 거의 사용되지 않음).     | user info                                                 |
| MINFO  | Mailbox Info       | 메일 정보       | 메일 박스 및 메일링 리스트 정보 (현재 거의 사용되지 않음). | mailbox.example.com                                       |
| HINFO  | Host Info          | 호스트 정보     | 서버의 하드웨어 및 운영체제 정보를 저장한다.               | "Intel" "Linux"                                           |

### SOA 내부 필드

| 필드    | 이름         | 의미        | 상세 설명                                      | 예시              |
| ------- | ------------ | ----------- | ---------------------------------------------- | ----------------- |
| MNAME   | Primary NS   | 주 서버     | Zone을 대표하는 기본 DNS 서버                  | ns1.example.com   |
| RNAME   | Admin Mail   | 관리자 메일 | 관리자 이메일 (@ 대신 . 사용)                  | admin.example.com |
| SERIAL  | Version      | 버전 번호   | Zone 변경 여부를 판단하는 기준 번호            | 2026013101        |
| REFRESH | Refresh Time | 갱신 주기   | 보조 DNS가 주 서버에 변경 여부를 확인하는 주기 | 3600 (1시간)      |
| RETRY   | Retry Time   | 재시도      | 주 서버 연결 실패 시 재시도 간격               | 1800 (30분)       |
| EXPIRE  | Expire Time  | 만료 시간   | 일정 기간 동기화 실패 시 Zone 무효 처리        | 604800 (7일)      |
| MINIMUM | Minimum TTL  | 최소 TTL    | 캐시 유지 시간 (Negative Cache 기준)           | 86400 (1일)       |

\* 실제 SOA 작성 형태

```
example.com. IN SOA ns1.example.com. admin.example.com. (
  2026013101
  3600
  1800
  604800
  86400
)
```

### Zone

일반적으로 `www.example.com`, `api.example.com` 등과 같은 서브 도메인들은 `example.com` Zone 안에서 A, CNAME, MX 등의 레코드로 관리하지만,<br>
특정 서브 도메인을 별도의 Zone으로 분리할 경우 부모 Zone에서 해당 서브 도메인에 대한 관리 권한을 전용 NS로 위임하고,<br>
해당 서브도메인에 대한 Zone 파일을 따로 생성하여 관리한다.
