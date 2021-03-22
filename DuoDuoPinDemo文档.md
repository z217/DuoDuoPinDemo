# 多多拼项目

请求地址并非固定，以后可能会更改  
未标注 $content-type$ 默认为 $application/json$  
返回`json`对象，包含三个属性：

- $code$ ：状态码
- $message$ ：状态码对应的消息
- $content$ ：内容，如果请求没有出错的结果会以对象形式存放在这里

其中状态码与对应消息列表如下：

| 状态码  |                消息                |       描述       |
| :-----: | :--------------------------------: | :--------------: |
|  $100$  |             $success$              |     请求成功     |
| $-1000$ |        $unknown\ \ problem$        |     未知问题     |
| $-1001$ | $Username\ \ or\ \ password error$ | 用户名或密码错误 |
| $-1002$ |       $User\ \ not\ \ found$       |    用户未找到    |
| $-1003$ |       $User\ \ not\ \ login$       |    用户未登录    |
| $-1004$ |  $Username\ \ already\ \ exists$   |   用户名已存在   |
| $-1005$ |    $Insufficient\ \ authority$     |     权限不足     |
| $-1006$ |  $Share\ \ bill\ \ not\ \ found$   |    未找到拼单    |
| $-1007$ |     $Share\ \ bill\ \ illegal$     |     非法拼单     |
| $-1008$ |      $Join\ \ team\ \ failed$      |   加入小组失败   |
| $-1009$ |    $Join\ \ team\ \ duplicated$    |   重复加入小组   |

## 1. 登录

登录使用的是仿`JWT`方式。在成功登录后会返回 $id$ 和 $token$ 。在之后进行需要登录才能使用的功能时，要将`HTTP`请求的 $token$ 头内容设置为 $\{id\}\_\{token\}$ 的格式。不确定以后会不会更改，先凑合着用。

### 1.1 注册

参数：  
$username$ : 用户名  
$nickname$ : 昵称  
$password$ : 密码  

$PUT$ 方式发送请求至

```url
http://123.57.12.189:8080/User/register
```

### 1.2 登录

参数：  
$username$ : 用户名  
$password$ : 密码  

$POST$ 方式发送请求至

```url
http://123.57.12.189:8080/User/login
```

返回结果：  
$userId$ : 用户 $userId$  
$nickname$ ：用户昵称  
$token$ : 令牌，有效期为 $72$ 小时

### 1.3 登出

$token$ : [用户 $id$] + "_" + $token$  

$DELETE$ 方式发送请求至

```url
http://123.57.12.189:8080/User/logout
```

登出后令牌有效期清零，无法使用。

### 1.4 删除用户

$DELETE$ 方式发送至

```url
http://123.57.12.189:8080/User/delete/{id}
```

将路径上的 $\{id\}$ 替换为待删除的用户 $id$ 即可  
要求管理员进行该操作

### 1.5 查找用户

$POST$ 方式发送至

```url
http://123.57.12.189:8080/User/{id}
```

返回结果：  
$userId$ ：用户 $userId$  
$username$ ：用户名  
$nickname$ ：昵称  
$password$ ：为空，可以忽略  

## 2. 拼单

以后可能会添加分页。按序排列在分页之后实现。

### 2.1 添加拼单

$token$ : [用户 $id$] + "_" + $token$  
参数：  
$type$ : [$BILL$, $CAR$]，分别对应拼单、拼车   
$title$ : 标题  
$description$ : 描述  
$address$ : 详细地址  
$time$ : 时间，以 $yyyy-MM-ddThh:mm:ss$ 格式发送（秒数可以统一设置为 $00$）  
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

返回结果：  
$billId$ ：拼单 $id$  
$userId$ ：创建者用户 $id$  
$nickname$ ：创建者昵称  
$title$ ：标题  
$type$ ：类型  
$description$ ：描述  
$address$ ：地址  
$time$ ：时间  
$curPeople$ ：当前人数  
$maxPeople$ ：最大人数  
$price$ ：价格  
$longitude$ ：经度  
$latitude$ ：纬度  
$geohash$ ：$geohash$ 值  

#### 2.2.2 按用户`id`查找

$POST$ 方式发送至

```url
http://123.57.12.189:8080/ShareBill/user/{id}
```

使用用户 $id$ 代替路径上的 $\{id\}$ 即可

返回结果同上

#### 2.2.3 按其他信息查找

参数：  
$type$ : 类型，同上。可以选择 $ALL$ 代表全部类型。  
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

返回结果比之前新增一项：

$distance$ ：距离，单位千米

### 2.3 删除拼单

$DELETE$ 方式发送至

```url
http://123.57.12.189:8080/ShareBill/del/{id}
```

使用拼单 $id$ 代替路径上的 $\{id\}$ 即可  
非管理员账户只能删除自己的拼单

### 2.4 申请加入

$PUT$ 方式发送至

```url
http://123.57.12.189:8080/ShareBill/join/{id}
```

使用拼单 $id$ 代替路径上的 $\{id\} 即可  

### 2.5 允许加入

$POST$ 方式发送至

```url
http://123.57.12.189:8080/ShareBill/apply/{id}/allow
```

使用消息 $id$ 代替路径上的 $\{id\}$ 即可，只有组长可以处理该操作

### 2.6 拒绝加入

$POST$ 方式发送至

```url
http://123.57.12.189:8080/ShareBill/apply/{id}/reject
```

使用消息 $id$ 代替路径上的 $\{id\}$ 即可，只有组长可以处理该操作

### 2.7 退出

$DELETE$ 方式发送至

```url
http://123.57.12.189:8080/ShareBill/quit/{bill_id}/{user_id}
```

### 2.8 成员查询

$POST$ 方式发送至

```url
http://123.57.12.189:8080/ShareBill/team/{bill_id}
```

## 3. 聊天

### 3.1 `WebSocket`

小组聊天

```url
ws://123.57.12.189:8080/ws/chat/{bill_id}
```

### 3.2 查询小组聊天记录

$POST$ 方式发送至

```url
http://123.57.12.189:8080/chat/{id}
```

返回：  
$userId$  
$billId$  
$nickname$  
$type$ ：$CHAT$, $JOIN$, $QUIT$  
$time$  
$content$  

使用拼单 $id$ 替换路径上的 $id$ 即可

### 3.3 查询用户聊天记录

$POST$ 方式发送至

```url
http://123.57.12.189:8080/chat/{bill_id}/{user_id}
```

### 3.4 查询离线消息

$POST$ 方式发送至

```url
http://123.57.12.189:8080/{bill_id}/unchecked
```

## 4. 系统消息

### 4.1 `WebSocket`

在登录之后连接到

```url
ws://123.57.12.189:8080/ws/system/{user_id}
```

### 4.2 检查系统消息

$POST$ 方式发送至

```url
http://123.57.12.189:8080/system/check
```

返回：  
$messageId$ ：消息 $id$  
$senderId$ ：发送者 $id$  
$receiverId$ ：接收者 $id$ ，可以为空（广播）  
$billId$ ：可以为空（广播）  
$type$ ：$BROAD$, $APPLY$, $ALLOW$, $REJEC$ ，后两者分别代表允许加入和拒绝加入  
$time$  
$content$  

在登录之后进行检查，查收离线消息

### 4.3 广播消息

$PUT$ 方式发送至

```url
http://123.57.12.189:8080/system/broad
```

只有管理员才可以进行该请求
