fun main() {
    println(
        "Введите одну из команд:\n" +
                "1. exit\n" +
                "2. help\n" +
                "3. add <Имя> phone <Номер телефона>\n" +
                "4. add <Имя> email <Адрес электронной почты>\n" +
                "5. show <Имя>\n" +
                "6. find <Телефон или Email>"
    )
    while (true) {
        val command = readCommand(readLine()!!.lowercase())
        println(command)
        if (command.isValid()) {
            when (command) {
                is AddPhoneCommand -> phoneBook.getOrPut(command.name) { Person(command.name) }.also {
                    it.phones.add(command.phone)
                    println("Добавлено: ${it.name}, телефон: ${command.phone}")
                }

                is AddEmailCommand -> phoneBook.getOrPut(command.name) { Person(command.name) }.also {
                    it.emails.add(command.email)
                    println("Добавлено: ${it.name}, email: ${command.email}")
                }

                is ShowCommand -> {
                    phoneBook[command.name]?.let {
                        println(
                            "Имя: ${it.name}; \n" +
                                    "Телефоны: ${it.phones.joinToString()}; \n" +
                                    "Emails: ${it.emails.joinToString()}"
                        )
                    } ?: println("Запись не найдена")
                }

                is FindCommand -> {
                    phoneBook.values.filter {
                        it.phones.contains(command.info)
                                || it.emails.contains(command.info)
                    }.takeIf { it.isNotEmpty() }?.forEach {
                        println(
                            "Имя: ${it.name}; \n" +
                                    "Телефоны: ${it.phones.joinToString()}; \n" +
                                    "Emails: ${it.emails.joinToString()}"
                        )
                    } ?: println("Записи не найдены")
                }

                is HelpCommand -> {
                    println(
                        "Список доступных команд:\n" +
                                "1. exit\n" +
                                "2. help\n" +
                                "3. add <Имя> phone <Номер телефона>\n" +
                                "4. add <Имя> email <Адрес электронной почты>\n" +
                                "5. show <Имя>\n" +
                                "6. find <Телефон или Email>"
                    )
                }

                is ExitCommand -> return
            }
        } else {
            println("Неверный формат команды, попробуйте еще раз")
        }
    }
}

val phoneBook = mutableMapOf<String, Person>()

// Функция для чтения команды пользователя из консоли
fun readCommand(input: String): Command {
    val parts = input.split(" ")
    return when {
        parts[0] == "exit" -> ExitCommand
        parts[0] == "help" -> HelpCommand
        parts.size == 2 -> when (parts[0]) {
            "show" -> ShowCommand(parts[1])
            "find" -> FindCommand(parts[1])
            else -> HelpCommand
        }

        parts.size == 4 && parts[0] == "add" -> when (parts[2]) {
            "phone" -> AddPhoneCommand(parts[1], parts[3])
            "email" -> AddEmailCommand(parts[1], parts[3])
            else -> HelpCommand
        }

        else -> HelpCommand
    }
}