# demo-c-platform

Insurance platform PoC, C안(퍼블리시 + 제너레이터)의 플랫폼 레포. 아티팩트·워크플로는 B안(`demo-b-platform`)과 같고, 템플릿을 GitHub Template 레포 대신 이 레포의 `templates/`와 `tools/create-project.sh`로 전달한다.

## 퍼블리시 아티팩트 (GitHub Packages, group `io.github.khkim6040.cplatform`)

| 아티팩트 | 내용 |
|---|---|
| `platform-bom` | `spring-boot-dependencies` import + 플랫폼 스타터 버전 제약. 전이 의존성 버전 강제 수단 |
| `platform-catalog` | `gradle/libs.versions.toml` + `platform` 버전 별칭 + `platform-bom`·`platform-http-starter` 라이브러리 별칭. 플러그인 버전의 중앙 출처 |
| `platform-http-starter` | Correlation ID 필터, ProblemDetail 핸들러, `platform-defaults.yml` |

버전은 git 태그 `vX.Y.Z` = 아티팩트 `X.Y.Z`. `git tag v1.0.0 && git push origin v1.0.0` → `publish` 워크플로. 같은 태그에 템플릿과 스크립트가 들어 있으므로 "플랫폼 vX.Y.Z로 생성"이 성립한다.

## 워크플로

- `ci.yml`: 빌드·테스트·로컬 퍼블리시 검증. 템플릿은 빌드하지 않는다(퍼블리시된 패키지가 있어야 빌드 가능)
- `publish.yml`: `v*` 태그 → GitHub Packages
- `release-app.yml`: `workflow_call`. 비즈니스 레포가 `uses: khkim6040/demo-c-platform/.github/workflows/release-app.yml@vX.Y.Z`로 호출. 이 레포의 `Dockerfile`·`.dockerignore`를 같은 커밋에서 가져다 쓴다

## 템플릿과 제너레이터

- `templates/multi-service/`: 비즈니스 모노레포 템플릿. 그 자체로 빌드 가능한 완전한 프로젝트(`rootProject.name = "demo-c-template"`, 패키지 `io.github.khkim6040.sample`)
- `tools/create-project.sh <이름> <패키지> [대상 경로]`: 커밋된 템플릿을 `git archive`로 꺼내 레포 이름·패키지를 치환하고 `git init` + 첫 커밋을 만든다. 치환이 남으면 실패한다. push는 사용자가 한다

```
git clone https://github.com/khkim6040/demo-c-platform.git   # 개발자당 1회
cd demo-c-platform
tools/create-project.sh demo-c-contract contract ../demo-c-contract
cd ../demo-c-contract && git remote add origin https://github.com/khkim6040/demo-c-contract.git && git push -u origin main
```

## 소비 측

`gradle.properties`의 `platformVersion` 하나로 카탈로그를 가져오고, 카탈로그 별칭 `libs.platform.bom`, `libs.platform.http.starter`를 쓴다. 컨벤션 플러그인(`build-logic`)은 템플릿에 복사되며 중앙 업데이트되지 않는다.

로컬 자격 증명: `~/.gradle/gradle.properties`에 `gpr.user`, `gpr.key`(`read:packages` 토큰).
