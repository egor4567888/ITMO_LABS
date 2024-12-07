import subprocess

def run_dict_program(input_key):
    # Запуск ассемблерной программы с передачей ключа через стандартный ввод
    result = subprocess.run(['./output'], input=input_key, text=True, capture_output=True)
    output = result.stdout.strip() if result.stdout.strip() else result.stderr.strip()
    print(f"Input: {input_key}, Output: '{output}'")  # Отладочный вывод
    return output

def test_dict_program():
    # Тесты для существующих ключей
    assert run_dict_program('key1') == 'word1', "Test failed for key1"
    assert run_dict_program('key2') == 'word2', "Test failed for key2"
    assert run_dict_program('key3') == 'word3', "Test failed for key3"
    
    # Тест для несуществующего ключа
    assert run_dict_program('key4') == 'not_found', "Test failed for non-existent key"

    print("All tests passed!")

if __name__ == "__main__":
    test_dict_program()