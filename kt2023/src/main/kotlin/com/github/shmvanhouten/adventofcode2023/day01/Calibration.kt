fun sumCalibrationValues1(input: String) =
    input.lines()
        .map { line -> ("" + line.first { it.isDigit() } + line.last { it.isDigit() }) }
        .sumOf { it.toInt() }

fun sumCalibrationValues2(input: String) = input.lines()
    .map { (firstNumber(it) + lastNumber(it)) }
    .sumOf { it.toInt() }

fun firstNumber(input: String): String {

    val indexOfFirstDigit = input.indexOfFirst { it.isDigit() }
    val indexOfFirstNr = nineWholeNumbers.filter { input.contains(it) }.minOfOrNull { input.indexOf(it) }
    return if (indexOfFirstNr == null || indexOfFirstDigit in 0..<indexOfFirstNr) {
        input[indexOfFirstDigit]
    } else {
        nineWholeNumbers.filter { input.contains(it) }
            .minByOrNull { nr -> input.indexOf(nr) }!!
            .stringToDigit()
    }.toString()
}

fun lastNumber(input: String): String {

    val indexOfLastDigit = input.indexOfLast { it.isDigit() }
    val indexOfLastNr = nineWholeNumbers.filter{ input.contains(it) }.maxOfOrNull {input.lastIndexOf(it)}
    return if (indexOfLastNr == null || indexOfLastDigit > indexOfLastNr) {
        input[indexOfLastDigit]
    } else {
        nineWholeNumbers.filter { input.contains(it) }
            .maxByOrNull { nr -> input.lastIndexOf(nr) }!!
            .stringToDigit()
    }.toString()
}

private val nineWholeNumbers: List<String> = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
private fun String.stringToDigit() = nineWholeNumbers.indexOf(this) + 1