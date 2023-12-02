fun sumCalibrationValues1(input: String) =
    input.lines()
        .map { line -> ("${line.first { it.isDigit() }}${line.last { it.isDigit() }}") }
        .sumOf { it.toInt() }

fun sumCalibrationValues2(input: String) = input.lines()
    .map { (firstNumber(it) + lastNumber(it)) }
    .sumOf { it.toInt() }

fun firstNumber(input: String): String {
    val indexOfFirstDigit = input.indexOfFirst { it.isDigit() }
    val (indexOfFirstNr, firstNr) = input.findAnyOf(nineWholeNumbers)?: (Int.MAX_VALUE to "")

    return if (indexOfFirstDigit in 0..<indexOfFirstNr) {
        input[indexOfFirstDigit]
    } else {
        firstNr.stringToDigit()
    }.toString()
}

fun lastNumber(input: String): String {
    val indexOfLastDigit = input.indexOfLast { it.isDigit() }
    val (indexOfLastNr, lastNr) = input.findLastAnyOf(nineWholeNumbers)?: (-1 to "")

    return if (indexOfLastDigit > indexOfLastNr) {
        input[indexOfLastDigit]
    } else {
        lastNr.stringToDigit()
    }.toString()
}

private val nineWholeNumbers: List<String> = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
private fun String.stringToDigit() = nineWholeNumbers.indexOf(this) + 1