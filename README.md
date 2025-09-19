## 📌 프로젝트 소개
Kotlin과 Spring Boot 기반으로 구현한 **RealWorld Backend** 프로젝트입니다.  
[RealWorld 스펙](https://docs.realworld.show/specifications/backend/endpoints/)을 참고하여 개발했으며,  
**도메인 주도 설계(DDD, Domain-Driven Design)** 및 **CQRS(Command Query Responsibility Segregation) 패턴**을 적용했습니다.  
RealWorld 스펙에 정의된 **사용자, 프로필, 아티클, 댓글, 좋아요, 태그** 관련 API를 제공합니다.

- **언어/런타임**: Kotlin 1.9.25, JDK 21  
- **프레임워크**: Spring Boot 3.5.4  
- **빌드 도구**: Gradle (Kotlin DSL)  
- **DB**: H2  
- **ORM/JPA**: Spring Data JPA, Querydsl 5.0.0 (jakarta)  
- **보안/인증**: Spring Security, JWT (jjwt 0.12.6)  
- **직렬화**: Jackson (jackson-module-kotlin)  
- **테스트/품질 관리**:  
  - JUnit5, Spring Boot Test  
  - Mockk, SpringMockk  
  - JaCoCo (코드 커버리지 리포트 및 검증)
  


## 🏗 아키텍처

### DDD
- **Domain Layer**: 비즈니스 규칙과 핵심 로직  
- **Application Layer**: 유스케이스 구현  
- **Infrastructure Layer**: 영속성(JPA), JWT 보안 등  
- **Presentation Layer**: REST Controller 및 DTO  

### CQRS
- **Command와 Query를 분리한 Application Layer**  
  - `Command UseCase`: CUD(Create, Update, Delete) 작업을 처리하며, 트랜잭션을 관리하고 도메인 엔티티를 실행하여 비즈니스 규칙이 적용되도록 조율합니다.
  - `Query UseCase`: 조회 작업에 특화되며, 성능 중심의 Repository를 통해 도메인 경계를 무시한 효율적인 데이터 접근을 지원합니다.  
- **DB는 단일 인스턴스를 사용하지만**, 읽기/쓰기 책임을 애플리케이션 계층에서 분리하여 CQRS의 핵심 개념을 반영했습니다.  



## 📂 프로젝트 구조

``` text
src
 └── main
     ├── realworld
     │   ├── article
     │   │   ├── presentation
     │   │   ├── application
     │   │   │   ├── usecase
     │   │   │   └── query
     │   │   ├── domain
     │   │   │   ├── aggregate
     │   │   │   └── vo
     │   │   └── infrastructure
     │   └── ...
     └── resources
         └── application.yml
```

## 📖 구현 스펙

### 인증 & 사용자
- `POST /api/users/login` — 로그인  
- `POST /api/users` — 회원가입  
- `GET /api/user` — 현재 사용자 정보 조회  
- `PUT /api/user` — 사용자 정보 수정  

### 프로필
- `GET /api/profiles/:username` — 프로필 조회  
- `POST /api/profiles/:username/follow` — 팔로우  
- `DELETE /api/profiles/:username/follow` — 언팔로우  

### 아티클
- `GET /api/articles` — 글 목록 조회 (필터/페이징 지원)  
- `GET /api/articles/feed` — 팔로우한 유저 글 목록  
- `GET /api/articles/:slug` — 글 단건 조회  
- `POST /api/articles` — 글 작성  
- `PUT /api/articles/:slug` — 글 수정  
- `DELETE /api/articles/:slug` — 글 삭제  

### 댓글
- `POST /api/articles/:slug/comments` — 댓글 작성  
- `GET /api/articles/:slug/comments` — 댓글 목록 조회  
- `DELETE /api/articles/:slug/comments/:id` — 댓글 삭제  

### 좋아요
- `POST /api/articles/:slug/favorite` — 좋아요  
- `DELETE /api/articles/:slug/favorite` — 좋아요 취소  

### 태그
- `GET /api/tags` — 태그 목록 조회  

