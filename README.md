


# API 명세


## 회원 가입

### Request

```json
Method : Post
URL : /members

body:

{
  "loginId" : "string",
  "password" : "string",
  "email" : "string",
  "name" : "string",
  "nickname" : "string"
}

```

### Response

#### Basic
```json
Status : 201 (created)
body : null
```


#### A1 - 필드의 Validation을 통과하지 못했을 때
```json
status : 400 (bad request)
body : 
{
 "timestamp" : "발생 시간 ex ) 2023-01-01T00:00:01"
 "status" : 400
 "error" : "FieldValidationException"
 "code" : "G-001"
 "message" : "입력하신 내용을 다시 확인해주세요."
 "path" : "/members"
}

```

#### A2 - 중복된 loginId를 이용할 때
```json
status : 409 (conflict)
body :
{
"timestamp" : "발생 시간 ex ) 2023-01-01T00:00:01"
"status" : 409
"error" : "LoginIdDuplicatedException"
"code" : "M-001"
"message" : "로그인 아이디가 중복됩니다."
"path" : "/members"
}
```



## 로그인

### Request
```json
Method : Post
URL : /members/login
Body : 
{
  "loginId" : "String",
  "password" : "string"
}

```

### Response

#### Basic Path

```json
status : 200 (OK)
body : null
```

#### A1 id, password가 유효성 검사를 통과하지 못했을 때
```json
status : 400 (bad request)
body :
{
    "timestamp" : "발생 시간 ex ) 2023-01-01T00:00:01"
    "status" : 400
    "error" : "FieldValidationException"
    "code" : "G-001"
    "message" : "입력하신 내용을 다시 확인해주세요."
    "path" : "/members/login"
}

```


#### A2. Id 혹은 Password가 알맞지 않았을 때 

```json
status : 409 (conflict)
body :
{
    "timestamp" : "발생 시간 ex ) 2023-01-01T00:00:01"
    "status" : 409
    "error" : "WrongIdOrPasswordException"
    "code" : "M-002"
    "message" : "아이디 혹은 패스워드가 틀렸습니다."
    "path" : "/members/login"
}

```



## 회원 정보 수정






## 내 정보 조회

### Request
```json
    Method : GET
    URL : /members/my
    
```

### Response

#### Basic Path

```json
status : 200 (OK)
body :
{
  "loginId" : "string",
  "password" : "string",
  "name" : "string",
  "nickname" : "string",
  "statusMessage" : "string"
}

```

#### A1. 세션 정보가 없는 상태로 접근했을 때

```json
status : 401 (UnAuthorized)
body :
{
  "timestamp" : "발생 시간 ex ) 2023-01-01T00:00:01"
  "status" : 401
  "error" : "UN_AUTHORIZED_ACCESS"
  "code" : "G-002"
  "message" : "인증되지 않은 접근입니다."
  "path" : "/members/my"
}

```



#### E1. 세션 정보가 DB에 조회되지 못했을 경우

```json
status : 401 (UnAuthorized)
body :
{
  "timestamp" : "발생 시간 ex ) 2023-01-01T00:00:01"
  "status" : 401
  "error" : "UN_AUTHORIZED_ACCESS"
  "code" : "G-002"
  "message" : "인증되지 않은 접근입니다."
  "path" : "/members/my"
}

```


## 회원 정보 수정

### Request

```json
method : PATCH
URL : /members/my

BODY
{
  "nickname" : "string",
  "password" : "string",
  "statusMessage" : "string"
}
```