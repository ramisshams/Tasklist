package tasklist

import kotlinx.datetime.*
import java.io.File
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

val task = mutableListOf<String>()
val tasks = mutableListOf<String>()
val jsonFile = File("tasklist.json")
var strTask = ""
var list : Array<String> = tasks.toTypedArray()

val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
val listAdapter = moshi.adapter(Array::class.java)


fun add() {
    println("Input the task priority (C, H, N, L):")
    var priorityLetter = readln()
    while (!priorityLetter.matches("[cChHnNlL]".toRegex())) {
        println("Input the task priority (C, H, N, L):")
        priorityLetter = readln()
    }
    var priority = if (priorityLetter.matches("[nN]".toRegex())) "\u001B[102m \u001B[0m" else if (priorityLetter.matches("[hH]".toRegex())) "\u001B[103m \u001B[0m" else if (priorityLetter.matches("[cC]".toRegex())) "\u001B[101m \u001B[0m" else "\u001B[104m \u001B[0m"
    println("Input the date (yyyy-mm-dd):")
    var dateAsAString = readln()
    var checkDate = false
    var date = LocalDate(2000, 1, 1)
    while (!checkDate) {
        try {
            checkDate = true
            date = LocalDate(dateAsAString.split("-")[0].toInt(), dateAsAString.split("-")[1].toInt(), dateAsAString.split("-")[2].toInt())
        } catch(e: IllegalArgumentException) {
            println("The input date is invalid")
            println("Input the date (yyyy-mm-dd):")
            dateAsAString = readln()
            checkDate = false
        }
    }
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+0")).date
    val numberOfDays = currentDate.daysUntil(date)
    var duetag = if (numberOfDays > 0) "\u001B[102m \u001B[0m" else if (numberOfDays == 0) "\u001B[103m \u001B[0m" else "\u001B[101m \u001B[0m"
    println("Input the time (hh:mm):")
    var timeAsAString = readln()
    var checkTime = false
    var time = LocalDateTime(2000, 1, 1, 0, 0)
    while (!checkTime) {
        try {
            checkTime = true
            time = LocalDateTime(dateAsAString.split("-")[0].toInt(), dateAsAString.split("-")[1].toInt(), dateAsAString.split("-")[2].toInt(), timeAsAString.split(":")[0].toInt(), timeAsAString.split(":")[1].toInt())
        } catch(e: IllegalArgumentException) {
            println("The input time is invalid")
            println("Input the time (hh:mm):")
            timeAsAString = readln()
            checkTime = false
        }
    }
    var year = time.year
    var month = if (time.monthNumber < 10) "0${time.monthNumber}" else "${time.monthNumber}"
    var day = if (time.dayOfMonth < 10) "0${time.dayOfMonth}" else "${time.dayOfMonth}"
    var hour = if (time.hour < 10) "0${time.hour}" else "${time.hour}"
    var minutes = if (time.minute < 10) "0${time.minute}" else "${time.minute}"
    task.add("| ${year}-${month}-${day} | ${hour}:${minutes} | $priority | $duetag |")
    println("Input a new task (enter a blank line to end):")
    var list = readln()
    if (list.matches("\\s+".toRegex()) || list.isEmpty()) {
        println("The task is blank")
    } else {
        while (!list.matches("\\s+".toRegex()) && list.isNotEmpty()) {
            task.add(list)
            list = readln()
        }
    }
    strTask = "${task[0]}"
    for(i in 1..task.lastIndex) {
        if (task[i].length <= 44) {
            while (task[i].length != 44) {
                task[i] += " "
            }
            if (i < task.lastIndex) {
                strTask += task[i] + "|\n|    |            |       |   |   |"
            } else strTask += task[i] + "|"
        } else {
            while (task[i].length > 44) {
                strTask += task[i].substring(0, 44) + "|\n|    |            |       |   |   |"
                task[i] = task[i].replace("${task[i].substring(0, 44)}", "")
            }
            while (task[i].length != 44) {
                task[i] += " "
            }
            if (i < task.lastIndex) {
                strTask += task[i] + "|\n|    |            |       |   |   |"
            } else strTask += task[i] + "|"
        }
    }
    tasks.add(strTask)
    task.clear()
}

fun printF() {
    if (tasks.isEmpty()) {
        println("No tasks have been input")
    } else {
        println("""+----+------------+-------+---+---+--------------------------------------------+
| N  |    Date    | Time  | P | D |                   Task                     |
+----+------------+-------+---+---+--------------------------------------------+""")

        for (index in tasks.indices) {
            if (index < 9) {
                println("| ${index + 1}  ${tasks[index]}")
                println("+----+------------+-------+---+---+--------------------------------------------+")
            } else {
                println("| ${index + 1} ${tasks[index]}")
                println("+----+------------+-------+---+---+--------------------------------------------+")
            }
        }
    }
}

fun delete() {
    if (tasks.isEmpty()) {
        println("No tasks have been input")
    } else {
        printF()
        println("Input the task number (1-${tasks.size}):")
        var taskNumberAsAString = readln()
        var taskNumber = 0
        while (!taskNumberAsAString.matches("[0-9]+".toRegex()) || taskNumberAsAString.toInt() < 1 || taskNumberAsAString.toInt() > tasks.size) {
            println("Invalid task number")
            println("Input the task number (1-${tasks.size}):")
            taskNumberAsAString = readln()
        }
        taskNumber = taskNumberAsAString.toInt()
        tasks.removeAt(taskNumber - 1)
        println("The task is deleted")
    }
}

fun edit() {
    if (tasks.isEmpty()) {
        println("No tasks have been input")
    } else {
        printF()
        println("Input the task number (1-${tasks.size}):")
        var taskNumberAsAString = readln()
        var taskNumber = 0
        while (!taskNumberAsAString.matches("\\d".toRegex()) || taskNumberAsAString.toInt() < 1 || taskNumberAsAString.toInt() > tasks.size) {
            println("Invalid task number")
            println("Input the task number (1-${tasks.size}):")
            taskNumberAsAString = readln()
        }
        taskNumber = taskNumberAsAString.toInt()
        println("Input a field to edit (priority, date, time, task):")
        var fieldToEdit = readln()
        while (!fieldToEdit.matches("priority|date|time|task".toRegex())) {
            println("Invalid field")
            println("Input a field to edit (priority, date, time, task):")
            fieldToEdit = readln()
        }
        var editingPlaceAsAList = tasks[taskNumber - 1].split("| ", " | ", " |").toMutableList()
        var listWithoutSpaces = mutableListOf<String>()
        for (i in editingPlaceAsAList) {
            if (i.isNotEmpty()) listWithoutSpaces.add(i)
        }
        var editingDate = listWithoutSpaces[0]
        var editingTime = listWithoutSpaces[1]
        var priorityLetter = listWithoutSpaces[2]
        var duetag = listWithoutSpaces[3]
        var taskSpecified = tasks[taskNumber - 1].substring(50)
        if (fieldToEdit == "priority") {
            println("Input the task priority (C, H, N, L):")
            priorityLetter = readln()
            while (!priorityLetter.matches("[cChHnNlL]".toRegex())) {
                println("Input the task priority (C, H, N, L):")
                priorityLetter = readln()
            }
            var priority = if (priorityLetter.matches("[nN]".toRegex())) "\u001B[102m \u001B[0m" else if (priorityLetter.matches("[hH]".toRegex())) "\u001B[103m \u001B[0m" else if (priorityLetter.matches("[cC]".toRegex())) "\u001B[101m \u001B[0m" else "\u001B[104m \u001B[0m"
            tasks[taskNumber - 1] = "| $editingDate | $editingTime | $priority | $duetag |$taskSpecified"
            println("The task is changed")
        } else if (fieldToEdit == "date") {
            println("Input the date (yyyy-mm-dd):")
            var dateAsAString = readln()
            var checkDate = false
            var date = LocalDate(2000, 1, 1)
            while (!checkDate) {
                try {
                    checkDate = true
                    date = LocalDate(dateAsAString.split("-")[0].toInt(), dateAsAString.split("-")[1].toInt(), dateAsAString.split("-")[2].toInt())
                } catch(e: IllegalArgumentException) {
                    println("The input date is invalid")
                    println("Input the date (yyyy-mm-dd):")
                    dateAsAString = readln()
                    checkDate = false
                }
            }
            val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+0")).date
            val numberOfDays = currentDate.daysUntil(date)
            duetag = if (numberOfDays > 0) "\u001B[102m \u001B[0m" else if (numberOfDays == 0) "\u001B[103m \u001B[0m" else "\u001B[101m \u001B[0m"
            var year = date.year
            var month = if (date.monthNumber < 10) "0${date.monthNumber}" else "${date.monthNumber}"
            var day = if (date.dayOfMonth < 10) "0${date.dayOfMonth}" else "${date.dayOfMonth}"
            tasks[taskNumber - 1] = "| $year-$month-$day | $editingTime | $priorityLetter | $duetag |$taskSpecified"
            println("The task is changed")
        } else if ((fieldToEdit == "time")) {
            println("Input the time (hh:mm):")
            var timeAsAString = readln()
            var checkTime = false
            var time = LocalDateTime(2000, 1, 1, 0, 0)
            while (!checkTime) {
                try {
                    checkTime = true
                    time = LocalDateTime(editingDate.split("-")[0].toInt(), editingDate.split("-")[1].toInt(), editingDate.split("-")[2].toInt(), timeAsAString.split(":")[0].toInt(), timeAsAString.split(":")[1].toInt())
                } catch(e: IllegalArgumentException) {
                    println("The input time is invalid")
                    println("Input the time (hh:mm):")
                    timeAsAString = readln()
                    checkTime = false
                }
            }
            var hour = if (time.hour < 10) "0${time.hour}" else "${time.hour}"
            var minutes = if (time.minute < 10) "0${time.minute}" else "${time.minute}"
            tasks[taskNumber - 1] = "| $editingDate | ${hour}:${minutes} | $priorityLetter | $duetag |$taskSpecified"
            println("The task is changed")
        } else {
            println("Input a new task (enter a blank line to end):")
            var list = readln()
            if (list.matches("\\s+".toRegex()) || list.isEmpty()) {
                println("The task is blank")
            } else {
                while (!list.matches("\\s+".toRegex()) && list.isNotEmpty()) {
                    task.add(list)
                    list = readln()
                }
            }
            strTask = ""
            for(i in 0..task.lastIndex) {
                if (task[i].length <= 44) {
                    while (task[i].length != 44) {
                        task[i] += " "
                    }
                    if (i < task.lastIndex) {
                        strTask += task[i] + "|\n|    |            |       |   |   |"
                    } else strTask += task[i] + "|"
                } else {
                    while (task[i].length > 44) {
                        strTask += task[i].substring(0, 44) + "|\n|    |            |       |   |   |"
                        task[i] = task[i].replace("${task[i].substring(0, 44)}", "")
                    }
                    while (task[i].length != 44) {
                        task[i] += " "
                    }
                    if (i < task.lastIndex) {
                        strTask += task[i] + "|\n|    |            |       |   |   |"
                    } else strTask += task[i] + "|"
                }
            }
            task.clear()
            tasks[taskNumber - 1] = "| $editingDate | $editingTime | $priorityLetter | $duetag |$strTask"
            println("The task is changed")
        }
    }
}

fun menu(op: String) {
    if (op == "add") {
        add()
    } else if (op == "print") {
        printF()
    } else if (op == "delete") {
        delete()
    } else if (op == "edit") {
        edit()
    } else {
        println("The input action is invalid")
    }
}

fun main() {
    if (jsonFile.exists()) {
        var strTaskJson = File("tasklist.json").readText()
        var oldList = listAdapter.fromJson(strTaskJson)
        if (oldList != null) {
            for (task in oldList.indices) {
                tasks.add(oldList[task] as String)
            }
        }
    }
    println("Input an action (add, print, edit, delete, end):")
    var action = readln()
    while (action != "end") {
        menu(action)
        println("Input an action (add, print, edit, delete, end):")
        action = readln()
    }
    if (action == "end") {
        list = tasks.toTypedArray()
        File("tasklist.json").writeText(listAdapter.toJson(list))
        println("Tasklist exiting!")
    }
}


