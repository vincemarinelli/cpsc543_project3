SOURCE_DIR = src
CLASS_DIR = build
PACKAGE = nonblocking

all: build

$(CLASS_DIR)/%.class: $(SOURCE_DIR)/%.java
	javac -d build -cp src:src/jcip-annotations.jar $<

build: $(CLASS_DIR)/$(PACKAGE)/NonBlockingDataflow.class

test: build
	bash -x ./runtest.sh 1

clean:
	rm -rf build
	rm -f *.csv *.txt *.dif

.PHONY: build test clean
