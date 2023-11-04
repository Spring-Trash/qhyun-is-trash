


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
 "path" : "/member"
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
"path" : "/member"
}
```



