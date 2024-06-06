data class Test(
    var test: String,
    var firstAnswer: String,
    var secondAnswer: String,
    var thirdAnswer: String,
    var rightAnswer: String
) {
    override fun toString(): String {
        return "$test \n        1)$firstAnswer    2)$secondAnswer    3)$thirdAnswer."
    }
}
