package fyi.manpreet.portfolio

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform