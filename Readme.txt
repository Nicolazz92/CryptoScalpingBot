docker kill $(docker ps -q) && /
docker rm $(docker ps -a -q) && /
docker rmi $(docker images -q) && /
docker build -t "cryptoscalpingbot:Dockerfile" . && /
docker logs -f $(docker run -d -t "cryptoscalpingbot:Dockerfile" --name csb-master)






2021-12-10 09:56:11.944  INFO 1 --- [   scheduling-1] o.v.s.s.trade.AbstractTradingService     : Не найдено условий для выставления ордеров, всего денег: 493.73727524053993$
2021-12-10 09:57:11.647  INFO 1 --- [   scheduling-1] o.v.s.s.trade.AbstractTradingService     : Не найдено условий для выставления ордеров, всего денег: 494.3492474183629$
2021-12-10 09:58:11.929  INFO 1 --- [   scheduling-1] o.v.s.s.trade.AbstractTradingService     : Не найдено условий для выставления ордеров, всего денег: 494.2895959147259$
2021-12-10 09:59:12.183  INFO 1 --- [   scheduling-1] o.v.s.s.trade.AbstractTradingService     : Не найдено условий для выставления ордеров, всего денег: 494.3167102345609$
2021-12-10 10:00:12.004  INFO 1 --- [   scheduling-1] o.v.s.s.trade.AbstractTradingService     : Не найдено условий для выставления ордеров, всего денег: 494.3834543776045$
2021-12-10 10:01:12.284  INFO 1 --- [   scheduling-1] o.v.s.s.trade.AbstractTradingService     : Не найдено условий для выставления ордеров, всего денег: 494.5498985934244$
2021-12-10 10:02:12.829  INFO 1 --- [   scheduling-1] o.v.s.s.trade.AbstractTradingService     : Не найдено условий для выставления ордеров, всего денег: 494.63416235787815$
2021-12-10 10:03:10.296  INFO 1 --- [   scheduling-1] o.v.s.s.trade.AbstractTradingService     : Не найдено условий для выставления ордеров, всего денег: 494.45645366819366$
2021-12-10 10:04:10.537  INFO 1 --- [   scheduling-1] o.v.s.s.trade.AbstractTradingService     : Не найдено условий для выставления ордеров, всего денег: 494.2649784456264$
2021-12-10 10:05:10.537  INFO 1 --- [   scheduling-1] o.v.s.s.trade.AbstractTradingService     : Не найдено условий для выставления ордеров, всего денег: 493.99007174218383$
2021-12-10 10:06:10.303  INFO 1 --- [   scheduling-1] o.v.s.s.trade.AbstractTradingService     : Не найдено условий для выставления ордеров, всего денег: 493.4815527256644$

Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread "pool-1-thread-1116"

Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread "pool-1-thread-1115"
Exception in thread "VelikokhBinanceTradingBot Telegram Connection" Exception in thread "OkHttp api.binance.com Writer" java.lang.OutOfMemoryError: Java heap space
java.lang.OutOfMemoryError: Java heap space

Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread "Thread-3"

Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread "scheduling-1"

Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread "OkHttp ConnectionPool"
C


