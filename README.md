# Kream

## Overview
This code clones the Cream app, and only the minimum functionality is implemented.

## Architecture
TBD

## Stack
- Kotlin 1.9.20
- Spring Boot 3.2.0
- Data JPA & QueryDSL
- MySQL & MongoDB
- Kafka
- Redis

## Test

### Test all

`./gradlew testAll`

### Unit test

`./gradlew testUnit`

### E2E test

`./gradlew teste2e`

## Functional requirements

### 회원
- [ ] 회원가입
- [ ] 로그인

### 상품
- [ ] 상품 목록 노출
- [ ] 상품 등록
    - 등록과 동시에 판매 시작

### 판매
- [ ] 상품 판매
- [ ] 상품 판매 입찰
- [ ] 상품 구매
- [ ] 상품 구매 입찰

### 배송
- [ ] 판매자 상품 배송
- [ ] 크림 관리자 입고 완료
- [ ] 크림 관리자 검수 완료
- [ ] 크림 관리자 상품 배송 및 판매 완료 처리
