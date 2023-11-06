


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


