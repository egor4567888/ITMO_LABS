section .text
global find_word
     %include "colon.inc"

section .data
    define_word 'key1', message1
    db "word1", 0
    define_word 'key2', message2
    db "word2", 0
    define_word 'key3', message3
    db "word3", 0