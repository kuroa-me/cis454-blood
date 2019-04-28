## API Specification

This document is written in the following format:

> ### API Catagoty
> 
> #### /api_name 
>
> description of /api_name.
>
> params:
>
> ```
> parameter: type,
> ?optional_parameter: type,
> ...
> parameter: type
> ```
> 
> returns:
> 
> ```
> return: type,
> ?optional_return: type,
> ...
> return: type

`parameter: type` defines a parameter required by an API. Parameter name begins with `?` indicates that the parameter is optional. 

 `return: type` defines a field in returned JSON object by an API. Field name begins with `?` indicates that the field is optional.

The following types will be used in the documentation:

```
string: string.
uint: unsigned integer.
timestamp: unsigned integer.
donate_history: {id: uint, donor_id: uint, blood_type: uint, date_received: timestamp, used: bool, ?date_used: timestamp, ?used_by: string}.
blood_request: {id: uint, requester_name: string, requester_age: string, requester_sex: string}
blood_request_status: {id: uint, date_requested: timestamp, accepted: bool, ?date_accepted: timestamp}
blood_type: {id: uint, type_name: string}
user: {id: uint, username: string, fname: string, lname: string, type: string}
```

### Index

- [/misc/getbloodtypes](#miscgetbloodtypes)
- [/user/auth](#userauth)
- [/user/register](#userregister)
- [/user/get](#userget)
- [/user/update](#userupdate)
- [/user/logout](#userlogout)
- [/donor/donate](#donordonate)
- [/donor/get_donates](#donorget_donates)
- [/donor/request/list](#donorrequestlist)
- [/donor/request/accept](#donorrequestaccept)
- [/requester/request/new](#requesterrequestnew)
- [/requester/request/list](#requesterrequestlist)
- [/admin/user/list](#adminuserlist)
- [/admin/user/remove](#adminuserremove)
- [/admin/user/edit](#adminuseredit)
- [/admin/blood/list](#adminbloodlist)
- [/admin/blood/remove](#adminbloodremove)
- [/admin/bloodtype/add](#adminbloodtypeadd)
- [/admin/bloodtype/remove](#adminbloodtyperemove)
- [/admin/bloodtype/edit](#adminbloodtypeedit)

### Misc API

#### /misc/getbloodtypes

Get avaliable blood types. Return an array of `blood_type` if success (ok: true), otherwise an error message will be set.

params:
```
```

returns:

```
ok: bool,
?types: blood_type[],
?error: string
```

### User API

#### /user/auth

Authenticate a user, return token if success (ok: true), otherwise an error message will be set.

params:

``` 
username: string,
password: string
```

returns:

```
ok: bool,
?token: string,
?error: string
```

#### /user/register

Register a new user, return token if success (ok: true), otherwise an error message will be set.

params:

``` 
username: string,
password: string,
?is_donor: bool,
first_name: string,
last_name: string,
blood_type: int,
age: uint,
sex: string,
height: uint
```

returns:

```
ok: bool,
?token: string,
?error: string
```

#### /user/get

Get info about current user. Return user info if success (ok: true), otherwise an error message will be set.

params:

```
token: string
```

returns:

```
username: string,
is_donor: bool,
first_name: string,
last_name: string,
user_type: string,
?blood_type: int,
?age: uint,
?sex: string,
?height: uint
```


#### /user/update

Change a user's infomation. Return (ok: true) if success, otherwise an error message will be set.

params:

```
token: string,
password: string,
first_name: string,
last_name: string,
blood_type: int,
age: uint,
sex: string,
height: uint
```

returns:

```
ok: bool,
?error: string
```

#### /user/logout

Dissociate a token, so the user logged in with this token will be logged out. Return (ok: true) if success, otherwise an error message will be set.

```
token: string
```

returns:

```
ok: bool,
?error: string
```

### Donor's API

#### /donor/donate

Add a donation record. Return (ok: true) if success, otherwise an error message will be set.

params:

```
token: string,
time: timestamp
```

returns:

```
ok: bool,
?error: string
```

#### /donor/get_donates

Get donation records. Get donation records. Return an array of `donate_history` if success (ok: true), otherwise an error message will be set.

params:

```
token: string
```

returns: 

```
ok: bool,
?history: donate_history[],
?error: string
```

#### /donor/request/list

List active blood requests that match the donor's blood type. Return an array of `blood_request` if success (ok: true), otherwise an error message will be set.

params:

```
token: string
```

returns: 

```
ok: bool,
?requests: blood_request[],
?error: string
```

#### /donor/request/accept

Accept a blood request. Return (ok: true) if success, otherwise an error message will be set.

params:

```
token: string,
request_id: uint
```

returns: 

```
ok: bool,
?error: string
```


### Requester's API

#### /requester/request/new

Open a new blood request. Return (ok: true) if success, otherwise an error message will be set.

params:

```
token: string
```

returns: 

```
ok: bool,
?error: string
```

#### /requester/request/list

List all blood requests issue by current requester. Return an array of `blood_request_status` if success (ok: true), otherwise an error message will be set.

params:

```
token: string
```

returns: 

```
ok: bool,
?requests: blood_request_status[],
?error: string
```

### Administration API

#### /admin/user/list

List all users. Return an array of `user` if success (ok: true), otherwise an error message will be set.

params:

```
token: string
```

returns: 

```
ok: bool,
?users: user[],
?error: string
```

#### /admin/user/remove

Remove user by ID. Return (ok: true) if success, otherwise an error message will be set.

params:

```
token: string,
user_id: uint
```

returns: 

```
ok: bool,
?error: string
```

#### /admin/user/edit

Edit a user. Return (ok: true) if success, otherwise an error message will be set.

params:

```
token: string
user_id: uint,
username: string,
password: string,
first_name: string,
last_name: string,
blood_type: int,
age: uint,
sex: string,
height: uint
```

returns: 

```
ok: bool,
?error: string
```


#### /admin/blood/list

Get list of donation history of all donors.  Return an array of `donate_history` if success (ok: true), otherwise an error message will be set.

params:

```
token: string
```

returns: 

```
ok: bool,
?bloods: donate_history[],
?error: string
```

#### /admin/blood/remove

Remove a donation history. Return (ok: true) if success, otherwise an error message will be set.

params:

```
token: string,
blood_id: uint
```

returns: 

```
ok: bool,
?error: string
```

#### /admin/bloodtype/add

Register a new type of blood. Return newly registered `blood_id` if success (ok: true), otherwise an error message will be set.

```
token: string,
blood_type: string
```

returns: 

```
ok: bool,
?type_id: uint,
?error: string
```

#### /admin/bloodtype/remove

Unregister a blood type. Return (ok: true) if success, otherwise an error message will be set.


params:

```
token: string,
type_id: uint
```

returns: 

```
ok: bool,
?error: string
```

#### /admin/bloodtype/edit

Edit a blood type. Return (ok: true) if success, otherwise an error message will be set.

params:

```
token: string,
type_id: uint,
blood_type: string
```

returns: 

```
ok: bool,
?error: string
```