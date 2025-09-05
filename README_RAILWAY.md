# Railway 배포 가이드

## 배포 준비 완료

### 1. Railway 프로젝트 생성
1. [Railway](https://railway.app) 로그인
2. "New Project" 클릭
3. "Deploy from GitHub repo" 선택

### 2. PostgreSQL 데이터베이스 추가
```bash
# Railway 대시보드에서:
1. "+ New" → "Database" → "Add PostgreSQL"
2. DATABASE_URL이 자동으로 생성됨
```

### 3. 환경변수 설정 (Railway Dashboard → Variables)
```env
# 자동 생성됨
DATABASE_URL=postgresql://...

# RabbitMQ (CloudAMQP 무료 플랜 사용 권장)
RABBITMQ_HOST=your-rabbitmq-host
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=your-username
RABBITMQ_PASSWORD=your-password

# JWT Secret (필수)
JWT_SECRET_KEY=your-very-long-secret-key-here-at-least-256-bits
```

### 4. CloudAMQP 설정 (RabbitMQ)
1. [CloudAMQP](https://www.cloudamqp.com/) 가입
2. 무료 플랜 생성 (Little Lemur)
3. Details에서 연결 정보 복사

### 5. 배포 명령어
```bash
# Git 저장소로 푸시
cd C:\ai_front_mukkabi.com\PART2\ch08\service
git init
git add .
git commit -m "Initial Railway deployment"
git remote add origin YOUR_GITHUB_REPO_URL
git push -u origin main

# Railway가 자동으로 배포 시작
```

### 6. 프론트엔드 연결
프론트엔드의 `axios.config.ts`에서 API URL 업데이트:
```typescript
const API_BASE_URL = "https://your-app.railway.app"
```

## 파일 구조
- `railway.json` - Railway 배포 설정
- `nixpacks.toml` - 빌드 환경 설정
- `application-railway.yaml` - Railway 프로필 설정
- `.env.example` - 환경변수 템플릿

## 주의사항
- Java 11 사용
- 테스트는 빌드 시 제외 (-x test)
- PostgreSQL 자동 마이그레이션 (ddl-auto: update)
- 최대 연결 풀 5개로 제한 (무료 플랜)