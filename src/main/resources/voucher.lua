---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by dongfeng297.
--- DateTime: 2025/1/3 下午2:00
---
local voucherId=ARGV[1]
local userId=ARGV[2]
--库存key 存储库存数量
local stockKey="voucher:stock:"..voucherId
--订单key 存储用户id
local orderKey="voucher:order:"..voucherId
--判断库存容量
if(tonumber(redis.call("get",stockKey))<=0) then
    return 1
end
--判断用户是否已经下过单
if(redis.call("sismember",orderKey,userId)==1) then
    return 2
end
--减少库存
redis.call('incrby',stockKey,-1)
--保存用户id
redis.call('sadd',orderKey,userId)

return 0