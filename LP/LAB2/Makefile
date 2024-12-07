# Цель по умолчанию
all: main

# Компиляция файлов ассемблера
lib.o: lib.asm
	nasm -f elf64 -o lib.o lib.asm

dict.o: dict.asm
	nasm -f elf64 -o dict.o dict.asm

main.o: main.asm
	nasm -f elf64 -o main.o main.asm

# Линковка объектных файлов
main: main.o dict.o lib.o
	ld -o main main.o dict.o lib.o

# Цель для запуска тестов
test: main test.py
	python3 test.py

# Цель для очистки сгенерированных файлов
clean:
	rm -f *.o main
