;dict.asm
global dict_find_word
global dict_print_word
     %include "colon.inc"
     %include "lib.inc"

section .data

   

section .text
    dict_find_word:
        push r12 ; адрес искомого ключа
        push r13 ; адрес вхождения
        mov r12, rdi
        mov r13, rsi
        push rbp ;для выравнивания стека
        mov rbp, rsp
        .loop:
            mov rdi, r13
            add rdi, 16
            mov rsi, r12
            and rsp, -16
            call string_equals
            mov rsp, rbp
            test rax, rax
            jnz .equal
            mov r13, [r13]
            cmp r13, 0
            jnz .loop
            xor rax, rax
            jmp .ret
        .equal:
            mov rax, r13
            
        .ret:
            pop rbp
            pop r13
            pop r12
            ret
    
    ;выводит слово по адресу вхождения
    dict_print_word:
        add rdi, 16 ; пропускаем адрес следующего вхождения
        xor rax, rax
        xor rsi, rsi
        .scip_key_loop:
            mov al, byte[rdi]
            mov rsi, rax
            inc rdi
            test rsi, rsi
            jnz .scip_key_loop
        
        ; .scip_zero_loop:
        ;     mov rsi, [rdi]
        ;     inc rdi
        ;     test rsi, rsi
        ;     jz .scip_zero_loop

            push rbp
            mov rbp, rsp
            and rsp, -16
            call print_string
            mov rsp, rbp
            pop rbp
            ret

    
    ; Использование макроса define_word для добавления элементов в словарь
    
; global _start
; _start:


;  f:   mov rdi, message
;  mov rsi, dict_start
;     call dict_find_word
;     mov rdi, rax
;     call dict_print_word
    

;     mov rax, 60        
;     xor rdi, rdi
;     syscall

