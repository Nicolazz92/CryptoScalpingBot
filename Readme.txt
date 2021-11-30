docker rmi $(docker images)
docker build -t "cryptoscalpingbot:csb-master-docker" .
docker run -d --name csb-master <image_id>