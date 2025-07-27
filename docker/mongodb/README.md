# Run MongoDB

This folder contains a docker compose file to setup a mongo database with primary-secondary replication pattern. See the reference [Here](https://www.mongodb.com/docs/manual/replication/).

The databases run at port 27017 (primary) and 27018 (secondary).

To setup the containers, run
```sh
docker compose up -d
```