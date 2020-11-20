FROM maven:3.6.3-jdk-11

RUN apt-get update && apt-get install -y make

# Copy the files.
COPY ./src /src
COPY ./pom.xml pom.xml

RUN mvn install

COPY ./pdf-parser pdf-parser

CMD ["/bin/bash"]

# Build the Docker container:
# docker build -t pdf-parser .

# Run the Docker container:
# docker run -it -v <input-dir-with-pdf-files>:/input -v <output-dir>:/output pdf-parser