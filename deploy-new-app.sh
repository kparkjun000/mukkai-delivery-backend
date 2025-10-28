#!/bin/bash

# 새 Heroku 앱 생성 및 배포 스크립트
echo "🚀 Mukkai 백엔드 새 앱 생성 및 배포 시작..."

cd /Users/junho/Downloads/mukkai-delivery-backend

# 1. Heroku 로그인
echo "📝 1단계: Heroku 로그인 중..."
echo "브라우저가 열립니다. 로그인해주세요."
heroku login

# 2. 새 Heroku 앱 생성
echo "🆕 2단계: 새 Heroku 앱 생성 중..."
APP_NAME="mukkai-backend-$(date +%s)"
heroku create $APP_NAME
echo "✅ 앱 이름: $APP_NAME"

# 3. MySQL 데이터베이스 추가
echo "🗄️  3단계: MySQL 데이터베이스 추가 중..."
heroku addons:create jawsdb:kitefin -a $APP_NAME

# 4. RabbitMQ 추가 (선택사항)
echo "🐰 4단계: RabbitMQ 추가 중..."
heroku addons:create cloudamqp:lemur -a $APP_NAME || echo "RabbitMQ 추가 실패 (계속 진행)"

# 5. 환경 변수 설정
echo "⚙️  5단계: 환경 변수 설정 중..."
heroku config:set SPRING_PROFILES_ACTIVE=heroku -a $APP_NAME
heroku config:set JWT_SECRET_KEY=SpringBootJWTHelperTokenSecretKeyValue123!@# -a $APP_NAME

# 6. Java 버전 설정
echo "☕ 6단계: Java 버전 설정 중..."
heroku config:set JAVA_TOOL_OPTIONS="-Xmx300m -Xss512k -XX:CICompilerCount=2" -a $APP_NAME

# 7. Git에 커밋 (필요시)
echo "📦 7단계: Git 준비 중..."
git add .
git commit -m "Deploy to new Heroku app" 2>/dev/null || echo "변경사항 없음"

# 8. Heroku에 배포
echo "🚢 8단계: Heroku에 배포 중... (5-10분 소요)"
git push heroku master:main

# 9. 앱 열기
echo "🌐 9단계: 앱 URL 확인 중..."
heroku open -a $APP_NAME

# 10. 로그 확인
echo "📋 10단계: 배포 로그 확인 중..."
heroku logs --tail -a $APP_NAME

echo ""
echo "✅ 배포 완료!"
echo "🌐 앱 URL: https://$APP_NAME.herokuapp.com"
echo "📝 앱 이름을 기록해두세요: $APP_NAME"
echo ""
echo "⚠️  중요: 프론트엔드에서 이 새 URL을 사용하도록 설정해야 합니다!"

