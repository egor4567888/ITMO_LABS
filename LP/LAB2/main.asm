%include "words.inc"
%include "dict.inc" 
%include "lib.inc"

section .data
    err_message: db "not_found",0
    
section .text


global _start
_start:


    push rbp
    mov rbp, rsp
    and rsp, -16
    sub rsp,256
    
    mov rdi, rbp
    sub rdi, 256
    mov rsi, 256
    call read_word

    mov rdi, rbp
    sub rdi, 256
    mov rsi, dict_start
    call dict_find_word
    test rax, rax
    jz .print_err
    mov rdi, rax
    call dict_print_word
    jmp .exit
    
    .print_err:
    mov rdi, err_message
    call string_length
    mov rsi, err_message
    mov rdx, rax
    mov rax, 1           ; 'write' syscall number
    mov rdi, 2           ; stderr descriptor
    syscall

    .exit:
    mov rsp, rbp
    pop rbp
    mov rax, 60        
    xor rdi, rdi
    syscall

