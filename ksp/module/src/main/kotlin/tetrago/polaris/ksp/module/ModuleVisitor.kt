package tetrago.polaris.ksp.module

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo
import tetrago.polaris.module.ModuleProvider

class ModuleVisitor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator
) : KSVisitorVoid() {
    companion object {
        const val PACKAGE_NAME = "tetrago.polaris.ksp.generated"
        const val CLASS_NAME = "Module"
    }

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        if(classDeclaration.classKind != ClassKind.CLASS) {
            logger.error("Only annotate classes as registries")
            return
        }

        val file = FileSpec.builder(PACKAGE_NAME, CLASS_NAME)
            .addImport(classDeclaration.packageName.asString(), classDeclaration.simpleName.asString())
            .addType(TypeSpec.classBuilder(ClassName(PACKAGE_NAME, CLASS_NAME))
                .addType(TypeSpec.companionObjectBuilder()
                    .addFunction(FunSpec.builder("get")
                        .addAnnotation(JvmStatic::class)
                        .returns(ModuleProvider::class)
                        .addStatement("return ${classDeclaration.simpleName.asString()}()")
                        .build())
                    .build())
                .build())
            .build()

        file.writeTo(codeGenerator, Dependencies(true))
    }
}
