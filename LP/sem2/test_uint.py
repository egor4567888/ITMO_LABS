#!/usr/bin/python3

import os
import subprocess
import sys
import re
import sys
from subprocess import CalledProcessError, Popen, PIPE

#-------helpers---------------

def starts_uint( s ):
  matches = re.findall('^\\d+', s)
  if matches:
    return (int(matches[0]), len(matches[0]))
  else:
    return (0, 0)

def starts_int( s ):
  matches = re.findall('^-?\\d+', s)
  if matches:
    return (int(matches[0]), len(matches[0]))
  else:
    return (0, 0)

def unsigned_reinterpret(x):
  if x < 0:
    return x + 2**64
  else:
    return x

def first_or_empty( s ):
  sp = s.split()
  if sp == [] : 
    return ''
  else:
    return sp[0]

#-----------------------------

before_all="""
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
"""

def compile( fname, text ):
  f = open( fname + '.asm', 'w')
  f.write( text )
  f.close()

  subprocess.call( ['nasm', '--version'] )

  if subprocess.call( ['nasm', '-f', 'elf64', fname + '.asm', '-o', fname+'.o'] ) == 0 and \
     subprocess.call( ['ld', '-o' , fname, fname+'.o'] ) == 0:
    print(' {} : compiled'.format(fname))
    return True
  else: 
    print(' {} : failed to compile'.format(fname))
    return False


def launch(fname, input):
  output = b''
  try:
    p = Popen(['./' + fname], shell = None, stdin = PIPE, stdout = PIPE)
    (output, err) = p.communicate(input.encode())
    return (output.decode(), p.returncode)
  except CalledProcessError as exc:
    return (exc.output.decode(), exc.returncode)
  else:
    return (output.decode(), 0)

def test_asm(text, name,  input):
  if compile(name, text):
    r = launch(name, input)
    return r 
  return None 

class Test:
  name = ''
  string = lambda x : x
  checker = lambda input, output, code : False

  def __init__(self, name, stringctor, checker):
    self.checker = checker
    self.string = stringctor
    self.name = name
  def perform(self, arg):
    res = test_asm( self.string(arg), self.name, arg)
    if res is None:
      return False
    (output, code) = res
    print('"{}" -> {}'.format(arg,  res))
    return self.checker( arg, output, code )

tests=[
  Test('parse_uint',
    lambda v: before_all + """
global _start 
section .data
  input: db '""" + v  + """', 0
section .text
  print_uint_test:
    mov rax, rdi
    push r12
    push r13
    push r15
    xor r12, r12
    mov r13, 32
    mov r15, 10
    push r12
    push r12
    push r12
    push r12
  ._A:
    test rax, rax
    jz ._B
    xor rdx, rdx
    div r15
    add dl, '0'
    dec r13
    mov byte [rsp + r13], dl
    inc r12
    jmp ._A
  ._B:
    test r12, r12
    jnz ._C
    dec r13
    mov byte [rsp + r13], '0'
    inc r12
  ._C:
    mov rax, 1
    mov rdi, 1
    lea rsi, [rsp + r13]
    mov rdx, r12
    syscall
    add rsp, 32
    pop r15
    pop r13
    pop r12
    ret
  _start:
    mov rdi, input
    call parse_uint
    push rdx
    sub rsp, 8
    mov rdi, rax
    call print_uint_test
    add rsp, 8
    mov rax, 60
    pop rdi
    syscall""", 
    lambda i,o,r:  starts_uint(i)[0] == int(o) and r == starts_uint( i )[1])
    ]

inputs= {
  'parse_uint'           
    : ["0", "1234567890987654321hehehey", "1"]
}
        
if __name__ == "__main__":
  found_error = False
  for t in tests:
    for arg in inputs[t.name]:
      if not found_error:
        try:
          print('      testing {} on "{}"'.format(t.name, arg))
          res = t.perform(arg)
          if res: 
            print('  [  ok  ]')
          else:
            print('* [ fail ]')
            found_error = True
        except:
          print('* [ fail ] with exception {}'.format(sys.exc_info()[0]))
          raise
          found_error = True
  if found_error:
    sys.exit('Not all tests have been passed')
  else:
    print("Good work, all tests are passed")
