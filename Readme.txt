docker kill $(docker ps -q) && /
docker rm $(docker ps -a -q) && /
docker rmi $(docker images -q) && /
docker build -t "cryptoscalpingbot:Dockerfile" . && /
docker logs -f $(docker run -d -t "cryptoscalpingbot:Dockerfile" --name csb-master)
