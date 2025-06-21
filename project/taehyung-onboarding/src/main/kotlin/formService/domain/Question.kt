package formService.domain

class Question(
    var id: Long? = null,
    var name: String,
    var description: String,
    var inputType: InputType,
    var required: Boolean,
    options: List<QuestionOption>? = null,
) {
    var isRemoved: Boolean

    private val mutableOptions = options?.toMutableList()
    val options get() = mutableOptions?.toList()

    constructor(id: Long?, name: String, description: String, inputType: InputType, required: Boolean, isRemoved: Boolean) : this(
        id,
        name,
        description,
        inputType,
        required,
    ) {
        this.isRemoved = isRemoved
    }
    constructor(
        id: Long?,
        name: String,
        description: String,
        inputType: InputType,
        required: Boolean,
        options: List<QuestionOption>?,
        isRemoved: Boolean,
    ) : this(id, name, description, inputType, required, options) {
        this.isRemoved = isRemoved
    }

    init {
        isRemoved = false
    }

    fun modify(
        name: String,
        description: String,
        inputType: InputType,
        required: Boolean,
        isRemoved: Boolean,
    ) {
        this.name = name
        this.description = description
        this.inputType = inputType
        this.required = required
        this.isRemoved = isRemoved
    }

    fun checkRequired() {
    }

    override fun toString(): String =
        """
        Question(
            id=$id, 
            name='$name', 
            description='$description', 
            inputType=$inputType, 
            required=$required, 
            isRemoved=$isRemoved, 
            options=$options
        )
        """.trimIndent()
}
