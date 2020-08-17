# 多多拼项目

请求地址并非固定，以后可能会更改

## 1. 登录

登录使用的是仿`JWT`方式。在成功登录后会返回 $id$ 和 $token$ 。在之后进行需要登录才能使用的功能时，要将`HTTP`请求的 $authorization$ 头内容设置为 $\{id\}\_\{token\}$ 的格式。不确定以后会不会更改，先凑合着用。

### 1.1 注册

$content-type: multipart/form-data$  
参数：  
$username$ : 用户名  
$nickname$ : 昵称  
$password$ : 密码  

$PUT$ 方式发送请求至

```url
http://123.57.12.189:8080/User/register
```

### 1.2 登录

$content-type: multipart/form-data$  
参数：  
$username$ : 用户名  
$password$ : 密码  

$POST$ 方式发送请求至

```url
http://123.57.12.189:8080/User/login
```

返回结果：  
$id$ : 用户 $id$  
$token$ : 令牌，有效期为 $72$ 小时

### 1.3 登出

$authorization$ : [用户 $id$] + "_" + $token$  

$DELETE$ 方式发送请求至

```url
http://123.57.12.189:8080/User/logout
```

登出后令牌有效期清零，无法使用。

## 2. 拼单

以后可能会添加分页。按序排列在分页之后实现。

### 2.1 添加拼单

$content-type: multipart/form-data$  
$authorization$ : [用户 $id$] + "_" + $token$  
参数：  
$type$ : [$MEAL$, $CAR$, $HOUSE$, $OTHER$]，分别对应拼餐、拼车、拼房、其他  
$description$ : 描述  
$address$ : 详细地址  
$time$ : 时间，以 $yyyy-MM-dd hh:mm:ss$ 格式发送（秒数可以统一设置为 $00$）  
$curPeople$ : 当前人数（默认为 $1$）  
$maxPeople$ : 最大人数  
$price$ : 预计价格  
$longitude$ : 经度（通过地图 $API$ 获取，尽量精确）  
$latitude$ : 纬度  

$PUT$ 方式发送至

```url
http://123.57.12.189:8080/ShareBill/add
```

### 2.2 查找拼单

#### 2.2.1 按拼单`id`查找

$POST$ 方式发送至

```url
http://123.57.12.189:8080/ShareBill/{id}
```

使用拼单 $id$ 代替路径上的 $\{id\}$ 即可

#### 2.2.2 按用户`id`查找

$POST$ 方式发送至

```url
http://123.57.12.189:8080/ShareBill/user/{id}
```

使用用户 $id$ 代替路径上的 $\{id\}$ 即可

#### 2.2.3 按其他信息查找

$content-type: multipart/form-data$  
参数：  
$type$ : 类型，同上  
$description$ : 关键词，多个关键词之间使用空格隔开  
$startTime$ : 开始时间，格式同上  
$endTime$ : 结束时间，格式同上  
$minPrice$ : 最低价格  
$maxPrice$ : 最高价格  
$longitude$ : 经度  
$latitude$ : 纬度  
$distance$ : [M500, KM1, KM2] ，分别对应 $500$ 米，$1$ 千米，$2$ 千米  

$POST$ 方式发送至

```url
http://123.57.12.189:8080/ShareBill/info
```

### 2.3 删除拼单

$DELETE$ 方式发送至

```url
http://123.57.12.189:8080/ShareBill/del/{id}
```

使用拼单 $id$ 代替路径上的 $\{id\}$ 即可  
非管理员账户只能删除自己的拼单
