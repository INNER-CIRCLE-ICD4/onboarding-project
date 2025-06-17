package formService.domain

class QuestionOption(
    var id: Long? = null,
    var value: String,
) {
    fun modify(value: String) {
        this.value = value
    }

    override fun toString(): String = "QuestionOption(id=$id, value='$value')"
}
