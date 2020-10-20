docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 \
           --name postgres-db -e POSTGRES_USER=app \
           -e POSTGRES_PASSWORD=app -e POSTGRES_DB=db \
           -p 5432:5432 postgres:12.3