global exit
global string_length
global print_string
global print_char
global print_newline
global print_uint
global print_int
global string_equals
global read_char
global read_word
global parse_uint
global parse_int
global string_copy
exit: 
    mov rax, 60    ; Номер системного вызова 'exit' (60 для x86-64)
    syscall        ; Вызов системного вызова 'exit'


; Принимает указатель на нуль-терминированную строку, возвращает её длину
string_length:
    mov  rax, rdi
  .counter:
    cmp  byte [rdi], 0
    je   .end
    inc  rdi
    jmp  .counter
  .end:
    sub  rdi, rax
    mov  rax, rdi
    ret

; Принимает указатель на нуль-терминированную строку, выводит её в stdout
print_string:
    push rbx
    push rdi
    mov rbx, rsp ; Сохранение текущего значения rsp
    and rsp, -16     ; выравнивание rsp по 16 байтам
    call string_length
    mov rsp, rbx ; Восстановление значения rsp
    pop rsi
    
    pop rbx
    mov rdx, rax
    mov rax, 1           ; 'write' syscall number
    mov rdi, 1           ; stdout descriptor
    syscall
    ret

; Принимает код символа и выводит его в stdout
print_char:            
    mov rsi, rsp           ; Используем  стек для временного хранения символа
    dec rsi
    mov byte [rsi], dil     ; Записываем символ в стек
    mov rdi, 1             ; stdout descriptor
    mov rdx, 1             ; Размер данных для вывода - 1 байт
    mov rax, 1             ; 'write' syscall number
    syscall
    ret

; Переводит строку (выводит символ с кодом 0xA)
print_newline:
    mov rdi, 0xA
    push rbx
    mov rbx, rsp
    and rsp, -16
    call print_char
    mov rsp, rbx
    pop rbx
    ret

; Выводит беззнаковое 8-байтовое число в десятичном формате 
; Совет: выделите место в стеке и храните там результаты деления
; Не забудьте перевести цифры в их ASCII коды.
print_uint:
    mov rax, rdi    
    mov r10, 10     ; Делитель
    xor rcx, rcx    ; Счётчик цифр


    push rbp    
    mov rbp, rsp    ; Сохраняем текущее значение rsp
    sub rsp, 64    ; Выделяем место под результаты деления


    ;цикл записи цифр в память
    mov rcx, rbp     ;вычисляем адрес для записи цифры
    dec rcx
    mov byte [rcx], 0  ; Записываем нуль-терминатор
    .print_uint_loop:
        xor rdx, rdx
        div r10
        add dl, '0'
        dec rcx
        mov byte [rcx], dl  ; Записываем цифру в стек
        test rax, rax 
        jnz .print_uint_loop
    ;цикл вывода цифр
    .print_uint_print:
        mov rdi, rcx
        push rbx    ; Сохраняем значение rbx
        mov rbx, rsp    ; Сохраняем текущее значение rsp
        and rsp, -16    ; Выравниваем rsp по 16 байтам
        call print_string
        mov rsp, rbx
        pop rbx
        
    
    mov rsp, rbp
    pop rbp
    ret


; Выводит знаковое 8-байтовое число в десятичном формате 
print_int:
    cmp rdi, 0  ; Проверяем, отрицательное ли число
    jge .print_int_positive
    push rdi ; Сохраняем rdi
    ; Выводим минус
    mov rdi, '-' 
    push rbx
    mov rbx, rsp
    and rsp, -16
    call print_char
    mov rsp, rbx
    pop rbx
    pop rdi

    neg rdi ; Делаем число положительным
    ; Выводим положительное число
    .print_int_positive:
        push rbx
        mov rbx, rsp
        and rsp, -16
        call print_uint
        mov rsp, rbx
        pop rbx
        ret

; Принимает два указателя на нуль-терминированные строки, возвращает 1 если они равны, 0 иначе
string_equals:
    .string_equals_loop:
    mov al, [rdi]   ; Загружаем символ из первой строки
    cmp al, byte [rsi]  ; Сравниваем с символом из второй строки
    
    jne .string_equals_not_equal
    cmp al, 0   ; Проверяем, достигнут ли конец строки
    je .string_equals_equal
    inc rdi   ; Переходим к следующему символу
    inc rsi
    jmp .string_equals_loop
    .string_equals_equal:
        mov rax, 1
        ret
    .string_equals_not_equal:
        mov rax, 0
        ret
    ret

; Читает один символ из stdin и возвращает его. Возвращает 0 если достигнут конец потока

section .text
    global read_char

; Читает один символ из stdin и возвращает его. Возвращает 0 если достигнут конец потока
read_char:
    mov rax, 0          ; syscall: read
    mov rdi, 0          ; file descriptor: stdin
    lea rsi, [rsp-1]    ; адрес для чтения символа (используем стек)
    mov rdx, 1          ; количество байтов для чтения
    syscall             

    ; Проверка на конец потока
    test rax, rax       
    jz .end_of_stream   

    
    movzx rax, byte [rsp-1] 
    ret

.end_of_stream:
    xor rax, rax        
    ret

; Принимает: адрес начала буфера, размер буфера
; Читает в буфер слово из stdin, пропуская пробельные символы в начале, .
; Пробельные символы это пробел 0x20, табуляция 0x9 и перевод строки 0xA.
; Останавливается и возвращает 0 если слово слишком большое для буфера
; При успехе возвращает адрес буфера в rax, длину слова в rdx.
; При неудаче возвращает 0 в rax
; Эта функция должна дописывать к слову нуль-терминатор

read_word:
    ;сохраняем регистры (эти регистры используются, чтобы избежать сохранения значений перед вызовом функции в цикле )
    push rbx    ;используем rbx для хранения адреса текущего символа
    push r12    ;используем r12 для хранения адреса начала слова
    push r13    ;используем r13 для хранения адреса конца буфера

    mov r13, rsi
    add r13, rdi

    mov r12, rdi
    .skip_spaces:
    
    push rbp
        mov rbp, rsp
        and rsp, -16
        call read_char  ;читаем символ 
        mov rsp, rbp
        pop rbp
    

    ;проверяем, является ли символ пробельным
    cmp rax, 0x20
    je .skip_spaces
    cmp rax, 0x9
    je .skip_spaces
    cmp rax, 0xA
    je .skip_spaces
    
    mov rbx, r12
    jmp .write_first  ;переходим к read2, т.к. первый символ уже считан
    

    .read:
        push rbp
        mov rbp, rsp
        and rsp, -16
        call read_char      ;читаем символ 
        mov rsp, rbp
        pop rbp
    .write_first:             ;для первого цикла (т.к. символ уже считан)

        ;проверяем, является ли символ пробельным
        cmp rax, 0x20
        je .end
        cmp rax, 0x9
        je .end
        cmp rax, 0xA
        je .end
        cmp al, 0
        je .end
        
        ;записываем символ в буфер
        mov [rbx], al
        inc rbx
        cmp rbx, r13
        je .zero
        jmp .read

        

    .zero:
        xor rax, rax  ; В случае переполнения возвращаем 0
        jmp .done
    .end:
        mov rax, r12
        mov byte [rbx], 0  ; Дописываем нуль-терминатор
        sub rbx, r12
        mov rdx, rbx  ; Возвращаем длину строки
    .done:
        pop r13
        pop r12
        pop rbx

    ret
 

; Принимает указатель на строку, пытается
; прочитать из её начала беззнаковое число.
; Возвращает в rax: число, rdx : его длину в символах
; rdx = 0 если число прочитать не удалось
parse_uint:
    mov r10, 10 ; Делитель
    xor rax, rax    ; Результат
    xor rcx, rcx    ;считыванная цифра
    mov r11, rdi
    .loop:
    mov cl, [rdi]   ; Загружаем символ
    sub cl, '0'    ; Проверяем, что символ больше или равен '0'
    jb .end   
    cmp cl, 9   ; Проверяем, что символ меньше или равен '9'
    ja .end

    mul r10   ; Умножаем результат на 10
    add rax, rcx    ; Добавляем новую цифру

    inc rdi  ; Переходим к следующему символу
    jmp .loop

    .end:
        sub rdi, r11
        mov rdx, rdi
        ret   




; Принимает указатель на строку, пытается
; прочитать из её начала знаковое число.
; Если есть знак, пробелы между ним и числом не разрешены.
; Возвращает в rax: число, rdx : его длину в символах (включая знак, если он был) 
; rdx = 0 если число прочитать не удалось
parse_int:

    movzx rax , byte[rdi] ; Проверяем первый символ
    cmp rax, '-'
    je .negative
    
    ; Если первый символ не минус, то читаем беззнаковое число
    push rbp
    mov rbp, rsp
    and rsp, -16
    call parse_uint
    mov rsp, rbp
    pop rbp

    ret
    ; Если первый символ минус, то читаем беззнаковое число и вернуть его с отрицательным знаком
    .negative:
        inc rdi
        push rbp
    mov rbp, rsp
    and rsp, -16
    call parse_uint
    mov rsp, rbp
    pop rbp
        inc rdx ; Увеличиваем длину на 1, чтобы учесть знак
        neg rax ; Делаем число отрицательным
        ret


; Принимает указатель на строку, указатель на буфер и длину буфера
; Копирует строку в буфер
; Возвращает длину строки если она умещается в буфер, иначе 0
string_copy:

    xor rcx, rcx        ; Обнуляем rcx для подсчета символов

.copy_loop:
    cmp rcx, rdx        ; Проверяем, не превышена ли длина буфера
    jae .buffer_overflow 

    mov al, [rdi + rcx] ; Загружаем символ из строки
    test al, al         ; Проверяем, достигнут ли конец строки (символ 0)
    je .copy_done       ; Если достигнут, переходим к метке .copy_done

    mov [rsi + rcx], al ; Копируем символ в буфер
    inc rcx             ; Увеличиваем счетчик символов
    jmp .copy_loop      ; Переходим к началу цикла

.copy_done:
    mov byte[rsi + rcx], 0  ; Дописываем нуль-терминатор
    mov rax, rcx        ; Возвращаем длину строки
    ret

.buffer_overflow:
    xor rax, rax        ; Возвращаем 0, если строка не умещается в буфер
    ret

