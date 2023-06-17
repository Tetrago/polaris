package tetrago.polaris.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo
import tetrago.polaris.module.ModuleProvider
import tetrago.polaris.module.PolarisModule

class Processor(private val codeGenerator: CodeGenerator) : SymbolProcessor {
    private fun findModuleClass(resolver: Resolver): KSClassDeclaration? {
        val list = resolver.getSymbolsWithAnnotation(PolarisModule::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()

        return list.singleOrNull()?.run {
            if(superTypes.any {
                    it.resolve().declaration.qualifiedName?.asString() == ModuleProvider::class.qualifiedName!! }) {
                this
            } else {
                null
            }
        }
    }

    private fun buildFile(target: KSClassDeclaration): FileSpec {
        val packageName = "tetrago.polaris.ksp.generated"
        val className = "Module"

        return FileSpec.builder(packageName, className)
            .addImport(target.packageName.asString(), target.simpleName.asString())
            .addType(TypeSpec.classBuilder(ClassName(packageName, className))
                .addType(TypeSpec.companionObjectBuilder()
                    .addFunction(FunSpec.builder("get")
                        .addAnnotation(JvmStatic::class)
                        .returns(ModuleProvider::class)
                        .addStatement("return ${target.simpleName.asString()}()")
                        .build())
                    .build())
                .build())
            .build()
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        println("Processing")

        findModuleClass(resolver)?.let {
            println("Resolving class `${it.qualifiedName!!.asString()}`")
            buildFile(it).writeTo(codeGenerator, Dependencies(false, it.containingFile!!))
        }

        return emptyList()
    }
}

class ProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return Processor(environment.codeGenerator)
    }
}
