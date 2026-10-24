# demo-c-platform

Insurance platform PoC, B안(퍼블리시 + 템플릿)의 플랫폼 레포.

## 퍼블리시 아티팩트 (GitHub Packages, group `io.github.khkim6040.platform`)

| 아티팩트 | 내용 |
|---|---|
| `platform-bom` | `spring-boot-dependencies` import + 플랫폼 스타터 버전 제약. 전이 의존성 버전 강제 수단 |
| `platform-catalog` | `gradle/libs.versions.toml` + `platform` 버전 별칭 + `platform-bom`·`platform-http-starter` 라이브러리 별칭. 플러그인 버전의 중앙 출처 |
| `platform-http-starter` | Correlation ID 필터, ProblemDetail 핸들러, `platform-defaults.yml` |

버전은 git 태그 `vX.Y.Z` = 아티팩트 `X.Y.Z`. `git tag v1.0.0 && git push origin v1.0.0` → `publish` 워크플로.

## 워크플로

- `ci.yml`: 빌드·테스트·로컬 퍼블리시 검증
- `publish.yml`: `v*` 태그 → GitHub Packages
- `release-app.yml`: `workflow_call`. 비즈니스 레포가 `uses: khkim6040/demo-c-platform/.github/workflows/release-app.yml@vX.Y.Z`로 호출. 이 레포의 `Dockerfile`·`.dockerignore`를 같은 커밋에서 가져다 쓴다

## 소비 측 (demo-b-template 참고)

`gradle.properties`의 `platformVersion` 하나로 카탈로그를 가져오고, 카탈로그 별칭 `libs.platform.bom`, `libs.platform.http.starter`를 쓴다. 컨벤션 플러그인(`build-logic`)은 템플릿에 복사되며 중앙 업데이트되지 않는다.

로컬 자격 증명: `~/.gradle/gradle.properties`에 `gpr.user`, `gpr.key`(`read:packages` 토큰).
