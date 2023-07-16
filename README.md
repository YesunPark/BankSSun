# ☀️ BankSSun 프로그램 by 박예선

> 제로베이스 백엔드 스쿨 (백엔드 개발자 부트캠프)에서 진행한 개인 프로젝트이며\
> 핀테크(Fin-Tech)의 기초적인 계좌 기능을 구현한 프로그램입니다.
>
> **진행 기간은 [ 23.07.11 ~ 23.08.14 ]** 로 **5주간** 진행 예정입니다.
>
>

### ☀️ 목차

1. [프로그램 주요 기능](#프로그램-주요-기능)
    + [회원가입](#1-회원가입)
    + [로그인](#2-로그인)
    + [계좌 관리 (생성 / 삭제 / 목록조회)](#3-계좌-관리--생성--삭제--목록조회-)
    + [입출금](#4-입출금)
    + [타 은행 계좌 등록](#5-타-은행-계좌-등록)
    + [계좌 검색](#6-계좌-검색)
    + [송금 기능 및 입출금 내역 조회](#7-송금-기능-및-입출금-내역-조회)
2. [추가 구현하고 싶은 기능들](#추가-구현하고-싶은-기능들)
3. [사용한 기술 스택](#사용한-기술-스택)
4. [ERD 데이터 모델링](#erd-데이터-모델링)
5. [Trouble Shooting](#trouble-shooting)
6. [프로젝트를 하며 느낀 점](#프로젝트를-하며-느낀-점)

## [프로그램 주요 기능]

### 1. 회원가입

- [ ] 실명, 휴대전화번호(10~11자리), 비밀번호를 입력해 회원가입 할 수 있다.
- [ ] 전화번호는 중복 불가하다.

### 2. 로그인

- [ ] 회원가입 시 사용한 휴대전화번호와 비밀번호로 로그인 할 수 있다.
- [ ] 회원가입과 로그인을 제외한 BankSSun 프로그램의 모든 기능은 로그인 후 사용 가능하다.

### 3. 계좌 관리 (생성 / 삭제 / 목록조회)

- [ ] 계좌를 생성, 삭제 할 수 있다.\
  계좌 생성은 `SSun 은행`의 계좌만 가능하다.\
  삭제 시 데이터베이스에서 삭제되지 않고 삭제 여부가 저장된다.
- [ ] 계좌 목록 조회 요청 시 본인이 소유한 SSun 은행 계좌목록을 확인할 수 있다.\
  (계좌번호, 잔액이 표시된다.)

### 4. 입출금

- [ ] 계좌에 있는 금액을 출금할 수 있다.
- [ ] 계좌에 입금할 수 있다.

### 5. 타 은행 계좌 등록

- [ ] 타 은행 계좌로의 송금을 위해서는 먼저 계좌(11~13자리)가 등록되어 있어야 한다.\
  은행명, 소유주명, 계좌번호를 이용해 등록할 수 있다.\
  등록할 수 있는 은행은 `Wind`, `Rain`, `Cloud`, `Snow` 4곳이다.\
  모든 은행의 계좌번호는 중복되지 않는다.

### 6. 계좌 검색

- [ ] 계좌번호와 은행명을 이용해 계좌를 검색할 수 있다.\
  계좌를 검색하면 은행명, 계좌번호, 계좌 소유주명을 확인할 수 있다.

### 7. 송금 기능 및 입출금 내역 조회

- [ ] 본인 계좌의 잔액내에서 송금을 할 수 있다.\
  타 은행으로의 송금은 BankSSun 프로그램에 등록된 계좌로만 가능하다.
- [ ] 본인의 계좌별 입출금 내역을 조회할 수 있다. 거래 금액, 거래 대상, 거래 일시를 확인할 수 있다.\
  기본적으로 최근 일주일간의 내역이 조회되며 원하는 기간으로 필터링할 수 있다.

## [추가 구현하고 싶은 기능들]

> 추후 여유가 된다면 구현해보고 싶은 기능입니다.

- [ ] 회원가입 시 전화번호 SMS 인증
- [ ] 계좌 비밀번호 - 계좌 생성 시 비밀번호를 등록해야 하고, 출/송금시 비밀번호를 입력해야 한다.
- [ ] 본인 소유 SSun 은행의 계좌에 별칭을 설정
- [ ] 입금, 출금, 송금 시 메모 입력

## [사용한 기술 스택]

<div align=center> 

<img height="30" src="https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=Spring&logoColor=white"/>
<img height="30" src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=java&logoColor=white"/>
<img height="30" src="https://img.shields.io/badge/MySql-4479A1?style=flat-square&logo=mysql&logoColor=white"/>
<br/>
<img height="30" src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white"/>
<img height="30" src="https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=Postman&logoColor=white"/>
<img height="30" src="https://img.shields.io/badge/GitHub-black?style=flat-square&logo=GitHub&logoColor=white"/>

</div>


## [ERD 데이터 모델링]

![img](./docs/ERD%200716.png)

## [Trouble Shooting]

[프로젝트를 진행하며 발생했던 어려움들과 해결방법](./docs/Trouble_Shooting.md)입니다.

## [프로젝트를 하며 느낀 점]

< 프로젝트 완성 후 추가 예정>

