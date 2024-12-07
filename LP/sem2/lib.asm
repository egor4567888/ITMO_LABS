; Принимает указатель на строку, пытается
; прочитать из её начала знаковое число.
; Если есть знак, пробелы между ним и числом не разрешены.
; Возвращает в rax: число, rdx : его длину в символах (включая знак, если он был) 
; rdx = 0 если число прочитать не удалось



; check_int.asm
section .data
    
    str: db "1234", 0

section .text


parse_uint:
    mov r10, 10
    xor rax, rax
    xor rcx, rcx
    mov r11, rdi
    .loop:
    mov cl, [rdi]
    sub cl, "0"
    jb .end
    cmp cl, 9
    ja .end

    mul r10
    add rax, rcx

    inc rdi
    jmp .loop

    .end:
        sub rdi, r11
        mov rdx, rdi
        ret    

