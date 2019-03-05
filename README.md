# UserInfo
otus hometask 2

Для задачи валидации разработал custom views (библиотека) для ввода и валидации данных:

ValidatedTextInputLayout:
имеет состояния valid и invalid (отслеживается через TextWatcher)
для валидации есть атрибут regexp
для inputType phone и email при отсутсвии regexp работают классы google
для для phone обеспечивается mask и formatting от google (c учетом локали)
 
ValidatedInputForm: 
играет роль формы для отслеживания состояния любого количества (и любой вложенности)  ValidatedTextInputLayout views. 
Подписчик имеет актуальную информацию о валидности формы

Для передачи данных между фрагментами использую viewmodel привязанный к activity
