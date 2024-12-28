package fyi.manpreet.portfolio

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm Auto"
}

actual fun getPlatform(): Platform = WasmPlatform()