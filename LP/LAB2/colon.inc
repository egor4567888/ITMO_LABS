; colon.inc - Заголовочный файл с макросом для создания слов в словаре

section .data
%define dict_start 0

%macro define_word 2
    ; Создаем новую запись в словаре
    %2:
        dq dict_start, 0  ; Адрес следующей записи (текущего начала словаря)
        db %1, 0       ; Ключ

    ; Обновляем указатель на начало словаря
    %define dict_start %2
%endmacro