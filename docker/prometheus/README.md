# Run Prometheus

This folder contains a Docker Compose file to set up a Prometheus server for monitoring.
See the reference [Here](https://prometheus.io/docs/introduction/overview/).

Services included:
- Prometheus server running on port 9090
- Prometheus push gateway for handling metrics from short-lived jobs running on port 9091
- Grafana for visualization running on port 3000

To set up the container, run
```sh
docker compose up -d
```