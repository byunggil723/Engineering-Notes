# 인증/인가

### 로그인 로직

```TS
import * as bcrypt from 'bcrypt';
// bcrypt 모듈이 export한 모든 것들을 하나의 객체(bcrypt)로 묶어서 가져오라는 뜻

@Injectable()
export class UserService {
  private users = [];

  async register(username: string, password: string) {
    const hashed = await bcrypt.hash(password, 10);
    const user = { id: Date.now(), username, password: hashed };
    this.users.push(user);
    return user;
  }

  async validateUser(username: string, password: string) {
    const user = this.users.find(u => u.username === username);
    if (user && (await bcrypt.compare(password, user.password))) return user;
    return null;
  }
}
```

\* 참고

```TS
import * as bcrypt from 'bcrypt';
-> bcrypt 모듈이 export한 모든 것들을 하나의 객체(bcrypt)로 묶어서 가져오라는 뜻

EX.
bcrypt 모듈 안에 이런 export들이 있다고 가정

export function hash(...) { ... }
export function compare(...) { ... }

이것들을 위와 같이 import하여 아래처럼 사용 가능
import * as bcrypt from 'bcrypt';
bcrypt.hash(...);
bcrypt.compare(...);
```

사용자가 회원 가입 시, 서버는 비밀번호를 해싱하여 DB에 저장한다.
여기서는 비밀번호 해싱에 사용되는 여러 알고리즘 중, bcrypt 알고리즘에 대해 설명한다.

1. 임의의 문자열, `salt` 를 생성한다.
2. `salt` 와 비밀번호를 설정한 `cost = 10` 에 따라 `2^10` 번의 반복 연산을 수행한다.
3. 결과적으로 다음과 같은 문자열을 생성하여 저장된다.

   `$2b$10$Juz7a6Zz6xTKnLRtQOxF4eLxwG6soh7SL4B/YmKfSBGqmvTqRbSuq`

| 구간                              | 내용                                   |
| --------------------------------- | -------------------------------------- |
| `$2b$`                            | bcrypt 알고리즘 버전                   |
| `10$`                             | cost factor (2^10 반복)                |
| `Juz7a6Zz6xTKnLRtQOxF4e`          | salt 문자열 (Base64 인코딩된 16바이트) |
| `LxwG6soh7SL4B/YmKfSBGqmvTqRbSuq` | 최종 해시(비밀번호 + salt의 결과물)    |

4. 로그인 시, 서버는 저장된 해시 문자열로부터
   bcrypt 알고리즘 버전, cost factor, salt 값을 추출한다.
   이후 사용자가 입력한 비밀번호를 동일한 조건으로 다시 해싱하여
   기존 해시값과 비교함으로써 일치 여부를 검증한다.

   사용자가 로그인 과정을 거친 후, 시스템은 해당 사용자가 인증된 상태인지 판별해야 한다.
   이를 구현하는 방식에는 다음과 같은 여러 종류의 방법이 존재한다.

   | 분류                               | 설명                                                                                                                                                                                                  | 대표 예시                       |
   | ---------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------- |
   | **세션 기반 인증 (Session-based)** | 서버가 **로그인 상태(session)**를 서버 메모리나 Redis 등에 **동적으로 저장(stateful)**. 클라이언트는 세션 ID만 쿠키에 담아 전송하며, 서버는 요청마다 세션 저장소에서 사용자를 조회함.                 | PHP/JSP/Express의 전통적 로그인 |
   | **토큰 기반 인증 (Token-based)**   | 서버가 상태를 저장하지 않고(**stateless**), **JWT 같은 토큰 자체에 사용자 정보와 만료시간을 포함**하여 발급. 이후 요청 시 서명 검증만으로 인증 수행.                                                  | JWT, OAuth 2.0, OpenID Connect  |
   | **API Key 기반 인증**              | **사전에 발급된 고정 키**를 요청 헤더에 포함. 서버는 매 요청마다 **DB의 키 목록을 조회하여 유효성 검증**. 세션처럼 클라이언트의 상태를 보관하지는 않으며(**stateless**), 단순히 등록된 키인지만 판단. | OpenAI, Google Maps API         |
   | **OAuth 2.0 / OpenID Connect**     | 제3자 인증 위임 프로토콜.                                                                                                                                                                             | 구글 로그인, 카카오 로그인      |
   | **기타 보조 방식**                 | 메인 인증을 보완하기 위한 보조 수단. 예: CSRF Token(요청 위조 방지), OTP/Device Token(2FA·기기 식별용)                                                                                                | 요청 위조 방지, 2FA 보안용      |

### 세션 기반 인증

세션 기반의 인증 절차는 다음과 같다.

1. 사용자가 아이디/비밀번호를 입력하면 클라이언트(브라우저)가 이를 서버로 전송
2. 서버는 DB에서 해당 사용자를 조회하고 비밀번호를 검증
   성공하면 서버의 세션 저장소(예: 서버 메모리, Redis, DB 등)에 랜덤하게 생성된 세션 ID와 함께 다음과 같은 데이터를 저장한다.

   ```TS
   sessionStore["ab34fe..."] = {
   userId: "user1",
   role: "admin",
   loginTime: 2025-10-18T19:00Z
   }
   ```

3. 서버는 응답 시 Set-Cookie 헤더를 이용해 세션 ID를 브라우저에 내려준다.

   ```bash
   Set-Cookie: sessionId=ab34fe...; Path=/; HttpOnly; Secure; SameSite=Lax

   HttpOnly: JavaScript에서 접근 불가 → XSS 방어
   Secure: HTTPS 환경에서만 전송
   SameSite: CSRF 방어 설정 (Lax 또는 Strict 권장)
   ```

4. 브라우저는 서버에 요청을 보낼 때마다 `withCredentials: true` 속성을 통해 자동으로 쿠키를 포함한다. 서버는 쿠키에 담긴 세션 ID를 세션 저장소에서 찾아보고, 존재하면 인증된 사용자로 판단하여 요청을 처리한다.

5. 일정 시간이 지나면(예: 30분) 자동으로 세션이 만료되고, 사용자가 로그아웃하면 세션 저장소에서 해당 세션을 삭제한다.

   세션 기반 인증은 서버가 로그인한 사용자의 상태(Session)를 직접 저장하는 stateful한 방식이다.
   이로 인해 서버 확장 과정에서 세션 동기화가 필요해지고, 실제 서비스와는 직접 관련이 없는 인프라 관리에 불필요한 리소스가 소모되는 문제가 있다.

   이에 대해서는 세션 클러스터링(Session Clustering), 스티키 세션(Sticky Session), 세션 스토리지 분리(Session Storage Separation) 등 다양한 인프라적 해결책이 존재한다.
   그러나 이러한 방식들은 여전히 서버 측 상태를 유지해야 하므로 완전한 확장성 확보에는 한계가 있다.
   이러한 한계를 보완하기 위해 등장한 방식이 바로 JWT 기반의 무상태(Stateless) 인증 구조이다.

### 토큰 기반 인증

JWT(JSON Web Token)는 인증 정보를 “서버가 아닌 클라이언트”가 직접 보관하도록 설계된 stateless 인증 방식이다.

JWT 토큰은 점으로 구분된 세 개의 파트로 구성된다.

`xxxxx.yyyyy.zzzzz`

| 구분          | 설명                                           |
| ------------- | ---------------------------------------------- |
| **Header**    | 어떤 알고리즘으로 서명했는지 명시 (예: HS256)  |
| **Payload**   | 사용자 정보(claims)와 만료 시간(exp) 등을 포함 |
| **Signature** | 비밀키(Secret)로 Header + Payload를 서명한 값  |

\* Header

```JSON
{
  "alg": "HS256",
  "typ": "JWT"
}
```

\* Payload

```JSON
{
  "sub": "user123",
  "role": "admin",
  "exp": 1712345678
}
```

JWT 토큰의 생성 과정은 다음과 같다.

1. Header와 Payload를 문자열로 직렬화 (stringify) 한다.

   ```TS
   const header = { alg: "HS256", typ: "JWT" };
   const payload = { "userId": 123, "role": "user" };
   const headerJson = JSON.stringify(header); // '{"alg":"HS256","typ":"JWT"}'
   const payloadJson = JSON.stringify(payload); // '{ "userId": 123, "role": "user" }'
   ```

2. 문자열로 변환한 각각의 Header와 Payload를 base64url로 인코딩한다.

   ```TS
   const encodedHeader = base64Url(Header);
   const encodedPayload = base64Url(Payload);
   ```

3. HMAC_SHA256 알고리즘과 비밀키를 통해 JWT 토큰의 Signature 부분을 생성한다.

   ```TS
   const data = `${encodedHeader}.${encodedPayload}`;
   const Signature = HMAC_SHA256( data, secret );
   const encodedSignature = base64Url(Signature);
   ```

\* `HMAC-SHA256`: 내부적으로 SHA-256 해시 함수를 사용하는 “메시지 인증 코드(Message Authentication Code, MAC)” 알고리즘

4. 최종적으로 Header, Payload, Signature를 점으로 이어 붙여 JWT 토큰을 생성한다.

   ```TS
   const JWT = encodedHeader + "." + encodedPayload + "." + encodedSignature
   ```

   NestJS에서는 보통 @nestjs/jwt 패키지를 사용한다.

   ```TS
   import { JwtService } from '@nestjs/jwt';

   const jwtService = new JwtService({
     secret: 'mySuperSecretKey', // 비밀키
     signOptions: { algorithm: 'HS256' }, // 서명 알고리즘
   });

    const payload = { userId: 123, role: 'user' };

    const token = jwtService.sign(payload);
    console.log(token);
   ```

클라이언트로부터 받은 JWT 토큰을 검증하는 과정은 다음과 같다.

1. 전달 받은 토큰에서 Header와 Payload 부분을 추출
2. Header.Payload 부분을 똑같이 HMAC_SHA256(header.payload, secret)으로 해싱
3. 생성된 값과 Signature를 비교
4. Signature는 토큰 발급 시 생성된 값이므로, 중간에 토큰이 조작되었다면 검증 시 새로 계산한 서명과 일치하지 않게 됨

   ```TS
   try {
     const payload = this.jwtService.verify(token);
     req.user = payload; // 유효하면 payload 추출하여 req.user = payload 로 저장
     next(); // next()를 호출해야 다음 처리 단계(컨트롤러나 라우트 핸들러)로 진행 가능
   } catch (error) {
     throw new UnauthorizedException('유효하지 않은 토큰입니다.');
   }

   // 정상 토큰이면 → payload 반환
   // 위조/만료된 토큰이면 → UnauthorizedException (401) 발생
   ```

JWT는 토큰 자체의 서명으로 유효성을 검증하므로, 서명에 쓰인 비밀키가 유출되면 공격자는 임의의 Payload를 만들어 HS256(HMAC-SHA256)으로 서명하여 유효한 토큰을 생성할 수 있다.
또한, 사용자 식별 정보와 권한 등의 데이터를 Payload에 직접 포함하므로, 일반적인 세션 ID 기반 인증보다 토큰의 크기가 상대적으로 크다. 이로 인해 매 요청마다 Authorization 헤더에 포함되어 전송되는 데이터 양이 증가하여 트래픽 부담이 커질 수 있다.

### OAuth 2.0 + OpneId Connect

OAuth 2.0은 특정 애플리케이션 서버(클라이언트) 가 사용자 대신 리소스 서버에 접근하여
사용자에 대한 특정 정보를 받아올 권한(Access Token) 을 부여받을 수 있도록 하는 인가(Authorization) 프로토콜이다.

OpenID Connect는 OAuth 2.0 위에 인증(Authentication) 계층을 추가한 프로토콜이다.
즉, 리소스 서버는 OAuth 2.0이 발급하는 Access Token과 함께,
현재 로그인을 시도하는 사용자가 누구인지를 명시하는 ID Token(JWT 기반) 을 추가로 제공한다.

카카오 로그인은 이러한 OAuth 2.0 + OpenID Connect 구조를 따르는 대표적인 예시이다.
카카오 로그인 서비스를 통해 OAuth 2.0 + OpenID Connect 구조를 살펴보자.

카카오 로그인 서비스의 내부적인 절차는 다음과 같다.

1. 클라이언트가 앱 서버의 api/auth/kakao 엔드포인트로 로그인 요청을 보낸다.

2. 서버는 카카오의 공식 로그인 페이지로 클라이언트를 리다이렉트시키며, 이때 URL에는 다음 정보가 포함된다.
   `클라이언트 ID`
   `리다이렉트 URI`
   `응답 타입 (response_type=code)`

   ```TS
   // NestJS Controller
   @Get('api/auth/kakao')
   redirectToKakao(@Res() res: Response) {
     const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=${process.env.KAKAO_CLIENT_ID}&redirect_uri=${process.env.KAKAO_REDIRECT_URI}&response_type=code`;
     return res.redirect(KAKAO_AUTH_URL);
   }
   ```

3. 리다이렉트된 페이지에서 사용자가 카카오 계정으로 로그인을 완료하면, 카카오 서버는 등록된 리다이렉트 URI로 302 응답을 보낸다. 이때 인가 코드(code) 가 쿼리 파라미터에 포함되어 전송된다.

   ```bash
   HTTP/1.1 302 Found
   Location: https://api.myapp.com/api/auth/kakao/callback?code=abcd1234
   ```

4. 브라우저는 리다이렉트와 동시에 앱 서버의 `api/auth/kakao/callback` 엔드포인트로 GET 요청을 보낸다.

5. 앱 서버는 쿼리 파라미터에 담긴 code 값을 추출하여, 카카오 서버로 액세스 토큰(Access Token)과 리프레시 토큰(Refresh Token)을 요청한다.
   이때 교환된 토큰은 앱 서버가 카카오 API에 접근할 수 있는 권한 증표이다.

6. 발급받은 액세스 토큰을 이용해 카카오 서버로부터 사용자의 고유한 프로필 정보를 요청한다.
   이 과정에서 앱 서버 개발자는 사용자 정보의 어떤 항목을 받을지 사전에 정의하고,
   사용자는 로그인 시 해당 정보 제공에 대한 동의를 해야 한다.

   ```TS
   async getUserInfo(accessToken: string) {
     const response = await axios.get('https://kapi.kakao.com/v2/user/me', {
       headers: { Authorization: `Bearer ${accessToken}` },
     });
     return response.data; // { id, kakao_account: { profile, email, ... } }
   }
   ```

7. 받아온 사용자 고유 식별 정보(예: kakaoId)를 이용해 앱 서버의 DB에 해당 사용자가 존재하는지 확인한다.

8. 기존 사용자라면, 앱 서버는 자체적으로 관리하는 Access Token과 Refresh Token을 새로 발급하고, `api/auth/kakao/callback` 요청에 대한 응답으로 클라이언트에 반환한다.

OAuth 2.0 + OpenID Connect 로그인 과정에서 authorization code가 URL에 노출되지 않도록 다음을 반드시 고려해야 한다.

- 서버 콜백(redirect_uri) → 백엔드 도메인 한정
- 콜백 직후 즉시 토큰 교환 + 코드 제거 리다이렉트
- state 파라미터 (CSRF 방지)
- PKCE (SPA, 모바일 필수)
- HTTPS + HSTS
- Referrer-Policy / CSP / XSS 방어 / 로그 마스킹
- 코드 단회성 보장 & 짧은 만료 시간

이 원칙들을 지키면, 카카오 로그인 등 OAuth 2.0 + OIDC 기반 인증에서
인가 코드 탈취와 위조 위험을 효과적으로 차단할 수 있다.
