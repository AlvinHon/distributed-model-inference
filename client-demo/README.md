## Client Demo

It is a simple `NodeJs` script to make HTTP requests in a loop.

There are two "async" loops:
1. Keep picking a image from `/images` folder to upload to server `localhost:8080/image`
1. Keep querying the latest inference result by invoking server `localhost:8080/result`, and then displays them on console.

Run the command below to execute the script (you need to install `NodeJS` first):
```sh
node app.js
```
