
%macro call 1
mov rax, rsp
and rax, 15
test rax, rax
jnz alignment_error
mov rax, -1
push rbx
push rbp
push r12 
push r13 
push r14 
push r15 
call %1
cmp r15, [rsp] 
jne convention_error
pop r15
cmp r14, [rsp] 
jne convention_error
pop r14
cmp r13, [rsp] 
jne convention_error
pop r13
cmp r12, [rsp] 
jne convention_error
pop r12
cmp rbp, [rsp] 
jne convention_error
pop rbp
cmp rbx, [rsp] 
jne convention_error
pop rbx
mov rdi, -1
mov rsi, -1
mov rcx, -1
mov r8, -1
mov r9, -1
mov r10, -1
mov r11, -1
%endmacro

%include "lib.asm"

global _start

section .text
alignment_error:
    mov rax, 1
    mov rdi, 2
    mov rsi, err_alignment_error
    mov rdx, err_alignment_error.end - err_alignment_error
    syscall
    mov rax, 60
    mov rdi, -41
    syscall

convention_error:
    mov rax, 1
    mov rdi, 2
    mov rsi, err_calling_convention
    mov rdx, err_calling_convention.end - err_calling_convention
    syscall
    mov rax, 60
    mov rdi, -41
    syscall

section .data
    err_alignment_error: db "You did not respect the calling convention! Check that you align stack before call correctly", 10
    .end:
    err_calling_convention: db "You did not respect the calling convention! Check that you handled caller-saved and callee-saved registers correctly", 10
    .end:

section .data
word_buf: times 20 db 0xca

section .text
_start:
    mov rdi, word_buf
    mov rsi, 20 
    call read_word
    mov rdi, rax
    call print_string
    mov rax, 60
    xor rdi, rdi
    syscall
