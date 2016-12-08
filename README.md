# 个人信息相关
- 注册：http://119.29.241.75:8080/BangBang/UserAction!signUp?username=zrl&password=123&phoneNumber=110
- 查看：http://119.29.241.75:8080/BangBang/UserAction!getUertInfo?username=zrl
- 修改：http://119.29.241.75:8080/BangBang/UserAction!editUertInfo?username=zrl&password=123&phoneNumber=110(username字段是必须的字段，其他字段可选，没有传的字段不修改)119.29.241.75

# 任务相关
- 发布任务： http://119.29.241.75:8080/BangBang/UserAction!mission_I_bangbang?username=zjb&MissionRcecerName=老司机&MissionRecerPhone=1568081287&MissionCop=申通&MissionCA=1527&giveScore=5&BeTime=1

- 接受任务：http://119.29.241.75:8080/BangBang/UserAction!mission_u_jiedan?username=zrl&MissionNo=20161207-002

- 确认完成：http://119.29.241.75:8080/BangBang/UserAction!mission_u_confirm?MissionNo=20161207-001

- 查询任务：http://119.29.241.75:8080/BangBang/UserAction!listMissions?offset=0&size=10&relsuid=-1&recvuid=-1&hasExpired=1&MissionCop=
    + offset: 分页偏移量（缺省值为 0）
    + size: 每页数量（缺省值为 3）
    + hasExpired: 检索结果是否包含过期任务（截止时间在当前时间之前），
        * 1:包含   ？用于检索自己的已发布和已接单时用。
        * 0(缺省)：不包含  
    + relsuid：发布用户的userid（缺省值为 -1,此时该条件被屏蔽）
    + recvuid：接受用户的userid（缺省值为 -1,此时该条件被屏蔽）
    + MissionCop 快递公司
    + MissionStatus 任务状态[待接单，已接单，已完成]
    + MissionRecerName 任务的收件人姓名
    + MissionCA 任务的取货号