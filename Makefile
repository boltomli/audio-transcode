cr := cn
nm := nm

ifndef ver
	ver := int
endif

all:
	docker build -t $(cr)/$(nm):$(ver) .

run:
	docker run --rm -it -p 8080:8080 $(cr)/$(nm):$(ver)

local:
	mvn clean install
	java -jar target/audio-transcode-0.0.1-SNAPSHOT.jar
