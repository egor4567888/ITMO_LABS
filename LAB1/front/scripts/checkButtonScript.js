document.getElementById('submitButton').addEventListener('click', function () {
    // Получаем значения X, Y и R
    const xValue = document.querySelector('input[name="x"]:checked');
    const yValue = document.getElementById('yInput').value;
    const rValue = document.getElementById('rInput').value;
  
    // Проверяем, что все значения выбраны
    if (!xValue || yValue === '' || rValue === '') {
      alert('Пожалуйста, введите все значения.');
      return;
    }
    if (!Number.isInteger(yValue) || !Number.isInteger(rValue) || yValue < -5 || yValue > 5 || rValue < 2 || rValue > 5){
      alert('Пожалуйста, введите корректные значения. Y должен быть целым числом от -5 до 5, R - от 2 до 5');
      return;
    }
    
  
    // Формируем объект данных
    const data = {
      x: xValue.value,
      y: yValue,
      r: rValue
    };
  
    // Отправляем данные на сервер с помощью Fetch API
    fetch('/process', { // замените на нужный путь
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Ошибка сети');
      }
      return response.json();  // Ожидаем ответ в формате JSON
    })
    .then(data => {
      console.log('Ответ сервера:', data);  // Обрабатываем данные ответа
    })
    .catch(error => {
      console.error('Произошла ошибка:', error);
    });
  });
