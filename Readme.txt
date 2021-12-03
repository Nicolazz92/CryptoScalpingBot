docker kill $(docker ps -q) && /
docker rm $(docker ps -a -q) && /
docker rmi $(docker images -q) && /
docker build -t "cryptoscalpingbot:Dockerfile" . && /
docker run -d -t "cryptoscalpingbot:Dockerfile" --name csb-master


2021-11-30 21:28:38.475  INFO 1 --- [pool-1-thread-1] .v.s.s.r.SingleCoinRatioSelectingService : При подборе коэффициентов для пары CHRBNB были выбраны коэффициенты RatioParams(symbol=CHRBNB, deltaMinuteInterval=30, deltaPercent=7.0, resultPercent=326.7627773596055, dealsCount=81, freshLimit=2021-12-05T23:24:37.637896)
2021-11-30 21:36:00.547  INFO 1 --- [pool-2-thread-2] o.v.s.s.trade.AbstractTradingService     : Готово к выставлению ордера: RatioParams(symbol=CHRBNB, deltaMinuteInterval=30, deltaPercent=7.0, resultPercent=326.7627773596055, dealsCount=81, freshLimit=2021-12-05T23:24:37.637896)
2021-11-30 21:36:00.547  INFO 1 --- [pool-2-thread-2] o.v.s.s.trade.local.LocalTradingService  : Открыта позиция на пару CHRBNB: Hold(buyingPrice=0.0015229, expectingPrice=0.0015262, moneyAmount=32799.26456103488, buyingDate=null, sellingDate=null)
