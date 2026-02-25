CC = gcc
CFLAGS = -Wall -Wextra -std=c11
TARGET = out/clox
SOURCES = main.c chunk.c compiler.c debug.c memory.c scanner.c value.c vm.c

$(TARGET): $(SOURCES) | out
	$(CC) $(CFLAGS) -o $(TARGET) $(SOURCES)

out:
	mkdir -p out

run: $(TARGET)
	./$(TARGET)

clean:
	rm -f $(TARGET)

.PHONY: clean run
