package fc.innercircle.jinhoonboarding.common.dto

data class CommonResponse<T> (
    val message: String,
    val code: String = "SUCCESS",
    val data: T? = null
)

