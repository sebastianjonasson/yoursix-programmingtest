# yoursix-programmingtest

## Running
Run the following commands to start the API:
1. Clone and `cd` into repo
2. Run `docker-compose up --build -d` to build and start the container
3. Run `docker exec -it yoursix_yoursix_1 bash` to enter the container
4. Run `lein run` to start the API service

## Assignment considerations
At first glance of this assignment I thought I would approach this test as if I was implementing a web service going into production. But with the major limitation of not using any third party libraries I decided to reduce the scope to just focus on the core components of the assignment: Robot management, robot movement, and web server/router. Having that said I find it important to mention that the following components have been intentionally left out:

 - Error handling
 - Proper API responses for non 2** requests
 - Schema layer and validation
 - Authentication
 - Permissions
 - Logs and metrics

As I consider these components essential to understand and include when building a web service for production, I wanted to highlight that these have been intentionally left out.

## API Specification
[Found here](https://github.com/sebastianjonasson/yoursix-programmingtest/blob/master/openapi.yaml)
