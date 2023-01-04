package non.mametich.newsapp.utils

sealed class Resourses <T> (
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?): Resourses<T>(data = data)
    class Error<T>(message: String?): Resourses<T>(message = message)
    class Loading<T>(): Resourses<T>()
}