# miaosha
秒杀系统

# 当前版本思路
1、先查选库存
   1.1 存在库存则下订单
   1.2 不存在库存则不允许下订单
2、查询库存使用缓存，采用简单且高效，先下订单再删库存缓存
参考：https://mp.weixin.qq.com/s?__biz=MzU1NTA0NTEwMg==&mid=2247484200&idx=1&sn=6b6c7251ee83fe8ef9201373aafcffdd&chksm=fbdb1aa9ccac93bfe26655f89056b0d25b3a536f6b11148878fe96ffdf1d8349d44659cad784&token=841068032&lang=zh_CN#rd
