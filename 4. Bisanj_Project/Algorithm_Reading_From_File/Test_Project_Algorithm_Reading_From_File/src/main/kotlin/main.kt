import java.io.File

fun main(args: Array<String>) {
    var otvet = ""
    for ((index, test) in readListOfTestsFromFile("Kotlin", 8, ".txt").withIndex()) {
        println("${index+1})$test")
        otvet += "\n${index+1})" +  if(test.rightAnswer == readLine())
             "Right! Great"
        else
            "0ops, Wrong! Right answer is ${test.rightAnswer}!"
    }

    println(otvet)
}

fun readListOfTestsFromFile(nameOfSubject: String, grade: Int, formatOfFile:String): List<Test> {
    val listOfLines = File(nameOfSubject + grade.toString() + formatOfFile).useLines {
        it.toList()
    }.filter {
        it.trim() != ""
    }

    val listOfTests = arrayListOf<Test>()

    var i = -1
    while(++i < listOfLines.size) {
        listOfTests.add(Test(
            listOfLines[i++].trim(),
            listOfLines[i++].trim(),
            listOfLines[i++].trim(),
            listOfLines[i++].trim(),
            listOfLines[i].trim()
        ))
    }

    return listOfTests.shuffled()
}