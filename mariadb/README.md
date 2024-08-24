# Run MariaDB with MaxScale

This folder contains a docker compose file to setup a database layer that uses master-slave pattern to scale `MariaDB` used by `inference-server`. See the refernece [Here](https://mariadb.com/resources/blog/all-about-mariadb-maxscale-database-proxy-for-read-write-splitting/).

The database proxy runs at port 3306 (for jdbc), and exposes port 8989 for administration page accessible at http://localhost:8989/
- user: admin
- password: mariadb

```sh
docker compose up -d
```