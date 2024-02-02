# Kream

## Overview
This project is used as a playground while creating an app similar to Kream.

As a side note, I did my best to achieve 100% test coverage.

## System Architecture
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
- [x] 회원가입
- [ ] 로그인

### 상품
- [ ] 상품 목록 노출
- [x] 상품 등록
    - 등록과 동시에 판매 시작

### 판매
- [x] 상품 즉시 판매
- [x] 상품 즉시 구매
- [x] 상품 판매 입찰
- [x] 상품 구매 입찰

### 배송
- [ ] 판매자 상품 배송
- [ ] 크림 관리자 입고 완료
- [ ] 크림 관리자 검수 완료
- [ ] 크림 관리자 상품 배송 및 판매 완료 처리
