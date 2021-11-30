docker rmi $(docker images)
docker build -t "cryptoscalpingbot:Dockerfile" .
docker run -d --name csb-master <image_id>