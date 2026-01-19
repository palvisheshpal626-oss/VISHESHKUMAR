package com.visheshkumar.app.data.model

/**
 * Supported languages in Piston API with their configurations
 */
enum class PistonLanguage(
    val languageId: String,
    val pistonName: String,
    val version: String,
    val extension: String,
    val supportsRealExecution: Boolean = true
) {
    KOTLIN("kotlin", "kotlin", "1.8.20", "kt"),
    JAVA("java", "java", "15.0.2", "java"),
    PYTHON("python", "python", "3.10.0", "py"),
    JAVASCRIPT("javascript", "javascript", "18.15.0", "js"),
    TYPESCRIPT("typescript", "typescript", "5.0.3", "ts"),
    CPP("cpp", "c++", "10.2.0", "cpp"),
    C("c", "c", "10.2.0", "c"),
    CSHARP("csharp", "csharp", "6.12.0", "cs"),
    SWIFT("swift", "swift", "5.3.3", "swift"),
    GO("go", "go", "1.16.2", "go"),
    RUST("rust", "rust", "1.68.2", "rs"),
    PHP("php", "php", "8.2.3", "php"),
    RUBY("ruby", "ruby", "3.0.1", "rb"),
    DART("dart", "dart", "2.19.6", "dart"),
    R("r", "r", "4.1.1", "r"),
    SCALA("scala", "scala", "3.2.2", "scala"),
    SQL("sql", "sql", "3.36.0", "sql", false), // No real execution for SQL
    HTML("html", "html", "1.0.0", "html", false), // No real execution for HTML
    CSS("css", "css", "1.0.0", "css", false), // No real execution for CSS
    BASH("bash", "bash", "5.2.0", "sh");
    
    companion object {
        /**
         * Find PistonLanguage by language ID
         */
        fun fromLanguageId(id: String): PistonLanguage? {
            return values().find { it.languageId == id.lowercase() }
        }
        
        /**
         * Check if a language supports real execution
         */
        fun supportsExecution(languageId: String): Boolean {
            return fromLanguageId(languageId)?.supportsRealExecution ?: false
        }
    }
}
